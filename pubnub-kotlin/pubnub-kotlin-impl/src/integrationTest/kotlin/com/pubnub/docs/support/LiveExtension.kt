package com.pubnub.docs.support

import com.pubnub.docs.support.tools.Groups
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import org.junit.jupiter.api.extension.TestWatcher

/**
 * JUnit 5 extension of every live docs test class: `@ExtendWith(LiveExtension::class)`.
 *
 * - Before the first class of a run (once per JVM): print which roles are set or missing, run the janitor, wait until the
 *   Access Manager keysets refuse a client without a token again, run the preflight. `LIVE_JANITOR=off` and
 *   `LIVE_PREFLIGHT=off` skip the last two for single page runs.
 * - Before each class: require the roles of the class's group (see groups.json). A missing role fails with the env names.
 * - Test parameters of type [LiveClients] and [Recorder] are resolved, one per test.
 * - After each test: registered cleanups newest first, then every tracked client is unsubscribed, its listeners removed
 *   and destroyed.
 * - After the last class (when the root context closes): the janitor again, then a check for leftover non-daemon threads.
 */
class LiveExtension : BeforeAllCallback, AfterEachCallback, AfterAllCallback, ParameterResolver, TestWatcher {
    private val namespace = ExtensionContext.Namespace.create(LiveExtension::class.java)

    /** One per JVM. Its close() runs when the whole run ends. */
    private class Run(private val roles: List<String>) : ExtensionContext.Store.CloseableResource {
        init {
            Env.printStatus()
            if (Env.switchOn("LIVE_JANITOR")) {
                check(Janitor.runAndPrint(roles)) { "the janitor failed before the run, see the janitor lines above" }
            }
            roles.filter { Env.hasSecret(it) && Env.missing(it).isEmpty() }.forEach { role ->
                val seconds = Janitor.verifyApplicationGrantClosed(role)
                println("live roles: application level grant of $role is closed (${seconds}s)")
            }
            if (Env.switchOn("LIVE_PREFLIGHT")) {
                check(Preflight.runAndPrint(roles)) { "the preflight found missing keyset features, see the preflight lines above" }
            }
        }

        override fun close() {
            var clean = true
            if (Env.switchOn("LIVE_JANITOR")) {
                clean = Janitor.run(roles).also { Janitor.formatReport(it).forEach { line -> println(line) } }.critical.isEmpty()
            }
            val leftovers = LeakCheck.nonDaemonThreadsAfterWinddown()
            if (leftovers.isNotEmpty()) {
                // Printed as well, because the build log shows only the exception class of an extension failure.
                println("live roles: non-daemon threads still alive after the run: ${Redact.scrub(leftovers.joinToString(", "))}")
            }
            check(clean) { "the janitor could not revoke the application level grant after the run" }
            check(leftovers.isEmpty()) { "non-daemon threads are still alive after the run: ${leftovers.joinToString(", ")}" }
        }
    }

    private fun rolesOfRun(): List<String> {
        val group = Env.group()
        if (group != null) {
            return Groups.load().firstOrNull { it.name == group }?.roles ?: error("LIVE_GROUP '$group' is not a group in groups.json")
        }
        return Env.keysetRoles.filter { Env.missing(it).isEmpty() }
    }

    override fun beforeAll(context: ExtensionContext) {
        Env.load()
        val needed = Groups.groupOfClass(context.requiredTestClass.name)?.roles ?: listOf("open")
        for (role in needed) {
            val gone = Env.missing(role)
            val names = gone.joinToString(", ")
            check(gone.isEmpty()) {
                "role '$role' is missing env: $names. Set them in the environment or in the env file (see .env.example)."
            }
        }
        context.root.getStore(namespace).getOrComputeIfAbsent("run", { Run(rolesOfRun()) }, Run::class.java)
    }

    private fun testStore(context: ExtensionContext) = context.getStore(namespace)

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Boolean = parameterContext.parameter.type == LiveClients::class.java || parameterContext.parameter.type == Recorder::class.java

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext,
    ): Any {
        val store = testStore(extensionContext)
        if (parameterContext.parameter.type == LiveClients::class.java) {
            return store.getOrComputeIfAbsent("clients", { LiveClients() }, LiveClients::class.java).also { LiveClients.current = it }
        }
        return store.getOrComputeIfAbsent("recorder", { Recorder() }, Recorder::class.java)
    }

    override fun afterEach(context: ExtensionContext) {
        val clients = testStore(context).get("clients", LiveClients::class.java)
        try {
            // Leave first: the cleanups delete channel groups, and a leave through a deleted group does not clear presence.
            clients?.leaveAll()
            Cleanup.runCleanups()
        } finally {
            clients?.stopAll()
            LiveClients.current = null
        }
    }

    override fun afterAll(context: ExtensionContext) {
        // The run itself closes when the root context closes. Nothing per class.
    }

    /**
     * Prints why a test failed, so the CI log shows more than the exception class. Gradle keeps its short exception
     * format (it would print messages unredacted), so this is the only place a failure message reaches the log: every
     * message of the cause chain goes through [Redact] (registered key and token values, key and token shapes, auth and
     * signature query values) and is capped in length.
     */
    override fun testFailed(
        context: ExtensionContext,
        cause: Throwable?,
    ) {
        var current = cause
        var depth = 0
        while (current != null && depth < MAX_CAUSES) {
            val message = Redact.scrub(current.message.orEmpty()).take(MAX_MESSAGE_CHARS)
            val label =
                if (depth == 0) {
                    "failed"
                } else {
                    "caused by"
                }
            println("live failure: ${context.displayName} $label ${current.javaClass.name}: $message")
            current = current.cause.takeIf { it !== current }
            depth++
        }
    }

    private companion object {
        const val MAX_CAUSES = 5
        const val MAX_MESSAGE_CHARS = 2000
    }
}

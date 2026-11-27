package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import java.util.Collections
import java.util.IdentityHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Clients of one test, per role and user name. Every client is tracked and stopped after the test: it leaves its channels and
 * groups first (see [leaveAll]), then its listeners are removed and it is destroyed. The Java client
 * (`com.pubnub.api.java.PubNub`) is created by reflection, because the Java API is not on the classpath of the Kotlin module.
 * Java tests get it back as their own type, see [javaClient].
 *
 * Every client built here has the shortest presence timeout of the SDK (20 seconds, see [LiveKeys.shortPresence]), so that a
 * leave that never reached the service costs the next test seconds, not the default 300. A `configure` block can change it.
 */
class LiveClients : AutoCloseable {
    private val tracked = CopyOnWriteArrayList<Any>()
    private val left: MutableSet<Any> = Collections.newSetFromMap(IdentityHashMap())

    /** A Kotlin client for [role] with the literal user id [user]. The role's secret key is set when it has one. */
    @JvmOverloads
    fun kotlinClient(
        role: String,
        user: String,
        configure: Consumer<PNConfiguration.Builder>? = null,
    ): PubNub {
        val builder = PNConfiguration.builder(UserId(user), Env.subscribeKey(role))
        LiveKeys.apply(builder, role)
        LiveKeys.shortPresence(builder)
        configure?.accept(builder)
        return track(PubNub.create(builder.build()))
    }

    /** Kotlin lambda variant of [kotlinClient]. */
    fun kotlinClient(
        role: String,
        user: String,
        configure: PNConfiguration.Builder.() -> Unit,
    ): PubNub = kotlinClient(role, user, Consumer<PNConfiguration.Builder> { it.configure() })

    /** A Kotlin client that is allowed to use the secret key (the role must have one). */
    fun server(
        role: String,
        user: String = "docs-server",
    ): PubNub {
        check(Env.hasSecret(role)) { "role '$role' has no secret key" }
        return kotlinClient(role, user)
    }

    /**
     * A Java client (`com.pubnub.api.java.PubNub`) for [role]. [configure] receives the Java builder
     * (`com.pubnub.api.java.v2.PNConfiguration.Builder`) typed as [Any].
     */
    @JvmOverloads
    fun <T : Any> javaClient(
        role: String,
        user: String,
        configure: Consumer<Any>? = null,
    ): T {
        val configType = Class.forName("com.pubnub.api.java.v2.PNConfiguration")
        val builder =
            configType.getMethod("builder", UserId::class.java, String::class.java)
                .invoke(null, UserId(user), Env.subscribeKey(role))
        LiveKeys.apply(builder, role)
        LiveKeys.shortPresence(builder)
        configure?.accept(builder)
        val config = Class.forName("com.pubnub.api.java.v2.PNConfiguration\$Builder").getMethod("build").invoke(builder)
        val create = Class.forName("com.pubnub.api.java.PubNub").getMethod("create", configType)
        @Suppress("UNCHECKED_CAST")
        return track(create.invoke(null, config) as T)
    }

    /** Registers a client a sample created itself, so that it is stopped after the test. Returns it. */
    fun <T : Any> track(client: T): T {
        tracked.add(client)
        return client
    }

    /**
     * Leaves every channel and group each tracked client is still subscribed to, and returns when the service no longer lists
     * the client's user there, so that a presence occupancy never leaks into the next test. Call it BEFORE the cleanups of the
     * test: a leave for a channel group that was deleted already cannot clear the occupancy of the channels in it.
     *
     * The SDK has no public leave call that can be awaited (`unsubscribeAll` and `presence(connected = false)` send the leave
     * in the background), so the leave is confirmed with a `hereNow` of a watcher client. Never throws.
     */
    fun leaveAll() {
        for (client in tracked.toList().asReversed()) {
            if (!left.add(client)) {
                continue
            }
            try {
                leave(client)
            } catch (error: Exception) {
                println(Redact.scrub("client leave failed: ${error.javaClass.simpleName}: ${error.cause?.message ?: error.message}"))
            }
        }
    }

    /** Leaves (see [leaveAll]), then removes the listeners and destroys every client, newest first. Never throws. */
    fun stopAll() {
        leaveAll()
        while (tracked.isNotEmpty()) {
            val client = tracked.removeAt(tracked.size - 1)
            left.remove(client)
            invoke(client, "removeAllListeners")
            invoke(client, "forceDestroy")
        }
    }

    override fun close() = stopAll()

    private fun invoke(
        target: Any,
        name: String,
    ): Any? =
        try {
            target.javaClass.methods.firstOrNull { it.name == name && it.parameterCount == 0 }?.invoke(target)
        } catch (error: Exception) {
            println(Redact.scrub("client step $name failed: ${error.javaClass.simpleName}: ${error.cause?.message ?: error.message}"))
            null
        }

    private fun names(
        client: Any,
        getter: String,
    ): List<String> =
        (invoke(client, getter) as? Collection<*>).orEmpty().map { it.toString() }.filterNot { it.endsWith("-pnpres") }.distinct()

    private fun leave(client: Any) {
        val channels = names(client, "getSubscribedChannels")
        val groups = names(client, "getSubscribedChannelGroups")
        invoke(client, "removeAllListeners")
        if (channels.isEmpty() && groups.isEmpty()) {
            return
        }
        val configuration = invoke(client, "getConfiguration") ?: return
        val user = invoke(invoke(configuration, "getUserId") ?: return, "getValue") as? String ?: return
        val suppressed = invoke(configuration, "getSuppressLeaveEvents") as? Boolean ?: false
        val subscribeKey = invoke(configuration, "getSubscribeKey") as? String
        invoke(client, "unsubscribeAll")
        if (suppressed) {
            return
        }
        val role = Env.keysetRoles.firstOrNull { Env.has(it) && Env.subscribeKey(it) == subscribeKey } ?: return
        val builder = PNConfiguration.builder(UserId("docs-leave-watch"), Env.subscribeKey(role))
        LiveKeys.apply(builder, role)
        val watcher = PubNub.create(builder.build())
        try {
            awaitGone(watcher, user, channels, groups)
        } finally {
            watcher.forceDestroy()
        }
    }

    private fun awaitGone(
        watcher: PubNub,
        user: String,
        channels: List<String>,
        groups: List<String>,
    ) {
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(LEAVE_WAIT_MS)
        while (true) {
            val present =
                try {
                    val here = watcher.hereNow(channels = channels, channelGroups = groups, includeUUIDs = true).sync()
                    here.channels.values.filter { data -> data.occupants.any { it.uuid == user } }.joinToString { it.channelName }
                } catch (error: Exception) {
                    "unknown (${error.javaClass.simpleName})"
                }
            if (present.isEmpty()) {
                return
            }
            if (System.nanoTime() >= deadline) {
                println(
                    Redact.scrub(
                        "client leave: $user is still present on [$present] after ${LEAVE_WAIT_MS / 1000}s, " +
                            "the presence timeout removes it",
                    ),
                )
                return
            }
            Thread.sleep(LEAVE_POLL_MS)
        }
    }

    companion object {
        private const val LEAVE_WAIT_MS = 15_000L
        private const val LEAVE_POLL_MS = 500L

        /** The clients of the running test (tests of a class run one after the other). Set by [LiveExtension]. */
        @Volatile
        internal var current: LiveClients? = null

        /**
         * For the hidden line of a "Main" sample that creates its client itself and never destroys it (the docs text stays as it
         * is): registers [client] with the running test so that it leaves and is destroyed after the test.
         */
        @JvmStatic
        fun trackCurrent(client: Any) {
            checkNotNull(current) { "no live test is running" }.track(client)
        }
    }
}

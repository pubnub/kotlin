package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException

/**
 * Cleanup registry. [cleanupAfterTest] runs a function after the test body, newest first, before the test's clients
 * stop. A 404 is silent, any other failure is printed (redacted) and never hides the test result.
 *
 * [record] registers the removal of one resource of the open keyset that the test created. Kinds: `channel_group`,
 * `history`, `uuid_metadata`, `channel_metadata`.
 */
object Cleanup {
    private val pending = ArrayDeque<Pair<String, () -> Unit>>()
    private var helper: LiveClients? = null
    private var helperClient: PubNub? = null

    @Synchronized
    fun cleanupAfterTest(
        label: String,
        fn: () -> Unit,
    ) {
        pending.addLast(label to fn)
    }

    /** Java friendly twin. */
    @JvmStatic
    fun cleanupAfterTest(
        label: String,
        task: Runnable,
    ) = cleanupAfterTest(label) { task.run() }

    private fun is404(error: Throwable): Boolean {
        return (error is PubNubException && error.statusCode == 404) || error.message?.contains("404") == true
    }

    /** Runs and clears every registered cleanup. Never throws. */
    @JvmStatic
    fun runCleanups() {
        try {
            while (true) {
                val next = synchronized(this) { pending.removeLastOrNull() } ?: break
                try {
                    next.second()
                } catch (error: Exception) {
                    if (!is404(error)) {
                        println(Redact.scrub("cleanup '${next.first}' failed: ${error.javaClass.simpleName}: ${error.message}"))
                    }
                }
            }
        } finally {
            stopHelper()
        }
    }

    @Synchronized
    private fun helper(): PubNub {
        helperClient?.let { return it }
        val clients = LiveClients()
        helper = clients
        return clients.kotlinClient("open", "cleanup").also { helperClient = it }
    }

    @Synchronized
    @JvmStatic
    fun stopHelper() {
        helper?.stopAll()
        helper = null
        helperClient = null
    }

    /** Registers the removal of a resource of the open keyset that the test created. */
    @JvmStatic
    fun record(
        kind: String,
        name: String,
    ) {
        when (kind) {
            "channel_group" -> cleanupAfterTest("delete channel group $name") { helper().deleteChannelGroup(name).sync() }
            "history" -> cleanupAfterTest("delete history of $name") { helper().deleteMessages(listOf(name)).sync() }
            "uuid_metadata" -> cleanupAfterTest("remove user metadata $name") { helper().removeUUIDMetadata(name).sync() }
            "channel_metadata" -> cleanupAfterTest("remove channel metadata $name") { helper().removeChannelMetadata(name).sync() }
            else -> throw IllegalArgumentException("unknown cleanup kind '$kind'")
        }
    }
}

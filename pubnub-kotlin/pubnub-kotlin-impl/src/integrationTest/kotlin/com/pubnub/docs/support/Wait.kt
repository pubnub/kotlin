package com.pubnub.docs.support

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Waits. [eventually] retries until one pass succeeds, [settled] until several consecutive passes succeed (a read after
 * a write can hit a lagging replica). [sleepToProve] is only for proving that something does NOT happen.
 *
 * The retry loop is written out instead of using Awaitility, so every timeout message can say what was last seen. A
 * pass means "the function did not throw an AssertionError". Any other exception ends the wait at once.
 */
object Wait {
    const val PRESENCE_TIMEOUT_MS = 45_000L
    const val PRESENCE_EVENT_TIMEOUT_MS = 30_000L

    data class Policy(val timeoutMs: Long = 15_000, val intervalMs: Long = 500, val passes: Int = 1)

    val EVENTUALLY = Policy()
    val SETTLED = Policy(timeoutMs = 15_000, intervalMs = 750, passes = 3)

    private fun <T> poll(
        policy: Policy,
        what: String,
        fn: () -> T,
    ): T {
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(policy.timeoutMs)
        var last: AssertionError? = null
        var good = 0
        while (true) {
            try {
                val result = fn()
                good++
                if (good >= policy.passes) {
                    return result
                }
            } catch (e: AssertionError) {
                last = e
                good = 0
            }
            if (System.nanoTime() >= deadline) {
                val seen = Redact.scrub(last?.message ?: "no failure message")
                throw AssertionError(
                    "$what not met within ${policy.timeoutMs / 1000.0}s (${policy.passes} consecutive passes). Last failure: $seen",
                    last,
                )
            }
            Thread.sleep(policy.intervalMs)
        }
    }

    /** Retries [fn] on AssertionError until it passes once (15 s, 500 ms apart). */
    fun <T> eventually(
        what: String = "condition",
        timeoutMs: Long = EVENTUALLY.timeoutMs,
        intervalMs: Long = EVENTUALLY.intervalMs,
        fn: () -> T,
    ): T = poll(Policy(timeoutMs, intervalMs, 1), what, fn)

    /** Retries [fn] until it passes [passes] times in a row (default 3, 750 ms apart, 15 s). */
    fun <T> settled(
        what: String = "condition",
        timeoutMs: Long = SETTLED.timeoutMs,
        intervalMs: Long = SETTLED.intervalMs,
        passes: Int = SETTLED.passes,
        fn: () -> T,
    ): T = poll(Policy(timeoutMs, intervalMs, passes), what, fn)

    /** Java friendly twin of [eventually]. */
    @JvmStatic
    fun eventuallyRun(
        what: String,
        run: Runnable,
    ) = eventually(what) { run.run() }

    /** Java friendly twin of [settled]. */
    @JvmStatic
    fun settledRun(
        what: String,
        run: Runnable,
    ) = settled(what) { run.run() }

    /** Only to prove absence (a fired message is not delivered). Never to wait for a result. */
    @JvmStatic
    fun sleepToProve(millis: Long) {
        Thread.sleep(millis)
    }

    /** Records every status and event of one client while a wait runs, so that a timeout can say what was seen. */
    class Observer : EventListener, StatusListener {
        val statuses = CopyOnWriteArrayList<PNStatus>()
        val events = CopyOnWriteArrayList<Pair<String, Any>>()
        private val watchers = CopyOnWriteArrayList<() -> Unit>()

        internal fun onChange(block: () -> Unit) {
            watchers.add(block)
        }

        private fun changed() = watchers.forEach { it() }

        override fun status(
            pubnub: PubNub,
            status: PNStatus,
        ) {
            statuses.add(status)
            changed()
        }

        override fun message(
            pubnub: PubNub,
            result: PNMessageResult,
        ) = record("message", result)

        override fun signal(
            pubnub: PubNub,
            result: PNSignalResult,
        ) = record("signal", result)

        override fun presence(
            pubnub: PubNub,
            result: PNPresenceEventResult,
        ) = record("presence", result)

        override fun messageAction(
            pubnub: PubNub,
            result: PNMessageActionResult,
        ) = record("messageAction", result)

        override fun objects(
            pubnub: PubNub,
            result: PNObjectEventResult,
        ) = record("objects", result)

        override fun file(
            pubnub: PubNub,
            result: PNFileEventResult,
        ) = record("file", result)

        private fun record(
            kind: String,
            result: Any,
        ) {
            events.add(kind to result)
            changed()
        }

        fun describe(): String {
            val categories = statuses.joinToString(", ") { it.category.name }.ifEmpty { "none" }
            val kinds =
                events.groupingBy { it.first }.eachCount().entries.joinToString(", ") { "${it.key} x${it.value}" }
                    .ifEmpty { "none" }
            return "statuses seen: $categories. events seen: $kinds"
        }
    }

    /** A wait armed before the action. Register it first, act, then call [await]. */
    class Expectation internal constructor(
        private val pubnub: PubNub,
        private val observer: Observer,
        private val what: String,
        private val test: (Observer) -> Any?,
    ) : AutoCloseable {
        private val latch = CountDownLatch(1)

        @Volatile
        private var result: Any? = null

        init {
            observer.onChange { check() }
        }

        private fun check() {
            if (latch.count > 0) {
                val found = test(observer)
                if (found != null) {
                    result = found
                    latch.countDown()
                }
            }
        }

        /** Returns the first matching event (or status), or fails with what was seen. */
        @JvmOverloads
        fun await(timeoutMs: Long = 15_000): Any {
            check()
            try {
                if (!latch.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                    throw AssertionError(Redact.scrub("$what did not arrive within ${timeoutMs / 1000.0}s. ${observer.describe()}"))
                }
                return result!!
            } finally {
                close()
            }
        }

        override fun close() {
            pubnub.removeListener(observer)
        }
    }

    private fun observe(pubnub: PubNub): Observer {
        val observer = Observer()
        pubnub.addListener(observer as EventListener)
        pubnub.addListener(observer as StatusListener)
        return observer
    }

    /** Arms a wait for the first event of [kind] (message, signal, presence, messageAction, objects, file) that matches. */
    @JvmOverloads
    fun expectEvent(
        pubnub: PubNub,
        kind: String,
        predicate: (Any) -> Boolean = { true },
    ): Expectation =
        Expectation(pubnub, observe(pubnub), "$kind event") { observer ->
            observer.events.firstOrNull { it.first == kind && predicate(it.second) }?.second
        }

    /** Arms a wait for a connected status that covers every channel and group. Register before subscribing. */
    @JvmOverloads
    fun connected(
        pubnub: PubNub,
        channels: Collection<String> = emptyList(),
        channelGroups: Collection<String> = emptyList(),
    ): Expectation =
        Expectation(pubnub, observe(pubnub), "connected status for $channels $channelGroups") { observer ->
            val ok =
                observer.statuses.filter {
                    it.category == PNStatusCategory.PNConnectedCategory || it.category == PNStatusCategory.PNSubscriptionChanged
                }
            val seenChannels = ok.flatMap { it.affectedChannels }.toSet()
            val seenGroups = ok.flatMap { it.affectedChannelGroups }.toSet()
            if (ok.isNotEmpty() && seenChannels.containsAll(channels) && seenGroups.containsAll(channelGroups)) {
                ok.last()
            } else {
                null
            }
        }
}

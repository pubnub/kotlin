@file:Suppress("ktlint")

package com.pubnub.docs.messagePersistence

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.docs.support.Kit.assertTimetoken
import com.pubnub.docs.support.Kit.probe
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.MessagingKit.publishStored
import com.pubnub.docs.support.MessagingKit.resetHistory
import com.pubnub.docs.support.Recorder
import com.pubnub.docs.support.Stdout
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

/**
 * Live tests of the Kotlin docs page storage-and-playback (docs/sdks/kotlin/api-reference/storage-and-playback.md). One test
 * per snippet. The text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the
 * assertions below it. Every test starts from an empty history of its channels, so an exact read is exact.
 */
@ExtendWith(LiveExtension::class)
class StorageAndPlaybackDocsTest {
    /** Publishes [count] stored messages `<label>-1` ... to [channel] and returns the timetokens in order. */
    private fun seed(client: PubNub, channel: String, label: String, count: Int): List<Long> =
        (1..count).map { publishStored(client, channel, "$label-$it").timetoken }

    private fun storedTimetokens(client: PubNub, channel: String): List<Long?> =
        client.fetchMessages(listOf(channel)).sync().channels[channel].orEmpty().map { it.timetoken }

    // ---------------------------------------------------------------------------------------------------------------
    // Delete messages
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun deleteMessagesBasic(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "channel_1", "channel_2")
        val first = seed(reader, "channel_1", "keep", 1)
        val second = seed(reader, "channel_2", "keep", 1)
        val out = Stdout.captureForTest()

        // snippet.deleteMessagesBasic
        pubnub.deleteMessages(
            channels = listOf("channel_1", "channel_2"),
            start = 1460693607379L,
            end = 1460893617271L
        ).async { result ->
            // The deleteMessages() method does not return actionable data.
            // Be sure to check the status on the outcome of the operation

            if (result.isSuccess) {
                println("Successfully deleted messages.")
            } else {
                println("Error deleting messages: ${result.exceptionOrNull()?.message}")
            }
        }
        // snippet.end

        recorder.awaitOk("deleteMessages")
        Wait.eventually("the sample printed the success line") { out.assertPrinted("Successfully deleted messages.") }
        // the range of the sample lies in 1970, so it deletes nothing of what was published now
        Wait.sleepToProve(1500)
        assertEquals(first, storedTimetokens(reader, "channel_1"))
        assertEquals(second, storedTimetokens(reader, "channel_2"))
    }

    @Test
    fun deleteSpecificMessageFromHistory(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "channel_1")
        val published = seed(reader, "channel_1", "message", 2)
        val out = Stdout.captureForTest()

        // snippet.deleteSpecificMessageFromHistory
        pubnub.deleteMessages(
            channels = listOf("channel_1"),
            start = 15526611838554309L,
            end = 15526611838554310L
        ).async { result ->
            // The deleteMessages() method does not return actionable data.
            // Be sure to check the status on the outcome of the operation

            if (result.isSuccess) {
                println("Successfully deleted messages.")
            } else {
                println("Error deleting messages: ${result.exceptionOrNull()?.message}")
            }
        }
        // snippet.end

        recorder.awaitOk("deleteMessages")
        Wait.eventually("the sample printed the success line") { out.assertPrinted("Successfully deleted messages.") }
        // the window of the sample (start exclusive, end inclusive) holds no message of this run
        Wait.sleepToProve(1500)
        assertEquals(published, storedTimetokens(reader, "channel_1"))
        // the same call with the window T-1 to T removes exactly the message T, as the page says
        reader.deleteMessages(listOf("channel_1"), start = published[1] - 1, end = published[1]).sync()
        Wait.settled("only the message outside the window is left") {
            assertEquals(listOf(published[0]), storedTimetokens(reader, "channel_1"))
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Fetch messages (Main samples)
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun fetchMessages(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "demo-channel")
        val out = Stdout.captureForTest()

        com.pubnub.docs.messagePersistence.samples.fetchMessages.main()

        out.assertPrinted("SUCCESS: Retrieved message history")
        out.assertPrinted("SUCCESS: Retrieved message history with options")
        assertEquals(2, out.lines("Found 6 messages for channel: demo-channel").size)
        assertTrue(out.lines("ERROR").isEmpty(), "the sample reported an error")
        for (number in 1..5) {
            // the sample prints the first five of the list, which is ordered oldest first
            assertEquals(2, out.lines("Message: \"Message 0$number\"").size, "Message 0$number is not printed twice")
        }
        // the sample deleted the history of the channel at its end
        Wait.settled("the sample removed the history of demo-channel") {
            assertTrue(reader.fetchMessages(listOf("demo-channel")).sync().channels["demo-channel"].orEmpty().isEmpty(), "the history is not empty")
        }
    }

    @Test
    fun fetchMessagesWithPaging(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "kotlin-ch1", "kotlin-ch2")
        val first = seed(reader, "kotlin-ch1", "paging", 2)
        val second = seed(reader, "kotlin-ch2", "paging", 2)
        val out = Stdout.captureForTest()

        com.pubnub.docs.messagePersistence.samples.fetchMessagesWithPaging.main()

        // The start of the sample is a fixed timetoken of May 2025, every message of this run is newer, so the sample finds
        // nothing to print. The messages are there: a read with a start in the future lists them.
        Wait.sleepToProve(3000)
        assertTrue(out.lines("Channel kotlin-ch").isEmpty(), "the sample printed messages that are newer than its start")
        val now = System.currentTimeMillis() * 10_000L
        val page = reader.fetchMessages(listOf("kotlin-ch1", "kotlin-ch2"), PNBoundedPage(limit = 25, start = now)).sync()
        assertEquals(first, page.channels["kotlin-ch1"].orEmpty().map { it.timetoken })
        assertEquals(second, page.channels["kotlin-ch2"].orEmpty().map { it.timetoken })
    }

    @Test
    fun historyPagingExampleDeprecated(clients: LiveClients) {
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "my_channel")
        val published = seed(reader, "my_channel", "page", 3)
        val out = Stdout.captureForTest()

        com.pubnub.docs.messagePersistence.samples.deprecatedHistory.main()

        // The sample starts at timetoken 0 and walks backwards, so it finds nothing older than that. The messages are there:
        // a history call without a start lists them.
        Wait.sleepToProve(3000)
        assertTrue(out.lines("page-").isEmpty(), "the sample printed messages that are newer than its start")
        val history = reader.history("my_channel", count = 10).sync()
        assertEquals(listOf("page-1", "page-2", "page-3"), history.messages.map { it.entry.asString })
    }

    // ---------------------------------------------------------------------------------------------------------------
    // History (deprecated)
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun historyRetrieveMessagesNewerThanGivenTimetoken(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "my_channel")
        val published = seed(reader, "my_channel", "newer", 3)

        // snippet.historyRetrieveMessagesNewerThanGivenTimetoken
        pubnub.history(
            channel = "my_channel", // where to fetch history from
            start = 13847168620721752L, // first timestamp
            reverse = true // should go in reverse?
        ).async { result ->
            result.onSuccess { res ->
                res.messages.forEach { message ->
                    message.entry // custom JSON structure for message
                }
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        val history = recorder.awaitOk("history") as PNHistoryResult
        assertEquals(listOf("newer-1", "newer-2", "newer-3"), history.messages.map { it.entry.asString })
    }

    @Test
    fun historyRetrieveMessagesUntilGivenTimetoken(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "my_channel")
        val published = seed(reader, "my_channel", "until", 3)

        // snippet.historyRetrieveMessagesUntilGivenTimetoken
        pubnub.history(
            channel = "my_channel", // where to fetch history from
            count = 100, // how many items to fetch
            start = -1, // first timestamp
            end = 13847168819178600L, // last timestamp
            reverse = true // should go in reverse?
        ).async { result ->
            result.onSuccess { res ->
                res.messages // ["Pub3","Pub4","Pub5"]
                res.startTimetoken // 13406746780720711
                res.endTimetoken // 13406746845892666
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end

        // Docs finding: the sample sends start=-1 and the service refuses it, so it never returns the messages that the page
        // promises (the end timetoken of the sample lies in 2013, nothing of this run is older anyway)
        val error = recorder.awaitFailure("history") as PubNubException
        assertEquals(400, error.statusCode)
        assertTrue(error.message!!.contains("Invalid Arguments"), "the service refused the call for another reason")
    }

    @Test
    fun historyThreeOldestMessages(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "my_channel")
        seed(reader, "my_channel", "oldest", 5)

        // snippet.historyThreeOldestMessages
        pubnub.history(
            channel = "my_channel", // where to fetch history from
            count = 3, // how many items to fetch
            reverse = true // should go in reverse?
        ).async { result ->
            result.onSuccess { res ->
                res.messages.forEach { message ->
                    message.entry // custom JSON structure for message
                }
            }
        }
        // snippet.end

        val history = recorder.awaitOk("history") as PNHistoryResult
        assertEquals(listOf("oldest-1", "oldest-2", "oldest-3"), history.messages.map { it.entry.asString })
    }

    @Test
    fun includeTimetoken(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "history_channel")
        val published = seed(reader, "history_channel", "stamped", 3)

        // snippet.includeTimetoken
        pubnub.history(
            channel = "history_channel", // where to fetch history from
            count = 10, // how many items to fetch
            includeTimetoken = true // include timetoken with each entry
        ).async { result ->
            result.onSuccess { value ->
                value!!.messages.forEach {
                    it.entry // custom JSON structure for message
                    it.timetoken // requested message timetoken
                }
            }
        }
        // snippet.end

        val history = recorder.awaitOk("history") as PNHistoryResult
        assertEquals(published, history.messages.map { it.timetoken })
        history.messages.forEach { assertTimetoken(it.timetoken) }
        assertEquals(listOf("stamped-1", "stamped-2", "stamped-3"), history.messages.map { it.entry.asString })
    }

    @Test
    fun retrieveLast100MessagesOnChannel(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "history_channel")
        seed(reader, "history_channel", "last", 3)

        // snippet.retrieveLast100MessagesOnChannel
        pubnub.history(
            channel = "history_channel", // where to fetch history from
            count = 100 // how many items to fetch
        ).async { result -> }
        // snippet.end

        val history = recorder.awaitOk("history") as PNHistoryResult
        assertEquals(listOf("last-1", "last-2", "last-3"), history.messages.map { it.entry.asString })
        assertTrue(history.messages.all { it.timetoken == null }, "a timetoken is listed although includeTimetoken is off")
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Message counts
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun messageCountsBasic(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "news")
        seed(reader, "news", "count", 3)
        val anHourAgo = (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)) * 10_000L
        Wait.settled("the service counts the three messages") {
            assertEquals(3L, reader.messageCounts(listOf("news"), listOf(anHourAgo)).sync().channels["news"])
        }

        // snippet.messageCountsBasic
        val lastHourTimetoken = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)

        pubnub.messageCounts(
            channels = listOf("news"),
            channelsTimetoken = listOf(lastHourTimetoken * 10_000L)
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end

        val counts = recorder.awaitOk("messageCounts") as PNMessageCountResult
        assertEquals(mapOf("news" to 3L), counts.channels)
    }

    @Test
    fun messageCountsDifferentTimetokenForEachChannel(clients: LiveClients, recorder: Recorder) {
        val pubnub = recorder.wrap(clients.kotlinClient(OPEN, "myUserId"))
        val reader = clients.kotlinClient(OPEN, "docs-receiver")
        resetHistory(reader, "news", "info")
        seed(reader, "news", "count", 2)
        seed(reader, "info", "count", 3)
        val anHourAgo = (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)) * 10_000L
        Wait.settled("the service counts the five messages") {
            val counts = reader.messageCounts(listOf("news", "info"), listOf(anHourAgo, anHourAgo)).sync().channels
            assertEquals(mapOf("news" to 2L, "info" to 3L), counts)
        }
        val out = Stdout.captureForTest()

        // snippet.messageCountsDifferentTimetokenForEachChannel
        val lastHourTimetoken = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)
        val lastDayTimetoken = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)

        pubnub.messageCounts(
            channels = listOf("news", "info"),
            channelsTimetoken = listOf(lastHourTimetoken, lastDayTimetoken).map { it * 10_000L }
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { pmMessageCountResult ->
                pmMessageCountResult.channels.forEach { (channel, count) ->
                    println("$count new messages on $channel")
                }
            }
        }
        // snippet.end

        val counts = recorder.awaitOk("messageCounts") as PNMessageCountResult
        assertEquals(mapOf("news" to 2L, "info" to 3L), counts.channels)
        Wait.eventually("the sample printed both counts") {
            out.assertPrinted("2 new messages on news")
            out.assertPrinted("3 new messages on info")
        }
    }

    private companion object {
        const val OPEN = "open"
    }
}

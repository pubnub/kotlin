package com.pubnub.api.integration

import com.pubnub.api.*
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.history.Action
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.suite.await
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

class MessageActionsIntegrationTest : BaseIntegrationTest() {

    lateinit var publishResult: PNPublishResult
    lateinit var expectedChannel: String

    override fun onBefore() {
        expectedChannel = randomValue()

        publishResult = pubnub.publish().apply {
            channel = expectedChannel
            message = generatePayload()
        }.sync()!!
    }

    @Test
    fun testAddMessageAction() {
        pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction =
                PNMessageAction(
                    type = "someother",
                    value = "smiley",
                    messageTimetoken = publishResult.timetoken
                )
        }.await { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNAddMessageAction, status.operation)

        }
    }

    @Test
    fun testGetMessageAction() {
        pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction =
                PNMessageAction(
                    type = randomValue(),
                    value = randomValue(),
                    messageTimetoken = publishResult.timetoken
                )
        }.sync()!!

        pubnub.getMessageActions().apply {
            channel = expectedChannel
        }.await { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNGetMessageActions, status.operation)
        }
    }

    @Test
    fun testDeleteMessageAction() {
        val expectedValue = UUID.randomUUID().toString()

        val addMessageActionResult = pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction = (
                    PNMessageAction(
                        type = "REACTION",
                        value = expectedValue,
                        messageTimetoken = publishResult.timetoken
                    ))
        }.sync()!!

        pubnub.removeMessageAction().apply {
            messageTimetoken = publishResult.timetoken
            actionTimetoken = addMessageActionResult.actionTimetoken
            channel = expectedChannel
        }.await { _, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNDeleteMessageAction, status.operation)
        }
    }

    @Test
    fun testAddGetMessageAction() {
        val expectedValue = UUID.randomUUID().toString()

        val addMessageActionResult = pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction = PNMessageAction(
                type = "REACTION",
                value = expectedValue,
                messageTimetoken = publishResult.timetoken
            )
        }.sync()

        pubnub.getMessageActions().apply {
            channel = expectedChannel

        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNGetMessageActions, status.operation)
            assertEquals(1, result!!.actions.size)
            assertEquals(result.actions[0].actionTimetoken, addMessageActionResult!!.actionTimetoken)
        }
    }

    @Test
    fun testAddGetMessageAction_Bulk() {
        val expectedMessageCount = 10
        val expectedChannel = randomValue()

        publishMixed(pubnub, expectedMessageCount, expectedChannel).forEach { pnPublishResult ->
            pubnub.addMessageAction().apply {
                channel = expectedChannel
                messageAction = PNMessageAction(
                    type = "reaction",
                    value = emoji(),
                    messageTimetoken = pnPublishResult.timetoken
                )
            }.sync()!!
        }

        pubnub.getMessageActions().apply {
            channel = expectedChannel
        }.await { result, status ->
            assertFalse(status.error)
            assertEquals(PNOperationType.PNGetMessageActions, status.operation)
            assertEquals(expectedMessageCount, result!!.actions.size)
        }
    }

    @Test
    @Throws(PubNubException::class)
    fun testAddGetMessageAction_Bulk_Pagination() {
        val expectedChannelName = randomValue()
        val messageCount = 10
        val messages = publishMixed(pubnub, messageCount, expectedChannelName)

        assertEquals(10, messages.size)

        messages.forEachIndexed { index, i ->
            pubnub.addMessageAction().apply {
                channel = expectedChannelName
                messageAction = PNMessageAction(
                    type = "reaction",
                    value = "${index + 1}_${emoji()}",
                    messageTimetoken = i.timetoken
                )
            }.sync()
        }

        val success = AtomicBoolean()
        val count = AtomicInteger()

        page(expectedChannelName, System.currentTimeMillis() * 10_000, object : Callback {
            override fun onMore(actions: List<PNMessageAction>) {
                count.set(count.get() + actions.size)
            }

            override fun onDone() {
                success.set(count.get() == messageCount)
            }
        })

        success.listen()
    }

    interface Callback {
        fun onMore(actions: List<PNMessageAction>)
        fun onDone()
    }

    fun page(channel: String, start: Long, callback: Callback) {
        pubnub.getMessageActions().apply {
            this.channel = channel
            this.start = start
            this.limit = 5

        }.async { result, status ->
            if (!status.error && !result!!.actions.isEmpty()) {
                callback.onMore(result.actions)
                page(channel, result.actions[0].actionTimetoken!!, callback)
            } else {
                callback.onDone()
            }
        }
    }

    @Test
    fun loopActions() {
        val publishList = mutableListOf<PNPublishResult>()

        val totalMessageCount = 10

        for (i in 1..totalMessageCount) {
            pubnub.publish().apply {
                channel = expectedChannel
                message = "${i}_${randomValue()}"
            }.sync()!!.run {
                publishList.add(this)
            }
        }

        assertEquals(totalMessageCount, publishList.size)

        publishList.forEachIndexed { i, it ->
            pubnub.addMessageAction().apply {
                channel = expectedChannel
                messageAction =
                    PNMessageAction(
                        type = "REACTION",
                        value = "${(i + 1)}_${randomValue(5)}",
                        messageTimetoken = it.timetoken
                    )
            }.sync()!!
        }

        val pnActionList = mutableListOf<PNMessageAction>()

        pageActions(3, expectedChannel, null, object : Callback {
            override fun onMore(actions: List<PNMessageAction>) {
                pnActionList.addAll(actions.reversed())
            }

            override fun onDone() {
                val tts = pnActionList.map { it.actionTimetoken!! }.toList()
                assertTrue(tts == tts.sorted().reversed())
            }
        })
    }

    fun pageActions(chunk: Int, channel: String, start: Long?, callback: Callback) {
        val builder: GetMessageActions = pubnub.getMessageActions().apply {
            this.limit = chunk
            this.channel = channel
        }
        if (start != null) {
            builder.start = start
        }
        val messageActionsResult = builder.sync()
        if (messageActionsResult!!.actions.isNotEmpty()) {
            callback.onMore(messageActionsResult.actions)
            pageActions(chunk, channel, messageActionsResult.actions[0].actionTimetoken, callback)
        } else {
            callback.onDone()
        }
    }

    @Test
    fun testFetchHistory() {
        val expectedChannel = randomValue()
        val expectedMessageCount = 10
        val publishResultList = publishMixed(pubnub, expectedMessageCount, expectedChannel)


        publishResultList.forEachIndexed { i, it ->
            if (i % 2 == 0 && i % 3 == 0) {
                pubnub.addMessageAction().apply {
                    channel = expectedChannel
                    messageAction = PNMessageAction(
                        type = "receipt",
                        value = emoji(),
                        messageTimetoken = publishResultList[i].timetoken
                    )
                }.sync()!!
            }
            if (i % 3 == 0) {
                pubnub.addMessageAction().apply {
                    channel = expectedChannel
                    messageAction = PNMessageAction(
                        type = "receipt",
                        value = emoji(),
                        messageTimetoken = publishResultList[i].timetoken
                    )
                }.sync()!!
            }
            if (i % 2 == 0) {
                pubnub.addMessageAction().apply {
                    channel = expectedChannel
                    messageAction = PNMessageAction(
                        type = "receipt",
                        value = emoji(),
                        messageTimetoken = publishResultList[i].timetoken
                    )
                }.sync()!!
            }
            if (i % 5 == 0) {
                pubnub.addMessageAction().apply {
                    channel = expectedChannel
                    messageAction = PNMessageAction(
                        type = "fiver",
                        value = emoji(),
                        messageTimetoken = publishResultList[i].timetoken
                    )
                }.sync()!!
            }
        }

        val fetchMessagesResultWithActions: PNFetchMessagesResult = pubnub.fetchMessages().apply {
            channels = Collections.singletonList(expectedChannel)
            includeMeta = true
            includeMessageActions = true
        }.sync()!!

        fetchMessagesResultWithActions.channels.forEach { (channel, item: List<PNFetchMessageItem>) ->
            println("Channel: " + channel + ". Messages: " + item.size)
            item.forEach(Consumer { pnFetchMessageItem: PNFetchMessageItem ->
                println("\tMessage: " + pnFetchMessageItem.message)
                println("\tTimetoken: " + pnFetchMessageItem.timetoken)
                println("\tMeta: " + pnFetchMessageItem.meta)
                if (pnFetchMessageItem.actions == null) {
                    println("\t\tNo actions here.")
                    return@Consumer
                }
                println("\t\tTotal action types: " + pnFetchMessageItem.actions!!.size)
                pnFetchMessageItem.actions!!.forEach { (type, map: HashMap<String, List<Action>>) ->
                    println("\t\t\tAction type: $type")
                    map.forEach { (value, actions: List<Action>) ->
                        println("\t\t\t\tAction value: $value")
                        actions.forEach(Consumer { action: Action ->
                            println("\t\t\t\tAction uuid: " + action.uuid)
                            println("\t\t\t\tAction timetoken: " + action.actionTimetoken)
                        })
                    }
                }
                println("--------------------")
            })
        }
        fetchMessagesResultWithActions.channels.forEach { (s: String?, pnFetchMessageItems: List<PNFetchMessageItem>) ->
            pnFetchMessageItems.forEach(
                Consumer { pnFetchMessageItem: PNFetchMessageItem ->
                    assertNotNull(pnFetchMessageItem.actions)
                    assertTrue(pnFetchMessageItem.actions!!.size >= 0)
                }
            )
        }
        val fetchMessagesResultNoActions: PNFetchMessagesResult = pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
        }.sync()!!
        fetchMessagesResultNoActions.channels.forEach { (s: String?, pnFetchMessageItems: List<PNFetchMessageItem>) ->
            pnFetchMessageItems.forEach(
                Consumer { pnFetchMessageItem: PNFetchMessageItem ->
                    assertNull(
                        pnFetchMessageItem.actions
                    )
                }
            )
        }
    }

    @Test
    fun testActionReceive() {
        val expectedChannelName = randomValue()
        val expectedMessageCount = 1

        val publishResultList = mutableListOf<PNPublishResult>()

        repeat(expectedMessageCount) {
            pubnub.publish().apply {
                channel = expectedChannelName
                message = "${it}_msg"
                if (it % 2 == 0) meta = generateMap()
            }.sync()!!.run {
                publishResultList.add(this)
            }
        }

        assertEquals(expectedMessageCount, publishResultList.size)

        val actionsCount = AtomicInteger()

        pubnub.addListener(object : SubscribeCallback() {
            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.category == PNStatusCategory.PNConnectedCategory) {
                    if (pnStatus.operation == PNOperationType.PNSubscribeOperation) {
                        publishResultList.forEach {
                            pubnub.addMessageAction().apply {
                                channel = expectedChannelName
                                messageAction = PNMessageAction(
                                    type = "reaction",
                                    value = emoji(),
                                    messageTimetoken = it.timetoken
                                )
                            }.sync()!!
                        }
                    }
                }
            }

            override fun message(pubnub: PubNub, pnMessageResult: PNMessageResult) {
                failTest()
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                failTest()
            }

            override fun signal(pubnub: PubNub, pnSignalResult: PNSignalResult) {
                failTest()
            }

            override fun messageAction(pubnub: PubNub, pnMessageActionResult: PNMessageActionResult) {
                assertEquals(expectedChannelName, pnMessageActionResult.channel)
                actionsCount.incrementAndGet();
            }
        })

        pubnub.subscribe().apply {
            channels = Collections.singletonList(expectedChannelName)
            withPresence = true

        }.execute()

        Awaitility.await()
            .atMost(Durations.TEN_SECONDS)
            .untilAtomic(actionsCount, IsEqual.equalTo(expectedMessageCount));

    }

    @Test
    fun testAddAction_NoChannel() {
        try {
            pubnub.addMessageAction()
                .sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testAddAction_NoMessageActionObject() {
        try {
            pubnub.addMessageAction().apply {
                channel = randomValue()
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_ACTION_MISSING, e)
        }
    }

    @Test
    fun testAddAction_NoMessageActionType() {
        try {
            pubnub.addMessageAction().apply {
                channel = randomValue()
                messageAction = PNMessageAction(
                    type = "",
                    value = randomValue(),
                    messageTimetoken = 1L
                )
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_ACTION_TYPE_MISSING, e)
        }
    }

    @Test
    fun testAddAction_NoMessageActionValue() {
        try {
            pubnub.addMessageAction().apply {
                channel = randomValue()
                messageAction = PNMessageAction(
                    type = randomValue(),
                    value = "",
                    messageTimetoken = 1L
                )
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_ACTION_VALUE_MISSING, e)
        }
    }

    @Test
    fun testGetActions_NoChannel() {
        try {
            pubnub.getMessageActions()
                .sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testRemoveAction_NoChannel() {
        try {
            pubnub.removeMessageAction()
                .sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testRemoveAction_NoMessageTimetoken() {
        try {
            pubnub.removeMessageAction().apply {
                channel = randomValue()
                actionTimetoken = 1L
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_TIMETOKEN_MISSING, e)
        }
    }

    @Test
    fun testRemoveAction_NoMessageActionTimetoken() {
        try {
            pubnub.removeMessageAction().apply {
                channel = randomValue()
                messageTimetoken = 1L
            }.sync()
        } catch (e: Exception) {
            assertPnException(PubNubError.MESSAGE_ACTION_TIMETOKEN_MISSING, e)
        }
    }

    @Test
    fun testAddSameActionTwice() {
        val expectedChannel = randomValue()
        val expectedEmoji = emoji()

        val timetoken = pubnub.publish().apply {
            channel = expectedChannel
            message = randomValue()
            shouldStore = true
        }.sync()!!.timetoken

        pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction = PNMessageAction(
                type = "reaction",
                value = expectedEmoji,
                messageTimetoken = timetoken
            )
        }.sync()!!

        pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction = PNMessageAction(
                type = "reaction",
                value = expectedEmoji,
                messageTimetoken = timetoken
            )
        }.await { _, status ->
            assertTrue(status.error)
            assertEquals(409, status.statusCode)
        }

    }

}
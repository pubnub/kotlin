package com.pubnub.api.integration.pam

import com.google.gson.Gson
import com.pubnub.api.PubNub
import com.pubnub.api.asyncRetry
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.integration.emoji
import com.pubnub.api.integration.generateMap
import com.pubnub.api.integration.generatePayload
import com.pubnub.api.integration.unicode
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.randomChannel
import com.pubnub.api.randomValue
import com.pubnub.api.retry
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

abstract class AccessManagerIntegrationTest : BaseIntegrationTest() {

    companion object {
        const val LEVEL_APP = "subkey"
        const val LEVEL_USER = "user"
        const val LEVEL_CHANNEL = "channel"

        const val READ = 1
        const val WRITE = 2
        const val MANAGE = 4
        const val DELETE = 8
    }

    lateinit var expectedChannel: String
    lateinit var expectedAuthKey: String

    override fun onPrePubnub() {
        expectedChannel = randomChannel()
        expectedAuthKey = "auth_${randomValue()}_${unicode()}".toLowerCase()

        logger.info("Level: ${getPamLevel()}")
        if (::expectedChannel.isInitialized) {
            logger.info("Channel: $expectedChannel")
        }
        if (::expectedAuthKey.isInitialized) {
            logger.info("AuthKey: $expectedAuthKey")
        }
    }

    override fun onBefore() {
        if (performOnServer()) {
            pubnub = server
        } else {
            revokeAllAccess()
        }
    }

    override fun onAfter() {
        revokeAllAccess()
    }

    override fun provideAuthKey() = expectedAuthKey

    @Test
    fun testGetPublishMessageWithPermission() {
        pubnub.publish().apply {
            channel = expectedChannel
            message = randomValue()
        }.asyncRetry { _, status ->
            requestAccess(WRITE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testGetPublishMessageWithoutPermission() {
        pubnub.publish().apply {
            channel = expectedChannel
            message = generateMap()
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testPostPublishMessageWithPermission() {
        pubnub.publish().apply {
            channel = expectedChannel
            message = generatePayload()
            usePost = true
        }.asyncRetry { _, status ->
            requestAccess(WRITE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testPostPublishMessageWithoutPermission() {
        pubnub.publish().apply {
            channel = expectedChannel
            message = generateMap()
            usePost = true
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testMessageCountsWithPermission() {
        requestAccess(READ, WRITE)

        pubnub.messageCounts().apply {
            channels = listOf(expectedChannel)
            channelsTimetoken = listOf(System.currentTimeMillis())
        }.asyncRetry { _, status ->
            requestAccess(READ, WRITE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testMessageCountsWithoutPermission() {
        pubnub.messageCounts().apply {
            channels = listOf(expectedChannel)
            channelsTimetoken = listOf(System.currentTimeMillis())
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testHistoryWithPermission() {
        pubnub.history().apply {
            channel = expectedChannel
        }.asyncRetry { _, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testHistoryWithoutPermission() {
        pubnub.history().apply {
            channel = expectedChannel
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testPublishHistoryWithPermission() {
        val expectedMessagePayload = generatePayload()

        pubnub.publish().apply {
            channel = expectedChannel
            message = expectedMessagePayload
            shouldStore = true
        }.asyncRetry { _, status ->
            requestAccess(READ, WRITE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }

        pubnub.history().apply {
            channel = expectedChannel
        }.asyncRetry { result, status ->
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
            assertNotNull(result!!.messages)
            assertEquals(1, result.messages.size)
            assertEquals(expectedMessagePayload, result.messages[0].entry)
        }
    }

    @Test
    fun testHereNowWithPermission() {
        pubnub.hereNow().apply {
            channels = listOf(expectedChannel)
        }.asyncRetry { _, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testHereNowWithoutPermission() {
        pubnub.hereNow().apply {
            channels = listOf(expectedChannel)
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testSetStateWithoutPermission() {
        val expectedStatePayload = generatePayload()

        pubnub.setPresenceState().apply {
            channels = listOf(expectedChannel)
            uuid = pubnub.configuration.uuid
            state = expectedStatePayload
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testSetStateWithPermission() {
        val expectedStatePayload = generatePayload()

        pubnub.setPresenceState().apply {
            channels = listOf(expectedChannel)
            uuid = pubnub.configuration.uuid
            state = expectedStatePayload
        }.asyncRetry { result, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
            assertEquals(expectedStatePayload, result!!.state)
        }
    }

    @Test
    fun testGetSetStateWithoutPermission() {
        pubnub.getPresenceState().apply {
            channels = listOf(expectedChannel)
            uuid = pubnub.configuration.uuid
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testGetStateWithPermission() {
        pubnub.getPresenceState().apply {
            channels = listOf(expectedChannel)
            uuid = pubnub.configuration.uuid
        }.asyncRetry { _, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testStateComboWithPermission() {
        val expectedStatePayload = generatePayload()

        pubnub.setPresenceState().apply {
            channels = listOf(expectedChannel)
            uuid = pubnub.configuration.uuid
            state = expectedStatePayload
        }.asyncRetry { result, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
            assertEquals(expectedStatePayload, result!!.state)
        }

        pubnub.getPresenceState().apply {
            channels = listOf(expectedChannel)
            uuid = pubnub.configuration.uuid
        }.asyncRetry { result, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
            assertEquals(expectedStatePayload, result!!.stateByUUID[expectedChannel])
        }
    }

    @Test
    fun testPresenceWithPermission() {
        val success = AtomicBoolean()

        pubnub.addListener(object : SubscribeCallback() {

            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
                if (pnStatus.category == PNStatusCategory.PNConnectedCategory &&
                    pnStatus.operation == PNOperationType.PNSubscribeOperation &&
                    pnStatus.uuid == this@AccessManagerIntegrationTest.pubnub.configuration.uuid
                ) {
                    server.subscribe().apply {
                        withPresence = true
                        channels = listOf(expectedChannel)
                    }.execute()
                }
            }

            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
                if (pnPresenceEventResult.event == "join" &&
                    pnPresenceEventResult.channel == expectedChannel &&
                    pnPresenceEventResult.uuid == server.configuration.uuid

                ) {
                    success.set(true)
                }
            }
        })

        requestAccess(READ)

        pubnub.subscribe().apply {
            withPresence = true
            channels = listOf(expectedChannel)
        }.execute()

        Awaitility.await()
            .atMost(Durations.TEN_SECONDS)
            .with()
            .pollInterval(Durations.ONE_SECOND)
            .untilTrue(success)
    }

    @Test
    fun testPublishSignalWithPermission() {
        val expectedPayload = randomValue(5)

        pubnub.signal().apply {
            channel = expectedChannel
            message = expectedPayload
        }.asyncRetry { _, status ->
            requestAccess(WRITE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testPublishSignalWithoutPermission() {
        val expectedPayload = randomValue(5)

        pubnub.signal().apply {
            channel = expectedChannel
            message = expectedPayload
        }.asyncRetry { _, status ->
            assertAuthKey(status)
            revokeAllAccess()
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testDeleteMessageWithPermission() {
        pubnub.deleteMessages().apply {
            channels = listOf(expectedChannel)
        }.asyncRetry { _, status ->
            requestAccess(DELETE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testDeleteMessageWithoutPermission() {
        pubnub.deleteMessages().apply {
            channels = listOf(expectedChannel)
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testFetchMessagesWithPermission() {
        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
        }.asyncRetry { _, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testFetchMessagesWithoutPermission() {
        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testFetchMessageActionsWithPermission() {
        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
            includeMessageActions = true
        }.asyncRetry { _, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testFetchMessageActionsWithoutPermission() {
        pubnub.fetchMessages().apply {
            channels = listOf(expectedChannel)
            includeMessageActions = true
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testAddAMessageActionWithPermission() {
        pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction = PNMessageAction(
                type = "reaction",
                value = emoji(),
                messageTimetoken = 1L
            )
        }.asyncRetry { _, status ->
            requestAccess(WRITE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testAddMessageActionWithoutPermission() {
        pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction =
                PNMessageAction(
                    type = "reaction",
                    value = emoji(),
                    messageTimetoken = 1L
                )
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testGetMessageActionsWithPermission() {
        pubnub.getMessageActions().apply {
            channel = expectedChannel
        }.asyncRetry { _, status ->
            requestAccess(READ)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testGetMessageActionsWithoutPermission() {
        pubnub.getMessageActions().apply {
            channel = expectedChannel
        }.asyncRetry { _, status ->
            revokeAllAccess()
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    @Test
    fun testRemoveMessageActionWithPermission() {
        lateinit var addMessageActionResult: PNAddMessageActionResult

        retry {
            requestAccess(WRITE)

            addMessageActionResult = pubnub.addMessageAction().apply {
                channel = expectedChannel
                messageAction = PNMessageAction(
                    type = ("reaction"),
                    value = (emoji()),
                    messageTimetoken = (1L)
                )
            }.sync()!!
        }

        revokeAllAccess()

        pubnub.removeMessageAction().apply {
            channel = expectedChannel
            messageTimetoken = addMessageActionResult.messageTimetoken
            actionTimetoken = addMessageActionResult.actionTimetoken
        }.asyncRetry { _, status ->
            requestAccess(DELETE)
            assertAuthKey(status)
            assertUuid(status)
            assertStatusSuccess(status)
        }
    }

    @Test
    fun testRemoveMessageActionWithoutPermission() {
        lateinit var publishResult: PNPublishResult

        retry {
            requestAccess(WRITE)

            publishResult = pubnub.publish().apply {
                channel = expectedChannel
                message = randomValue()
                shouldStore = true
            }.sync()!!
        }

        val addMessageActionResult = pubnub.addMessageAction().apply {
            channel = expectedChannel
            messageAction = PNMessageAction(
                type = "reaction",
                value = emoji(),
                messageTimetoken = publishResult.timetoken
            )
        }.sync()

        revokeAllAccess()

        pubnub.removeMessageAction().apply {
            channel = expectedChannel
            messageTimetoken = addMessageActionResult!!.messageTimetoken
            actionTimetoken = addMessageActionResult.actionTimetoken
        }.asyncRetry { _, status ->
            assertAuthKey(status)
            assertUuid(status)
            assertStatusError(status)
            assertCategory(status)
        }
    }

    private fun requestAccess(vararg bitmasks: Int) {
        val revokeAllAccess = bitmasks.sum() == 0

        if (performOnServer()) {
            return
        }

        if (!revokeAllAccess) {
            logger.info("Requesting access for ${bitmasks.sum()} at ${getPamLevel()} level")
        } else {
            logger.info("Revoking all access!")
        }

        val grantOperationBuilder = server.grant().apply {
            read = bitmasks.contains(READ)
            write = bitmasks.contains(WRITE)
            manage = bitmasks.contains(MANAGE)
            delete = bitmasks.contains(DELETE)
            ttl = 1
        }

        // if 0 then ignore levels and revoke ALL

        if (!revokeAllAccess) {
            when (getPamLevel()) {
                LEVEL_USER -> {
                    grantOperationBuilder.authKeys = listOf(expectedAuthKey)
                    grantOperationBuilder.channels = (listOf(expectedChannel, "$expectedChannel-pnpres"))
                }
                LEVEL_CHANNEL -> {
                    grantOperationBuilder.channels = (listOf(expectedChannel, "$expectedChannel-pnpres"))
                }
            }
        }

        retry {
            val grantResult = grantOperationBuilder.sync()!!
            logger.info("Access request result: " + Gson().toJson(grantResult))
            assertNotNull(grantResult)

            if (revokeAllAccess) {
                assertEquals(LEVEL_APP, grantResult.level)
            } else {
                assertEquals(getPamLevel(), grantResult.level)
            }
        }
    }

    private fun revokeAllAccess() {
        requestAccess()
    }

    private fun assertAuthKey(status: PNStatus) {
        if (!performOnServer()) {
            assertEquals(expectedAuthKey, status.authKey)
        }
    }

    private fun assertStatusError(status: PNStatus) {
        if (!performOnServer()) {
            assertTrue(status.error)
        } else {
            assertFalse(status.error)
        }
    }

    private fun assertStatusSuccess(status: PNStatus) {
        if (!performOnServer()) {
            assertFalse(status.error)
        } else {
            assertFalse(status.error)
        }
    }

    private fun assertCategory(status: PNStatus) {
        if (!performOnServer()) {
            assertEquals(PNStatusCategory.PNAccessDeniedCategory, status.category)
        } else {
            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
        }
    }

    private fun assertUuid(pnStatus: PNStatus) {
        assertEquals(pubnub.configuration.uuid, pnStatus.uuid)
    }

    abstract fun getPamLevel(): String

    open fun performOnServer(): Boolean {
        return false
    }
}

// package com.pubnub.api.integration.pam
//
// import com.google.gson.Gson
// import com.pubnub.api.CommonUtils.emoji
// import com.pubnub.api.CommonUtils.generateMap
// import com.pubnub.api.CommonUtils.generatePayload
// import com.pubnub.api.CommonUtils.randomChannel
// import com.pubnub.api.CommonUtils.randomValue
// import com.pubnub.api.CommonUtils.retry
// import com.pubnub.api.CommonUtils.unicode
// import com.pubnub.api.PubNub
// import com.pubnub.api.asyncRetry
// import com.pubnub.api.callbacks.SubscribeCallback
// import com.pubnub.api.enums.PNOperationType
// import com.pubnub.api.enums.PNStatusCategory
// import com.pubnub.api.integration.BaseIntegrationTest
// import com.pubnub.api.models.consumer.PNPublishResult
// import com.pubnub.api.models.consumer.PNStatus
// import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
// import com.pubnub.api.models.consumer.message_actions.PNMessageAction
// import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
// import com.pubnub.api.retryForbidden
// import org.awaitility.Awaitility
// import org.awaitility.Durations
// import org.junit.Assert.assertEquals
// import org.junit.Assert.assertFalse
// import org.junit.Assert.assertNotNull
// import org.junit.Assert.assertTrue
// import org.junit.Test
// import java.util.Locale
// import java.util.concurrent.atomic.AtomicBoolean
//
// abstract class AccessManagerIntegrationTest() : BaseIntegrationTest() {
//
//    companion object {
//        const val LEVEL_APP = "subkey"
//        const val LEVEL_USER = "user"
//        const val LEVEL_CHANNEL = "channel"
//
//        const val READ = 1
//        const val WRITE = 2
//        const val MANAGE = 4
//        const val DELETE = 8
//    }
//
//    lateinit var expectedChannel: String
//    lateinit var expectedAuthKey: String
//    open val pubnubToTest: PubNub = pubnub
//
//    override fun onPrePubnub() {
//        expectedChannel = randomChannel()
//        expectedAuthKey = "auth_${randomValue()}_${unicode()}".lowercase(Locale.getDefault())
//
//        logger.info("Level: ${getPamLevel()}")
//        if (::expectedChannel.isInitialized) {
//            logger.info("Channel: $expectedChannel")
//        }
//        if (::expectedAuthKey.isInitialized) {
//            logger.info("AuthKey: $expectedAuthKey")
//        }
//    }
//
//    override fun onBefore() {
//        // todo: why the heck we've got mutable configuration?!
//        pubnubToTest.configuration.includeInstanceIdentifier = false
//        pubnubToTest.configuration.includeRequestIdentifier = false
//        if (!performOnServer()) {
//            revokeAllAccess()
//        }
//    }
//
//    override fun onAfter() {
//        revokeAllAccess()
//    }
//
//    override fun provideAuthKey() = expectedAuthKey
//
//    @Test
//    fun testGetPublishMessageWithPermission() {
//        pubnubToTest.publish(
//            channel = expectedChannel,
//            message = randomValue()
//        ).asyncRetry { result ->
//            requestAccess(WRITE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testGetPublishMessageWithoutPermission() {
//        pubnubToTest.publish(
//            channel = expectedChannel,
//            message = generateMap()
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testPostPublishMessageWithPermission() {
//        pubnubToTest.publish(
//            channel = expectedChannel,
//            message = generatePayload(),
//            usePost = true
//        ).asyncRetry { result ->
//            requestAccess(WRITE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testPostPublishMessageWithoutPermission() {
//        pubnubToTest.publish(
//            channel = expectedChannel,
//            message = generateMap(),
//            usePost = true
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testMessageCountsWithPermission() {
//        requestAccess(READ, WRITE)
//
//        pubnubToTest.messageCounts(
//            channels = listOf(expectedChannel),
//            channelsTimetoken = listOf(System.currentTimeMillis())
//        ).asyncRetry { result ->
//            requestAccess(READ, WRITE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testMessageCountsWithoutPermission() {
//        pubnubToTest.messageCounts(
//            channels = listOf(expectedChannel),
//            channelsTimetoken = listOf(System.currentTimeMillis())
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testHistoryWithPermission() {
//        pubnubToTest.history(
//            channel = expectedChannel
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testHistoryWithoutPermission() {
//        pubnubToTest.history(
//            channel = expectedChannel
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testPublishHistoryWithPermission() {
//        val expectedMessagePayload = generatePayload()
//
//        pubnubToTest.publish(
//            channel = expectedChannel,
//            message = expectedMessagePayload,
//            shouldStore = true
//        ).asyncRetry { result ->
//            requestAccess(READ, WRITE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//
//        pubnubToTest.history(
//            channel = expectedChannel
//        ).asyncRetry { result ->
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//            assertNotNull(result!!.messages)
//            assertEquals(1, result.messages.size)
//            assertEquals(expectedMessagePayload, result.messages[0].entry)
//        }
//    }
//
//    @Test
//    fun testHereNowWithPermission() {
//        pubnubToTest.hereNow(
//            channels = listOf(expectedChannel)
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testHereNowWithoutPermission() {
//        pubnubToTest.hereNow(
//            channels = listOf(expectedChannel)
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testSetStateWithoutPermission() {
//        val expectedStatePayload = generatePayload()
//
//        pubnubToTest.setPresenceState(
//            channels = listOf(expectedChannel),
//            uuid = pubnubToTest.configuration.userId.value,
//            state = expectedStatePayload
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testSetStateWithPermission() {
//        val expectedStatePayload = generatePayload()
//
//        pubnubToTest.setPresenceState(
//            channels = listOf(expectedChannel),
//            uuid = pubnubToTest.configuration.userId.value,
//            state = expectedStatePayload
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//            assertEquals(expectedStatePayload, result!!.state)
//        }
//    }
//
//    @Test
//    fun testGetSetStateWithoutPermission() {
//        pubnubToTest.getPresenceState(
//            channels = listOf(expectedChannel),
//            uuid = pubnubToTest.configuration.userId.value
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testGetStateWithPermission() {
//        pubnubToTest.getPresenceState(
//            channels = listOf(expectedChannel),
//            uuid = pubnubToTest.configuration.userId.value
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testStateComboWithPermission() {
//        val expectedStatePayload = generatePayload()
//
//        pubnubToTest.setPresenceState(
//            channels = listOf(expectedChannel),
//            uuid = pubnubToTest.configuration.userId.value,
//            state = expectedStatePayload
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//            assertEquals(expectedStatePayload, result!!.state)
//        }
//
//        pubnubToTest.getPresenceState(
//            channels = listOf(expectedChannel),
//            uuid = pubnubToTest.configuration.userId.value
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//            assertEquals(expectedStatePayload, result!!.stateByUUID[expectedChannel])
//        }
//    }
//
//    @Test
//    fun testPresenceWithPermission() {
//        val success = AtomicBoolean()
//
//        pubnubToTest.addListener(object : SubscribeCallback() {
//
//            override fun status(pubnub: PubNub, pnStatus: PNStatus) {
//                if (pnStatus.category == PNStatusCategory.Connected &&
//                    pnStatus.operation == PNOperationType.PNSubscribeOperation &&
//                    pnStatus.uuid == this@AccessManagerIntegrationTest.pubnubToTest.configuration.userId.value
//                ) {
//                    server.subscribe(
//                        withPresence = true,
//                        channels = listOf(expectedChannel)
//                    )
//                }
//            }
//
//            override fun presence(pubnub: PubNub, pnPresenceEventResult: PNPresenceEventResult) {
//                if (pnPresenceEventResult.event == "join" &&
//                    pnPresenceEventResult.channel == expectedChannel &&
//                    pnPresenceEventResult.uuid == server.configuration.userId.value
//
//                ) {
//                    success.set(true)
//                }
//            }
//        })
//
//        requestAccess(READ)
//
//        pubnubToTest.subscribe(
//            withPresence = true,
//            channels = listOf(expectedChannel)
//        )
//
//        Awaitility.await()
//            .atMost(Durations.TEN_SECONDS)
//            .with()
//            .pollInterval(Durations.ONE_SECOND)
//            .untilTrue(success)
//    }
//
//    @Test
//    fun testPublishSignalWithPermission() {
//        val expectedPayload = randomValue(5)
//
//        pubnubToTest.signal(
//            channel = expectedChannel,
//            message = expectedPayload
//        ).asyncRetry { result ->
//            requestAccess(WRITE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testPublishSignalWithoutPermission() {
//        val expectedPayload = randomValue(5)
//
//        pubnubToTest.signal(
//            channel = expectedChannel,
//            message = expectedPayload
//        ).asyncRetry { result ->
//            assertAuthKey(status)
//            revokeAllAccess()
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testDeleteMessageWithPermission() {
//        pubnubToTest.deleteMessages(
//            channels = listOf(expectedChannel)
//        ).retryForbidden({ requestAccess(DELETE) }) { result ->
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testDeleteMessageWithoutPermission() {
//        pubnubToTest.deleteMessages(
//            channels = listOf(expectedChannel)
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testFetchMessagesWithPermission() {
//        pubnubToTest.fetchMessages(
//            channels = listOf(expectedChannel)
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testFetchMessagesWithoutPermission() {
//        pubnubToTest.fetchMessages(
//            channels = listOf(expectedChannel)
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testFetchMessageActionsWithPermission() {
//        pubnubToTest.fetchMessages(
//            channels = listOf(expectedChannel),
//            includeMessageActions = true
//        ).asyncRetry { result ->
//            requestAccess(READ)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testFetchMessageActionsWithoutPermission() {
//        pubnubToTest.fetchMessages(
//            channels = listOf(expectedChannel),
//            includeMessageActions = true
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testAddAMessageActionWithPermission() {
//        pubnubToTest.addMessageAction(
//            channel = expectedChannel,
//            messageAction = PNMessageAction(
//                type = "reaction",
//                value = emoji(),
//                messageTimetoken = 1L
//            )
//        ).asyncRetry { result ->
//            requestAccess(WRITE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testAddMessageActionWithoutPermission() {
//        pubnubToTest.addMessageAction(
//            channel = expectedChannel,
//            messageAction =
//            PNMessageAction(
//                type = "reaction",
//                value = emoji(),
//                messageTimetoken = 1L
//            )
//        ).asyncRetry { result ->
//            revokeAllAccess()
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testGetMessageActionsWithPermission() {
//        pubnubToTest.getMessageActions(
//            channel = expectedChannel
//        ).retryForbidden({ requestAccess(READ) }) { result ->
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testGetMessageActionsWithoutPermission() {
//        pubnubToTest.getMessageActions(
//            channel = expectedChannel
//        ).retryForbidden({ revokeAllAccess() }) { result ->
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    @Test
//    fun testRemoveMessageActionWithPermission() {
//        lateinit var addMessageActionResult: PNAddMessageActionResult
//
//        retry {
//            requestAccess(WRITE)
//
//            addMessageActionResult = pubnubToTest.addMessageAction(
//                channel = expectedChannel,
//                messageAction = PNMessageAction(
//                    type = ("reaction"),
//                    value = (emoji()),
//                    messageTimetoken = (1L)
//                )
//            ).sync()
//        }
//
//        revokeAllAccess()
//
//        pubnubToTest.removeMessageAction(
//            channel = expectedChannel,
//            messageTimetoken = addMessageActionResult.messageTimetoken,
//            actionTimetoken = addMessageActionResult.actionTimetoken!!
//        ).asyncRetry { result ->
//            requestAccess(DELETE)
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusSuccess(status)
//        }
//    }
//
//    @Test
//    fun testRemoveMessageActionWithoutPermission() {
//        lateinit var publishResult: PNPublishResult
//
//        retry {
//            requestAccess(WRITE)
//
//            publishResult = pubnubToTest.publish(
//                channel = expectedChannel,
//                message = randomValue(),
//                shouldStore = true
//            ).sync()
//        }
//
//        val addMessageActionResult = pubnubToTest.addMessageAction(
//            channel = expectedChannel,
//            messageAction = PNMessageAction(
//                type = "reaction",
//                value = emoji(),
//                messageTimetoken = publishResult.timetoken
//            )
//        ).sync()
//
//        revokeAllAccess()
//
//        pubnubToTest.removeMessageAction(
//            channel = expectedChannel,
//            messageTimetoken = addMessageActionResult!!.messageTimetoken,
//            actionTimetoken = addMessageActionResult.actionTimetoken!!
//        ).asyncRetry { result ->
//            assertAuthKey(status)
//            assertUuid(status)
//            assertStatusError(status)
//            assertCategory(status)
//        }
//    }
//
//    private fun requestAccess(vararg bitmasks: Int) {
//        val revokeAllAccess = bitmasks.sum() == 0
//
//        if (performOnServer()) {
//            return
//        }
//
//        if (!revokeAllAccess) {
//            logger.info("Requesting access for ${bitmasks.sum()} at ${getPamLevel()} level")
//        } else {
//            logger.info("Revoking all access!")
//        }
//
//        // if 0 then ignore levels and revoke ALL
//        var authKeys: List<String> = listOf()
//        var channels: List<String> = listOf()
//        if (!revokeAllAccess) {
//            when (getPamLevel()) {
//                LEVEL_USER -> {
//                    authKeys = listOf(expectedAuthKey)
//                    channels = (listOf(expectedChannel, "$expectedChannel-pnpres"))
//                }
//                LEVEL_CHANNEL -> {
//                    channels = (listOf(expectedChannel, "$expectedChannel-pnpres"))
//                }
//            }
//        }
//        val grantOperation = server.grant(
//            read = bitmasks.contains(READ),
//            write = bitmasks.contains(WRITE),
//            manage = bitmasks.contains(MANAGE),
//            delete = bitmasks.contains(DELETE),
//            ttl = 1,
//
//            authKeys = authKeys,
//            channels = channels
//        )
//
//        retry {
//            val grantResult = grantOperation.sync()
//            logger.info("Access request result: " + Gson().toJson(grantResult))
//            assertNotNull(grantResult)
//
//            if (revokeAllAccess) {
//                assertEquals(LEVEL_APP, grantResult.level)
//            } else {
//                assertEquals(getPamLevel(), grantResult.level)
//            }
//        }
//    }
//
//    private fun revokeAllAccess() {
//        requestAccess()
//    }
//
//    private fun assertAuthKey(status: PNStatus) {
//        if (!performOnServer()) {
//            assertEquals(expectedAuthKey, status.authKey)
//        }
//    }
//
//    private fun assertStatusError(status: PNStatus) {
//        if (!performOnServer()) {
//            assertTrue(result.isFailure)
//        } else {
//            assertFalse(result.isFailure)
//        }
//    }
//
//    private fun assertStatusSuccess(status: PNStatus) {
//        if (!performOnServer()) {
//            assertFalse(result.isFailure)
//        } else {
//            assertFalse(result.isFailure)
//        }
//    }
//
//    private fun assertCategory(status: PNStatus) {
//        if (!performOnServer()) {
//            assertEquals(PNStatusCategory.PNAccessDeniedCategory, status.category)
//        } else {
//            assertEquals(PNStatusCategory.PNAcknowledgmentCategory, status.category)
//        }
//    }
//
//    private fun assertUuid(pnStatus: PNStatus) {
//        assertEquals(pubnubToTest.configuration.userId.value, pnStatus.uuid)
//    }
//
//    abstract fun getPamLevel(): String
//
//    open fun performOnServer(): Boolean {
//        return false
//    }
// }

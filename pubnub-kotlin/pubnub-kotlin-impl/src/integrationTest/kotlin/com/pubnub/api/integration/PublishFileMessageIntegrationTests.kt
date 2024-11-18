package com.pubnub.api.integration

import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class PublishFileMessageIntegrationTests : BaseIntegrationTest() {
    @Test
    fun can_publishFileMessage() {
        val publishFileMessageResult: PNPublishFileMessageResult = pubnub.publishFileMessage(
            channel = "whatever",
            fileName = "whatever",
            fileId = "whatever",
            message = "whatever",
            meta = "whatever",
            ttl = 1,
            shouldStore = true,
            customMessageType = "myCustom-Messag_e"
        ).sync()
        assertNotNull(publishFileMessageResult.timetoken)
    }
}

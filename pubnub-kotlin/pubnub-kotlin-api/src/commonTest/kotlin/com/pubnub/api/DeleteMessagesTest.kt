package com.pubnub.api

import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeleteMessagesTest : BaseIntegrationTest() {
    private val channel = randomString()

    @Test
    fun deleteMessages() = runTest {
        val expectedMeta = mapOf(randomString() to randomString())
        val expectedMessage = randomString()

        val result = pubnub.publish(
            channel = channel,
            message = expectedMessage,
            meta = expectedMeta,
            shouldStore = true,
            ttl = 60,
        ).await()

        pubnub.deleteMessages(
            listOf(channel),
            start = result.timetoken + 1,
            end = result.timetoken
        ).await()

        val messages = pubnub.fetchMessages(
            listOf(channel), PNBoundedPage(
                start = result.timetoken + 1,
                end = result.timetoken
            )
        ).await()

        if (messages.channels.isNotEmpty()) {
            assertEquals(channel, messages.channels.entries.single().key)
            assertTrue { messages.channels.entries.single().value.isEmpty() }
        }
    }
}
package com.pubnub.api

import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds

class ChannelMetadataTest : BaseIntegrationTest() {
    private val channel = "myChannel" + randomString()
    private val name = randomString()
    private val description = randomString()
    private val status = randomString()
    private val customData = mapOf("aa" to randomString())
    private val custom = createCustomObject(customData)
    private val includeCustom = true
    private val type = randomString()

    @Test
    fun can_set_metadata() = runTest(timeout = 10.seconds) {
        // when
        val result = pubnub.setChannelMetadata(
            channel,
            name = name,
            status = status,
            custom = custom,
            includeCustom = includeCustom,
            type = type,
            description = description
        ).await()

        // then
        val pnuuidMetadata = result.data
        requireNotNull(pnuuidMetadata)
        assertEquals(channel, pnuuidMetadata.id)
        assertEquals(name, pnuuidMetadata.name)
        assertEquals(status, pnuuidMetadata.status)
        assertEquals(customData, pnuuidMetadata.custom)
        assertEquals(type, pnuuidMetadata.type)
        assertEquals(description, pnuuidMetadata.description)
    }

    @Test
    fun can_receive_set_metadata_event() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            // given
            pubnub.awaitSubscribe(listOf(channel))

            // when
            pubnub.setChannelMetadata(
                channel,
                name = name,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type,
                description = description
            ).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNSetChannelMetadataEventMessage
            assertEquals(channel, message.data.id)
            assertEquals(name, message.data.name)
            assertEquals(description, message.data.description)
            assertEquals(status, message.data.status)
            assertEquals(customData, message.data.custom)
            assertEquals(type, message.data.type)
        }
    }

    @Test
    fun can_delete_metadata() = runTest(timeout = 10.seconds) {
        // given
        pubnub.setChannelMetadata(
            channel,
            name = name,
            status = status,
            custom = custom,
            includeCustom = includeCustom,
            type = type,
            description = description
        ).await()

        // when
        pubnub.removeChannelMetadata(channel).await()

        // then
        val exception = assertFailsWith<PubNubException> {
            pubnub.getChannelMetadata(channel).await()
        }
        assertEquals(404, exception.statusCode)
    }

    @Test
    fun can_receive_delete_metadata_event() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            pubnub.setChannelMetadata(
                channel,
                name = name,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type,
                description = description
            ).await()
            pubnub.awaitSubscribe(listOf(channel))

            // when
            pubnub.removeChannelMetadata(channel).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNDeleteChannelMetadataEventMessage
            assertEquals(channel, message.channel)
        }
    }
}

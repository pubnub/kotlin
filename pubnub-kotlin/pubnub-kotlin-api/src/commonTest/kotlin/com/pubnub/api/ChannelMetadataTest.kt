package com.pubnub.api

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
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
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
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
    fun can_set_metadata() = runTest(timeout = defaultTimeout) {
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
    fun can_receive_set_metadata_event() = runTest(timeout = defaultTimeout) {
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
    fun can_delete_metadata() = runTest(timeout = defaultTimeout) {
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
    fun can_receive_delete_metadata_event() = runTest(timeout = defaultTimeout) {
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
            var result = nextEvent<PNObjectEventResult>()
            if (result.extractedMessage !is PNDeleteChannelMetadataEventMessage) {
                result = nextEvent()
            }
            val message = result.extractedMessage
            assertTrue { message is PNDeleteChannelMetadataEventMessage }
            message as PNDeleteChannelMetadataEventMessage
            assertEquals(channel, message.channel)
        }
    }

    @Test
    fun can_get_all_metadata_with_paging() = runTest(timeout = 30.seconds) {
        // given
        repeat(6) {
            pubnub.setChannelMetadata(
                channel + it,
                name = name,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type,
                description = description
            ).await()
        }

        // when
        val allChannels = mutableListOf<PNChannelMetadata>()
        var next: PNPage.PNNext? = null
        while (true) {
            val result = pubnub.getAllChannelMetadata(
                limit = 2,
                page = next,
                includeCustom = true,
                filter = "id LIKE \"$channel*\""
            ).await()
            allChannels.addAll(result.data)
            next = result.next
            if (next == null || result.data.isEmpty()) {
                break
            }
        }

        // clean up before asserting
        repeat(6) {
            pubnub.removeChannelMetadata(channel + it)
        }

        // then
        assertTrue { allChannels.size == 6 }
        repeat(6) { index ->
            val pnChannelMetadata = allChannels.firstOrNull { it.id == channel + index }
            assertNotNull(pnChannelMetadata)
            assertEquals(name, pnChannelMetadata.name)
            assertEquals(description, pnChannelMetadata.description)
            assertEquals(status, pnChannelMetadata.status)
            assertEquals(customData, pnChannelMetadata.custom)
            assertEquals(type, pnChannelMetadata.type)
        }
    }
}

package com.pubnub.test.integration

import com.pubnub.api.PubNubException
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
    fun can_set_metadata() = runTest {
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
        assertEquals(channel, pnuuidMetadata.id)
        assertEquals(name, pnuuidMetadata.name?.value)
        assertEquals(status, pnuuidMetadata.status?.value)
        assertEquals(customData, pnuuidMetadata.custom?.value)
        assertEquals(type, pnuuidMetadata.type?.value)
        assertEquals(description, pnuuidMetadata.description?.value)
    }

    @Test
    fun can_receive_set_metadata_event() = runTest {
        pubnub.test(backgroundScope) {
            // given
            pubnub.awaitSubscribe(listOf(channel))

            // when
            pubnub.setChannelMetadata(
                channel,
                name = name,
                status = null,
                custom = custom,
                includeCustom = false,
                type = type,
                description = description
            ).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNSetChannelMetadataEventMessage
            assertEquals(channel, message.data.id)
            assertEquals(name, message.data.name?.value)
            assertEquals(description, message.data.description?.value)
            assertEquals(null, message.data.status?.value)
            assertEquals(customData, message.data.custom?.value)
            assertEquals(type, message.data.type?.value)
        }
    }

    @Test
    fun can_delete_metadata() = runTest {
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
    fun can_receive_delete_metadata_event() = runTest {
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
            assertEquals(name, pnChannelMetadata.name?.value)
            assertEquals(null, pnChannelMetadata.description!!.value)
            assertEquals(status, pnChannelMetadata.status?.value)
            assertEquals(customData, pnChannelMetadata.custom?.value)
            assertEquals(type, pnChannelMetadata.type?.value)
        }
    }
}

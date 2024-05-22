package com.pubnub.api

import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
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

//    @Test
//    fun can_receive_set_metadata_event() = runTest(timeout = 10.seconds) {
//        pubnub.test(backgroundScope) {
//            // given
//            pubnub.awaitSubscribe(listOf(channel))
//
//            // when
//            pubnub.setUUIDMetadata(
//                channel,
//                name = name,
//                externalId = externalId,
//                profileUrl = profileUrl,
//                email = email,
//                status = status,
//                custom = custom,
//                includeCustom = includeCustom,
//                type = type
//            ).await()
//
//            // then
//            val result = nextEvent<PNObjectEventResult>()
//            val message = result.extractedMessage
//            message as PNSetUUIDMetadataEventMessage
//            assertEquals(channel, message.data.id)
//            assertEquals(name, message.data.name)
//            assertEquals(externalId, message.data.externalId)
//            assertEquals(profileUrl, message.data.profileUrl)
//            assertEquals(email, message.data.email)
//            assertEquals(status, message.data.status)
//            assertEquals(customData, message.data.custom)
//            assertEquals(type, message.data.type)
//        }
//    }
//
//    @Test
//    fun can_delete_metadata() = runTest(timeout = 10.seconds) {
//        // given
//        pubnub.setUUIDMetadata(
//            channel,
//            name = name,
//            externalId = externalId,
//            profileUrl = profileUrl,
//            email = email,
//            status = status,
//            custom = custom,
//            includeCustom = includeCustom,
//            type = type
//        ).await()
//
//        // when
//        pubnub.removeUUIDMetadata(channel).await()
//
//        // then
//        assertFailsWith<PubNubException> {
//            pubnub.getUUIDMetadata(channel).await()
//        }
//    }
//
//    @Test
//    fun can_receive_delete_metadata_event() = runTest(timeout = 10.seconds) {
//        pubnub.test(backgroundScope) {
//            // given
//            pubnub.setUUIDMetadata(
//                channel,
//                name = name,
//                externalId = externalId,
//                profileUrl = profileUrl,
//                email = email,
//                status = status,
//                custom = custom,
//                includeCustom = includeCustom,
//                type = type
//            ).await()
//            pubnub.awaitSubscribe(listOf(channel))
//
//            // when
//            pubnub.removeUUIDMetadata(channel).await()
//
//            // then
//            val result = nextEvent<PNObjectEventResult>()
//            val message = result.extractedMessage
//            message as PNDeleteUUIDMetadataEventMessage
//            assertEquals(channel, message.uuid)
//        }
//    }
}

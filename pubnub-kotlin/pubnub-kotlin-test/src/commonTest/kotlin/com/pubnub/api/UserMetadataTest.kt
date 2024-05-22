package com.pubnub.api

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class UserMetadataTest : BaseIntegrationTest() {
    private val uuid = "myUser" + randomString()
    private val name = randomString()
    private val externalId = randomString()
    private val profileUrl = randomString()
    private val email = randomString()
    private val status = randomString()
    private val customData = mapOf("aa" to randomString())
    private val custom = createCustomObject(customData)
    private val includeCustom = true
    private val type = randomString()

    @Test
    fun can_set_metadata() = runTest(timeout = 10.seconds) {
        // when
        val result = pubnub.setUUIDMetadata(
            uuid,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            status = status,
            custom = custom,
            includeCustom = includeCustom,
            type = type
        ).await()

        // then
        val pnuuidMetadata = result.data
        requireNotNull(pnuuidMetadata)
        assertEquals(uuid, pnuuidMetadata.id)
        assertEquals(name, pnuuidMetadata.name)
        assertEquals(externalId, pnuuidMetadata.externalId)
        assertEquals(profileUrl, pnuuidMetadata.profileUrl)
        assertEquals(email, pnuuidMetadata.email)
        assertEquals(status, pnuuidMetadata.status)
        assertEquals(customData, pnuuidMetadata.custom)
        assertEquals(type, pnuuidMetadata.type)
    }

    @Test
    fun can_receive_set_metadata_event() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            // given
            pubnub.awaitSubscribe(listOf(uuid))

            // when
            pubnub.setUUIDMetadata(
                uuid,
                name = name,
                externalId = externalId,
                profileUrl = profileUrl,
                email = email,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type
            ).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNSetUUIDMetadataEventMessage
            assertEquals(uuid, message.data.id)
            assertEquals(name, message.data.name)
            assertEquals(externalId, message.data.externalId)
            assertEquals(profileUrl, message.data.profileUrl)
            assertEquals(email, message.data.email)
            assertEquals(status, message.data.status)
            assertEquals(customData, message.data.custom)
            assertEquals(type, message.data.type)
        }
    }

    @Test
    fun can_delete_metadata() = runTest(timeout = 10.seconds) {
        // given
        pubnub.setUUIDMetadata(
            uuid,
            name = name,
            externalId = externalId,
            profileUrl = profileUrl,
            email = email,
            status = status,
            custom = custom,
            includeCustom = includeCustom,
            type = type
        ).await()

        // when
        pubnub.removeUUIDMetadata(uuid).await()

        // then
        assertFailsWith<PubNubException> {
            pubnub.getUUIDMetadata(uuid).await()
        }
    }

    @Test
    fun can_receive_delete_metadata_event() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            // given
            pubnub.setUUIDMetadata(
                uuid,
                name = name,
                externalId = externalId,
                profileUrl = profileUrl,
                email = email,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type
            ).await()
            pubnub.awaitSubscribe(listOf(uuid))

            // when
            pubnub.removeUUIDMetadata(uuid).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNDeleteUUIDMetadataEventMessage
            assertEquals(uuid, message.uuid)
        }
    }
}

package com.pubnub.test.integration

import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteUUIDMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetUUIDMetadataEventMessage
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import com.pubnub.test.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

internal const val HTTP_PRECONDITION_FAILED = 412

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
    fun can_set_metadata() = runTest {
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
        assertEquals(name, pnuuidMetadata.name?.value)
        assertEquals(externalId, pnuuidMetadata.externalId?.value)
        assertEquals(profileUrl, pnuuidMetadata.profileUrl?.value)
        assertEquals(email, pnuuidMetadata.email?.value)
        assertEquals(status, pnuuidMetadata.status?.value)
        assertEquals(customData, pnuuidMetadata.custom?.value)
        assertEquals(type, pnuuidMetadata.type?.value)
    }

    @Test
    fun set_metadata_ifMatch_allows_change() = runTest {
        // given
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

        val pnuuidMetadata = result.data

        // when
        val newData = pubnub.setUUIDMetadata(uuid, externalId = "someNewId", ifMatchesEtag = pnuuidMetadata.eTag?.value).await().data

        // then
        assertEquals("someNewId", newData.externalId?.value)
    }

    @Test
    fun set_metadata_ifMatch_prohibits_change() = runTest {
        // given
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

        val pnuuidMetadata = result.data
        pubnub.setUUIDMetadata(uuid, name = "someNewName").await()

        // when
        val ex = assertFailsWith<PubNubException> {
            pubnub.setUUIDMetadata(uuid, externalId = "someNewId", ifMatchesEtag = pnuuidMetadata.eTag?.value).await()
        }

        // then
        assertEquals(HTTP_PRECONDITION_FAILED, ex.statusCode)
    }

    @Test
    fun can_receive_set_metadata_event() = runTest(timeout = 30.seconds) {
        pubnub.test(backgroundScope) {
            // given
            pubnub.awaitSubscribe(listOf(uuid))

            // Wait a bit to ensure subscription is fully established
            delay(200)

            // when
            pubnub.setUUIDMetadata(
                uuid,
                externalId = externalId,
                profileUrl = profileUrl,
                email = email,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type
            ).await()

            // then
            val result = waitForEvent(PNSetUUIDMetadataEventMessage::class)
            val message = result.extractedMessage as PNSetUUIDMetadataEventMessage
            assertEquals(uuid, message.data.id)
            assertNull(message.data.name?.value)
            assertEquals(externalId, message.data.externalId?.value)
            assertEquals(profileUrl, message.data.profileUrl?.value)
            assertEquals(email, message.data.email?.value)
            assertEquals(status, message.data.status?.value)
            assertEquals(customData, message.data.custom?.value)
            assertEquals(type, message.data.type?.value)
        }
    }

    @Test
    fun can_delete_metadata() = runTest {
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
    fun can_receive_delete_metadata_event() = runTest(timeout = 30.seconds) {
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

            // Wait a bit to ensure subscription is fully established
            delay(200)

            // when
            pubnub.removeUUIDMetadata(uuid).await()

            // then
            val result = waitForEvent(PNDeleteUUIDMetadataEventMessage::class)
            val message = result.extractedMessage as PNDeleteUUIDMetadataEventMessage
            assertEquals(uuid, message.uuid)
        }
    }

    @Test
    fun can_get_metadata() = runTest {
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
        val result = pubnub.getUUIDMetadata(uuid, includeCustom = true).await()

        // then
        val pnuuidMetadata = result.data
        requireNotNull(pnuuidMetadata)
        assertEquals(uuid, pnuuidMetadata.id)
        assertEquals(name, pnuuidMetadata.name?.value)
        assertEquals(externalId, pnuuidMetadata.externalId?.value)
        assertEquals(profileUrl, pnuuidMetadata.profileUrl?.value)
        assertEquals(email, pnuuidMetadata.email?.value)
        assertEquals(status, pnuuidMetadata.status?.value)
        assertEquals(customData, pnuuidMetadata.custom?.value)
        assertEquals(type, pnuuidMetadata.type?.value)
    }

    @Test
    fun can_get_all_metadata_with_paging() = runTest(timeout = 30.seconds) {
        // given
        repeat(6) {
            pubnub.setUUIDMetadata(
                uuid + it,
                name = name,
                externalId = externalId,
                profileUrl = profileUrl,
                email = email,
                status = status,
                custom = custom,
                includeCustom = includeCustom,
                type = type
            ).await()
        }

        // when
        val allUsers = mutableListOf<PNUUIDMetadata>()
        var next: PNPage.PNNext? = null
        while (true) {
            val result: PNUUIDMetadataArrayResult = pubnub.getAllUUIDMetadata(
                limit = 2,
                page = next,
                includeCustom = true,
                filter = "id LIKE \"$uuid*\""
            ).await()
            allUsers.addAll(result.data)
            next = result.next
            if (next == null || result.data.isEmpty()) {
                break
            }
        }

        // clean up before asserting
        repeat(6) {
            pubnub.removeUUIDMetadata(uuid + it)
        }

        // then
        assertEquals(6, allUsers.size)
        repeat(6) {
            val pnuuidMetadata = allUsers.firstOrNull { user -> user.id == uuid + it }
            assertNotNull(pnuuidMetadata)
            assertEquals(name, pnuuidMetadata.name?.value)
            assertEquals(externalId, pnuuidMetadata.externalId?.value)
            assertEquals(profileUrl, pnuuidMetadata.profileUrl?.value)
            assertEquals(email, pnuuidMetadata.email?.value)
            assertEquals(status, pnuuidMetadata.status?.value)
            assertEquals(customData, pnuuidMetadata.custom?.value)
            assertEquals(type, pnuuidMetadata.type?.value)
        }
    }
}

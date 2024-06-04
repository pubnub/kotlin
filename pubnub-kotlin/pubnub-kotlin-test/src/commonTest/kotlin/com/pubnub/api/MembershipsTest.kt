package com.pubnub.api

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetChannelMetadataEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.time.Duration.Companion.seconds

class MembershipsTest : BaseIntegrationTest() {
    private val channel = "myChannel" + randomString()
    private val name = randomString()
    private val description = randomString()
    private val status = randomString()
    private val customData = mapOf("aa" to randomString())
    private val custom = createCustomObject(customData)
    private val includeCustom = true
    private val type = randomString()

    @Test
    fun can_set_memberships() = runTest(timeout = 10.seconds) {
        // when
        val result = pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channel, custom, status)),
            includeCustom = includeCustom,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.channel?.id == channel }
        assertEquals(channel, pnChannelDetails.channel?.id)
        assertEquals(customData, pnChannelDetails.custom)
    }

    @Test
    fun can_receive_set_membership_event() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            // given
            pubnub.awaitSubscribe(listOf(channel))

            // when
            pubnub.setMemberships(
                listOf(PNChannelMembership.Partial(channel, custom, status)),
                includeCustom = includeCustom,
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
            ).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNSetMembershipEventMessage
            assertEquals(channel, message.data.channel)
            assertEquals(pubnub.configuration.userId.value, message.data.uuid)
            assertEquals(customData, message.data.custom)
        }
    }

    @Test
    fun can_delete_membership() = runTest(timeout = 10.seconds) {
        // given
        pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channel, custom, status))
        ).await()

        // when
        pubnub.removeMemberships(listOf(channel)).await()

        // then
        var next: PNPage.PNNext? = null
        while(true) {
            val result = pubnub.getChannelMembers(channel, page = next).await()
            next = result.next
            assertFalse { result.data.any { it.uuid?.id == pubnub.configuration.userId.value } }
            if (next == null || result.data.isEmpty()) {
                break
            }
        }
    }

    @Test
    fun can_receive_delete_membership_event() = runTest(timeout = 10.seconds) {
        pubnub.test(backgroundScope) {
            // given
            pubnub.setMemberships(
                listOf(PNChannelMembership.Partial(channel, custom, status))
            ).await()

            pubnub.awaitSubscribe(listOf(channel))

            // when
            pubnub.removeMemberships(listOf(channel)).await()

            // then
            val result = nextEvent<PNObjectEventResult>()
            val message = result.extractedMessage
            message as PNDeleteMembershipEventMessage
            assertEquals(pubnub.configuration.userId.value, message.data.uuid)
            assertEquals(channel, message.data.channelId)
        }
    }
}

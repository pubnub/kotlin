package com.pubnub.test.integration

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import com.pubnub.test.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
    fun can_set_memberships() = runTest {
        // when
        val result = pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channel, custom, status)),
            includeCustom = includeCustom,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.channel.id == channel }
        assertEquals(channel, pnChannelDetails.channel.id)
        assertEquals(customData, pnChannelDetails.custom?.value)
    }

    @Test
    fun can_set_and_get_memberships_for_other_uuid() = runTest {
        // when
        val userId = "some-user-" + randomString()
        pubnub.setMemberships(
            channels = listOf(PNChannelMembership.Partial(channel, custom, status)),
            uuid = userId,
            includeCustom = includeCustom,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
        ).await()

        val result = pubnub.getMemberships(uuid = userId, filter = "channel.id == '$channel'", includeCustom = true).await()

        // then
        val pnChannelDetails = result.data.single { it.channel.id == channel }
        assertEquals(channel, pnChannelDetails.channel.id)
        assertEquals(customData, pnChannelDetails.custom?.value)
    }

    @Test
    fun can_receive_set_membership_event() = runTest {
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
            assertEquals(customData, message.data.custom?.value)
        }
    }

    @Test
    fun can_delete_membership() = runTest {
        // given
        pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channel, custom, status))
        ).await()

        // when
        pubnub.removeMemberships(listOf(channel)).await()

        // then
        var next: PNPage.PNNext? = null
        while (true) {
            val result = pubnub.getChannelMembers(channel, page = next).await()
            next = result.next
            assertFalse { result.data.any { it.uuid.id == pubnub.configuration.userId.value } }
            if (next == null || result.data.isEmpty()) {
                break
            }
        }
    }

    @Test
    fun can_receive_delete_membership_event() = runTest {
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
            val message = result.extractedMessage as PNDeleteMembershipEventMessage
            assertEquals(pubnub.configuration.userId.value, message.data.uuid)
            assertEquals(channel, message.data.channelId)
        }
    }
}

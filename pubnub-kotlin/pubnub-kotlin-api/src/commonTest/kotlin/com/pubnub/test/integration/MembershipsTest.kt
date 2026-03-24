package com.pubnub.test.integration

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.kmp.PLATFORM
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
    private val status = randomString()
    private val customData = mapOf("aa" to randomString())
    private val custom = createCustomObject(customData)
    private val includeCustom = true
    private val includeChannel = true
    private val includeChannelCustom = true
    private val type = randomString()

    @Test
    fun can_set_memberships_deprecated() = runTest {
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
    fun can_set_memberships() = runTest {
        // when
        val result = pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channel, custom, status, type)),
            include = MembershipInclude(
                includeCustom = includeCustom,
                includeChannel = includeChannel,
                includeChannelCustom = includeChannelCustom,
                includeStatus = true,
                includeType = true
            ),
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.channel.id == channel }
        assertEquals(channel, pnChannelDetails.channel.id)
        assertEquals(customData, pnChannelDetails.custom?.value)
        assertEquals(status, pnChannelDetails.status?.value)
        assertEquals(type, pnChannelDetails.type?.value)
    }

    @Test
    fun can_set_and_get_memberships_for_other_uuid_deprecated() = runTest {
        // when
        val userId = "some-user-" + randomString()
        pubnub.setMemberships(
            channels = listOf(PNChannelMembership.Partial(channel, custom, status)),
            uuid = userId,
            includeCustom = includeCustom,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM,
        ).await()

        val result =
            pubnub.getMemberships(uuid = userId, filter = "channel.id == '$channel'", includeCustom = true).await()

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
            channels = listOf(PNChannelMembership.Partial(channel, custom, status, type)),
            userId = userId,
            include = MembershipInclude(
                includeCustom = includeCustom,
                includeChannel = includeChannel,
                includeChannelCustom = includeChannelCustom
            ),
        ).await()

        val result =
            pubnub.getMemberships(
                userId = userId,
                filter = "channel.id == '$channel'",
                include = MembershipInclude(
                    includeCustom = includeCustom,
                    includeStatus = true,
                    includeType = true
                ),
            ).await()

        // then
        val pnChannelDetails = result.data.single { it.channel.id == channel }
        assertEquals(channel, pnChannelDetails.channel.id)
        assertEquals(customData, pnChannelDetails.custom?.value)
        assertEquals(status, pnChannelDetails.status?.value)
        assertEquals(type, pnChannelDetails.type?.value)
    }

    @Test
    fun can_receive_set_membership_event_deprecated() = runTest {
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
    fun can_receive_set_membership_event() = runTest {
        pubnub.test(backgroundScope) {
            // given
            pubnub.awaitSubscribe(listOf(channel))

            // when
            pubnub.setMemberships(
                listOf(PNChannelMembership.Partial(channel, custom, status)),
                include = MembershipInclude(
                    includeCustom = includeCustom,
                    includeChannel = includeChannel,
                    includeChannelCustom = includeChannelCustom
                ),
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
    fun setMemberships_partial_membership_update_preserves_unchanged_fields() = runTest {
        val channelForPartialUpdate = "partialUpdate" + randomString()
        val initialStatus = "initialStatus" + randomString()
        val initialType = "initialType" + randomString()
        val initialCustomData = mapOf("key" to "initialValue" + randomString())
        val initialCustom = createCustomObject(initialCustomData)
        val updatedType = "updatedType" + randomString()

        // Step 1: Create membership with status, type and custom set
        pubnub.setMemberships(
            channels = listOf(
                PNChannelMembership.Partial(
                    channelId = channelForPartialUpdate,
                    custom = initialCustom,
                    status = initialStatus,
                    type = initialType,
                )
            ),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true
            ),
        ).await()

        // Step 2: Update only type (status and custom are omitted / null in Partial)
        val updateResult = pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channelForPartialUpdate, type = updatedType)),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true
            ),
        ).await()

        // Step 3: Verify the direct setMemberships response
        val membership = updateResult.data.single { it.channel.id == channelForPartialUpdate }
        assertEquals(updatedType, membership.type?.value, "type should be updated to the new value")
        assertEquals(initialStatus, membership.status?.value, "status should be preserved from the initial set")
        assertEquals(initialCustomData, membership.custom?.value, "custom should be preserved from the initial set")

        // Cleanup
        pubnub.removeMemberships(listOf(channelForPartialUpdate)).await()
    }

    @Test
    fun membership_updated_event_contains_only_fields_included_in_updates() = runTest {
        val channelForUpdate = "partialEvent" + randomString()
        val initialStatus = "memberStatus" + randomString()
        val initialType = "memberType" + randomString()
        val initialCustomData = mapOf("role" to "member" + randomString())
        val initialCustom = createCustomObject(initialCustomData)
        val updatedStatus = "moderatorStatus" + randomString()

        // Step 1: Create membership with status, type and custom
        pubnub.setMemberships(
            listOf(PNChannelMembership.Partial(channelForUpdate, custom = initialCustom, status = initialStatus, type = initialType)),
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true
            ),
        ).await()
        pubnub.test(backgroundScope, checkAllEvents = false) {
            // Step 2: Subscribe to the channel to receive membership events
            pubnub.awaitSubscribe(listOf(channelForUpdate))

            // Step 3: Partially update only status
            val directResult = pubnub.setMemberships(
                listOf(PNChannelMembership.Partial(channelForUpdate, status = updatedStatus)),
                include = MembershipInclude(
                    includeCustom = true,
                    includeStatus = true,
                    includeType = true
                ),
            ).await()
            // Step 4: Verify direct response
            val directMembership = directResult.data.single { it.channel.id == channelForUpdate }
            assertEquals(updatedStatus, directMembership.status?.value, "direct result: status should be updated")
            assertEquals(initialType, directMembership.type?.value, "direct result: type should be preserved")
            assertEquals(initialCustomData, directMembership.custom?.value, "direct result: custom should be preserved")

            // Step 5: Verify event callback
            // The event payload only contains fields included in the update request,
            // so type and custom won't be present since we only updated status
            val event = nextEvent<PNObjectEventResult>()
            val message = event.extractedMessage
            message as PNSetMembershipEventMessage
            assertEquals(channelForUpdate, message.data.channel)
            assertEquals(pubnub.configuration.userId.value, message.data.uuid)
            assertEquals(updatedStatus, message.data.status?.value, "event: status should be updated")

            // Cleanup
            println("-=before: removeMemberships")
            pubnub.removeMemberships(listOf(channelForUpdate)).await()
        }
    }

    @Test
    fun can_delete_membership() = runTest {
        if (PLATFORM == "JS" || PLATFORM == "iOS") { // todo enable for JS/iOS once is implemented
            return@runTest
        }
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
        if (PLATFORM == "JS" || PLATFORM == "iOS") { // todo enable for JS/iOS once is implemented
            return@runTest
        }
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

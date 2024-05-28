package com.pubnub.api

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.time.Duration.Companion.seconds

class MembersTest : BaseIntegrationTest() {
    private val channel = "myChannel" + randomString()
    private val name = randomString()
    private val description = randomString()
    private val status = randomString()
    private val customData = mapOf("aa" to randomString())
    private val custom = createCustomObject(customData)
    private val includeCustom = true
    private val type = randomString()

    @Test
    fun can_set_members() = runTest(timeout = 10.seconds) {
        // when
        val result = pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, custom, status)),
            includeCustom = includeCustom,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.uuid?.id == pubnub.configuration.userId.value }
        assertEquals(customData, pnChannelDetails.custom)
        assertEquals(status, pnChannelDetails.status)
    }

    @Test
    fun can_delete_members() = runTest(timeout = 10.seconds) {
        // given
        pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, custom, status))
        ).await()

        // when
        pubnub.removeChannelMembers(channel, listOf(pubnub.configuration.userId.value)).await()

        // then
        var next: PNPage.PNNext? = null
        while(true) {
            val result = pubnub.getMemberships(pubnub.configuration.userId.value, page = next).await()
            next = result.next
            assertFalse { result.data.any { it.channel?.id == channel } }
            if (next == null || result.data.isEmpty()) {
                break
            }
        }
    }
}

package com.pubnub.test.integration

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.kmp.PLATFORM
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertEquals
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
    fun can_set_members_deprecated() = runTest {
        // when
        val result = pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, custom, null)),
            includeCustom = includeCustom,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.uuid.id == pubnub.configuration.userId.value }
        assertEquals(customData, pnChannelDetails.custom?.value)
    }

    @Test
    fun can_set_members() = runTest {
        // when
        val result = pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, custom, status, type)),
            include = MemberInclude(includeCustom = includeCustom, includeUser = true, includeUserCustom = true, includeStatus = true, includeType = true),
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.uuid.id == pubnub.configuration.userId.value }
        assertEquals(customData, pnChannelDetails.custom?.value)
        assertEquals(status, pnChannelDetails.status?.value)
        assertEquals(type, pnChannelDetails.type?.value)
    }

    @Test
    fun can_get_members() = runTest {
        // when
        pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, custom, status, type)),
            include = MemberInclude(
                includeCustom = includeCustom,
                includeUser = true,
                includeUserCustom = true,
                includeStatus = true,
                includeType = true
            ),
        ).await()

        val result = pubnub.getChannelMembers(
            channel,
            include = MemberInclude(includeCustom = includeCustom, includeStatus = true, includeType = true)
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.uuid.id == pubnub.configuration.userId.value }
        assertEquals(customData, pnChannelDetails.custom?.value)
        assertEquals(status, pnChannelDetails.status?.value)
        assertEquals(type, pnChannelDetails.type?.value)
    }

        @Test
    fun can_set_members_with_status_deprecated() = runTest(timeout = 10.seconds) {
        if (PLATFORM == "JS") {
            // TODO JS doesn't have membership status
            return@runTest
        }
        // when
        val result = pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, null, status)),
            includeCustom = includeCustom,
            includeUUIDDetails = PNUUIDDetailsLevel.UUID_WITH_CUSTOM,
        ).await()

        // then
        val pnChannelDetails = result.data.single { it.uuid.id == pubnub.configuration.userId.value }
        assertEquals(status, pnChannelDetails.status?.value)
    }

    @Test
    fun can_delete_members() = runTest {
        if (PLATFORM == "JS" || PLATFORM == "iOS") { // todo enable for JS/iOS once is implemented
            return@runTest
        }
        // given
        pubnub.setChannelMembers(
            channel,
            listOf(PNMember.Partial(pubnub.configuration.userId.value, custom, status))
        ).await()

        // when
        pubnub.removeChannelMembers(channel, listOf(pubnub.configuration.userId.value)).await()

        // then
        var next: PNPage.PNNext? = null
        while (true) {
            yield()
            val result = pubnub.getMemberships(pubnub.configuration.userId.value, page = next).await()
            next = result.next
            assertFalse { result.data.any { it.channel.id == channel } }
            if (next == null || result.data.isEmpty()) {
                break
            }
        }
    }
}

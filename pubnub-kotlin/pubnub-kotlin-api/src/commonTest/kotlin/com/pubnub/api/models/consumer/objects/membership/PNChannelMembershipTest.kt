package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEvent
import com.pubnub.api.utils.PatchValue
import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals

class PNChannelMembershipTest {
    @Test
    fun plus() {
        val originalMembership = PNChannelMembership(
            PNChannelMetadata(randomString()),
            custom = PatchValue.of(mapOf(randomString() to randomString())),
            updated = randomString(),
            eTag = randomString(),
            status = null
        )

        val updateMembership = PNSetMembershipEvent(
            originalMembership.channel.id,
            randomString(),
            custom = null,
            updated = randomString(),
            eTag = randomString(),
            status = PatchValue.of(randomString())
        )
        val expectedMembership = originalMembership.copy(
            updated = updateMembership.updated,
            eTag = updateMembership.eTag,
            status = updateMembership.status,
            type = updateMembership.ty
        )

        val actualMembership = originalMembership + updateMembership

        assertEquals(expectedMembership, actualMembership)
    }
}

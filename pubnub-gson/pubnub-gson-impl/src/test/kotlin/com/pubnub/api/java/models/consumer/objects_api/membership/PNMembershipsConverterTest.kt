package com.pubnub.api.java.models.consumer.objects_api.membership

import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataConverter
import com.pubnub.api.java.models.consumer.objects_api.channel.randomString
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.utils.PatchValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNMembershipsConverterTest {
    @Test
    fun from() {
        val membership = PNChannelMembership(
            com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata(
                randomString(),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(mapOf(randomString() to randomString())),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
            ),
            PatchValue.of(mapOf(randomString() to randomString())),
            randomString(),
            randomString(),
            PatchValue.of(randomString())
        )
        val actual = PNMembershipConverter.from(membership)
        assertEquals(membership, actual!!)
    }

    @Test
    fun from_multiple() {
        val items = buildList<PNChannelMembership>(3) {
            PNChannelMembership(
                com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata(
                    randomString(),
                    PatchValue.of(randomString()),
                    PatchValue.of(randomString()),
                    PatchValue.of(mapOf(randomString() to randomString())),
                    PatchValue.of(randomString()),
                    PatchValue.of(randomString()),
                    PatchValue.of(randomString()),
                ),
                PatchValue.of(mapOf(randomString() to randomString())),
                randomString(),
                randomString(),
                PatchValue.of(randomString())
            )
        }
        val actualList = PNMembershipConverter.from(items)
        actualList.zip(items).forEach {
            val actual = it.second
            val metadata = it.first
            assertEquals(metadata, actual)
        }
    }

    private fun assertEquals(
        metadata: PNChannelMembership,
        actual: PNMembership,
    ) {
        assertEquals(PNChannelMetadataConverter.from(metadata.channel), actual.channel)
        assertEquals(metadata.status, actual.status)
        assertEquals(metadata.eTag, actual.eTag)
        assertEquals(metadata.custom, actual.custom)
        assertEquals(metadata.updated, actual.updated)
    }
}

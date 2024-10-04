package com.pubnub.api.java.models.consumer.objects_api.member

import com.pubnub.api.java.models.consumer.objects_api.channel.randomString
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataConverter
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNMembersConverterTest {
    @Test
    fun from() {
        val member = PNMember(
            PNUUIDMetadata(
                randomString(),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(mapOf(randomString() to randomString())),
                PatchValue.of(randomString()),
            ),
            PatchValue.of(mapOf(randomString() to randomString())),
            randomString(),
            randomString(),
            PatchValue.of(randomString())
        )
        val actual = PNMembersConverter.from(member)
        assertEquals(member, actual!!)
    }

    @Test
    fun from_multiple() {
        val items = buildList<PNMember>(3) {
            PNMember(
                PNUUIDMetadata(
                    randomString(),
                    PatchValue.of(randomString()),
                    PatchValue.of(randomString()),
                    PatchValue.of(randomString()),
                    PatchValue.of(randomString()),
                    PatchValue.of(mapOf(randomString() to randomString())),
                    PatchValue.of(randomString()),
                ),
                PatchValue.of(mapOf(randomString() to randomString())),
                randomString(),
                randomString(),
                PatchValue.of(randomString())
            )
        }
        val actualList = PNMembersConverter.from(items)
        actualList.zip(items).forEach {
            val actual = it.second
            val metadata = it.first
            assertEquals(metadata, actual)
        }
    }

    private fun assertEquals(
        metadata: PNMember,
        actual: PNMembers,
    ) {
        assertEquals(PNUUIDMetadataConverter.from(metadata.uuid)!!, actual.uuid)
        assertEquals(metadata.status, actual.status)
        assertEquals(metadata.eTag, actual.eTag)
        assertEquals(metadata.custom, actual.custom)
        assertEquals(metadata.updated, actual.updated)
    }
}

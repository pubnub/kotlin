package com.pubnub.api.java.models.consumer.objects_api.channel

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataConverter
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNUUIDMetadataConverterTest {
    @Test
    fun from() {
        val metadata = PNUUIDMetadata(
            randomString(),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(mapOf(randomString() to randomString())),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
        )
        val actual = PNUUIDMetadataConverter.from(metadata)
        assertEquals(metadata, actual!!)
    }

    @Test
    fun fromMultiple() {
        val items = buildList<PNUUIDMetadata>(3) {
            PNUUIDMetadata(
                randomString(),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(mapOf(randomString() to randomString())),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
            )
        }
        val actualList = PNUUIDMetadataConverter.from(items)
        actualList.zip(items).forEach {
            val actual = it.second
            val metadata = it.first
            assertEquals(metadata, actual)
        }
    }

    private fun assertEquals(
        metadata: PNUUIDMetadata,
        actual: com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata,
    ) {
        assertEquals(metadata.id, actual.id)
        assertEquals(metadata.email, actual.email)
        assertEquals(metadata.externalId, actual.externalId)
        assertEquals(metadata.profileUrl, actual.profileUrl)
        assertEquals(metadata.name, actual.name)
        assertEquals(metadata.status, actual.status)
        assertEquals(metadata.eTag, actual.eTag)
        assertEquals(metadata.type, actual.type)
        assertEquals(metadata.custom, actual.custom)
        assertEquals(metadata.updated, actual.updated)
    }
}

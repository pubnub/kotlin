package com.pubnub.api.java.models.consumer.objects_api.channel

import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.utils.PatchValue
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNChannelMetadataConverterTest {
    @Test
    fun from() {
        val metadata = PNChannelMetadata(
            randomString(),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(mapOf(randomString() to randomString())),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
            PatchValue.of(randomString()),
        )
        val actual = PNChannelMetadataConverter.from(metadata)
        assertEquals(metadata.id, actual.id)
        assertEquals(metadata.name, actual.name)
        assertEquals(metadata.status, actual.status)
        assertEquals(metadata.eTag, actual.eTag)
        assertEquals(metadata.type, actual.type)
        assertEquals(metadata.custom, actual.custom)
        assertEquals(metadata.description, actual.description)
        assertEquals(metadata.updated, actual.updated)
    }

    @Test
    fun fromMultiple() {
        val items = buildList<PNChannelMetadata>(3) {
            PNChannelMetadata(
                randomString(),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(mapOf(randomString() to randomString())),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
                PatchValue.of(randomString()),
            )
        }
        val actualList = PNChannelMetadataConverter.from(items)
        actualList.zip(items).forEach {
            val actual = it.second
            val metadata = it.first
            assertEquals(metadata, actual)
        }
    }

    private fun assertEquals(
        metadata: com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata,
        actual: PNChannelMetadata,
    ) {
        assertEquals(metadata.id, actual.id)
        assertEquals(metadata.name, actual.name)
        assertEquals(metadata.status, actual.status)
        assertEquals(metadata.eTag, actual.eTag)
        assertEquals(metadata.type, actual.type)
        assertEquals(metadata.custom, actual.custom)
        assertEquals(metadata.description, actual.description)
        assertEquals(metadata.updated, actual.updated)
    }
}

internal fun randomString() = RandomStringUtils.random(5, "abcdefgh")

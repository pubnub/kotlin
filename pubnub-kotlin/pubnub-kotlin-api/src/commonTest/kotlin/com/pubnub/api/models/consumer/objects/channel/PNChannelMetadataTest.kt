package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.utils.PatchValue
import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals

class PNChannelMetadataTest {
    @Test
    fun plus() {
        val originalMetadata = PNChannelMetadata(
            randomString(),
            name = PatchValue.of(randomString()),
            description = PatchValue.of(randomString()),
            custom = PatchValue.of(mapOf(randomString() to randomString())),
            updated = PatchValue.of(randomString()),
            eTag = PatchValue.of(randomString()),
            type = PatchValue.of(randomString()),
            status = PatchValue.of(randomString())
        )
        val updateMetadata = PNChannelMetadata(
            originalMetadata.id,
            name = PatchValue.of(randomString()),
            description = PatchValue.of(randomString()),
            custom = PatchValue.of(null),
        )

        val resultingMetadata = originalMetadata + updateMetadata

        val expectedMetadata = PNChannelMetadata(
            originalMetadata.id,
            name = updateMetadata.name,
            description = updateMetadata.description,
            custom = updateMetadata.custom,
            updated = originalMetadata.updated,
            type = originalMetadata.type,
            status = originalMetadata.status,
            eTag = originalMetadata.eTag,
        )

        assertEquals(expectedMetadata, resultingMetadata)
    }
}

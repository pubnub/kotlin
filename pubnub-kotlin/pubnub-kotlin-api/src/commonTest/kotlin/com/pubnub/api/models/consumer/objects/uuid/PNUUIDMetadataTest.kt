package com.pubnub.api.models.consumer.objects.uuid

import com.pubnub.api.utils.PatchValue
import com.pubnub.test.randomString
import kotlin.test.Test
import kotlin.test.assertEquals

class PNUUIDMetadataTest {
    @Test
    fun plus() {
        val originalMetadata = PNUUIDMetadata(
            randomString(),
            name = PatchValue.of(randomString()),
            externalId = PatchValue.of(randomString()),
            profileUrl = PatchValue.of(randomString()),
            email = PatchValue.of(randomString()),
            custom = PatchValue.of(mapOf(randomString() to randomString())),
            updated = PatchValue.of(randomString()),
            eTag = PatchValue.of(randomString()),
            type = PatchValue.of(randomString()),
            status = PatchValue.of(randomString())
        )
        val updateMetadata = PNUUIDMetadata(
            originalMetadata.id,
            name = PatchValue.of(randomString()),
            updated = PatchValue.of(randomString()),
            custom = PatchValue.of(null),
        )

        val resultingMetadata = originalMetadata + updateMetadata

        val expectedMetadata = PNUUIDMetadata(
            originalMetadata.id,
            updateMetadata.name,
            originalMetadata.externalId,
            originalMetadata.profileUrl,
            originalMetadata.email,
            updateMetadata.custom,
            updateMetadata.updated,
            originalMetadata.eTag,
            originalMetadata.type,
            originalMetadata.status
        )

        assertEquals(expectedMetadata, resultingMetadata)
    }
}

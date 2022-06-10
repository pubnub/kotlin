package com.pubnub.api.models.consumer.objects.channel

import com.pubnub.api.utils.OptionalChange
import org.junit.Assert.assertEquals
import org.junit.Test

class PNChannelMetadataChangeTest {
    private val id = "id"
    private val updated = "updated"
    private val eTag = "eTag"
    private val name = "name"
    private val description = "description"
    private val type = "type"
    private val status = "status"
    private val custom = mapOf("custom" to "Value")

    @Test
    fun patchCanResetFields() {
        val channelMetadata = PNChannelMetadata(
            id = id,
            name = name,
            description = description,
            custom = custom,
            updated = updated,
            eTag = eTag,
            type = type,
            status = status
        )

        val change = PNChannelMetadataChange(
            id = id,
            updated = updated,
            eTag = eTag,
            customChange = OptionalChange.Removed(),
            nameChange = OptionalChange.Removed(),
            descriptionChange = OptionalChange.Removed(),
            statusChange = OptionalChange.Removed(),
            typeChange = OptionalChange.Removed(),
        )

        val patched = change.patch(channelMetadata)

        assertEquals(
            PNChannelMetadata(
                id = id,
                name = null,
                description = null,
                custom = null,
                updated = updated,
                eTag = eTag,
                type = null,
                status = null
            ), patched
        )
    }

    @Test
    fun patchCanSetFields() {
        val channelMetadata = PNChannelMetadata(
            id = id,
            name = null,
            description = null,
            custom = null,
            updated = updated,
            eTag = eTag,
            type = null,
            status = null
        )

        val change = PNChannelMetadataChange(
            id = id,
            updated = updated,
            eTag = eTag,
            customChange = OptionalChange.Changed(custom),
            nameChange = OptionalChange.Changed(name),
            descriptionChange = OptionalChange.Changed(description),
            statusChange = OptionalChange.Changed(status),
            typeChange = OptionalChange.Changed(type),
        )

        val patched = change.patch(channelMetadata)

        assertEquals(
            PNChannelMetadata(
                id = id,
                name = name,
                description = description,
                custom = custom,
                updated = updated,
                eTag = eTag,
                type = type,
                status = status
            ), patched
        )
    }
}
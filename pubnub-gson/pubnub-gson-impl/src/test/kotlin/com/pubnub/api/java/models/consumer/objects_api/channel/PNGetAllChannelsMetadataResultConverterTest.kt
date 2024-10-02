package com.pubnub.api.java.models.consumer.objects_api.channel

import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNGetAllChannelsMetadataResultConverterTest {
    @Test
    fun from() {
        val expected = PNChannelMetadataArrayResult(
            200,
            listOf(),
            123,
            PNPage.PNNext(randomString()),
            PNPage.PNPrev(
                randomString()
            )
        )
        val actual = PNGetAllChannelsMetadataResultConverter.from(expected)

        assertEquals(actual?.status, expected.status)
        assertEquals(actual?.data, expected.data)
        assertEquals(actual?.totalCount, expected.totalCount)
        assertEquals(actual?.next, expected.next?.pageHash)
        assertEquals(actual?.prev, expected.prev?.pageHash)
    }
}

package com.pubnub.api.java.models.consumer.objects_api.channel

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNGetAllUUIDMetadataResultConverter
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNGetAllMetadataResultConverterTest {
    @Test
    fun from() {
        val expected = PNUUIDMetadataArrayResult(
            200,
            listOf(),
            123,
            PNPage.PNNext(randomString()),
            PNPage.PNPrev(
                randomString()
            )
        )
        val actual = PNGetAllUUIDMetadataResultConverter.from(expected)

        assertEquals(actual?.status, expected.status)
        assertEquals(actual?.data, expected.data)
        assertEquals(actual?.totalCount, expected.totalCount)
        assertEquals(actual?.next, expected.next?.pageHash)
        assertEquals(actual?.prev, expected.prev?.pageHash)
    }
}

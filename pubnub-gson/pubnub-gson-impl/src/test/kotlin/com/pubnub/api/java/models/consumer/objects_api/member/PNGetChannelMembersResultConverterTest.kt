package com.pubnub.api.java.models.consumer.objects_api.member

import com.pubnub.api.java.models.consumer.objects_api.channel.randomString
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PNGetChannelMembersResultConverterTest {
    @Test
    fun from() {
        val expected = PNMemberArrayResult(
            200,
            listOf(),
            100,
            PNPage.PNNext(randomString()),
            PNPage.PNPrev(randomString())
        )
        val actual = PNGetChannelMembersResultConverter.from(expected)

        assertEquals(actual?.status, expected.status)
        assertEquals(actual?.data, expected.data)
        assertEquals(actual?.totalCount, expected.totalCount)
        assertEquals(actual?.next, expected.next?.pageHash)
        assertEquals(actual?.prev, expected.prev?.pageHash)
    }
}

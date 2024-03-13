package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.internal.PubNubCore
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DeleteMessagesImplTest {
    private lateinit var objectUnderTest: DeleteMessagesImpl

    private val pubNubCore: PubNubCore = mockk(relaxed = true)
    private val channels: List<String> = listOf("Channel01")
    private val start: Long = 123456789L
    private val end: Long = 987654321L

    @Test
    fun createDeleteMessageImplActionShouldGetAllNecessaryParams() {
        // given
        objectUnderTest = DeleteMessagesImpl(pubNubCore)
        objectUnderTest.channels(channels)
        objectUnderTest.start(start)
        objectUnderTest.end(end)

        // when
        val action = objectUnderTest.createAction()

        // then
        verify { pubNubCore.deleteMessages(channels, start, end) }
        assertTrue(action is ExtendedRemoteAction<PNDeleteMessagesResult>)
    }
}

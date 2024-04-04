package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.internal.PubNubCore
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FetchMessagesImplTest {
    private lateinit var objectUnderTest: FetchMessagesImpl

    private val pubNubCore: PubNubCore = mockk(relaxed = true)
    private val channels: List<String> = listOf("Channel01")
    private val maximumPerChannel: Int = 2
    private val start: Long = 123
    private val end: Long = 321
    private val includeMeta = false
    private val includeMessageActions = false
    private val includeMessageType = true
    private val includeUUID = true

    @Test
    fun createFetchMessagesImplActionShouldGetAllNecessaryParams() {
        // given
        objectUnderTest = FetchMessagesImpl(pubNubCore)
        objectUnderTest.channels(channels)
        objectUnderTest.maximumPerChannel(maximumPerChannel)
        objectUnderTest.start(start)
        objectUnderTest.end(end)
        objectUnderTest.includeMeta(includeMeta)
        objectUnderTest.includeMessageActions(includeMessageActions)
        objectUnderTest.includeMessageType(includeMessageType)
        objectUnderTest.includeUUID(includeUUID)

        // when
        val action = objectUnderTest.createAction()

        // then
        verify {
            pubNubCore.fetchMessages(
                channels,
                PNBoundedPage(start, end, maximumPerChannel),
                includeUUID,
                includeMeta,
                includeMessageActions,
                includeMessageType,
            )
        }
        assertTrue(action is ExtendedRemoteAction<PNFetchMessagesResult>)
    }
}

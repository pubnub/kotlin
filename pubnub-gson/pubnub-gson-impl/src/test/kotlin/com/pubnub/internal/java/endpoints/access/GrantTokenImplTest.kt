package com.pubnub.internal.java.endpoints.access

import com.pubnub.api.PubNub
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.internal.endpoints.access.GrantTokenEndpoint
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GrantTokenImplTest {
    private lateinit var objectUnderTest: GrantTokenImpl

    private val pubNubCore: PubNub = mockk()
    private val grantTokenEndpoint: GrantTokenEndpoint = mockk()
    private val ttl: Int = 123
    private val meta: Any? = null
    private val authorizedUUID: String? = "myUUID"
    private val channels = listOf(ChannelGrant.name("myChannel01").delete(), ChannelGrant.name("myChannel02").manage())
    private val channelGroups = listOf(ChannelGroupGrant.pattern("myChannelGroup01").manage())
    private val uuids = listOf(UUIDGrant.id("myUUID").update())
    private val channelsCapture: CapturingSlot<List<com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant>> =
        slot()
    private val channelGroupsCapture: CapturingSlot<List<com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant>> =
        slot()
    private val uuidsCapture: CapturingSlot<List<com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant>> =
        slot()

    @Test
    fun createGrantTokenImplActionShouldGetAllNecessaryParams() {
        // given
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
        objectUnderTest.meta(meta)
        objectUnderTest.authorizedUUID(authorizedUUID)
        objectUnderTest.channels(channels)
        objectUnderTest.channelGroups(channelGroups)
        objectUnderTest.uuids(uuids)
        every {
            pubNubCore.grantToken(
                ttl,
                meta,
                authorizedUUID,
                capture(channelsCapture),
                capture(channelGroupsCapture),
                capture(uuidsCapture),
            )
        } returns grantTokenEndpoint

        // when
        val action = objectUnderTest.createRemoteAction()

        // then
        verify { pubNubCore.grantToken(ttl, meta, authorizedUUID, any(), any(), any()) }
        val capturedChannels: List<com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant> =
            channelsCapture.captured
        val capturedChannelGroups: List<com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant> =
            channelGroupsCapture.captured
        val capturedUUIDs: List<com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant> = uuidsCapture.captured
        assertEquals(2, capturedChannels.size)
        assertEquals(1, capturedChannelGroups.size)
        assertEquals(1, capturedUUIDs.size)
    }
}

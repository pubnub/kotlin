package com.pubnub.internal.java.endpoints.access

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.internal.endpoints.access.GrantTokenEndpoint
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
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
    private val usersCapture: CapturingSlot<List<com.pubnub.api.models.consumer.access_manager.v3.UserGrant>> =
        slot()
    private val dataSyncCapture: CapturingSlot<List<com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType>> =
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

    @Test
    fun dataSyncWithoutUsersRoutesToUsersOverloadSoDataSyncIsNotDropped() {
        // given — a caller supplies `dataSync` but NO `users`. Before the routing fix this fell into the legacy
        // `uuids` overload, which no longer carries `dataSync`, silently dropping the grants. It must now reach the
        // `authorizedUserId`/`users`/`dataSync` overload.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
        objectUnderTest.meta(meta)
        objectUnderTest.authorizedUUID(authorizedUUID)
        objectUnderTest.dataSync(listOf(DataSyncGrant.entity("capy-001").get().update()))
        every {
            pubNubCore.grantToken(
                ttl,
                any<UserId>(),
                meta,
                any(),
                any(),
                capture(usersCapture),
                capture(dataSyncCapture),
            )
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — delegated to overload B with empty users and the non-empty dataSync grant preserved.
        verify { pubNubCore.grantToken(ttl, any<UserId>(), meta, any(), any(), any(), any()) }
        assertTrue(usersCapture.captured.isEmpty())
        assertEquals(1, dataSyncCapture.captured.size)
    }

    @Test
    fun authorizedUserIdGatewayCarriesDataSyncToV4Overload() {
        // given — the fluent v4 entry point: authorizedUserId(...) opens the DataSync world where dataSync(...) lives.
        // The authorized principal must reach the v4 overload as a UserId built from the same value.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        val authorizedUser = "myUUID"
        val authorizedUserIdCapture: CapturingSlot<UserId> = slot()
        objectUnderTest.ttl(ttl)
            .authorizedUserId(UserId(authorizedUser))
            .dataSync(listOf(DataSyncGrant.entity("capy-001").get().update()))
        every {
            pubNubCore.grantToken(
                ttl,
                capture(authorizedUserIdCapture),
                meta,
                any(),
                any(),
                capture(usersCapture),
                capture(dataSyncCapture),
            )
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — routed to the v4 overload with the authorized user preserved and the dataSync grant carried through.
        verify { pubNubCore.grantToken(ttl, any<UserId>(), meta, any(), any(), any(), any()) }
        assertEquals(authorizedUser, authorizedUserIdCapture.captured.value)
        assertEquals(1, dataSyncCapture.captured.size)
    }

    @Test
    fun authorizedUserIdIsAv4EntryGateOnTheNeutralBuilder() {
        // given — authorizedUserId(...) must be reachable as the FIRST v4-world call directly on the neutral
        // GrantTokenBuilder returned by grantToken(...), mirroring the legacy authorizedUUID(...) gate. Binding the
        // chain start to the GrantTokenBuilder interface type (not the impl) guards the interface method: removing it
        // breaks compilation of this test.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        val entry: GrantTokenBuilder = objectUnderTest
        val authorizedUser = "myUUID"
        val authorizedUserIdCapture: CapturingSlot<UserId> = slot()
        entry.authorizedUserId(UserId(authorizedUser))
            .dataSync(listOf(DataSyncGrant.entity("capy-001").get().update()))
            .ttl(ttl)
        every {
            pubNubCore.grantToken(
                ttl,
                capture(authorizedUserIdCapture),
                meta,
                any(),
                any(),
                capture(usersCapture),
                capture(dataSyncCapture),
            )
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — the gate enters the v4 world and the authorized principal reaches the v4 overload.
        verify { pubNubCore.grantToken(ttl, any<UserId>(), meta, any(), any(), any(), any()) }
        assertEquals(authorizedUser, authorizedUserIdCapture.captured.value)
        assertEquals(1, dataSyncCapture.captured.size)
    }

    @Test
    fun combiningUuidsWithDataSyncThrows() {
        // given — the legacy `uuids` bucket can not be mixed with the new `dataSync` bucket.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
        objectUnderTest.uuids(uuids)
        objectUnderTest.dataSync(listOf(DataSyncGrant.entity("capy-001").get()))

        // when / then
        assertThrows(PubNubException::class.java) { objectUnderTest.sync() }
    }
}

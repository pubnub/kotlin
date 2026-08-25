package com.pubnub.internal.java.endpoints.access

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.java.models.consumer.access_manager.v3.UserGrant
import com.pubnub.internal.endpoints.access.GrantTokenEndpoint
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
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
    private val grantsCapture: CapturingSlot<List<com.pubnub.api.models.consumer.access_manager.v3.TokenGrant>> =
        slot()

    @Test
    fun createGrantTokenImplActionShouldGetAllNecessaryParams() {
        // given — the legacy `uuids` path is untouched and still delegates to the legacy kotlin overload.
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
        objectUnderTest.createRemoteAction()

        // then
        verify { pubNubCore.grantToken(ttl, meta, authorizedUUID, any(), any(), any()) }
        assertEquals(2, channelsCapture.captured.size)
        assertEquals(1, channelGroupsCapture.captured.size)
        assertEquals(1, uuidsCapture.captured.size)
    }

    @Test
    fun grantsRouteToTheFlatListOverload() {
        // given — a caller supplies a flat `grants` list. It must reach the new single-list overload, not the legacy
        // `uuids` overload.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
        objectUnderTest.meta(meta)
        objectUnderTest.authorizedUserId(UserId(authorizedUUID!!))
        objectUnderTest.grants(listOf(DataSyncGrant.entity("capy-001").get().update()))
        every {
            pubNubCore.grantToken(
                ttl,
                any<UserId>(),
                meta,
                capture(grantsCapture),
            )
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — delegated to the flat-list overload with the dataSync grant preserved.
        verify { pubNubCore.grantToken(ttl, any<UserId>(), meta, any()) }
        assertEquals(1, grantsCapture.captured.size)
    }

    @Test
    fun authorizedUserIdCarriesToFlatListOverload() {
        // given — authorizedUserId(...) sets the authorized principal, which must reach the flat-list overload as a
        // UserId built from the same value.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        val authorizedUser = "myUUID"
        val authorizedUserIdCapture: CapturingSlot<UserId> = slot()
        objectUnderTest.ttl(ttl)
            .authorizedUserId(UserId(authorizedUser))
            .grants(listOf(DataSyncGrant.entity("capy-001").get().update()))
        every {
            pubNubCore.grantToken(
                ttl,
                capture(authorizedUserIdCapture),
                meta,
                capture(grantsCapture),
            )
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — routed to the flat-list overload with the authorized user preserved and the grant carried through.
        verify { pubNubCore.grantToken(ttl, any<UserId>(), meta, any()) }
        assertEquals(authorizedUser, authorizedUserIdCapture.captured.value)
        assertEquals(1, grantsCapture.captured.size)
    }

    @Test
    fun authorizedUserIdIsAnEntryGateOnTheNeutralBuilder() {
        // given — authorizedUserId(...) and grants(...) must be reachable directly on the neutral GrantTokenBuilder
        // returned by grantToken(...), returning the same neutral builder (no separate type-state world). Binding the
        // chain start to the GrantTokenBuilder interface type guards those interface methods: removing either breaks
        // compilation of this test.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        val entry: GrantTokenBuilder = objectUnderTest
        val authorizedUser = "myUUID"
        val authorizedUserIdCapture: CapturingSlot<UserId> = slot()
        entry.authorizedUserId(UserId(authorizedUser))
            .grants(listOf(DataSyncGrant.entity("capy-001").get().update()))
            .ttl(ttl)
        every {
            pubNubCore.grantToken(
                ttl,
                capture(authorizedUserIdCapture),
                meta,
                capture(grantsCapture),
            )
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — the gate reaches the flat-list overload and the authorized principal is preserved.
        verify { pubNubCore.grantToken(ttl, any<UserId>(), meta, any()) }
        assertEquals(authorizedUser, authorizedUserIdCapture.captured.value)
        assertEquals(1, grantsCapture.captured.size)
    }

    @Test
    fun grantsAreAdditiveWithChannelsAndChannelGroups() {
        // given — channels()/channelGroups() supplied via the shared setters must be prepended to the flat `grants`
        // list (additive semantics), all funneled into the single flat-list overload.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
            .channels(channels)
            .channelGroups(channelGroups)
            .grants(listOf<TokenGrant>(UserGrant.id("user-A").get(), DataSyncGrant.entity("capy-001").get()))
        every {
            pubNubCore.grantToken(ttl, any(), meta, capture(grantsCapture))
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — 2 channels + 1 channelGroup + 2 flat grants = 5 grants forwarded
        verify { pubNubCore.grantToken(ttl, any(), meta, any()) }
        assertEquals(5, grantsCapture.captured.size)
    }

    @Test
    fun combiningUuidsWithGrantsThrows() {
        // given — the legacy `uuids` bucket can not be mixed with the flat `grants` list.
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
        objectUnderTest.uuids(uuids)
        objectUnderTest.grants(listOf(DataSyncGrant.entity("capy-001").get()))

        // when / then
        assertThrows(PubNubException::class.java) { objectUnderTest.sync() }
    }
}

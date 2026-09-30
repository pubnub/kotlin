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
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.models.consumer.access_manager.v3.PNPatternGrant
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
            .grants(listOf<TokenGrant>(DataSyncGrant.user("user-A").get(), DataSyncGrant.entity("capy-001").get()))
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

    @Test
    fun dataSyncChannelUserAndSubscribeGrantsConvertToKotlinEquivalents() {
        // given — Java DataSync channel/user grants (exact + pattern) and subscribe helpers
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
            .grants(
                listOf<TokenGrant>(
                    DataSyncGrant.channel("chat-1").get().update().projection("admin"),
                    DataSyncGrant.channelPattern("chat-.*").get(),
                    DataSyncGrant.user("user-A").get().delete(),
                    DataSyncGrant.userPattern("user-.*").create().projection("admin"),
                    DataSyncGrant.subscribe("chat-1", "admin"),
                    DataSyncGrant.subscribePattern("chat-.*"),
                ),
            )
        every {
            pubNubCore.grantToken(ttl, any(), meta, capture(grantsCapture))
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then — channel/user grants keep their namespace, bits and projection; subscribe becomes a channel read
        val grants = grantsCapture.captured
        assertEquals(6, grants.size)

        val channel = grants[0] as DataSyncGrantType
        assertEquals(DataSyncNamespace.CHANNELS_PROJECTION, channel.namespace)
        assertEquals("chat-1", channel.id)
        assertTrue(channel.get && channel.update && !channel.create && !channel.delete)
        assertEquals("admin", channel.projection)
        assertTrue(channel !is PNPatternGrant)

        val channelPattern = grants[1] as DataSyncGrantType
        assertEquals(DataSyncNamespace.CHANNELS_PROJECTION, channelPattern.namespace)
        assertEquals("chat-.*", channelPattern.id)
        assertTrue(channelPattern is PNPatternGrant)

        val user = grants[2] as DataSyncGrantType
        assertEquals(DataSyncNamespace.USERS_PROJECTION, user.namespace)
        assertTrue(user.get && user.delete)
        assertEquals(null, user.projection)

        val userPattern = grants[3] as DataSyncGrantType
        assertEquals(DataSyncNamespace.USERS_PROJECTION, userPattern.namespace)
        assertTrue(userPattern.create && userPattern is PNPatternGrant)
        assertEquals("admin", userPattern.projection)

        val subscribe = grants[4] as com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
        assertEquals("__admin__chat-1", subscribe.id)
        assertTrue(subscribe.read && !subscribe.get)
        assertTrue(subscribe !is DataSyncGrantType && subscribe !is PNPatternGrant)

        val subscribePattern = grants[5] as com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
        assertEquals("^(?:chat-.*)", subscribePattern.id)
        assertTrue(subscribePattern.read && subscribePattern is PNPatternGrant)
    }

    @Test
    fun projectionOnMembershipGrantThrows() {
        // memberships use the built-in Membership class, which has no named projections
        assertThrows(IllegalStateException::class.java) { DataSyncGrant.membership("user-1:chat-1").projection("admin") }
        assertThrows(IllegalStateException::class.java) { DataSyncGrant.membershipPattern("user-1:.*").projection("admin") }
    }

    @Test
    fun membershipGrantConvertsWithoutProjection() {
        objectUnderTest = GrantTokenImpl(pubNubCore)
        objectUnderTest.ttl(ttl)
            .grants(
                listOf<TokenGrant>(
                    DataSyncGrant.membership("user-1:chat-1").get().delete(),
                    DataSyncGrant.membershipPattern("user-1:.*").get(),
                ),
            )
        every {
            pubNubCore.grantToken(ttl, any(), meta, capture(grantsCapture))
        } returns grantTokenEndpoint

        // when
        objectUnderTest.createRemoteAction()

        // then
        val membership = grantsCapture.captured[0] as DataSyncGrantType
        assertEquals(DataSyncNamespace.MEMBERSHIPS, membership.namespace)
        assertTrue(membership.get && membership.delete)
        assertEquals(null, membership.projection)

        val membershipPattern = grantsCapture.captured[1] as DataSyncGrantType
        assertEquals(DataSyncNamespace.MEMBERSHIPS, membershipPattern.namespace)
        assertTrue(membershipPattern is PNPatternGrant)
        assertEquals(null, membershipPattern.projection)
    }
}

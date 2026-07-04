package com.pubnub.api.integration.pam

import com.pubnub.api.PubNub
import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken.PNResourcePermissions
import com.pubnub.test.CommonUtils
import com.pubnub.test.Keys
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GrantTokenIntegrationTest : BaseIntegrationTest() {
    /**
     * Regression test for a pre-existing permission-decode bug: the SDK encodes [com.pubnub.api.models.TokenBitmask.CREATE]
     * (16) when minting a grant and PAM enforces it correctly, but `PNResourcePermissions(grant: Int)` never decodes the
     * create bit. So a token that was granted `create = true` round-trips lossily through [PubNub.parseToken] — create is
     * silently reported as false. This predates DataSync; DataSync merely surfaces it.
     *
     * This fails until `PNResourcePermissions` exposes `create` and the parser reads bit 16.
     */
    @Test
    fun parseToken_preservesCreatePermission() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val channelName = "createChannel" + CommonUtils.randomChannel()

        // when — grant create (alongside read so the channel resource is non-trivial)
        val token =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                channels = listOf(ChannelGrant.name(name = channelName, read = true, create = true)),
            ).sync().token

        // then — create must survive the grant -> PAM -> parseToken round-trip
        val (_, _, _, _, resources) = pubNubUnderTest.parseToken(token)
        val channelPermissions = resources.channels[channelName]!!

        assertTrue("read should be granted", channelPermissions.read)
        assertTrue("create was granted but the parser dropped it", channelPermissions.create)
    }

    @Test
    fun happyPath_SUM() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val expectedAuthorizedUserId = UserId("authorizedUser01")
        val expectedSpaceIdValue = "mySpace01"
        val expectedSpaceIdPattern = "mySpace.*"
        val expectedUserIdValue = "myUser01"
        val expectedUserIdPattern = "myUser.*"

        // when
        val grantTokenEndpoint =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                authorizedUserId = expectedAuthorizedUserId,
                spacesPermissions =
                    listOf(
                        SpacePermissions.id(spaceId = SpaceId(expectedSpaceIdValue), read = true, delete = true),
                        SpacePermissions.pattern(pattern = expectedSpaceIdPattern, write = true, manage = true),
                    ),
                usersPermissions =
                    listOf(
                        UserPermissions.id(userId = UserId(expectedUserIdValue), delete = true),
                        UserPermissions.pattern(pattern = expectedUserIdPattern, update = true),
                    ),
            )

        val token = grantTokenEndpoint.sync().token

        // then
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), ttl)

        assertEquals(expectedTTL.toLong(), ttl)
        assertEquals(
            PNResourcePermissions(
                read = true,
                delete = true,
            ),
            resources.channels[expectedSpaceIdValue],
        )
        assertEquals(
            PNResourcePermissions(
                write = true,
                manage = true,
            ),
            patterns.channels[expectedSpaceIdPattern],
        )
        assertEquals(PNResourcePermissions(delete = true), resources.uuids[expectedUserIdValue])
        assertEquals(PNResourcePermissions(update = true), patterns.uuids[expectedUserIdPattern])
    }

    @Test
    fun happyPath() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val expectedChannelResourceName = "channelResource"
        val expectedChannelPattern = "channel.*"
        val expectedChannelGroupResourceId = "channelGroup"
        val expectedChannelGroupPattern = "channelGroup.*"

        // when
        val token =
            pubNubUnderTest
                .grantToken(
                    ttl = expectedTTL,
                    channels =
                        listOf(
                            ChannelGrant.name(name = expectedChannelResourceName, delete = true),
                            ChannelGrant.pattern(pattern = expectedChannelPattern, write = true),
                        ),
                    channelGroups =
                        listOf(
                            ChannelGroupGrant.id(id = expectedChannelGroupResourceId, read = true),
                            ChannelGroupGrant.pattern(pattern = expectedChannelGroupPattern, manage = true),
                        ),
                )
                .sync()
                .token
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)

        println(pubNubUnderTest.parseToken(token))
        // then
        assertEquals(expectedTTL.toLong(), ttl)
        assertEquals(
            PNResourcePermissions(
                delete = true,
            ),
            resources.channels[expectedChannelResourceName],
        )
        assertEquals(
            PNResourcePermissions(
                read = true,
            ),
            resources.channelGroups[expectedChannelGroupResourceId],
        )
        assertEquals(
            PNResourcePermissions(
                write = true,
            ),
            patterns.channels[expectedChannelPattern],
        )
        assertEquals(
            PNResourcePermissions(
                manage = true,
            ),
            patterns.channelGroups[expectedChannelGroupPattern],
        )
    }

    @Test
    fun happyPath_datasync() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val entityName = "capy-001"
        val membershipName = "user-123:channel-X"

        // when — mint a token carrying a mix of entity/relationship/membership grants, exact + pattern
        val token =
            pubNubUnderTest
                .grantToken(
                    ttl = expectedTTL,
                    authorizedUUID = "pam-debug-admin",
                    datasync =
                        listOf(
                            DataSyncGrant.entity(entityName, get = true, update = true),
                            DataSyncGrant.entityPattern(".*", get = true),
                            DataSyncGrant.relationshipPattern(".*", get = true),
                            DataSyncGrant.membership(membershipName, get = true),
                        ),
                )
                .sync()
                .token

        // then
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), ttl)

        assertEquals(
            PNResourcePermissions(get = true, update = true),
            resources.datasyncEntities[entityName],
        )
        assertEquals(
            PNResourcePermissions(get = true),
            resources.datasyncMemberships[membershipName],
        )
        assertEquals(
            PNResourcePermissions(get = true),
            patterns.datasyncEntities[".*"],
        )
        assertEquals(
            PNResourcePermissions(get = true),
            patterns.datasyncRelationships[".*"],
        )
    }

    @Test
    fun canReadGroupsInChannelGroupWhenPamEnabled() {
        val expectedTTL = 1337
        val channelGroupName = "channelGroup" + CommonUtils.randomChannel()
        val channel01 = "channel" + CommonUtils.randomChannel()
        val channel02 = "channel" + CommonUtils.randomChannel()
        val channel03 = "channel" + CommonUtils.randomChannel()

        // create token
        val token =
            server.grantToken(
                ttl = expectedTTL,
                channelGroups =
                    listOf(
                        ChannelGroupGrant.id(id = channelGroupName, read = true, manage = true),
                    ),
            ).sync().token

        // create pubnub instance with PAM enabled but not server(secretKey is not configured)
        val pubNubTest = createPubNub {
            userId = UserId(PubNub.generateUUID())
            subscribeKey = Keys.pamSubKey
            publishKey = Keys.pamPubKey
            logVerbosity = PNLogVerbosity.NONE
        }

        // setToken
        pubNubTest.setToken(token)

        // add channels to channelGroup
        pubNubTest.addChannelsToChannelGroup(
            channelGroup = channelGroupName,
            channels = listOf(channel01, channel02, channel03),
        ).sync()

        // get channels in channelGroup
        val channelsInChannelGroup = pubNubTest.listChannelsForChannelGroup(channelGroup = channelGroupName).sync()

        channelsInChannelGroup?.channels?.let { channels ->
            Assert.assertTrue(channels.contains(channel01))
            Assert.assertTrue(channels.contains(channel02))
            Assert.assertTrue(channels.contains(channel03))
            assertEquals(3, channels.size)
        }
    }
}

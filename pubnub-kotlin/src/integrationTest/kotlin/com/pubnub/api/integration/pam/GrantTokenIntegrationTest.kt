package com.pubnub.api.integration.pam

import com.pubnub.api.CommonUtils
import com.pubnub.api.Keys
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken.PNResourcePermissions
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class GrantTokenIntegrationTest : BaseIntegrationTest() {
    private val pubNubUnderTest: PubNub = server

    @Test
    fun happyPath_SUM() {
        // given
        pubNubUnderTest.configuration.logVerbosity = PNLogVerbosity.BODY
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
        pubNubUnderTest.configuration.logVerbosity = PNLogVerbosity.BODY
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
        val pnConfiguration =
            PNConfiguration(userId = UserId(PubNub.generateUUID())).apply {
                subscribeKey = Keys.pamSubKey
                publishKey = Keys.pamPubKey
                logVerbosity = PNLogVerbosity.NONE
                httpLoggingInterceptor = CommonUtils.createInterceptor(logger)
            }
        val pubNubTest = createPubNub(pnConfiguration)

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

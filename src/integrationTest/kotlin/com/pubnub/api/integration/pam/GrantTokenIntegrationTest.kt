package com.pubnub.api.integration.pam

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken.PNResourcePermissions
import org.junit.Assert
import org.junit.Test

class GrantTokenIntegrationTest : BaseIntegrationTest() {
    private val pubNubUnderTest: PubNub = server

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
        val token = pubNubUnderTest
            .grantToken(
                ttl = expectedTTL,
                channels = listOf(
                    ChannelGrant.name(name = expectedChannelResourceName, delete = true),
                    ChannelGrant.pattern(pattern = expectedChannelPattern, write = true)
                ),
                channelGroups = listOf(
                    ChannelGroupGrant.id(id = expectedChannelGroupResourceId, read = true),
                    ChannelGroupGrant.pattern(pattern = expectedChannelGroupPattern, manage = true)
                )
            )
            .sync()!!
            .token
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)

        println(pubNubUnderTest.parseToken(token))
        // then
        Assert.assertEquals(expectedTTL.toLong(), ttl)
        Assert.assertEquals(
            PNResourcePermissions(
                delete = true
            ),
            resources.channels[expectedChannelResourceName]
        )
        Assert.assertEquals(
            PNResourcePermissions(
                read = true
            ),
            resources.channelGroups[expectedChannelGroupResourceId]
        )
        Assert.assertEquals(
            PNResourcePermissions(
                write = true
            ),
            patterns.channels[expectedChannelPattern]
        )
        Assert.assertEquals(
            PNResourcePermissions(
                manage = true
            ),
            patterns.channelGroups[expectedChannelGroupPattern]
        )
    }
}

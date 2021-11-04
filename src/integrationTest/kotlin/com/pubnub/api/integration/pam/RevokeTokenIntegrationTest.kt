package com.pubnub.api.integration.pam

import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import org.junit.Test

class RevokeTokenIntegrationTest : BaseIntegrationTest() {
    private val pubNubUnderTest: PubNub = server

    @Test
    fun happyPath() {
        // given
        pubNubUnderTest.configuration.logVerbosity = PNLogVerbosity.BODY
        val tokenToRevoke = grantToken()

        // when
        pubNubUnderTest.revokeToken(token = tokenToRevoke).sync()

        // then
    }

    private fun grantToken(): String {
        val expectedTTL = 1337
        val expectedChannelResourceName = "channelResource"

        return pubNubUnderTest
            .grantToken(
                ttl = expectedTTL,
                channels = listOf(
                    ChannelGrant.name(name = expectedChannelResourceName, delete = true)
                )
            )
            .sync()!!
            .token
    }
}

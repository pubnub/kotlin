package com.pubnub.kmp

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.v2.createPNConfiguration
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GrantTokenCategoriesTest {
    private lateinit var pubnub: PubNub

    @BeforeTest
    fun before() {
        pubnub = createPubNub(createPNConfiguration(UserId("grant-token-categories-test"), "demo", "demo", authToken = null))
    }

    @AfterTest
    fun after() {
        pubnub.destroy()
    }

    @Test
    fun getAllChannelsIsNotSupportedOnJs() {
        assertFailsWith<PubNubException> {
            pubnub.grantToken(ttl = 60, channels = listOf(ChannelGrant.name("ch-1", read = true)), getAllChannels = true)
        }
    }

    @Test
    fun getAllUUIDsIsNotSupportedOnJs() {
        assertFailsWith<PubNubException> {
            pubnub.grantToken(ttl = 60, channels = listOf(ChannelGrant.name("ch-1", read = true)), getAllUUIDs = true)
        }
    }
}

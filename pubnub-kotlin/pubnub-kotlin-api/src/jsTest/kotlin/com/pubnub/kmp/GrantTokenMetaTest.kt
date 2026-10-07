package com.pubnub.kmp

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.v2.createPNConfiguration
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GrantTokenMetaTest {
    private lateinit var pubnub: PubNub

    private val metaWithProjections =
        CustomObject(mapOf(DataSyncNamespace.PN_PROJECTIONS to mapOf("res" to mapOf("datasync:entities:e1" to "admin"))))

    @BeforeTest
    fun before() {
        pubnub = createPubNub(createPNConfiguration(UserId("grant-token-meta-test"), "demo", "demo", authToken = null))
    }

    @AfterTest
    fun after() {
        pubnub.destroy()
    }

    @Test
    fun flatOverloadRejectsPnProjectionsInMeta() {
        val exception =
            assertFailsWith<PubNubException> {
                pubnub.grantToken(
                    ttl = 60,
                    meta = metaWithProjections,
                    grants = listOf(ChannelGrant.name("ch-1", read = true)),
                )
            }
        assertTrue(exception.message!!.contains(DataSyncNamespace.PN_PROJECTIONS))
    }

    @Test
    fun flatOverloadRejectsPnProjectionsInMetaAlongsideProjectionGrant() {
        val exception =
            assertFailsWith<PubNubException> {
                pubnub.grantToken(
                    ttl = 60,
                    meta = metaWithProjections,
                    grants = listOf(DataSyncGrant.entity("e1", get = true, projection = "admin")),
                )
            }
        assertTrue(exception.message!!.contains(DataSyncNamespace.PN_PROJECTIONS))
    }

    @Test
    fun legacyOverloadRejectsPnProjectionsInMeta() {
        val exception =
            assertFailsWith<PubNubException> {
                pubnub.grantToken(
                    ttl = 60,
                    meta = metaWithProjections,
                    channels = listOf(ChannelGrant.name("ch-1", read = true)),
                )
            }
        assertTrue(exception.message!!.contains(DataSyncNamespace.PN_PROJECTIONS))
    }
}

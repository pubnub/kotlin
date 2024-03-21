package com.pubnub.internal.v2

import com.pubnub.api.UserId
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.internal.BasePubNubImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class PNConfigurationImplTest {
    @Test
    fun testDefaultTimeoutValues() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        assertEquals(300, config.presenceTimeout)
        assertEquals(0, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues1() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        config.setPresenceTimeout(100)
        assertEquals(100, config.presenceTimeout)
        assertEquals(49, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues2() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        config.setHeartbeatInterval(100)
        assertEquals(300, config.presenceTimeout)
        assertEquals(100, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues3() {
        val config = PNConfiguration.builder(UserId(BasePubNubImpl.generateUUID()), "demo")
        config.setHeartbeatInterval(40)
        config.setPresenceTimeout(50)
        assertEquals(50, config.presenceTimeout)
        assertEquals(24, config.heartbeatInterval)
    }
}

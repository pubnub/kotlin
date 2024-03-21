package com.pubnub.api

import com.pubnub.internal.BasePubNubImpl
import org.junit.Assert
import org.junit.Test

class PNConfigurationTest {
    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun setUUIDToEmptyString() {
        PNConfiguration("")
    }

    @Suppress("DEPRECATION")
    @Test(expected = PubNubException::class)
    fun resetUUIDToEmptyString() {
        val config = PNConfiguration(BasePubNubImpl.generateUUID())
        config.setUuid("")
    }

    @Suppress("DEPRECATION")
    @Test
    fun resetUUIDToNonEmptyString() {
        val config = PNConfiguration(BasePubNubImpl.generateUUID())
        val newUUID = BasePubNubImpl.generateUUID()
        config.setUuid(newUUID)

        Assert.assertEquals(newUUID, config.userId.value)
    }

    @Test
    fun testDefaultTimeoutValues() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        Assert.assertEquals(300, config.presenceTimeout)
        Assert.assertEquals(0, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues1() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.setPresenceTimeout(100)
        Assert.assertEquals(100, config.presenceTimeout)
        Assert.assertEquals(49, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues2() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.setHeartbeatInterval(100)
        Assert.assertEquals(300, config.presenceTimeout)
        Assert.assertEquals(100, config.heartbeatInterval)
    }

    @Test
    fun testCustomTimeoutValues3() {
        val config = PNConfiguration(UserId(BasePubNubImpl.generateUUID()))
        config.setHeartbeatInterval(40)
        config.setPresenceTimeout(50)
        Assert.assertEquals(50, config.presenceTimeout)
        Assert.assertEquals(24, config.heartbeatInterval)
    }
}

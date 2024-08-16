package com.pubnub.api

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.v2.PNConfigurationImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class PNConfigurationImplTest {
    @Test
    fun testDefaultTimeoutValues() {
        val p = PubNubImpl(PNConfigurationImpl(userId = UserId(PubNubImpl.generateUUID())))
        assertEquals(300, p.configuration.presenceTimeout)
        assertEquals(0, p.configuration.heartbeatInterval)
        p.forceDestroy()
    }
}

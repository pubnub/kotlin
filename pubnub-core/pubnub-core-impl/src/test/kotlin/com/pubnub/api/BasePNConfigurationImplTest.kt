package com.pubnub.api

import com.pubnub.internal.BasePubNubImpl
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.BasePNConfigurationImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class BasePNConfigurationImplTest {
    @Test
    fun testDefaultTimeoutValues() {
        val p = TestPubNub(BasePNConfigurationImpl(userId = UserId(BasePubNubImpl.generateUUID())))
        assertEquals(300, p.pubNubCore.configuration.presenceTimeout)
        assertEquals(0, p.pubNubCore.configuration.heartbeatInterval)
        p.forceDestroy()
    }
}

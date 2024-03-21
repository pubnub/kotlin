package com.pubnub.api

import com.pubnub.internal.BasePubNubImpl
import com.pubnub.internal.TestPubNub
import com.pubnub.internal.v2.BasePNConfigurationImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class BasePNConfigurationImplTest {
    @Suppress("DEPRECATION")
    @Test
    fun pnConfigurationGeneratesPnsdkWithSuffixes() {
        val name1 = "key1"
        val suffix1 = "value1/1.0.0"
        val name2 = "key2"
        val suffix2 = "value2/2.0.0"
        val suffix11 = "value3/2.0.0"

        val pnConfiguration =
            object : BasePNConfigurationImpl(userId = UserId(BasePubNubImpl.generateUUID())) {
                override val pnsdkSuffixes: Map<String, String>
                    get() = buildMap {
                        put(name1, suffix1)
                        put(name2, suffix2)
                        put(name1, suffix11)
                    }
            }

        val version = "someVersion"
        val generatedPnsdk = pnConfiguration.generatePnsdk(version)
        assertEquals("PubNub-Kotlin/$version $suffix11 $suffix2", generatedPnsdk)
    }

    @Test
    fun testDefaultTimeoutValues() {
        val p = TestPubNub(BasePNConfigurationImpl(userId = UserId(BasePubNubImpl.generateUUID())))
        assertEquals(300, p.pubNubCore.configuration.presenceTimeout)
        assertEquals(0, p.pubNubCore.configuration.heartbeatInterval)
        p.forceDestroy()
    }
}

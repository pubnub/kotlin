package com.pubnub.api

import org.junit.Assert.assertEquals
import org.junit.Test

class PNConfigurationTest {
    @Test
    fun pnConfigurationGeneratesPnsdkWithSuffixes() {
        val name1 = "key1"
        val suffix1 = "value1/1.0.0"
        val name2 = "key2"
        val suffix2 = "value2/2.0.0"
        val suffix11 = "value3/2.0.0"

        val pnConfiguration = PNConfiguration().apply {
            addPnsdkSuffix(name1 to suffix1, name2 to suffix2)
            addPnsdkSuffix(mapOf(name1 to suffix11))
        }
        val version = "someVersion"
        val generatedPnsdk = pnConfiguration.generatePnsdk(version)
        assertEquals("PubNub-Kotlin/$version $suffix11 $suffix2", generatedPnsdk)
    }
}

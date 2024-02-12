package com.pubnub.api.integration

import com.pubnub.api.CommonUtils
import com.pubnub.api.SignatureUtils
import com.pubnub.api.listen
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Ignore
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class SpecialCharsIntegrationTest : BaseIntegrationTest() {

    @Test
    @Ignore //TODO didn't work on master either
    fun testSpecialCharsPathAndUrl() {
        val expectedChannel = CommonUtils.randomChannel()
        val keyName = "special_char"
        val value = CommonUtils.getSpecialCharsMap().map { it.regular }.shuffled().joinToString("")

        server.publish(
            channel = expectedChannel,
            message = value,
            meta = mapOf(
                keyName to value
            )
        ).apply {
            queryParam += mapOf(
                "za" to value,
                "aa" to value,
                "s" to value,
                "Zz" to value,
                "ZZZ" to value,
                "123" to value
            )
        }.sync()

        val messages = server.history(
            channel = expectedChannel,
            includeMeta = true
        ).sync().messages

        assertEquals(1, messages.size)
        assertEquals(value, messages[0].meta!!.asJsonObject[keyName].asString)
        assertEquals(value, messages[0].entry.asString)

    }
}

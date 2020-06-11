package com.pubnub.api.integration

import com.pubnub.api.listen
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

class TimeIntegrationTest : BaseIntegrationTest() {

    @Test
    fun testGetPubNubTime() {
        val success = AtomicBoolean()

        pubnub.time()
            .async { result, status ->
                assertFalse(status.error)
                assertNotNull(result!!.timetoken)
                success.set(true)
            }

        success.listen()
    }

}
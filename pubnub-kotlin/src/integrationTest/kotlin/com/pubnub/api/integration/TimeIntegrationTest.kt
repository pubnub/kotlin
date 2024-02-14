package com.pubnub.api.integration

import com.pubnub.api.listen
import com.pubnub.api.v2.callbacks.getOrThrow
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class TimeIntegrationTest : BaseIntegrationTest() {

    @Test
    fun testGetPubNubTime() {
        val success = AtomicBoolean()

        pubnub.time()
            .async { result ->
                assertFalse(result.isFailure)
                assertNotNull(result.getOrThrow().timetoken)
                success.set(true)
            }
        success.listen()
    }
}

package com.pubnub.api

import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.await
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class TimeTest : BaseIntegrationTest() {

    @Test
    fun time() = runTest {
        val time = pubnub.time().await()
        assertTrue { time.timetoken > 1000000 }
    }
}
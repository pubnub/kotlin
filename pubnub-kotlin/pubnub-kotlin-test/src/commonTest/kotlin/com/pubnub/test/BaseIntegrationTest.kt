package com.pubnub.test

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.kmp.createPubNub
import com.pubnub.api.v2.createPNConfiguration
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

abstract class BaseIntegrationTest {

    lateinit var pubnub: PubNub

    @BeforeTest
    fun before() {
        pubnub = createPubNub(createPNConfiguration(UserId("demo_user"), Keys.subKey, Keys.pubKey))
    }

    @AfterTest
    fun after() {
        pubnub.unsubscribeAll()
        pubnub.destroy()
    }
}

suspend fun <T> Endpoint<T>.await() = suspendCancellableCoroutine { cont ->
    async { result ->
        result.onSuccess {
            cont.resume(it)
        }.onFailure {
            cont.resumeWithException(it)
        }
    }
}
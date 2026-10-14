package com.pubnub.internal

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.retry.RetryConfiguration
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.internal.endpoints.pubsub.SubscribeEndpoint
import com.pubnub.internal.retry.RetryableBase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.milliseconds

/**
 * `silentCancel()` must stop the request, every retry of it, and every late result - success or HTTP error.
 */
class EndpointCoreSilentCancelTest : BaseTest() {
    private val userCallbackInvocations = CopyOnWriteArrayList<Result<Any>>()

    override fun onBefore() {
        super.onBefore()
        @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
        val retryConfiguration =
            RetryConfiguration.Exponential.createForTest(
                minDelayInSec = RETRY_MIN_DELAY_MS.milliseconds,
                maxDelayInSec = RETRY_MIN_DELAY_MS.milliseconds,
                maxRetryNumber = 3,
                // without isInternal the public 2s minimum delay is applied and RETRY_WINDOW_MS is too short
                isInternal = true,
            )
        config.retryConfiguration = retryConfiguration
    }

    // region WireMock

    @Test
    fun silentCancelOfInFlightRequestIsNotRetried() {
        stubFor(
            get(urlMatching("/v2/subscribe/.*"))
                .willReturn(aResponse().withFixedDelay(3_000).withBody(SUBSCRIBE_RESPONSE)),
        )
        val endpoint = SubscribeEndpoint(pubnub).apply { channels = listOf("ch") }
        val invocations = AtomicInteger()

        endpoint.async { invocations.incrementAndGet() }
        awaitSubscribeRequests(1)
        endpoint.silentCancel()
        Thread.sleep(RETRY_WINDOW_MS)

        assertEquals(1, subscribeRequestCount())
        assertEquals(0, invocations.get())
    }

    @Test
    fun silentCancelReachesInFlightRetryClone() {
        stubFor(
            get(urlMatching("/v2/subscribe/.*"))
                .inScenario("retry")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(aResponse().withStatus(500))
                .willSetStateTo("retried"),
        )
        stubFor(
            get(urlMatching("/v2/subscribe/.*"))
                .inScenario("retry")
                .whenScenarioStateIs("retried")
                .willReturn(aResponse().withFixedDelay(LATE_RESPONSE_DELAY_MS.toInt()).withBody(SUBSCRIBE_RESPONSE)),
        )
        val endpoint = SubscribeEndpoint(pubnub).apply { channels = listOf("ch") }
        val invocations = AtomicInteger()

        endpoint.async { invocations.incrementAndGet() }
        awaitSubscribeRequests(2) // the 500 was retried, the clone is in flight
        endpoint.silentCancel()
        // without the fix the untracked clone receives the delayed 200 and delivers it
        Thread.sleep(maxOf(RETRY_WINDOW_MS, LATE_RESPONSE_DELAY_MS + 1_000))

        assertEquals(2, subscribeRequestCount())
        assertEquals(0, invocations.get())
    }

    // endregion

    // region fake Call - late results delivered after silentCancel()

    @Test
    fun lateSuccessAfterSilentCancelIsNotDelivered() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val fakeCall = endpoint.calls.single()

        endpoint.silentCancel()
        fakeCall.callback!!.onResponse(fakeCall, Response.success<Any>(BODY))

        assertEquals(0, userCallbackInvocations.size)
    }

    @Test
    fun lateSuccessOnRetryCloneAfterSilentCancelIsNotDelivered() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val fakeCall = endpoint.calls.single()

        fakeCall.callback!!.onFailure(fakeCall, SocketTimeoutException())
        Awaitility.await().atMost(Durations.FIVE_SECONDS).until { fakeCall.clone.callback != null }
        endpoint.silentCancel()
        fakeCall.clone.callback!!.onResponse(fakeCall.clone, Response.success<Any>(BODY))

        assertTrue(fakeCall.clone.isCanceled)
        assertEquals(0, userCallbackInvocations.size)
    }

    @Test
    fun successWithoutSilentCancelIsDelivered() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val fakeCall = endpoint.calls.single()

        fakeCall.callback!!.onResponse(fakeCall, Response.success<Any>(BODY))

        assertEquals(1, userCallbackInvocations.size)
        assertTrue(userCallbackInvocations.single().isSuccess)
    }

    @Test
    fun lateHttpErrorAfterSilentCancelIsNotDelivered() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val fakeCall = endpoint.calls.single()

        endpoint.silentCancel()
        fakeCall.callback!!.onResponse(fakeCall, errorResponse(403))

        assertEquals(0, userCallbackInvocations.size)
    }

    @Test
    fun lateRetryableHttpErrorAfterSilentCancelIsNeitherRetriedNorDelivered() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val fakeCall = endpoint.calls.single()

        endpoint.silentCancel()
        fakeCall.callback!!.onResponse(fakeCall, errorResponse(500))
        Thread.sleep(RETRY_WINDOW_MS)

        assertEquals(0, fakeCall.clone.enqueueCount.get())
        assertEquals(0, userCallbackInvocations.size)
    }

    @Test
    fun httpErrorWithoutSilentCancelIsDelivered() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val fakeCall = endpoint.calls.single()

        fakeCall.callback!!.onResponse(fakeCall, errorResponse(403))

        assertEquals(1, userCallbackInvocations.size)
        assertTrue(userCallbackInvocations.single().isFailure)
    }

    @Test
    fun silentCancelBeforeAsyncDoesNotSendOrRetry() {
        val endpoint = FakeEndpoint(pubnub, onEnqueue = ::failCanceledOrTimeout)

        endpoint.silentCancel()
        endpoint.async { userCallbackInvocations.add(it) }
        Thread.sleep(RETRY_WINDOW_MS)

        val fakeCall = endpoint.calls.single()
        assertTrue(fakeCall.isCanceled)
        assertEquals("request must not be sent", 0, fakeCall.enqueueCount.get())
        assertEquals(0, fakeCall.clone.enqueueCount.get())
        assertEquals(0, userCallbackInvocations.size)
    }

    @Test
    fun cancelBetweenDoWorkAndCallbackPublishIsNotRetried() {
        val endpoint = FakeEndpoint(pubnub, onEnqueue = ::failCanceledOrTimeout)
        endpoint.cancelWhileCreatingCallback = true

        endpoint.async { userCallbackInvocations.add(it) }
        Thread.sleep(RETRY_WINDOW_MS)

        val fakeCall = endpoint.calls.single()
        assertTrue(fakeCall.isCanceled)
        assertEquals("request must not be sent", 0, fakeCall.enqueueCount.get())
        assertEquals(0, fakeCall.clone.enqueueCount.get())
        assertEquals(0, userCallbackInvocations.size)
    }

    @Test
    fun cancelBetweenDoWorkAndCallbackPublishOnRepeatedAsyncIsNotRetried() {
        val endpoint = FakeEndpoint(pubnub)
        endpoint.async { userCallbackInvocations.add(it) }
        val firstCall = endpoint.calls.single()
        firstCall.callback!!.onResponse(firstCall, Response.success<Any>(BODY))
        assertEquals(1, userCallbackInvocations.size)

        // the previous callback is still published while the second call is being set up
        endpoint.onEnqueue = ::failCanceledOrTimeout
        endpoint.cancelWhileCreatingCallback = true
        endpoint.async { userCallbackInvocations.add(it) }
        Thread.sleep(RETRY_WINDOW_MS)

        val secondCall = endpoint.calls[1]
        assertTrue(secondCall.isCanceled)
        assertEquals("request must not be sent", 0, secondCall.enqueueCount.get())
        assertEquals(0, secondCall.clone.enqueueCount.get())
        assertEquals(1, userCallbackInvocations.size)
    }

    // endregion

    private fun awaitSubscribeRequests(count: Int) {
        Awaitility.await().atMost(Durations.FIVE_SECONDS).until { subscribeRequestCount() >= count }
        // let the request reach the server so the cancel hits an in-flight request
        Thread.sleep(200)
    }

    private fun subscribeRequestCount() = findAll(getRequestedFor(urlMatching("/v2/subscribe/.*"))).size

    private fun errorResponse(code: Int): Response<Any> =
        Response.error(code, """{"error":true}""".toResponseBody("application/json".toMediaType()))

    // a missing cancel lets the call fail with a genuine, retryable error
    private fun failCanceledOrTimeout(call: FakeCall, callback: Callback<Any>) {
        if (call.isCanceled) {
            callback.onFailure(call, IOException("Canceled"))
        } else {
            callback.onFailure(call, SocketTimeoutException())
        }
    }

    private class FakeEndpoint(
        pubnub: PubNubImpl,
        @Volatile var onEnqueue: (FakeCall, Callback<Any>) -> Unit = { _, _ -> },
    ) : EndpointCore<Any, Any>(pubnub) {
        val calls = CopyOnWriteArrayList<FakeCall>()

        // silentCancel() once from isEndpointRetryable(), which runs after `call = doWork()` and before the
        // new RetryableCallback is published
        @Volatile
        var cancelWhileCreatingCallback = false

        override fun doWork(queryParams: HashMap<String, String>): Call<Any> {
            val call = FakeCall { fakeCall, callback -> onEnqueue(fakeCall, callback) }
            calls.add(call)
            return call
        }

        override fun createResponse(input: Response<Any>): Any = input.body()!!

        override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.SUBSCRIBE

        override fun isEndpointRetryable(): Boolean {
            if (cancelWhileCreatingCallback) {
                cancelWhileCreatingCallback = false
                silentCancel()
            }
            return true
        }

        override fun operationType(): PNOperationType {
            TODO("Not yet implemented")
        }
    }

    /** Sends nothing; captures the callback so the test can deliver any result at any time. */
    private class FakeCall(
        private val onEnqueue: (FakeCall, Callback<Any>) -> Unit = { _, _ -> },
    ) : Call<Any> {
        @Volatile
        var callback: Callback<Any>? = null
        val enqueueCount = AtomicInteger()

        @Volatile
        private var canceled = false

        // clone() always returns the same fake so tests can assert on it; the clone never fails by itself
        val clone: FakeCall by lazy { FakeCall() }

        override fun enqueue(callback: Callback<Any>) {
            this.callback = callback
            enqueueCount.incrementAndGet()
            onEnqueue(this, callback)
        }

        override fun execute(): Response<Any> = throw UnsupportedOperationException()

        override fun isExecuted(): Boolean = enqueueCount.get() > 0

        override fun cancel() {
            canceled = true
        }

        override fun isCanceled(): Boolean = canceled

        override fun clone(): Call<Any> = clone

        override fun request(): Request = Request.Builder().url("http://localhost/").build()

        override fun timeout(): Timeout = Timeout.NONE
    }

    private companion object {
        const val RETRY_MIN_DELAY_MS = 100L

        // a negative ("no retry") assertion is only meaningful once the worst-case first retry (min delay + jitter) would have fired
        const val RETRY_WINDOW_MS = RETRY_MIN_DELAY_MS + RetryableBase.MAX_RANDOM_DELAY_IN_MILLIS + 500L
        const val LATE_RESPONSE_DELAY_MS = 2_000L
        const val BODY = "body"
        const val SUBSCRIBE_RESPONSE = """{"t":{"t":"14607577960932487","r":1},"m":[]}"""
    }
}

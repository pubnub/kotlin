package com.pubnub.api.retry

import com.pubnub.api.listen
import com.pubnub.internal.retry.RetryableCallback
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicBoolean

class RetryableCallbackTest : RetryableTestBase() {
    private lateinit var mockCall: Call<Any>
    private lateinit var mockResponse: Response<Any>
    private var onFinalResponseCalled = false
    private var onFinalFailureCalled = false

    @BeforeEach
    fun setUp() {
        mockCall = mockk(relaxed = true)
        mockResponse = mockk(relaxed = true)
    }

    private fun getRetryableCallback(
        retryPolicy: RequestRetryPolicy = RequestRetryPolicy.None,
        onFinalFailureFinished: AtomicBoolean = AtomicBoolean(false),
        onFinalResponseFinished: AtomicBoolean = AtomicBoolean(false),
        endpointGroupName: RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE
    ): RetryableCallback<Any> {
        return object : RetryableCallback<Any>(
            retryPolicy, endpointGroupName, mockCall, isEndpointRetryable = true
        ) {
            override fun onFinalResponse(call: Call<Any>, response: Response<Any>) {
                onFinalResponseCalled = true
                onFinalResponseFinished.set(true)
            }

            override fun onFinalFailure(call: Call<Any>, t: Throwable) {
                onFinalFailureCalled = true
                onFinalFailureFinished.set(true)
            }
        }
    }

    @Test
    fun `should not retry when response is successful`() {
        // given
        val retryableCallback = getRetryableCallback()
        every { mockResponse.isSuccessful } returns true

        // when
        retryableCallback.onResponse(mockCall, mockResponse)

        // then
        assertTrue(onFinalResponseCalled)
    }

    @Test
    fun `should not retry when response is not successful and retryPolicy not defined`() {
        // given
        val retryableCallback = getRetryableCallback()
        val errorResponse: Response<Any> = Response.error<Any>(500, ResponseBody.create(null, ""))

        // when
        retryableCallback.onResponse(mockCall, errorResponse)

        // then
        assertTrue(onFinalResponseCalled)
        verify(exactly = 0) { mockCall.enqueue(retryableCallback) }
        verify(exactly = 0) { mockCall.clone() }
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry when linear retryPolicy is set and SocketTimeoutException`() {
        // given
        val success = AtomicBoolean()
        val retryableCallback =
            getRetryableCallback(retryPolicy = RequestRetryPolicy.Linear(delayInSec = 2, maxRetryNumber = 3), onFinalResponseFinished = success)
        val errorResponse: Response<Any> = Response.error<Any>(500, ResponseBody.create(null, ""))
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 500 // Assuming 500 is a retryable error
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.enqueue(any()) } answers {
            val callback = arg<Callback<Any>>(0)
            callback.onFailure(mockCall, UnknownHostException())
        } andThenAnswer {
            val callback = arg<Callback<Any>>(0)
            callback.onFailure(mockCall, UnknownHostException())
        } andThenAnswer {
            val callback = arg<Callback<Any>>(0)
            callback.onResponse(mockCall, successfulResponse)
        }
        every { mockCall.clone() } returns mockCall

        // when
        retryableCallback.onResponse(mockCall, errorResponse)
        success.listen(10)

        // then
        verify(exactly = 3) { mockCall.enqueue(retryableCallback) }
        verify(exactly = 3) { mockCall.clone() }
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry onResponse when exponential retryPolicy is set and retryable error 500 occurs`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 3,
            maxRetryNumber = 2,
        )
        val success = AtomicBoolean()
        val retryableCallback = getRetryableCallback(retryPolicy = retryPolicy, onFinalResponseFinished = success)
        val errorResponse: Response<Any> = Response.error<Any>(500, ResponseBody.create(null, ""))
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 500 // Assuming 500 is a retryable error
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.enqueue(any()) } answers {
            val callback = arg<Callback<Any>>(0)
            callback.onResponse(mockCall, errorResponse)
        } andThenAnswer {
            val callback = arg<Callback<Any>>(0)
            callback.onResponse(mockCall, successfulResponse)
        }
        every { mockCall.clone() } returns mockCall

        // when
        retryableCallback.onResponse(mockCall, errorResponse)
        success.listen(10)

        // then
        verify(exactly = 2) { mockCall.enqueue(retryableCallback) }
        verify(exactly = 2) { mockCall.clone() }
        assertTrue(onFinalResponseCalled)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry onFailure when exponential retryPolicy is set and SocketTimeoutException`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 3,
            maxRetryNumber = 2,
        )
        val success = AtomicBoolean()
        val retryableCallback = getRetryableCallback(retryPolicy = retryPolicy, onFinalResponseFinished = success)
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 500 // Assuming 500 is a retryable error
        val successfulResponse: Response<Any> = Response.success(null)
        every { mockCall.enqueue(any()) } answers {
            val callback = arg<Callback<Any>>(0)
            callback.onFailure(mockCall, SocketTimeoutException())
        } andThenAnswer {
            val callback = arg<Callback<Any>>(0)
            callback.onResponse(mockCall, successfulResponse)
        }
        every { mockCall.clone() } returns mockCall

        // when
        retryableCallback.onFailure(mockCall, SocketTimeoutException())
        success.listen(10)

        // then
        verify(exactly = 2) { mockCall.enqueue(retryableCallback) }
        verify(exactly = 2) { mockCall.clone() }
        assertTrue(onFinalResponseCalled)
    }

    @Test
    @EnabledIf("enableLongRunningRetryTests")
    fun `should retry onFailure and fail when exponential retryPolicy is set and UnknownHostException`() {
        // given
        val retryPolicy = RequestRetryPolicy.Exponential(
            minDelayInSec = 2,
            maxDelayInSec = 3,
            maxRetryNumber = 2,
        )
        val success = AtomicBoolean()
        val retryableCallback = getRetryableCallback(retryPolicy = retryPolicy, onFinalFailureFinished = success)
        every { mockResponse.isSuccessful } returns false
        every { mockResponse.code() } returns 500 // Assuming 500 is a retryable error
        every { mockCall.enqueue(any()) } answers {
            val callback = arg<Callback<Any>>(0)
            callback.onFailure(mockCall, UnknownHostException())
        } andThenAnswer {
            val callback = arg<Callback<Any>>(0)
            callback.onFailure(mockCall, UnknownHostException())
        } andThenAnswer {
            val callback = arg<Callback<Any>>(0)
            callback.onFailure(mockCall, UnknownHostException())
        }
        every { mockCall.clone() } returns mockCall

        // when
        retryableCallback.onFailure(mockCall, UnknownHostException())
        success.listen(10)

        // then
        verify(exactly = 2) { mockCall.enqueue(retryableCallback) }
        verify(exactly = 2) { mockCall.clone() }
        assertTrue(onFinalFailureCalled)
    }
}

package com.pubnub.api.legacy.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import okhttp3.Request
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class EndpointTest : BaseTest() {

    @Test
    fun testDefaultInstanceParamSetting() {
        assertTrue(pubnub.configuration.includeRequestIdentifier)
        assertFalse(pubnub.configuration.includeInstanceIdentifier)
    }

    // todo test other params

    @Test
    fun testBaseParamsIncludeInstanceId() {
        pubnub.configuration.includeInstanceIdentifier = true

        fakeEndpoint {
            assertTrue(it.containsKey("instanceid"))
        }.sync()
    }

    @Test
    fun testBaseParamsNoIncludeInstanceId() {
        fakeEndpoint {
            assertEquals("myUUID", it["uuid"])
            assertFalse(it.containsKey("instanceid"))
        }.sync()
    }

    @Test
    fun testBaseParamsIncludeRequestId() {
        fakeEndpoint {
            assertEquals("myUUID", it["uuid"])
            assertTrue(it.containsKey("requestid"))
        }.sync()
    }

    @Test
    fun testBaseParamsNoIncludeRequestId() {
        pubnub.configuration.includeRequestIdentifier = false

        fakeEndpoint {
            assertEquals("myUUID", it["uuid"])
            assertFalse(it.containsKey("requestid"))
        }.sync()
    }

    @Test
    fun testBaseParamsPersistentRequestId() {
        pubnub.configuration.includeInstanceIdentifier = true

        val instanceId1 = AtomicReference<String>()
        val instanceId2 = AtomicReference<String>()

        fakeEndpoint {
            instanceId1.set(it["instanceid"])
        }.sync()

        fakeEndpoint {
            instanceId2.set(it["instanceid"])
        }.sync()

        assertEquals(pubnub.instanceId, instanceId1.get())
        assertEquals(instanceId1.get(), instanceId2.get())
    }

    @Test
    fun testUuid() {
        val expectedUuid = UUID.randomUUID().toString()
        pubnub.configuration.uuid = expectedUuid

        fakeEndpoint {
            println(it)
            assertEquals(expectedUuid, it["uuid"])
        }.sync()
    }

    @Test
    fun testQueryParam() {
        fakeEndpoint {
            println(it)
            assertEquals("sf", it["city"])
            assertEquals(pubnub.configuration.uuid, it["uuid"])
            assertEquals(4, it.size)
            assertTrue(it.contains("pnsdk"))
            assertTrue(it.contains("requestid"))
            assertTrue(it.contains("uuid"))
        }.apply {
            queryParam = mapOf(
                "city" to "sf",
                "uuid" to "overwritten"
            )
        }.sync()
    }

    @Test
    fun testQueryParamEmpty() {
        fakeEndpoint {
            assertEquals(3, it.size)
            assertTrue(it.contains("pnsdk"))
            assertTrue(it.contains("requestid"))
            assertTrue(it.contains("uuid"))
        }.apply {
            queryParam = emptyMap()
        }.sync()
    }

    @Test
    fun testQueryParamMissing() {
        fakeEndpoint {
            assertEquals(3, it.size)
            assertTrue(it.contains("pnsdk"))
            assertTrue(it.contains("requestid"))
            assertTrue(it.contains("uuid"))
        }.sync()
    }

    private fun fakeEndpoint(
        paramsCondition: (map: HashMap<String, String>) -> Unit
    ) = object : Endpoint<Any, Any>(pubnub) {

        override fun doWork(queryParams: HashMap<String, String>): Call<Any> {
            paramsCondition.invoke(queryParams)
            return fakeCall()
        }

        override fun createResponse(input: Response<Any>) = this
        override fun operationType() = PNOperationType.PNSubscribeOperation
        override fun isSubKeyRequired() = false
        override fun isPubKeyRequired() = false
        override fun isAuthRequired() = false
    }

    private fun fakeCall() = object : Call<Any> {
        override fun enqueue(callback: Callback<Any>) {}
        override fun isExecuted() = false
        override fun clone(): Call<Any> = this
        override fun isCanceled() = false
        override fun cancel() {}
        override fun execute(): Response<Any> = Response.success(null)
        override fun request() = Request.Builder().build()
    }
}
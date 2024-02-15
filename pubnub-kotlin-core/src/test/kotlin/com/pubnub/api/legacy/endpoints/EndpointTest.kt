package com.pubnub.api.legacy.endpoints

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.any
import com.github.tomakehurst.wiremock.client.WireMock.forbidden
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.listen
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.v2.callbacks.onFailure
import com.pubnub.internal.BasePubNub
import com.pubnub.internal.Endpoint
import com.pubnub.internal.PNConfiguration
import com.pubnub.internal.TestPubNub
import okhttp3.Request
import okio.Timeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean
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
        val expectedUuid = UserId(BasePubNub.generateUUID())
        pubnub.configuration.userId = expectedUuid

        fakeEndpoint {
            assertEquals(expectedUuid.value, it["uuid"])
        }.sync()
    }

    @Test
    fun testQueryParam() {
        fakeEndpoint {
            assertEquals("sf", it["city"])
            assertEquals(pubnub.configuration.userId.value, it["uuid"])
            assertEquals(4, it.size)
            assertTrue(it.contains("pnsdk"))
            assertTrue(it.contains("requestid"))
            assertTrue(it.contains("uuid"))
        }.apply {
            queryParam += mapOf(
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
            queryParam.clear()
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

    @Test
    fun testErrorAffectedChannelsAndChannelGroups() {
        stubFor(
            any(UrlPattern.ANY)
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withBody(
                            JsonObject().apply {
                                add(
                                    "payload",
                                    JsonObject().apply {
                                        add("channels", JsonArray().apply { add("ch1"); add("ch2") })
                                        add("channel-groups", JsonArray().apply { add("cg1") })
                                    }
                                )
                            }.toString()
                        )
                )
        )

        val success = AtomicBoolean()

        pubnub.time()
            .async { result ->
                result.onFailure {
                    assertEquals(listOf("ch1", "ch2"), it.affectedChannels)
                    assertEquals(listOf("cg1"), it.affectedChannelGroups)
                    success.set(true)
                }
            }

        success.listen()
    }

    @Test
    fun testNoAffectedChannelsAndChannelGroups() {
        stubFor(
            any(UrlPattern.ANY)
                .willReturn(
                    aResponse()
                        .withStatus(400)
                        .withBody("""{}""")
                )
        )

        val success = AtomicBoolean()

        pubnub.time()
            .async { result ->
                result.onFailure {
                    it as PubNubException
                    assertTrue(it.affectedChannels.isEmpty())
                    assertTrue(it.affectedChannelGroups.isEmpty())
                    success.set(true)
                }
            }

        success.listen()
    }

//    @Test // TODO investigate for Result changes
//    fun testNoSecretKeySignatureParam() {
//        pubnub.configuration.secretKey = ""
//
//        stubFor(
//            any(UrlPattern.ANY).willReturn(
//                aResponse().withBody("""[100]""")
//            )
//        )
//
//        val success = AtomicBoolean()
//
//        pubnub.time()
//            .async { result ->
//                assertNull(status.param("signature"))
//                success.set(true)
//            }
//
//        success.listen()
//    }

//    @Test  // TODO investigate for Result changes
//    fun testSecretKeySignatureParam() {
//        pubnub.configuration.secretKey = "mySecretKey"
//
//        stubFor(
//            any(UrlPattern.ANY).willReturn(
//                aResponse().withBody("""[100]""")
//            )
//        )
//
//        val success = AtomicBoolean()
//
//        pubnub.time()
//            .async { result ->
//                assertNotNull(status.param("signature"))
//                success.set(true)
//            }
//
//        success.listen()
//    }

    @Test
    fun testUnauthorized() {
        stubFor(
            any(UrlPattern.ANY)
                .willReturn(
                    forbidden()
                )
        )

        val success = AtomicBoolean()

        pubnub.time()
            .async { result ->
                assertTrue(result.isFailure)
                result.onFailure {
                    assertEquals(403, (it as PubNubException).statusCode)
                    success.set(true)
                }
            }

        success.listen()
    }

    @Test
    fun testDefaultTimeoutValues() {
        val p = TestPubNub(PNConfiguration(userId = UserId(BasePubNub.generateUUID())))
        assertEquals(300, p.pubNubImpl.configuration.presenceTimeout)
        assertEquals(0, p.pubNubImpl.configuration.heartbeatInterval)
        p.forceDestroy()
    }

    @Test
    fun testCustomTimeoutValues1() {
        val p = TestPubNub(PNConfiguration(userId = UserId(BasePubNub.generateUUID())))
        p.pubNubImpl.configuration.presenceTimeout = 100
        assertEquals(100, p.pubNubImpl.configuration.presenceTimeout)
        assertEquals(49, p.pubNubImpl.configuration.heartbeatInterval)
        p.forceDestroy()
    }

    @Test
    fun testCustomTimeoutValues2() {
        val p = TestPubNub(PNConfiguration(userId = UserId(BasePubNub.generateUUID())))
        p.pubNubImpl.configuration.heartbeatInterval = 100
        assertEquals(300, p.pubNubImpl.configuration.presenceTimeout)
        assertEquals(100, p.pubNubImpl.configuration.heartbeatInterval)
        p.forceDestroy()
    }

    @Test
    fun testCustomTimeoutValues3() {
        val p = TestPubNub(PNConfiguration(userId = UserId(BasePubNub.generateUUID())))
        p.pubNubImpl.configuration.heartbeatInterval = 40
        p.pubNubImpl.configuration.presenceTimeout = 50
        assertEquals(50, p.pubNubImpl.configuration.presenceTimeout)
        assertEquals(24, p.pubNubImpl.configuration.heartbeatInterval)
        p.forceDestroy()
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
        override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUBLISH
    }

    private fun fakeCall() = object : Call<Any> {
        override fun enqueue(callback: Callback<Any>) {}
        override fun isExecuted() = false
        override fun clone(): Call<Any> = this
        override fun isCanceled() = false
        override fun cancel() {}
        override fun execute(): Response<Any> = Response.success(null)
        override fun request() = Request.Builder().build()
        override fun timeout(): Timeout = Timeout.NONE
    }
}

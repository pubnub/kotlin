package com.pubnub.api.legacy.endpoints.pubsub

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.matching
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import com.pubnub.api.CommonUtils.assertPnException
import com.pubnub.api.CommonUtils.failTest
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.SpaceId
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.MessageType
import org.awaitility.Awaitility
import org.hamcrest.core.IsEqual
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class PublishTest : BaseTest() {

    val channel = "coolChannel"

    @Test
    fun testFireSuccessSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.fire(
            channel = channel,
            message = "hi"
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("true", requests[0].queryParameter("norep").firstValue())
        assertEquals("0", requests[0].queryParameter("store").firstValue())
    }

    @Test
    fun testNoRepSuccessSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            replicate = false
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("true", requests[0].queryParameter("norep").firstValue())
    }

    @Test
    fun testRepDefaultSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/%22hirep%22"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hirep",
            replicate = false
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("true", requests[0].queryParameter("norep").firstValue())
    }

    @Test
    fun testSuccessSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            replicate = false
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessSequenceSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi"
        ).sync()!!

        pubnub.publish(
            channel = channel,
            message = "hi"
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(2, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("1", requests[0].queryParameter("seqn").firstValue())
        assertEquals("2", requests[1].queryParameter("seqn").firstValue())
    }

    @Test
    fun testSuccessPostSync() {
        stubFor(
            post(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = listOf("m1", "m2"),
            usePost = true
        ).sync()!!

        val requests = findAll(postRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("""["m1","m2"]""", String(requests[0].body, Charset.forName("UTF-8")))
    }

    @Test
    fun testSuccessStoreFalseSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            shouldStore = false
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("0", requests[0].queryParameter("store").firstValue())
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessStoreTrueSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            shouldStore = true
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("1", requests[0].queryParameter("store").firstValue())
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessMetaSync() {
        stubFor(
            get(publishUrl())
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam(
                    "pnsdk", matching("PubNub-Kotlin/.*")
                ) // .withQueryParam("meta", matching("%5B%22m1%22%2C%22m2%22%5D"))
                .withQueryParam("meta", equalToJson("""["m1","m2"]"""))
                .withQueryParam("store", matching("0"))
                .withQueryParam("seqn", matching("1"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            meta = listOf("m1", "m2"),
            shouldStore = false
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
    }

    @Test
    fun testSuccessAuthKeySync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )
        pubnub.configuration.authKey = "authKey"

        pubnub.publish(
            channel = channel,
            message = "hi"
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("authKey", requests[0].queryParameter("auth").firstValue())
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessIntSync() {
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/10"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = 10
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessArraySync() {
        stubFor(
            get(
                urlPathEqualTo(
                    "/publish/myPublishKey/mySubscribeKey/0/$channel/0/[%22a%22,%22b%22,%22c%22]"
                )
            ).willReturn(aSuccessPublishResponse())
        )

        val l = listOf("a", "b", "c")

        pubnub.publish(
            channel = channel,
            message = l
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessArrayEncryptedSync() {
        stubFor(
            get(
                urlPathEqualTo(
                    "/publish/myPublishKey/mySubscribeKey/0/$channel/0/%22HFP7V6bDwBLrwc1t8Rnrog==%22"
                )
            )
                .willReturn(aSuccessPublishResponse())
        )
        pubnub.configuration.cipherKey = "testCipher"
        pubnub.configuration.useRandomInitializationVector = false

        pubnub.publish(
            channel = channel,
            message = listOf("m1", "m2")
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testSuccessPostEncryptedSync() {
        stubFor(
            post(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0"))
                .willReturn(aSuccessPublishResponse())
        )
        pubnub.configuration.cipherKey = "testCipher"
        pubnub.configuration.useRandomInitializationVector = false

        pubnub.publish(
            channel = channel,
            message = listOf("m1", "m2"),
            usePost = true
        ).sync()!!

        val requests = findAll(postRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals(
            """"HFP7V6bDwBLrwc1t8Rnrog=="""",
            String(requests[0].body, Charset.forName("UTF-8"))
        )
    }

    @Test
    fun testSuccessHashMapSync() {
        val params: MutableMap<String, Any> = HashMap()
        params["a"] = 10
        params["z"] = "test"
        stubFor(
            get(
                urlPathEqualTo(
                    "/publish/myPublishKey/mySubscribeKey/0/$channel/0/%7B%22a%22:10,%22z%22:%22test%22%7D"
                )
            )
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = params
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    private class TestPojo() {

        constructor(s1: String, s2: String) : this() {
            field1 = s1
            field2 = s2
        }

        private var field1: String? = null
        private var field2: String? = null
    }

    @Test
    fun testSuccessPOJOSync() {
        val testPojo = TestPojo("10", "20")
        stubFor(
            get(
                urlPathEqualTo(
                    "/publish/myPublishKey/mySubscribeKey/0/$channel/0/%7B%22field1%22:%2210%22,%22field2%22:%2220%22%7D"
                )
            )
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = testPojo
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testJSONObject() {
        val testMessage = JSONObject()
        testMessage.put("hi", "test")
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/%7B%22hi%22:%22test%22%7D"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = testMessage.toMap()
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testJSONList() {
        val testMessage = JSONArray()
        testMessage.put("hi")
        testMessage.put("hi2")
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/[%22hi%22,%22hi2%22]"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = testMessage.toList()
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
    }

    @Test
    fun testEmptyChannel() {
        stubFor(
            get(publishUrl())
                .willReturn(aResponse().withBody("""[1,"Sent","158832720000000000"]"""))
        )

        try {
            pubnub.publish(
                channel = " ",
                message = "hi"
            ).sync()!!
            failTest()
        } catch (e: PubNubException) {
            assertPnException(PubNubError.CHANNEL_MISSING, e)
        }
    }

    @Test
    fun testOperationTypeSuccessAsync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )
        val atomic = AtomicInteger(0)

        pubnub.publish(
            channel = channel,
            message = "hi"
        ).async { _, status ->
            if (status.operation === PNOperationType.PNPublishOperation) {
                atomic.incrementAndGet()
            }
        }

        Awaitility.await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAtomic(atomic, IsEqual.equalTo(1))
    }

    @Test
    fun testEmptySubKeySync() {
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/%22hirep%22"))
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.configuration.subscribeKey = ""

        try {
            pubnub.publish(
                channel = channel,
                message = "hirep"
            ).sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.SUBSCRIBE_KEY_MISSING, e)
        }
    }

    @Test
    fun testEmptyPublishKeySync() {
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/%22hirep%22"))
                .willReturn(aSuccessPublishResponse())
        )
        pubnub.configuration.publishKey = ""

        try {
            pubnub.publish(
                channel = channel,
                message = "hirep"
            ).sync()!!
            failTest()
        } catch (e: Exception) {
            assertPnException(PubNubError.PUBLISH_KEY_MISSING, e)
        }
    }

    @Test
    fun testTTLShouldStoryDefaultSuccessSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            ttl = 10
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("10", requests[0].queryParameter("ttl").firstValue())
    }

    @Test
    fun testTTLShouldStoreFalseSuccessSync() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            shouldStore = false
        ).sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("0", requests[0].queryParameter("store").firstValue())
        assertFalse(requests[0].queryParameter("ttl").isPresent)
    }

    @Test
    fun testSpaceIdQueryParamIsPassedInPublish() {
        val spaceIdValue = "thisIsSpaceId"

        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            spaceId = SpaceId(spaceIdValue)
        ).sync()

        WireMock.verify(
            getRequestedFor(publishUrl())
                .withQueryParam(Publish.SPACE_ID_QUERY_PARAM, EqualToPattern(spaceIdValue))
        )
    }

    @Test
    fun testMissingSpaceIdQueryParamIsNotSet() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi"
        ).sync()

        WireMock.verify(
            getRequestedFor(publishUrl())
                .withQueryParam(Publish.SPACE_ID_QUERY_PARAM, AbsentPattern.ABSENT)
        )
    }

    @Test
    fun testMessageTypeQueryParamIsPassedInPublish() {
        val messageTypeValue = "messageType"

        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi",
            messageType = MessageType(messageTypeValue)
        ).sync()

        WireMock.verify(
            getRequestedFor(publishUrl())
                .withQueryParam(Publish.MESSAGE_TYPE_QUERY_PARAM, EqualToPattern(messageTypeValue))
        )
    }

    @Test
    fun testMissingMessageTypeQueryParamIsNotSet() {
        stubFor(
            get(publishUrl())
                .willReturn(aSuccessPublishResponse())
        )

        pubnub.publish(
            channel = channel,
            message = "hi"
        ).sync()

        WireMock.verify(
            getRequestedFor(publishUrl())
                .withQueryParam(Publish.MESSAGE_TYPE_QUERY_PARAM, AbsentPattern.ABSENT)
        )
    }

    private fun publishUrl() = urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/$channel/0/%22hi%22")

    private fun aSuccessPublishResponse() = aResponse().withBody("""[1,"Sent","15883272000000000"]""")
}

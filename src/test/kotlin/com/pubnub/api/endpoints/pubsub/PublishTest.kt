package com.pubnub.api.endpoints.pubsub

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.pubnub.api.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PublishTest : BaseTest() {

    @Test
    fun testFireSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withBody("""[1,"Sent","14598111595318003"]"""))
        )

        pubnub.fire().apply {
            channel = "coolChannel"
            message = "hi"
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("true", requests[0].queryParameter("norep").firstValue())
        assertEquals("0", requests[0].queryParameter("store").firstValue())
    }

    @Test
    fun testNoRepSuccessSync() {
        stubFor(
            get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withBody("""[1,"Sent","14598111595318003"]"""))
        )

        pubnub.publish().apply {
            channel = "coolChannel"
            message = "hi"
            replicate = false
        }.sync()!!

        val requests = findAll(getRequestedFor(urlMatching("/.*")))
        assertEquals(1, requests.size)
        assertEquals("myUUID", requests[0].queryParameter("uuid").firstValue())
        assertEquals("true", requests[0].queryParameter("norep").firstValue())
    }

}
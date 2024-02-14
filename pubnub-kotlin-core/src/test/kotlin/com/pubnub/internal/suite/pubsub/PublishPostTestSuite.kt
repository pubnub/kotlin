package com.pubnub.internal.suite.pubsub

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.Gson
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.endpoints.pubsub.Publish
import com.pubnub.internal.suite.AUTH
import com.pubnub.internal.suite.EndpointTestSuite
import com.pubnub.internal.suite.PUB
import com.pubnub.internal.suite.SUB
import org.json.JSONObject
import org.junit.Assert.assertEquals

class PublishPostTestSuite : com.pubnub.internal.suite.EndpointTestSuite<Publish, PNPublishResult>() {

    override fun telemetryParamName() = "l_pub"

    override fun pnOperation() = PNOperationType.PNPublishOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.PUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): Publish {
        return pubnub.publish(
            channel = "ch1",
            message = mapOf(
                "name" to "john",
                "age" to 30,
                "private" to false
            ),
            meta = JSONObject().apply {
                put("city", "sf")
            }.toMap(),
            usePost = true
        )
    }

    override fun verifyResultExpectations(result: PNPublishResult) {
        assertEquals(15883272000000000L, result.timetoken)
    }

    override fun successfulResponseBody() = """[1,"Sent","15883272000000000"]"""

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return post(
            urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/ch1/0")
        )
            .withQueryParam(
                "meta", equalToJson("""{"city":"sf"}""")
            )
            .withRequestBody(
                equalToJson(
                    Gson().toJson(
                        mapOf(
                            "name" to "john",
                            "age" to 30,
                            "private" to false
                        )
                    )
                )
            )!!
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()
}

package com.pubnub.internal.suite.pubsub

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.Gson
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.internal.suite.AUTH
import com.pubnub.internal.suite.CoreEndpointTestSuite
import com.pubnub.internal.suite.PUB
import com.pubnub.internal.suite.SUB
import org.junit.Assert.assertEquals

/**
 * Covers the non-string serializer path for V2 publish: the message is a structured object
 * (Map) rather than a String, so the simple JSON-quote escaping is not exercised.
 * Sibling of [PublishV2PostTestSuite], which covers the String-payload path.
 */
class PublishV2PostMapPayloadTestSuite : CoreEndpointTestSuite<Publish, PNPublishResult>() {
    // Map payload large enough to exceed PUBLISH_V1_MAX_POST_BODY_BYTES (32,768) so V2 routing
    // is exercised.
    private val largeMapMessage: Map<String, Any> = mapOf(
        "id" to "abc-123",
        "kind" to "event",
        "data" to "y".repeat(40_000),
        "count" to 42,
    )

    override fun pnOperation() = PNOperationType.PNPublishOperation

    override fun requiredKeys() = SUB + PUB + AUTH

    override fun snippet(): Publish {
        return pubnub.publish(
            channel = "ch1",
            message = largeMapMessage,
            usePost = true,
        )
    }

    override fun verifyResultExpectations(result: PNPublishResult) {
        assertEquals(15883272000000000L, result.timetoken)
    }

    override fun successfulResponseBody() = """[1,"Sent","15883272000000000"]"""

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return post(
            urlPathEqualTo("/v2/publish/myPublishKey/mySubscribeKey/0/ch1/0"),
        )
            .withRequestBody(
                equalToJson(Gson().toJson(largeMapMessage)),
            )!!
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()
}

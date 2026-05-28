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
 * Test suite for publish V2 endpoint used for large messages (> 32KB).
 * This test verifies that messages exceeding the V1 POST limit use the V2 endpoint.
 */
class PublishV2PostTestSuite : CoreEndpointTestSuite<Publish, PNPublishResult>() {
    // Create a message larger than 32KB to trigger V2 routing
    // PUBLISH_V1_MAX_POST_BODY_BYTES = 32,768 bytes
    // Need to account for JSON serialization overhead (quotes, escaping)
    private val largeMessage = "x".repeat(40_000)

    override fun pnOperation() = PNOperationType.PNPublishOperation

    override fun requiredKeys() = SUB + PUB + AUTH

    override fun snippet(): Publish {
        return pubnub.publish(
            channel = "ch1",
            message = largeMessage,
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
            // V2 endpoint path
            urlPathEqualTo("/v2/publish/myPublishKey/mySubscribeKey/0/ch1/0"),
        )
            .withRequestBody(
                equalToJson(Gson().toJson(largeMessage)),
            )!!
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()
}

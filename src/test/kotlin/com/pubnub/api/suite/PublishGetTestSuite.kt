package com.pubnub.api.suite

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.google.gson.Gson
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNPublishResult
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class PublishGetTestSuite : EndpointTestSuite<Publish, PNPublishResult>() {

    override fun telemetryParamName() = "l_pub"

    override fun pnOperation() = PNOperationType.PNPublishOperation

    override fun requiredKeys() = SUB + PUB + AUTH

    override fun snippet(): Publish {
        return pubnub.publish().apply {
            channel = "foo"
            message = "bar"
        }
    }

    override fun verifyResultExpectations(result: PNPublishResult) {
        assertEquals(15883272000000000L, result.timetoken)
    }

    override fun successfulResponseBody() = """[1,"Sent","15883272000000000"]"""

    override fun unsuccessfulResponseBodyList() = emptyList<String>()

    override fun mappingBuilder(): MappingBuilder {
        return get(
            urlPathEqualTo(
                "/publish/myPublishKey/mySubscribeKey/0/foo/0/%s".format(
                    URLEncoder.encode(Gson().toJson("bar"), StandardCharsets.UTF_8.name())
                )
            )
        )!!
    }

    override fun affectedChannelsAndGroups() = listOf("foo") to emptyList<String>()

    /*companion object {
        @JvmStatic
        fun predefined(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of("foo"),
                Arguments.of(123),
                Arguments.of(3.14)
            )
        }
    }*/

}
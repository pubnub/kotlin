package com.pubnub.internal.suite.history.v2

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.history.PNHistoryResult
import com.pubnub.internal.endpoints.History
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class HistoryTestSuite : com.pubnub.internal.suite.EndpointTestSuite<History, PNHistoryResult>() {

    override fun telemetryParamName() = "l_hist"

    override fun pnOperation() = PNOperationType.PNHistoryOperation

    override fun requiredKeys() = com.pubnub.internal.suite.SUB + com.pubnub.internal.suite.AUTH

    override fun snippet(): History =
        pubnub.history(
            channel = "ch1"
        )

    override fun verifyResultExpectations(result: PNHistoryResult) {
        assertEquals(100L, result.startTimetoken)
        assertEquals(200L, result.endTimetoken)
        assertEquals(2, result.messages.size)
        assertEquals("msg1", result.messages[0].entry.asString)
        assertEquals("msg2", result.messages[1].entry.asString)
        assertNull(result.messages[0].meta)
        assertNull(result.messages[1].meta)
        assertNull(result.messages[0].timetoken)
        assertNull(result.messages[1].timetoken)
    }

    override fun successfulResponseBody() = """[["msg1","msg2"],100,200]"""

    override fun unsuccessfulResponseBodyList() = listOf(
        "[]",
        "[{}]"
    )

    override fun mappingBuilder(): MappingBuilder {
        return get(
            urlPathEqualTo("/v2/history/sub-key/mySubscribeKey/channel/ch1")
        )
            .withQueryParam("include_token", equalTo("false"))
            .withQueryParam("count", equalTo("100"))
            .withQueryParam("include_meta", equalTo("false"))
            .withQueryParam("reverse", equalTo("false"))
    }

    override fun affectedChannelsAndGroups() = listOf("ch1") to emptyList<String>()

    override fun optionalScenarioList() = listOf(
        com.pubnub.internal.suite.OptionalScenario<PNHistoryResult>().apply {
            responseBuilder = {
                withBody("""["First Element Not An Array",0,0]""")
            }
            additionalChecks = { result ->
                assertTrue(result.isFailure)
                assertEquals((result.exceptionOrNull() as? PubNubException)?.errorMessage, "History is disabled")
            }
            result = com.pubnub.internal.suite.Result.FAIL
            pnError = PubNubError.HTTP_ERROR
        }
    )
}

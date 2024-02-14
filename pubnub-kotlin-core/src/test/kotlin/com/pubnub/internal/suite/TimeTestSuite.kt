package com.pubnub.internal.suite

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.v2.callbacks.getOrThrow
import com.pubnub.internal.endpoints.Time
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class TimeTestSuite : com.pubnub.internal.suite.EndpointTestSuite<Time, PNTimeResult>() {
    override fun telemetryParamName() = "l_time"

    override fun pnOperation() = PNOperationType.PNTimeOperation

    override fun requiredKeys() = 0

    override fun snippet(): Time {
        return pubnub.time()
    }

    override fun verifyResultExpectations(result: PNTimeResult) {
        assertEquals(1000, result.timetoken)
    }

    override fun successfulResponseBody() = """[1000]"""

    override fun unsuccessfulResponseBodyList() = listOf(
        """{}""",
        """[false]"""
    )

    override fun optionalScenarioList(): List<com.pubnub.internal.suite.OptionalScenario<PNTimeResult>> {
        return listOf(
            com.pubnub.internal.suite.OptionalScenario<PNTimeResult>().apply {
                responseBuilder = { withBody("[wrong]") }
                result = com.pubnub.internal.suite.Result.FAIL
                additionalChecks = { result ->
                    assertTrue(result.isFailure)
                    assertNull(result)
                }
            },
            com.pubnub.internal.suite.OptionalScenario<PNTimeResult>().apply {
                responseBuilder = { withBody("[123]") }
                additionalChecks = { result ->
                    assertFalse(result.isFailure)
                    assertEquals(123, result.getOrThrow().timetoken)
                }
            }
        )
    }

    override fun mappingBuilder() = get(urlPathEqualTo("/time/0"))

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()
}

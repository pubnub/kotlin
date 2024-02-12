package com.pubnub.api.suite

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.endpoints.Time
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNTimeResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class TimeTestSuite : EndpointTestSuite<Time, PNTimeResult>() {
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

    override fun optionalScenarioList(): List<OptionalScenario<PNTimeResult>> {
        return listOf(
            OptionalScenario<PNTimeResult>().apply {
                responseBuilder = { withBody("[wrong]") }
                result = Result.FAIL
                additionalChecks = { result ->
                    assertTrue(result.isFailure)
                    assertNull(result)
                }
            },
            OptionalScenario<PNTimeResult>().apply {
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

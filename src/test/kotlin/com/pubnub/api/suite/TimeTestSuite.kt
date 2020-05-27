package com.pubnub.api.suite

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.pubnub.api.endpoints.Time
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNTimeResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class TimeTestSuite : EndpointTestSuite<Time, PNTimeResult>() {
    override fun telemetryParamName() = "l_time"

    override fun pnOperation() = PNOperationType.PNTimeOperation

    override fun requiredKeys() = 0

    override fun snippet(): Time {
        return pubnub.time().apply {
            queryParam = mapOf(
                "key_1_space" to "ab cd",
                "key_2_plus" to "ab+cd"
            )
        }
    }

    override fun verifyResultExpectations(result: PNTimeResult) {
        assertEquals(1000, result.timetoken)
    }

    override fun successfulResponseBody() = """[1000]"""

    override fun unsuccessfulResponseBodyList() = listOf(
        """{}""",
        """[false]"""
    )

    override fun optionalScenarioList(): List<OptionalScenario> {
        return listOf(
            OptionalScenario().apply {
                responseBuilder = { withBody("[wrong]") }
                result = Result.FAIL
                additionalChecks = { status ->
                    assertTrue(status.error)
                }
            },
            OptionalScenario().apply {
                responseBuilder = { withBody("[123]") }
            }
        )
    }

    override fun mappingBuilder() = get(urlMatching("/time/0.*"))

    override fun affectedChannelsAndGroups() = emptyList<String>() to emptyList<String>()
}
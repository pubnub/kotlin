package com.pubnub.api.models.consumer.datasync

import com.pubnub.api.PubNubException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PubNubExceptionDataSyncTest {
    private fun exception(jso: String?) = PubNubException(jso = jso)

    @Test
    fun singleErrorWithoutPathOrLocation() {
        val e = exception("""{"errors":[{"errorCode":"DS-0100","message":"Entity not found: user-42"}]}""")

        assertEquals(
            listOf(PNDataSyncError(code = "DS-0100", message = "Entity not found: user-42")),
            e.dataSyncErrors(),
        )
        assertEquals("DS-0100", e.dataSyncErrorCode())
    }

    @Test
    fun multipleValidationErrorsKeepOrderAndPath() {
        val e =
            exception(
                """{"errors":[{"errorCode":"DS-0004","message":"must not be null","path":"channelId"},""" +
                    """{"errorCode":"DS-0004","message":"must not be null","path":"userId"}]}""",
            )

        assertEquals(
            listOf(
                PNDataSyncError(code = "DS-0004", message = "must not be null", path = "channelId"),
                PNDataSyncError(code = "DS-0004", message = "must not be null", path = "userId"),
            ),
            e.dataSyncErrors(),
        )
    }

    @Test
    fun filterErrorWithLocation() {
        val e =
            exception(
                """{"errors":[{"errorCode":"DS-1001","message":"Field 'score' is not filterable",""" +
                    """"path":"filter_fast","location":{"offset":6,"length":5}}]}""",
            )

        assertEquals(
            PNDataSyncError(
                code = "DS-1001",
                message = "Field 'score' is not filterable",
                path = "filter_fast",
                location = PNDataSyncErrorLocation(offset = 6, length = 5),
            ),
            e.dataSyncErrors().single(),
        )
    }

    @Test
    fun malformedLocationIsDroppedButItemKept() {
        val e =
            exception(
                """{"errors":[{"errorCode":"DS-1001","message":"m","location":{"offset":"6","length":5}}]}""",
            )

        val error = e.dataSyncErrors().single()
        assertEquals("DS-1001", error.code)
        assertNull(error.location)
    }

    @Test
    fun missingMessageDefaultsToEmpty() {
        val e = exception("""{"errors":[{"errorCode":"DS-0000"}]}""")

        assertEquals(PNDataSyncError(code = "DS-0000", message = ""), e.dataSyncErrors().single())
    }

    @Test
    fun itemsWithoutDataSyncCodeAreSkipped() {
        val e =
            exception(
                """{"errors":[{"errorCode":"E-1","message":"x"},{"message":"no code"},{"errorCode":42},"str",""" +
                    """{"errorCode":"DS-0301","message":"Conflict"}]}""",
            )

        assertEquals(listOf(PNDataSyncError(code = "DS-0301", message = "Conflict")), e.dataSyncErrors())
    }

    @Test
    fun nonDataSyncBodiesYieldEmptyList() {
        val bodies =
            listOf(
                null,
                "",
                "   ",
                "Not Acceptable",
                "{not json",
                "[]",
                "\"errors\"",
                """{"errors":"x"}""",
                """{"errors":[]}""",
                """{"errors":[{"message":"no code"}]}""",
                // App Context v2 error shape
                """{"status":403,"error":{"message":"Forbidden"}}""",
                // Spring Boot default /error shape
                """{"timestamp":"2026-10-05T10:00:00Z","status":500,"error":"Internal Server Error","path":"/v1"}""",
            )

        bodies.forEach { body ->
            val e = exception(body)
            assertTrue(e.dataSyncErrors().isEmpty(), "expected no DataSync errors for body: $body")
            assertNull(e.dataSyncErrorCode(), "expected no DataSync code for body: $body")
        }
    }
}

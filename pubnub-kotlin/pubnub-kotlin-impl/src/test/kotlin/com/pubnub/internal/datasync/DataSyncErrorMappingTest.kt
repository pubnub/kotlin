package com.pubnub.internal.datasync

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncError
import com.pubnub.api.models.consumer.datasync.dataSyncErrorCode
import com.pubnub.api.models.consumer.datasync.dataSyncErrors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Guards that a non-2xx DataSync response carrying a `DS-xxxx` error envelope is reported as a `DATASYNC_*`
 * [PubNubError] chosen by HTTP status (instead of the generic [PubNubError.HTTP_ERROR]) and that the envelope is
 * reachable via [dataSyncErrors]. Bodies without a DataSync code keep [PubNubError.HTTP_ERROR].
 */
class DataSyncErrorMappingTest : BaseTest() {
    private val itemPath = "/v1/datasync/subkeys/mySubscribeKey/entities/e1"

    private fun failGetEntity(
        status: Int,
        body: String,
        contentType: String = "application/json",
    ): PubNubException {
        stubFor(
            get(urlPathEqualTo(itemPath)).willReturn(
                aResponse().withStatus(status).withHeader("Content-Type", contentType).withBody(body),
            ),
        )
        return assertThrows(PubNubException::class.java) { pubnub.dataSync.getEntity("e1").sync() }
    }

    private fun dsBody(code: String) = """{"errors":[{"errorCode":"$code","message":"msg $code"}]}"""

    @Test
    fun badRequest_400_carries_every_validation_item() {
        val body =
            """{"errors":[{"errorCode":"DS-0004","message":"must not be null","path":"channelId"},""" +
                """{"errorCode":"DS-0004","message":"must not be null","path":"userId"}]}"""

        val e = failGetEntity(400, body)

        assertEquals(PubNubError.DATASYNC_BAD_REQUEST, e.pubnubError)
        assertEquals(400, e.statusCode)
        assertEquals(
            listOf(
                PNDataSyncError(code = "DS-0004", message = "must not be null", path = "channelId"),
                PNDataSyncError(code = "DS-0004", message = "must not be null", path = "userId"),
            ),
            e.dataSyncErrors(),
        )
    }

    @Test
    fun unauthenticated_401_maps_to_access_denied() {
        val e = failGetEntity(401, dsBody("DS-0203"))

        assertEquals(PubNubError.DATASYNC_ACCESS_DENIED, e.pubnubError)
        assertEquals("DS-0203", e.dataSyncErrorCode())
    }

    @Test
    fun forbidden_403_maps_to_access_denied() {
        val e = failGetEntity(403, dsBody("DS-0202"))

        assertEquals(PubNubError.DATASYNC_ACCESS_DENIED, e.pubnubError)
        assertEquals("DS-0202", e.dataSyncErrorCode())
    }

    @Test
    fun notFound_404_maps_to_not_found() {
        val e = failGetEntity(404, dsBody("DS-0100"))

        assertEquals(PubNubError.DATASYNC_NOT_FOUND, e.pubnubError)
        assertEquals("DS-0100", e.dataSyncErrorCode())
    }

    @Test
    fun conflict_409_maps_to_conflict() {
        val e = failGetEntity(409, dsBody("DS-0302"))

        assertEquals(PubNubError.DATASYNC_CONFLICT, e.pubnubError)
        assertEquals("DS-0302", e.dataSyncErrorCode())
    }

    @Test
    fun preconditionFailed_412_maps_to_precondition_failed() {
        val e = failGetEntity(412, dsBody("DS-0300"))

        assertEquals(PubNubError.DATASYNC_PRECONDITION_FAILED, e.pubnubError)
        assertEquals("DS-0300", e.dataSyncErrorCode())
    }

    @Test
    fun serviceUnavailable_503_maps_to_server_error() {
        val e = failGetEntity(503, dsBody("DS-0009"))

        assertEquals(PubNubError.DATASYNC_SERVER_ERROR, e.pubnubError)
        assertEquals("DS-0009", e.dataSyncErrorCode())
    }

    @Test
    fun internalServerError_500_maps_to_server_error() {
        val e = failGetEntity(500, dsBody("DS-0000"))

        assertEquals(PubNubError.DATASYNC_SERVER_ERROR, e.pubnubError)
        assertEquals("DS-0000", e.dataSyncErrorCode())
    }

    @Test
    fun unmapped_status_with_datasync_body_stays_http_error() {
        // 429 is a client/throttling status, not a server fault: no DATASYNC_* category is guessed for it, but the
        // DS code stays reachable.
        val e = failGetEntity(429, dsBody("DS-9999"))

        assertEquals(PubNubError.HTTP_ERROR, e.pubnubError)
        assertEquals(429, e.statusCode)
        assertEquals("DS-9999", e.dataSyncErrorCode())
    }

    @Test
    fun forbidden_403_without_datasync_body_stays_http_error() {
        val e = failGetEntity(403, """{"status":403,"error":{"message":"Forbidden"}}""")

        assertEquals(PubNubError.HTTP_ERROR, e.pubnubError)
        assertEquals(403, e.statusCode)
        assertTrue(e.dataSyncErrors().isEmpty())
    }

    @Test
    fun notAcceptable_406_plain_text_stays_http_error() {
        val e = failGetEntity(406, "Not Acceptable", contentType = "text/plain")

        assertEquals(PubNubError.HTTP_ERROR, e.pubnubError)
        assertEquals(406, e.statusCode)
        assertTrue(e.dataSyncErrors().isEmpty())
    }

    @Test
    fun errorMessage_still_holds_the_raw_body() {
        val body = dsBody("DS-0302")

        val e = failGetEntity(409, body)

        assertEquals(body, e.errorMessage)
    }
}

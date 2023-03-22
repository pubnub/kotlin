package com.pubnub.api.legacy.endpoints.files

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.github.tomakehurst.wiremock.matching.AbsentPattern
import com.github.tomakehurst.wiremock.matching.EqualToPattern
import com.pubnub.api.SpaceId
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.legacy.BaseTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PublishFileMessageTest : BaseTest() {

    val channel = "coolChannel"
    val fileId = "fileId"
    val filename = "filename"
    val message = "hi"

    @BeforeEach
    fun before() {
        stubFor(
            get(filesUrlPathMatcher()).willReturn(
                aResponse().withBody("""[1,"Sent","15883272000000000"]""")
            )
        )
    }

    @Test
    fun testSpaceIdQueryParamIsPassedInPublish() {
        val spaceIdValue = "thisIsSpaceId"

        pubnub.publishFileMessage(
            channel = channel, message = message, spaceId = SpaceId(spaceIdValue), fileId = fileId, fileName = filename
        ).sync()

        verify(
            getRequestedFor(filesUrlPathMatcher())
                .withQueryParam(PublishFileMessage.SPACE_ID_QUERY_PARAM, EqualToPattern(spaceIdValue))
        )
    }

    @Test
    fun testMissingSpaceIdQueryParamIsNotSet() {
        pubnub.publishFileMessage(
            channel = channel, message = message, fileId = fileId, fileName = filename
        ).sync()

        verify(
            getRequestedFor(filesUrlPathMatcher())
                .withQueryParam(PublishFileMessage.SPACE_ID_QUERY_PARAM, AbsentPattern.ABSENT)
        )
    }

    @Test
    fun testTypeQueryParamIsPassedInPublish() {
        val typeValue = "type"

        pubnub.publishFileMessage(
            channel = channel,
            message = message,
            fileId = fileId,
            fileName = filename,
            type = typeValue
        ).sync()

        verify(
            getRequestedFor(filesUrlPathMatcher())
                .withQueryParam(PublishFileMessage.TYPE_QUERY_PARAM, EqualToPattern(typeValue))
        )
    }

    @Test
    fun testMissingTypeQueryParamIsNotSet() {
        pubnub.publishFileMessage(
            channel = channel, message = message, fileId = fileId, fileName = filename
        ).sync()

        verify(
            getRequestedFor(filesUrlPathMatcher())
                .withQueryParam(PublishFileMessage.TYPE_QUERY_PARAM, AbsentPattern.ABSENT)
        )
    }

    private fun filesUrlPathMatcher() =
        urlPathEqualTo("/v1/files/publish-file/${pubnub.configuration.publishKey}/${pubnub.configuration.subscribeKey}/0/$channel/0/%7B%22message%22:%22$message%22,%22file%22:%7B%22id%22:%22$fileId%22,%22name%22:%22$filename%22%7D%7D")
}

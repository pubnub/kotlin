package com.pubnub.internal.endpoints.datasync.channel

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.pubnub.api.legacy.BaseTest
import org.junit.Assert.assertTrue
import org.junit.Test

class GetChannelsEndpointTest : BaseTest() {
    private val path = "/v1/datasync/subkeys/mySubscribeKey/channels"

    private fun stubList() {
        stubFor(
            get(urlPathEqualTo(path)).willReturn(
                aResponse().withBody("""{"status":200,"data":[]}"""),
            ),
        )
    }

    @Test
    fun filters_percent_encode_literal_plus_and_percent() {
        stubList()

        pubnub.dataSync.getChannels(filterFast = "name == \"a+b\"", filter = "name == \"50%\"").sync()

        val url = findAll(getRequestedFor(urlPathEqualTo(path))).single().url
        assertTrue(url, url.contains("filter_fast=name%20%3D%3D%20%22a%2Bb%22"))
        assertTrue(url, url.contains("filter=name%20%3D%3D%20%2250%25%22"))
    }
}
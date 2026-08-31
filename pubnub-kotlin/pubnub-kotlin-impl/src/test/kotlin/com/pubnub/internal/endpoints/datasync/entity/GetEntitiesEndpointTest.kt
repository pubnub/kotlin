package com.pubnub.internal.endpoints.datasync.entity

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import org.junit.Test

class GetEntitiesEndpointTest : BaseTest() {
    private val path = "/v1/datasync/subkeys/mySubscribeKey/entities"

    private fun stubList() {
        stubFor(
            get(urlPathEqualTo(path)).willReturn(
                aResponse().withBody("""{"status":200,"data":[]}"""),
            ),
        )
    }

    @Test
    fun ascending_sort_serializes_bare_property() {
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            sort = listOf(PNDataSyncSortField("username")),
        ).sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("sort", equalTo("username")))
    }

    @Test
    fun descending_sort_serializes_desc_suffix() {
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            sort = listOf(PNDataSyncSortField("username", ascending = false)),
        ).sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("sort", equalTo("username:desc")))
    }

    @Test
    fun mixed_multi_field_sort_joins_with_comma() {
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            sort = listOf(
                PNDataSyncSortField("username"),
                PNDataSyncSortField("email", ascending = false),
            ),
        ).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("sort", equalTo("username,email:desc")),
        )
    }

    @Test
    fun filterFast_serializes_to_filter_fast_wire_param() {
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            filterFast = "username == \"alice\"",
        ).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter_fast", equalTo("username == \"alice\"")),
        )
    }

    @Test
    fun filter_serializes_to_filter_wire_param() {
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            filter = "username LIKE \"a*\"",
        ).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter", equalTo("username LIKE \"a*\"")),
        )
    }
}

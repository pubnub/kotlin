package com.pubnub.internal.endpoints.datasync.user

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

class GetUsersEndpointTest : BaseTest() {
    private val path = "/v1/datasync/subkeys/mySubscribeKey/users"

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

        pubnub.dataSync.getUsers(
            sort = listOf(PNDataSyncSortField("name")),
        ).sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("sort", equalTo("name")))
    }

    @Test
    fun descending_sort_serializes_desc_suffix() {
        stubList()

        pubnub.dataSync.getUsers(
            sort = listOf(PNDataSyncSortField("name", ascending = false)),
        ).sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("sort", equalTo("name:desc")))
    }

    @Test
    fun mixed_multi_field_sort_joins_with_comma() {
        stubList()

        pubnub.dataSync.getUsers(
            sort = listOf(
                PNDataSyncSortField("name"),
                PNDataSyncSortField("updated", ascending = false),
            ),
        ).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("sort", equalTo("name,updated:desc")),
        )
    }
}

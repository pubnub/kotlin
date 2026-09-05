package com.pubnub.internal.endpoints.datasync.membership

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import org.junit.Test

class GetMembershipsEndpointTest : BaseTest() {
    private val path = "/v1/datasync/subkeys/mySubscribeKey/memberships"

    private fun stubList() {
        stubFor(
            get(urlPathEqualTo(path)).willReturn(
                aResponse().withBody("""{"status":200,"data":[]}"""),
            ),
        )
    }

    @Test
    fun channelId_and_userId_serialize_to_snake_case_params() {
        stubList()

        pubnub.dataSync.getMemberships(channelId = "c1", userId = "u1").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("channel_id", equalTo("c1"))
                .withQueryParam("user_id", equalTo("u1")),
        )
    }

    @Test
    fun classVersion_serializes_to_relationship_class_version_as_string() {
        stubList()

        pubnub.dataSync.getMemberships(classVersion = 7).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("relationship_class_version", equalTo("7")),
        )
    }

    @Test
    fun filterFast_serializes_to_filter_fast_wire_param() {
        stubList()

        pubnub.dataSync.getMemberships(filterFast = "status == \"active\"").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter_fast", equalTo("status == \"active\"")),
        )
    }

    @Test
    fun filter_serializes_to_filter_wire_param_not_filter_advanced() {
        stubList()

        pubnub.dataSync.getMemberships(filter = "status == \"active\"").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter", equalTo("status == \"active\""))
                .withQueryParam("filter_advanced", absent()),
        )
    }

    @Test
    fun mixed_multi_field_sort_joins_with_comma() {
        stubList()

        pubnub.dataSync.getMemberships(
            sort = listOf(
                PNDataSyncSortField("createdAt"),
                PNDataSyncSortField("status", ascending = false),
            ),
        ).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("sort", equalTo("createdAt,status:desc")),
        )
    }

    @Test
    fun limit_serializes_as_string() {
        stubList()

        pubnub.dataSync.getMemberships(limit = 42).sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("limit", equalTo("42")))
    }

    @Test
    fun cursor_is_forwarded() {
        stubList()

        pubnub.dataSync.getMemberships(cursor = "opaqueCursor").sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("cursor", equalTo("opaqueCursor")))
    }

    @Test
    fun omitted_params_are_absent() {
        stubList()

        pubnub.dataSync.getMemberships().sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("channel_id", absent())
                .withQueryParam("user_id", absent())
                .withQueryParam("relationship_class_version", absent())
                .withQueryParam("filter_fast", absent())
                .withQueryParam("filter", absent())
                .withQueryParam("sort", absent())
                .withQueryParam("limit", absent())
                .withQueryParam("cursor", absent()),
        )
    }
}

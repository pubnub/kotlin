package com.pubnub.internal.endpoints.datasync.channel

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
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
    fun class_filters_serialize_to_entity_class_params() {
        stubList()

        pubnub.dataSync.getChannels(
            className = "Room",
            classVersion = 7,
            classLevel = PNDataSyncClassLevel.SUBKEY,
        ).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("entity_class", equalTo("Room"))
                .withQueryParam("entity_class_version", equalTo("7"))
                .withQueryParam("entity_class_level", equalTo(PNDataSyncClassLevel.SUBKEY.value)),
        )
    }

    @Test
    fun filterFast_serializes_to_filter_fast_wire_param() {
        stubList()

        pubnub.dataSync.getChannels(filterFast = "name == \"lobby\"").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter_fast", equalTo("name == \"lobby\"")),
        )
    }

    @Test
    fun filter_serializes_to_filter_wire_param_not_filter_advanced() {
        stubList()

        pubnub.dataSync.getChannels(filter = "name == \"lobby\"").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter", equalTo("name == \"lobby\""))
                .withQueryParam("filter_advanced", absent()),
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

    @Test
    fun query_values_with_reserved_characters_round_trip() {
        // Regression guard: every free-text query value (not only filters) must be percent-encoded, otherwise
        // `+` decodes to a space, `%41` to `A`, and a bare `%` makes the URL invalid.
        stubList()

        pubnub.dataSync.getChannels(
            className = "Test+Class",
            sort = listOf(PNDataSyncSortField("x y&z=1#f")),
            cursor = "a+b/100%a%41==",
        ).sync()

        val url = findAll(getRequestedFor(urlPathEqualTo(path))).single().url
        assertTrue(url, url.contains("entity_class=Test%2BClass"))
        assertTrue(url, url.contains("cursor=a%2Bb"))
        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("entity_class", equalTo("Test+Class"))
                .withQueryParam("sort", equalTo("x y&z=1#f"))
                .withQueryParam("cursor", equalTo("a+b/100%a%41==")),
        )
    }

    @Test
    fun mixed_multi_field_sort_joins_with_comma() {
        stubList()

        pubnub.dataSync.getChannels(
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

    @Test
    fun limit_and_cursor_are_forwarded() {
        stubList()

        pubnub.dataSync.getChannels(limit = 42, cursor = "opaqueCursor").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("limit", equalTo("42"))
                .withQueryParam("cursor", equalTo("opaqueCursor")),
        )
    }

    @Test
    fun omitted_params_are_absent() {
        stubList()

        pubnub.dataSync.getChannels().sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("entity_class", absent())
                .withQueryParam("entity_class_version", absent())
                .withQueryParam("entity_class_level", absent())
                .withQueryParam("filter_fast", absent())
                .withQueryParam("filter", absent())
                .withQueryParam("sort", absent())
                .withQueryParam("limit", absent())
                .withQueryParam("cursor", absent()),
        )
    }
}

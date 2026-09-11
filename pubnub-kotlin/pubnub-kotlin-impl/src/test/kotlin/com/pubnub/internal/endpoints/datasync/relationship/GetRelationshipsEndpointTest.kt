package com.pubnub.internal.endpoints.datasync.relationship

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import org.junit.Assert.assertThrows
import org.junit.Test

class GetRelationshipsEndpointTest : BaseTest() {
    private val path = "/v1/datasync/subkeys/mySubscribeKey/relationships"

    private fun stubList() {
        stubFor(
            get(urlPathEqualTo(path)).willReturn(
                aResponse().withBody("""{"status":200,"data":[]}"""),
            ),
        )
    }

    @Test
    fun relationship_class_is_always_on_the_wire_even_with_no_optional_params() {
        // Regression guard for the clone bug: `relationship_class` is a REQUIRED query param and must be
        // emitted unconditionally (like GetEntitiesEndpoint's entity_class), NOT via a ?.let the way
        // GetMembershipsEndpoint handles its optional class. Fails if cloned from GetMembershipsEndpoint.
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("relationship_class", equalTo("Friendship")),
        )
    }

    @Test
    fun entityAId_and_entityBId_serialize_to_snake_case_params() {
        // Clone-bug guard: must NOT inherit Membership's channel_id / user_id keys.
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship", entityAId = "a1", entityBId = "b1").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("entity_a_id", equalTo("a1"))
                .withQueryParam("entity_b_id", equalTo("b1"))
                .withQueryParam("channel_id", absent())
                .withQueryParam("user_id", absent()),
        )
    }

    @Test
    fun classVersion_serializes_to_relationship_class_version_as_string() {
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship", classVersion = 7).sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("relationship_class_version", equalTo("7")),
        )
    }

    @Test
    fun filterFast_serializes_to_filter_fast_wire_param() {
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship", filterFast = "status == \"active\"").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter_fast", equalTo("status == \"active\"")),
        )
    }

    @Test
    fun filter_serializes_to_filter_wire_param_not_filter_advanced() {
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship", filter = "status == \"active\"").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter", equalTo("status == \"active\""))
                .withQueryParam("filter_advanced", absent()),
        )
    }

    @Test
    fun mixed_multi_field_sort_joins_with_comma() {
        stubList()

        pubnub.dataSync.getRelationships(
            className = "Friendship",
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

        pubnub.dataSync.getRelationships(className = "Friendship", limit = 42).sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("limit", equalTo("42")))
    }

    @Test
    fun cursor_is_forwarded() {
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship", cursor = "opaqueCursor").sync()

        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("cursor", equalTo("opaqueCursor")))
    }

    @Test
    fun omitted_optional_params_are_absent_but_relationship_class_is_present() {
        stubList()

        pubnub.dataSync.getRelationships(className = "Friendship").sync()

        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("relationship_class", equalTo("Friendship"))
                .withQueryParam("entity_a_id", absent())
                .withQueryParam("entity_b_id", absent())
                .withQueryParam("relationship_class_version", absent())
                .withQueryParam("filter_fast", absent())
                .withQueryParam("filter", absent())
                .withQueryParam("sort", absent())
                .withQueryParam("limit", absent())
                .withQueryParam("cursor", absent()),
        )
    }

    @Test
    fun blank_className_throws_and_never_reaches_the_wire() {
        // Regression guard: a blank className must throw ENTITY_CLASS_MISSING in validateParams before any
        // request is built. Fails if the endpoint is cloned from GetMembershipsEndpoint (no class guard).
        stubList()

        assertThrows(PubNubException::class.java) {
            pubnub.dataSync.getRelationships(className = " ").sync()
        }

        verify(0, getRequestedFor(urlPathEqualTo(path)))
    }
}

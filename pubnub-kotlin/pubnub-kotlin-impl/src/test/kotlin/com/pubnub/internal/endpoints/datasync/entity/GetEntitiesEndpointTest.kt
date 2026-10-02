package com.pubnub.internal.endpoints.datasync.entity

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.findAll
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.UserId
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.PNDataSyncSortField
import com.pubnub.test.SignatureUtils.decomposeAndVerifySignature
import org.junit.Assert.assertTrue
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

    @Test
    fun filters_percent_encode_literal_plus_and_percent() {
        // Regression guard: an un-encoded `+` would reach the server as a space and `%` as the start of an
        // escape sequence. The service relies on Retrofit/OkHttp to encode query values (bare @QueryMap).
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            filterFast = "email == \"a+b@x.com\"",
            filter = "note == \"50%\"",
        ).sync()

        val url = findAll(getRequestedFor(urlPathEqualTo(path))).single().url
        assertTrue(url, url.contains("filter_fast=email%20%3D%3D%20%22a%2Bb%40x.com%22"))
        assertTrue(url, url.contains("filter=note%20%3D%3D%20%2250%25%22"))
        verify(
            getRequestedFor(urlPathEqualTo(path))
                .withQueryParam("filter_fast", equalTo("email == \"a+b@x.com\""))
                .withQueryParam("filter", equalTo("note == \"50%\"")),
        )
    }

    @Test
    fun query_values_with_reserved_characters_round_trip() {
        // Regression guard: every free-text query value (not only filters) must be percent-encoded, otherwise
        // `+` decodes to a space, `%41` to `A`, and a bare `%` makes the URL invalid.
        stubList()

        pubnub.dataSync.getEntities(
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
    fun user_id_with_reserved_characters_round_trips_as_uuid() {
        config.userId = UserId("u+1%41")
        stubList()

        pubnub.dataSync.getEntities(className = "TestUser").sync()

        val url = findAll(getRequestedFor(urlPathEqualTo(path))).single().url
        assertTrue(url, url.contains("uuid=u%2B1%2541"))
        verify(getRequestedFor(urlPathEqualTo(path)).withQueryParam("uuid", equalTo("u+1%41")))
    }

    @Test
    fun signature_is_valid_for_query_values_with_reserved_characters() {
        config.secretKey = "mySecretKey"
        stubList()

        pubnub.dataSync.getEntities(
            className = "TestUser",
            filterFast = "email == \"a+b@x.com\"",
            filter = "note == \"50%\"",
            cursor = "c+d",
        ).sync()

        val request = findAll(getRequestedFor(urlPathEqualTo(path))).single()
        decomposeAndVerifySignature(pubnub.configuration, request)
    }
}

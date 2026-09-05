package com.pubnub.internal.endpoints.datasync.membership

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.delete
import com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.patch
import com.github.tomakehurst.wiremock.client.WireMock.patchRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.put
import com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import org.junit.Test

/**
 * WireMock coverage for the /memberships write endpoints (create/set/update/remove). Unlike the model unit
 * tests (which cover request-body JSON and `@SerializedName`), these assert the parts that live only in the
 * Retrofit annotations and cannot be exercised by serializing a request object: the versioned membership
 * Content-Type, the JSON-Patch Content-Type, the `If-Match` header, and the `/memberships/{id}` paths. A live
 * IT catches a total miss, but a wrong header value would still round-trip against the real backend, so it is
 * asserted here.
 */
class MembershipWriteEndpointsTest : BaseTest() {
    private val collectionPath = "/v1/datasync/subkeys/mySubscribeKey/memberships"
    private val itemPath = "/v1/datasync/subkeys/mySubscribeKey/memberships/m1"
    private val membershipContentType = "application/vnd.pubnub.objects.membership+json;version=1"
    private val jsonPatchContentType = "application/json-patch+json"
    private val entityBody = """{"status":200,"data":{"id":"m1"}}"""

    @Test
    fun create_posts_to_memberships_with_the_versioned_membership_content_type() {
        stubFor(post(urlPathEqualTo(collectionPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.createMembership(
            channelId = "c1",
            userId = "u1",
            classVersion = 1,
            membershipId = "m1",
        ).sync()

        verify(
            postRequestedFor(urlPathEqualTo(collectionPath))
                .withHeader("Content-Type", equalTo(membershipContentType)),
        )
    }

    @Test
    fun set_puts_to_memberships_id_with_the_versioned_content_type_and_if_match() {
        stubFor(put(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.setMembership(
            membershipId = "m1",
            classVersion = 1,
            status = "active",
            ifMatch = "etag-1",
        ).sync()

        verify(
            putRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(membershipContentType))
                .withHeader("If-Match", equalTo("etag-1")),
        )
    }

    @Test
    fun update_patches_memberships_id_with_the_json_patch_content_type_and_if_match() {
        stubFor(patch(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.updateMembership(
            membershipId = "m1",
            operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive")),
            ifMatch = "etag-2",
        ).sync()

        verify(
            patchRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(jsonPatchContentType))
                .withHeader("If-Match", equalTo("etag-2")),
        )
    }

    @Test
    fun remove_deletes_memberships_id_with_if_match() {
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.removeMembership(membershipId = "m1", ifMatch = "etag-3").sync()

        verify(
            deleteRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("If-Match", equalTo("etag-3")),
        )
    }

    @Test
    fun if_match_is_absent_when_not_supplied() {
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.removeMembership(membershipId = "m1").sync()

        verify(
            deleteRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("If-Match", absent()),
        )
    }
}

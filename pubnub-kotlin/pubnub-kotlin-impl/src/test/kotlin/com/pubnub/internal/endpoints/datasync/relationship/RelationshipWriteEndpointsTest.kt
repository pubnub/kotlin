package com.pubnub.internal.endpoints.datasync.relationship

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
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * WireMock coverage for the /relationships write endpoints (create/set/update/remove). Unlike the model unit
 * tests (which cover request-body JSON and `@SerializedName`), these assert the parts that live only in the
 * Retrofit annotations and cannot be exercised by serializing a request object: the versioned relationship
 * Content-Type, the JSON-Patch Content-Type, the `If-Match` header, and the `/relationships/{id}` paths. A live
 * IT catches a total miss, but a wrong header value would still round-trip against the real backend, so it is
 * asserted here.
 */
class RelationshipWriteEndpointsTest : BaseTest() {
    private val collectionPath = "/v1/datasync/subkeys/mySubscribeKey/relationships"
    private val itemPath = "/v1/datasync/subkeys/mySubscribeKey/relationships/r1"
    private val relationshipContentType = "application/vnd.pubnub.objects.relationship+json;version=1"
    private val jsonPatchContentType = "application/json-patch+json"
    private val entityBody = """{"status":200,"data":{"id":"r1"}}"""

    @Test
    fun create_posts_to_relationships_with_the_versioned_relationship_content_type() {
        stubFor(post(urlPathEqualTo(collectionPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.createRelationship(
            entityAId = "a1",
            entityBId = "b1",
            className = "Friendship",
            classVersion = 1,
            relationshipId = "r1",
        ).sync()

        verify(
            postRequestedFor(urlPathEqualTo(collectionPath))
                .withHeader("Content-Type", equalTo(relationshipContentType)),
        )
    }

    @Test
    fun set_puts_to_relationships_id_with_the_versioned_content_type_and_if_match() {
        stubFor(put(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.setRelationship(
            relationshipId = "r1",
            classVersion = 1,
            status = "active",
            ifMatch = "etag-1",
        ).sync()

        verify(
            putRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(relationshipContentType))
                .withHeader("If-Match", equalTo("etag-1")),
        )
    }

    @Test
    fun update_patches_relationships_id_with_the_json_patch_content_type_and_if_match() {
        stubFor(patch(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.updateRelationship(
            relationshipId = "r1",
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
    fun remove_deletes_relationships_id_with_if_match() {
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.removeRelationship(relationshipId = "r1", ifMatch = "etag-3").sync()

        verify(
            deleteRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("If-Match", equalTo("etag-3")),
        )
    }

    @Test
    fun create_whitespace_className_throws_and_never_reaches_the_wire() {
        // Regression guard: the create className guard is `isBlank()`, not `isEmpty()`, so a whitespace-only
        // className must throw before any request is built. Pins the isBlank contract that a bare `""` IT
        // would not catch if the guard were ever downgraded to isEmpty().
        stubFor(post(urlPathEqualTo(collectionPath)).willReturn(aResponse().withBody(entityBody)))

        assertThrows(PubNubException::class.java) {
            pubnub.dataSync.createRelationship(
                entityAId = "a1",
                entityBId = "b1",
                className = " ",
                classVersion = 1,
            ).sync()
        }

        verify(0, postRequestedFor(urlPathEqualTo(collectionPath)))
    }

    @Test
    fun create_whitespace_entityId_throws_and_never_reaches_the_wire() {
        // Same isBlank contract for the required entity ids: a whitespace-only entityBId must throw before the wire.
        stubFor(post(urlPathEqualTo(collectionPath)).willReturn(aResponse().withBody(entityBody)))

        assertThrows(PubNubException::class.java) {
            pubnub.dataSync.createRelationship(
                entityAId = "a1",
                entityBId = " ",
                className = "Friendship",
                classVersion = 1,
            ).sync()
        }

        verify(0, postRequestedFor(urlPathEqualTo(collectionPath)))
    }

    @Test
    fun if_match_is_absent_when_not_supplied() {
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.removeRelationship(relationshipId = "r1").sync()

        verify(
            deleteRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("If-Match", absent()),
        )
    }
}

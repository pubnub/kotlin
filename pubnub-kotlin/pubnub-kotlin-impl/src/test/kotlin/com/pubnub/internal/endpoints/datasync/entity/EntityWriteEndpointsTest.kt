package com.pubnub.internal.endpoints.datasync.entity

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.absent
import com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.anyUrl
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
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.legacy.BaseTest
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * WireMock coverage for the /entities write endpoints (create/set/update/remove): the versioned entity
 * Content-Type, the JSON-Patch Content-Type, the `If-Match` header and the `/entities/{id}` paths, which live only
 * in the Retrofit annotations. Also guards that a blank id / class / empty patch never reaches the wire.
 */
class EntityWriteEndpointsTest : BaseTest() {
    private val collectionPath = "/v1/datasync/subkeys/mySubscribeKey/entities"
    private val itemPath = "/v1/datasync/subkeys/mySubscribeKey/entities/e1"
    private val entityContentType = "application/vnd.pubnub.objects.entity+json;version=1"
    private val jsonPatchContentType = "application/json-patch+json"
    private val entityBody = """{"status":200,"data":{"id":"e1"}}"""
    private val operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"))

    @Test
    fun create_posts_to_entities_with_the_versioned_entity_content_type() {
        stubFor(post(urlPathEqualTo(collectionPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.createEntity(className = "TestUser", classVersion = 1, entityId = "e1").sync()

        verify(
            postRequestedFor(urlPathEqualTo(collectionPath))
                .withHeader("Content-Type", equalTo(entityContentType)),
        )
    }

    @Test
    fun set_puts_to_entities_id_with_the_versioned_content_type_and_if_match() {
        stubFor(put(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.setEntity(entityId = "e1", classVersion = 1, status = "active", ifMatch = "etag-1").sync()

        verify(
            putRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(entityContentType))
                .withHeader("If-Match", equalTo("etag-1")),
        )
    }

    @Test
    fun update_patches_entities_id_with_the_json_patch_content_type_and_if_match() {
        stubFor(patch(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))

        pubnub.dataSync.updateEntity(entityId = "e1", operations = operations, ifMatch = "etag-2").sync()

        verify(
            patchRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(jsonPatchContentType))
                .withHeader("If-Match", equalTo("etag-2")),
        )
    }

    @Test
    fun remove_deletes_entities_id_with_if_match() {
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.removeEntity(entityId = "e1", ifMatch = "etag-3").sync()

        verify(
            deleteRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("If-Match", equalTo("etag-3")),
        )
    }

    @Test
    fun if_match_is_absent_when_not_supplied() {
        stubFor(put(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))
        stubFor(patch(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(entityBody)))
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.setEntity(entityId = "e1", classVersion = 1).sync()
        pubnub.dataSync.updateEntity(entityId = "e1", operations = operations).sync()
        pubnub.dataSync.removeEntity(entityId = "e1").sync()

        verify(putRequestedFor(urlPathEqualTo(itemPath)).withHeader("If-Match", absent()))
        verify(patchRequestedFor(urlPathEqualTo(itemPath)).withHeader("If-Match", absent()))
        verify(deleteRequestedFor(urlPathEqualTo(itemPath)).withHeader("If-Match", absent()))
    }

    @Test
    fun blank_className_on_create_throws_and_never_reaches_the_wire() {
        assertRejected(PubNubError.ENTITY_CLASS_MISSING) {
            pubnub.dataSync.createEntity(className = " ", classVersion = 1).sync()
        }
    }

    @Test
    fun blank_entityId_on_set_update_remove_throws_and_never_reaches_the_wire() {
        assertRejected(PubNubError.ENTITY_ID_MISSING) {
            pubnub.dataSync.setEntity(entityId = " ", classVersion = 1).sync()
        }
        assertRejected(PubNubError.ENTITY_ID_MISSING) {
            pubnub.dataSync.updateEntity(entityId = " ", operations = operations).sync()
        }
        assertRejected(PubNubError.ENTITY_ID_MISSING) {
            pubnub.dataSync.removeEntity(entityId = " ").sync()
        }
    }

    @Test
    fun empty_operations_on_update_throws_and_never_reaches_the_wire() {
        assertRejected(PubNubError.JSON_PATCH_OPERATIONS_MISSING) {
            pubnub.dataSync.updateEntity(entityId = "e1", operations = emptyList()).sync()
        }
    }

    private fun assertRejected(
        expected: PubNubError,
        call: () -> Unit,
    ) {
        val e = assertThrows(PubNubException::class.java) { call() }
        assertEquals(expected, e.pubnubError)
        verify(0, anyRequestedFor(anyUrl()))
    }
}

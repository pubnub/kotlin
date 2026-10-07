package com.pubnub.internal.endpoints.datasync.user

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
 * WireMock coverage for the /users write endpoints (create/set/update/remove): the versioned user Content-Type
 * (not the entity one), the JSON-Patch Content-Type, the `If-Match` header and the `/users/{id}` paths, which live
 * only in the Retrofit annotations. Also guards that a blank id / empty patch never reaches the wire.
 */
class UserWriteEndpointsTest : BaseTest() {
    private val collectionPath = "/v1/datasync/subkeys/mySubscribeKey/users"
    private val itemPath = "/v1/datasync/subkeys/mySubscribeKey/users/u1"
    private val userContentType = "application/vnd.pubnub.objects.user+json;version=1"
    private val jsonPatchContentType = "application/json-patch+json"
    private val userBody = """{"status":200,"data":{"id":"u1"}}"""
    private val operations = listOf(PNJsonPatchOperation(op = "replace", path = "/status", value = "inactive"))

    @Test
    fun create_posts_to_users_with_the_versioned_user_content_type() {
        stubFor(post(urlPathEqualTo(collectionPath)).willReturn(aResponse().withBody(userBody)))

        pubnub.dataSync.createUser(classVersion = 1, userId = "u1").sync()

        verify(
            postRequestedFor(urlPathEqualTo(collectionPath))
                .withHeader("Content-Type", equalTo(userContentType)),
        )
    }

    @Test
    fun set_puts_to_users_id_with_the_versioned_content_type_and_if_match() {
        stubFor(put(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(userBody)))

        pubnub.dataSync.setUser(userId = "u1", classVersion = 1, status = "active", ifMatch = "etag-1").sync()

        verify(
            putRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(userContentType))
                .withHeader("If-Match", equalTo("etag-1")),
        )
    }

    @Test
    fun update_patches_users_id_with_the_json_patch_content_type_and_if_match() {
        stubFor(patch(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(userBody)))

        pubnub.dataSync.updateUser(userId = "u1", operations = operations, ifMatch = "etag-2").sync()

        verify(
            patchRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("Content-Type", equalTo(jsonPatchContentType))
                .withHeader("If-Match", equalTo("etag-2")),
        )
    }

    @Test
    fun remove_deletes_users_id_with_if_match() {
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.removeUser(userId = "u1", ifMatch = "etag-3").sync()

        verify(
            deleteRequestedFor(urlPathEqualTo(itemPath))
                .withHeader("If-Match", equalTo("etag-3")),
        )
    }

    @Test
    fun if_match_is_absent_when_not_supplied() {
        stubFor(put(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(userBody)))
        stubFor(patch(urlPathEqualTo(itemPath)).willReturn(aResponse().withBody(userBody)))
        stubFor(delete(urlPathEqualTo(itemPath)).willReturn(aResponse().withStatus(200)))

        pubnub.dataSync.setUser(userId = "u1", classVersion = 1).sync()
        pubnub.dataSync.updateUser(userId = "u1", operations = operations).sync()
        pubnub.dataSync.removeUser(userId = "u1").sync()

        verify(putRequestedFor(urlPathEqualTo(itemPath)).withHeader("If-Match", absent()))
        verify(patchRequestedFor(urlPathEqualTo(itemPath)).withHeader("If-Match", absent()))
        verify(deleteRequestedFor(urlPathEqualTo(itemPath)).withHeader("If-Match", absent()))
    }

    @Test
    fun blank_userId_on_set_update_remove_throws_and_never_reaches_the_wire() {
        assertRejected(PubNubError.ENTITY_ID_MISSING) {
            pubnub.dataSync.setUser(userId = " ", classVersion = 1).sync()
        }
        assertRejected(PubNubError.ENTITY_ID_MISSING) {
            pubnub.dataSync.updateUser(userId = " ", operations = operations).sync()
        }
        assertRejected(PubNubError.ENTITY_ID_MISSING) {
            pubnub.dataSync.removeUser(userId = " ").sync()
        }
    }

    @Test
    fun empty_operations_on_update_throws_and_never_reaches_the_wire() {
        assertRejected(PubNubError.JSON_PATCH_OPERATIONS_MISSING) {
            pubnub.dataSync.updateUser(userId = "u1", operations = emptyList()).sync()
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

package com.pubnub.internal.models.server.access_manager.v3

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class GrantTokenRequestBodyTest {
    private val gson = Gson()

    @Test
    fun serializesDataSyncNamespacesAsLiteralColonKeys() {
        // given a mix of exact-resource and pattern DataSync grants across all three namespaces
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = "pam-debug-admin",
                dataSync =
                    listOf(
                        DataSyncGrant.entity("capy-001", get = true, update = true),
                        DataSyncGrant.entityPattern(".*", get = true, create = true),
                        DataSyncGrant.relationship("rel-1", delete = true),
                        DataSyncGrant.membership("user-123:channel-X", get = true),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val resources = json["permissions"].asJsonObject["resources"].asJsonObject
        val patterns = json["permissions"].asJsonObject["patterns"].asJsonObject

        // then — literal colon keys with the right bitmask ints (get=32, create=16, update=64, delete=8)
        assertEquals(32 + 64, intAt(resources, "datasync:entities", "capy-001"))
        assertEquals(8, intAt(resources, "datasync:relationships", "rel-1"))
        assertEquals(32, intAt(resources, "datasync:memberships", "user-123:channel-X"))
        assertEquals(32 + 16, intAt(patterns, "datasync:entities", ".*"))
    }

    @Test
    fun foldsGrantProjectionsIntoPnProjectionsMeta() {
        // given grants carrying projections across res + pat; relationship id keeps its colon separator
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = "pam-debug-admin",
                dataSync =
                    listOf(
                        DataSyncGrant.entity("user.A", get = true, projection = "admin"),
                        DataSyncGrant.entityPattern("user.*", get = true, projection = "__default__"),
                        DataSyncGrant.relationship("user.A:channel.X", get = true, projection = "admin"),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject
        val projections = meta["pn-projections"].asJsonObject
        val res = projections["res"].asJsonObject
        val pat = projections["pat"].asJsonObject

        // then — flat composite key "$namespace:$id" -> single projection string, colon in the id preserved verbatim
        assertEquals("admin", res["datasync:entities:user.A"].asString)
        assertEquals("admin", res["datasync:relationships:user.A:channel.X"].asString)
        assertEquals("__default__", pat["datasync:entities:user.*"].asString)
    }

    @Test
    fun omitsPnProjectionsWhenNoGrantCarriesProjection() {
        // given DataSync grants with no projection set
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
                dataSync = listOf(DataSyncGrant.entity("capy-001", get = true)),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject

        // then — no pn-projections block is emitted (meta stays the empty default)
        assertEquals(false, meta.has("pn-projections"))
    }

    @Test
    fun mergesGrantProjectionsWithCallerMeta() {
        // given caller meta with unrelated fields AND a pre-existing pn-projections entry
        val callerMeta =
            mapOf(
                "custom" to "keep-me",
                "pn-projections" to mapOf("res" to mapOf("datasync:entities:pre.existing" to "reader")),
            )
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = callerMeta,
                uuid = null,
                dataSync = listOf(DataSyncGrant.entity("user.A", get = true, projection = "admin")),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject
        val res = meta["pn-projections"].asJsonObject["res"].asJsonObject

        // then — caller's unrelated field and pre-existing projection are preserved; grant entry is added
        assertEquals("keep-me", meta["custom"].asString)
        assertEquals("reader", res["datasync:entities:pre.existing"].asString)
        assertEquals("admin", res["datasync:entities:user.A"].asString)
    }

    @Test
    fun throwsWhenProjectionUsedWithNonMapMeta() {
        // given a non-map (POJO) meta that cannot carry pn-projections, and a grant that does carry a projection
        val nonMapMeta = "just-a-string-meta"

        // when / then — the SDK must fail loudly rather than silently discard the caller's meta
        val exception =
            assertThrows(PubNubException::class.java) {
                GrantTokenRequestBody.of(
                    ttl = 60,
                    channels = emptyList(),
                    groups = emptyList(),
                    uuids = emptyList(),
                    meta = nonMapMeta,
                    uuid = null,
                    dataSync = listOf(DataSyncGrant.entity("user.A", get = true, projection = "admin")),
                )
            }
        assertTrue(exception.errorMessage!!.contains("pn-projections"))
    }

    @Test
    fun leavesNonMapMetaUntouchedWhenNoGrantCarriesProjection() {
        // given a non-map meta but NO grant carrying a projection — the guard must not fire
        val nonMapMeta = "just-a-string-meta"
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = nonMapMeta,
                uuid = null,
                dataSync = listOf(DataSyncGrant.entity("capy-001", get = true)),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject

        // then — the caller's non-map meta is passed through verbatim
        assertEquals(nonMapMeta, json["permissions"].asJsonObject["meta"].asString)
    }

    private fun intAt(
        obj: JsonObject,
        namespaceKey: String,
        id: String,
    ): Int = obj[namespaceKey].asJsonObject[id].asInt
}

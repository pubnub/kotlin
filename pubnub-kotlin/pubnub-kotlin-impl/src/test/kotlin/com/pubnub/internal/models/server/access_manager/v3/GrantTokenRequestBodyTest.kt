package com.pubnub.internal.models.server.access_manager.v3

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.UserGrant
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

    @Test
    fun userGrantLandsInUsersBucketNotUuids() {
        // given an exact-resource user grant and a user pattern grant
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = "pam-debug-admin",
                users =
                    listOf(
                        UserGrant.id("user-A", get = true, update = true, delete = true),
                        UserGrant.pattern("user-.*", get = true),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val resources = json["permissions"].asJsonObject["resources"].asJsonObject
        val patterns = json["permissions"].asJsonObject["patterns"].asJsonObject

        // then — the exact grant lands in resources.users (get=32, update=64, delete=8) and NOT in resources.uuids
        assertEquals(32 + 64 + 8, resources["users"].asJsonObject["user-A"].asInt)
        assertEquals(false, resources["uuids"].asJsonObject.has("user-A"))
        // and the pattern grant lands in patterns.users (get=32)
        assertEquals(32, patterns["users"].asJsonObject["user-.*"].asInt)
        assertEquals(false, patterns["uuids"].asJsonObject.has("user-.*"))
    }

    @Test
    fun orMergesBitmasksForDuplicateResourceIds() {
        // given two channel grants for the same id, each carrying a different permission bit
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels =
                    listOf(
                        ChannelGrant.name("x", read = true),
                        ChannelGrant.name("x", get = true),
                        ChannelGrant.name("other", write = true),
                    ),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val channels = json["permissions"].asJsonObject["resources"].asJsonObject["channels"].asJsonObject

        // then — the duplicate id is OR-merged (READ=1 or GET=32), not last-wins; distinct ids stay separate
        assertEquals(1 + 32, channels["x"].asInt)
        assertEquals(2, channels["other"].asInt)
    }

    @Test
    fun orMergesBitmasksForDuplicatePatternIds() {
        // given two channel pattern grants for the same pattern, each with a different bit
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels =
                    listOf(
                        ChannelGrant.pattern("chan-.*", read = true),
                        ChannelGrant.pattern("chan-.*", get = true),
                    ),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val channels = json["permissions"].asJsonObject["patterns"].asJsonObject["channels"].asJsonObject

        // then — the pattern pair is OR-merged (READ=1 or GET=32)
        assertEquals(1 + 32, channels["chan-.*"].asInt)
    }

    @Test
    fun foldsUserGrantProjectionUnderDatasyncUsersKey() {
        // given a user grant (exact) and a user pattern grant, both carrying a projection
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
                users =
                    listOf(
                        UserGrant.id("user-123", get = true, projection = "private"),
                        UserGrant.pattern("user-.*", get = true, projection = "admin"),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val projections = json["permissions"].asJsonObject["meta"].asJsonObject["pn-projections"].asJsonObject

        // then — the user's projection key uses the datasync:users namespace (permission stays in the users bucket)
        assertEquals("private", projections["res"].asJsonObject["datasync:users:user-123"].asString)
        assertEquals("admin", projections["pat"].asJsonObject["datasync:users:user-.*"].asString)
    }

    @Test
    fun foldsChannelGrantProjectionUnderDatasyncChannelsKey() {
        // given a channel grant carrying a projection
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = listOf(ChannelGrant.name("chan-A", get = true, projection = "admin")),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val projections = json["permissions"].asJsonObject["meta"].asJsonObject["pn-projections"].asJsonObject

        // then — the channel's projection key uses the datasync:channels namespace
        assertEquals("admin", projections["res"].asJsonObject["datasync:channels:chan-A"].asString)
    }

    @Test
    fun omitsPnProjectionsWhenUserAndChannelGrantsHaveNoProjection() {
        // given user/channel grants without any projection
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = listOf(ChannelGrant.name("chan-A", read = true)),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
                users = listOf(UserGrant.id("user-123", get = true)),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject

        // then — no pn-projections block is emitted
        assertEquals(false, meta.has("pn-projections"))
    }

    private fun intAt(
        obj: JsonObject,
        namespaceKey: String,
        id: String,
    ): Int = obj[namespaceKey].asJsonObject[id].asInt
}

package com.pubnub.internal.models.server.access_manager.v3

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun throwsWhenCallerMetaContainsPnProjectionsAndGrantCarriesProjection() {
        val callerMeta =
            mapOf(
                "custom" to "keep-me",
                "pn-projections" to mapOf("res" to mapOf("datasync:entities:pre.existing" to "reader")),
            )

        // when / then — pn-projections is owned by the SDK; the caller can't add to it through meta
        val exception =
            assertThrows(PubNubException::class.java) {
                GrantTokenRequestBody.of(
                    ttl = 60,
                    channels = emptyList(),
                    groups = emptyList(),
                    uuids = emptyList(),
                    meta = callerMeta,
                    uuid = null,
                    dataSync = listOf(DataSyncGrant.entity("user.A", get = true, projection = "admin")),
                )
            }
        assertTrue(exception.errorMessage!!.contains("pn-projections"))
    }

    @Test
    fun throwsWhenCallerMetaContainsPnProjectionsWithoutProjectionGrants() {
        // regression: the check must fire even when no grant carries a projection (the early return used to pass
        // the caller's pn-projections through untouched)
        val callerMeta = mapOf("pn-projections" to mapOf("res" to mapOf("datasync:entities:e1" to "admin")))

        val exception =
            assertThrows(PubNubException::class.java) {
                GrantTokenRequestBody.of(
                    ttl = 60,
                    channels = listOf(ChannelGrant.name("ch-1", read = true)),
                    groups = emptyList(),
                    uuids = emptyList(),
                    meta = callerMeta,
                    uuid = null,
                    dataSync = emptyList(),
                )
            }
        assertTrue(exception.errorMessage!!.contains("pn-projections"))
    }

    @Test
    fun keepsCallerMetaAlongsideGeneratedProjections() {
        // given caller meta with an unrelated field
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = mapOf("custom" to "keep-me"),
                uuid = null,
                dataSync = listOf(DataSyncGrant.entity("user.A", get = true, projection = "admin")),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject
        val res = meta["pn-projections"].asJsonObject["res"].asJsonObject

        // then — caller's field is kept and the block holds only the grant entry
        assertEquals("keep-me", meta["custom"].asString)
        assertEquals(setOf("datasync:entities:user.A"), res.keySet())
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
    fun dataSyncChannelAndUserGrantsLandInPlainBuckets() {
        // given DataSync channel/user grants, exact and pattern
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
                        DataSyncGrant.user("user-A", get = true, update = true, delete = true),
                        DataSyncGrant.userPattern("user-.*", get = true),
                        DataSyncGrant.channel("chat-1", get = true, create = true),
                        DataSyncGrant.channelPattern("chat-.*", update = true),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val resources = json["permissions"].asJsonObject["resources"].asJsonObject
        val patterns = json["permissions"].asJsonObject["patterns"].asJsonObject

        // then — exact grants land in resources.users/channels, patterns in patterns.users/channels
        assertEquals(32 + 64 + 8, resources["users"].asJsonObject["user-A"].asInt)
        assertEquals(false, resources["uuids"].asJsonObject.has("user-A"))
        assertEquals(32, patterns["users"].asJsonObject["user-.*"].asInt)
        assertEquals(32 + 16, resources["channels"].asJsonObject["chat-1"].asInt)
        assertEquals(64, patterns["channels"].asJsonObject["chat-.*"].asInt)
        // and there is no datasync:channels / datasync:users bucket: those strings are pn-projections prefixes only
        for (block in listOf(resources, patterns)) {
            assertFalse(block.has("datasync:channels"))
            assertFalse(block.has("datasync:users"))
        }
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
    fun orMergesChannelGrantWithDataSyncChannelGrantOnSameId() {
        // given a pub/sub channel grant and a DataSync channel grant for the same id
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = listOf(ChannelGrant.name("x", read = true)),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
                dataSync = listOf(DataSyncGrant.channel("x", get = true, update = true)),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val channels = json["permissions"].asJsonObject["resources"].asJsonObject["channels"].asJsonObject

        // then — one entry carrying READ=1 | GET=32 | UPDATE=64
        assertEquals(1 + 32 + 64, channels["x"].asInt)
    }

    @Test
    fun foldsDataSyncUserAndChannelProjectionsUnderTheirNamespaceKeys() {
        // given user/channel grants (exact + pattern) carrying projections
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
                dataSync =
                    listOf(
                        DataSyncGrant.user("user-123", get = true, projection = "private"),
                        DataSyncGrant.userPattern("user-.*", get = true, projection = "admin"),
                        DataSyncGrant.channel("chan-A", get = true, projection = "admin"),
                        DataSyncGrant.channelPattern("chan-.*", get = true, projection = "reader"),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val projections = json["permissions"].asJsonObject["meta"].asJsonObject["pn-projections"].asJsonObject

        // then — keys use the datasync:users / datasync:channels namespaces (permissions stay in the plain buckets)
        assertEquals("private", projections["res"].asJsonObject["datasync:users:user-123"].asString)
        assertEquals("admin", projections["pat"].asJsonObject["datasync:users:user-.*"].asString)
        assertEquals("admin", projections["res"].asJsonObject["datasync:channels:chan-A"].asString)
        assertEquals("reader", projections["pat"].asJsonObject["datasync:channels:chan-.*"].asString)
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
                dataSync = listOf(DataSyncGrant.user("user-123", get = true), DataSyncGrant.channel("chan-B", get = true)),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject

        // then — no pn-projections block is emitted
        assertEquals(false, meta.has("pn-projections"))
    }

    @Test
    fun subscribeGrantsReadOnResolvedRefChannel() {
        // given subscribe grants for the default, an explicit __default__ and a named projection
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels =
                    listOf(
                        DataSyncGrant.subscribe("x"),
                        DataSyncGrant.subscribe("y", "__default__"),
                        DataSyncGrant.subscribe("x", "admin"),
                    ),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val channels = json["permissions"].asJsonObject["resources"].asJsonObject["channels"].asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject

        // then — READ=1 on the bare id for the default projection, on __admin__x for "admin"; no meta
        assertEquals(1, channels["x"].asInt)
        assertEquals(1, channels["y"].asInt)
        assertEquals(1, channels["__admin__x"].asInt)
        assertEquals(false, channels.has("__default__y"))
        assertEquals(false, meta.has("pn-projections"))
    }

    @Test
    fun channelProjectionGrantPlusSubscribeProduceSeparateEntries() {
        // given the REST-read + subscribe pair for the same channel and projection
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = listOf(DataSyncGrant.subscribe("x", "admin")),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
                dataSync = listOf(DataSyncGrant.channel("x", get = true, projection = "admin")),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val channels = json["permissions"].asJsonObject["resources"].asJsonObject["channels"].asJsonObject
        val res = json["permissions"].asJsonObject["meta"].asJsonObject["pn-projections"].asJsonObject["res"].asJsonObject

        // then — GET on the record id, READ on the ref-channel, no OR-merge across them
        assertEquals(32, channels["x"].asInt)
        assertEquals(1, channels["__admin__x"].asInt)
        // and the projection key uses the DataSync id, never the ref-channel name
        assertEquals("admin", res["datasync:channels:x"].asString)
        assertEquals(false, res.has("datasync:channels:__admin__x"))
    }

    @Test
    fun subscribePatternGrantsReadOnAnchoredRefChannelPattern() {
        // given subscribe pattern grants for the default and a named projection
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = listOf(DataSyncGrant.subscribePattern("x-.*"), DataSyncGrant.subscribePattern("x-.*", "admin")),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = null,
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val patterns = json["permissions"].asJsonObject["patterns"].asJsonObject["channels"].asJsonObject
        val meta = json["permissions"].asJsonObject["meta"].asJsonObject

        // then — anchored patterns with READ=1, no pn-projections
        assertEquals(1, patterns["^(?:x-.*)"].asInt)
        assertEquals(1, patterns["^__admin__(?:x-.*)"].asInt)
        assertEquals(false, meta.has("pn-projections"))
    }

    private fun intAt(
        obj: JsonObject,
        namespaceKey: String,
        id: String,
    ): Int = obj[namespaceKey].asJsonObject[id].asInt
}

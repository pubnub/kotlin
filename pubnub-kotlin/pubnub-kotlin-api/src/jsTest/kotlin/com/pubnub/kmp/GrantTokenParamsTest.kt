package com.pubnub.kmp

import com.pubnub.api.UserId
import com.pubnub.api.buildFlatGrantTokenParams
import com.pubnub.api.buildLegacyGrantTokenParams
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.TokenGrant
import com.pubnub.api.toPNToken
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import PubNub as PubNubJs

class GrantTokenParamsTest {
    private fun flat(
        vararg grants: TokenGrant,
        meta: CustomObject? = null,
    ): PubNubJs.GrantTokenParameters = buildFlatGrantTokenParams(60, UserId("me"), meta, grants.toList())

    private fun hasKey(
        obj: Any?,
        key: String,
    ): Boolean = obj != null && obj.asDynamic().hasOwnProperty(key) as Boolean

    private fun JsMap<PubNubJs.GrantTokenPermissions>?.permissions(id: String): PubNubJs.GrantTokenPermissions =
        assertNotNull(this?.toMap()?.get(id), "no permissions for $id")

    private fun JsMap<String>?.projection(id: String): String? = this?.toMap()?.get(id)

    // (a) regression: `create` used to be dropped
    @Test
    fun channelCreateIsForwarded() {
        val params = flat(ChannelGrant.name("c", create = true))

        assertEquals(true, params.resources!!.channels.permissions("c").create)
    }

    // (b)
    @Test
    fun eachNamespaceLandsInItsBucket() {
        val params =
            flat(
                ChannelGrant.name("ch", read = true),
                ChannelGroupGrant.id("grp", read = true),
                DataSyncGrant.channel("dsch", get = true),
                DataSyncGrant.user("u1", get = true),
                DataSyncGrant.entity("e1", get = true),
                DataSyncGrant.relationship("user.A:channel.X", get = true),
                DataSyncGrant.membership("m1", get = true),
                DataSyncGrant.channelPattern("dsch.*", get = true),
                DataSyncGrant.userPattern("u.*", get = true),
                DataSyncGrant.entityPattern("e.*", get = true),
                DataSyncGrant.relationshipPattern("r.*", get = true),
                DataSyncGrant.membershipPattern("m.*", get = true),
            )
        val resources = params.resources!!
        val patterns = params.patterns!!

        assertEquals(setOf("ch", "dsch"), resources.channels!!.toMap().keys)
        assertEquals(setOf("grp"), resources.groups!!.toMap().keys)
        assertEquals(setOf("u1"), resources.users!!.toMap().keys)
        assertEquals(setOf("e1"), resources.dataSync!!.entities!!.toMap().keys)
        assertEquals(setOf("user.A:channel.X"), resources.dataSync!!.relationships!!.toMap().keys)
        assertEquals(setOf("m1"), resources.dataSync!!.memberships!!.toMap().keys)

        assertEquals(setOf("dsch.*"), patterns.channels!!.toMap().keys)
        assertFalse(hasKey(patterns, "groups"))
        assertEquals(setOf("u.*"), patterns.users!!.toMap().keys)
        assertEquals(setOf("e.*"), patterns.dataSync!!.entities!!.toMap().keys)
        assertEquals(setOf("r.*"), patterns.dataSync!!.relationships!!.toMap().keys)
        assertEquals(setOf("m.*"), patterns.dataSync!!.memberships!!.toMap().keys)
    }

    // (c)
    @Test
    fun channelGrantAndDataSyncChannelGrantOnSameIdAreOrMerged() {
        val params =
            flat(
                ChannelGrant.name("x", read = true),
                DataSyncGrant.channel("x", get = true, update = true),
            )

        val x = params.resources!!.channels.permissions("x")
        assertEquals(true, x.read)
        assertEquals(true, x.get)
        assertEquals(true, x.update)
        assertEquals(false, x.write)
    }

    @Test
    fun duplicateDataSyncGrantsAreOrMergedPerSide() {
        val params =
            flat(
                DataSyncGrant.entity("e1", get = true),
                DataSyncGrant.entity("e1", update = true),
                DataSyncGrant.entityPattern("e.*", create = true),
                DataSyncGrant.entityPattern("e.*", delete = true),
            )

        val resource = params.resources!!.dataSync!!.entities.permissions("e1")
        assertEquals(true, resource.get)
        assertEquals(true, resource.update)
        val pattern = params.patterns!!.dataSync!!.entities.permissions("e.*")
        assertEquals(true, pattern.create)
        assertEquals(true, pattern.delete)
        assertEquals(false, pattern.get)
    }

    @Test
    fun laterGrantDoesNotClearEarlierBit() {
        val params =
            flat(
                ChannelGrant.name("x", read = true),
                ChannelGrant.name("x", write = true),
            )

        val x = params.resources!!.channels.permissions("x")
        assertEquals(true, x.read)
        assertEquals(true, x.write)
    }

    @Test
    fun legacyOverloadOrMergesDuplicateIds() {
        val params =
            buildLegacyGrantTokenParams(
                ttl = 60,
                meta = null,
                authorizedUUID = null,
                channels = listOf(ChannelGrant.name("c", read = true), ChannelGrant.name("c", create = true)),
                channelGroups = emptyList(),
                uuids = emptyList(),
            )

        val c = params.resources!!.channels.permissions("c")
        assertEquals(true, c.read)
        assertEquals(true, c.create)
    }

    @Test
    fun lastProjectionForSameIdWins() {
        val params =
            flat(
                DataSyncGrant.entity("e1", get = true, projection = "first"),
                DataSyncGrant.entity("e1", get = true, projection = "second"),
            )

        assertEquals("second", params.dataSyncProjections!!.resources!!.entities.projection("e1"))
    }

    // (d)
    @Test
    fun projectionsAreRoutedByNamespaceAndSide() {
        val params =
            flat(
                DataSyncGrant.channel("x", get = true, projection = "admin"),
                DataSyncGrant.user("u1", get = true, projection = "admin"),
                DataSyncGrant.entity("e1", get = true, projection = "admin"),
                DataSyncGrant.relationship("user.A:channel.X", get = true, projection = "admin"),
                DataSyncGrant.channelPattern("x.*", get = true, projection = "public"),
            )
        val resources = params.dataSyncProjections!!.resources!!
        val patterns = params.dataSyncProjections!!.patterns!!

        assertEquals("admin", resources.channels.projection("x"))
        assertEquals("admin", resources.users.projection("u1"))
        assertEquals("admin", resources.entities.projection("e1"))
        assertEquals("admin", resources.relationships.projection("user.A:channel.X"))
        assertFalse(hasKey(resources, "memberships"))
        assertEquals("public", patterns.channels.projection("x.*"))
        assertFalse(hasKey(patterns, "entities"))
    }

    @Test
    fun nullProjectionEmitsNoProjections() {
        val params = flat(DataSyncGrant.channel("x", get = true))

        assertFalse(hasKey(params, "dataSyncProjections"))
    }

    @Test
    fun defaultProjectionNamesAreSentVerbatim() {
        val params =
            flat(
                DataSyncGrant.channel("x", get = true, projection = "default"),
                DataSyncGrant.channel("y", get = true, projection = "__default__"),
            )
        val channels = params.dataSyncProjections!!.resources!!.channels

        assertEquals("default", channels.projection("x"))
        assertEquals("__default__", channels.projection("y"))
    }

    // (e)
    @Test
    fun subscribeGrantsReadOnRefChannelWithoutProjections() {
        val params = flat(DataSyncGrant.subscribe("x", "admin"))

        assertEquals(true, params.resources!!.channels.permissions("__admin__x").read)
        assertFalse(hasKey(params, "dataSyncProjections"))
    }

    // (f)
    @Test
    fun flatOverloadNeverEmitsUuidsOrEmptyBuckets() {
        val params = flat(DataSyncGrant.user("u1", get = true), DataSyncGrant.userPattern("u.*", get = true))

        assertFalse(hasKey(params.resources, "uuids"))
        assertFalse(hasKey(params.patterns, "uuids"))
        assertFalse(hasKey(params.resources, "channels"))
        assertFalse(hasKey(params.resources, "groups"))
        assertFalse(hasKey(params.resources, "dataSync"))
    }

    // (h) deep meta conversion
    @Test
    fun nestedCallerMetaReachesParamsAsPlainJs() {
        val params =
            flat(
                ChannelGrant.name("c", read = true),
                meta = CustomObject(mapOf("custom" to mapOf("nested" to 1), "tags" to listOf("a", "b"))),
            )
        val meta = params.meta.asDynamic()

        assertEquals(1, meta.custom.nested as Int)
        assertTrue(js("Array.isArray")(meta.tags) as Boolean)
        assertEquals("b", meta.tags[1] as String)
    }

    // (g)
    @Test
    fun parsedTokenMapsPatternsUsersDataSyncAndCreate() {
        val parsed: dynamic = js("({})")
        parsed.version = 2
        parsed.timestamp = 1700000000
        parsed.ttl = 60
        parsed.authorized_uuid = "me"
        parsed.resources =
            js(
                """({
                channels: {
                    c1: { read: true, write: false, manage: false, "delete": false, get: false, update: false, join: false }
                },
                users: { u1: { create: true, get: true, update: false, "delete": false } },
                dataSync: {
                    entities: { e1: { create: true, get: false, update: false, "delete": false } },
                    relationships: { "user.A:channel.X": { create: false, get: true, update: false, "delete": false } },
                    memberships: { m1: { create: false, get: false, update: false, "delete": true } }
                }
            })"""
            )
        parsed.patterns =
            js(
                """({
                channels: {
                    "c.*": { read: true, write: false, manage: false, "delete": false, get: false, update: false, join: false }
                },
                dataSync: { entities: { "e.*": { create: true, get: true, update: false, "delete": false } } }
            })"""
            )

        val token = parsed.unsafeCast<PubNubJs.ParsedGrantToken>().toPNToken()

        assertEquals(2, token.version)
        assertEquals("me", token.authorizedUUID)
        assertEquals(PNToken.PNResourcePermissions(read = true), token.resources.channels["c1"])
        assertEquals(PNToken.PNResourcePermissions(create = true, get = true), token.resources.users["u1"])
        assertEquals(PNToken.PNResourcePermissions(create = true), token.resources.datasyncEntities["e1"])
        assertEquals(PNToken.PNResourcePermissions(get = true), token.resources.datasyncRelationships["user.A:channel.X"])
        assertEquals(PNToken.PNResourcePermissions(delete = true), token.resources.datasyncMemberships["m1"])
        assertEquals(PNToken.PNResourcePermissions(read = true), token.patterns.channels["c.*"])
        assertEquals(PNToken.PNResourcePermissions(create = true, get = true), token.patterns.datasyncEntities["e.*"])
        assertNull(token.meta)
        assertNull(token.projections)
    }

    @Test
    fun parsedTokenMapsMetaAndLiftsProjections() {
        val parsed: dynamic = js("({})")
        parsed.version = 2
        parsed.timestamp = 1700000000
        parsed.ttl = 60
        parsed.meta =
            js(
                """({
                plain: "v",
                "pn-projections": {
                    res: {
                        "datasync:entities:e1": "admin",
                        "datasync:relationships:user.A:channel.X": "admin",
                        "datasync:memberships:m1": "__default__",
                        "datasync:users:u1": "admin",
                        "datasync:channels:ch1": "admin"
                    },
                    pat: {
                        "datasync:entities:e.*": "public",
                        "datasync:relationships:user.A:.*": "public",
                        "datasync:memberships:m.*": "__default__",
                        "datasync:users:u.*": "public",
                        "datasync:channels:ch.*": "public"
                    }
                }
            })"""
            )

        val token = parsed.unsafeCast<PubNubJs.ParsedGrantToken>().toPNToken()

        val expectedMeta =
            mapOf(
                "plain" to "v",
                "pn-projections" to
                    mapOf(
                        "res" to
                            mapOf(
                                "datasync:entities:e1" to "admin",
                                "datasync:relationships:user.A:channel.X" to "admin",
                                "datasync:memberships:m1" to "__default__",
                                "datasync:users:u1" to "admin",
                                "datasync:channels:ch1" to "admin",
                            ),
                        "pat" to
                            mapOf(
                                "datasync:entities:e.*" to "public",
                                "datasync:relationships:user.A:.*" to "public",
                                "datasync:memberships:m.*" to "__default__",
                                "datasync:users:u.*" to "public",
                                "datasync:channels:ch.*" to "public",
                            ),
                    ),
            )
        assertEquals(expectedMeta, token.meta)

        val projections = assertNotNull(token.projections)
        assertEquals(mapOf("e1" to "admin"), projections.resources.entities)
        assertEquals(mapOf("user.A:channel.X" to "admin"), projections.resources.relationships)
        assertEquals(mapOf("m1" to "__default__"), projections.resources.memberships)
        assertEquals(mapOf("e.*" to "public"), projections.patterns.entities)
        assertEquals(mapOf("user.A:.*" to "public"), projections.patterns.relationships)
        assertEquals(mapOf("m.*" to "__default__"), projections.patterns.memberships)
        assertEquals(mapOf("u1" to "admin"), projections.resources.users)
        assertEquals(mapOf("ch1" to "admin"), projections.resources.channels)
        assertEquals(mapOf("u.*" to "public"), projections.patterns.users)
        assertEquals(mapOf("ch.*" to "public"), projections.patterns.channels)
    }
}

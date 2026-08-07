package com.pubnub.internal.managers

import co.nstant.`in`.cbor.CborBuilder
import co.nstant.`in`.cbor.CborEncoder
import com.pubnub.api.models.TokenBitmask
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.ByteArrayOutputStream
import java.math.BigInteger

class TokenParserTest {
    @Test
    fun parseTokenWithMeta() {
        val expectedParsedToken =
            PNToken(
                version = 2,
                timestamp = 1632335843,
                ttl = 1440,
                authorizedUUID = "myauthuuid1",
                resources =
                    PNToken.PNTokenResources(
                        channels =
                            mapOf(
                                "ch1" to
                                    PNToken.PNResourcePermissions(
                                        read = true, write = true, manage = true, delete = true, get = true, update = true, join = true, create = true,
                                    ),
                            ),
                        channelGroups =
                            mapOf(
                                "cg1" to
                                    PNToken.PNResourcePermissions(
                                        read = true, write = true, manage = true, delete = true, get = true, update = true, join = true, create = true,
                                    ),
                            ),
                        uuids =
                            mapOf(
                                "uuid1" to
                                    PNToken.PNResourcePermissions(
                                        read = true, write = true, manage = true, delete = true, get = true, update = true, join = true, create = true,
                                    ),
                            ),
                    ),
                patterns =
                    PNToken.PNTokenResources(
                        uuids =
                            mapOf(
                                "^\$" to
                                    PNToken.PNResourcePermissions(
                                        read = true,
                                        write = false,
                                        manage = false,
                                        delete = false,
                                        get = false,
                                        update = false,
                                        join = false,
                                    ),
                            ),
                    ),
                meta =
                    mapOf(
                        "score" to BigInteger.valueOf(100),
                        "color" to "red",
                        "author" to "pandu",
                    ),
            )
        val tokenWithMeta =
            "qEF2AkF0GmFLd-NDdHRsGQWgQ3Jlc6VEY2hhbqFjY2gxGP9DZ3JwoWNjZzEY_0N1c3KgQ3NwY6BEdXVpZKFldXVpZDEY_0NwYXSlRGNoYW6gQ2dycKBDdXNyoENzcGOgRHV1aWShYl4kAURtZXRho2VzY29yZRhkZWNvbG9yY3JlZGZhdXRob3JlcGFuZHVEdXVpZGtteWF1dGh1dWlkMUNzaWdYIP2vlxHik0EPZwtgYxAW3-LsBaX_WgWdYvtAXpYbKll3"
        val parsed = TokenParser().unwrapToken(tokenWithMeta)
        assertEquals(expectedParsedToken, parsed)
    }

    /**
     * Offline coverage for the DataSync decode path: the parser must read the literal `datasync:entities`,
     * `datasync:relationships` and `datasync:memberships` CBOR keys under both `res` and `pat`, and it must decode
     * the `CREATE = 16` bit (which historically was dropped by `PNResourcePermissions(Int)`).
     *
     */
    @Test
    fun parseTokenWithDataSyncResourcesAndPatterns() {
        val getUpdate = TokenBitmask.GET or TokenBitmask.UPDATE // 96
        val getCreate = TokenBitmask.GET or TokenBitmask.CREATE // 48
        val getOnly = TokenBitmask.GET // 32

        val token =
            encodeToken { map ->
                map.put("v", 2L)
                map.put("t", 1632335843L)
                map.put("ttl", 1440L)
                map.put("uuid", "myauthuuid1")
                // res: exact-resource grants
                map.putMap("res")
                    .putMap("chan").end()
                    .putMap("grp").end()
                    .putMap("uuid").end()
                    .putMap("datasync:entities").put("capy-001", getUpdate.toLong()).end()
                    .putMap("datasync:relationships").put("rel-1", getCreate.toLong()).end()
                    .putMap("datasync:memberships").put("user-123:channel-X", getOnly.toLong()).end()
                    .end()
                // pat: pattern grants
                map.putMap("pat")
                    .putMap("datasync:entities").put(".*", getOnly.toLong()).end()
                    .end()
                map.putMap("meta").end()
            }

        val parsed = TokenParser().unwrapToken(token)

        // resources
        assertEquals(
            PNToken.PNResourcePermissions(get = true, update = true),
            parsed.resources.datasyncEntities["capy-001"],
        )
        // CREATE bit must be decoded (regression guard for the historically-dropped bit 16)
        val relPerms = parsed.resources.datasyncRelationships["rel-1"]!!
        assertTrue(relPerms.get)
        assertTrue(relPerms.create)
        assertEquals(
            PNToken.PNResourcePermissions(get = true),
            parsed.resources.datasyncMemberships["user-123:channel-X"],
        )
        // patterns
        assertEquals(
            PNToken.PNResourcePermissions(get = true),
            parsed.patterns.datasyncEntities[".*"],
        )
    }

    /**
     * Offline coverage for the User (App Context v4) decode path: the parser must read the literal `usr` CBOR key
     * under both `res` and `pat`, and expose it as [PNToken.PNTokenResources.users]. Guards against the `usr`
     * grants being silently dropped (the pre-change behavior).
     */
    @Test
    fun parseTokenWithUserResourcesAndPatterns() {
        val getUpdate = TokenBitmask.GET or TokenBitmask.UPDATE // 96
        val getDelete = TokenBitmask.GET or TokenBitmask.DELETE // 40
        val getOnly = TokenBitmask.GET // 32

        val token =
            encodeToken { map ->
                map.put("v", 2L)
                map.put("t", 1632335843L)
                map.put("ttl", 1440L)
                map.put("uuid", "myauthuuid1")
                // res: exact-resource grants
                map.putMap("res")
                    .putMap("chan").end()
                    .putMap("grp").end()
                    .putMap("uuid").end()
                    .putMap("usr")
                    .put("user-A", getUpdate.toLong())
                    .put("user-B", getDelete.toLong())
                    .end()
                    .end()
                // pat: pattern grants
                map.putMap("pat")
                    .putMap("usr").put("user-.*", getOnly.toLong()).end()
                    .end()
                map.putMap("meta").end()
            }

        val parsed = TokenParser().unwrapToken(token)

        // resources
        assertEquals(
            PNToken.PNResourcePermissions(get = true, update = true),
            parsed.resources.users["user-A"],
        )
        assertEquals(
            PNToken.PNResourcePermissions(get = true, delete = true),
            parsed.resources.users["user-B"],
        )
        // patterns
        assertEquals(
            PNToken.PNResourcePermissions(get = true),
            parsed.patterns.users["user-.*"],
        )
    }

    /**
     * A token that carries no `usr` grants decodes to an empty [PNToken.PNTokenResources.users] map (never null),
     * so existing tokens without the User namespace remain safe to consume.
     */
    @Test
    fun parseTokenWithoutUserResources() {
        val token =
            encodeToken { map ->
                map.put("v", 2L)
                map.put("t", 1632335843L)
                map.put("ttl", 1440L)
                map.put("uuid", "myauthuuid1")
                map.putMap("res").putMap("chan").end().end()
                map.putMap("pat").end()
                map.putMap("meta").end()
            }

        val parsed = TokenParser().unwrapToken(token)

        assertTrue(parsed.resources.users.isEmpty())
        assertTrue(parsed.patterns.users.isEmpty())
    }

    /**
     * The `pn-projections` meta block must be lifted into the typed [PNToken.projections] field, split by namespace,
     * with each composite `datasync:<type>:<id>` key reduced to its bare id — including relationship ids that
     * themselves contain a colon. The raw block must remain accessible inside [PNToken.meta] (additive, non-breaking).
     */
    @Test
    fun parseTokenWithProjections() {
        val token =
            encodeToken { map ->
                map.put("v", 2L)
                map.put("t", 1632335843L)
                map.put("ttl", 1440L)
                map.put("uuid", "myauthuuid1")
                map.putMap("res").end()
                map.putMap("pat").end()
                map.putMap("meta")
                    .putMap("pn-projections")
                    .putMap("res")
                    .put("datasync:entities:user.A", "admin")
                    .put("datasync:relationships:user.A:channel.X", "admin")
                    .put("datasync:memberships:user-123:channel-X", "reader")
                    .end()
                    .putMap("pat")
                    .put("datasync:entities:user.*", "__default__")
                    .end()
                    .end()
                    .end()
            }

        val parsed = TokenParser().unwrapToken(token)

        val projections = parsed.projections!!
        assertEquals("admin", projections.resources.entities["user.A"])
        assertEquals("admin", projections.resources.relationships["user.A:channel.X"])
        assertEquals("reader", projections.resources.memberships["user-123:channel-X"])
        assertEquals("__default__", projections.patterns.entities["user.*"])

        // Raw block is still present in meta (non-breaking guarantee).
        @Suppress("UNCHECKED_CAST")
        val meta = parsed.meta as Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        val rawBlock = meta["pn-projections"] as Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        val rawRes = rawBlock["res"] as Map<String, Any>
        assertEquals("admin", rawRes["datasync:entities:user.A"].toString())
    }

    /**
     * A token whose meta carries no `pn-projections` block decodes to a `null` [PNToken.projections] (distinct from
     * a present-but-empty block).
     */
    @Test
    fun parseTokenWithoutProjections() {
        val token =
            encodeToken { map ->
                map.put("v", 2L)
                map.put("t", 1632335843L)
                map.put("ttl", 1440L)
                map.put("uuid", "myauthuuid1")
                map.putMap("res").end()
                map.putMap("pat").end()
                map.putMap("meta").put("app_version", "2.0").end()
            }

        val parsed = TokenParser().unwrapToken(token)

        assertNull(parsed.projections)
    }

    private fun encodeToken(build: (co.nstant.`in`.cbor.builder.MapBuilder<CborBuilder>) -> Unit): String {
        val cborBuilder = CborBuilder()
        val map = cborBuilder.addMap()
        build(map)
        val baos = ByteArrayOutputStream()
        CborEncoder(baos).encode(map.end().build())
        return com.pubnub.internal.vendor.Base64.encodeToString(
            baos.toByteArray(),
            com.pubnub.internal.vendor.Base64.URL_SAFE or com.pubnub.internal.vendor.Base64.NO_WRAP,
        )
    }
}

package com.pubnub.internal.managers

import co.nstant.`in`.cbor.CborBuilder
import co.nstant.`in`.cbor.CborEncoder
import com.pubnub.api.models.TokenBitmask
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
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

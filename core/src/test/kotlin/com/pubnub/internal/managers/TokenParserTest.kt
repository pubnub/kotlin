package com.pubnub.internal.managers

import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import java.math.BigInteger

class TokenParserTest {

    @Test
    fun parseTokenWithMeta() {
        val expectedParsedToken = PNToken(
            version = 2,
            timestamp = 1632335843,
            ttl = 1440,
            authorizedUUID = "myauthuuid1",
            resources = PNToken.PNTokenResources(
                channels = mapOf(
                    "ch1" to PNToken.PNResourcePermissions(
                        read = true, write = true, manage = true, delete = true, get = true, update = true, join = true
                    )
                ),
                channelGroups = mapOf(
                    "cg1" to PNToken.PNResourcePermissions(
                        read = true, write = true, manage = true, delete = true, get = true, update = true, join = true
                    )
                ),
                uuids = mapOf(
                    "uuid1" to PNToken.PNResourcePermissions(
                        read = true, write = true, manage = true, delete = true, get = true, update = true, join = true
                    )
                )
            ),
            patterns = PNToken.PNTokenResources(
                uuids = mapOf(
                    "^\$" to PNToken.PNResourcePermissions(
                        read = true,
                        write = false,
                        manage = false,
                        delete = false,
                        get = false,
                        update = false,
                        join = false
                    )
                )
            ),
            meta = mapOf(
                "score" to BigInteger.valueOf(100), "color" to "red", "author" to "pandu"
            )
        )
        val tokenWithMeta =
            "qEF2AkF0GmFLd-NDdHRsGQWgQ3Jlc6VEY2hhbqFjY2gxGP9DZ3JwoWNjZzEY_0N1c3KgQ3NwY6BEdXVpZKFldXVpZDEY_0NwYXSlRGNoYW6gQ2dycKBDdXNyoENzcGOgRHV1aWShYl4kAURtZXRho2VzY29yZRhkZWNvbG9yY3JlZGZhdXRob3JlcGFuZHVEdXVpZGtteWF1dGh1dWlkMUNzaWdYIP2vlxHik0EPZwtgYxAW3-LsBaX_WgWdYvtAXpYbKll3"
        val parsed = TokenParser().unwrapToken(tokenWithMeta)
        assertEquals(expectedParsedToken, parsed)
    }
}

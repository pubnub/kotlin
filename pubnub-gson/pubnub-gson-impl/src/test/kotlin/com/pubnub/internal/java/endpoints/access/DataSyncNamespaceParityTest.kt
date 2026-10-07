package com.pubnub.internal.java.endpoints.access

import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant as JavaDataSyncGrant

/**
 * The Java-facing [JavaDataSyncGrant] namespace constants must duplicate the Kotlin [DataSyncNamespace] values
 * because the two live in different modules.
 *
 * This test is that single point of enforcement: if either side drifts, a token minted by one SDK and parsed by
 * the other would silently lose its DataSync grants. `pubnub-gson-impl` sees both modules, so it can assert equality.
 */
class DataSyncNamespaceParityTest {
    @Test
    fun javaConstantsMatchKotlinDataSyncNamespace() {
        assertEquals(DataSyncNamespace.ENTITIES, JavaDataSyncGrant.DATASYNC_ENTITIES)
        assertEquals(DataSyncNamespace.RELATIONSHIPS, JavaDataSyncGrant.DATASYNC_RELATIONSHIPS)
        assertEquals(DataSyncNamespace.MEMBERSHIPS, JavaDataSyncGrant.DATASYNC_MEMBERSHIPS)
        assertEquals(DataSyncNamespace.CHANNELS_PROJECTION, JavaDataSyncGrant.DATASYNC_CHANNELS)
        assertEquals(DataSyncNamespace.USERS_PROJECTION, JavaDataSyncGrant.DATASYNC_USERS)
    }

    @Test
    fun javaRefChannelMatchesKotlin() {
        for (projection in listOf(null, "default", "__default__", "admin", "a.b")) {
            assertEquals(
                "projection=$projection",
                DataSyncNamespace.refChannel("capy-1", projection),
                JavaDataSyncGrant.refChannel("capy-1", projection),
            )
        }
        assertEquals("capy-1", JavaDataSyncGrant.refChannel("capy-1", null))
        assertEquals("__admin__capy-1", JavaDataSyncGrant.refChannel("capy-1", "admin"))
    }

    @Test
    fun javaRefChannelPatternMatchesKotlin() {
        val cases =
            listOf(
                "capy-.*" to null,
                "capy-.*" to "default",
                "capy-.*" to "__default__",
                "capy-.*" to "admin",
                "^capy-.*" to "admin",
                "^capy-.*" to null,
                "a|b" to "admin",
                "p" to "a.b",
                "a\\)" to null,
                "[)]" to "admin",
                "(a|b)-.*" to "admin",
            )
        for ((pattern, projection) in cases) {
            assertEquals(
                "pattern=$pattern projection=$projection",
                DataSyncNamespace.refChannelPattern(pattern, projection),
                JavaDataSyncGrant.refChannelPattern(pattern, projection),
            )
        }
        assertEquals("^(?:capy-.*)", JavaDataSyncGrant.refChannelPattern("capy-.*", null))
        assertEquals("^__admin__(?:capy-.*)", JavaDataSyncGrant.refChannelPattern("^capy-.*", "admin"))
        assertEquals("^__a\\.b__(?:p)", JavaDataSyncGrant.refChannelPattern("p", "a.b"))
    }

    @Test
    fun blankInputsThrowOnBothSides() {
        for (blank in listOf("", " ")) {
            assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannel("capy-1", blank) }
            assertThrows(IllegalArgumentException::class.java) { JavaDataSyncGrant.refChannel("capy-1", blank) }
            assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("capy-.*", blank) }
            assertThrows(IllegalArgumentException::class.java) { JavaDataSyncGrant.refChannelPattern("capy-.*", blank) }
            assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern(blank, "admin") }
            assertThrows(IllegalArgumentException::class.java) { JavaDataSyncGrant.refChannelPattern(blank, "admin") }
        }
    }

    @Test
    fun invalidOrWrapperEscapingPatternsThrowOnBothSides() {
        for (pattern in listOf("^", "a)|.*", "(a")) {
            assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern(pattern) }
            assertThrows(IllegalArgumentException::class.java) { JavaDataSyncGrant.refChannelPattern(pattern, null) }
        }
    }
}

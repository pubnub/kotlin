package com.pubnub.api.models.consumer.access_manager.v3

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DataSyncNamespaceTest {
    @Test
    fun refChannelUsesBareIdForDefaultProjection() {
        assertEquals("capy-1", DataSyncNamespace.refChannel("capy-1"))
        assertEquals("capy-1", DataSyncNamespace.refChannel("capy-1", "default"))
        assertEquals("capy-1", DataSyncNamespace.refChannel("capy-1", "__default__"))
    }

    @Test
    fun refChannelPrefixesNamedProjection() {
        assertEquals("__admin__capy-1", DataSyncNamespace.refChannel("capy-1", "admin"))
    }

    @Test
    fun refChannelRejectsBlankProjection() {
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannel("capy-1", "") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannel("capy-1", " ") }
    }

    @Test
    fun refChannelPatternAnchorsDefaultProjectionWithoutPrefix() {
        assertEquals("^(?:capy-.*)", DataSyncNamespace.refChannelPattern("capy-.*"))
        assertEquals("^(?:capy-.*)", DataSyncNamespace.refChannelPattern("capy-.*", "default"))
        assertEquals("^(?:capy-.*)", DataSyncNamespace.refChannelPattern("capy-.*", "__default__"))
    }

    @Test
    fun refChannelPatternPrefixesNamedProjection() {
        assertEquals("^__admin__(?:capy-.*)", DataSyncNamespace.refChannelPattern("capy-.*", "admin"))
    }

    @Test
    fun refChannelPatternMovesLeadingAnchorInFrontOfPrefix() {
        assertEquals("^__admin__(?:capy-.*)", DataSyncNamespace.refChannelPattern("^capy-.*", "admin"))
        assertEquals("^(?:capy-.*)", DataSyncNamespace.refChannelPattern("^capy-.*"))
    }

    @Test
    fun refChannelPatternGroupsAlternation() {
        // without the group, `^__admin__a|b` would let `b` match any channel
        assertEquals("^__admin__(?:a|b)", DataSyncNamespace.refChannelPattern("a|b", "admin"))
    }

    @Test
    fun refChannelPatternEscapesRegexMetaCharsInProjection() {
        assertEquals("^__a\\.b__(?:p)", DataSyncNamespace.refChannelPattern("p", "a.b"))
    }

    @Test
    fun refChannelPatternMatchesRefChannelsButNotProjectionMirrors() {
        val defaultRegex = Regex(DataSyncNamespace.refChannelPattern("capy-.*"))
        val adminRegex = Regex(DataSyncNamespace.refChannelPattern("capy-.*", "admin"))

        assertEquals(true, defaultRegex.containsMatchIn("capy-1"))
        assertEquals(false, defaultRegex.containsMatchIn("__admin__capy-1"))
        assertEquals(true, adminRegex.containsMatchIn(DataSyncNamespace.refChannel("capy-1", "admin")))
        assertEquals(false, adminRegex.containsMatchIn("capy-1"))
    }

    @Test
    fun refChannelPatternRejectsPatternThatWouldCloseTheWrapper() {
        // `a)|.*` would otherwise become `^(?:a)|.*`, whose `.*` branch escapes the anchor and matches every channel
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("a)|.*") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("a)|.*", "admin") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("(a") }
    }

    @Test
    fun refChannelPatternAcceptsParenthesesThatDoNotCloseTheWrapper() {
        assertEquals("^(?:a\\))", DataSyncNamespace.refChannelPattern("a\\)"))
        assertEquals("^(?:[)])", DataSyncNamespace.refChannelPattern("[)]"))
        assertEquals("^__admin__(?:(a|b)-.*)", DataSyncNamespace.refChannelPattern("(a|b)-.*", "admin"))
    }

    @Test
    fun refChannelPatternRejectsBlankPatternOrProjection() {
        // a lone `^` strips to an empty body, `^(?:)`, which would match every channel
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("^") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern(" ", "admin") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("capy-.*", "") }
        assertThrows(IllegalArgumentException::class.java) { DataSyncNamespace.refChannelPattern("capy-.*", " ") }
    }
}

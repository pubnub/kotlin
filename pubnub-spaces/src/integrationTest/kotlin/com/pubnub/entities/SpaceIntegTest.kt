package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.SpaceKey
import com.pubnub.entities.models.consumer.space.SpacesResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SpaceIntegTest() {
    private lateinit var pubnub: PubNub

    private val SPACE_ID = "spaceIntegId"
    private val SPACE_ID_01: String = SPACE_ID + "1"
    private val SPACE_ID_02: String = SPACE_ID + "2"
    private val SPACE_NAME = "spaceIntegName"
    private val DESCRIPTION = "space description"
    private val CUSTOM: Map<String, String> = mapOf("favouritePet" to "mouse")

    @BeforeEach
    fun setUp() {
        val config = PNConfiguration("kotlin").apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
        }
        pubnub = PubNub(config)

        pubnub.removeSpace(spaceId = SPACE_ID_01).sync()
        pubnub.removeSpace(spaceId = SPACE_ID_02).sync()
    }

    @Test
    internal fun can_createSpace() {
        val spaceId = SPACE_ID_01
        val space: Space? = createSpace(spaceId)

        assertEquals(spaceId, space?.id)
        assertEquals(SPACE_NAME, space?.name)
        assertEquals(DESCRIPTION, space?.description)
        assertEquals(CUSTOM, space?.custom)
        assertTrue(space?.updated != null)
        assertTrue(space?.eTag != null)
    }

    @Test
    internal fun can_fetchSpace() {
        val spaceId = SPACE_ID_01
        createSpace(spaceId)

        val space: Space? = pubnub.fetchSpace(spaceId = spaceId, includeCustom = true).sync()

        assertEquals(spaceId, space?.id)
        assertEquals(SPACE_NAME, space?.name)
        assertEquals(DESCRIPTION, space?.description)
        assertEquals(CUSTOM, space?.custom)
        assertTrue(space?.updated != null)
        assertTrue(space?.eTag != null)
    }

    @Test
    internal fun can_fetchSpaces() {
        val spaceId01 = SPACE_ID_01
        createSpace(spaceId01)
        val spaceId02 = SPACE_ID_02
        createSpace(spaceId02)

        val spacesResult: SpacesResult? = pubnub.fetchSpaces(limit = 100, includeCount = true).sync()

        assertEquals(2, spacesResult?.data?.size)
        assertEquals(2, spacesResult?.totalCount)
        assertEquals(spaceId01, spacesResult?.data?.first()?.id)
        assertEquals(spaceId02, spacesResult?.data?.elementAt(1)?.id)
    }

    @Test
    internal fun can_removeSpace() {
        val spaceId01 = SPACE_ID_01
        createSpace(spaceId01)

        pubnub.removeSpace(spaceId = spaceId01).sync()

        val exception = assertThrows<PubNubException> {
            pubnub.fetchSpace(spaceId01).sync()
        }
        assertTrue(exception.errorMessage?.contains("Requested object was not found") == true)
    }

    @Test
    internal fun can_updateSpace() {
        val spaceId01 = SPACE_ID_01
        createSpace(spaceId01)
        val newName = "NewName"
        val newDescription = "NewDescription"
        pubnub.updateSpace(spaceId = spaceId01, name = newName, description = newDescription).sync()

        val space: Space? = pubnub.fetchSpace(spaceId01).sync()
        assertEquals(newName, space?.name)
        assertEquals(newDescription, space?.description)
    }

    @Test
    internal fun can_fetch_sorted_spaces() {
        val spaceId01 = SPACE_ID_01
        createSpace(spaceId01)
        val spaceId02 = SPACE_ID_02
        createSpace(spaceId02)

        val spacesResultAsc: SpacesResult? = pubnub.fetchSpaces(
            limit = 100,
            includeCount = true,
            sort = listOf(
                ResultSortKey.Asc(key = SpaceKey.ID)
            )
        ).sync()

        assertEquals(SPACE_ID_01, spacesResultAsc?.data?.first()?.id)
        assertEquals(SPACE_ID_02, spacesResultAsc?.data?.elementAt(1)?.id)

        val spacesResultDesc: SpacesResult? = pubnub.fetchSpaces(
            limit = 100,
            includeCount = true,
            sort = listOf(
                ResultSortKey.Desc(key = SpaceKey.ID)
            )
        ).sync()

        assertEquals(SPACE_ID_02, spacesResultDesc?.data?.first()?.id)
        assertEquals(SPACE_ID_01, spacesResultDesc?.data?.elementAt(1)?.id)
    }

    @AfterEach
    internal fun tearDown() {
        pubnub.removeSpace(spaceId = SPACE_ID_01).sync()
        pubnub.removeSpace(spaceId = SPACE_ID_02).sync()
    }

    private fun createSpace(spaceId: String): Space? {
        return pubnub.createSpace(
            spaceId = spaceId,
            name = SPACE_NAME,
            description = DESCRIPTION,
            custom = CUSTOM,
            includeCustom = true
        ).sync()
    }
}

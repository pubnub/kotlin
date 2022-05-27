package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.entities.models.consumer.space.RemoveSpaceResult
import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.space.SpaceResult
import com.pubnub.entities.models.consumer.space.SpacesResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SpaceExtensionKtTest {
    private lateinit var pubNub: PubNub

    private val SPACE_ID = "unitTestKT_spaceId1"
    private val SPACE_ID_02 = "unitTestKT_spaceId2"
    private val SPACE_NAME = "unitTestKT_spaceName"
    private val SPACE_DESCRIPTION = "space description"
    private val CUSTOM = mapOf("My favourite car" to "Syrena")
    private val UPDATED = "2022-05-24T08:11:49.398709Z"
    private val E_TAG = "AeWNuf6b3aHYeg"
    private val TYPE = "type"
    private val STATUS = "status"
    private val expectedSpace01 = createSpace(SPACE_ID)
    private val expectedSpace02 = createSpace(SPACE_ID_02)

    @MockK
    lateinit var getChannelMetadataEndpoint: GetChannelMetadata

    @MockK
    lateinit var setChannelMetadataEndpoint: SetChannelMetadata

    @MockK
    lateinit var removeChannelMetadataEndpoint: RemoveChannelMetadata

    @MockK
    lateinit var getAllChannelMetadataEndpoint: GetAllChannelMetadata

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration = PNConfiguration(PubNub.generateUUID())
        pubNub = spyk(PubNub(pnConfiguration))
    }

    @Test
    internal fun can_createSpace() {
        val pnChannelMetadataResult = PNChannelMetadataResult(200, createPnChannelMetadata(SPACE_ID))
        every { pubNub.setChannelMetadata(any(), any(), any(), any(), any(), any(), any()) } returns setChannelMetadataEndpoint
        every { setChannelMetadataEndpoint.sync() } returns pnChannelMetadataResult

        val createSpaceEndpoint: ExtendedRemoteAction<SpaceResult?> = pubNub.createSpace(
            spaceId = SPACE_ID,
            name = SPACE_NAME,
            description = SPACE_DESCRIPTION,
            custom = CUSTOM,
            includeCustom = true,
            type = TYPE,
            status = STATUS
        )
        val spaceResult = createSpaceEndpoint.sync()

        assertEquals(SpaceResult(status = 200, data = expectedSpace01), spaceResult)
    }

    @Test
    internal fun can_fetchSpace() {
        val pnChannelMetadataResult = PNChannelMetadataResult(200, createPnChannelMetadata(SPACE_ID))
        every { pubNub.getChannelMetadata(any(), any()) } returns getChannelMetadataEndpoint
        every { getChannelMetadataEndpoint.sync() } returns pnChannelMetadataResult

        val fetchSpaceEndpoint: ExtendedRemoteAction<SpaceResult?> = pubNub.fetchSpace(spaceId = SPACE_ID)
        val spaceResult: SpaceResult? = fetchSpaceEndpoint.sync()

        assertEquals(SpaceResult(status = 200, data = expectedSpace01), spaceResult)
    }

    @Test
    internal fun can_updateSpace() {
        val pnChannelMetadataResult = PNChannelMetadataResult(200, createPnChannelMetadata(SPACE_ID))
        every { pubNub.setChannelMetadata(any(), any(), any(), any(), any()) } returns setChannelMetadataEndpoint
        every { setChannelMetadataEndpoint.sync() } returns pnChannelMetadataResult

        val updateSpaceEndpoint: ExtendedRemoteAction<SpaceResult?> = pubNub.updateSpace(
            spaceId = SPACE_ID, name = SPACE_NAME, custom = CUSTOM, description = SPACE_DESCRIPTION
        )
        val spaceResult = updateSpaceEndpoint.sync()

        assertEquals(SpaceResult(status = 200, data = expectedSpace01), spaceResult)
    }

    @Test
    internal fun can_removeSpace() {
        every { pubNub.removeChannelMetadata(any()) } returns removeChannelMetadataEndpoint
        every { removeChannelMetadataEndpoint.sync() } returns PNRemoveMetadataResult(200)

        val removeSpaceEndpoint: ExtendedRemoteAction<RemoveSpaceResult?> = pubNub.removeSpace(spaceId = SPACE_ID)
        val removeSpaceResult = removeSpaceEndpoint.sync()

        assertEquals(200, removeSpaceResult?.status)
    }

    @Test
    internal fun can_fetchSpaces() {
        val pnChannelMetadataList = listOf(createPnChannelMetadata(SPACE_ID), createPnChannelMetadata(SPACE_ID_02))
        val pnChannelMetadataArrayResult = PNChannelMetadataArrayResult(status = 200, data = pnChannelMetadataList, totalCount = 2, null, null)
        every { pubNub.getAllChannelMetadata(any(), any(), any(), any(), any(), any()) } returns getAllChannelMetadataEndpoint
        every { getAllChannelMetadataEndpoint.sync() } returns pnChannelMetadataArrayResult

        val fetchSpacesEndpoint = pubNub.fetchSpaces(limit = 10)
        val spacesResult = fetchSpacesEndpoint.sync()

        assertEquals(
            SpacesResult(
                status = 200, totalCount = 2, data = listOf(expectedSpace01, expectedSpace02), next = null, prev = null
            ),
            spacesResult
        )
    }

    private fun createPnChannelMetadata(id: String): PNChannelMetadata {
        return PNChannelMetadata(
            id = id,
            name = SPACE_NAME,
            description = SPACE_DESCRIPTION,
            custom = CUSTOM,
            updated = UPDATED,
            eTag = E_TAG,
            type = TYPE,
            status = STATUS
        )
    }

    private fun createSpace(id: String) = Space(
        id = id,
        name = SPACE_NAME,
        description = SPACE_DESCRIPTION,
        updated = UPDATED,
        eTag = E_TAG,
        custom = CUSTOM,
        type = TYPE,
        status = STATUS
    )
}

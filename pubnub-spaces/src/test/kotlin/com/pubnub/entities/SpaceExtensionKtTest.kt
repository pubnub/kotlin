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
import com.pubnub.entities.models.consumer.space.SpaceResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SpaceExtensionKtTest {
    private var pubNub: PubNub? = null

    private val SPACE_ID = "unitTestKT_spaceId1"
    private val SPACE_ID_02 = "unitTestKT_spaceId2"
    private val SPACE_NAME = "unitTestKT_spaceName"
    private val SPACE_DESCRIPTION = "space description"
    private val CUSTOM = mapOf("My favourite car" to "Syrena")
    private val UPDATED = "2022-05-24T08:11:49.398709Z"
    private val E_TAG = "AeWNuf6b3aHYeg"

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
        every {
            pubNub?.setChannelMetadata(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns setChannelMetadataEndpoint
        every { setChannelMetadataEndpoint.sync() } returns pnChannelMetadataResult

        val createSpaceEndpoint: ExtendedRemoteAction<SpaceResult?>? = pubNub?.createSpace(
            spaceId = SPACE_ID,
            name = SPACE_NAME,
            description = SPACE_DESCRIPTION,
            custom = CUSTOM,
            includeCustom = true
        )
        val spaceResult = createSpaceEndpoint?.sync()

        assertEquals(200, spaceResult?.status)
        assertEquals(SPACE_ID, spaceResult?.data?.id)
        assertEquals(SPACE_NAME, spaceResult?.data?.name)
        assertEquals(SPACE_DESCRIPTION, spaceResult?.data?.description)
        assertEquals(CUSTOM, spaceResult?.data?.custom)
        assertEquals(UPDATED, spaceResult?.data?.updated)
        assertEquals(E_TAG, spaceResult?.data?.eTag)
    }

    @Test
    internal fun can_fetchSpace() {
        val pnChannelMetadataResult = PNChannelMetadataResult(200, createPnChannelMetadata(SPACE_ID))
        every { pubNub?.getChannelMetadata(any(), any()) } returns getChannelMetadataEndpoint
        every { getChannelMetadataEndpoint.sync() } returns pnChannelMetadataResult

        val fetchSpaceEndpoint: ExtendedRemoteAction<SpaceResult?>? = pubNub?.fetchSpace(spaceId = SPACE_ID)
        val spaceResult: SpaceResult? = fetchSpaceEndpoint?.sync()

        assertEquals(200, spaceResult?.status)
        assertEquals(SPACE_ID, spaceResult?.data?.id)
        assertEquals(SPACE_NAME, spaceResult?.data?.name)
        assertEquals(SPACE_DESCRIPTION, spaceResult?.data?.description)
        assertEquals(UPDATED, spaceResult?.data?.updated)
        assertEquals(E_TAG, spaceResult?.data?.eTag)
    }

    @Test
    internal fun can_updateSpace() {
        val pnChannelMetadataResult = PNChannelMetadataResult(200, createPnChannelMetadata(SPACE_ID))
        every {
            pubNub?.setChannelMetadata(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns setChannelMetadataEndpoint
        every { setChannelMetadataEndpoint.sync() } returns pnChannelMetadataResult

        val updateSpaceEndpoint: ExtendedRemoteAction<SpaceResult?>? = pubNub?.updateSpace(
            spaceId = SPACE_ID,
            name = SPACE_NAME,
            custom = CUSTOM,
            description = SPACE_DESCRIPTION
        )
        val spaceResult = updateSpaceEndpoint?.sync()

        assertEquals(200, spaceResult?.status)
        assertEquals(SPACE_ID, spaceResult?.data?.id)
        assertEquals(SPACE_NAME, spaceResult?.data?.name)
        assertEquals(SPACE_DESCRIPTION, spaceResult?.data?.description)
        assertEquals(CUSTOM, spaceResult?.data?.custom)
        assertEquals(UPDATED, spaceResult?.data?.updated)
        assertEquals(E_TAG, spaceResult?.data?.eTag)
    }

    @Test
    internal fun can_removeSpace() {
        every { pubNub?.removeChannelMetadata(any()) } returns removeChannelMetadataEndpoint
        every { removeChannelMetadataEndpoint.sync() } returns PNRemoveMetadataResult(200)

        val removeSpaceEndpoint: ExtendedRemoteAction<RemoveSpaceResult?>? =
            pubNub?.removeSpace(spaceId = SPACE_ID)
        val removeSpaceResult = removeSpaceEndpoint?.sync()

        assertEquals(200, removeSpaceResult?.status)
    }

    @Test
    internal fun can_fetchSpaces() {
        val pnChannelMetadataList = listOf(createPnChannelMetadata(SPACE_ID), createPnChannelMetadata(SPACE_ID_02))
        val pnChannelMetadataArrayResult = PNChannelMetadataArrayResult(status = 200, data = pnChannelMetadataList, totalCount = 2, null, null)
        every {
            pubNub?.getAllChannelMetadata(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns getAllChannelMetadataEndpoint
        every { getAllChannelMetadataEndpoint.sync() } returns pnChannelMetadataArrayResult

        val fetchSpacesEndpoint = pubNub?.fetchSpaces(limit = 10)
        val spacesResult = fetchSpacesEndpoint?.sync()

        assertEquals(200, spacesResult?.status)
        assertEquals(2, spacesResult?.totalCount)
        assertEquals(SPACE_ID, spacesResult?.data?.first()?.id)
        assertEquals(SPACE_NAME, spacesResult?.data?.first()?.name)
        assertEquals(SPACE_DESCRIPTION, spacesResult?.data?.first()?.description)
        assertEquals(CUSTOM, spacesResult?.data?.first()?.custom)
        assertEquals(UPDATED, spacesResult?.data?.first()?.updated)
        assertEquals(E_TAG, spacesResult?.data?.first()?.eTag)
        assertEquals(SPACE_ID_02, spacesResult?.data?.elementAt(1)?.id)
    }

    private fun createPnChannelMetadata(id: String): PNChannelMetadata {
        return PNChannelMetadata(
            id = id,
            name = SPACE_NAME,
            description = SPACE_DESCRIPTION,
            custom = CUSTOM,
            updated = UPDATED,
            eTag = E_TAG
        )
    }
}

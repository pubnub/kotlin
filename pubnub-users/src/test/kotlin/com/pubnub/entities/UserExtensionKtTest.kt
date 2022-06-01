package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.entities.models.consumer.user.RemoveUserResult
import com.pubnub.entities.models.consumer.user.UserResult
import com.pubnub.entities.models.consumer.user.UsersResult
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserExtensionKtTest {
    private lateinit var pubNub: PubNub

    private val USER_ID = "userId"
    private val USER_ID_02 = "userId02"
    private val USER_NAME = "userName"
    private val EXTERNAL_ID = "externalId"
    private val PROFILE_URL = "profileUrl"
    private val EMAIL = "user@user.com"
    private val CUSTOM = mapOf("My favourite sport" to "windsurfing")
    private val INCLUDE_CUSOTM = true
    private val UPDATED = "2022-05-24T08:11:49.398709Z"
    private val E_TAG = "AeWNuf6b3aHYeg"

    @MockK
    lateinit var setUUIDMetadataEndpoint: SetUUIDMetadata

    @MockK
    lateinit var getUUIDMetadataEndpoint: GetUUIDMetadata

    @MockK
    lateinit var removeUUIDMetadataEndpoint: RemoveUUIDMetadata

    @MockK
    lateinit var getAllUUIDMetadataEndpoint: GetAllUUIDMetadata

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val pnConfiguration = PNConfiguration(PubNub.generateUUID())
        pubNub = spyk(PubNub(pnConfiguration))
    }

    @Test
    internal fun can_createUser() {
        val pnUUIDMetadata = createPnuuidMetadata(USER_ID)
        val pnUUIDMetadataResult = PNUUIDMetadataResult(status = 200, data = pnUUIDMetadata)
        every { pubNub.setUUIDMetadata(any(), any(), any(), any(), any(), any(), any()) } returns setUUIDMetadataEndpoint
        every { setUUIDMetadataEndpoint.sync() } returns pnUUIDMetadataResult

        val createUserEndpoint: ExtendedRemoteAction<UserResult?> = pubNub.createUser(
            userId = USER_ID,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = CUSTOM,
            includeCustom = INCLUDE_CUSOTM
        )
        val userResult = createUserEndpoint.sync()

        assertEquals(200, userResult?.status)
        assertEquals(USER_ID, userResult?.data?.id)
        assertEquals(USER_NAME, userResult?.data?.name)
        assertEquals(EXTERNAL_ID, userResult?.data?.externalId)
        assertEquals(PROFILE_URL, userResult?.data?.profileUrl)
        assertEquals(EMAIL, userResult?.data?.email)
        assertEquals(CUSTOM, userResult?.data?.custom)
        assertEquals(UPDATED, userResult?.data?.updated)
        assertEquals(E_TAG, userResult?.data?.eTag)
    }

    @Test
    internal fun can_fetchUser() {
        val pnUUIDMetadata = createPnuuidMetadata(USER_ID)
        val pnUUIDMetadataResult = PNUUIDMetadataResult(status = 200, data = pnUUIDMetadata)
        every { pubNub.getUUIDMetadata(any(), any()) } returns getUUIDMetadataEndpoint
        every { getUUIDMetadataEndpoint.sync() } returns pnUUIDMetadataResult

        val fetchUserEndpoint: ExtendedRemoteAction<UserResult?> =
            pubNub.fetchUser(userId = USER_ID, includeCustom = true)
        val userResult = fetchUserEndpoint.sync()

        assertEquals(200, userResult?.status)
        assertEquals(USER_ID, userResult?.data?.id)
        assertEquals(USER_NAME, userResult?.data?.name)
        assertEquals(EXTERNAL_ID, userResult?.data?.externalId)
        assertEquals(PROFILE_URL, userResult?.data?.profileUrl)
        assertEquals(EMAIL, userResult?.data?.email)
        assertEquals(CUSTOM, userResult?.data?.custom)
        assertEquals(UPDATED, userResult?.data?.updated)
        assertEquals(E_TAG, userResult?.data?.eTag)
    }

    @Test
    internal fun can_updateUser() {
        val pnUUIDMetadata = createPnuuidMetadata(USER_ID)
        val pnUUIDMetadataResult = PNUUIDMetadataResult(status = 200, data = pnUUIDMetadata)
        every { pubNub.setUUIDMetadata(any(), any(), any(), any(), any(), any(), any()) } returns setUUIDMetadataEndpoint
        every { setUUIDMetadataEndpoint.sync() } returns pnUUIDMetadataResult

        val updateUserEndpoint: ExtendedRemoteAction<UserResult?> = pubNub.updateUser(
            userId = USER_ID,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = CUSTOM,
            includeCustom = INCLUDE_CUSOTM
        )
        val userResult = updateUserEndpoint.sync()

        assertEquals(200, userResult?.status)
        assertEquals(USER_ID, userResult?.data?.id)
        assertEquals(USER_NAME, userResult?.data?.name)
        assertEquals(EXTERNAL_ID, userResult?.data?.externalId)
        assertEquals(PROFILE_URL, userResult?.data?.profileUrl)
        assertEquals(EMAIL, userResult?.data?.email)
        assertEquals(CUSTOM, userResult?.data?.custom)
        assertEquals(UPDATED, userResult?.data?.updated)
        assertEquals(E_TAG, userResult?.data?.eTag)
    }

    @Test
    internal fun can_removeUser() {
        val pnRemoveMetadataResult = PNRemoveMetadataResult(status = 200)
        every { pubNub.removeUUIDMetadata(any()) } returns removeUUIDMetadataEndpoint
        every { removeUUIDMetadataEndpoint.sync() } returns pnRemoveMetadataResult

        val removeUserEndpoint: ExtendedRemoteAction<RemoveUserResult?> = pubNub.removeUser(userId = USER_ID)
        val removeUserResult = removeUserEndpoint.sync()

        assertEquals(300, removeUserResult?.status)
    }

    @Test
    internal fun can_fetchUsers() {
        val pnUUIDMetadataList = listOf(createPnuuidMetadata(USER_ID), createPnuuidMetadata(USER_ID_02))
        val pnUUIDMetadataArrayResult = PNUUIDMetadataArrayResult(status = 200, data = pnUUIDMetadataList, totalCount = 2, next = null, prev = null)
        every { pubNub.getAllUUIDMetadata(any(), any(), any(), any(), any(), any()) } returns getAllUUIDMetadataEndpoint
        every { getAllUUIDMetadataEndpoint.sync() } returns pnUUIDMetadataArrayResult

        val fetchUsersEndpoint: ExtendedRemoteAction<UsersResult?> = pubNub.fetchUsers()
        val usersResult = fetchUsersEndpoint.sync()

        assertEquals(200, usersResult?.status)
        assertEquals(USER_ID, usersResult?.data?.first()?.id)
        assertEquals(USER_NAME, usersResult?.data?.first()?.name)
        assertEquals(EXTERNAL_ID, usersResult?.data?.first()?.externalId)
        assertEquals(PROFILE_URL, usersResult?.data?.first()?.profileUrl)
        assertEquals(EMAIL, usersResult?.data?.first()?.email)
        assertEquals(CUSTOM, usersResult?.data?.first()?.custom)
        assertEquals(UPDATED, usersResult?.data?.first()?.updated)
        assertEquals(E_TAG, usersResult?.data?.first()?.eTag)
        assertEquals(USER_ID_02, usersResult?.data?.elementAt(1)?.id)
        assertEquals(2, usersResult?.totalCount)
    }

    private fun createPnuuidMetadata(userId: String): PNUUIDMetadata {
        val pnUUIDMetadata = PNUUIDMetadata(
            id = userId,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = CUSTOM,
            updated = UPDATED,
            eTag = E_TAG
        )
        return pnUUIDMetadata
    }
}

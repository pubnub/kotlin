package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.models.consumer.objects.ResultSortKey
import com.pubnub.entities.models.consumer.user.User
import com.pubnub.entities.models.consumer.user.UserKey
import com.pubnub.entities.models.consumer.user.UsersResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserIntegTest() {
    private lateinit var pubnub: PubNub

    private val USER_ID = "userInteg_id"
    private val USER_ID_01 = USER_ID + "1"
    private val USER_ID_02 = USER_ID + "2"

    private val USER_NAME = "unitTestKT_name"
    private val EXTERNAL_ID = "externalId"
    private val PROFILE_URL = "profileUrl"
    private val EMAIL = "email"
    private val CUSTOM = mapOf("favouriteNumber" to 1, "favouriteColour" to "green")
    private val TYPE = "type"
    private val STATUS = "status"

    @BeforeEach
    fun setUp() {
        val config = PNConfiguration("kotlin").apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
            IntegTestConf.origin?.let {
                origin = it
            }
        }
        pubnub = PubNub(config)
        pubnub.removeUser(userId = USER_ID_01).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
    }

    @Test
    internal fun can_createUser() {
        val userId = USER_ID_01
        val user: User? = createUser(userId)

        assertEquals(userId, user?.id)
        assertEquals(USER_NAME, user?.name)
        assertEquals(EXTERNAL_ID, user?.externalId)
        assertEquals(PROFILE_URL, user?.profileUrl)
        assertEquals(EMAIL, user?.email)
        assertTrue(user?.custom?.containsKey("favouriteNumber")!!)
        assertTrue(user.custom?.containsKey("favouriteColour")!!)
        assertTrue(user.updated != null)
        assertTrue(user.eTag != null)
    }

    @Test
    internal fun can_fetchUser() {
        val userId = USER_ID_01
        createUser(userId)

        val user: User? = pubnub.fetchUser(userId = userId, includeCustom = true).sync()

        assertEquals(userId, user?.id)
        assertEquals(USER_NAME, user?.name)
        assertEquals(EXTERNAL_ID, user?.externalId)
        assertEquals(PROFILE_URL, user?.profileUrl)
        assertEquals(EMAIL, user?.email)
        assertEquals(STATUS, user?.status)
        assertEquals(TYPE, user?.type)
        assertTrue(user?.custom?.containsKey("favouriteNumber")!!)
        assertTrue(user?.custom?.containsKey("favouriteColour")!!)
        assertTrue(user?.updated != null)
        assertTrue(user?.eTag != null)
    }

    @Test
    internal fun can_fetchUsers() {
        val userId01 = USER_ID_01
        createUser(userId01)
        val userId02 = USER_ID_02
        createUser(userId02)

        val usersResult: UsersResult? = pubnub.fetchUsers(limit = 100, includeCount = true, includeCustom = true).sync()

        assertEquals(2, usersResult?.data?.size)
        assertEquals(2, usersResult?.totalCount)
        assertEquals(USER_ID_01, usersResult?.data?.first()?.id)
        assertEquals(USER_ID_02, usersResult?.data?.elementAt(1)?.id)
    }

    @Test
    internal fun can_deleteUser() {
        val userId = USER_ID_01
        createUser(userId)

        pubnub.removeUser(userId = userId).sync()

        val exception = assertThrows<PubNubException> {
            pubnub.fetchUser(userId).sync()
        }
        assertTrue(exception.errorMessage?.contains("Requested object was not found") == true)
    }

    @Test
    internal fun can_updateUser() {
        val userId = USER_ID_01
        createUser(userId)
        val newName = "NewName"
        val newExternalId = "NewExternalId"
        val newProfileUrl = "NewProfileUrl"
        val newEmail = "NewEmail"
        pubnub.updateUser(
            userId = userId,
            name = newName,
            externalId = newExternalId,
            profileUrl = newProfileUrl,
            email = newEmail
        ).sync()

        val user: User? = pubnub.fetchUser(userId).sync()
        assertEquals(newName, user?.name)
        assertEquals(newExternalId, user?.externalId)
        assertEquals(newProfileUrl, user?.profileUrl)
        assertEquals(newEmail, user?.email)
    }

    @Test
    internal fun can_fetch_sorted_users() {
        val userId01 = USER_ID_01
        createUser(userId01)
        val userId02 = USER_ID_02
        createUser(userId02)

        val usersResultAsc: UsersResult? = pubnub.fetchUsers(
            limit = 100,
            includeCount = true,
            sort = listOf(ResultSortKey.Asc(key = UserKey.ID))
        ).sync()

        assertEquals(USER_ID_01, usersResultAsc?.data?.first()?.id)
        assertEquals(USER_ID_02, usersResultAsc?.data?.elementAt(1)?.id)

        val usersResultDesc: UsersResult? = pubnub.fetchUsers(
            limit = 100,
            includeCount = true,
            sort = listOf(ResultSortKey.Desc(key = UserKey.ID))
        ).sync()

        assertEquals(USER_ID_02, usersResultDesc?.data?.first()?.id)
        assertEquals(USER_ID_01, usersResultDesc?.data?.elementAt(1)?.id)
    }

    @AfterEach
    internal fun tearDown() {
        pubnub.removeUser(userId = USER_ID_01).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
    }

    private fun createUser(userId: String): User? {
        return pubnub.createUser(
            userId = userId,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = CUSTOM,
            includeCustom = true,
            type = TYPE,
            status = STATUS
        ).sync()
    }
}

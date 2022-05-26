package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.entities.models.consumer.user.UserResult
import com.pubnub.entities.models.consumer.user.UsersResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class UserIntegTest() {
    private lateinit var pubnub: PubNub

    private val USER_ID = "unitTestKT_id"
    private val USER_ID_01 = USER_ID + "1"
    private val USER_ID_02 = USER_ID + "2"

    private val USER_NAME = "unitTestKT_name"
    private val EXTERNAL_ID = "externalId"
    private val PROFILE_URL = "profileUrl"
    private val EMAIL = "email"
    private val CUSTOM = "{favouritePet = \"mouse\"}"

    @BeforeEach
    fun setUp() {
        val config = PNConfiguration("kotlin").apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
        }
        pubnub = PubNub(config)
        pubnub.removeUser(userId = USER_ID_01).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
    }

    @Test
    internal fun demo() {
        var config: PNConfiguration = PNConfiguration("kotlin").apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
        }
        val pubNub = PubNub(config)
        pubnub.createUser(
            userId = "userId",
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            //custom = CUSTOM
        ).sync()

        println()
    }

    @Test
    internal fun can_createUser() {
        val userId = USER_ID_01
        val setPnUserResult: UserResult? = createUser(userId)

        assertEquals(200, setPnUserResult?.status)
        assertEquals(userId, setPnUserResult?.data?.id)
        assertEquals(USER_NAME, setPnUserResult?.data?.name)
        assertEquals(EXTERNAL_ID, setPnUserResult?.data?.externalId)
        assertEquals(PROFILE_URL, setPnUserResult?.data?.profileUrl)
        assertEquals(EMAIL, setPnUserResult?.data?.email)
//        assertEquals(CUSTOM, getPnUserResult?.data?.custom )
        assertTrue(setPnUserResult?.data?.updated != null)
        assertTrue(setPnUserResult?.data?.eTag != null)
    }

    @Test
    internal fun can_fetchUser() {
        val userId = USER_ID_01
        createUser(userId)

        val getPnUserResult: UserResult? = pubnub.fetchUser(userId).sync()

        assertEquals(200, getPnUserResult?.status)
        assertEquals(userId, getPnUserResult?.data?.id)
        assertEquals(USER_NAME, getPnUserResult?.data?.name)
        assertEquals(EXTERNAL_ID, getPnUserResult?.data?.externalId)
        assertEquals(PROFILE_URL, getPnUserResult?.data?.profileUrl)
        assertEquals(EMAIL, getPnUserResult?.data?.email)
//        assertEquals(CUSTOM, getPnUserResult?.data?.custom )
        assertTrue(getPnUserResult?.data?.updated != null)
        assertTrue(getPnUserResult?.data?.eTag != null)
    }

    @Test
    internal fun can_fetchUsers() {
        val userId01 = USER_ID_01
        createUser(userId01)
        val userId02 = USER_ID_02
        createUser(userId02)

        val pnUserArrayResult: UsersResult? = pubnub.fetchUsers(limit = 100, includeCount = true).sync()

        assertEquals(200, pnUserArrayResult?.status)
        assertEquals(2, pnUserArrayResult?.data?.size)
        assertEquals(2, pnUserArrayResult?.totalCount)
        assertEquals(USER_ID_01, pnUserArrayResult?.data?.first()?.id)
        assertEquals(USER_ID_02, pnUserArrayResult?.data?.elementAt(1)?.id)
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

        val pnUserResult: UserResult? = pubnub.fetchUser(userId).sync()
        assertEquals(newName, pnUserResult?.data?.name)
        assertEquals(newExternalId, pnUserResult?.data?.externalId)
        assertEquals(newProfileUrl, pnUserResult?.data?.profileUrl)
        assertEquals(newEmail, pnUserResult?.data?.email)
    }

    @AfterEach
    internal fun tearDown() {
        pubnub.removeUser(userId = USER_ID_01).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
    }

    private fun createUser(userId: String): UserResult? {
        return pubnub.createUser(
            userId = userId,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            //custom = CUSTOM,
            //includeCustom = true
        ).sync()
    }
}

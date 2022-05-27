package com.pubnub.entities

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.entities.models.consumer.membership.SpaceDetailsLevel
import com.pubnub.entities.models.consumer.membership.SpaceIdWithCustom
import com.pubnub.entities.models.consumer.space.SpaceResult
import com.pubnub.entities.models.consumer.space.SpacesResult
import com.pubnub.entities.models.consumer.user.UserResult
import com.pubnub.entities.models.consumer.user.UsersResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MembershipIntegTest {
    private lateinit var pubnub: PubNub

    private val USER_ID = "user_id"
    private val USER_NAME = "user name"
    private val USER_ID_02 = "user_id02"
    private val EXTERNAL_ID = "externalId"
    private val PROFILE_URL = "prorileUrl"
    private val EMAIL = "emaiL@mail.com"

    private val SPACE_ID = "space_id"
    private val SPACE_ID_02 = "space_id02"
    private val SPACE_NAME = "space_name"
    private val SPACE_DESCRIPTION = "space_description"
    private val CUSTOM = mapOf("favouritePet" to "mouse")

    @BeforeEach
    fun setUp() {
        val config = PNConfiguration(USER_ID).apply {
            subscribeKey = IntegTestConf.subscribeKey
            publishKey = IntegTestConf.publishKey
        }
        pubnub = PubNub(config)

        pubnub.removeSpace(spaceId = SPACE_ID).sync()
        pubnub.removeSpace(spaceId = SPACE_ID_02).sync()
        pubnub.removeUser(userId = USER_ID).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
        pubnub.removeMembershipOfSpace(spaceId = SPACE_ID, listOf(USER_ID, USER_ID_02)).sync()
        pubnub.removeMembershipOfSpace(spaceId = SPACE_ID_02, listOf(USER_ID, USER_ID_02)).sync()
    }

    @Test
    internal fun can_addMembershipOfUser() {
        val userResult = createUser(USER_ID)
        val spaceResult01 = createSpace(SPACE_ID)
        val spaceResult02 = createSpace(SPACE_ID_02)
        val spaceIdWithCustomList: List<SpaceIdWithCustom> = listOf(SpaceIdWithCustom(SPACE_ID, CUSTOM), SpaceIdWithCustom(SPACE_ID_02, CUSTOM))
        val addMembershipsResult = pubnub.addMembershipOfUser(spaceIdWithCustomList = spaceIdWithCustomList, userid = USER_ID).sync()

        assertEquals(200, addMembershipsResult?.status)

        val membershipsResult = pubnub.fetchMembershipsOfUser(userId = USER_ID, includeCustom = true, includeSpaceDetails = SpaceDetailsLevel.SPACE_WITH_CUSTOM).sync()
        val pnChannelMembershipArrayResult = pubnub.getMemberships(uuid = USER_ID).sync()

        val spacesResult: SpacesResult? = pubnub.fetchSpaces(limit = 100, includeCount = true).sync()
        val usersResult: UsersResult? = pubnub.fetchUsers(limit = 100, includeCount = true).sync()
        println("")
    }


    @AfterEach
    internal fun tearDown() {
        pubnub.removeSpace(spaceId = SPACE_ID).sync()
        pubnub.removeSpace(spaceId = SPACE_ID_02).sync()
        pubnub.removeUser(userId = USER_ID).sync()
        pubnub.removeUser(userId = USER_ID_02).sync()
    }

    private fun createUser(userId: String): UserResult? {
        return pubnub.createUser(
            userId = userId,
            name = USER_NAME,
            externalId = EXTERNAL_ID,
            profileUrl = PROFILE_URL,
            email = EMAIL,
            custom = CUSTOM,
            includeCustom = true
        ).sync()
    }

    private fun createSpace(spaceId: String): SpaceResult? {
        return pubnub.createSpace(
            spaceId = spaceId,
            name = SPACE_NAME,
            description = SPACE_DESCRIPTION,
            custom = CUSTOM,
            includeCustom = true
        ).sync()
    }
}
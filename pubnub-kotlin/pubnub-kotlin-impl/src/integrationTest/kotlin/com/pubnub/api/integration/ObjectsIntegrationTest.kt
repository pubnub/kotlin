package com.pubnub.api.integration

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.MemberInclude
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.MembershipInclude
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.pubsub.objects.PNDeleteMembershipEventMessage
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.models.consumer.pubsub.objects.PNSetMembershipEventMessage
import com.pubnub.api.utils.PatchValue
import com.pubnub.test.CommonUtils.randomValue
import com.pubnub.test.subscribeToBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ObjectsIntegrationTest : BaseIntegrationTest() {
    private val testUserId01 = "ThisIsMyId01" + randomValue()
    private val testUserId02 = "ThisIsMyId02" + randomValue()
    val channel = "ThisIsMyChannel01" + randomValue()
    val channel02 = "ThisIsMyChannel02" + randomValue()
    val otherChannel = "OtherChannel" + randomValue()
    val status01 = "Status01" + randomValue()
    val status02 = "Status02" + randomValue()
    val type01 = "Type01" + randomValue()
    val type02 = "Type02" + randomValue()
    val noUpdated = ""
    val noEtag = ""

    @Test
    fun setGetAndRemoveChannelMetadata() {
        val setResult =
            pubnub.setChannelMetadata(
                channel = channel,
                name = randomValue(15),
                status = status01,
                type = type01,
            ).sync()

        val getAllResult = pubnub.getAllChannelMetadata(filter = "id == \"$channel\"").sync()
        val getSingleResult = pubnub.getChannelMetadata(channel = channel).sync()
        pubnub.removeChannelMetadata(channel = channel).sync()

        assertTrue(getAllResult.data.any { it.id == channel })
        assertEquals(setResult, getSingleResult)

        val getAllAfterRemovalResult = pubnub.getAllChannelMetadata(filter = "id == \"$channel\"").sync()

        assertTrue(getAllAfterRemovalResult.data.none { it.id == channel })
    }

    @Test
    fun setGetAndRemoveUUIDMetadata() {
        // maintenance task: remove all UserMetadata
        // removeAllUserMetadataWithPaging() // this is not finished yet

        val setResult =
            pubnub.setUUIDMetadata(
                uuid = testUserId01,
                name = randomValue(15),
                status = status01,
                type = type01,
            ).sync()

        assertEquals(status01, setResult.data?.status?.value)
        assertEquals(type01, setResult.data?.type?.value)

        val getAllResult = pubnub.getAllUUIDMetadata(filter = "id == \"$testUserId01\"").sync()
        val getSingleResult = pubnub.getUUIDMetadata(uuid = testUserId01).sync()
        pubnub.removeUUIDMetadata(uuid = testUserId01).sync()

        assertTrue(getAllResult.data.any { it.id == testUserId01 })
        assertEquals(setResult, getSingleResult)

        val getAllAfterRemovalResult = pubnub.getAllUUIDMetadata(filter = "id == \"$testUserId01\"").sync()

        assertTrue(getAllAfterRemovalResult.data.none { it.id == testUserId01 })
    }

    @Test
    fun addGetAndRemoveMembershipDeprecated() {
        val channels = listOf(PNChannelMembership.Partial(channelId = channel))
        val setResult =
            pubnub.setMemberships(
                channels = channels,
                uuid = testUserId01,
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
            ).sync()

        val getAllResult =
            pubnub.getMemberships(
                uuid = testUserId01,
                sort = listOf(PNSortKey.PNDesc(PNMembershipKey.CHANNEL_ID)),
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
            ).sync()

        assertEquals(setResult, getAllResult)

        pubnub.removeMemberships(
            channels = channels.map { it.channel },
            uuid = testUserId01,
            includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
        ).sync()

        val getAllAfterRemovalResult =
            pubnub.getMemberships(
                uuid = testUserId01,
                includeChannelDetails = PNChannelDetailsLevel.CHANNEL,
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.channel != null }.none { it.channel!!.id == channel })
    }

    @Test
    fun addGetAndRemoveMembershipWithStatusAndType() {
        val customKey = randomValue()
        val customValue = randomValue()
        val custom = mapOf(customKey to customValue)
        val channelMembership01 = PNChannelMembership.Partial(channel, custom = custom, status = status01, type = type01)
        val channelMembership02 = PNChannelMembership.Partial(channel02, custom = custom, status = status02, type = type02)

        val channels = listOf(channelMembership01, channelMembership02)

        val setResult: PNChannelMembershipArrayResult =
            pubnub.setMemberships(
                channels = channels,
                userId = testUserId01,
                include = MembershipInclude(
                    includeCustom = true,
                    includeStatus = true,
                    includeType = true,
                    includeChannel = true,
                    includeChannelCustom = true,
                    includeChannelType = true,
                    includeChannelStatus = true,
                    includeTotalCount = true
                ),
                sort = listOf(PNSortKey.PNDesc(PNMembershipKey.STATUS))
            ).sync()

        val setMembership01 = setResult.data.first { membership -> membership.status?.value == status01 }
        val setMembership02 = setResult.data.first { membership -> membership.status?.value == status02 }

        assertEquals(type01, setMembership01.type?.value)
        assertEquals(type02, setMembership02.type?.value)
        assertEquals(custom, setMembership02.custom?.value)
        assertEquals(status01, (setResult.data as ArrayList)[1].status?.value)
        assertEquals(status02, (setResult.data as ArrayList)[0].status?.value)

        val getAllResult: PNChannelMembershipArrayResult =
            pubnub.getMemberships(
                userId = testUserId01,
                include = MembershipInclude(includeType = true, includeStatus = true),
                sort = listOf(PNSortKey.PNAsc(PNMembershipKey.CHANNEL_ID)),
            ).sync()

        val allPnChannelMemberships = getAllResult.data as ArrayList
        assertEquals(channel, allPnChannelMemberships[0].channel.id)
        assertEquals(channel02, allPnChannelMemberships[1].channel.id)
        val getMembership01 = getAllResult.data.first { membership -> membership.status?.value == status01 }
        val getMembership02 = getAllResult.data.first { membership -> membership.status?.value == status02 }

        assertEquals(type01, getMembership01.type?.value)
        assertEquals(type02, getMembership02.type?.value)

        // returns remaining memberships
        val removeMembershipResult = pubnub.removeMemberships(
            channels = listOf(channel),
            userId = testUserId01,
            include = MembershipInclude(
                includeCustom = true,
                includeStatus = true,
                includeType = true,
                includeChannel = true,
                includeChannelCustom = true,
                includeChannelType = true,
                includeChannelStatus = true,
                includeTotalCount = true
            ),
            sort = listOf(PNSortKey.PNAsc(PNMembershipKey.TYPE))
        ).sync()
        assertEquals(channel02, removeMembershipResult.data.first().channel.id)
        assertEquals(status02, removeMembershipResult.data.first().status?.value)
        assertEquals(type02, removeMembershipResult.data.first().type?.value)

        val getAllAfterRemovalResult =
            pubnub.getMemberships(
                userId = testUserId01,
                include = MembershipInclude(includeChannel = true)
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.channel != null }.none { it.channel!!.id == channel })
    }

    @Test
    fun addGetAndRemoveMembershipWithStatusAndType_includeTypeFalse_includeStatusFalse() {
        val channel01 = PNChannelMembership.Partial(channelId = channel, status = status01, type = type01)
        val channel02 = PNChannelMembership.Partial(channelId = channel02, status = status02, type = type02)
        val channels = listOf(channel01, channel02)

        val setResult: PNChannelMembershipArrayResult =
            pubnub.setMemberships(
                channels = channels,
                userId = testUserId01,
            ).sync()

        val sortedMembership = sortPNChannelMembershipsByUpdatedValue(setResult.data)

        assertNull(status01, sortedMembership[0].status?.value)
        assertNull(status02, sortedMembership[1].status?.value)
        assertNull(type01, sortedMembership[0].type?.value)
        assertNull(type02, sortedMembership[1].type?.value)

        val getAllResult: PNChannelMembershipArrayResult =
            pubnub.getMemberships(
                userId = testUserId01,
            ).sync()

        val sortedGetAllResult: List<PNChannelMembership> = sortPNChannelMembershipsByUpdatedValue(getAllResult.data)

        assertNull(status01, sortedGetAllResult[0].status?.value)
        assertNull(status02, sortedGetAllResult[1].status?.value)
        assertNull(type01, sortedGetAllResult[0].type?.value)
        assertNull(type02, sortedGetAllResult[1].type?.value)

        pubnub.removeMemberships(
            channels = channels.map { it.channel },
            userId = testUserId01,
        ).sync()

        val getAllAfterRemovalResult =
            pubnub.getMemberships(
                userId = testUserId01,
                include = MembershipInclude(includeChannel = true)
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.channel != null }.none { it.channel!!.id == channel })
    }

    @Test
    fun addGetAndRemoveMember() {
        val users = listOf(
            PNMember.Partial(uuidId = testUserId01, custom = null, status = status01, type = type01),
            PNMember.Partial(uuidId = testUserId02, custom = null, status = status02, type = type02),
        )

        val setResult: PNMemberArrayResult =
            pubnub.setChannelMembers(
                channel = channel,
                users = users,
                include = MemberInclude(includeUser = true, includeStatus = true, includeType = true),
                sort = listOf(PNSortKey.PNAsc(PNMemberKey.STATUS))
            ).sync()
        val pnMembers = setResult.data as ArrayList
        assertEquals(status01, pnMembers[0].status?.value)
        assertEquals(status02, pnMembers[1].status?.value)

        val getAllResult =
            pubnub.getChannelMembers(
                channel = channel,
                include = MemberInclude(includeUser = true, includeStatus = true, includeType = true),
                sort = listOf(PNSortKey.PNAsc(PNMemberKey.TYPE))
            ).sync()

        val getPnMembers = getAllResult.data as ArrayList
        assertEquals(type01, getPnMembers[0].type?.value)
        assertEquals(type02, getPnMembers[1].type?.value)
        assertEquals(setResult, getAllResult)

        pubnub.removeChannelMembers(
            channel = channel,
            userIds = users.map { it.uuid },
            include = MemberInclude(includeUser = true, includeStatus = true, includeType = true)
        ).sync()

        val getAllAfterRemovalResult =
            pubnub.getChannelMembers(
                channel = channel,
                include = MemberInclude(includeUser = true, includeStatus = true, includeType = true)
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.uuid != null }.none { it.uuid!!.id == testUserId01 })
    }

    @Test
    fun addGetAndRemoveMemberDeprecated() {
        val uuids = listOf(PNMember.Partial(uuidId = testUserId01, custom = null, null))

        val setResult: PNMemberArrayResult =
            pubnub.setChannelMembers(
                channel = channel,
                uuids = uuids,
                includeUUIDDetails = PNUUIDDetailsLevel.UUID,
            ).sync()

        val getAllResult =
            pubnub.getChannelMembers(
                channel = channel,
                includeUUIDDetails = PNUUIDDetailsLevel.UUID,
            ).sync()

        assertEquals(setResult, getAllResult)

        pubnub.removeChannelMembers(
            channel = channel,
            uuids = uuids.map { it.uuid },
            includeUUIDDetails = PNUUIDDetailsLevel.UUID,
        ).sync()

        val getAllAfterRemovalResult =
            pubnub.getChannelMembers(
                channel = channel,
                includeUUIDDetails = PNUUIDDetailsLevel.UUID,
            ).sync()

        assertTrue(getAllAfterRemovalResult.data.filter { it.uuid != null }.none { it.uuid!!.id == testUserId01 })
    }

    @Test
    fun testListeningToSetChannelMembersAndRemoveChannelMembersEventsWithStatusAndType() {
        val countDownLatch = CountDownLatch(2)
        val capturedPNObjectEventResult = mutableListOf<PNObjectEventResult>()
        val uuids = listOf(PNMember.Partial(uuidId = testUserId01, custom = null, status = status01, type = type01))
        pubnub.addListener(
            listener = object : SubscribeCallback() {
                override fun status(pubnub: PubNub, status: PNStatus) {
                }

                override fun objects(pubnub: PubNub, result: PNObjectEventResult) {
                    capturedPNObjectEventResult.add(result)
                    countDownLatch.countDown()
                }
            }
        )

        pubnub.subscribeToBlocking(channel)

        pubnub.setChannelMembers(channel = channel, users = uuids).sync()
        pubnub.removeChannelMembers(channel = channel, userIds = listOf(testUserId01)).sync()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
        val setChannelMembersEventMessage = capturedPNObjectEventResult[0].extractedMessage as PNSetMembershipEventMessage
        val removeChannelMembersEventMessage = capturedPNObjectEventResult[1].extractedMessage as PNDeleteMembershipEventMessage
        assertEquals(channel, setChannelMembersEventMessage.data.channel)
        assertEquals(status01, setChannelMembersEventMessage.data.status?.value)
        assertEquals(type01, setChannelMembersEventMessage.data.type?.value)
        assertEquals(channel, removeChannelMembersEventMessage.data.channelId)
        assertEquals(testUserId01, removeChannelMembersEventMessage.data.uuid)
    }

    @Test
    fun testListeningToSetMembershipAndRemoveMembershipEventsWithStatusAndType() {
        val countDownLatch = CountDownLatch(2)
        val capturedPNObjectEventResult = mutableListOf<PNObjectEventResult>()
        val channels = listOf(PNChannelMembership.Partial(channelId = channel, status = status01, type = type01))
        pubnub.addListener(
            listener = object : SubscribeCallback() {
                override fun status(pubnub: PubNub, status: PNStatus) {
                }

                override fun objects(pubnub: PubNub, result: PNObjectEventResult) {
                    capturedPNObjectEventResult.add(result)
                    countDownLatch.countDown()
                }
            }
        )

        pubnub.subscribeToBlocking(channel)

        pubnub.setMemberships(channels = channels).sync()
        pubnub.removeMemberships(channels = channels.map { it.channel }).sync()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
        val setMembershipEventMessage = capturedPNObjectEventResult[0].extractedMessage as PNSetMembershipEventMessage
        val removeMembershipEventMessage = capturedPNObjectEventResult[1].extractedMessage as PNDeleteMembershipEventMessage
        assertEquals(channel, setMembershipEventMessage.data.channel)
        assertEquals(status01, setMembershipEventMessage.data.status?.value)
        assertEquals(type01, setMembershipEventMessage.data.type?.value)
        assertEquals(channel, removeMembershipEventMessage.data.channelId)
    }

    @Test
    fun testListeningToAllObjectsEvents() {
        val countDownLatch = CountDownLatch(9)
        val status = "active"
        val type = "user"
        val uuids = listOf(PNMember.Partial(uuidId = testUserId01, custom = null, status = status, type = type))
        val channels = listOf(PNChannelMembership.Partial(channelId = channel, status = status, type = type))

        pubnub.addListener(
            listener =
                object : SubscribeCallback() {
                    override fun status(
                        pubnub: PubNub,
                        pnStatus: PNStatus,
                    ) {}

                    override fun objects(
                        pubnub: PubNub,
                        event: PNObjectEventResult,
                    ) {
                        println(event)
                        countDownLatch.countDown()
                    }
                },
        )

        pubnub.subscribeToBlocking(channel)

        pubnub.setMemberships(channels = channels).sync()
        pubnub.setChannelMembers(channel = channel, uuids = uuids).sync()
        pubnub.setChannelMetadata(channel = channel, name = randomValue(15), status = status, type = type).sync()
        pubnub.setUUIDMetadata(name = randomValue(15), status = status, type = type).sync()

        pubnub.setChannelMetadata(channel = channel, description = "aaa", status = status, type = type).sync()
        pubnub.removeChannelMetadata(channel = channel).sync()
        pubnub.removeUUIDMetadata().sync()
        pubnub.removeChannelMembers(channel = channel, userIds = uuids.map { it.uuid }).sync()
        pubnub.removeMemberships(channels = channels.map { it.channel }).sync()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
    }

    @Test
    fun testListeningToAllObjectsEventsV2() {
        val countDownLatch = CountDownLatch(16)
        val uuids = listOf(PNMember.Partial(uuidId = testUserId01, custom = null, null))
        val channels =
            listOf(
                PNChannelMembership.Partial(channelId = channel),
            )

        val metadataSub = pubnub.channelMetadata(channel).subscription()
        val userMetadataSub = pubnub.userMetadata(channel).subscription()

        metadataSub.addListener(
            listener =
                object : com.pubnub.api.v2.callbacks.EventListener {
                    override fun objects(
                        pubnub: PubNub,
                        result: PNObjectEventResult,
                    ) {
                        countDownLatch.countDown()
                    }
                },
        )
        userMetadataSub.addListener(
            listener =
                object : com.pubnub.api.v2.callbacks.EventListener {
                    override fun objects(
                        pubnub: PubNub,
                        result: PNObjectEventResult,
                    ) {
                        countDownLatch.countDown()
                    }
                },
        )
        metadataSub.subscribe()
        userMetadataSub.subscribe()

        Thread.sleep(2000)

        pubnub.setMemberships(
            channels = channels,
        ).sync()
        pubnub.setChannelMembers(channel = channel, uuids = uuids).sync()
        pubnub.setChannelMetadata(channel = channel, name = randomValue(15)).sync()
        pubnub.setUUIDMetadata(name = randomValue(15)).sync()

        pubnub.setChannelMetadata(channel = channel, description = "aaa").sync()
        pubnub.removeChannelMetadata(channel = channel).sync()
        pubnub.removeUUIDMetadata().sync()
        pubnub.removeChannelMembers(channel = channel, userIds = uuids.map { it.uuid }).sync()
        pubnub.removeMemberships(channels = channels.map { it.channel }).sync()

        assertTrue(countDownLatch.await(5000, TimeUnit.MILLISECONDS))
    }

    // TODO: separate it
    @Test
    fun customDataIsPassedCorrectly() {
        val expectedUUID = "my_main_uud"
        val expectedName = "my_main_uuid_name"
        val expectedEmail = "my_main_uuid_email"
        val expectedExternalId = "my_main_uuid_ext_id"
        val expectedProfileUrl = "http://my_main_uuid_profile_url"
        val expectedCustom = hashMapOf("color" to "red", "foo" to "bar")

        pubnub.setUUIDMetadata(
            uuid = expectedUUID,
            name = expectedName,
            email = expectedEmail,
            externalId = expectedExternalId,
            profileUrl = expectedProfileUrl,
            custom = expectedCustom,
            includeCustom = true,
        ).sync().apply {
            assertEquals(expectedUUID, this.data?.id)
            assertEquals(expectedName, this.data?.name?.value)
            assertEquals(expectedEmail, this.data?.email?.value)
            assertEquals(expectedExternalId, this.data?.externalId?.value)
            assertEquals(expectedProfileUrl, this.data?.profileUrl?.value)
            assertEquals(expectedCustom, this.data?.custom?.value)
        }
    }

    @Test
    fun testManageMember() {
        pubnub.setChannelMembers(
            channel = channel,
            users = listOf(PNMember.Partial(uuidId = testUserId02, status = status01, type = type01)),
            include = MemberInclude(
                includeStatus = true,
                includeType = true,
            )
        ).sync()

        pubnub.manageChannelMembers(
            channel = channel,
            usersToSet = listOf(PNMember.Partial(uuidId = testUserId01, status = status01, type = type01)),
            userIdsToRemove = listOf(),
            include = MemberInclude(includeStatus = true, includeType = true)
        ).sync()

        val getAllResult =
            pubnub.getChannelMembers(
                channel = channel,
                include = MemberInclude(
                    includeUser = true,
                    includeType = true,
                    includeStatus = true
                )
            ).sync().data

        val otherTestUuidMatcher =
            PNMember(
                uuidMetadata(id = testUserId02),
                custom = null,
                status = PatchValue.of(status01),
                eTag = noEtag,
                updated = noUpdated,
                type = PatchValue.of(type01),
            )
        val testUuidMatcher =
            PNMember(
                uuidMetadata(id = testUserId01),
                custom = null,
                status = PatchValue.of(status01),
                eTag = noEtag,
                updated = noUpdated,
                type = PatchValue.of(type01),
            )

        assertThat(
            getAllResult.map { it.copy(updated = noUpdated, eTag = noEtag) },
            containsInAnyOrder(testUuidMatcher, otherTestUuidMatcher),
        )

        val removeResult =
            pubnub.manageChannelMembers(
                channel = channel,
                usersToSet = listOf(),
                userIdsToRemove = listOf(testUserId01, testUserId02),
            ).sync().data

        assertThat(removeResult, not(containsInAnyOrder(testUuidMatcher, otherTestUuidMatcher)))
    }

    @Test
    fun testManageMemberDeprecated() {
        pubnub.setChannelMembers(
            channel = channel,
            uuids = listOf(PNMember.Partial(uuidId = testUserId02, status = status01)),
        ).sync()

        pubnub.manageChannelMembers(
            channel = channel,
            usersToSet = listOf(PNMember.Partial(uuidId = testUserId01, status = status01)),
            userIdsToRemove = listOf(),
        ).sync()

        val getAllResult =
            pubnub.getChannelMembers(channel = channel, includeUUIDDetails = PNUUIDDetailsLevel.UUID).sync().data

        val otherTestUuidMatcher =
            PNMember(
                uuidMetadata(id = testUserId02),
                custom = null,
                status = PatchValue.of(status01),
                eTag = noEtag,
                updated = noUpdated,
                type = PatchValue.of(null),
            )
        val testUuidMatcher =
            PNMember(
                uuidMetadata(id = testUserId01),
                custom = null,
                status = PatchValue.of(status01),
                eTag = noEtag,
                updated = noUpdated,
                type = PatchValue.of(null),
            )

        assertThat(
            getAllResult.map { it.copy(updated = noUpdated, eTag = noEtag) },
            containsInAnyOrder(testUuidMatcher, otherTestUuidMatcher),
        )

        val removeResult =
            pubnub.manageChannelMembers(
                channel = channel,
                usersToSet = listOf(),
                userIdsToRemove = listOf(testUserId01, testUserId02),
            ).sync().data

        assertThat(removeResult, not(containsInAnyOrder(testUuidMatcher, otherTestUuidMatcher)))
    }

    @Test
    fun testManageMembership() {
        pubnub.setMemberships(
            channels = listOf(PNChannelMembership.Partial(channelId = channel, status = status01)),
            userId = testUserId01
        ).sync()

        val manageMembershipsResult = pubnub.manageMemberships(
            channelsToSet = listOf(PNChannelMembership.Partial(channelId = otherChannel, status = status01, type = type01)),
            channelsToRemove = listOf(),
            userId = testUserId01,
            include = MembershipInclude(includeStatus = true, includeType = true)
        ).sync()

        val sortedGetAllResult: List<PNChannelMembership> = sortPNChannelMembershipsByUpdatedValue(manageMembershipsResult.data)

        assertEquals(status01, sortedGetAllResult[0].status?.value)
        assertEquals(status01, sortedGetAllResult[1].status?.value)
        assertNull(null, sortedGetAllResult[0].type?.value)
        assertEquals(type01, sortedGetAllResult[1].type?.value)

        val getAllResult =
            pubnub.getMemberships(uuid = testUserId01, includeChannelDetails = PNChannelDetailsLevel.CHANNEL).sync().data

        val channelMatcher =
            PNChannelMembership(
                channelMetadata(id = channel),
                custom = null,
                status = PatchValue.of(status01),
                type = PatchValue.of(null),
                eTag = noEtag,
                updated = noUpdated,
            )
        val otherChannelMatcher =
            PNChannelMembership(
                channelMetadata(id = otherChannel),
                custom = null,
                status = PatchValue.of(status01),
                type = PatchValue.of(type01),
                eTag = noEtag,
                updated = noUpdated,
            )

        assertThat(
            getAllResult.map { it.copy(updated = noUpdated, eTag = noEtag) },
            containsInAnyOrder(channelMatcher, otherChannelMatcher),
        )

        val removeResult =
            pubnub.manageMemberships(
                channelsToSet = listOf(),
                channelsToRemove = listOf(channel, otherChannel),
                userId = testUserId01,
            ).sync().data

        assertThat(
            removeResult,
            not(containsInAnyOrder(channelMatcher, otherChannelMatcher)),
        )
    }

    private fun uuidMetadata(id: String): PNUUIDMetadata {
        return PNUUIDMetadata(
            id = id,
            name = null,
            externalId = null,
            profileUrl = null,
            email = null,
            custom = null,
            updated = null,
            eTag = null,
            type = null,
            status = null,
        )
    }

    private fun channelMetadata(id: String): PNChannelMetadata {
        return PNChannelMetadata(
            id = id,
            name = null,
            description = null,
            custom = null,
            updated = null,
            eTag = null,
            type = null,
            status = null,
        )
    }

    fun removeAllUserMetadataWithPaging() { // should be fixed
        var hasMore = true
        var page: PNPage? = null
        while (hasMore) {
            println("-=page")
            val allMetadataPage: PNUUIDMetadataArrayResult = pubnub.getAllUUIDMetadata(page = page).sync()

            allMetadataPage.data.forEach { pnUUIDMetadata: PNUUIDMetadata ->
                pubnub.removeUUIDMetadata(uuid = pnUUIDMetadata.id).sync()
            }
            page = allMetadataPage.next
            hasMore = page != null
        }
    }

    private fun sortPNChannelMembershipsByUpdatedValue(channelMemberships: Collection<PNChannelMembership>): List<PNChannelMembership> {
        return channelMemberships.sortedWith(compareBy { ZonedDateTime.parse(it.updated, ISO_ZONED_DATE_TIME) })
    }
}

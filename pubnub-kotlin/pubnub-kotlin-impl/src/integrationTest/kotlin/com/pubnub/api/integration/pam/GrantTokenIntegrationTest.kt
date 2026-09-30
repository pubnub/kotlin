package com.pubnub.api.integration.pam

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.enums.PNLogVerbosity
import com.pubnub.api.integration.BaseIntegrationTest
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.models.consumer.access_manager.v3.PNToken.PNResourcePermissions
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.kmp.createCustomObject
import com.pubnub.test.CommonUtils
import com.pubnub.test.Keys
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GrantTokenIntegrationTest : BaseIntegrationTest() {
    /**
     * Regression test for a pre-existing permission-decode bug: the SDK encodes [com.pubnub.api.models.TokenBitmask.CREATE]
     * (16) when minting a grant and PAM enforces it correctly, but `PNResourcePermissions(grant: Int)` never decodes the
     * create bit. So a token that was granted `create = true` round-trips lossily through [PubNub.parseToken] — create is
     * silently reported as false. This predates DataSync; DataSync merely surfaces it.
     *
     * This fails until `PNResourcePermissions` exposes `create` and the parser reads bit 16.
     */
    @Test
    fun parseToken_preservesCreatePermission() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val channelName = "createChannel" + CommonUtils.randomChannel()

        // when — grant create (alongside read so the channel resource is non-trivial)
        val token =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                channels = listOf(ChannelGrant.name(name = channelName, read = true, create = true)),
            ).sync().token

        // then — create must survive the grant -> PAM -> parseToken round-trip
        val (_, _, _, _, resources) = pubNubUnderTest.parseToken(token)
        val channelPermissions = resources.channels[channelName]!!

        assertTrue("read should be granted", channelPermissions.read)
        assertTrue("create was granted but the parser dropped it", channelPermissions.create)
    }

    @Test
    fun happyPath_SUM() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val expectedAuthorizedUUID = "authorizedUser01"
        val expectedSpaceIdValue = "mySpace01"
        val expectedSpaceIdPattern = "mySpace.*"
        val expectedUserIdValue = "myUser01"
        val expectedUserIdPattern = "myUser.*"

        // when
        val grantTokenEndpoint =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                authorizedUUID = expectedAuthorizedUUID,
                channels =
                    listOf(
                        ChannelGrant.name(name = expectedSpaceIdValue, read = true, delete = true),
                        ChannelGrant.pattern(pattern = expectedSpaceIdPattern, write = true, manage = true),
                    ),
                uuids =
                    listOf(
                        UUIDGrant.id(id = expectedUserIdValue, delete = true),
                        UUIDGrant.pattern(pattern = expectedUserIdPattern, update = true),
                    ),
            )

        val token = grantTokenEndpoint.sync().token

        // then
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), ttl)

        assertEquals(expectedTTL.toLong(), ttl)
        assertEquals(
            PNResourcePermissions(
                read = true,
                delete = true,
            ),
            resources.channels[expectedSpaceIdValue],
        )
        assertEquals(
            PNResourcePermissions(
                write = true,
                manage = true,
            ),
            patterns.channels[expectedSpaceIdPattern],
        )
        assertEquals(PNResourcePermissions(delete = true), resources.uuids[expectedUserIdValue])
        assertEquals(PNResourcePermissions(update = true), patterns.uuids[expectedUserIdPattern])
    }

    @Test
    fun happyPath() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val expectedChannelResourceName = "channelResource"
        val expectedChannelPattern = "channel.*"
        val expectedChannelGroupResourceId = "channelGroup"
        val expectedChannelGroupPattern = "channelGroup.*"

        // when
        val token =
            pubNubUnderTest
                .grantToken(
                    ttl = expectedTTL,
                    channels =
                        listOf(
                            ChannelGrant.name(name = expectedChannelResourceName, delete = true),
                            ChannelGrant.pattern(pattern = expectedChannelPattern, write = true),
                        ),
                    channelGroups =
                        listOf(
                            ChannelGroupGrant.id(id = expectedChannelGroupResourceId, read = true),
                            ChannelGroupGrant.pattern(pattern = expectedChannelGroupPattern, manage = true),
                        ),
                )
                .sync()
                .token
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)

        println(pubNubUnderTest.parseToken(token))
        // then
        assertEquals(expectedTTL.toLong(), ttl)
        assertEquals(
            PNResourcePermissions(
                delete = true,
            ),
            resources.channels[expectedChannelResourceName],
        )
        assertEquals(
            PNResourcePermissions(
                read = true,
            ),
            resources.channelGroups[expectedChannelGroupResourceId],
        )
        assertEquals(
            PNResourcePermissions(
                write = true,
            ),
            patterns.channels[expectedChannelPattern],
        )
        assertEquals(
            PNResourcePermissions(
                manage = true,
            ),
            patterns.channelGroups[expectedChannelGroupPattern],
        )
    }

    @Test
    fun can_grantToken_for_datasync_elements() {
        // given
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val entityName = "capy-001"
        val membershipName = "user-123:channel-X"

        // when — mint a token carrying a mix of entity/relationship/membership grants, exact + pattern
        val token =
            pubNubUnderTest
                .grantToken(
                    ttl = expectedTTL,
                    authorizedUserId = UserId("pam-debug-admin"),
                    grants =
                        listOf(
                            DataSyncGrant.entity(entityName, get = true, update = true),
                            DataSyncGrant.entityPattern(".*", get = true),
                            DataSyncGrant.relationshipPattern(".*", get = true),
                            DataSyncGrant.membership(membershipName, get = true),
                        ),
                )
                .sync()
                .token

        // then
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), ttl)

        assertEquals(
            PNResourcePermissions(get = true, update = true),
            resources.datasyncEntities[entityName],
        )
        assertEquals(
            PNResourcePermissions(get = true),
            resources.datasyncMemberships[membershipName],
        )
        assertEquals(
            PNResourcePermissions(get = true),
            patterns.datasyncEntities[".*"],
        )
        assertEquals(
            PNResourcePermissions(get = true),
            patterns.datasyncRelationships[".*"],
        )
    }

    @Test
    fun grantToken_withAllGrantTypes_viaFlatList() {
        // given — mint a single token through the new flat `grants` overload carrying EVERY grant type that
        // implements TokenGrant: ChannelGrant, ChannelGroupGrant and DataSyncGrant. Each grant type is
        // exercised in both exact and pattern form, and DataSync covers every namespace (channels, users, entities,
        // relationships, memberships).
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val channelId = "channelResource"
        val channelPattern = "channel.*"
        val channelGroupId = "channelGroup"
        val channelGroupPattern = "channelGroup.*"
        val dataSyncChannelId = "dsChannel01"
        val dataSyncChannelPattern = "dsChannel.*"
        val userId = "user01"
        val userPattern = "user.*"
        val entityId = "capy-001"
        val entityPattern = "capy.*"
        val relationshipId = "user.A:channel.X"
        val relationshipPattern = "rel.*"
        val membershipId = "user-123:channel-X"
        val membershipPattern = "mem.*"

        // when
        val token =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                authorizedUserId = UserId("pam-debug-admin"),
                grants =
                    listOf(
                        ChannelGrant.name(name = channelId, read = true, write = true),
                        ChannelGrant.pattern(pattern = channelPattern, read = true),
                        ChannelGroupGrant.id(id = channelGroupId, read = true, manage = true),
                        ChannelGroupGrant.pattern(pattern = channelGroupPattern, read = true),
                        DataSyncGrant.subscribe(userId, projection = "admin"),
                        DataSyncGrant.channel(dataSyncChannelId, get = true, update = true),
                        DataSyncGrant.channelPattern(dataSyncChannelPattern, get = true, create = true),
                        DataSyncGrant.user(name = userId, get = true, update = true),
                        DataSyncGrant.userPattern(pattern = userPattern, get = true, create = true),
                        DataSyncGrant.entity(entityId, get = true, update = true),
                        DataSyncGrant.entityPattern(entityPattern, get = true),
                        DataSyncGrant.relationship(relationshipId, get = true),
                        DataSyncGrant.relationshipPattern(relationshipPattern, get = true),
                        DataSyncGrant.membership(membershipId, get = true),
                        DataSyncGrant.membershipPattern(membershipPattern, get = true),
                    ),
            ).sync().token

        // then — every grant survives the grant -> PAM -> parseToken round-trip in its own bucket.
        val (_, _, ttl, _, resources, patterns) = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), ttl)

        assertEquals(PNResourcePermissions(read = true, write = true), resources.channels[channelId])
        assertEquals(PNResourcePermissions(read = true), patterns.channels[channelPattern])

        assertEquals(PNResourcePermissions(read = true, manage = true), resources.channelGroups[channelGroupId])
        assertEquals(PNResourcePermissions(read = true), patterns.channelGroups[channelGroupPattern])

        // DataSyncGrant.channel permissions land in the plain `channels` bucket.
        assertEquals(PNResourcePermissions(get = true, update = true), resources.channels[dataSyncChannelId])
        assertEquals(PNResourcePermissions(get = true, create = true), patterns.channels[dataSyncChannelPattern])

        // DataSyncGrant.user permissions land in the plain `users` bucket (not `uuids`).
        assertEquals(PNResourcePermissions(get = true, update = true), resources.users[userId])
        assertEquals(PNResourcePermissions(get = true, create = true), patterns.users[userPattern])

        assertEquals(PNResourcePermissions(get = true, update = true), resources.datasyncEntities[entityId])
        assertEquals(PNResourcePermissions(get = true), patterns.datasyncEntities[entityPattern])
        assertEquals(PNResourcePermissions(get = true), resources.datasyncRelationships[relationshipId])
        assertEquals(PNResourcePermissions(get = true), patterns.datasyncRelationships[relationshipPattern])
        assertEquals(PNResourcePermissions(get = true), resources.datasyncMemberships[membershipId])
        assertEquals(PNResourcePermissions(get = true), patterns.datasyncMemberships[membershipPattern])
    }

    @Test
    fun canReadGroupsInChannelGroupWhenPamEnabled() {
        val expectedTTL = 1337
        val channelGroupName = "channelGroup" + CommonUtils.randomChannel()
        val channel01 = "channel" + CommonUtils.randomChannel()
        val channel02 = "channel" + CommonUtils.randomChannel()
        val channel03 = "channel" + CommonUtils.randomChannel()

        // create token
        val token =
            server.grantToken(
                ttl = expectedTTL,
                channelGroups =
                    listOf(
                        ChannelGroupGrant.id(id = channelGroupName, read = true, manage = true),
                    ),
            ).sync().token

        // create pubnub instance with PAM enabled but not server(secretKey is not configured)
        val pubNubTest = createPubNub {
            userId = UserId(PubNub.generateUUID())
            subscribeKey = Keys.pamSubKey
            publishKey = Keys.pamPubKey
            logVerbosity = PNLogVerbosity.NONE
        }

        // setToken
        pubNubTest.setToken(token)

        // add channels to channelGroup
        pubNubTest.addChannelsToChannelGroup(
            channelGroup = channelGroupName,
            channels = listOf(channel01, channel02, channel03),
        ).sync()

        // get channels in channelGroup
        val channelsInChannelGroup = pubNubTest.listChannelsForChannelGroup(channelGroup = channelGroupName).sync()

        channelsInChannelGroup?.channels?.let { channels ->
            Assert.assertTrue(channels.contains(channel01))
            Assert.assertTrue(channels.contains(channel02))
            Assert.assertTrue(channels.contains(channel03))
            assertEquals(3, channels.size)
        }
    }

    @Test
    fun grantToken_carriesDataSyncProjectionsInMeta() {
        // given — projections are declared inline on the DataSync grants; the SDK derives the pn-projections meta.
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val adminProjection = "admin"
        val entityId = "user.A"
        val entityPatternId = "user.*"
        // relationship id keeps its natural colon separator — the SDK must pass it through verbatim into the key.
        val relationshipId = "user.A:channel.X"
        val relationshipPatternId = "rel.*"
        val membershipId = "user-123:channel-X"
        val membershipPatternId = "mem.*"

        // composite keys the SDK is expected to build: "$namespace:$id"
        val entityKey = "${DataSyncNamespace.ENTITIES}:$entityId"
        val entityPatternKey = "${DataSyncNamespace.ENTITIES}:$entityPatternId"
        val relationshipKey = "${DataSyncNamespace.RELATIONSHIPS}:$relationshipId"
        val relationshipPatternKey = "${DataSyncNamespace.RELATIONSHIPS}:$relationshipPatternId"
        val membershipKey = "${DataSyncNamespace.MEMBERSHIPS}:$membershipId"
        val membershipPatternKey = "${DataSyncNamespace.MEMBERSHIPS}:$membershipPatternId"

        // when
        val token =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                authorizedUserId = null,
                grants =
                    listOf(
                        DataSyncGrant.entity(entityId, get = true, update = true, projection = adminProjection),
                        DataSyncGrant.entityPattern(entityPatternId, get = true, projection = DataSyncNamespace.DEFAULT_PROJECTION),
                        DataSyncGrant.relationship(relationshipId, get = true, projection = adminProjection),
                        DataSyncGrant.relationshipPattern(
                            relationshipPatternId,
                            get = true,
                            projection = DataSyncNamespace.DEFAULT_PROJECTION,
                        ),
                        // memberships take no projection, so they must not add a pn-projections entry
                        DataSyncGrant.membership(membershipId, get = true),
                        DataSyncGrant.membershipPattern(membershipPatternId, get = true),
                        ChannelGrant.name(name = "anyChannel", read = true),
                    ),
            ).sync().token

        // then — the pn-projections block must survive the round-trip and surface on the typed projections field.
        val parsed = pubNubUnderTest.parseToken(token)
        println("token: $token")
        assertEquals(expectedTTL.toLong(), parsed.ttl)

        // the parser lifts pn-projections into the typed field, split by namespace with bare ids as keys.
        val projections = parsed.projections!!
        assertEquals(adminProjection, projections.resources.entities[entityId])
        assertEquals(adminProjection, projections.resources.relationships[relationshipId]) // colon in id survives verbatim
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, projections.patterns.entities[entityPatternId])
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, projections.patterns.relationships[relationshipPatternId])
        assertTrue(projections.resources.memberships.isEmpty())
        assertTrue(projections.patterns.memberships.isEmpty())
        // the membership grants themselves still land in the token
        assertTrue(parsed.resources.datasyncMemberships.containsKey(membershipId))
        assertTrue(parsed.patterns.datasyncMemberships.containsKey(membershipPatternId))

        // the raw block also remains available under meta (additive, non-breaking).
        @Suppress("UNCHECKED_CAST")
        val rawProjections = (parsed.meta as Map<String, Any?>)[DataSyncNamespace.PN_PROJECTIONS] as Map<String, Any?>

        // every value is a flat composite key -> single projection-name string (entities, relationships, memberships alike)
        @Suppress("UNCHECKED_CAST")
        val res = rawProjections["res"] as Map<String, Any?>
        assertEquals(adminProjection, res[entityKey])
        assertEquals(adminProjection, res[relationshipKey]) // colon in the relationship id survives verbatim
        assertFalse(res.containsKey(membershipKey))

        @Suppress("UNCHECKED_CAST")
        val pat = rawProjections["pat"] as Map<String, Any?>
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, pat[entityPatternKey])
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, pat[relationshipPatternKey])
        assertFalse(pat.containsKey(membershipPatternKey))
    }

    @Test
    fun grantToken_keepsCallerMetaAlongsideProjections() {
        // given — the caller supplies their own plain meta (a pn-projections key in it is rejected). The SDK must add
        // the grant-derived pn-projections block next to it without dropping the caller's keys.
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val adminProjection = "admin"
        val entityId = "user.A"

        val entityKey = "${DataSyncNamespace.ENTITIES}:$entityId"

        val callerMeta = createCustomObject(mapOf("caller-key" to "caller-value"))

        // when
        val token =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                authorizedUserId = null,
                meta = callerMeta,
                grants =
                    listOf(
                        DataSyncGrant.entity(entityId, get = true, update = true, projection = adminProjection),
                        ChannelGrant.name(name = "anyChannel", read = true),
                    ),
            ).sync().token

        // then
        val parsed = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), parsed.ttl)

        @Suppress("UNCHECKED_CAST")
        val meta = parsed.meta as Map<String, Any?>

        // caller-supplied plain meta must survive alongside the generated pn-projections block
        assertEquals("caller-value", meta["caller-key"])

        @Suppress("UNCHECKED_CAST")
        val projections = meta[DataSyncNamespace.PN_PROJECTIONS] as Map<String, Any?>

        @Suppress("UNCHECKED_CAST")
        val res = projections["res"] as Map<String, Any?>
        assertEquals(mapOf(entityKey to adminProjection), res)

        // the block also surfaces on the typed field, split by namespace with bare ids as keys.
        assertEquals(adminProjection, parsed.projections!!.resources.entities[entityId])
    }
}

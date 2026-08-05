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
                    dataSync =
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
                dataSync =
                    listOf(
                        DataSyncGrant.entity(entityId, get = true, update = true, projection = adminProjection),
                        DataSyncGrant.entityPattern(entityPatternId, get = true, projection = DataSyncNamespace.DEFAULT_PROJECTION),
                        DataSyncGrant.relationship(relationshipId, get = true, projection = adminProjection),
                        DataSyncGrant.relationshipPattern(
                            relationshipPatternId,
                            get = true,
                            projection = DataSyncNamespace.DEFAULT_PROJECTION,
                        ),
                        DataSyncGrant.membership(membershipId, get = true, projection = adminProjection),
                        DataSyncGrant.membershipPattern(
                            membershipPatternId,
                            get = true,
                            projection = DataSyncNamespace.DEFAULT_PROJECTION,
                        ),
                    ),
                channels = listOf(ChannelGrant.name(name = "anyChannel", read = true)),
            ).sync().token

        // then — the pn-projections block must survive the round-trip and surface on the typed projections field.
        val parsed = pubNubUnderTest.parseToken(token)
        println("token: $token")
        assertEquals(expectedTTL.toLong(), parsed.ttl)

        // the parser lifts pn-projections into the typed field, split by namespace with bare ids as keys.
        val projections = parsed.projections!!
        assertEquals(adminProjection, projections.resources.entities[entityId])
        assertEquals(adminProjection, projections.resources.relationships[relationshipId]) // colon in id survives verbatim
        assertEquals(adminProjection, projections.resources.memberships[membershipId]) // colon in id survives verbatim
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, projections.patterns.entities[entityPatternId])
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, projections.patterns.relationships[relationshipPatternId])
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, projections.patterns.memberships[membershipPatternId])

        // the raw block also remains available under meta (additive, non-breaking).
        @Suppress("UNCHECKED_CAST")
        val rawProjections = (parsed.meta as Map<String, Any?>)[DataSyncNamespace.PN_PROJECTIONS] as Map<String, Any?>

        // every value is a flat composite key -> single projection-name string (entities, relationships, memberships alike)
        @Suppress("UNCHECKED_CAST")
        val res = rawProjections["res"] as Map<String, Any?>
        assertEquals(adminProjection, res[entityKey])
        assertEquals(adminProjection, res[relationshipKey]) // colon in the relationship id survives verbatim
        assertEquals(adminProjection, res[membershipKey]) // colon in the membership id survives verbatim

        @Suppress("UNCHECKED_CAST")
        val pat = rawProjections["pat"] as Map<String, Any?>
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, pat[entityPatternKey])
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, pat[relationshipPatternKey])
        assertEquals(DataSyncNamespace.DEFAULT_PROJECTION, pat[membershipPatternKey])
    }

    @Test
    fun grantToken_mergesCallerSuppliedProjectionsIntoMeta() {
        // given — the caller supplies their own meta carrying both a plain value and a pn-projections block. The SDK
        // must overlay the grant-derived projections onto that meta: the plain value survives, a caller projection for
        // a key no grant carries survives verbatim, and a caller projection colliding with a grant loses to the grant.
        val pubNubUnderTest = server
        val expectedTTL = 1337
        val adminProjection = "admin"
        val entityId = "user.A"

        val entityKey = "${DataSyncNamespace.ENTITIES}:$entityId"

        // a projection the caller injects directly into meta for a resource NO grant carries — it must survive verbatim.
        val callerOnlyKey = "${DataSyncNamespace.MEMBERSHIPS}:user-123:channel-X"
        val callerOnlyProjection = "caller-only"
        // a projection the caller sets for the SAME key a grant also generates — the grant-derived value must win.
        val callerColliding = "caller-should-lose"

        val callerMeta =
            createCustomObject(
                mapOf(
                    "caller-key" to "caller-value",
                    DataSyncNamespace.PN_PROJECTIONS to
                        mapOf(
                            "res" to
                                mapOf(
                                    callerOnlyKey to callerOnlyProjection,
                                    entityKey to callerColliding,
                                ),
                        ),
                ),
            )

        // when
        val token =
            pubNubUnderTest.grantToken(
                ttl = expectedTTL,
                authorizedUserId = null,
                meta = callerMeta,
                dataSync =
                    listOf(
                        DataSyncGrant.entity(entityId, get = true, update = true, projection = adminProjection),
                    ),
                channels = listOf(ChannelGrant.name(name = "anyChannel", read = true)),
            ).sync().token

        // then
        val parsed = pubNubUnderTest.parseToken(token)
        assertEquals(expectedTTL.toLong(), parsed.ttl)

        @Suppress("UNCHECKED_CAST")
        val meta = parsed.meta as Map<String, Any?>

        // caller-supplied plain meta must survive the merge alongside the generated pn-projections block
        assertEquals("caller-value", meta["caller-key"])

        @Suppress("UNCHECKED_CAST")
        val projections = meta[DataSyncNamespace.PN_PROJECTIONS] as Map<String, Any?>

        @Suppress("UNCHECKED_CAST")
        val res = projections["res"] as Map<String, Any?>
        assertEquals(adminProjection, res[entityKey]) // grant-derived value wins over the caller's colliding entry
        assertEquals(callerOnlyProjection, res[callerOnlyKey]) // caller projection for a key no grant carries survives

        // the merged block also surfaces on the typed field, split by namespace with bare ids as keys.
        val typedProjections = parsed.projections!!
        assertEquals(adminProjection, typedProjections.resources.entities[entityId]) // grant wins over caller's colliding entry
        // callerOnlyKey = "datasync:memberships:user-123:channel-X" -> membership bare id "user-123:channel-X"
        assertEquals(callerOnlyProjection, typedProjections.resources.memberships["user-123:channel-X"])
    }
}

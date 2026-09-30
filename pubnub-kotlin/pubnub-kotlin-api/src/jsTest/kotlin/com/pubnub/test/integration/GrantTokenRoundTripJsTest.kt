package com.pubnub.test.integration

import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.test.BaseIntegrationTest
import com.pubnub.test.Keys
import com.pubnub.test.await
import com.pubnub.test.randomString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// JS-only (not commonTest): iOS `grantToken` is still a TODO. Runs on Node, where npm can sign with the secret key.
class GrantTokenRoundTripJsTest : BaseIntegrationTest() {
    @Test
    fun grantToken_withAllGrantTypes_viaFlatList_roundTripsThroughParseToken() =
        runTest {
            if (Keys.pamSecKey == "demo") {
                return@runTest // no PAM keyset configured
            }
            val channel = randomString()
            val group = randomString()
            val dataSyncChannel = randomString()
            val user = randomString()
            val entity = randomString()
            // The colon is the case a naive `:` split of the `pn-projections` keys gets wrong.
            val relationship = "user.A:channel.X"
            val membership = randomString()
            val authorizedUserId = randomString()

            val token =
                pubnubPamServer.grantToken(
                    ttl = 1,
                    authorizedUserId = UserId(authorizedUserId),
                    grants =
                        listOf(
                            ChannelGrant.name(channel, read = true, write = true),
                            ChannelGrant.pattern("$channel.*", read = true),
                            ChannelGroupGrant.id(group, read = true, manage = true),
                            DataSyncGrant.channel(dataSyncChannel, get = true, update = true, projection = "admin"),
                            DataSyncGrant.user(user, get = true, create = true, update = true, delete = true),
                            DataSyncGrant.userPattern("$user.*", create = true),
                            DataSyncGrant.entity(entity, get = true, create = true, projection = "admin"),
                            DataSyncGrant.entityPattern("$entity.*", get = true, create = true),
                            DataSyncGrant.relationship(relationship, get = true, create = true, projection = "admin"),
                            DataSyncGrant.relationshipPattern("user.A:.*", create = true, delete = true),
                            DataSyncGrant.membership(membership, get = true, create = true),
                            DataSyncGrant.membershipPattern("$membership.*", create = true, update = true),
                        ),
                ).await().token

            val parsed = pubnubPamServer.parseToken(token)

            assertEquals(authorizedUserId, parsed.authorizedUUID)

            // npm doesn't decode CREATE for channels/uuids, so their `create` isn't asserted.
            val channelPermissions = assertNotNull(parsed.resources.channels[channel])
            assertTrue(channelPermissions.read)
            assertTrue(channelPermissions.write)
            assertTrue(assertNotNull(parsed.patterns.channels["$channel.*"]).read)
            val dataSyncChannelPermissions = assertNotNull(parsed.resources.channels[dataSyncChannel])
            assertTrue(dataSyncChannelPermissions.get)
            assertTrue(dataSyncChannelPermissions.update)
            assertEquals(
                PNToken.PNResourcePermissions(read = true, manage = true),
                parsed.resources.channelGroups[group],
            )

            assertEquals(
                PNToken.PNResourcePermissions(get = true, create = true, update = true, delete = true),
                parsed.resources.users[user],
            )
            assertEquals(PNToken.PNResourcePermissions(create = true), parsed.patterns.users["$user.*"])
            assertEquals(
                PNToken.PNResourcePermissions(get = true, create = true),
                parsed.resources.datasyncEntities[entity],
            )
            assertEquals(
                PNToken.PNResourcePermissions(get = true, create = true),
                parsed.patterns.datasyncEntities["$entity.*"],
            )
            assertEquals(
                PNToken.PNResourcePermissions(get = true, create = true),
                parsed.resources.datasyncRelationships[relationship],
            )
            assertEquals(
                PNToken.PNResourcePermissions(create = true, delete = true),
                parsed.patterns.datasyncRelationships["user.A:.*"],
            )
            assertEquals(
                PNToken.PNResourcePermissions(get = true, create = true),
                parsed.resources.datasyncMemberships[membership],
            )
            assertEquals(
                PNToken.PNResourcePermissions(create = true, update = true),
                parsed.patterns.datasyncMemberships["$membership.*"],
            )

            val projections = assertNotNull(parsed.projections)
            assertEquals(mapOf(entity to "admin"), projections.resources.entities)
            assertEquals(mapOf(relationship to "admin"), projections.resources.relationships)
            assertTrue(projections.resources.memberships.isEmpty())

            // Channel projections aren't lifted into `projections` (same as the JVM); they stay in the raw meta.
            val rawProjections = (parsed.meta as Map<*, *>)[DataSyncNamespace.PN_PROJECTIONS] as Map<*, *>
            val rawResources = rawProjections["res"] as Map<*, *>
            assertEquals("admin", rawResources["${DataSyncNamespace.CHANNELS_PROJECTION}:$dataSyncChannel"])
        }
}

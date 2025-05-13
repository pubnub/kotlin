package com.pubnub.docs.accessManager

import com.pubnub.api.SpaceId
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.docs.SnippetBase

class GrantTokenOther : SnippetBase() {
    private fun grantTokenDifferentAccessLevels() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-in-a-single-call

        val pubnub = createPubNub()

        // snippet.grantTokenDifferentAccessLevels
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.name(name = "channel-a", read = true),
                ChannelGrant.name(name = "channel-b", read = true, write = true),
                ChannelGrant.name(name = "channel-c", read = true, write = true),
                ChannelGrant.name(name = "channel-d", read = true, write = true)
            ),
            channelGroups = listOf(
                ChannelGroupGrant.id(id = "channel-group-b", read = true)
            ),
            uuids = listOf(
                UUIDGrant.id(id = "uuid-c", get = true),
                UUIDGrant.id(id = "uuid-d", get = true, update = true)
            )
        ).async { result ->
            result.onFailure { exception ->
                // Handle error
            }.onSuccess { value ->
                // Handle successful method result
            }
        }
        // snippet.end
    }

    private fun grantTokenWithPatternBasedChannelAccess() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-read-access-to-multiple-channels-using-regex

        val pubnub = createPubNub()

        // snippet.grantTokenWithPatternBasedChannelAccess
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.pattern(pattern = "^channel-[A-Za-z0-9]*$", read = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun grantTokenWithPatternBasedChannelGroupAccess() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-and-read-access-to-channels-using-regex-in-a-single-call

        val pubnub = createPubNub()

        // snippet.grantTokenWithPatternBasedChannelGroupAccess
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.name(name = "channel-a", read = true),
                ChannelGrant.name(name = "channel-b", read = true, write = true),
                ChannelGrant.name(name = "channel-c", read = true, write = true),
                ChannelGrant.name(name = "channel-d", read = true, write = true),
                ChannelGrant.pattern(pattern = "^channel-[A-Za-z0-9]*$", read = true)
            ),
            channelGroups = listOf(
                ChannelGroupGrant.id(id = "channel-group-b", read = true)
            ),
            uuids = listOf(
                UUIDGrant.id(id = "uuid-c", get = true),
                UUIDGrant.id(id = "uuid-d", get = true, update = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun grantTokenWithSpacePermission() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#basic-usage-1

        val pubnub = createPubNub()

        // snippet.grantTokenWithSpacePermission
        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("my-authorized-userId"),
            spacesPermissions = listOf(SpacePermissions.id(spaceId = SpaceId("my-space"), read = true))
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun grantTokenWithSpaceAndUserPermissionDifferentLevels() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-in-a-single-call-1

        val pubnub = createPubNub()

        // snippet.grantTokenWithSpaceAndUserPermissionDifferentLevels
        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("my-authorized-userId"),
            spacesPermissions = listOf(
                SpacePermissions.id(spaceId = SpaceId("channel-a"), read = true),
                SpacePermissions.id(spaceId = SpaceId("channel-b"), read = true, write = true),
                SpacePermissions.id(spaceId = SpaceId("channel-c"), read = true, write = true),
                SpacePermissions.id(spaceId = SpaceId("channel-d"), read = true, write = true)
            ),
            usersPermissions = listOf(
                UserPermissions.id(userId = UserId("userId-c"), get = true),
                UserPermissions.id(userId = UserId("userId-d"), get = true, update = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun grantTokenMultipleSpacesUsingRegEx() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-read-access-to-multiple-spaces-using-regex

        val pubnub = createPubNub()

        // snippet.grantTokenMultipleSpacesUsingRegEx
        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("my-authorized-userId"),
            spacesPermissions = listOf(
                SpacePermissions.pattern(pattern = "^space-[A-Za-z0-9]*$", read = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun grantTokenVariousResourcesUsingRegEx() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-and-read-access-to-spaces-using-regex-in-a-single-call

        val pubnub = createPubNub()

        // snippet.grantTokenMultipleSpacesUsingRegEx
        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("my-authorized-uuid"),
            spacesPermissions = listOf(
                SpacePermissions.id(spaceId = SpaceId("channel-a"), read = true),
                SpacePermissions.id(spaceId = SpaceId("channel-b"), read = true, write = true),
                SpacePermissions.id(spaceId = SpaceId("channel-c"), read = true, write = true),
                SpacePermissions.id(spaceId = SpaceId("channel-d"), read = true, write = true),
                SpacePermissions.pattern(pattern = "^spae-[A-Za-z0-9]*$", read = true)
            ),
            usersPermissions = listOf(
                UserPermissions.id(userId = UserId("userId-c"), get = true),
                UserPermissions.id(userId = UserId("userId-d"), get = true, update = true)
            )
        )
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }
}

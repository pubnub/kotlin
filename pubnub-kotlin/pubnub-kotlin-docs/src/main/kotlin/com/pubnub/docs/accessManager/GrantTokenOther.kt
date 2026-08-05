package com.pubnub.docs.accessManager

import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant

class GrantTokenOther {
    private fun grantTokenDifferentAccessLevels(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-in-a-single-call

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

    private fun grantTokenWithPatternBasedChannelAccess(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-read-access-to-multiple-channels-using-regex

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

    private fun grantTokenWithPatternBasedChannelGroupAccess(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-and-read-access-to-channels-using-regex-in-a-single-call

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

    private fun grantTokenWithSpacePermission(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#basic-usage-1

        // snippet.grantTokenWithSpacePermission
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-userId",
            channels = listOf(ChannelGrant.name(name = "my-space", read = true))
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

    private fun grantTokenWithSpaceAndUserPermissionDifferentLevels(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-in-a-single-call-1

        // snippet.grantTokenWithSpaceAndUserPermissionDifferentLevels
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-userId",
            channels = listOf(
                ChannelGrant.name(name = "channel-a", read = true),
                ChannelGrant.name(name = "channel-b", read = true, write = true),
                ChannelGrant.name(name = "channel-c", read = true, write = true),
                ChannelGrant.name(name = "channel-d", read = true, write = true)
            ),
            uuids = listOf(
                UUIDGrant.id(id = "userId-c", get = true),
                UUIDGrant.id(id = "userId-d", get = true, update = true)
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

    private fun grantTokenMultipleSpacesUsingRegEx(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-read-access-to-multiple-spaces-using-regex

        // snippet.grantTokenMultipleSpacesUsingRegEx
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-userId",
            channels = listOf(
                ChannelGrant.pattern(pattern = "^space-[A-Za-z0-9]*$", read = true)
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

    private fun grantTokenVariousResourcesUsingRegEx(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-and-read-access-to-spaces-using-regex-in-a-single-call

        // snippet.grantTokenMultipleSpacesUsingRegEx
        pubnub.grantToken(
            ttl = 15,
            authorizedUUID = "my-authorized-uuid",
            channels = listOf(
                ChannelGrant.name(name = "channel-a", read = true),
                ChannelGrant.name(name = "channel-b", read = true, write = true),
                ChannelGrant.name(name = "channel-c", read = true, write = true),
                ChannelGrant.name(name = "channel-d", read = true, write = true),
                ChannelGrant.pattern(pattern = "^spae-[A-Za-z0-9]*$", read = true)
            ),
            uuids = listOf(
                UUIDGrant.id(id = "userId-c", get = true),
                UUIDGrant.id(id = "userId-d", get = true, update = true)
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

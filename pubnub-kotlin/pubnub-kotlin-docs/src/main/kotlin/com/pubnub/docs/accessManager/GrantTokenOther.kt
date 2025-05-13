package com.pubnub.docs.accessManager

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
}

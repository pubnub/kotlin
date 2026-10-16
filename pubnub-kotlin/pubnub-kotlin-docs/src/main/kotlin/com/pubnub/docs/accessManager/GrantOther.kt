package com.pubnub.docs.accessManager

import com.pubnub.api.PubNub

class GrantOther {
    private fun grantWithGetAllMetadataAccess(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager-v2#category-permissions

        // snippet.grantWithGetAllMetadataAccess
        // Allow the auth keys to list all channel and uuid metadata on the keyset.
        // The `get` permission is sent automatically, so no other permission can be set.
        pubnub.grant(
            authKeys = listOf("my-auth-key"),
            getAllChannels = true,
            getAllUUIDs = true
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

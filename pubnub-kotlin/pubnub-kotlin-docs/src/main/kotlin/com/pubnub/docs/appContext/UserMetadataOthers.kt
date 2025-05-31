package com.pubnub.docs.appContext

import com.pubnub.api.PubNub

class UserMetadataOthers {
    private fun getUUIDMetadataBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-1

        // snippet.getUUIDMetadataBasic
        pubnub.getUUIDMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun setUUIDMetadataBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-2

        // snippet.setUUIDMetadataBasic
        pubnub.setUUIDMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun removeUUIDMetadataBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-3

        // snippet.removeUUIDMetadataBasic
        pubnub.removeUUIDMetadata()
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

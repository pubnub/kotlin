package com.pubnub.docs.appContext

import com.pubnub.docs.SnippetBase

class UserMetadataOthers : SnippetBase() {
    private fun getUUIDMetadataBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-1

        val pubnub = createPubNub()

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

    private fun setUUIDMetadataBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-2

        val pubnub = createPubNub()

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

    private fun removeUUIDMetadataBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-3

        val pubnub = createPubNub()

        // snippet.removeUUIDMetadataBasic
        pubnub.removeUUIDMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        //snippet.end
    }
}

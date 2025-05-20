package com.pubnub.docs.appContext

import com.pubnub.docs.SnippetBase

class ChannelMetadataOthers : SnippetBase() {
    private fun getAllChannelMetadataBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-4

        val pubnub = createPubNub()

        // snippet.getAllChannelMetadataBasic
        pubnub.getAllChannelMetadata()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun getChannelMetadataBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-5

        val pubnub = createPubNub()

        // snippet.getChannelMetadataBasic
        pubnub.getChannelMetadata(channel = "myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun setChannelMetadataBasic() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-6

        val pubnub = createPubNub()

        // snippet.setChannelMetadataBasic
        pubnub.setChannelMetadata(channel = "myChannel")
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun removeChannelMetadata() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-7

        val pubnub = createPubNub()

        // snippet.removeChannelMetadata
        pubnub.removeChannelMetadata(channel = "myChannel")
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

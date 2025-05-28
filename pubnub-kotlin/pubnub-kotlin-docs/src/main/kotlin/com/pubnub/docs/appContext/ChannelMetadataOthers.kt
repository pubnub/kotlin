package com.pubnub.docs.appContext

import com.pubnub.api.PubNub

class ChannelMetadataOthers {
    private fun getAllChannelMetadataBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-4

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

    private fun getChannelMetadataBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-5

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

    private fun setChannelMetadataBasic(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-6

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

    private fun removeChannelMetadata(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#basic-usage-7

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

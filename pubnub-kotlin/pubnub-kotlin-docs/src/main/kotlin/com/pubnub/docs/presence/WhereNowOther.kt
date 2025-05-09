package com.pubnub.docs.presence

import com.pubnub.docs.SnippetBase

class WhereNowOther : SnippetBase() {

    private fun whereNowGetListOfChannelsUuidIsSubscribedTo() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#get-a-list-of-channels-a-uuid-is-subscribed-to

        val pubnub = createPubNub()

        // snippet.whereNowGetListOfChannelsUuidIsSubscribedTo
        pubnub.whereNow()
            .async { result ->
                result.onFailure { exception ->
                    // Handle error
                }.onSuccess { value ->
                    // Handle successful method result
                }
            }
        // snippet.end
    }

    private fun whereNowGetListOfChannelsOtherUserIsSubscribeTo() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#obtain-information-about-the-current-list-of-channels-of-some-other-uuid

        val pubnub = createPubNub()

        // snippet.whereNowGetListOfChannelsOtherUserIsSubscribeTo
        pubnub.whereNow(
            uuid = "someUuid"
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

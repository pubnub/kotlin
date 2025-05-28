package com.pubnub.docs.presence

import com.pubnub.api.PubNub

class WhereNowOther {
    private fun whereNowGetListOfChannelsUuidIsSubscribedTo(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#get-a-list-of-channels-a-uuid-is-subscribed-to

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

    private fun whereNowGetListOfChannelsOtherUserIsSubscribeTo(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#obtain-information-about-the-current-list-of-channels-of-some-other-uuid

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

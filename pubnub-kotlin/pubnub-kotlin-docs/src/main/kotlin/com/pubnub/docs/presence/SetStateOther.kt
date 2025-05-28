package com.pubnub.docs.presence

import com.google.gson.JsonObject
import com.pubnub.api.PubNub

class SetStateOther {
    private fun setState(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#basic-usage-2

        // snippet.setState
        pubnub.setPresenceState(
            channels = listOf("my_channel"),
            state = mapOf("is_typing" to "true")
            // if no uuid supplied, own is used
        ).async { result -> }
        // snippet.end
    }

    private fun getState(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#basic-usage-2

        // snippet.getState
        pubnub.getPresenceState(
            channels = listOf("ch1", "ch2", "ch3"), // channels to fetch state for
            uuid = "such_uuid" // uuid of user to fetch, or own uuid by default
        ).async { result -> }
        // snippet.end
    }

    private fun setStateForChannelsInChannelGroup(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#set-state-for-channels-in-channel-group

        // snippet.setStateForChannelsInChannelGroup
        pubnub.setPresenceState(
            channels = listOf("ch1", "ch2", "ch3"), // apply on those channels
            channelGroups = listOf("cg1", "cg2", "cg3"), // apply on those channel groups
            state = JsonObject().apply { addProperty("is_typing", true) }
        ).async { result ->
            result.onSuccess { res ->
                res.state // {"data":{"is_typing":true}}
            }.onFailure { e ->
                // handle error
                e.message
                e.statusCode
                e.pubnubError
            }
        }
        // snippet.end
    }

    private fun getStateForUuid(pubnub: PubNub) {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/presence#get-state-for-uuid

        // snippet.getStateForUuid
        pubnub.getPresenceState(
            channels = listOf("ch1", "ch2"), // channels to fetch state for
            uuid = "such_uuid" // uuid of user to fetch, or own uuid by default
        ).async { result ->
            result.onFailure { exception ->
                exception.printStackTrace()
            }.onSuccess { value ->
                value.stateByUUID.forEach { (channel, state) ->
                    println("Channel: $channel, state: $state")
                }
            }
        }
        // snippet.end
    }
}

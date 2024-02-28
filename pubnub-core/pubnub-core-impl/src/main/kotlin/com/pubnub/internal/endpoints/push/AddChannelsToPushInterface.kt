package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult

interface AddChannelsToPushInterface : ExtendedRemoteAction<PNPushAddChannelResult> {
    val pushType: PNPushType
    val channels: List<String>
    val deviceId: String
    val topic: String?
    val environment: PNPushEnvironment
}

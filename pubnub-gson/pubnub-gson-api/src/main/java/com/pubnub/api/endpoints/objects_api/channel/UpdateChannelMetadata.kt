package com.pubnub.api.endpoints.objects_api.channel

import com.pubnub.api.endpoints.BuilderSteps.ChannelStep
import com.pubnub.api.endpoints.Endpoint
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult
import com.pubnub.api.utils.Optional

interface UpdateChannelMetadata : Endpoint<PNSetChannelMetadataResult?> {
    fun name(name: Optional<String?>): UpdateChannelMetadata?

    fun description(description: Optional<String?>): UpdateChannelMetadata?

    fun status(status: Optional<String?>): UpdateChannelMetadata?

    fun type(type: Optional<String?>): UpdateChannelMetadata?

    fun custom(custom: Optional<Map<String?, Any?>?>): UpdateChannelMetadata?

    fun includeCustom(includeCustom: Boolean): UpdateChannelMetadata?

    interface Builder : ChannelStep<UpdateChannelMetadata> {
        override fun channel(channel: String): UpdateChannelMetadata
    }
}

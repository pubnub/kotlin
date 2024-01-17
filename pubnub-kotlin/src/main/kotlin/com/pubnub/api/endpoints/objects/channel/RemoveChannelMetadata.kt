package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.endpoints.remoteaction.map
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.endpoints.objects.channel.IRemoveChannelMetadata
import com.pubnub.internal.endpoints.objects.channel.RemoveChannelMetadata

class RemoveChannelMetadata internal constructor(private val removeChannelMetadata: RemoveChannelMetadata) :
    DelegatingEndpoint<PNRemoveMetadataResult>(), IRemoveChannelMetadata by removeChannelMetadata {
    override fun createAction(): Endpoint<PNRemoveMetadataResult> = removeChannelMetadata.map(PNRemoveMetadataResult::from)
}
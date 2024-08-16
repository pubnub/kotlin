package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveChannelMetadataEndpoint(
    pubnub: PubNubImpl,
    private val channel: String,
) : EndpointCore<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), RemoveChannelMetadata {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        return retrofitManager.objectsService.deleteChannelMetadata(
            subKey = configuration.subscribeKey,
            channel = channel,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveChannelMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

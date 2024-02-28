package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.CoreEndpoint
import com.pubnub.internal.CorePubNubClient
import com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveChannelMetadataEndpoint(
    pubnub: CorePubNubClient,
    private val channel: String,
) : CoreEndpoint<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), RemoveChannelMetadataInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        return pubnub.retrofitManager.objectsService.deleteChannelMetadata(
            subKey = pubnub.configuration.subscribeKey,
            channel = channel,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveChannelMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

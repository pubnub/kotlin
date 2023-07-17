package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveChannelMetadata(
    pubnub: PubNub,
    private val channel: String
) : Endpoint<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        return pubnub.retrofitManager.objectsService.deleteChannelMetadata(
            subKey = pubnub.configuration.subscribeKey,
            channel = channel
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult? {
        return input.body()?.let { PNRemoveMetadataResult(it.status) }
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.PNRemoveChannelMetadataOperation
    }
}

package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveUUIDMetadata(
    pubnub: InternalPubNubClient,
    override val uuid: String? = null
) : Endpoint<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), IRemoveUUIDMetadata {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        return pubnub.retrofitManager.objectsService.deleteUUIDMetadata(
            subKey = pubnub.configuration.subscribeKey,
            uuid = uuid ?: pubnub.configuration.userId.value
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveUUIDMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

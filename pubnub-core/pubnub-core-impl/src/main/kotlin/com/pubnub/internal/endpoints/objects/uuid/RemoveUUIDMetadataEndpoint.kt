package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.CoreEndpoint
import com.pubnub.internal.CorePubNubClient
import com.pubnub.internal.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveUUIDMetadataEndpoint(
    pubnub: CorePubNubClient,
    override val uuid: String? = null,
) : CoreEndpoint<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), RemoveUUIDMetadataInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        return pubnub.retrofitManager.objectsService.deleteUUIDMetadata(
            subKey = pubnub.configuration.subscribeKey,
            uuid = uuid ?: pubnub.configuration.userId.value,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveUUIDMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveUUIDMetadataEndpoint(
    pubnub: PubNubImpl,
    override val uuid: String? = null,
) : EndpointCore<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), RemoveUUIDMetadata {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        return retrofitManager.objectsService.deleteUUIDMetadata(
            subKey = configuration.subscribeKey,
            uuid = uuid ?: configuration.userId.value,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveUUIDMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

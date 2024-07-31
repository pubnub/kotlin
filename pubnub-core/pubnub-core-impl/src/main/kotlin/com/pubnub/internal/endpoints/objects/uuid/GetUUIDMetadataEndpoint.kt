package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.getUUIDMetadata]
 */
class GetUUIDMetadataEndpoint internal constructor(
    pubnub: PubNubCore,
    override val uuid: String,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityEnvelope<PNUUIDMetadata>, PNUUIDMetadataResult>(pubnub), GetUUIDMetadataInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNUUIDMetadata>> {
        val params = queryParams + includeQueryParam.createIncludeQueryParams()
        return retrofitManager.objectsService.getUUIDMetadata(
            subKey = configuration.subscribeKey,
            uuid = uuid,
            options = params,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNUUIDMetadata>>): PNUUIDMetadataResult {
        return input.body()!!.let {
            PNUUIDMetadataResult(
                status = it.status,
                data = it.data!!,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetUUIDMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

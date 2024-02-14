package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.getAllUUIDMetadata]
 */
class GetAllUUIDMetadata internal constructor(
    pubnub: PubNubImpl,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val withInclude: IncludeQueryParam
) : Endpoint<EntityArrayEnvelope<PNUUIDMetadata>, PNUUIDMetadataArrayResult>(pubnub),
    IGetAllUUIDMetadata {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNUUIDMetadata>> {
        val params = queryParams + collectionQueryParameters.createCollectionQueryParams() + withInclude.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.getAllUUIDMetadata(
            subKey = pubnub.configuration.subscribeKey,
            options = params
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNUUIDMetadata>>): PNUUIDMetadataArrayResult {
        return input.body()!!.let { arrayEnvelope ->
            PNUUIDMetadataArrayResult(
                status = arrayEnvelope.status,
                data = arrayEnvelope.data,
                prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) },
                next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                totalCount = arrayEnvelope.totalCount
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetAllUUIDMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

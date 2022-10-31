package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.api.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
class GetAllUUIDMetadata internal constructor(
    pubnub: PubNub,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val withInclude: IncludeQueryParam
) : Endpoint<EntityArrayEnvelope<PNUUIDMetadata>, PNUUIDMetadataArrayResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNUUIDMetadata>> {
        val params = queryParams + collectionQueryParameters.createCollectionQueryParams() + withInclude.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.getAllUUIDMetadata(
            subKey = pubnub.configuration.subscribeKey,
            options = params
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNUUIDMetadata>>): PNUUIDMetadataArrayResult? {
        return input.body()?.let { arrayEnvelope ->
            PNUUIDMetadataArrayResult(
                status = arrayEnvelope.status,
                data = arrayEnvelope.data,
                prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) },
                next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                totalCount = arrayEnvelope.totalCount
            )
        }
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.PNGetAllUUIDMetadataOperation
    }
}

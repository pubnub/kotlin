package com.pubnub.api.endpoints.objects.uuid

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.ReturningCollection
import com.pubnub.api.endpoints.objects.internal.ReturningCustom
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap

/**
 * @see [PubNub.getAllUUIDMetadata]
 */
class GetAllUUIDMetadata internal constructor(
    pubnub: PubNub,
    private val returningCollection: ReturningCollection,
    private val withCustom: ReturningCustom
) : Endpoint<EntityArrayEnvelope<PNUUIDMetadata>, PNUUIDMetadataArrayResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNUUIDMetadata>> {
        val params = queryParams + returningCollection.createCollectionQueryParams() + withCustom.createIncludeQueryParams()

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

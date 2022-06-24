package com.pubnub.user

import com.pubnub.api.MoreAbstractEndpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.api.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.core.AppContext.getService
import com.pubnub.core.OperationType
import com.pubnub.user.models.consumer.UsersResult
import com.pubnub.user.models.consumer.toUsersResult
import retrofit2.Call
import retrofit2.Response

class FetchUsers internal constructor(
    pubnub: PubNub,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val withInclude: IncludeQueryParam
) : MoreAbstractEndpoint<EntityArrayEnvelope<PNUUIDMetadata>, UsersResult, OperationType>(pubnub) {

    override fun doWork(queryParams: Map<String, String>): Call<EntityArrayEnvelope<PNUUIDMetadata>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + withInclude.createIncludeQueryParams()

        val service = pubnub.getService<ObjectsService>()
        return service.getAllUUIDMetadata(
            subKey = pubnub.configuration.subscribeKey,
            options = params + mapOf("include" to "type,status")
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNUUIDMetadata>>): UsersResult? {
        return input.body()?.let { arrayEnvelope ->
            PNUUIDMetadataArrayResult(
                status = arrayEnvelope.status,
                data = arrayEnvelope.data,
                prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) },
                next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                totalCount = arrayEnvelope.totalCount
            ).toUsersResult()
        }
    }

    override fun operationType(): OperationType {
        return FetchUsersOperation
    }
}

data class PNUUIDMetadataResult(
    val status: Int,
    val data: PNUUIDMetadata?
)

data class EntityArrayEnvelope<T>(
    val status: Int = 0,
    val data: Collection<T> = listOf(),
    val totalCount: Int? = null,
    val next: String? = null,
    val prev: String? = null
)

data class PNUUIDMetadataArrayResult(
    val status: Int,
    val data: Collection<PNUUIDMetadata>,
    val totalCount: Int?,
    val next: PNPage.PNNext?,
    val prev: PNPage.PNPrev?
)

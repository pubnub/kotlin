package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PartialPNChannelMetadata
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.getAllChannelMetadata]
 */
class GetAllChannelMetadataEndpoint internal constructor(
    pubnub: PubNubCore,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityArrayEnvelope<PartialPNChannelMetadata>, PNChannelMetadataArrayResult>(pubnub),
    GetAllChannelMetadataInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PartialPNChannelMetadata>> {
        val params =
            queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return retrofitManager.objectsService.getAllChannelMetadata(
            subKey = configuration.subscribeKey,
            options = params,
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PartialPNChannelMetadata>>): PNChannelMetadataArrayResult {
        return input.body()!!.let { arrayEnvelope ->
            PNChannelMetadataArrayResult(
                status = arrayEnvelope.status,
                data = arrayEnvelope.data,
                prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) },
                next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                totalCount = arrayEnvelope.totalCount,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetAllChannelsMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

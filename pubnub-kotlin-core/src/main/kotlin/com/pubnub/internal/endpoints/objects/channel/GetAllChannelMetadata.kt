package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.internal.models.server.objects_api.EntityArrayEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.getAllChannelMetadata]
 */
class GetAllChannelMetadata internal constructor(
    pubnub: PubNubImpl,
    private val collectionQueryParameters: CollectionQueryParameters,
    private val includeQueryParam: IncludeQueryParam
) : Endpoint<EntityArrayEnvelope<PNChannelMetadata>, PNChannelMetadataArrayResult>(pubnub),
    IGetAllChannelMetadata {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityArrayEnvelope<PNChannelMetadata>> {
        val params = queryParams + collectionQueryParameters.createCollectionQueryParams() + includeQueryParam.createIncludeQueryParams()

        return pubnub.retrofitManager.objectsService.getAllChannelMetadata(
            subKey = pubnub.configuration.subscribeKey,
            options = params
        )
    }

    override fun createResponse(input: Response<EntityArrayEnvelope<PNChannelMetadata>>): PNChannelMetadataArrayResult {
        return input.body()!!.let { arrayEnvelope ->
            PNChannelMetadataArrayResult(
                status = arrayEnvelope.status,
                data = arrayEnvelope.data,
                prev = arrayEnvelope.prev?.let { PNPage.PNPrev(it) },
                next = arrayEnvelope.next?.let { PNPage.PNNext(it) },
                totalCount = arrayEnvelope.totalCount
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetAllChannelsMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

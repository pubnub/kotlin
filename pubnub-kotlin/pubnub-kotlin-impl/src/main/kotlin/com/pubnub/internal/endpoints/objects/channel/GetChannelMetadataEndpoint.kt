package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.getChannelMetadata]
 */
class GetChannelMetadataEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channel: String,
    private val includeQueryParam: IncludeQueryParam,
) : EndpointCore<EntityEnvelope<PNChannelMetadata>, PNChannelMetadataResult>(pubnub), GetChannelMetadata {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNChannelMetadata>> {
        val params = queryParams + includeQueryParam.createIncludeQueryParams()
        return retrofitManager.objectsService.getChannelMetadata(
            subKey = configuration.subscribeKey,
            channel = channel,
            options = params,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNChannelMetadata>>): PNChannelMetadataResult {
        return input.body()!!.let {
            PNChannelMetadataResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetChannelMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

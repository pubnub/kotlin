package com.pubnub.api.endpoints.objects.channel

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.getChannelMetadata]
 */
class GetChannelMetadata internal constructor(
    pubnub: PubNub,
    private val channel: String,
    private val includeQueryParam: IncludeQueryParam
) : Endpoint<EntityEnvelope<PNChannelMetadata>, PNChannelMetadataResult>(pubnub) {

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNChannelMetadata>> {
        val params = queryParams + includeQueryParam.createIncludeQueryParams()
        return pubnub.retrofitManager.objectsService.getChannelMetadata(
            subKey = pubnub.configuration.subscribeKey,
            channel = channel,
            options = params
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNChannelMetadata>>): PNChannelMetadataResult? {
        return input.body()?.let {
            PNChannelMetadataResult(
                status = it.status,
                data = it.data
            )
        }
    }

    override fun operationType(): PNOperationType {
        return PNOperationType.PNGetChannelMetadataOperation
    }
}

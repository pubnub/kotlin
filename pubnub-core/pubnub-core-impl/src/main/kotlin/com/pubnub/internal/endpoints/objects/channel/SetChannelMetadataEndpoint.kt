package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.api.utils.Optional
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
import com.pubnub.internal.models.server.objects_api.ChannelMetadataInput
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubCore.setChannelMetadata]
 */
class SetChannelMetadataEndpoint internal constructor(
    pubnub: PubNubCore,
    private val name: Optional<String?>,
    private val description: Optional<String?>,
    private val custom: Optional<Any?>,
    private val channel: String,
    private val includeQueryParam: IncludeQueryParam,
    private val type: Optional<String?>,
    private val status: Optional<String?>,
) : EndpointCore<EntityEnvelope<PNChannelMetadata>, PNChannelMetadataResult>(pubnub),
    SetChannelMetadataInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNChannelMetadata>> {
        val params = queryParams + includeQueryParam.createIncludeQueryParams()
        return retrofitManager.objectsService.setChannelMetadata(
            subKey = configuration.subscribeKey,
            body =
                ChannelMetadataInput(
                    name = name,
                    custom = custom,
                    description = description,
                    status = status,
                    type = type,
                ),
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

    override fun operationType(): PNOperationType = PNOperationType.PNSetChannelMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

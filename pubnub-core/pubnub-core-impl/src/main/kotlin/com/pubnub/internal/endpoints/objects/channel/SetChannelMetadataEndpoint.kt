package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.channel.NewPNChannelMetadata
import com.pubnub.api.retry.RetryableEndpointGroup
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
    private val name: String?,
    private val description: String?,
    private val custom: Any?,
    private val channel: String,
    private val includeQueryParam: IncludeQueryParam,
    private val type: String?,
    private val status: String?,
) : EndpointCore<EntityEnvelope<NewPNChannelMetadata>, PNChannelMetadataResult>(pubnub),
    SetChannelMetadataInterface {
    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<NewPNChannelMetadata>> {
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

    override fun createResponse(input: Response<EntityEnvelope<NewPNChannelMetadata>>): PNChannelMetadataResult {
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

package com.pubnub.internal.endpoints.datasync.channel

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.channel.GetChannel
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.channel.DataSyncChannel
import com.pubnub.api.models.consumer.datasync.channel.DataSyncGetChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getChannel]
 */
class GetChannelEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channelId: String,
) : EndpointCore<EntityEnvelope<DataSyncChannel>, DataSyncGetChannelResult>(pubnub), GetChannel {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (channelId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<DataSyncChannel>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channelId" to channelId
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetChannel API call",
            )
        )
        return retrofitManager.dataSyncService.getChannel(
            subKey = configuration.subscribeKey,
            channelId = channelId,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<DataSyncChannel>>): DataSyncGetChannelResult {
        return input.body()!!.let {
            DataSyncGetChannelResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetChannelOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

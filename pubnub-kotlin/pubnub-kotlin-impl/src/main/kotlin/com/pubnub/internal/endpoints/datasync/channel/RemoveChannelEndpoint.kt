package com.pubnub.internal.endpoints.datasync.channel

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.channel.RemoveChannel
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.channel.DataSyncRemoveChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.removeChannel]
 */
class RemoveChannelEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channelId: String,
    private val ifMatch: String?,
) : EndpointCore<Void, DataSyncRemoveChannelResult>(pubnub), RemoveChannel {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (channelId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channelId" to channelId,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "RemoveChannel API call",
            )
        )
        return retrofitManager.dataSyncService.deleteChannel(
            subKey = configuration.subscribeKey,
            channelId = channelId,
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): DataSyncRemoveChannelResult {
        // DeleteSuccessResponse has an empty body, so surface the HTTP status code.
        return DataSyncRemoveChannelResult(input.code())
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveChannelOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

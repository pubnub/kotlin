package com.pubnub.internal.endpoints.datasync.channel

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.channel.SetChannel
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.channel.DataSyncChannel
import com.pubnub.api.models.consumer.datasync.channel.DataSyncSetChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.SetEntityRequest
import com.pubnub.internal.models.server.datasync.SetEntityRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.setChannel]
 */
class SetChannelEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channelId: String,
    private val classVersion: Int,
    private val status: String?,
    private val payload: Any?,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<DataSyncChannel>, DataSyncSetChannelResult>(pubnub), SetChannel {
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
                        "channelId" to channelId,
                        "classVersion" to classVersion,
                        "status" to (status ?: ""),
                        "payload" to (payload ?: ""),
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "SetChannel API call",
            )
        )
        return retrofitManager.dataSyncService.setChannel(
            subKey = configuration.subscribeKey,
            channelId = channelId,
            body =
                SetEntityRequest(
                    data =
                        SetEntityRequestData(
                            entityClassVersion = classVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<DataSyncChannel>>): DataSyncSetChannelResult {
        return input.body()!!.let {
            DataSyncSetChannelResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNSetChannelOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

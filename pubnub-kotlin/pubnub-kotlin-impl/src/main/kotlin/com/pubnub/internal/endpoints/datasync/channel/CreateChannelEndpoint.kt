package com.pubnub.internal.endpoints.datasync.channel

import com.pubnub.api.endpoints.datasync.channel.CreateChannel
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel
import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncCreateChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.CreateChannelRequest
import com.pubnub.internal.models.server.datasync.CreateChannelRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.createChannel]
 */
class CreateChannelEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val className: String?,
    private val classVersion: Int,
    private val classLevel: PNDataSyncClassLevel?,
    private val channelId: String?,
    private val status: String?,
    private val payload: Any?,
) : EndpointCore<EntityEnvelope<PNDataSyncChannel>, PNDataSyncCreateChannelResult>(pubnub), CreateChannel {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncChannel>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "className" to (className ?: ""),
                        "classVersion" to classVersion,
                        "classLevel" to (classLevel?.value ?: ""),
                        "channelId" to (channelId ?: ""),
                        "status" to (status ?: ""),
                        "payload" to (payload ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "CreateChannel API call",
            )
        )
        return retrofitManager.dataSyncService.createChannel(
            subKey = configuration.subscribeKey,
            body =
                CreateChannelRequest(
                    data =
                        CreateChannelRequestData(
                            id = channelId,
                            entityClass = className,
                            entityClassVersion = classVersion,
                            entityClassLevel = classLevel?.value,
                            status = status,
                            payload = payload,
                        ),
                ),
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNDataSyncChannel>>): PNDataSyncCreateChannelResult {
        return input.body()!!.let {
            PNDataSyncCreateChannelResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNCreateChannelOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

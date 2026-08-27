package com.pubnub.internal.endpoints.datasync.channel

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.channel.UpdateChannel
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.channel.DataSyncChannel
import com.pubnub.api.models.consumer.datasync.channel.DataSyncUpdateChannelResult
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.JsonPatchOperation
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.updateChannel]
 */
class UpdateChannelEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val channelId: String,
    private val operations: List<PNJsonPatchOperation>,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<DataSyncChannel>, DataSyncUpdateChannelResult>(pubnub), UpdateChannel {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (channelId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
        if (operations.isEmpty()) {
            throw PubNubException(PubNubError.JSON_PATCH_OPERATIONS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<DataSyncChannel>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channelId" to channelId,
                        "operations" to operations,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "UpdateChannel API call",
            )
        )
        return retrofitManager.dataSyncService.updateChannel(
            subKey = configuration.subscribeKey,
            channelId = channelId,
            body = operations.map { JsonPatchOperation(op = it.op, path = it.path, value = it.value, from = it.from) },
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<DataSyncChannel>>): DataSyncUpdateChannelResult {
        return input.body()!!.let {
            DataSyncUpdateChannelResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNUpdateChannelOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

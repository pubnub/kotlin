package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.RemoveEntity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.entity.DataSyncRemoveEntityResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.removeEntity]
 */
class RemoveEntityEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityId: String,
    private val ifMatch: String?,
) : EndpointCore<Void, DataSyncRemoveEntityResult>(pubnub), RemoveEntity {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (entityId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityId" to entityId,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "RemoveEntity API call",
            )
        )
        return retrofitManager.dataSyncService.deleteEntity(
            subKey = configuration.subscribeKey,
            entityId = entityId,
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): DataSyncRemoveEntityResult {
        // DeleteSuccessResponse has an empty body, so surface the HTTP status code.
        return DataSyncRemoveEntityResult(input.code())
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveEntityOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

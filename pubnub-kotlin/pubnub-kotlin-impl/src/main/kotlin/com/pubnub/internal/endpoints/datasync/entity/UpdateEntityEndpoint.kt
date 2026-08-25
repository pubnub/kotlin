package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.UpdateEntity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.datasync.entity.PNUpdateEntityResult
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
 * @see [com.pubnub.api.datasync.DataSync.updateEntity]
 */
class UpdateEntityEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityId: String,
    private val operations: List<PNJsonPatchOperation>,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNEntity>, PNUpdateEntityResult>(pubnub), UpdateEntity {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (entityId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
        if (operations.isEmpty()) {
            throw PubNubException(PubNubError.JSON_PATCH_OPERATIONS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNEntity>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityId" to entityId,
                        "operations" to operations,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "UpdateEntity API call",
            )
        )
        return retrofitManager.dataSyncService.updateEntity(
            subKey = configuration.subscribeKey,
            entityId = entityId,
            body = operations.map { JsonPatchOperation(op = it.op, path = it.path, value = it.value, from = it.from) },
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNEntity>>): PNUpdateEntityResult {
        return input.body()!!.let {
            PNUpdateEntityResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNUpdateEntityOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

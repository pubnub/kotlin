package com.pubnub.internal.endpoints.datasync.relationship

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.relationship.UpdateRelationship
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncUpdateRelationshipResult
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
 * @see [com.pubnub.api.datasync.DataSync.updateRelationship]
 */
class UpdateRelationshipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val relationshipId: String,
    private val operations: List<PNJsonPatchOperation>,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNDataSyncRelationship>, PNDataSyncUpdateRelationshipResult>(pubnub), UpdateRelationship {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (relationshipId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
        if (operations.isEmpty()) {
            throw PubNubException(PubNubError.JSON_PATCH_OPERATIONS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncRelationship>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "relationshipId" to relationshipId,
                        "operations" to operations,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "UpdateRelationship API call",
            )
        )
        return retrofitManager.dataSyncService.updateRelationship(
            subKey = configuration.subscribeKey,
            relationshipId = relationshipId,
            body = operations.map { JsonPatchOperation(op = it.op, path = it.path, value = it.value, from = it.from) },
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntityEnvelope<PNDataSyncRelationship>>,
    ): PNDataSyncUpdateRelationshipResult {
        return input.body()!!.let {
            PNDataSyncUpdateRelationshipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNUpdateRelationshipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

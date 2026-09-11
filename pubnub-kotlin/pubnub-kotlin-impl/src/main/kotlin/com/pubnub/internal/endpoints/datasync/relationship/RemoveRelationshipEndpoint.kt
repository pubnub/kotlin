package com.pubnub.internal.endpoints.datasync.relationship

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.relationship.RemoveRelationship
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRemoveRelationshipResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.removeRelationship]
 */
class RemoveRelationshipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val relationshipId: String,
    private val ifMatch: String?,
) : EndpointCore<Void, PNDataSyncRemoveRelationshipResult>(pubnub), RemoveRelationship {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (relationshipId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "relationshipId" to relationshipId,
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "RemoveRelationship API call",
            )
        )
        return retrofitManager.dataSyncService.deleteRelationship(
            subKey = configuration.subscribeKey,
            relationshipId = relationshipId,
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<Void>): PNDataSyncRemoveRelationshipResult {
        // DeleteSuccessResponse has an empty body, so surface the HTTP status code.
        return PNDataSyncRemoveRelationshipResult(input.code())
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveRelationshipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

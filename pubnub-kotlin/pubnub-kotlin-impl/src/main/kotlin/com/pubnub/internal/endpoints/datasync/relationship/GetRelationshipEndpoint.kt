package com.pubnub.internal.endpoints.datasync.relationship

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.relationship.GetRelationship
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncGetRelationshipResult
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getRelationship]
 */
class GetRelationshipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val relationshipId: String,
) : EndpointCore<EntityEnvelope<PNDataSyncRelationship>, PNDataSyncGetRelationshipResult>(pubnub), GetRelationship {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (relationshipId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncRelationship>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "relationshipId" to relationshipId
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetRelationship API call",
            )
        )
        return retrofitManager.dataSyncService.getRelationship(
            subKey = configuration.subscribeKey,
            relationshipId = relationshipId,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNDataSyncRelationship>>): PNDataSyncGetRelationshipResult {
        return input.body()!!.let {
            PNDataSyncGetRelationshipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetRelationshipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

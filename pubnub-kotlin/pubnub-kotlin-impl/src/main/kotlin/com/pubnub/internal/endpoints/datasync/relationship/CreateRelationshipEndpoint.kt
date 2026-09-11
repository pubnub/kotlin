package com.pubnub.internal.endpoints.datasync.relationship

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.relationship.CreateRelationship
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncCreateRelationshipResult
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.CreateRelationshipRequest
import com.pubnub.internal.models.server.datasync.CreateRelationshipRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.createRelationship]
 */
class CreateRelationshipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityAId: String,
    private val entityBId: String,
    private val className: String,
    private val classVersion: Int,
    private val relationshipId: String?,
    private val status: String?,
    private val payload: Any?,
) : EndpointCore<EntityEnvelope<PNDataSyncRelationship>, PNDataSyncCreateRelationshipResult>(pubnub), CreateRelationship {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        // entityAId / entityBId are required on create (unlike the optional relationshipId, which the server
        // generates when omitted).
        if (entityAId.isBlank() || entityBId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
        // className is required on create — unlike Membership, which implies its class.
        if (className.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_CLASS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncRelationship>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityAId" to entityAId,
                        "entityBId" to entityBId,
                        "className" to className,
                        "classVersion" to classVersion,
                        "relationshipId" to (relationshipId ?: ""),
                        "status" to (status ?: ""),
                        "payload" to (payload ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "CreateRelationship API call",
            )
        )
        return retrofitManager.dataSyncService.createRelationship(
            subKey = configuration.subscribeKey,
            body =
                CreateRelationshipRequest(
                    data =
                        CreateRelationshipRequestData(
                            id = relationshipId,
                            entityAId = entityAId,
                            entityBId = entityBId,
                            relationshipClass = className,
                            relationshipClassVersion = classVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntityEnvelope<PNDataSyncRelationship>>,
    ): PNDataSyncCreateRelationshipResult {
        return input.body()!!.let {
            PNDataSyncCreateRelationshipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNCreateRelationshipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

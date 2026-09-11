package com.pubnub.internal.endpoints.datasync.relationship

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.relationship.SetRelationship
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncSetRelationshipResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.SetRelationshipRequest
import com.pubnub.internal.models.server.datasync.SetRelationshipRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.setRelationship]
 */
class SetRelationshipEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val relationshipId: String,
    private val classVersion: Int,
    private val status: String?,
    private val payload: Any?,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNDataSyncRelationship>, PNDataSyncSetRelationshipResult>(pubnub), SetRelationship {
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
                        "relationshipId" to relationshipId,
                        "classVersion" to classVersion,
                        "status" to (status ?: ""),
                        "payload" to (payload ?: ""),
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "SetRelationship API call",
            )
        )
        return retrofitManager.dataSyncService.setRelationship(
            subKey = configuration.subscribeKey,
            relationshipId = relationshipId,
            body =
                SetRelationshipRequest(
                    data =
                        SetRelationshipRequestData(
                            relationshipClassVersion = classVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(
        input: Response<EntityEnvelope<PNDataSyncRelationship>>,
    ): PNDataSyncSetRelationshipResult {
        return input.body()!!.let {
            PNDataSyncSetRelationshipResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNSetRelationshipOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.SetEntity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import com.pubnub.api.models.consumer.datasync.entity.PNSetEntityResult
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
 * @see [com.pubnub.api.datasync.DataSync.setEntity]
 */
class SetEntityEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityId: String,
    private val entityClassVersion: Int,
    private val status: String?,
    private val payload: Any?,
    private val ifMatch: String?,
) : EndpointCore<EntityEnvelope<PNEntity>, PNSetEntityResult>(pubnub), SetEntity {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (entityId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNEntity>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityId" to entityId,
                        "entityClassVersion" to entityClassVersion,
                        "status" to (status ?: ""),
                        "payload" to (payload ?: ""),
                        "ifMatch" to (ifMatch ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "SetEntity API call",
            )
        )
        return retrofitManager.dataSyncService.setEntity(
            subKey = configuration.subscribeKey,
            entityId = entityId,
            body =
                SetEntityRequest(
                    data =
                        SetEntityRequestData(
                            entityClassVersion = entityClassVersion,
                            status = status,
                            payload = payload,
                        ),
                ),
            ifMatch = ifMatch,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNEntity>>): PNSetEntityResult {
        return input.body()!!.let {
            PNSetEntityResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNSetEntityOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

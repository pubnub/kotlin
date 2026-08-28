package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.CreateEntity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.PNDataSyncClassLevel
import com.pubnub.api.models.consumer.datasync.entity.DataSyncCreateEntityResult
import com.pubnub.api.models.consumer.datasync.entity.DataSyncEntity
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.datasync.CreateEntityRequest
import com.pubnub.internal.models.server.datasync.CreateEntityRequestData
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.createEntity]
 */
class CreateEntityEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val className: String,
    private val classVersion: Int,
    private val classLevel: PNDataSyncClassLevel?,
    private val entityId: String?,
    private val status: String?,
    private val payload: Any?,
) : EndpointCore<EntityEnvelope<DataSyncEntity>, DataSyncCreateEntityResult>(pubnub), CreateEntity {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (className.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_CLASS_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<DataSyncEntity>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "className" to className,
                        "classVersion" to classVersion,
                        "classLevel" to (classLevel?.value ?: ""),
                        "entityId" to (entityId ?: ""),
                        "status" to (status ?: ""),
                        "payload" to (payload ?: "")
                    ),
                    operation = this::class.simpleName
                ),
                details = "CreateEntity API call",
            )
        )
        return retrofitManager.dataSyncService.createEntity(
            subKey = configuration.subscribeKey,
            body =
                CreateEntityRequest(
                    data =
                        CreateEntityRequestData(
                            id = entityId,
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

    override fun createResponse(input: Response<EntityEnvelope<DataSyncEntity>>): DataSyncCreateEntityResult {
        return input.body()!!.let {
            DataSyncCreateEntityResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNCreateEntityOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

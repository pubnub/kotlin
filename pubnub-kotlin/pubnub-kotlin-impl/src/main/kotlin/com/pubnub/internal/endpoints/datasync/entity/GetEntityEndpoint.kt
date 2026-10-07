package com.pubnub.internal.endpoints.datasync.entity

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.datasync.entity.GetEntity
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncEntity
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncGetEntityResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

/**
 * @see [com.pubnub.api.datasync.DataSync.getEntity]
 */
class GetEntityEndpoint internal constructor(
    pubnub: PubNubImpl,
    private val entityId: String,
) : EndpointCore<EntityEnvelope<PNDataSyncEntity>, PNDataSyncGetEntityResult>(pubnub), GetEntity {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (entityId.isBlank()) {
            throw PubNubException(PubNubError.ENTITY_ID_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNDataSyncEntity>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "entityId" to entityId
                    ),
                    operation = this::class.simpleName
                ),
                details = "GetEntity API call",
            )
        )
        return retrofitManager.dataSyncService.getEntity(
            subKey = configuration.subscribeKey,
            entityId = entityId,
            options = queryParams,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNDataSyncEntity>>): PNDataSyncGetEntityResult {
        return input.body()!!.let {
            PNDataSyncGetEntityResult(
                status = it.status,
                data = it.data,
            )
        }
    }

    override fun operationType(): PNOperationType = PNOperationType.PNGetEntityOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.DATASYNC
}

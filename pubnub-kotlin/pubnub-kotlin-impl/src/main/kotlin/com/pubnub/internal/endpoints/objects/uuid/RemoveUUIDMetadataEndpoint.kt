package com.pubnub.internal.endpoints.objects.uuid

import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response

class RemoveUUIDMetadataEndpoint(
    pubnub: PubNubImpl,
    override val uuid: String? = null,
) : EndpointCore<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), RemoveUUIDMetadata {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        log.trace(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "uuid" to (uuid ?: configuration.userId.value),
                        "queryParams" to queryParams
                    )
                ),
                details = "RemoveUUIDMetadata API call",
            )
        )
        return retrofitManager.objectsService.deleteUUIDMetadata(
            subKey = configuration.subscribeKey,
            uuid = uuid ?: configuration.userId.value,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveUUIDMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

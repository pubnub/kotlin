package com.pubnub.internal.endpoints.objects.channel

import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.ExtendedLogger
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Response

class RemoveChannelMetadataEndpoint(
    pubnub: PubNubImpl,
    private val channel: String,
) : EndpointCore<EntityEnvelope<Any?>, PNRemoveMetadataResult>(pubnub), RemoveChannelMetadata {
    private val log: ExtendedLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<Any?>> {
        log.trace(
            LogMessage(
                pubNubId = pubnub.instanceId,
                logLevel = Level.TRACE,
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "channel" to channel,
                        "queryParams" to queryParams
                    )
                ),
                details = "RemoveChannelMetadata API call"
            )
        )
        return retrofitManager.objectsService.deleteChannelMetadata(
            subKey = configuration.subscribeKey,
            channel = channel,
        )
    }

    override fun createResponse(input: Response<EntityEnvelope<Any?>>): PNRemoveMetadataResult {
        return PNRemoveMetadataResult(input.body()!!.status)
    }

    override fun operationType(): PNOperationType = PNOperationType.PNRemoveChannelMetadataOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.APP_CONTEXT
}

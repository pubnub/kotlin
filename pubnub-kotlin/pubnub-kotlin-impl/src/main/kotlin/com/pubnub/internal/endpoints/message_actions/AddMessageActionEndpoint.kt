package com.pubnub.internal.endpoints.message_actions

import com.google.gson.JsonObject
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.addMessageAction]
 */
class AddMessageActionEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channel: String,
    override val messageAction: PNMessageAction,
) : EndpointCore<EntityEnvelope<PNMessageAction>, PNAddMessageActionResult>(pubnub), AddMessageAction {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (channel.isBlank()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (messageAction.type.isBlank()) {
            throw PubNubException(PubNubError.MESSAGE_ACTION_TYPE_MISSING)
        }
        if (messageAction.value.isBlank()) {
            throw PubNubException(PubNubError.MESSAGE_ACTION_VALUE_MISSING)
        }
    }

    override fun getAffectedChannels() = listOf(channel)

    override fun doWork(queryParams: HashMap<String, String>): Call<EntityEnvelope<PNMessageAction>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channel" to channel,
                        "messageActionType" to messageAction.type,
                        "messageActionValue" to messageAction.value,
                        "messageTimetoken" to messageAction.messageTimetoken
                    ),
                    operation = this::class.simpleName
                ),
                details = "AddMessageAction API call",
            )
        )

        val body =
            JsonObject().apply {
                addProperty("type", messageAction.type)
                addProperty("value", messageAction.value)
            }

        return retrofitManager.messageActionService
            .addMessageAction(
                subKey = configuration.subscribeKey,
                channel = channel,
                messageTimetoken = messageAction.messageTimetoken.toString().lowercase(Locale.getDefault()),
                body = body,
                options = queryParams,
            )
    }

    override fun createResponse(input: Response<EntityEnvelope<PNMessageAction>>): PNAddMessageActionResult =
        PNAddMessageActionResult(
            action = input.body()!!.data!!,
        )

    override fun operationType() = PNOperationType.PNAddMessageAction

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_REACTION
}

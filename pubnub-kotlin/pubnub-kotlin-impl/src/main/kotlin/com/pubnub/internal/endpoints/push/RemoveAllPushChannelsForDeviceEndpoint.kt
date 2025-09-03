package com.pubnub.internal.endpoints.push

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDeviceEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val pushType: PNPushType,
    override val deviceId: String,
    override val environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
    override val topic: String? = null,
) : EndpointCore<Void, PNPushRemoveAllChannelsResult>(pubnub), RemoveAllPushChannelsForDevice {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun validateParams() {
        super.validateParams()
        if (deviceId.isBlank()) {
            throw PubNubException(PubNubError.DEVICE_ID_MISSING)
        }
        if (pushType == PNPushType.APNS2 && topic.isNullOrBlank()) {
            throw PubNubException(PubNubError.PUSH_TOPIC_MISSING)
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        log.trace(
            LogMessage(
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "pushType" to pushType,
                        "deviceId" to deviceId,
                        "environment" to environment,
                        "topic" to (topic ?: ""),
                        "queryParams" to queryParams
                    )
                ),
                details = "RemoveAllPushChannelsForDevice API call"
            )
        )

        addQueryParams(queryParams)

        return if (pushType != PNPushType.APNS2) {
            retrofitManager.pushService
                .removeAllChannelsForDevice(
                    subKey = configuration.subscribeKey,
                    pushToken = deviceId,
                    options = queryParams,
                )
        } else {
            retrofitManager.pushService
                .removeAllChannelsForDeviceApns2(
                    subKey = configuration.subscribeKey,
                    deviceApns2 = deviceId,
                    options = queryParams,
                )
        }
    }

    override fun createResponse(input: Response<Void>): PNPushRemoveAllChannelsResult = PNPushRemoveAllChannelsResult()

    override fun operationType() = PNOperationType.PNRemoveAllPushNotificationsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUSH_NOTIFICATION

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (pushType != PNPushType.APNS2) {
            queryParams["type"] = pushType.toParamString()
            return
        }

        queryParams["environment"] = environment.name.lowercase(Locale.getDefault())
        topic?.run { queryParams["topic"] = this }
    }
}

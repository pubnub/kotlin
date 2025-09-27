package com.pubnub.internal.endpoints.push

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [PubNubImpl.auditPushChannelProvisions]
 */
class ListPushProvisionsEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val pushType: PNPushType,
    override val deviceId: String,
    override val topic: String? = null,
    override val environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
) : EndpointCore<List<String>, PNPushListProvisionsResult>(pubnub), ListPushProvisions {
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

    override fun doWork(queryParams: HashMap<String, String>): Call<List<String>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    message = mapOf(
                        "pushType" to pushType,
                        "deviceId" to deviceId,
                        "topic" to (topic ?: ""),
                        "environment" to environment,
                        "queryParams" to queryParams
                    )
                ),
                details = "ListPushProvisions API call",
            )
        )

        addQueryParams(queryParams)

        return if (pushType != PNPushType.APNS2) {
            retrofitManager.pushService
                .listChannelsForDevice(
                    subKey = configuration.subscribeKey,
                    pushToken = deviceId,
                    options = queryParams,
                )
        } else {
            retrofitManager.pushService
                .listChannelsForDeviceApns2(
                    subKey = configuration.subscribeKey,
                    deviceApns2 = deviceId,
                    options = queryParams,
                )
        }
    }

    override fun createResponse(input: Response<List<String>>): PNPushListProvisionsResult = PNPushListProvisionsResult(input.body()!!)

    override fun operationType() = PNOperationType.PNPushNotificationEnabledChannelsOperation

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (pushType != PNPushType.APNS2) {
            queryParams["type"] = pushType.toParamString()
            return
        }

        queryParams["environment"] = environment.name.lowercase(Locale.getDefault())
        topic?.run { queryParams["topic"] = this }
    }

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUSH_NOTIFICATION
}

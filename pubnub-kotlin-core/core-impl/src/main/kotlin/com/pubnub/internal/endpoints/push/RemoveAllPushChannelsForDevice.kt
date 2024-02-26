package com.pubnub.internal.endpoints.push

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.InternalPubNubClient
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [InternalPubNubClient.removeAllPushNotificationsFromDeviceWithPushToken]
 */
class RemoveAllPushChannelsForDevice internal constructor(
    pubnub: InternalPubNubClient,
    override val pushType: PNPushType,
    override val deviceId: String,
    override val environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT,
    override val topic: String? = null,
) : Endpoint<Void, PNPushRemoveAllChannelsResult>(pubnub), IRemoveAllPushChannelsForDevice {
    override fun validateParams() {
        super.validateParams()
        if (deviceId.isBlank()) throw PubNubException(PubNubError.DEVICE_ID_MISSING)
        if (pushType == PNPushType.APNS2 && topic.isNullOrBlank()) throw PubNubException(PubNubError.PUSH_TOPIC_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {
        addQueryParams(queryParams)

        return if (pushType != PNPushType.APNS2) {
            pubnub.retrofitManager.pushService
                .removeAllChannelsForDevice(
                    subKey = pubnub.configuration.subscribeKey,
                    pushToken = deviceId,
                    options = queryParams,
                )
        } else {
            pubnub.retrofitManager.pushService
                .removeAllChannelsForDeviceApns2(
                    subKey = pubnub.configuration.subscribeKey,
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

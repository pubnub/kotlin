package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.policies.RetryableEndpointGroup
import com.pubnub.api.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap
import java.util.Locale

/**
 * @see [PubNub.removePushNotificationsFromChannels]
 */
class RemoveChannelsFromPush internal constructor(
    pubnub: PubNub,
    val pushType: PNPushType,
    val channels: List<String>,
    val deviceId: String,
    val topic: String? = null,
    val environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
) : Endpoint<Void, PNPushRemoveChannelResult>(pubnub) {

    override fun getAffectedChannels() = channels

    override fun validateParams() {
        super.validateParams()
        if (deviceId.isBlank()) throw PubNubException(PubNubError.DEVICE_ID_MISSING)
        if (channels.isEmpty()) throw PubNubException(PubNubError.CHANNEL_MISSING)
        if (pushType == PNPushType.APNS2 && topic.isNullOrBlank()) throw PubNubException(PubNubError.PUSH_TOPIC_MISSING)
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<Void> {

        addQueryParams(queryParams)

        return if (pushType != PNPushType.APNS2)
            pubnub.retrofitManager.pushService
                .modifyChannelsForDevice(
                    subKey = pubnub.configuration.subscribeKey,
                    pushToken = deviceId,
                    options = queryParams
                )
        else
            pubnub.retrofitManager.pushService
                .modifyChannelsForDeviceApns2(
                    subKey = pubnub.configuration.subscribeKey,
                    deviceApns2 = deviceId,
                    options = queryParams
                )
    }

    override fun createResponse(input: Response<Void>): PNPushRemoveChannelResult =
        PNPushRemoveChannelResult()

    override fun operationType() = PNOperationType.PNRemovePushNotificationsFromChannelsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUSH_NOTIFICATION

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        queryParams["remove"] = channels.toCsv()

        if (pushType != PNPushType.APNS2) {
            queryParams["type"] = pushType.toParamString()
            return
        }

        queryParams["environment"] = environment.name.lowercase(Locale.getDefault())
        topic?.run { queryParams["topic"] = this }
    }
}

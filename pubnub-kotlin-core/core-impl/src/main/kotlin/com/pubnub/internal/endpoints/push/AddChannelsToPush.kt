package com.pubnub.internal.endpoints.push

import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.InternalPubNubClient
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

/**
 * @see [InternalPubNubClient.addPushNotificationsOnChannels]
 */
class AddChannelsToPush internal constructor(
    pubnub: InternalPubNubClient,
    override val pushType: PNPushType,
    override val channels: List<String>,
    override val deviceId: String,
    override val topic: String? = null,
    override val environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
) : Endpoint<Void, PNPushAddChannelResult>(pubnub), IAddChannelsToPush {

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

    override fun createResponse(input: Response<Void>): PNPushAddChannelResult =
        PNPushAddChannelResult()

    override fun operationType() = PNOperationType.PNAddPushNotificationsOnChannelsOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PUSH_NOTIFICATION

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        queryParams["add"] = channels.toCsv()

        if (pushType != PNPushType.APNS2) {
            queryParams["type"] = pushType.toParamString()
            return
        }

        queryParams["environment"] = environment.name.lowercase(Locale.getDefault())
        topic?.run { queryParams["topic"] = this }
    }
}

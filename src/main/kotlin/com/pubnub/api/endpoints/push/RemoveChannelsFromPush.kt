package com.pubnub.api.endpoints.push

import com.pubnub.api.*
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import retrofit2.Call
import retrofit2.Response
import java.util.*

class RemoveChannelsFromPush(pubnub: PubNub) : Endpoint<List<Any>, PNPushRemoveChannelResult>(pubnub) {

    lateinit var pushType: PNPushType
    lateinit var channels: List<String>
    lateinit var deviceId: String
    var environment = PNPushEnvironment.DEVELOPMENT
    lateinit var topic: String

    override fun getAffectedChannels() = channels

    override fun validateParams() {
        super.validateParams()
        if (!::pushType.isInitialized) {
            throw PubNubException(PubNubError.PUSH_TYPE_MISSING)
        }
        if (!::deviceId.isInitialized || deviceId.isBlank()) {
            throw PubNubException(PubNubError.DEVICE_ID_MISSING)
        }
        if (!::channels.isInitialized || channels.isEmpty()) {
            throw PubNubException(PubNubError.CHANNEL_MISSING)
        }
        if (pushType == PNPushType.APNS2) {
            if (!::topic.isInitialized || topic.isBlank()) {
                throw PubNubException(PubNubError.PUSH_TOPIC_MISSING)
            }
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Any>> {
        queryParams["remove"] = channels.toCsv()

        if (pushType != PNPushType.APNS2) {
            queryParams["type"] = pushType.toParamString()

            return pubnub.retrofitManager.pushService
                .modifyChannelsForDevice(
                    subKey = pubnub.configuration.subscribeKey,
                    pushToken = deviceId,
                    options = queryParams
                )
        }

        queryParams["environment"] = environment.name.toLowerCase()
        queryParams["topic"] = topic

        return pubnub.retrofitManager.pushService
            .modifyChannelsForDeviceApns2(
                subKey = pubnub.configuration.subscribeKey,
                deviceApns2 = deviceId,
                options = queryParams
            )

    }

    override fun createResponse(input: Response<List<Any>>): PNPushRemoveChannelResult? {
        return PNPushRemoveChannelResult()
    }

    override fun operationType() = PNOperationType.PNRemovePushNotificationsFromChannelsOperation
}
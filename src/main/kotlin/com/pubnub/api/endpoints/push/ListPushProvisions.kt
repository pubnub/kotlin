package com.pubnub.api.endpoints.push

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import retrofit2.Call
import retrofit2.Response
import java.util.*

class ListPushProvisions(pubnub: PubNub) : Endpoint<List<String>, PNPushListProvisionsResult>(pubnub) {

    lateinit var pushType: PNPushType
    lateinit var deviceId: String
    var environment = PNPushEnvironment.DEVELOPMENT
    lateinit var topic: String

    override fun validateParams() {
        super.validateParams()
        if (!::pushType.isInitialized) {
            throw PubNubException(PubNubError.PUSH_TYPE_MISSING)
        }
        if (!::deviceId.isInitialized || deviceId.isBlank()) {
            throw PubNubException(PubNubError.DEVICE_ID_MISSING)
        }
        if (pushType == PNPushType.APNS2) {
            if (!::topic.isInitialized || topic.isBlank()) {
                throw PubNubException(PubNubError.PUSH_TOPIC_MISSING)
            }
        }
    }

    override fun doWork(queryParams: HashMap<String, String>): Call<List<String>> {
        if (pushType != PNPushType.APNS2) {
            queryParams["type"] = pushType.toParamString()

            return pubnub.retrofitManager.pushService
                .listChannelsForDevice(
                    subKey = pubnub.configuration.subscribeKey,
                    pushToken = deviceId,
                    options = queryParams
                )
        }

        queryParams["environment"] = environment.name.toLowerCase()
        queryParams["topic"] = topic

        return pubnub.retrofitManager.pushService
            .listChannelsForDeviceApns2(
                subKey = pubnub.configuration.subscribeKey,
                deviceApns2 = deviceId,
                options = queryParams
            )

    }

    override fun createResponse(input: Response<List<String>>): PNPushListProvisionsResult? {
        return PNPushListProvisionsResult(input.body()!!)
    }

    override fun operationType() = PNOperationType.PNPushNotificationEnabledChannelsOperation
}
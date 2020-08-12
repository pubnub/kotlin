package com.pubnub.api.endpoints

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNTimeResult
import retrofit2.Response

/**
 * @see [PubNub.time]
 */
class Time internal constructor(pubnub: PubNub) : Endpoint<List<Long>, PNTimeResult>(pubnub) {

    override fun getAffectedChannels() = emptyList<String>()

    override fun getAffectedChannelGroups() = emptyList<String>()

    override fun doWork(queryParams: HashMap<String, String>) =
        pubnub.retrofitManager.timeService.fetchTime(queryParams)

    override fun createResponse(input: Response<List<Long>>): PNTimeResult? {
        return PNTimeResult(input.body()!![0])
    }

    override fun operationType() = PNOperationType.PNTimeOperation

    override fun isAuthRequired() = false
    override fun isSubKeyRequired() = false
}

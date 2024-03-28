package com.pubnub.internal.endpoints

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubCore
import retrofit2.Response

/**
 * @see [PubNubCore.time]
 */
class TimeEndpoint(pubnub: PubNubCore, private val excludeFromRetry: Boolean = false) :
    EndpointCore<List<Long>, PNTimeResult>(pubnub),
    TimeInterface {
    override fun getAffectedChannels() = emptyList<String>()

    override fun getAffectedChannelGroups() = emptyList<String>()

    override fun doWork(queryParams: HashMap<String, String>) = pubnub.retrofitManager.timeService.fetchTime(queryParams)

    override fun createResponse(input: Response<List<Long>>): PNTimeResult {
        return PNTimeResult(input.body()!![0])
    }

    override fun operationType() = PNOperationType.PNTimeOperation

    override fun isAuthRequired() = false

    override fun isSubKeyRequired() = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.MESSAGE_PERSISTENCE

    // it is excluded here because retry in old subscribeLoop uses it to check connectivity
    override fun isEndpointRetryable(): Boolean = !excludeFromRetry
}

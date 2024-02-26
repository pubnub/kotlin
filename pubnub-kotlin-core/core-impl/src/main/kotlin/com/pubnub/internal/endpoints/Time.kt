package com.pubnub.internal.endpoints

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.Endpoint
import com.pubnub.internal.InternalPubNubClient
import retrofit2.Response

/**
 * @see [InternalPubNubClient.time]
 */
class Time(pubnub: InternalPubNubClient, private val excludeFromRetry: Boolean = false) :
    Endpoint<List<Long>, PNTimeResult>(pubnub),
    ITime {
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

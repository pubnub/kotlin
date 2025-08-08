package com.pubnub.internal.endpoints

import com.pubnub.api.endpoints.Time
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.logging.LogMessageType
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.ExtendedLogger
import com.pubnub.internal.logging.LoggerManager
import org.slf4j.event.Level
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNubImpl.time]
 */
class TimeEndpoint(pubnub: PubNubImpl, private val excludeFromRetry: Boolean = false) :
    EndpointCore<List<Long>, PNTimeResult>(pubnub),
    Time {
    private val log: ExtendedLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    override fun getAffectedChannels() = emptyList<String>()

    override fun getAffectedChannelGroups() = emptyList<String>()

    override fun doWork(queryParams: HashMap<String, String>): Call<List<Long>> {
        log.trace(
            LogMessage(
                pubNubId = pubnub.instanceId,
                logLevel = Level.TRACE,
                location = this::class.java.toString(),
                type = LogMessageType.OBJECT,
                message = LogMessageContent.Object(
                    message = mapOf(
                        "excludeFromRetry" to excludeFromRetry,
                        "queryParams" to queryParams
                    )
                ),
                details = "Time API call"
            )
        )

        return retrofitManager.timeService.fetchTime(queryParams)
    }

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

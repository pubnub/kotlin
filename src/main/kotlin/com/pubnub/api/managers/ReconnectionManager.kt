package com.pubnub.api.managers

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.ReconnectionCallback
import com.pubnub.api.endpoints.Time
import com.pubnub.api.retry.RetryConfiguration
import org.slf4j.LoggerFactory
import java.util.Calendar
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow

internal class ReconnectionManager(val pubnub: PubNub) {

    private val log = LoggerFactory.getLogger("ReconnectionManager")

    private companion object {
        private const val LINEAR_INTERVAL = 3
        private const val MIN_EXPONENTIAL_BACKOFF = 1
        private const val MAX_EXPONENTIAL_BACKOFF = 32

        private const val MILLISECONDS = 1000
        private const val MAX_RANDOM_DELAY_IN_MILLIS = 1000
        private const val NO_RETRIES = 0
    }

    internal lateinit var reconnectionCallback: ReconnectionCallback

    private var exponentialMultiplier = 1
    private var failedCalls = 0

    private lateinit var pnReconnectionPolicy: RetryConfiguration
    private var maxConnectionRetries = 6

    private var timer: Timer? = null
    private val random = Random()

    internal fun startPolling(pnConfiguration: PNConfiguration) {
        pnReconnectionPolicy = pnConfiguration.retryConfForOldSubscribeLoop

        if (isReconnectionPolicyUndefined())
            return

        maxConnectionRetries = getMaxConnectionRetries(pnReconnectionPolicy)
        exponentialMultiplier = 1
        failedCalls = 0

        registerRetryTimer()
    }

    private fun getMaxConnectionRetries(retryConfiguration: RetryConfiguration): Int {
        return when (retryConfiguration) {
            is RetryConfiguration.None -> {
                NO_RETRIES
            }
            is RetryConfiguration.Linear -> {
                retryConfiguration.maxRetryNumber
            }
            is RetryConfiguration.Exponential -> {
                retryConfiguration.maxRetryNumber
            }
        }
    }

    private fun registerRetryTimer() {
        // make sure only one timer is running at a time.
        stopRetryTimer()

        if (isReconnectionPolicyUndefined()) {
            return
        }

        if (failedCalls >= maxConnectionRetries) {
            reconnectionCallback.onMaxReconnectionExhaustion()
            return
        }
        timer = Timer("Reconnection Manager timer", true)
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    callTime()
                }
            },
            getBestIntervalInMillis().toLong()
        )
    }

    private fun getBestIntervalInMillis(): Int {
        var timerInterval = 0
        when (pnReconnectionPolicy) {
            is RetryConfiguration.Exponential -> {
                timerInterval = (2.0.pow(exponentialMultiplier.toDouble()) - 1).toInt()
                if (timerInterval > MAX_EXPONENTIAL_BACKOFF) {
                    timerInterval = MIN_EXPONENTIAL_BACKOFF
                    exponentialMultiplier = 1
                    log.info("timerInterval > MAXEXPONENTIALBACKOFF at: " + Calendar.getInstance().time.toString())
                } else if (timerInterval < 1) {
                    timerInterval = MIN_EXPONENTIAL_BACKOFF
                }
                timerInterval = (timerInterval * MILLISECONDS) + getRandomDelayInMilliSeconds()
                log.info("timerInterval = " + timerInterval + "ms at: " + Calendar.getInstance().time.toString())
            }
            is RetryConfiguration.Linear -> {
                timerInterval = (LINEAR_INTERVAL * MILLISECONDS) + getRandomDelayInMilliSeconds()
            }
            is RetryConfiguration.None -> {
                timerInterval = (LINEAR_INTERVAL * MILLISECONDS) + getRandomDelayInMilliSeconds()
            }
        }
        return timerInterval
    }

    private fun getRandomDelayInMilliSeconds(): Int {
        return random.nextInt(MAX_RANDOM_DELAY_IN_MILLIS)
    }

    private fun stopRetryTimer() {
        timer?.cancel()
    }

    private fun callTime() {
        // Time endpoint by default is retryable, here we don't want it to retry
        Time(pubnub = pubnub, excludeFromRetry = true)
            .async { result ->
                if (!result.isFailure) {
                    stopRetryTimer()
                    reconnectionCallback.onReconnection()
                } else {
                    log.info("callTime at ${System.currentTimeMillis()}")
                    exponentialMultiplier++
                    failedCalls++
                    registerRetryTimer()
                }
            }
    }

    private fun isReconnectionPolicyUndefined(): Boolean {
        if (pnReconnectionPolicy == RetryConfiguration.None) {
            log.info("reconnection policy is disabled, please handle reconnection manually.")
            return true
        }
        return false
    }
}

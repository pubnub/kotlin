package com.pubnub.api.policies

/**
 * This sealed class represents the various retry policies for a request.
 */
sealed class RequestRetryPolicy {

    /**
     * None represents no retry policy in a network request
     */
    object None: RequestRetryPolicy()

    /**
     * This data class represents a linear retry policy for network requests with a delay between retries,
     * a maximum number of retries and a list of operations to exclude from retries.
     *
     * @property delayInSec The delay in seconds between retries. Minimum value is 3 seconds.
     * @property maxRetryNumber The maximum number of retries allowed. Maximum value is 10.
     * @property excludedOperations Operations (Endpoints) to be excluded from retry.
     */
    data class Linear(
        var delayInSec: Int, // todo add random seconds between 1.0 and 3.0(with floating point)
        var maxRetryNumber: Int,
        val excludedOperations: List<String>? = null  //todo String --> Endpoint
    ) : RequestRetryPolicy() {
        init {
            if(delayInSec < 3){
                delayInSec = 3
            }
            if(maxRetryNumber > 10){
                maxRetryNumber = 10
            }
        }
    }

    /**
     * This class represents an exponential retry policy with a minimum and
     * maximum delay between retries, a maximum number of retries, and a list of
     * operations to exclude from retry attempts.
     *
     * @property minDelayInSec The minimum delay in seconds between retries. Minimum value is 3 seconds.
     * @property maxDelayInSec The maximum delay in seconds between retries.
     * @property maxRetry The maximum number of retries allowed. Maximum value is 10.
     * @property excludedOperations Operations (Endpoints) to exclude from retry.
     */
    class Exponential(
        var minDelayInSec: Int, // todo add random seconds between 1.0 and 3.0(with floating point)
        val maxDelayInSec: Int,
        var maxRetryNumber: Int,
        val excludedOperations: List<String>? = null //todo String --> Endpoint
    ): RequestRetryPolicy(){
        init {
            if(minDelayInSec < 3){
                minDelayInSec = 3
            }
            if(maxRetryNumber > 10){
                maxRetryNumber = 10
            }
        }
    }
}
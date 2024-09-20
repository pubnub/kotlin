package com.pubnub.api.endpoints.remoteaction

import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.v2.callbacks.Consumer
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture

interface ExtendedRemoteAction<Output> : RemoteAction<Output> {
    /**
     * Return the type of this operation from the values defined in [PNOperationType].
     */
    fun operationType(): PNOperationType
}

interface RemoteAction<Output> : PNFuture<Output>, Cancelable {
    /**
     * Run the action synchronously, potentially blocking the calling thread.
     * @return returns the result of the action
     * @throws PubNubException in case of an error
     */
    @Throws(PubNubException::class)
    fun sync(): Output

    /**
     * Run the action asynchronously, without blocking the calling thread and delivering the result through
     * the [callback].
     *
     * The delivered result can be either a success (including a result value if any) or
     * a failure (including a [PubNubException]).
     *
     * The recommended pattern to use is:
     * ```kotlin
     * action.async { result ->
     *     result.onSuccess { value ->
     *         // do something with value
     *     }.onFailure { exception ->
     *         // do something with exception
     *     }
     * }
     * ```
     */
    override fun async(callback: Consumer<Result<Output>>)

    /**
     * Attempt to retry the action and deliver the result to a callback registered with a previous call to [async].
     */
    fun retry()
}

interface Cancelable {
    /**
     * Cancel the action without reporting any further results.
     */
    fun silentCancel()
}

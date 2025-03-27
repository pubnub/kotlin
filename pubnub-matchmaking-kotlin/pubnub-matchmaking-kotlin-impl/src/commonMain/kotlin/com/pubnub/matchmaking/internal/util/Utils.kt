package com.pubnub.matchmaking.internal.util

import co.touchlab.kermit.Logger
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.v2.callbacks.Result
import com.pubnub.kmp.PNFuture
import com.pubnub.kmp.catch

internal const val HTTP_ERROR_404 = 404
internal expect fun urlDecode(encoded: String): String

internal val PNFetchMessagesResult.channelsUrlDecoded: Map<String, List<PNFetchMessageItem>>
    get() = channels.mapKeys {
        urlDecode(
            it.key
        )
    }

inline fun PubNubException.logErrorAndReturnException(log: Logger): PubNubException = this.apply {
    log.e(throwable = this) { this.message.orEmpty() }
}

inline fun PubNubException.logWarnAndReturnException(log: Logger): PubNubException = this.apply {
    log.w(throwable = this) { this.message.orEmpty() }
}

inline fun Logger.pnError(message: String): Nothing = throw PubNubException(message).logErrorAndReturnException(this)

inline fun Logger.logErrorAndReturnException(message: String): PubNubException {
    return PubNubException(message).logErrorAndReturnException(this)
}

internal fun <T> PNFuture<T>.nullOn404() = catch {
    if (it is PubNubException && it.statusCode == HTTP_ERROR_404) {
        Result.success(null)
    } else {
        Result.failure(it)
    }
}
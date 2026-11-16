package com.pubnub.internal.managers

import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

/**
 * OkHttp **network** interceptor installed on the subscribe client only. It remembers the connection used by the
 * latest subscribe call (handshake or receive), so [closeIfHttp2] can close it when the app calls `reconnect()`.
 *
 * Why: on HTTP/2, cancelling a call only resets its stream. A silently dead connection stays in the pool and looks
 * healthy to OkHttp, so the next subscribe call would be multiplexed onto it and hang until `subscribeTimeout`.
 * Closing the socket makes OkHttp treat the connection as unhealthy, so the next call opens a new one.
 *
 * On HTTP/1.1 cancelling a call already closes its socket, so nothing is done there.
 */
internal class SubscribeConnectionTracker : Interceptor {
    private val lastConnection = AtomicReference<Connection?>(null)

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.connection()?.let { lastConnection.set(it) }
        return chain.proceed(chain.request())
    }

    /**
     * Forgets the tracked connection and, if it uses HTTP/2, closes its socket. No-op when nothing is tracked.
     */
    fun closeIfHttp2() {
        val connection = lastConnection.getAndSet(null) ?: return
        if (connection.protocol() != Protocol.HTTP_2) {
            return
        }
        try {
            connection.socket().close()
        } catch (ignored: IOException) {
            // The connection is being dropped anyway; a failure to close it cleanly changes nothing.
        }
    }
}

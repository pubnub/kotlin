package com.pubnub.internal.managers

import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

/**
 * OkHttp **network** interceptor installed on the subscribe client only. It remembers the connection used by the
 * latest subscribe call (handshake or receive), so [closeIfHttp2] can close it when the app calls `reconnect()`.
 *
 * Why: on HTTP/2, cancelling a call only resets its stream. A silently dead connection stays in the pool and looks
 * healthy to OkHttp, so the next subscribe call would be multiplexed onto it and hang until `subscribeTimeout`.
 * Closing the socket makes OkHttp treat the connection as unhealthy, so the next call opens a new one.
 *
 * On HTTP/1.1 cancelling a call already closes its socket, so nothing is done there.
 *
 * @param closeTimeoutMillis how long [closeIfHttp2] waits for the socket to close. See [closeIfHttp2].
 */
internal class SubscribeConnectionTracker(
    private val closeTimeoutMillis: Long = DEFAULT_CLOSE_TIMEOUT_MILLIS,
) : Interceptor {
    private val lastConnection = AtomicReference<Connection?>(null)

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.connection()?.let { lastConnection.set(it) }
        return chain.proceed(chain.request())
    }

    /**
     * Forgets the tracked connection and, if it uses HTTP/2, closes its socket. No-op when nothing is tracked.
     *
     * The socket is a TLS socket, and closing it first sends a TLS close message. On a dead path with a full send
     * buffer that write can block until TCP gives up (minutes). This runs on the subscribe effect thread, and a block
     * there would stall every later subscribe step, including the next `reconnect()`. So the close runs on its own
     * daemon thread and this waits at most [closeTimeoutMillis]. Normally the close finishes at once; if it hangs, the
     * subscribe loop goes on and the close thread finishes or fails on its own later.
     */
    fun closeIfHttp2() {
        val connection = lastConnection.getAndSet(null) ?: return
        if (connection.protocol() != Protocol.HTTP_2) {
            return
        }
        val socket = connection.socket()
        val closed = CountDownLatch(1)
        thread(isDaemon = true, name = CLOSE_THREAD_NAME) {
            try {
                socket.close()
            } catch (ignored: IOException) {
                // The connection is being dropped anyway; a failure to close it cleanly changes nothing.
            } finally {
                closed.countDown()
            }
        }
        try {
            closed.await(closeTimeoutMillis, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    private companion object {
        const val DEFAULT_CLOSE_TIMEOUT_MILLIS = 1_000L
        const val CLOSE_THREAD_NAME = "pubnub-subscribe-connection-close"
    }
}

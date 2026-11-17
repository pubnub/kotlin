package com.pubnub.internal.managers

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.io.IOException
import java.net.Socket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SubscribeConnectionTrackerTest {
    private val tracker = SubscribeConnectionTracker()

    private fun connection(protocol: Protocol): Pair<Connection, Socket> {
        val socket = mockk<Socket>(relaxed = true)
        val connection = mockk<Connection>()
        every { connection.protocol() } returns protocol
        every { connection.socket() } returns socket
        return connection to socket
    }

    private fun interceptWith(connection: Connection?): Response {
        val request = Request.Builder().url("https://ps.pndsn.com/v2/subscribe/sub/ch/0").build()
        val response = mockk<Response>()
        val chain = mockk<Interceptor.Chain>()
        every { chain.connection() } returns connection
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        return tracker.intercept(chain).also { assertSame(response, it) }
    }

    @Test
    fun `closes the socket of the tracked HTTP2 connection`() {
        val (connection, socket) = connection(Protocol.HTTP_2)
        interceptWith(connection)

        tracker.closeIfHttp2()

        verify(exactly = 1) { socket.close() }
    }

    @Test
    fun `does not close the socket of the tracked HTTP1_1 connection`() {
        val (connection, socket) = connection(Protocol.HTTP_1_1)
        interceptWith(connection)

        tracker.closeIfHttp2()

        verify(exactly = 0) { socket.close() }
    }

    @Test
    fun `is a no-op when nothing is tracked`() {
        interceptWith(null)

        tracker.closeIfHttp2() // must not throw
    }

    @Test
    fun `clears the tracked connection after closing`() {
        val (connection, socket) = connection(Protocol.HTTP_2)
        interceptWith(connection)

        tracker.closeIfHttp2()
        tracker.closeIfHttp2()

        verify(exactly = 1) { socket.close() }
    }

    @Test
    fun `tracks only the latest connection`() {
        val (first, firstSocket) = connection(Protocol.HTTP_2)
        val (second, secondSocket) = connection(Protocol.HTTP_2)
        interceptWith(first)
        interceptWith(second)

        tracker.closeIfHttp2()

        verify(exactly = 0) { firstSocket.close() }
        verify(exactly = 1) { secondSocket.close() }
    }

    @Test
    @Timeout(10, unit = TimeUnit.SECONDS)
    fun `returns after the timeout when closing the socket blocks`() {
        // given: a TLS close that hangs, as it can on a dead path with a full send buffer.
        // A real Socket subclass, not a mockk answer: blocking inside a mocked call can deadlock mockk's verify.
        val blockingTracker = SubscribeConnectionTracker(closeTimeoutMillis = 200)
        val closeStarted = CountDownLatch(1)
        val release = CountDownLatch(1)
        val socket =
            object : Socket() {
                override fun close() {
                    closeStarted.countDown()
                    release.await()
                }
            }
        val connection = mockk<Connection>()
        every { connection.protocol() } returns Protocol.HTTP_2
        every { connection.socket() } returns socket
        val chain = mockk<Interceptor.Chain>()
        val request = Request.Builder().url("https://ps.pndsn.com/v2/subscribe/sub/ch/0").build()
        every { chain.connection() } returns connection
        every { chain.request() } returns request
        every { chain.proceed(request) } returns mockk()
        blockingTracker.intercept(chain)

        try {
            // when
            val startNanos = System.nanoTime()
            blockingTracker.closeIfHttp2()
            val elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos)

            // then: the caller (the subscribe effect thread) is released after the timeout, not when close returns
            assertTrue(elapsedMillis >= 150, "returned before the timeout: ${elapsedMillis}ms")
            assertTrue(elapsedMillis < 2_000, "blocked on the hanging close: ${elapsedMillis}ms")
            assertTrue(closeStarted.await(1, TimeUnit.SECONDS), "socket close was never attempted")
        } finally {
            release.countDown()
        }
    }

    @Test
    fun `swallows IOException thrown while closing`() {
        val (connection, socket) = connection(Protocol.HTTP_2)
        every { socket.close() } throws IOException("already broken")
        interceptWith(connection)

        tracker.closeIfHttp2() // must not throw
    }
}

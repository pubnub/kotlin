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
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.Socket

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
    fun `swallows IOException thrown while closing`() {
        val (connection, socket) = connection(Protocol.HTTP_2)
        every { socket.close() } throws IOException("already broken")
        interceptWith(connection)

        tracker.closeIfHttp2() // must not throw
    }
}

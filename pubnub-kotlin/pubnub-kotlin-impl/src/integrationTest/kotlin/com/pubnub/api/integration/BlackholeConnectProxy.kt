package com.pubnub.api.integration

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

/**
 * Minimal HTTP CONNECT proxy on loopback. [Tunnel.blackhole] turns a tunnel into a silently dead path: nothing is
 * forwarded in either direction, the client's bytes are still read (so its writes succeed) and counted, and a close
 * by the server is not passed on. Only a close by the client ends a blackholed tunnel.
 *
 * TLS and ALPN stay end-to-end, so the client negotiates the origin's real protocol (HTTP/1.1 or HTTP/2).
 */
internal class BlackholeConnectProxy : AutoCloseable {
    private val serverSocket = ServerSocket(0, BACKLOG, InetAddress.getLoopbackAddress())
    private val tunnels = CopyOnWriteArrayList<Tunnel>()

    val port: Int get() = serverSocket.localPort

    init {
        thread(isDaemon = true, name = "blackhole-proxy-accept") { acceptLoop() }
    }

    fun tunnelCount(): Int = tunnels.size

    fun openTunnels(): List<Tunnel> = tunnels.filterNot { it.isClosed }

    override fun close() {
        serverSocket.closeQuietly()
        tunnels.forEach { it.close() }
    }

    private fun acceptLoop() {
        while (!serverSocket.isClosed) {
            val client =
                try {
                    serverSocket.accept()
                } catch (e: IOException) {
                    return
                }
            thread(isDaemon = true, name = "blackhole-proxy-tunnel") { serve(client) }
        }
    }

    private fun serve(client: Socket) {
        try {
            // "CONNECT host:port HTTP/1.1"
            val target = readRequestHead(client.getInputStream()).split(' ').getOrNull(1)
            if (target == null) {
                client.closeQuietly()
                return
            }
            val upstream = Socket(target.substringBeforeLast(':'), target.substringAfterLast(':').toInt())
            val tunnel = Tunnel(client, upstream)
            tunnels.add(tunnel)
            client.getOutputStream().apply {
                write("HTTP/1.1 200 Connection Established\r\n\r\n".toByteArray())
                flush()
            }
            thread(isDaemon = true, name = "blackhole-proxy-pump") { tunnel.pumpFromClient() }
            tunnel.pumpFromUpstream()
        } catch (e: IOException) {
            client.closeQuietly()
        }
    }

    /** Reads byte by byte up to the blank line, so no tunnel bytes (TLS ClientHello) are consumed. */
    private fun readRequestHead(input: InputStream): String {
        val head = ByteArrayOutputStream()
        var matched = 0
        while (matched < HEAD_END.size) {
            val b = input.read()
            if (b < 0) {
                break
            }
            head.write(b)
            matched =
                when (b.toByte()) {
                    HEAD_END[matched] -> matched + 1
                    HEAD_END[0] -> 1
                    else -> 0
                }
        }
        return head.toString(Charsets.US_ASCII.name())
    }

    private companion object {
        const val BACKLOG = 50
        val HEAD_END = "\r\n\r\n".toByteArray()
    }
}

internal class Tunnel(
    private val client: Socket,
    private val upstream: Socket,
) {
    private val blackholed = AtomicBoolean(false)
    val bytesSwallowedFromClient = AtomicLong()

    @Volatile
    var isClosed = false
        private set

    fun blackhole() = blackholed.set(true)

    fun pumpFromClient() = pump(client.getInputStream(), upstream.getOutputStream(), fromClient = true)

    fun pumpFromUpstream() = pump(upstream.getInputStream(), client.getOutputStream(), fromClient = false)

    fun close() {
        isClosed = true
        client.closeQuietly()
        upstream.closeQuietly()
    }

    private fun pump(
        from: InputStream,
        to: OutputStream,
        fromClient: Boolean,
    ) {
        val buffer = ByteArray(BUFFER_SIZE)
        try {
            while (true) {
                val read = from.read(buffer)
                if (read < 0) {
                    break
                }
                if (blackholed.get()) {
                    if (fromClient) {
                        bytesSwallowedFromClient.addAndGet(read.toLong())
                    }
                    continue
                }
                to.write(buffer, 0, read)
                to.flush()
            }
        } catch (e: IOException) {
            // socket closed by the other pump or by close()
        }
        // a dead path never tells the client that the server went away
        if (fromClient || !blackholed.get()) {
            close()
        }
    }

    private companion object {
        const val BUFFER_SIZE = 16 * 1024
    }
}

private fun Socket.closeQuietly() {
    try {
        close()
    } catch (e: IOException) {
        // ignore
    }
}

private fun ServerSocket.closeQuietly() {
    try {
        close()
    } catch (e: IOException) {
        // ignore
    }
}

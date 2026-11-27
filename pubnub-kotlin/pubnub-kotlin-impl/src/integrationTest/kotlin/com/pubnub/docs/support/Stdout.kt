package com.pubnub.docs.support

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream

/**
 * Captures what a sample prints with `println` or `System.out.println`, so a test can prove that a listener of the sample
 * really ran. Start it before the region, stop it after the assertions.
 *
 * The captured text ([text], [lines], [printedToken]) is the raw output, the test needs the real values. The copy that
 * still reaches the original stream (the Gradle console, so the CI log) is passed through [Redact.scrub] one line at a
 * time, so a grant token that a sample prints is masked there even before it is registered. A line is written when its
 * newline arrives, and the rest when the capture closes.
 *
 * [capture] swaps the JVM wide `System.out`, so only one capture may be open at a time (the live tier runs one test at a
 * time). Register [close] with `Cleanup.cleanupAfterTest` right after starting, so a failing test cannot leave the stream
 * swapped.
 */
class Stdout private constructor(private val original: PrintStream) : AutoCloseable {
    private val buffer = ByteArrayOutputStream()
    private val pendingLine = ByteArrayOutputStream()
    private val lock = Any()
    private var closed = false

    /** Writes the pending line to the original stream, redacted. Called with [lock] held. */
    private fun emitPendingLine() {
        if (pendingLine.size() == 0) {
            return
        }
        val redacted = Redact.scrub(pendingLine.toString("UTF-8")).toByteArray(Charsets.UTF_8)
        pendingLine.reset()
        original.write(redacted, 0, redacted.size)
    }

    private val tee =
        object : OutputStream() {
            override fun write(b: Int) {
                synchronized(lock) {
                    buffer.write(b)
                    pendingLine.write(b)
                    if (b == '\n'.code) {
                        emitPendingLine()
                    }
                }
            }

            override fun write(
                b: ByteArray,
                off: Int,
                len: Int,
            ) {
                for (i in off until off + len) {
                    write(b[i].toInt())
                }
            }

            override fun flush() {
                synchronized(lock) { original.flush() }
            }
        }

    init {
        System.setOut(PrintStream(tee, true, "UTF-8"))
    }

    /** Everything printed since the capture started, raw (not redacted). */
    fun text(): String = synchronized(lock) { buffer.toString("UTF-8") }

    /** The printed lines that contain [part]. */
    fun lines(part: String): List<String> = text().lines().filter { it.contains(part) }

    /** Fails with the captured text (redacted) unless a line with [part] was printed. */
    fun assertPrinted(part: String) {
        if (lines(part).isEmpty()) {
            throw AssertionError(Redact.scrub("nothing printed that contains '$part'. Printed so far: ${text().take(1500)}"))
        }
    }

    override fun close() {
        synchronized(lock) {
            if (!closed) {
                closed = true
                emitPendingLine()
                System.out.flush()
                System.setOut(original)
            }
        }
    }

    companion object {
        /** Starts a capture. */
        @JvmStatic
        fun capture(): Stdout = Stdout(System.out)

        /** Starts a capture and registers its end with the cleanups of the test, so no test leaves the stream swapped. */
        @JvmStatic
        fun captureForTest(): Stdout =
            capture().also { stream -> Cleanup.cleanupAfterTest("restore System.out") { stream.close() } }
    }
}

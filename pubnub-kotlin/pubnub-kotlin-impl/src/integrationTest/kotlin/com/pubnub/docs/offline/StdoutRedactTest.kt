package com.pubnub.docs.offline

import com.pubnub.docs.support.Stdout
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

/**
 * The console copy of a captured sample is redacted, the captured text is not. A sample that prints a grant token must
 * not put it in the Gradle output (CI logs), while the test still reads the raw token back with `printedToken`.
 */
class StdoutRedactTest {
    // A well formed Access Manager v3 token (the shape the SDK parser tests use). Not a live credential.
    private val token =
        "qEF2AkF0GmFLd-NDdHRsGQWgQ3Jlc6VEY2hhbqFjY2gxGP9DZ3JwoWNjZzEY_0N1c3KgQ3NwY6BEdXVpZKFldXVpZDEY_0NwYXSlRGNoYW6gQ2dycKBDdXNyoENzcGOgRHV1aWShYl4kAURtZXRho2VzY29yZRhkZWNvbG9yY3JlZGZhdXRob3JlcGFuZHVEdXVpZGtteWF1dGh1dWlkMUNzaWdYIP2vlxHik0EPZwtgYxAW3-LsBaX_WgWdYvtAXpYbKll3"

    /** Runs [block] with System.out pointed at a buffer and returns what reached that buffer. */
    private fun consoleOf(block: () -> Unit): String {
        val savedOut = System.out
        val console = ByteArrayOutputStream()
        System.setOut(PrintStream(console, true, "UTF-8"))
        try {
            block()
        } finally {
            System.setOut(savedOut)
        }
        return console.toString("UTF-8")
    }

    @Test
    fun printedTokenIsMaskedOnTheConsoleButReadBackRaw() {
        var captured = ""
        val console =
            consoleOf {
                val stdout = Stdout.capture()
                println("Token: $token")
                captured = stdout.text()
                stdout.close()
            }
        assertTrue(captured.contains(token), "the captured text must keep the raw token for printedToken")
        assertFalse(console.contains(token), "the console must not show the token")
        assertTrue(console.contains("Token: ***"), "the console must show the masked line, got: $console")
    }

    @Test
    fun tokenSplitAcrossPrintCallsIsMaskedOnTheConsole() {
        val console =
            consoleOf {
                val stdout = Stdout.capture()
                print("Token: ")
                print(token.substring(0, 60))
                print(token.substring(60))
                println()
                stdout.close()
            }
        assertFalse(console.contains(token), "a token split over several writes must still be masked")
        assertTrue(console.contains("Token: ***"), "got: $console")
    }

    @Test
    fun unterminatedLineIsFlushedRedactedOnClose() {
        val console =
            consoleOf {
                val stdout = Stdout.capture()
                print("Token: $token")
                stdout.close()
            }
        assertFalse(console.contains(token), "the last line without a newline must be masked when the capture closes")
        assertTrue(console.contains("Token: ***"), "got: $console")
    }
}

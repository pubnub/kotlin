package com.pubnub.docs.offline

import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Redact
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.reflect.Proxy

/**
 * Gradle keeps its short exception format, so the failure message of a live test reaches the CI log only through
 * LiveExtension.testFailed. That line must never hold a key, a token, or an auth or signature query value. Needs no keys
 * and no network: the values below are made-up shapes.
 */
class FailureOutputRedactTest {
    private val registered = "a-registered-test-secret-value"
    private val keyShape = "sub-c-" + "0123456789abcdef0123456789abcdef"
    private val tokenShape = "qEF2" + "A".repeat(60)

    /** An ExtensionContext that answers only getDisplayName, the one call testFailed makes. */
    private val context: ExtensionContext =
        Proxy.newProxyInstance(ExtensionContext::class.java.classLoader, arrayOf(ExtensionContext::class.java)) { _, method, _ ->
            if (method.name == "getDisplayName") {
                "someTest()"
            } else {
                throw UnsupportedOperationException(method.name)
            }
        } as ExtensionContext

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
    fun aFailureMessageIsPrintedRedactedWithItsCauses() {
        Redact.register(registered)
        val cause = IllegalStateException("server said: $tokenShape at https://ps.pndsn.com/x?auth=$registered&signature=abc123")
        val failure = AssertionError("expected $registered and $keyShape to match", cause)

        val printed = consoleOf { LiveExtension().testFailed(context, failure) }

        assertTrue(printed.contains("live failure: someTest() failed java.lang.AssertionError: expected"), "the failure line is missing")
        assertTrue(printed.contains("caused by java.lang.IllegalStateException: server said:"), "the cause line is missing")
        assertFalse(printed.contains(registered), "a registered secret reached the failure output")
        assertFalse(printed.contains(keyShape), "a key shaped value reached the failure output")
        assertFalse(printed.contains(tokenShape), "a token shaped value reached the failure output")
        assertFalse(printed.contains("signature=abc123"), "a signature query value reached the failure output")
        assertTrue(printed.contains(Redact.MASK), "nothing was masked")
    }

    @Test
    fun aVeryLongFailureMessageIsCapped() {
        val printed = consoleOf { LiveExtension().testFailed(context, AssertionError("x".repeat(10_000))) }
        assertTrue(printed.length < 2_500, "the failure output is not capped")
    }
}

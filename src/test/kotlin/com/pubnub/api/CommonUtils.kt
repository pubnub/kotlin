package com.pubnub.api

import com.github.tomakehurst.wiremock.client.WireMock
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pubnub.api.models.consumer.PNStatus
import org.awaitility.Awaitility
import org.awaitility.Durations
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.stream.Collectors

object CommonUtils {

    var DEFAULT_LISTEN_DURATION = 5

    internal fun observe(success: AtomicBoolean, seconds: Int) {
        Awaitility.await()
            .atMost(seconds.toLong(), TimeUnit.SECONDS)
            .untilTrue(success)
    }

    fun assertPnException(expectedPubNubError: PubNubError, pnStatus: PNStatus) {
        assertTrue(pnStatus.error)
        assertEquals(expectedPubNubError, pnStatus.exception!!.pubnubError)
    }

    fun assertPnException(expectedPubNubError: PubNubError, exception: Exception) {
        exception as PubNubException
        assertEquals(expectedPubNubError, exception.pubnubError)
    }

    fun emptyJson() = WireMock.aResponse().withBody("{}")

    fun failTest(message: String? = null) {
        fail(message)
    }

    fun getSpecialCharsMap(): List<SpecialChar> {
        val resourceFileAsString = getResourceFileAsString("special_chars.json")
        return Gson().fromJson(
            resourceFileAsString,
            object : TypeToken<List<SpecialChar>>() {}.type
        )
    }

    fun getResourceFileAsString(fileName: String?): String? {
        val classLoader = ClassLoader.getSystemClassLoader()
        classLoader.getResourceAsStream(fileName).use { inputStream ->
            if (inputStream == null) return null
            InputStreamReader(inputStream).use { isr ->
                BufferedReader(isr).use { reader ->
                    return reader.lines().collect(Collectors.joining(System.lineSeparator()))
                }
            }
        }
    }

    class SpecialChar(
        val name: String,
        val regular: String,
        val encoded: String
    )

    fun retry(function: () -> Unit) {
        val success = AtomicBoolean()
        val block = {
            try {
                val executor = Executors.newSingleThreadExecutor()
                val submit = executor.submit(
                    Callable {
                        try {
                            function.invoke()
                            true
                        } catch (t: Throwable) {
                            false
                        }
                    }
                )
                success.set(submit.get())
            } catch (t: Throwable) {
                success.set(false)
            }
        }

        Awaitility.await()
            .atMost(DEFAULT_LISTEN_DURATION.toLong(), TimeUnit.SECONDS)
            .pollInterval(Durations.FIVE_HUNDRED_MILLISECONDS)
            .until {
                block.invoke()
                success.get()
            }
    }
}

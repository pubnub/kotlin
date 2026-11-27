@file:Suppress("ktlint")

package com.pubnub.docs.logging

import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.logging.HttpMethod
import com.pubnub.api.logging.LogContentConfig
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.docs.logging.samples.AnalyticsService
import com.pubnub.docs.logging.samples.MonitoringLogger
import com.pubnub.docs.logging.samples.MonitoringService
import com.pubnub.docs.support.Cleanup
import com.pubnub.docs.support.Env
import com.pubnub.docs.support.GettingStartedKit
import com.pubnub.docs.support.GettingStartedKit.LOGGING_RESOURCES
import com.pubnub.docs.support.Kit.armMessage
import com.pubnub.docs.support.Kit.probe
import com.pubnub.docs.support.Kit.storedItem
import com.pubnub.docs.support.Kit.subscribeAndConnect
import com.pubnub.docs.support.LiveClients
import com.pubnub.docs.support.LiveExtension
import com.pubnub.docs.support.Redact
import com.pubnub.docs.support.Retry
import com.pubnub.docs.support.Wait
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Properties

/**
 * Live tests of the Kotlin docs page logging (docs/sdks/kotlin/logging.md) and of the Android logging partial. One test per
 * snippet. The text between a start marker and `snippet.end` is the published sample, the arrangement is above it and the
 * assertions below it.
 *
 * The whole-file embeds of the page (logback.xml, log4j2.xml, simplelogger.properties and the Android logback.xml) are
 * resource files in `src/integrationTest/resources/com/pubnub/docs/logging/`, outside the classpath root so that they do not
 * change the logging of any other test. The logback.xml is loaded explicitly for one test. The other backends are not on
 * the classpath of the tests (one SLF4J binding only), so their files are checked for what the docs say about them.
 */
@ExtendWith(LiveExtension::class)
class LoggingDocsTest {
    private val OPEN = "open"

    private fun message(text: String) = LogMessage(message = LogMessageContent.Text(text))

    // ---------------------------------------------------------------------------------------------------------------
    // Custom logger
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun customLoggerImplementation() {
        MonitoringService.clear()
        AnalyticsService.clear()
        val logger = MonitoringLogger()

        assertEquals("MonitoringLogger", logger.name)
        // an error with its structured data goes to the monitoring service
        logger.error(LogMessage(message = LogMessageContent.Error(type = "PubNubException", message = "boom", stack = listOf("frame"))))
        assertEquals(listOf("boom"), MonitoringService.errors.toList())
        // a failed network request is reported with method and URL, a successful one is not
        logger.error(
            LogMessage(
                message = LogMessageContent.NetworkRequest(
                    origin = "https://ps.pndsn.com",
                    path = "/time/0",
                    method = HttpMethod.GET,
                    failed = true,
                ),
            ),
        )
        logger.error(
            LogMessage(
                message = LogMessageContent.NetworkRequest(origin = "https://ps.pndsn.com", path = "/time/1", method = HttpMethod.GET),
            ),
        )
        assertEquals(listOf("get https://ps.pndsn.com/time/0"), MonitoringService.networkFailures.toList())
        // other content types of an error are ignored
        logger.error(message("plain text"))
        assertEquals(1, MonitoringService.errors.size)
        // string warnings go to the monitoring service, null does not
        logger.warn("deprecated call")
        logger.warn(null as String?)
        assertEquals(listOf("deprecated call"), MonitoringService.warnings.toList())
        // an object log of an operation with "Configuration" in its name goes to the analytics service
        logger.debug(LogMessage(message = LogMessageContent.Object(arguments = mapOf("a" to 1), operation = "UpdateConfiguration"), pubNubId = "instance-1"))
        logger.debug(LogMessage(message = LogMessageContent.Object(arguments = mapOf("a" to 1), operation = "TimeEndpoint"), pubNubId = "instance-2"))
        logger.debug(message("text"))
        assertEquals(listOf("instance-1"), AnalyticsService.configChanges.toList())
    }

    @Test
    fun customLoggerConfiguration(clients: LiveClients) {
        MonitoringService.clear()
        AnalyticsService.clear()

        // snippet.customLoggerConfiguration

        // Configure SDK with custom logger
        val config = PNConfiguration.builder(UserId("abc"), "subscribeKey") {
            customLoggers = listOf(MonitoringLogger())
        }.build()

        val pubnub = PubNub.create(config)

        // snippet.end
        clients.track(pubnub)

        // the SDK keeps the logger of the sample in its configuration
        val loggers = pubnub.configuration.customLoggers
        assertEquals(1, loggers?.size)
        assertTrue(loggers!![0] is MonitoringLogger)
        assertEquals("MonitoringLogger", loggers[0].name)
        // a real failure of the SDK reaches the monitoring service: the service does not know the subscribe key of the sample
        val failure = org.junit.jupiter.api.Assertions.assertThrows(PubNubException::class.java) { pubnub.hereNow(channels = listOf("myChannel")).sync() }
        Wait.eventually("the monitoring service heard of the failed call") {
            assertTrue(MonitoringService.errors.isNotEmpty() || MonitoringService.networkFailures.isNotEmpty() || MonitoringService.warnings.isNotEmpty(), "nothing reached the monitoring service")
        }
        println("monitoring: errors=${MonitoringService.errors.size} warnings=${MonitoringService.warnings.size} failureStatus=${failure.statusCode}")
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Log content configuration
    // ---------------------------------------------------------------------------------------------------------------

    private fun publishApiCall(log: GettingStartedKit.LogCapture, marker: String): String =
        log.containing("Publish API call").lastOrNull { it.contains(marker.take(8)) }
            ?: throw AssertionError("no 'Publish API call' log entry for the published message. Logged: ${log.messages().size} entries")

    @Test
    fun logContentConfigCustom(clients: LiveClients) {
        val log = GettingStartedKit.captureDebug()

        // snippet.logContentConfigCustom
        val config = PNConfiguration.builder(UserId("demoUserId"), "demo") {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
            logContentConfig = LogContentConfig(
                loggedMessageContentMaxBytes = 1000,
                loggedHttpResponseMaxBytes = 4000,
            )
        }.build()

        val pubnub = PubNub.create(config)
        // snippet.end
        clients.track(pubnub)
        Cleanup.record("history", "docs-log-content")

        assertEquals(1000, config.logContentConfig.loggedMessageContentMaxBytes)
        assertEquals(4000, config.logContentConfig.loggedHttpResponseMaxBytes)
        // a message of 1500 bytes: the log entry of the publish shows the first 1000 bytes only
        val body = "k".repeat(1500)
        pubnub.publish("docs-log-content", body).sync()
        val entry = publishApiCall(log, "kkkkkkkk")
        val shown = entry.count { it == 'k' }
        assertTrue(shown in 990..1000, "the logged message content is not capped near 1000 bytes ($shown)")
        assertTrue(entry.contains("\"pn_totalBytes\": 1502"), "the entry does not name the total size of the message")
        assertTrue(entry.contains("pn_mfp"), "the entry has no message fingerprint")
    }

    @Test
    fun logContentConfigSuppressMessages(clients: LiveClients) {
        val log = GettingStartedKit.captureDebug()

        // snippet.logContentConfigSuppressMessages
        val configSuppressMessages = PNConfiguration.builder(UserId("demoUserId"), "demo") {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
            logContentConfig = LogContentConfig(
                loggedMessageContentMaxBytes = 0,
            )
        }.build()

        val pubnubSuppressMessages = PubNub.create(configSuppressMessages)
        // snippet.end
        clients.track(pubnubSuppressMessages)
        Cleanup.record("history", "docs-log-content")

        assertEquals(0, configSuppressMessages.logContentConfig.loggedMessageContentMaxBytes)
        val secret = probe("hide-this-text")
        pubnubSuppressMessages.publish("docs-log-content", secret).sync()
        // the docs: the SDK replaces the content with [...] and still logs the fingerprint and the byte count
        val entry = publishApiCall(log, secret.take(8).let { "Publish API call" })
        assertTrue(entry.contains("\"message\": \"[...]\""), "the content of the publish is not replaced by the marker")
        assertFalse(entry.contains(secret), "the message text is in the publish log entry")
        assertTrue(entry.contains("pn_mfp"), "the entry has no message fingerprint")
        assertTrue(entry.contains("pn_totalBytes"), "the entry has no byte count")
        // the same message is not logged by the other entries of the publish
        val others = log.messages().filter { it.contains(secret) && !it.contains("NetworkRequest") && !it.contains("NetworkResponse") }
        assertEquals(0, others.size, "the message text is in a log entry that is not a network request: " + others.map { Redact.scrub(it.replace(secret, "<text>").take(120)) })
    }

    @Test
    fun logContentConfigUnlimitedResponse(clients: LiveClients) {
        val log = GettingStartedKit.captureDebug()
        val writer = clients.kotlinClient(OPEN, "docs-config-sender")
        val ending = "END-OF-THE-LAST-MESSAGE"
        Cleanup.record("history", "docs-log-content")
        Retry.retryTransient(label = "first big message") { writer.publish("docs-log-content", "a".repeat(1500)).sync() }
        val last = Retry.retryTransient(label = "second big message") { writer.publish("docs-log-content", "b".repeat(1500) + ending).sync() }
        storedItem(writer, "docs-log-content", last.timetoken)
        // control: a client with the default cap of 2000 bytes shows a truncated response
        val control = clients.kotlinClient(OPEN, "docs-config-receiver")
        control.fetchMessages(listOf("docs-log-content")).sync()
        val controlResponses = log.containing("NetworkResponse").filter { it.contains("fetch") || it.contains("history") }
        assertTrue(controlResponses.isNotEmpty(), "the control client logged no response of the history call")
        assertFalse(controlResponses.any { it.contains(ending) }, "the default cap did not truncate the response")
        val before = log.messages().size

        // snippet.logContentConfigUnlimitedResponse
        val configUnlimitedResponse = PNConfiguration.builder(UserId("demoUserId"), "demo") {
            publishKey = "demo"
            // snippet.hide
            com.pubnub.docs.support.LiveKeys.apply(this, "open")
            // snippet.show
            logContentConfig = LogContentConfig(
                loggedHttpResponseMaxBytes = Int.MAX_VALUE,
            )
        }.build()

        val pubnubUnlimitedResponse = PubNub.create(configUnlimitedResponse)
        // snippet.end
        clients.track(pubnubUnlimitedResponse)

        assertEquals(Int.MAX_VALUE, configUnlimitedResponse.logContentConfig.loggedHttpResponseMaxBytes)
        pubnubUnlimitedResponse.fetchMessages(listOf("docs-log-content")).sync()
        val responses = log.messages().drop(before).filter { it.contains("NetworkResponse") && (it.contains("fetch") || it.contains("history")) }
        assertTrue(responses.isNotEmpty(), "the client of the sample logged no response of the history call")
        assertTrue(responses.any { it.contains(ending) }, "the unlimited response cap still truncated the response")
    }

    // ---------------------------------------------------------------------------------------------------------------
    // Whole-file backends and the samples that use them
    // ---------------------------------------------------------------------------------------------------------------

    private fun runSample(
        clients: LiveClients,
        channel: String,
        text: String,
        user: String,
        run: () -> Unit,
    ): PNMessageResult {
        val receiver = clients.kotlinClient(OPEN, "docs-config-receiver")
        val arrived = armMessage(receiver, channel, text)
        subscribeAndConnect(receiver, listOf(channel))
        Cleanup.record("history", channel)
        run()
        val message = arrived.await() as PNMessageResult
        assertEquals(user, message.publisher)
        assertEquals(text, message.message.asString)
        storedItem(receiver, channel, message.timetoken!!)
        return message
    }

    @Test
    fun loggingLogbackMain(clients: LiveClients) {
        // The logs carry the keys of the keyset: nothing reaches the console, the output is kept in memory.
        val out = GettingStartedKit.QuietOut.captureForTest()
        // the sample logs through logback, configured by the logback.xml of the docs
        GettingStartedKit.useLogbackConfig("$LOGGING_RESOURCES/logback.xml")

        runSample(clients, "logging-demo-channel", "Hello from Logging Example", "loggingDemoUser") {
            com.pubnub.docs.logging.samples.LoggingLogback.main(emptyArray())
        }

        val printed = out.text()
        assertTrue(printed.contains("Starting PubNub logging example..."), "the sample's own INFO line is missing")
        assertTrue(printed.contains("Message published - check logs for details"), "the sample did not finish")
        assertFalse(printed.contains("Error during operations"), "the sample logged an error")
        // the pattern of the file: time, the stray text of the Kotlin docs file, thread, level, logger
        assertTrue(
            printed.contains("-=extra if you like=- ["),
            "the pattern of the logback.xml was not used: " + Redact.scrub(printed.lines().filter { it.contains("Starting PubNub") }.joinToString("|").take(300)) + " size=" + printed.length,
        )
        // the logger of com.pubnub is on TRACE in the file: the SDK's DEBUG lines (configuration, network) reach the console
        assertTrue(printed.contains("DEBUG com.pubnub"), "the SDK logged no DEBUG line through the file's com.pubnub logger")
        assertTrue(printed.contains("Configuration logged"), "the SDK did not log its configuration")
        assertTrue(printed.contains("NetworkRequest"), "the SDK did not log its network requests")
    }

    @Test
    fun loggingLog4j2Main(clients: LiveClients) {
        val log = GettingStartedKit.captureDebug()

        runSample(clients, "log4j-demo-channel", "Hello from Log4j Example", "log4jDemoUser") {
            com.pubnub.docs.logging.samples.LoggingLog4j2.main(emptyArray())
        }

        // log4j2 is not the SLF4J binding of the tests: the sample logs through the one that is there
        assertTrue(log.containing("Time operation completed").isNotEmpty(), "the sample did not finish the time call")
        assertTrue(log.containing("Message published successfully").isNotEmpty(), "the sample did not publish")
        assertTrue(log.containing("Log4j example complete").isNotEmpty(), "the sample did not finish")
        assertTrue(log.containing("Error during operations").isEmpty(), "the sample logged an error")
    }

    @Test
    fun loggingSlf4jSimpleMain(clients: LiveClients) {
        val log = GettingStartedKit.captureDebug()

        runSample(clients, "slf4j-simple-demo-channel", "Hello from SLF4J Simple Example", "slf4jSimpleDemoUser") {
            com.pubnub.docs.logging.samples.LoggingSlf4jSimple.main(emptyArray())
        }

        assertTrue(log.containing("Time operation completed").isNotEmpty(), "the sample did not finish the time call")
        assertTrue(log.containing("Message published successfully").isNotEmpty(), "the sample did not publish")
        assertTrue(log.containing("SLF4J Simple example complete").isNotEmpty(), "the sample did not finish")
        assertTrue(log.containing("Error during operations").isEmpty(), "the sample logged an error")
    }

    @Test
    fun logbackXmlOfTheDocsRoutesTheLogsOfTheSdk() {
        // the same file loaded alone: it is valid logback configuration, the root logger is INFO and com.pubnub is TRACE
        GettingStartedKit.useLogbackConfig("$LOGGING_RESOURCES/logback.xml")
        val context = org.slf4j.LoggerFactory.getILoggerFactory() as ch.qos.logback.classic.LoggerContext
        assertEquals(ch.qos.logback.classic.Level.INFO, context.getLogger("ROOT").level)
        assertEquals(ch.qos.logback.classic.Level.TRACE, context.getLogger("com.pubnub").level)
        assertFalse(context.getLogger("com.pubnub").isAdditive, "the com.pubnub logger of the file is additive")
        assertNotNull(context.getLogger("com.pubnub").getAppender("CONSOLE"))
    }

    @Test
    fun log4j2XmlOfTheDocsSetsComPubnubToTrace() {
        val document = GettingStartedKit.parseXml("$LOGGING_RESOURCES/log4j2.xml")
        assertEquals("Configuration", document.documentElement.tagName)
        val loggers = document.getElementsByTagName("Logger")
        val names = (0 until loggers.length).associate { loggers.item(it).attributes.getNamedItem("name").nodeValue to loggers.item(it) }
        val pubnub = names["com.pubnub"] ?: throw AssertionError("the file has no com.pubnub logger")
        assertEquals("TRACE", pubnub.attributes.getNamedItem("level").nodeValue)
        assertEquals("INFO", document.getElementsByTagName("Root").item(0).attributes.getNamedItem("level").nodeValue)
        assertEquals(1, document.getElementsByTagName("Console").length)
    }

    @Test
    fun simpleloggerPropertiesOfTheDocsSetComPubnubToDebug() {
        val properties = Properties().apply {
            GettingStartedKit.resourceBytes("$LOGGING_RESOURCES/simplelogger.properties").inputStream().use { load(it) }
        }
        assertEquals("debug", properties.getProperty("org.slf4j.simpleLogger.defaultLogLevel"))
        assertEquals("debug", properties.getProperty("org.slf4j.simpleLogger.log.com.pubnub"))
        assertEquals("true", properties.getProperty("org.slf4j.simpleLogger.showDateTime"))
        assertEquals("true", properties.getProperty("org.slf4j.simpleLogger.showThreadName"))
    }

    @Test
    fun androidLogbackXmlOfTheDocsLogsToLogcat() {
        // the LogcatAppender class exists on Android only, so the file cannot be loaded on the JVM. It is read as XML.
        val document = GettingStartedKit.parseXml("$LOGGING_RESOURCES/android/logback.xml")
        assertEquals("configuration", document.documentElement.tagName)
        val appender = document.getElementsByTagName("appender").item(0)
        assertEquals("ch.qos.logback.classic.android.LogcatAppender", appender.attributes.getNamedItem("class").nodeValue)
        val logger = document.getElementsByTagName("logger").item(0)
        assertEquals("com.pubnub", logger.attributes.getNamedItem("name").nodeValue)
        assertEquals("TRACE", logger.attributes.getNamedItem("level").nodeValue)
        assertEquals("INFO", document.getElementsByTagName("root").item(0).attributes.getNamedItem("level").nodeValue)
    }

    // ---------------------------------------------------------------------------------------------------------------
    // What the docs say about keys in the log
    // ---------------------------------------------------------------------------------------------------------------

    @Test
    fun theConfigurationLogMasksTheSecretKey(clients: LiveClients) {
        // The docs: the configuration is logged at DEBUG when the client is created, the secret key is partly masked
        // (the first 8 characters and the length), and the publish and subscribe keys are logged as they are.
        val log = GettingStartedKit.captureDebug()
        val secret = Env.secretKey("pam")!!

        clients.kotlinClient("pam", "docs-config-sender")

        val entries = log.containing("Configuration logged")
        assertTrue(entries.isNotEmpty(), "the SDK did not log the configuration at DEBUG")
        val entry = entries.last()
        assertTrue(entry.contains("\"userId\": \"docs-config-sender\""), "the configuration log lacks the user id")
        assertTrue(entry.contains(secret.take(8) + "… (len=${secret.length})"), "the secret key is not shown as the first 8 characters and the length")
        assertFalse(log.text().contains(secret), "the full secret key is in the log")
    }

    @Test
    fun aSignedRequestNeverLogsTheSecretKey(clients: LiveClients) {
        val log = GettingStartedKit.captureDebug()
        val secret = Env.secretKey("pam")!!
        val pam = clients.kotlinClient("pam", "docs-config-sender")

        val token = pam.grantToken(ttl = 1, authorizedUUID = "docs-config-receiver", channels = listOf(com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant.name("docs-log-pam", read = true))).sync()
        Redact.register(token.token)

        // the docs: signature and token travel in the logs of the request and response, the secret key itself does not
        assertTrue(log.containing("NetworkRequest").isNotEmpty(), "the signed call was not logged")
        assertFalse(log.text().contains(secret), "the full secret key is in the log of a signed request")
    }
}

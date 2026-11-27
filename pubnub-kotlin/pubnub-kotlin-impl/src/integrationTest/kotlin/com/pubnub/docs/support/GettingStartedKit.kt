package com.pubnub.docs.support

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.joran.spi.JoranException
import ch.qos.logback.core.read.ListAppender
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Helpers of the getting-started group (configuration, logging, encryption, miscellaneous). Nothing here is part of a sample.
 *
 * The logging helpers change the logging of the whole JVM for the time of one test and put it back with a cleanup of the
 * test. The tier runs one test at a time, so no other test sees the change.
 */
object GettingStartedKit {
    /** The folder of the whole-file logging embeds in the integrationTest resources. */
    const val LOGGING_RESOURCES = "com/pubnub/docs/logging"

    /**
     * What a sample printed, kept in memory and never echoed: the logging of the SDK carries the keys of the keyset, so the
     * text must not reach the test log. Registered with the cleanups of the test, so no test leaves `System.out` swapped.
     */
    class QuietOut private constructor(private val original: PrintStream) : AutoCloseable {
        private val buffer = ByteArrayOutputStream()
        private var closed = false

        init {
            System.setOut(PrintStream(buffer, true, "UTF-8"))
        }

        fun text(): String = synchronized(buffer) { buffer.toString("UTF-8") }

        /** The number of registered key values that appear in what was printed. Never prints a value. */
        fun keyLeaks(): Int = Redact.leakCount(text())

        @Synchronized
        override fun close() {
            if (!closed) {
                closed = true
                System.out.flush()
                System.setOut(original)
            }
        }

        companion object {
            @JvmStatic
            fun captureForTest(): QuietOut =
                QuietOut(System.out).also { out -> Cleanup.cleanupAfterTest("restore System.out") { out.close() } }
        }
    }

    /** The log events of the `com.pubnub` loggers since [captureDebug], kept in memory. */
    class LogCapture internal constructor(private val appender: ListAppender<ILoggingEvent>) {
        /** The formatted messages in order. */
        fun messages(): List<String> = synchronized(appender.list) { appender.list.map { it.formattedMessage } }

        fun text(): String = messages().joinToString("\n")

        /** The messages that contain [part]. */
        fun containing(part: String): List<String> = messages().filter { it.contains(part) }

        fun keyLeaks(): Int = Redact.leakCount(text())
    }

    private fun context(): LoggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

    /**
     * Collects what the SDK logs at DEBUG and above (the level the docs say carries the configuration, the API calls and the
     * network traffic). The level and the appender are put back after the test.
     */
    @JvmStatic
    fun captureDebug(): LogCapture {
        val logger = LoggerFactory.getLogger("com.pubnub") as Logger
        val before = logger.level
        val additive = logger.isAdditive
        val appender = ListAppender<ILoggingEvent>()
        appender.context = context()
        appender.start()
        logger.level = Level.DEBUG
        // Not additive: the DEBUG lines carry the keys of the keyset and must not reach the console appender of the root logger.
        logger.isAdditive = false
        logger.addAppender(appender)
        Cleanup.cleanupAfterTest("stop the log capture") {
            logger.detachAppender(appender)
            appender.stop()
            logger.level = before
            logger.isAdditive = additive
        }
        return LogCapture(appender)
    }

    /** The text of a file of the integrationTest resources, for example `com/pubnub/docs/logging/logback.xml`. */
    @JvmStatic
    fun resourceText(path: String): String {
        val stream = GettingStartedKit::class.java.classLoader.getResourceAsStream(path) ?: error("resource $path is missing")
        return stream.use { it.readBytes().toString(Charsets.UTF_8) }
    }

    /** The bytes of a resource, to compare with the file that the docs embed. */
    @JvmStatic
    fun resourceBytes(path: String): ByteArray {
        val stream = GettingStartedKit::class.java.classLoader.getResourceAsStream(path) ?: error("resource $path is missing")
        return stream.use { it.readBytes() }
    }

    /**
     * Configures logback from the resource file [path] (a file of the whole-file logging embeds, kept out of the classpath
     * root so that no other test sees it) and puts the configuration of the classpath back after the test. The console
     * appender writes to the current `System.out` on every event, so a [QuietOut] started before or after sees the output.
     */
    @JvmStatic
    fun useLogbackConfig(path: String) {
        val context = context()
        Cleanup.cleanupAfterTest("restore the logback configuration of the classpath") {
            context.reset()
            ch.qos.logback.classic.util.ContextInitializer(context).autoConfig()
        }
        context.reset()
        try {
            JoranConfigurator().also { it.context = context }.doConfigure(resourceBytes(path).inputStream())
        } catch (e: JoranException) {
            throw AssertionError("logback could not read $path: ${e.message}")
        }
    }

    /** Parses an XML resource, fails when it is not well formed. */
    @JvmStatic
    fun parseXml(path: String): Document {
        val factory = DocumentBuilderFactory.newInstance()
        // The docs files never need an external entity.
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
        return factory.newDocumentBuilder().parse(resourceBytes(path).inputStream())
    }
}

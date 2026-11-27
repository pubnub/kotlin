package com.pubnub.docs.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.logging.HttpMethod;
import com.pubnub.api.logging.LogContentConfig;
import com.pubnub.api.logging.LogMessage;
import com.pubnub.api.logging.LogMessageContent;
import com.pubnub.api.logging.LogMessageType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.docs.logging.samples.AnalyticsService;
import com.pubnub.docs.logging.samples.MonitoringLogger;
import com.pubnub.docs.logging.samples.MonitoringService;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.Env;
import com.pubnub.docs.support.GettingStartedKit;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Redact;
import com.pubnub.docs.support.Wait;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Live tests of the Java docs page logging (docs/sdks/java/logging.md). One test per snippet. The text between a start marker
 * and {@code snippet.end} is the published sample, the arrangement is above it and the assertions below it.
 *
 * <p>The whole-file embeds of the page (logback.xml, log4j2.xml, simplelogger.properties) are resource files in
 * {@code src/integrationTest/resources/com/pubnub/docs/logging/}, outside the classpath root so that they do not change the
 * logging of any other test. The logback.xml is loaded explicitly for one test. The other backends are not on the classpath
 * of the tests (one SLF4J binding only), so their files are checked for what the docs say about them.
 */
@ExtendWith(LiveExtension.class)
class LoggingDocsTest {
    private static final String OPEN = "open";
    private static final String LOGGING = GettingStartedKit.LOGGING_RESOURCES;

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Custom logger
    // -----------------------------------------------------------------------------------------------------------------

    // The Java constructor of LogMessage has no defaults, so every argument is given.
    private static LogMessage message(LogMessageContent content, LogMessageType type, String pubNubId) {
        return new LogMessage(content, null, type, null, pubNubId, null, "12:00:00.000");
    }

    private static LogMessageContent.NetworkRequest request(String path, boolean failed) {
        return new LogMessageContent.NetworkRequest("https://ps.pndsn.com", path, null, HttpMethod.GET, null, null, null, null, null, false, failed);
    }

    @Test
    void customLoggerImplementation() {
        MonitoringService.clear();
        AnalyticsService.clear();
        MonitoringLogger logger = new MonitoringLogger();

        assertEquals("MonitoringLogger", logger.getName());
        // an error with its structured data goes to the monitoring service
        logger.error(message(new LogMessageContent.Error("PubNubException", "boom", Collections.singletonList("frame")), LogMessageType.ERROR, null));
        assertEquals(Collections.singletonList("boom"), MonitoringService.ERRORS);
        // a failed network request is reported with method and URL, a successful one is not
        logger.error(message(request("/time/0", true), LogMessageType.NETWORK_REQUEST, null));
        logger.error(message(request("/time/1", false), LogMessageType.NETWORK_REQUEST, null));
        assertEquals(Collections.singletonList("get https://ps.pndsn.com/time/0"), MonitoringService.NETWORK_FAILURES);
        // other content types of an error are ignored
        logger.error(message(new LogMessageContent.Text("plain text"), LogMessageType.TEXT, null));
        assertEquals(1, MonitoringService.ERRORS.size());
        // string warnings go to the monitoring service, null does not
        logger.warn("deprecated call");
        logger.warn((String) null);
        assertEquals(Collections.singletonList("deprecated call"), MonitoringService.WARNINGS);
        // an object log of an operation with "Configuration" in its name goes to the analytics service
        logger.debug(message(new LogMessageContent.Object(Collections.<String, Object>singletonMap("a", 1), "UpdateConfiguration"), LogMessageType.OBJECT, "instance-1"));
        logger.debug(message(new LogMessageContent.Object(Collections.<String, Object>singletonMap("a", 1), "TimeEndpoint"), LogMessageType.OBJECT, "instance-2"));
        logger.debug(message(new LogMessageContent.Text("text"), LogMessageType.TEXT, null));
        assertEquals(Collections.singletonList("instance-1"), AnalyticsService.CONFIG_CHANGES);
    }

    @Test
    void customLoggerConfiguration(LiveClients clients) throws Exception {
        MonitoringService.clear();
        AnalyticsService.clear();

        // snippet.customLoggerConfiguration

        // Configure SDK with custom logger
        PNConfiguration config = PNConfiguration.builder(new UserId("demoUserId"), "subscribeKey")
                .customLoggers(Collections.singletonList(new MonitoringLogger()))
                .build();

        PubNub pubnub = PubNub.create(config);

        // snippet.end
        clients.track(pubnub);

        // the SDK keeps the logger of the sample in its configuration
        assertEquals(1, pubnub.getConfiguration().getCustomLoggers().size());
        assertTrue(pubnub.getConfiguration().getCustomLoggers().get(0) instanceof MonitoringLogger);
        assertEquals("MonitoringLogger", pubnub.getConfiguration().getCustomLoggers().get(0).getName());
        // a real failure of the SDK reaches the monitoring service: the client has no publish key
        PubNubException failure = assertThrows(PubNubException.class, () -> pubnub.hereNow().channels(Collections.singletonList("myChannel")).sync());
        Wait.eventuallyRun(
                "the monitoring service heard of the failed call",
                () -> assertTrue(!MonitoringService.ERRORS.isEmpty() || !MonitoringService.WARNINGS.isEmpty(), "nothing reached the monitoring service"));
        System.out.println("monitoring: errors=" + MonitoringService.ERRORS.size() + " warnings=" + MonitoringService.WARNINGS.size() + " failureStatus=" + failure.getStatusCode());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Log content configuration
    // -----------------------------------------------------------------------------------------------------------------

    private static String publishApiCall(GettingStartedKit.LogCapture log) {
        List<String> entries = log.containing("Publish API call");
        if (entries.isEmpty()) {
            throw new AssertionError("no 'Publish API call' log entry. Logged: " + log.messages().size() + " entries");
        }
        return entries.get(entries.size() - 1);
    }

    @Test
    void logContentConfigCustom(LiveClients clients) throws Exception {
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();

        // snippet.logContentConfigCustom
        PNConfiguration config = PNConfiguration.builder(new UserId("demoUserId"), "demo")
                .publishKey("demo")
                // snippet.hide
                .subscribeKey(Env.INSTANCE.subscribeKey("open"))
                .publishKey(Env.INSTANCE.publishKey("open"))
                // snippet.show
                .logContentConfig(new LogContentConfig(1000, 4000))
                .build();

        PubNub pubnub = PubNub.create(config);
        // snippet.end
        clients.track(pubnub);
        Cleanup.record("history", "docs-log-content");

        assertEquals(1000, config.getLogContentConfig().getLoggedMessageContentMaxBytes());
        assertEquals(4000, config.getLogContentConfig().getLoggedHttpResponseMaxBytes());
        // a message of 1500 bytes: the log entry of the publish shows the first 1000 bytes only
        String body = String.join("", Collections.nCopies(1500, "k"));
        pubnub.publish().channel("docs-log-content").message(body).sync();
        String entry = publishApiCall(log);
        long shown = entry.chars().filter(c -> c == 'k').count();
        assertTrue(shown >= 990 && shown <= 1000, "the logged message content is not capped near 1000 bytes (" + shown + ")");
        assertTrue(entry.contains("\"pn_totalBytes\": 1502"), "the entry does not name the total size of the message");
        assertTrue(entry.contains("pn_mfp"), "the entry has no message fingerprint");
    }

    @Test
    void logContentConfigSuppressMessages(LiveClients clients) throws Exception {
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();

        // snippet.logContentConfigSuppressMessages
        PNConfiguration configSuppressMessages = PNConfiguration.builder(new UserId("demoUserId"), "demo")
                .publishKey("demo")
                // snippet.hide
                .subscribeKey(Env.INSTANCE.subscribeKey("open"))
                .publishKey(Env.INSTANCE.publishKey("open"))
                // snippet.show
                .logContentConfig(new LogContentConfig(
                        0,
                        LogContentConfig.DEFAULT_LOGGED_HTTP_RESPONSE_MAX_BYTES
                ))
                .build();

        PubNub pubnubSuppressMessages = PubNub.create(configSuppressMessages);
        // snippet.end
        clients.track(pubnubSuppressMessages);
        Cleanup.record("history", "docs-log-content");

        assertEquals(0, configSuppressMessages.getLogContentConfig().getLoggedMessageContentMaxBytes());
        String secret = Kit.probe("hide-this-text");
        pubnubSuppressMessages.publish().channel("docs-log-content").message(secret).sync();
        // the docs: the SDK replaces the content with [...] and still logs the fingerprint and the byte count
        String entry = publishApiCall(log);
        assertTrue(entry.contains("\"message\": \"[...]\""), "the content of the publish is not replaced by the marker");
        assertFalse(entry.contains(secret), "the message text is in the publish log entry");
        assertTrue(entry.contains("pn_mfp"), "the entry has no message fingerprint");
        assertTrue(entry.contains("pn_totalBytes"), "the entry has no byte count");
        List<String> others = log.messages().stream().filter(m -> m.contains(secret) && !m.contains("NetworkRequest") && !m.contains("NetworkResponse")).collect(Collectors.toList());
        assertEquals(0, others.size(), "the message text is in a log entry that is not a network request: " + others.stream().map(m -> Redact.scrub(m.replace(secret, "<text>").substring(0, Math.min(120, m.length())))).collect(Collectors.toList()));
    }

    @Test
    void logContentConfigUnlimitedResponse(LiveClients clients) throws Exception {
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();
        PubNub writer = clients.javaClient(OPEN, "docs-config-sender");
        String ending = "END-OF-THE-LAST-MESSAGE";
        Cleanup.record("history", "docs-log-content");
        Kit.retry("first big message", () -> writer.publish().channel("docs-log-content").message(String.join("", Collections.nCopies(1500, "a"))).sync());
        PNPublishResult last = Kit.retry("second big message", () -> writer.publish().channel("docs-log-content").message(String.join("", Collections.nCopies(1500, "b")) + ending).sync());
        Kit.storedItem(kotlinClient(clients, "docs-config-receiver"), "docs-log-content", last.getTimetoken());
        // control: a client with the default cap of 2000 bytes shows a truncated response
        PubNub control = clients.javaClient(OPEN, "docs-config-receiver");
        control.fetchMessages().channels(Collections.singletonList("docs-log-content")).sync();
        List<String> controlResponses = log.containing("NetworkResponse").stream().filter(m -> m.contains("history")).collect(Collectors.toList());
        assertFalse(controlResponses.isEmpty(), "the control client logged no response of the history call");
        assertFalse(controlResponses.stream().anyMatch(m -> m.contains(ending)), "the default cap did not truncate the response");
        int before = log.messages().size();

        // snippet.logContentConfigUnlimitedResponse
        PNConfiguration configUnlimitedResponse = PNConfiguration.builder(new UserId("demoUserId"), "demo")
                .publishKey("demo")
                // snippet.hide
                .subscribeKey(Env.INSTANCE.subscribeKey("open"))
                .publishKey(Env.INSTANCE.publishKey("open"))
                // snippet.show
                .logContentConfig(new LogContentConfig(
                        LogContentConfig.DEFAULT_LOGGED_MESSAGE_CONTENT_MAX_BYTES,
                        Integer.MAX_VALUE
                ))
                .build();

        PubNub pubnubUnlimitedResponse = PubNub.create(configUnlimitedResponse);
        // snippet.end
        clients.track(pubnubUnlimitedResponse);

        assertEquals(Integer.MAX_VALUE, configUnlimitedResponse.getLogContentConfig().getLoggedHttpResponseMaxBytes());
        pubnubUnlimitedResponse.fetchMessages().channels(Collections.singletonList("docs-log-content")).sync();
        List<String> responses = log.messages().subList(before, log.messages().size()).stream().filter(m -> m.contains("NetworkResponse") && m.contains("history")).collect(Collectors.toList());
        assertFalse(responses.isEmpty(), "the client of the sample logged no response of the history call");
        assertTrue(responses.stream().anyMatch(m -> m.contains(ending)), "the unlimited response cap still truncated the response");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Whole-file backends and the samples that use them
    // -----------------------------------------------------------------------------------------------------------------

    private interface Run {
        void run() throws Exception;
    }

    private static PNMessageResult runSample(LiveClients clients, String channel, String text, String user, Run run) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-config-receiver");
        Wait.Expectation arrived = Kit.armMessage(receiver, channel, text);
        Kit.subscribeAndConnect(receiver, Collections.singletonList(channel));
        Cleanup.record("history", channel);
        run.run();
        PNMessageResult message = (PNMessageResult) arrived.await();
        assertEquals(user, message.getPublisher());
        assertEquals(text, message.getMessage().getAsString());
        Kit.storedItem(receiver, channel, message.getTimetoken());
        return message;
    }

    @Test
    void loggingLogbackApp(LiveClients clients) throws Exception {
        // The logs carry the keys of the keyset: nothing reaches the console, the output is kept in memory.
        GettingStartedKit.QuietOut out = GettingStartedKit.QuietOut.captureForTest();
        // the sample logs through logback, configured by the logback.xml of the docs
        GettingStartedKit.useLogbackConfig(LOGGING + "/logback.xml");

        runSample(clients, "logging-demo-channel", "Hello from Logging Example", "loggingDemoUser",
                () -> com.pubnub.docs.logging.samples.LoggingLogbackApp.main(new String[0]));

        String printed = out.text();
        assertTrue(printed.contains("Starting PubNub logging example..."), "the sample's own INFO line is missing");
        assertTrue(printed.contains("Message published - check logs for details"), "the sample did not finish");
        // the pattern of the file: time, thread, level, logger
        assertTrue(printed.contains("] INFO  c.p.d.l.s.LoggingLogbackApp - Starting") || printed.matches("(?s).*\\] INFO +\\S*LoggingLogbackApp - Starting.*"), "the pattern of the logback.xml was not used");
        // the logger of com.pubnub is on TRACE in the file: the SDK's DEBUG lines (configuration, network) reach the console
        assertTrue(printed.contains("DEBUG com.pubnub"), "the SDK logged no DEBUG line through the file's com.pubnub logger");
        assertTrue(printed.contains("Configuration logged"), "the SDK did not log its configuration");
        assertTrue(printed.contains("NetworkRequest"), "the SDK did not log its network requests");
    }

    @Test
    void loggingLog4j2App(LiveClients clients) throws Exception {
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();

        runSample(clients, "log4j2-demo-channel", "Hello from Log4j2 Example", "log4j2DemoUser",
                () -> com.pubnub.docs.logging.samples.LoggingLog4j2App.main(new String[0]));

        // log4j2 is not the SLF4J binding of the tests: the sample logs through the one that is there
        assertFalse(log.containing("Time operation completed").isEmpty(), "the sample did not finish the time call");
        assertFalse(log.containing("Message published successfully").isEmpty(), "the sample did not publish");
        assertFalse(log.containing("Log4j2 example complete").isEmpty(), "the sample did not finish");
    }

    @Test
    void loggingSlf4jSimpleApp(LiveClients clients) throws Exception {
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();

        runSample(clients, "slf4j-simple-demo-channel", "Hello from SLF4J Simple Example", "slf4jSimpleDemoUser",
                () -> com.pubnub.docs.logging.samples.LoggingSlf4jSimpleApp.main(new String[0]));

        assertFalse(log.containing("Time operation completed").isEmpty(), "the sample did not finish the time call");
        assertFalse(log.containing("Message published successfully").isEmpty(), "the sample did not publish");
        assertFalse(log.containing("SLF4J Simple example complete").isEmpty(), "the sample did not finish");
    }

    @Test
    void logbackXmlOfTheDocsRoutesTheLogsOfTheSdk() {
        // the same file loaded alone: it is valid logback configuration, the root logger is INFO and com.pubnub is TRACE
        GettingStartedKit.useLogbackConfig(LOGGING + "/logback.xml");
        ch.qos.logback.classic.LoggerContext context = (ch.qos.logback.classic.LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
        assertEquals(ch.qos.logback.classic.Level.INFO, context.getLogger("ROOT").getLevel());
        assertEquals(ch.qos.logback.classic.Level.TRACE, context.getLogger("com.pubnub").getLevel());
        assertFalse(context.getLogger("com.pubnub").isAdditive(), "the com.pubnub logger of the file is additive");
        assertNotNull(context.getLogger("com.pubnub").getAppender("CONSOLE"));
    }

    @Test
    void log4j2XmlOfTheDocsSetsComPubnubToTrace() {
        Document document = GettingStartedKit.parseXml(LOGGING + "/log4j2.xml");
        assertEquals("Configuration", document.getDocumentElement().getTagName());
        NodeList loggers = document.getElementsByTagName("Logger");
        String level = null;
        for (int i = 0; i < loggers.getLength(); i++) {
            if ("com.pubnub".equals(loggers.item(i).getAttributes().getNamedItem("name").getNodeValue())) {
                level = loggers.item(i).getAttributes().getNamedItem("level").getNodeValue();
            }
        }
        assertNotNull(level, "the file has no com.pubnub logger");
        assertEquals("TRACE", level.toUpperCase());
        assertEquals("INFO", document.getElementsByTagName("Root").item(0).getAttributes().getNamedItem("level").getNodeValue());
        assertEquals(1, document.getElementsByTagName("Console").getLength());
    }

    @Test
    void simpleloggerPropertiesOfTheDocsSetComPubnubToDebug() throws IOException {
        Properties properties = new Properties();
        try (InputStream stream = new java.io.ByteArrayInputStream(GettingStartedKit.resourceBytes(LOGGING + "/simplelogger.properties"))) {
            properties.load(stream);
        }
        assertEquals("debug", properties.getProperty("org.slf4j.simpleLogger.defaultLogLevel"));
        assertEquals("debug", properties.getProperty("org.slf4j.simpleLogger.log.com.pubnub"));
        assertEquals("true", properties.getProperty("org.slf4j.simpleLogger.showDateTime"));
        assertEquals("true", properties.getProperty("org.slf4j.simpleLogger.showThreadName"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // What the docs say about keys in the log
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void theConfigurationLogMasksTheSecretKey(LiveClients clients) {
        // The docs: the configuration is logged at DEBUG when the client is created, the secret key is partly masked
        // (the first 8 characters and the length), and the publish and subscribe keys are logged as they are.
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();
        String secret = Env.INSTANCE.secretKey("pam");

        clients.javaClient("pam", "docs-config-sender");

        List<String> entries = log.containing("Configuration logged");
        assertFalse(entries.isEmpty(), "the SDK did not log the configuration at DEBUG");
        String entry = entries.get(entries.size() - 1);
        assertTrue(entry.contains("\"userId\": \"docs-config-sender\""), "the configuration log lacks the user id");
        assertTrue(entry.contains(secret.substring(0, 8) + "… (len=" + secret.length() + ")"), "the secret key is not shown as the first 8 characters and the length");
        assertFalse(log.text().contains(secret), "the full secret key is in the log");
    }

    @Test
    void aSignedRequestNeverLogsTheSecretKey(LiveClients clients) throws Exception {
        GettingStartedKit.LogCapture log = GettingStartedKit.captureDebug();
        String secret = Env.INSTANCE.secretKey("pam");
        PubNub pam = clients.javaClient("pam", "docs-config-sender");

        PNGrantTokenResult token =
                pam.grantToken(1)
                        .authorizedUUID("docs-config-receiver")
                        .channels(Collections.singletonList(ChannelGrant.name("docs-log-pam").read()))
                        .sync();
        Redact.register(token.getToken());

        // the docs: signature and token travel in the logs of the request and response, the secret key itself does not
        assertFalse(log.containing("NetworkRequest").isEmpty(), "the signed call was not logged");
        assertFalse(log.text().contains(secret), "the full secret key is in the log of a signed request");
    }
}

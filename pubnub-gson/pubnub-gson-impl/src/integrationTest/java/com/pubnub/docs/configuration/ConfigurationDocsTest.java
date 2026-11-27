package com.pubnub.docs.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.PNConfigurationOverride;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.CompileOnly;
import com.pubnub.docs.support.JavaWait;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Redact;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page configuration (docs/sdks/java/api-reference/configuration.md). One test per snippet. The
 * text between a start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions
 * below it. The samples build clients with placeholder keys. Where the sample's behavior only shows against the service (a
 * filter, a cipher key, a secret key, a publish key) a hidden line swaps in the live keys of a role.
 *
 * <p>The sample's own client is a Java client ({@code com.pubnub.api.java.PubNub}), the second clients of a test are Kotlin
 * clients ({@code com.pubnub.api.PubNub}).
 */
@ExtendWith(LiveExtension.class)
class ConfigurationDocsTest {
    private static final String OPEN = "open";

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static List<?> staticList(String className, String field) throws Exception {
        Field f = Class.forName(className).getDeclaredField(field);
        f.setAccessible(true);
        return (List<?>) f.get(null);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Configuration
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void configurationMain(LiveClients clients) throws Exception {
        String logger = "com.pubnub.docs.configuration.samples.MyCustomLoggerImpl";
        java.lang.reflect.Method clear = Class.forName(logger).getDeclaredMethod("clear");
        clear.setAccessible(true);
        clear.invoke(null);
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.configuration.samples.ConfigurationApp.main(new String[0]);

        out.assertPrinted("PubNub client initialized.");
        assertEquals(0, Redact.INSTANCE.leakCount(out.text()), "the sample printed a key of the keysets");
        // the custom logger of the sample is in the configuration: the SDK logged the configuration of the new client to it
        List<?> logMessages = staticList(logger, "logMessages");
        assertFalse(logMessages.isEmpty(), "the custom logger of the sample received no log message");
        boolean configurationLogged =
                logMessages.stream()
                        .map(m -> (com.pubnub.api.logging.LogMessage) m)
                        .anyMatch(m -> "Configuration logged".equals(m.getDetails()));
        assertTrue(configurationLogged, "the custom logger did not receive the configuration of the client");
    }

    @Test
    void basicConfiguration() throws Exception {
        // snippet.basicConfiguration
        // import com.pubnub.api.java.v2.PNConfiguration;
        PNConfiguration.builder(new UserId("yourUserId"), "demo").build();
        // snippet.end

        // the sample drops what it builds, so the same call is checked here
        PNConfiguration built = PNConfiguration.builder(new UserId("yourUserId"), "demo").build();
        assertEquals("yourUserId", built.getUserId().getValue());
        assertEquals("demo", built.getSubscribeKey());
        assertTrue(built.getSecure());
    }

    @Test
    void valueOverride(LiveClients clients, Recorder recorder) throws Exception {
        PubNub client = javaClient(clients, "yourUserId");
        PubNub pubNub = recorder.wrap(client, PubNub.class);
        Cleanup.record("history", "myChannel");
        // control: without the override the client publishes
        PNPublishResult control = Kit.retry("publish without override", () -> client.publish().channel("myChannel").message("control").sync());
        Kit.assertTimetoken(control.getTimetoken());

        // the override carries a publish key that the service does not know, so only the override can make the call fail
        PubNubException error = assertThrows(PubNubException.class, () -> valueOverrideSample(pubNub));

        Throwable recorded = recorder.awaitFailure("publish");
        assertTrue(recorded == error || String.valueOf(recorded.getMessage()).equals(String.valueOf(error.getMessage())), "the recorder saw another failure");
        assertEquals(400, error.getStatusCode());
        assertTrue(String.valueOf(error.getErrorMessage()).contains("Invalid Key") || String.valueOf(error.getMessage()).contains("Invalid Key"), "the service did not name the key");
        // the client itself is unchanged: it still publishes with its own key
        Kit.assertTimetoken(Kit.retry("publish after override", () -> client.publish().channel("myChannel").message("after").sync()).getTimetoken());
    }

    private static void valueOverrideSample(PubNub pubNub) throws PubNubException {
        // snippet.valueOverride
        //import com.pubnub.api.java.v2.PNConfiguration;
        //import com.pubnub.api.java.v2.PNConfigurationOverride;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.publishKey("PublishKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        configBuilder.cryptoModule(CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true));
        PNConfiguration config = configBuilder.build();

        // publish
        PNConfiguration overrideConfig = PNConfigurationOverride.from(config)
                .publishKey("overridePublishKey")
                .build();

        pubNub.publish().channel("myChannel").message("myMessage")
                .overrideConfiguration(overrideConfig).sync();
        // snippet.end
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Initialization
    // -----------------------------------------------------------------------------------------------------------------

    // The sample drops the client it creates, so nothing can destroy it and its non-daemon pool threads outlive the run.
    // The same call with a kept client is run by initializePubNubBasicExample.
    @CompileOnly("the sample discards the PubNub client it creates, which leaks non-daemon threads that fail the thread check of the run")
    private void initializePubNubMethod() throws Exception {
        // snippet.initializePubNubMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub.create(configBuilder.build());
        // snippet.end
    }

    @Test
    void initializePubNubBasicExample(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-config-receiver");
        String text = Kit.probe("initialize");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", text);
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");

        // snippet.initializePubNubBasicExample
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        assertEquals("yourUserId", pubNub.getConfiguration().getUserId().getValue());
        // the client built by the sample publishes: it has the keys of the keyset
        PNPublishResult published = pubNub.publish().channel("myChannel").message(text).sync();
        PNMessageResult received = (PNMessageResult) arrived.await();
        assertEquals("yourUserId", received.getPublisher());
        assertEquals(published.getTimetoken(), received.getTimetoken());
        Kit.assertTimetoken(received.getTimetoken());
    }

    @Test
    void initializeNonSecureClient(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-config-receiver");
        String text = Kit.probe("non-secure");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", text);
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");

        // snippet.initializeNonSecureClient
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.secure(false);
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        PubNub pubNub = PubNub.create(configBuilder.build());

        // snippet.end
        clients.track(pubNub);

        assertFalse(pubNub.getConfiguration().getSecure());
        // the service takes a publish over plain HTTP, a secure subscriber gets it
        PNPublishResult published = pubNub.publish().channel("myChannel").message(text).sync();
        PNMessageResult received = (PNMessageResult) arrived.await();
        assertEquals(published.getTimetoken(), received.getTimetoken());
        assertEquals("yourUserId", received.getPublisher());
        Kit.storedItem(receiver, "myChannel", published.getTimetoken());
    }

    @Test
    void initializeReadOnlyClient(LiveClients clients) throws Exception {
        Cleanup.record("history", "myChannel");

        // snippet.initializeReadOnlyClient
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        // snippet.hide
        configBuilder.subscribeKey(com.pubnub.docs.support.Env.INSTANCE.subscribeKey("open"));
        // snippet.show
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        // no publish key: the client reads, it cannot publish
        assertEquals("", pubNub.getConfiguration().getPublishKey());
        PubNubException error = assertThrows(PubNubException.class, () -> pubNub.publish().channel("myChannel").message("read only").sync());
        assertEquals(PubNubError.PUBLISH_KEY_MISSING.getMessage(), error.getErrorMessage());
        // reading works with the subscribe key alone
        Kit.assertTimetoken(pubNub.time().sync().getTimetoken());
        assertNotNull(pubNub.hereNow().channels(Collections.singletonList("myChannel")).sync());
    }

    @Test
    void initializeWithSsl(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub receiver = kotlinClient(clients, "docs-config-receiver");
        String text = Kit.probe("ssl");
        Wait.Expectation arrived = Kit.armMessage(receiver, "myChannel", text);
        Kit.subscribeAndConnect(receiver, Collections.singletonList("myChannel"));
        Cleanup.record("history", "myChannel");

        // snippet.initializeWithSsl
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.secure(true);
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        // snippet.show
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        assertTrue(pubNub.getConfiguration().getSecure());
        PNPublishResult published = pubNub.publish().channel("myChannel").message(text).sync();
        PNMessageResult received = (PNMessageResult) arrived.await();
        assertEquals(published.getTimetoken(), received.getTimetoken());
        assertEquals("yourUserId", received.getPublisher());
    }

    @Test
    void initializeWithAccessManager(LiveClients clients) throws Exception {
        // snippet.initializeWithAccessManager
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.secure(true);
        configBuilder.secretKey("secretKey");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "pam");
        // snippet.show
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        assertTrue(pubNub.getConfiguration().getSecure());
        assertFalse(pubNub.getConfiguration().getSecretKey().isEmpty(), "the client has no secret key");
        // the secret key signs the Access Manager calls, a client without it cannot grant
        PNGrantTokenResult granted =
                pubNub.grantToken(1)
                        .authorizedUUID("docs-config-receiver")
                        .channels(Collections.singletonList(ChannelGrant.name("docs-config-pam").read()))
                        .sync();
        Redact.INSTANCE.register(granted.getToken());
        assertFalse(granted.getToken().isEmpty(), "the grant returned no token");
    }

    @Test
    void initializeWithProxy(LiveClients clients) throws Exception {
        // control: the service is reachable without the proxy
        Kit.assertTimetoken(javaClient(clients, "yourUserId").time().sync().getTimetoken());

        // snippet.initializeWithProxy
        //  import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.publishKey("myPublishKey");
        configBuilder.secure(true);
        configBuilder.secretKey("secretKey");
        configBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("http://mydomain.com", 8080)));
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        assertEquals(Proxy.Type.HTTP, pubNub.getConfiguration().getProxy().type());
        assertEquals(8080, ((InetSocketAddress) pubNub.getConfiguration().getProxy().address()).getPort());
        // every request of the client goes through the proxy, the address of the sample does not exist
        assertThrows(PubNubException.class, () -> pubNub.time().sync());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // UserId
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void configurationSetUserIdMethod() throws Exception {
        // snippet.configurationSetUserIdMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        // snippet.end

        // the sample drops the builder, the same call is checked here
        PNConfiguration.Builder builder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        assertEquals("yourUserId", builder.build().getUserId().getValue());
    }

    @Test
    void configurationSetUserId(LiveClients clients) throws Exception {
        // snippet.configurationSetUserId
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
        clients.track(pubNub);

        assertEquals("yourUserId", pubNub.getConfiguration().getUserId().getValue());
        // the configuration of the client is immutable: a copy with another user id leaves it as it is
        PNConfiguration copy = PNConfigurationOverride.from(pubNub.getConfiguration()).setUserId(new UserId("new-user-id")).build();
        assertEquals("new-user-id", copy.getUserId().getValue());
        assertEquals("yourUserId", pubNub.getConfiguration().getUserId().getValue());
    }

    @Test
    void configurationGetUserIdMethod(LiveClients clients) throws Exception {
        PNConfiguration.Builder arranged = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(arranged.build());
        clients.track(pubNub);

        // snippet.configurationGetUserIdMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        pubNub.getConfiguration().getUserId();
        // snippet.end

        // the sample drops the result, the same call is checked here
        assertEquals(new UserId("yourUserId"), pubNub.getConfiguration().getUserId());
    }

    @Test
    void configurationGetUserId(LiveClients clients) throws Exception {
        // snippet.configurationGetUserId
        //  import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());

        pubNub.getConfiguration().getUserId();
        // snippet.end
        clients.track(pubNub);

        assertEquals("yourUserId", pubNub.getConfiguration().getUserId().getValue());
    }

    @Test
    void userIdMustNotBeBlank() {
        // The docs say a client without a userId cannot connect. The SDK enforces it when the UserId is created.
        PubNubException empty = assertThrows(PubNubException.class, () -> new UserId(""));
        assertEquals(PubNubError.USERID_NULL_OR_EMPTY, empty.getPubnubError());
        assertEquals("UserId can't have empty value", empty.getErrorMessage());
        PubNubException blank = assertThrows(PubNubException.class, () -> new UserId("   "));
        assertEquals(PubNubError.USERID_NULL_OR_EMPTY, blank.getPubnubError());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Filter expression
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void configurationSetFilterExpressionMethod() throws Exception {
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");

        // snippet.configurationSetFilterExpressionMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        configBuilder.filterExpression("someString");
        // snippet.end

        assertEquals("someString", configBuilder.build().getFilterExpression());
    }

    @Test
    void configurationSetFilterExpression(LiveClients clients) throws Exception {
        PubNub publisher = javaClient(clients, "docs-config-sender");
        Cleanup.record("history", "docs-config-filter");

        // snippet.configurationSetFilterExpression
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.filterExpression("such=wow");
        // snippet.end

        // the sample only fills the builder: the live keys and the client come after it
        com.pubnub.docs.support.LiveKeys.apply(configBuilder, "open");
        PNConfiguration configuration = configBuilder.build();
        assertEquals("such=wow", configuration.getFilterExpression());
        PubNub pubNub = clients.track(PubNub.create(configuration));
        String wanted = Kit.probe("filter-wanted");
        String unwanted = Kit.probe("filter-unwanted");
        // control: the filter language of the service needs a comparison operator and a quoted value
        PNConfiguration.Builder controlBuilder = PNConfiguration.builder(new UserId("docs-config-receiver"), "yourSubscribeKey");
        controlBuilder.filterExpression("such=='wow'");
        com.pubnub.docs.support.LiveKeys.apply(controlBuilder, "open");
        PubNub control = clients.track(PubNub.create(controlBuilder.build()));
        JavaWait.Expectation controlConnected = JavaWait.connected(control, Collections.singletonList("docs-config-filter"), Collections.emptyList());
        JavaWait.Expectation controlWanted = JavaWait.message(control, "docs-config-filter", wanted);
        JavaWait.Expectation controlUnwanted = JavaWait.message(control, "docs-config-filter", unwanted);
        control.subscribe().channels(Collections.singletonList("docs-config-filter")).execute();
        controlConnected.await();
        JavaWait.Expectation connected = JavaWait.connected(pubNub, Collections.singletonList("docs-config-filter"), Collections.emptyList());
        JavaWait.Expectation arrivedWanted = JavaWait.message(pubNub, "docs-config-filter", wanted);
        JavaWait.Expectation arrivedUnwanted = JavaWait.message(pubNub, "docs-config-filter", unwanted);
        pubNub.subscribe().channels(Collections.singletonList("docs-config-filter")).execute();
        connected.await();
        publisher.publish().channel("docs-config-filter").message(unwanted).meta(Collections.singletonMap("such", "sad")).sync();
        publisher.publish().channel("docs-config-filter").message(wanted).meta(Collections.singletonMap("such", "wow")).sync();
        PNMessageResult received = (PNMessageResult) controlWanted.await();
        assertEquals("{\"such\":\"wow\"}", received.getUserMetadata().toString());
        JavaWait.assertNothingArrives(controlUnwanted, "a message that the filter expression excludes arrived", 3000);
        // The expression of the docs sample ("such=wow") is not valid filter syntax: the service takes the subscription and
        // then delivers nothing to it, not even the message that matches (docs finding).
        JavaWait.assertNothingArrives(arrivedWanted, "the filter expression of the sample delivered a message", 1000);
        JavaWait.assertNothingArrives(arrivedUnwanted, "the filter expression of the sample delivered a message", 1000);
    }

    @Test
    void configurationGetFilterExpressionMethod(LiveClients clients) throws Exception {
        PNConfiguration.Builder arranged = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        arranged.filterExpression("such=wow");
        PubNub pubNub = clients.track(PubNub.create(arranged.build()));

        // snippet.configurationGetFilterExpressionMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        pubNub.getConfiguration().getFilterExpression();
        // snippet.end

        // the sample drops the result, the same call is checked here
        assertEquals("such=wow", pubNub.getConfiguration().getFilterExpression());
    }

    @Test
    void configurationGetFilterExpression(LiveClients clients) throws Exception {
        // snippet.configurationGetFilterExpression
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.filterExpression("such=wow");
        PubNub pubNub = PubNub.create(configBuilder.build());

        pubNub.getConfiguration().getFilterExpression();
        // snippet.end
        clients.track(pubNub);

        assertEquals("such=wow", pubNub.getConfiguration().getFilterExpression());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Encryption on the configuration
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void cryptoModuleConfiguration(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub plainReader = kotlinClient(clients, "docs-config-sender");
        String text = Kit.probe("crypto");
        Wait.Expectation seenByPlainReader = Kit.armMessage(plainReader, "docs-config-crypto", "");
        Kit.subscribeAndConnect(plainReader, Collections.singletonList("docs-config-crypto"));
        com.pubnub.api.PubNub sameKeyReader =
                clients.kotlinClient(
                        OPEN,
                        "docs-config-receiver",
                        (Consumer<com.pubnub.api.v2.PNConfiguration.Builder>)
                                b -> {
                                    try {
                                        b.setCryptoModule(CryptoModule.createLegacyCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true));
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
        Wait.Expectation seenBySameKey = Kit.armMessage(sameKeyReader, "docs-config-crypto", text);
        Kit.subscribeAndConnect(sameKeyReader, Collections.singletonList("docs-config-crypto"));
        Cleanup.record("history", "docs-config-crypto");

        // snippet.cryptoModuleConfiguration
        PNConfiguration.Builder builder = PNConfiguration.builder(new UserId("yourUserId"), "demo");
        // snippet.hide
        com.pubnub.docs.support.LiveKeys.apply(builder, "open");
        // snippet.show

        // encrypts using 256-bit AES-CBC cipher (recommended)
        // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
        // use a high-entropy, randomly generated cipher key rather than a dictionary word
        builder.cryptoModule(CryptoModule.createAesCbcCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true));

        // OR

        // encrypts with 128-bit cipher key entropy (legacy)
        // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
        builder.cryptoModule(CryptoModule.createLegacyCryptoModule("pn-9F3kQ7zR2xV8mB1tD6wL4yH0sN5cJ", true));

        PNConfiguration pnConfiguration = builder.build();
        // snippet.end

        assertNotNull(pnConfiguration.getCryptoModule());
        // the second call wins: the legacy module encrypts, a reader with the same cipher key reads the text
        PubNub pubNub = clients.track(PubNub.create(pnConfiguration));
        PNPublishResult published = pubNub.publish().channel("docs-config-crypto").message(text).sync();
        PNMessageResult readable = (PNMessageResult) seenBySameKey.await();
        assertEquals(text, readable.getMessage().getAsString());
        assertEquals(published.getTimetoken(), readable.getTimetoken());
        // a client without a crypto module gets the cipher text, not the message
        PNMessageResult raw = (PNMessageResult) seenByPlainReader.await();
        assertEquals(published.getTimetoken(), raw.getTimetoken());
        assertNotEquals(text, raw.getMessage().getAsString());
        assertFalse(raw.getMessage().toString().contains(text), "the message crossed the service in clear text");
    }
}

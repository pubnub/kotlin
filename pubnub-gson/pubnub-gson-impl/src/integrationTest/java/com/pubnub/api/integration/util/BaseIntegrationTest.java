package com.pubnub.api.integration.util;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.callbacks.SubscribeCallback;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.retry.RetryConfiguration;
import okhttp3.logging.HttpLoggingInterceptor;
import org.aeonbits.owner.ConfigFactory;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.awaitility.pollinterval.FibonacciPollInterval;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public abstract class BaseIntegrationTest {

    protected Logger log = LoggerFactory.getLogger(BaseIntegrationTest.class);

    private static String SUB_KEY;
    private static String PUB_KEY;
    private static String PAM_SUB_KEY;
    private static String PAM_PUB_KEY;
    private static String PAM_SEC_KEY;

    public com.pubnub.api.java.PubNub pubNub;
    public com.pubnub.api.java.PubNub server;

    public int TIMEOUT_MEDIUM = 5;
    public int TIMEOUT_LOW = 2;

    private List<com.pubnub.api.java.PubNub> mGuestClients = new ArrayList<>();

    @BeforeClass
    public static void onlyOnce() {
        final ITTestConfig itTestConfig = ConfigFactory.create(ITTestConfig.class, System.getenv());
        SUB_KEY = itTestConfig.subscribeKey();
        PUB_KEY = itTestConfig.publishKey();
        PAM_SUB_KEY = itTestConfig.pamSubKey();
        PAM_PUB_KEY = itTestConfig.pamPubKey();
        PAM_SEC_KEY = itTestConfig.pamSecKey();
    }

    @Before
    public void before() {
        onPrePubNub();
        pubNub = getPubNub();
        if (needsServer()) {
            server = getServer();
        }
        onBefore();
    }

    @After
    public void after() {
        onAfter();
        destroyClient(pubNub);
        if (mGuestClients != null) {
            for (com.pubnub.api.java.PubNub guestClient : mGuestClients) {
                destroyClient(guestClient);
            }
        }
        // properties.clear();
    }

    public com.pubnub.api.java.PubNub getPubNub(@Nullable Consumer<PNConfiguration.Builder> action) {
        PNConfiguration pnConfiguration = provideStagingConfiguration(action);
        if (pnConfiguration == null) {
            pnConfiguration = getBasicPnConfiguration(action);
        }
        final com.pubnub.api.java.PubNub pubNub = com.pubnub.api.java.PubNub.create(pnConfiguration);
        registerGuestClient(pubNub);
        return pubNub;
    }

    public com.pubnub.api.java.PubNub getPubNub() {
        return getPubNub(null);
    }

    protected com.pubnub.api.java.PubNub getServer(@Nullable Consumer<PNConfiguration.Builder> action) {
        final com.pubnub.api.java.PubNub pubNub = com.pubnub.api.java.PubNub.create(getServerPnConfiguration(action));
        registerGuestClient(pubNub);
        return pubNub;
    }

    public com.pubnub.api.java.PubNub getServer() {
        return getServer(null);
    }

//    public PubNub getPubNub(PNConfiguration pnConfiguration) {
//        final PubNub pubNub = PubNub.create(pnConfiguration);
//        registerGuestClient(pubNub);
//        return pubNub;
//    }

    protected void registerGuestClient(com.pubnub.api.java.PubNub guestClient) {
        if (mGuestClients == null) {
            mGuestClients = new ArrayList<>();
        }
        mGuestClients.add(guestClient);
    }

    protected void destroyClient(com.pubnub.api.java.PubNub client) {
        client.unsubscribeAll();
        client.forceDestroy();
    }

    private PNConfiguration getBasicPnConfiguration(@Nullable Consumer<PNConfiguration.Builder> action) {
        final com.pubnub.api.java.v2.PNConfiguration.Builder pnConfiguration;
        try {
        if (!needsServer()) {
            pnConfiguration = com.pubnub.api.java.v2.PNConfiguration.builder(new UserId("client-".concat(UUID.randomUUID().toString())), SUB_KEY);
            pnConfiguration.publishKey(PUB_KEY);
        } else {
            pnConfiguration = com.pubnub.api.java.v2.PNConfiguration.builder(new UserId("client-".concat(UUID.randomUUID().toString())), PAM_SUB_KEY);
            pnConfiguration.publishKey(PAM_PUB_KEY);
            pnConfiguration.authKey(provideAuthKey());
        }
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
        pnConfiguration.retryConfiguration(RetryConfiguration.None.INSTANCE);
        pnConfiguration.logVerbosity(PNLogVerbosity.NONE);
        pnConfiguration.httpLoggingInterceptor(createInterceptor());
        if (action != null) {
            action.accept(pnConfiguration);
        }
        return pnConfiguration.build();
    }

    private PNConfiguration getServerPnConfiguration(@Nullable Consumer<PNConfiguration.Builder> action) {
        final PNConfiguration.Builder pnConfiguration;
        try {
            pnConfiguration = PNConfiguration.builder(new UserId("server-".concat(UUID.randomUUID().toString())), PAM_SUB_KEY);
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
        pnConfiguration.publishKey(PAM_PUB_KEY);
        pnConfiguration.secretKey(PAM_SEC_KEY);
        pnConfiguration.logVerbosity(PNLogVerbosity.NONE);
        pnConfiguration.httpLoggingInterceptor(createInterceptor());
        if (action != null) {
            action.accept(pnConfiguration);
        }
        return pnConfiguration.build();
    }

    private HttpLoggingInterceptor createInterceptor() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(log::debug);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    protected void subscribeToChannel(@NotNull com.pubnub.api.java.PubNub pubnub, @NotNull String... channels) {
        pubnub.subscribe()
                .channels(Arrays.asList(channels))
                .withPresence()
                .execute();
        pause(1);
    }

    protected void subscribeToChannel(final com.pubnub.api.java.PubNub pubnub, final List<String> channels) {

        final AtomicBoolean subscribeSuccess = new AtomicBoolean();

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(@NotNull com.pubnub.api.java.PubNub pubnub, @NotNull PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    if (status.getAffectedChannels().containsAll(channels)) {
                        subscribeSuccess.set(true);
                    }
                } else if (status.getCategory() == PNStatusCategory.PNSubscriptionChanged) {
                    if (status.getAffectedChannels().containsAll(channels)) {
                        subscribeSuccess.set(true);
                    }
                }
            }
        });

        pubnub.subscribe()
                .channels(channels)
                .withPresence()
                .execute();

        Awaitility.await().atMost(Durations.TEN_SECONDS).untilTrue(subscribeSuccess);
    }

    protected void subscribeToChannelGroup(@NotNull com.pubnub.api.java.PubNub pubnub, @NotNull String group) {
        pubnub.subscribe()
                .channelGroups(Collections.singletonList(group))
                .withPresence()
                .execute();
        pause(1);
    }

    protected void unsubscribeFromChannel(com.pubnub.api.java.PubNub pubNub, String channel) {
        pubNub.unsubscribe()
                .channels(Collections.singletonList(channel))
                .execute();
        pause(1);
    }

    protected void unsubscribeFromAllChannels(com.pubnub.api.java.PubNub pubNub) {
        pubNub.unsubscribeAll();
        pause(1);
    }

    protected Map<String, String> generateMessage(com.pubnub.api.java.PubNub pubNub, String message) {
        final Map<String, String> map = new HashMap<>();
        map.put("publisher", pubNub.getConfiguration().getUserId().getValue());
        map.put("text", "mymsg" + RandomGenerator.newValue(5) + "+" + RandomGenerator.newValue(5));
        map.put("uncd", RandomGenerator.unicode(8));
        map.put("extra", message);
        return map;
    }

    protected JsonObject generateMessage(PubNub pubNub) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("publisher", pubNub.getConfiguration().getUserId().getValue());
        jsonObject.addProperty("text", RandomGenerator.newValue(8));
        jsonObject.addProperty("uncd", RandomGenerator.unicode(8));
        return jsonObject;
    }

    protected JsonObject generatePayload() {
        final JsonObject state = new JsonObject();
        state.addProperty("text", RandomGenerator.newValue(10));
        state.addProperty("uncd", RandomGenerator.unicode(8));
        state.addProperty("info", RandomGenerator.newValue(8));
        return state;
    }

    protected JSONObject generatePayloadJSON() {
        final JSONObject state = new JSONObject();
        try {
            state.put("text", RandomGenerator.newValue(10));
            state.put("uncd", RandomGenerator.unicode(8));
            state.put("info", RandomGenerator.newValue(8));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return state;
    }

    protected HashMap<String, Object> generateMap() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("text", RandomGenerator.newValue(10));
        map.put("uncd", RandomGenerator.unicode(8));
        map.put("info", RandomGenerator.newValue(8));
        return map;
    }

    protected void publishMessage(com.pubnub.api.java.PubNub pubNub, String channel, String message) {
        pubNub.publish()
                .message(generateMessage(pubNub, message))
                .channel(channel)
                .shouldStore(true)
                .async((result) -> {

                });
    }

    protected void publishMessage(com.pubnub.api.java.PubNub pubNub, String channel, String message, Map<String, Object> meta) {
        pubNub.publish()
                .message(generateMessage(pubNub, message))
                .channel(channel)
                .meta(meta)
                .shouldStore(true)
                .async((result) -> {

                });
    }

    protected void pause(int seconds) {
        try {
            Thread.sleep(seconds * 1_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean needsServer() {
        return provideAuthKey() != null;
    }

    protected void listen(AtomicBoolean success) {
        Awaitility.await()
                .atMost(Durations.FIVE_SECONDS)
                .with()
                .untilTrue(success);
    }
    protected void listen(AtomicBoolean success, Duration timeout) {
        Awaitility.await()
                .atMost(timeout)
                .with()
                .untilTrue(success);
    }

    protected void listen(AtomicBoolean success, Callable<Boolean> callable) {
        Awaitility.await()
                .atMost(Durations.TEN_MINUTES)
                .pollInterval(new FibonacciPollInterval(TimeUnit.SECONDS))
                .with()
                .until(callable);
    }

    protected void assertException(PubNubError pubNubError, PubNubException e) {
        assertEquals(pubNubError, e.getPubnubError());
    }

    protected void onBefore() {

    }

    protected void onAfter() {

    }

    protected void onPrePubNub() {

    }

    protected String provideAuthKey() {
        return null;
    }

    protected PNConfiguration provideStagingConfiguration(@Nullable Consumer<PNConfiguration.Builder> action) {
        return null;
    }

}

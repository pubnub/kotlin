package com.pubnub.api.integration;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import org.awaitility.Awaitility;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StreamFilteringIntegrationTests extends BaseIntegrationTest {

    private final Map<String, Object> metaEnglish = getMetaLanguageFilter("en");
    private final Map<String, Object> metaFrench = getMetaLanguageFilter("fr");
    private final Map<String, Object> metaSpanish = getMetaLanguageFilter("es");
    private final Map<String, Object> metaGerman = getMetaLanguageFilter("de");
    private final Map<String, Object> metaItalian = getMetaLanguageFilter("it");

    private final Map<String, Object> metaTemperature_25 = getMetaTempatureFilter("25");
    private final Map<String, Object> metaTemperature_35 = getMetaTempatureFilter("35");
    private final Map<String, Object> metaTemperature_55 = getMetaTempatureFilter("55");

    private final String messageEnglish = "This is just another message in English";
    private final String messageFrench = "This is just another message in French";
    private final String messageItalian = "This is just another message in Italian";
    private final String messageGerman = "This is just another message in German";
    private final String messageSpanish = "This is just another message in Spanish";

    @Override
    protected void onBefore() {

    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testSubscribeWithLanguageFiltering() {
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.getConfiguration().setFilterExpression("language == 'en'");

        final String channel = randomChannel();

        subscribeToChannel(pubNub, channel);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                assertTrue(message.getMessage().toString().contains(messageEnglish));
                success.set(true);
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {

            }

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull final PubNub pubnub, @NotNull final PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        publishMessage(pubNub, channel, messageFrench, metaFrench);
        publishMessage(pubNub, channel, messageGerman, metaGerman);
        publishMessage(pubNub, channel, messageEnglish, metaEnglish);

        listen(success);
    }

    @Test
    public void testSubscribeWithMultipleLanguageFiltering() {
        final AtomicInteger success = new AtomicInteger(0);
        pubNub.getConfiguration().setFilterExpression("('fr', 'en', 'de') contains language");

        final String channel = randomChannel();
        subscribeToChannel(pubNub, channel);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                success.incrementAndGet();
                assertFalse(message.getMessage().toString().contains("italian"));
                assertFalse(message.getMessage().toString().contains("spanish"));
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {

            }

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull PubNub pubNub, @NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        publishMessage(pubNub, channel, messageFrench, metaFrench);
        publishMessage(pubNub, channel, messageEnglish, metaEnglish);
        publishMessage(pubNub, channel, messageItalian, metaItalian);
        publishMessage(pubNub, channel, messageSpanish, metaSpanish);
        publishMessage(pubNub, channel, messageGerman, metaGerman);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(success, equalTo(3));
    }

    @Test
    public void testSubscribeWithLanguageNegationFiltering() {
        final AtomicInteger success = new AtomicInteger(0);

        pubNub.getConfiguration().setFilterExpression("language != 'en'");

        final String channel = randomChannel();
        subscribeToChannel(pubNub, channel);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                success.incrementAndGet();
                assertFalse(message.getMessage().toString().contains("english"));
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {

            }

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull PubNub pubNub, @NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        publishMessage(pubNub, channel, messageFrench, metaFrench);
        publishMessage(pubNub, channel, messageEnglish, metaEnglish);
        publishMessage(pubNub, channel, messageItalian, metaItalian);
        publishMessage(pubNub, channel, messageGerman, metaGerman);
        publishMessage(pubNub, channel, messageSpanish, metaSpanish);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(success, equalTo(4));
    }

    @Test
    public void testSubscribeWithGreaterThanFiltering() {
        final AtomicInteger success = new AtomicInteger(0);

        pubNub.getConfiguration().setFilterExpression("temperature > 50");

        final String channel = randomChannel();
        subscribeToChannel(pubNub, channel);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                success.incrementAndGet();
                assertFalse(message.getMessage().toString().contains("25"));
                assertFalse(message.getMessage().toString().contains("35"));
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {

            }

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull PubNub pubNub, @NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        final String messageTemp25 = "This is just message for today temperature : 25";
        publishMessage(pubNub, channel, messageTemp25, metaTemperature_25);
        final String messageTemp35 = "This is just message for today temperature : 35";
        publishMessage(pubNub, channel, messageTemp35, metaTemperature_35);
        final String messageTemp55 = "This is just message for today temperature : 55";
        publishMessage(pubNub, channel, messageTemp55, metaTemperature_55);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAtomic(success, equalTo(1));
    }

    @Test
    public void testSubscribeWithLikeFiltering() {
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.getConfiguration().setFilterExpression("message_part LIKE '*success*'");

        final String channel = randomChannel();
        subscribeToChannel(pubNub, channel);

        final String messageBoring = "This is just another boring message!";
        final Map<String, Object> metaMessage_1_Part = getMetaLikeFilter(messageBoring);
        final String messageSuccess = "This is just another successful message :)";
        final Map<String, Object> metaMessage_2_Part = getMetaLikeFilter(messageSuccess);

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {

            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult message) {
                boolean correctMessage = false;
                if (message.getMessage().toString().contains("success")) {
                    correctMessage = true;
                }
                assertTrue(correctMessage);
                success.set(true);
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult presence) {

            }

            @Override
            public void signal(@NotNull PubNub pubNub, @NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@NotNull PubNub pubNub, @NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {

            }
        });

        publishMessage(pubNub, channel, messageBoring, metaMessage_1_Part);
        publishMessage(pubNub, channel, messageSuccess, metaMessage_2_Part);

        listen(success);
    }

    private Map<String, Object> getMetaLanguageFilter(String metaFilter) {
        final Map<String, Object> meta = new HashMap<>();
        meta.put("language", metaFilter);
        return meta;
    }

    private Map<String, Object> getMetaTempatureFilter(String metaFilter) {
        final Map<String, Object> meta = new HashMap<>();
        meta.put("temperature", metaFilter);
        return meta;
    }

    private Map<String, Object> getMetaLikeFilter(String metaFilter) {
        final Map<String, Object> meta = new HashMap<>();
        meta.put("message_part", metaFilter);
        return meta;
    }

}

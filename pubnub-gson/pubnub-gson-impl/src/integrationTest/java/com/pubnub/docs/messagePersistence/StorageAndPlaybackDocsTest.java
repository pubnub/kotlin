package com.pubnub.docs.messagePersistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.history.PNMessageCountResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.MessagingKit;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page storage-and-playback (docs/sdks/java/api-reference/storage-and-playback.md). One test per
 * snippet. The text between a start marker and {@code snippet.end} is the published sample, the arrangement is above it and
 * the assertions below it. Every test starts from an empty history of its channels, so an exact read is exact.
 */
@ExtendWith(LiveExtension.class)
class StorageAndPlaybackDocsTest {
    private static final String OPEN = "open";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    /** Publishes {@code count} stored messages {@code label-1} ... to the channel and returns the timetokens in order. */
    private static List<Long> seed(com.pubnub.api.PubNub client, String channel, String label, int count) {
        List<Long> timetokens = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            timetokens.add(MessagingKit.publishStored(client, channel, label + "-" + i).getTimetoken());
        }
        return timetokens;
    }

    private static List<String> texts(PNHistoryResult history) {
        List<String> texts = new ArrayList<>();
        for (PNHistoryItemResult item : history.getMessages()) {
            texts.add(item.getEntry().getAsString());
        }
        return texts;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Delete messages
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void deleteMessages(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "channel_1", "channel_2");
        List<Long> first = seed(reader, "channel_1", "keep", 1);
        List<Long> second = seed(reader, "channel_2", "keep", 1);

        // snippet.deleteMessages
        pubNub.deleteMessages()
                .channels(Arrays.asList("channel_1", "channel_2"))
                .start(1460693607379L)
                .end(1460893617271L)
                .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("deleteMessages");
        // the range of the sample lies in 1970, so it deletes nothing of what was published now
        Wait.sleepToProve(1500);
        assertEquals(first, MessagingKit.storedTimetokens(reader, "channel_1"));
        assertEquals(second, MessagingKit.storedTimetokens(reader, "channel_2"));
    }

    @Test
    void deleteSpecificMessages(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "channel_1");
        List<Long> published = seed(reader, "channel_1", "message", 2);

        // snippet.deleteSpecificMessages
        pubNub.deleteMessages()
                .channels(Arrays.asList("channel_1"))
                .start(15526611838554309L)
                .end(15526611838554310L)
                .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("deleteMessages");
        // the window of the sample (start exclusive, end inclusive) holds no message of this run
        Wait.sleepToProve(1500);
        assertEquals(published, MessagingKit.storedTimetokens(reader, "channel_1"));
        // the same call with the window T-1 to T removes exactly the message T, as the page says
        long second = published.get(1);
        reader.deleteMessages(Collections.singletonList("channel_1"), second - 1, second).sync();
        Wait.settledRun("only the message outside the window is left", () ->
                assertEquals(Collections.singletonList(published.get(0)), MessagingKit.storedTimetokens(reader, "channel_1")));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Fetch messages (App samples)
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void fetchMessagesApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        MessagingKit.clearActions(reader, "my_channel");
        long timetoken = MessagingKit.publishStored(reader, "my_channel", "Message with a reaction").getTimetoken();
        PNMessageAction added = reader.addMessageAction("my_channel", new PNMessageAction("reaction", "smiley_face", timetoken)).sync();
        MessagingKit.awaitActionInHistory(reader, "my_channel", timetoken);
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.messagePersistence.samples.FetchMessagesApp.main(new String[0]);

        Wait.eventuallyRun("the sample printed the message with its action", () -> {
            out.assertPrinted("Message: \"Message with a reaction\"");
            out.assertPrinted("Timetoken: " + timetoken);
            out.assertPrinted("UUID: docs-receiver");
            out.assertPrinted("Action type: reaction");
            out.assertPrinted("Action value: smiley_face");
            out.assertPrinted("Action timetoken: " + added.getActionTimetoken());
            out.assertPrinted("Action publisher: docs-receiver");
        });
        assertTrue(out.lines("Error fetching messages").isEmpty(), "the sample reported an error");
    }

    @Test
    void FetchMessagesWithPagingApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "kotlin-ch1", "kotlin-ch2");
        List<Long> first = seed(reader, "kotlin-ch1", "paging", 2);
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.messagePersistence.samples.FetchMessagesWithPagingApp.main(new String[0]);

        // The start of the sample is a fixed timetoken of May 2025, every message of this run is newer, so the sample finds
        // nothing to print. The messages are there: the stored history lists them.
        Wait.sleepToProve(3000);
        assertTrue(out.lines("Channel kotlin-ch").isEmpty(), "the sample printed messages that are newer than its start");
        assertEquals(first, MessagingKit.storedTimetokens(reader, "kotlin-ch1"));
    }

    @Test
    void historyPagingExampleDeprecated(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "kotlin-ch1");
        List<Long> first = seed(reader, "kotlin-ch1", "page", 3);
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.messagePersistence.samples.DeprecatedHistoryApp.main(new String[0]);

        // The start of the sample is a fixed timetoken of May 2025, every message of this run is newer, so the sample finds
        // nothing to print. The messages are there: the legacy history call lists them.
        Wait.sleepToProve(3000);
        assertTrue(out.lines("page-").isEmpty(), "the sample printed messages that are newer than its start");
        assertEquals(Arrays.asList("page-1", "page-2", "page-3"), MessagingKit.historyTexts(reader, "kotlin-ch1", 10));
        assertEquals(first, MessagingKit.storedTimetokens(reader, "kotlin-ch1"));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // History (deprecated)
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void historyDeprecatedBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "history_channel");
        seed(reader, "history_channel", "basic", 3);

        // snippet.historyDeprecatedBasic
        pubNub.history()
                .channel("history_channel") // where to fetch history from
                .count(100) // how many items to fetch
                .async(result -> { /* check result */ });
        // snippet.end

        PNHistoryResult history = (PNHistoryResult) recorder.awaitOk("history");
        assertEquals(Arrays.asList("basic-1", "basic-2", "basic-3"), texts(history));
    }

    @Test
    void historyRetrieveMessagesNewerThanGivenTimetoken(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        seed(reader, "my_channel", "newer", 3);

        // snippet.historyRetrieveMessagesNewerThanGivenTimetoken
        pubNub.history()
                .channel("my_channel") // where to fetch history from
                .start(13847168620721752L) // first timestamp
                .reverse(true) // should go in reverse?
                .async(result -> {
                    result.onSuccess((PNHistoryResult res) -> {
                        for (PNHistoryItemResult pnHistoryItemResult: res.getMessages()) {
                            pnHistoryItemResult.getEntry(); // custom JSON structure for message
                        }
                    });
                });
        // snippet.end

        PNHistoryResult history = (PNHistoryResult) recorder.awaitOk("history");
        assertEquals(Arrays.asList("newer-1", "newer-2", "newer-3"), texts(history));
    }

    @Test
    void historyRetrieveMessagesUntilGivenTimetoken(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        seed(reader, "my_channel", "until", 3);

        // snippet.historyRetrieveMessagesUntilGivenTimetoken
        pubNub.history()
                .channel("my_channel") // where to fetch history from
                .count(100) // how many items to fetch
                .start(-1L) // first timestamp
                .end(13847168819178600L) // last timestamp
                .reverse(true) // should go in reverse?
                .async(result -> { /* check result */ });
        // snippet.end

        // Docs finding: the sample sends start=-1 and the service refuses it, so the call never returns the messages that the
        // page promises
        Throwable error = recorder.awaitFailure("history");
        assertTrue(error instanceof PubNubException, "the failure is not a PubNubException");
        assertEquals(400, ((PubNubException) error).getStatusCode());
        assertTrue(error.getMessage().contains("Invalid Arguments"), "the service refused the call for another reason");
    }

    @Test
    void historyThreeOldestMessages(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        seed(reader, "my_channel", "oldest", 5);

        // snippet.historyThreeOldestMessages
        pubNub.history()
                .channel("my_channel") // where to fetch history from
                .count(3) // how many items to fetch
                .reverse(true) // should go in reverse?
                .async(result -> {
                    result.onSuccess((PNHistoryResult res) -> {
                        for (PNHistoryItemResult pnHistoryItemResult: res.getMessages()) {
                            pnHistoryItemResult.getEntry(); // custom JSON structure for message
                        }
                    });
                });
        // snippet.end

        PNHistoryResult history = (PNHistoryResult) recorder.awaitOk("history");
        assertEquals(Arrays.asList("oldest-1", "oldest-2", "oldest-3"), texts(history));
    }

    @Test
    void historyWithIncludeTimetoken(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "history_channel");
        List<Long> published = seed(reader, "history_channel", "stamped", 3);

        // snippet.historyWithIncludeTimetoken
        pubNub.history()
                .channel("history_channel") // where to fetch history from
                .count(100) // how many items to fetch
                .includeTimetoken(true) // include timetoken with each entry
                .async(result -> { /* check result */ });
        // snippet.end

        PNHistoryResult history = (PNHistoryResult) recorder.awaitOk("history");
        List<Long> timetokens = new ArrayList<>();
        for (PNHistoryItemResult item : history.getMessages()) {
            Kit.assertTimetoken(item.getTimetoken());
            timetokens.add(item.getTimetoken());
        }
        assertEquals(published, timetokens);
        assertEquals(Arrays.asList("stamped-1", "stamped-2", "stamped-3"), texts(history));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Message counts
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void messageCounts(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "news");
        seed(reader, "news", "count", 3);
        long anHourAgo = (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)) * 10000L;
        Kit.eventually("the service counts the three messages", () ->
                assertEquals(Long.valueOf(3L), reader.messageCounts(Collections.singletonList("news"), Collections.singletonList(anHourAgo)).sync().getChannels().get("news")));

        // snippet.messageCounts
        Long lastHourTimetoken = (Calendar.getInstance().getTimeInMillis() - TimeUnit.HOURS.toMillis(1)) * 10000L;

        pubNub.messageCounts()
                .channels(Arrays.asList("news"))
                .channelsTimetoken(Arrays.asList(lastHourTimetoken))
                .async(result -> {
                    result.onSuccess((PNMessageCountResult res) -> {
                        for (Map.Entry<String, Long> messageCountEntry : res.getChannels().entrySet()) {
                            messageCountEntry.getKey(); // the channel name
                            messageCountEntry.getValue(); // number of messages for that channel
                        }
                    }).onFailure(( PubNubException exception) -> {
                        exception.getMessage();
                    });
                });
        // snippet.end

        PNMessageCountResult counts = (PNMessageCountResult) recorder.awaitOk("messageCounts");
        Map<String, Long> expected = new HashMap<>();
        expected.put("news", 3L);
        assertEquals(expected, counts.getChannels());
    }

    @Test
    void messageCountsDifferentTimetokenForEachChannel(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "news", "info");
        seed(reader, "news", "count", 2);
        seed(reader, "info", "count", 3);
        long anHourAgo = (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)) * 10000L;
        Map<String, Long> expected = new HashMap<>();
        expected.put("news", 2L);
        expected.put("info", 3L);
        Kit.eventually("the service counts the five messages", () ->
                assertEquals(expected, reader.messageCounts(Arrays.asList("news", "info"), Arrays.asList(anHourAgo, anHourAgo)).sync().getChannels()));

        // snippet.messageCountsDifferentTimetokenForEachChannel
        Long lastHourTimetokenForNewsChannel = (Calendar.getInstance().getTimeInMillis() - TimeUnit.HOURS.toMillis(1)) * 10000L;
        Long lastDayTimetokenForInfoChannel = (Calendar.getInstance().getTimeInMillis() - TimeUnit.DAYS.toMillis(1)) * 10000L;

        pubNub.messageCounts()
                .channels(Arrays.asList("news", "info"))
                .channelsTimetoken(Arrays.asList(lastHourTimetokenForNewsChannel, lastDayTimetokenForInfoChannel))
                .async(result -> {
                    result.onSuccess((PNMessageCountResult res) -> {
                        for (Map.Entry<String, Long> messageCountEntry : res.getChannels().entrySet()) {
                            messageCountEntry.getKey(); // the channel name
                            messageCountEntry.getValue(); // number of messages for that channel
                        }
                    }).onFailure(( PubNubException exception) -> {
                        exception.getMessage();
                    });
                });
        // snippet.end

        PNMessageCountResult counts = (PNMessageCountResult) recorder.awaitOk("messageCounts");
        assertEquals(expected, counts.getChannels());
    }
}

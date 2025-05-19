package com.pubnub.docs.messagePersistence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.history.PNMessageCountResult;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageCounts extends SnippetBase {

    private void messageCounts() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#basic-usage-2

        PubNub pubNub = createPubNub();

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
    }

    private void messageCountsDifferentTimetokenForEachChannel() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/storage-and-playback#retrieve-count-of-messages-using-different-timetokens-for-each-channel

        PubNub pubNub = createPubNub();

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
    }
}

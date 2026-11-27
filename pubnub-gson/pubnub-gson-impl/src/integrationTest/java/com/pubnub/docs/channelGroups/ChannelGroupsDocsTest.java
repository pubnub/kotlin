package com.pubnub.docs.channelGroups;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult;
import com.pubnub.docs.support.Cleanup;
import com.pubnub.docs.support.Kit;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.MessagingKit;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page channel-groups (docs/sdks/java/api-reference/channel-groups.md). One test per snippet. The
 * text between a start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions
 * below it. The second client of a test is a Kotlin client, so the Kotlin helpers work on it.
 */
@ExtendWith(LiveExtension.class)
class ChannelGroupsDocsTest {
    private static final String OPEN = "open";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    @Test
    void ChannelGroupsApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Cleanup.record("channel_group", "cg1");
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.channelGroups.samples.ChannelGroupsApp.main(new String[0]);

        Wait.eventuallyRun("the sample printed that it added the channels", () -> out.assertPrinted("Channels added to channel group successfully."));
        MessagingKit.awaitGroup(reader, "cg1", "ch1", "ch2", "ch3");
    }

    @Test
    void deleteChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(reader, "family", "son");
        MessagingKit.awaitGroup(reader, "family", "son");

        // snippet.deleteChannelGroup
        pubNub.deleteChannelGroup()
                .channelGroup("family")
                .async(result -> { /* check result */ });
        // snippet.end

        recorder.awaitOk("deleteChannelGroup");
        MessagingKit.awaitGroup(reader, "family");
    }

    @Test
    void listChannelsForChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(reader, "cg1", "ch1", "ch2", "ch3");
        MessagingKit.awaitGroup(reader, "cg1", "ch1", "ch2", "ch3");

        // snippet.listChannelsForChannelGroup
        pubNub.listChannelsForChannelGroup()
                .channelGroup("cg1")
                .async(result -> { /* check result */ });
        // snippet.end

        PNChannelGroupsAllChannelsResult listed = (PNChannelGroupsAllChannelsResult) recorder.awaitOk("listChannelsForChannelGroup");
        java.util.List<String> channels = new java.util.ArrayList<>(listed.getChannels());
        java.util.Collections.sort(channels);
        assertEquals(Arrays.asList("ch1", "ch2", "ch3"), channels);
    }

    @Test
    void removeChannelsFromChannelGroup(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        Kit.createGroup(reader, "family", "son", "daughter");
        MessagingKit.awaitGroup(reader, "family", "son", "daughter");

        // snippet.removeChannelsFromChannelGroup
        pubNub.removeChannelsFromChannelGroup()
                .channelGroup("family")
                .channels(Arrays.asList("son"))
                .sync();
        // snippet.end

        recorder.awaitOk("removeChannelsFromChannelGroup");
        MessagingKit.awaitGroup(reader, "family", "daughter");
    }
}

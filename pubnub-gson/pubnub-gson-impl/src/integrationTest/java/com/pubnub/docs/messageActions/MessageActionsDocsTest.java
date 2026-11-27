package com.pubnub.docs.messageActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.docs.support.LiveClients;
import com.pubnub.docs.support.LiveExtension;
import com.pubnub.docs.support.MessagingKit;
import com.pubnub.docs.support.Recorder;
import com.pubnub.docs.support.Stdout;
import com.pubnub.docs.support.Wait;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Live tests of the Java docs page message-actions (docs/sdks/java/api-reference/message-actions.md). One test per snippet. The
 * text between a start marker and {@code snippet.end} is the published sample, the arrangement is above it and the assertions
 * below it. An action needs a stored message, so the tests publish one first.
 */
@ExtendWith(LiveExtension.class)
class MessageActionsDocsTest {
    private static final String OPEN = "open";

    private static PubNub javaClient(LiveClients clients, String user) {
        return clients.javaClient(OPEN, user);
    }

    private static com.pubnub.api.PubNub kotlinClient(LiveClients clients, String user) {
        return clients.kotlinClient(OPEN, user);
    }

    @Test
    void getMessageActionsWithPagingApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        MessagingKit.clearActions(reader, "my_channel");
        long timetoken = MessagingKit.publishStored(reader, "my_channel", "Message with a reaction").getTimetoken();
        reader.addMessageAction("my_channel", new PNMessageAction("reaction", "smiley_face", timetoken)).sync();
        Wait.settledRun("the service lists the action", () ->
                assertEquals(1, MessagingKit.actionCount(reader, "my_channel")));
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.messageActions.samples.GetMessageActionsWithPagingApp.main(new String[0]);

        Wait.eventuallyRun("the sample printed the page of actions", () -> out.assertPrinted("Next set of actions: 1"));
        assertTrue(out.lines("Fetch error").isEmpty(), "the sample reported an error");
    }

    @Test
    void messageReactionsApp(LiveClients clients) throws Exception {
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        MessagingKit.clearActions(reader, "my_channel");
        Stdout out = Stdout.captureForTest();

        com.pubnub.docs.messageActions.samples.MessageReactionsApp.main(new String[0]);

        // Docs finding: the sample's message timetoken (2019) names no message of the keyset, and the service accepts the action
        Wait.eventuallyRun("the sample printed the new action", () -> {
            out.assertPrinted("Type: reaction");
            out.assertPrinted("Value: smiley_face");
            out.assertPrinted("UUID: demoUserId");
            out.assertPrinted("Message Timetoken: 15701761818730000");
        });
        assertTrue(out.lines("Error adding message action").isEmpty(), "the sample reported an error");
        long actionTimetoken = Long.parseLong(out.lines("Action Timetoken: ").get(0).substring("Action Timetoken: ".length()).trim());
        Wait.settledRun("the service lists the new action", () ->
                assertEquals(Collections.singletonList(actionTimetoken), MessagingKit.actionTimetokens(reader, "my_channel")));
    }

    @Test
    void getMessageActionsBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.resetHistory(reader, "my_channel");
        MessagingKit.clearActions(reader, "my_channel");
        long timetoken = MessagingKit.publishStored(reader, "my_channel", "Message with a reaction").getTimetoken();
        PNMessageAction added = reader.addMessageAction("my_channel", new PNMessageAction("reaction", "smiley_face", timetoken)).sync();
        Wait.settledRun("the service lists the action", () ->
                assertEquals(1, MessagingKit.actionCount(reader, "my_channel")));
        Stdout out = Stdout.captureForTest();

        // snippet.getMessageActionsBasic
        pubNub.getMessageActions()
                .channel("my_channel")
                .async(result -> {
                    result.onSuccess(res -> {
                        List<PNMessageAction> actions = res.getActions();
                        for (PNMessageAction action : actions) {
                            System.out.println(action.getType());
                            System.out.println(action.getValue());
                            System.out.println(action.getUuid());
                            System.out.println(action.getActionTimetoken());
                            System.out.println(action.getMessageTimetoken());
                        }
                    }).onFailure(exception -> {
                        exception.printStackTrace();
                    });
                });
        // snippet.end

        PNGetMessageActionsResult listed = (PNGetMessageActionsResult) recorder.awaitOk("getMessageActions");
        assertEquals(1, listed.getActions().size());
        PNMessageAction action = listed.getActions().get(0);
        assertEquals("reaction", action.getType());
        assertEquals("smiley_face", action.getValue());
        assertEquals("docs-receiver", action.getUuid());
        assertEquals(Long.valueOf(timetoken), action.getMessageTimetoken());
        assertEquals(added.getActionTimetoken(), action.getActionTimetoken());
        Wait.eventuallyRun("the sample printed the action", () -> {
            out.assertPrinted("smiley_face");
            out.assertPrinted("docs-receiver");
            out.assertPrinted(String.valueOf(action.getActionTimetoken()));
            out.assertPrinted(String.valueOf(timetoken));
        });
    }

    @Test
    void removeMessageActionBasic(LiveClients clients, Recorder recorder) throws Exception {
        PubNub pubNub = recorder.wrap(javaClient(clients, "yourUserId"), PubNub.class);
        com.pubnub.api.PubNub reader = kotlinClient(clients, "docs-receiver");
        MessagingKit.clearActions(reader, "my_channel");

        // snippet.removeMessageActionBasic
        pubNub.removeMessageAction()
                .channel("my_channel")
                .messageTimetoken(15701761818730000L)
                .actionTimetoken(15701775691010000L)
                .async(result -> { /* check result */ });
        // snippet.end

        // Docs finding: the two timetokens of the sample name no action, so the service answers 400 "No matching message actions
        // to delete". A control with the real timetokens of an action shows that the same call removes it.
        Throwable error = recorder.awaitFailure("removeMessageAction");
        assertTrue(error instanceof com.pubnub.api.PubNubException, "the failure is not a PubNubException");
        assertEquals(400, ((com.pubnub.api.PubNubException) error).getStatusCode());
        assertTrue(error.getMessage().contains("No matching message actions to delete"), "the service refused for another reason");
        MessagingKit.resetHistory(reader, "my_channel");
        long timetoken = MessagingKit.publishStored(reader, "my_channel", "Message with a reaction").getTimetoken();
        PNMessageAction added = reader.addMessageAction("my_channel", new PNMessageAction("reaction", "smiley_face", timetoken)).sync();
        Wait.settledRun("the service lists the action", () -> assertEquals(1, MessagingKit.actionCount(reader, "my_channel")));
        reader.removeMessageAction("my_channel", timetoken, added.getActionTimetoken()).sync();
        Wait.settledRun("the removed action is no longer listed", () -> assertEquals(0, MessagingKit.actionCount(reader, "my_channel")));
    }
}

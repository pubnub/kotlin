package com.pubnub.docs.messageReactions;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.docs.SnippetBase;
import java.util.List;

public class MessageReactionsOthers extends SnippetBase {
    private void removeMessageActionBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/message-actions#basic-usage-1

        PubNub pubNub = createPubNub();

        // snippet.removeMessageActionBasic
        pubNub.removeMessageAction()
                .channel("my_channel")
                .messageTimetoken(15701761818730000L)
                .actionTimetoken(15701775691010000L)
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void getMessageActionsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/message-actions#basic-usage-2

        PubNub pubNub = createPubNub();

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
    }
}

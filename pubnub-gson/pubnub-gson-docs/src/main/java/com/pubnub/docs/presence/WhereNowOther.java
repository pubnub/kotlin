package com.pubnub.docs.presence;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.models.consumer.presence.PNWhereNowResult;
import com.pubnub.docs.SnippetBase;

public class WhereNowOther extends SnippetBase {
    private void getListOfChannelsUuidIsSubscribeTo() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#get-a-list-of-channels-a-uuid-is-subscribed-to

        PubNub pubNub = createPubNub();

        // snippet.getListOfChannelsUuidIsSubscribeTo
        pubNub.whereNow().async(result -> {
            result.onSuccess((PNWhereNowResult res) -> {
                System.out.println("User is currently subscribed to following channels: " + res.getChannels());
            }).onFailure((PubNubException exception) -> {
                // Handle errors if the request fails
                System.err.println("Error retrieving whereNow data: " + exception.getMessage());
                exception.getPubnubError();
                exception.getCause();
                exception.getStatusCode();
            });
        });
        // snippet.end
    }

    private void getListOfChannelsOtherUserIsSubscribeTo() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/presence#obtain-information-about-the-current-list-of-channels-of-some-other-uuid

        PubNub pubNub = createPubNub();

        // snippet.getListOfChannelsOtherUserIsSubscribeTo
        pubNub.whereNow().uuid("some-other-uuid").async(result -> {
            result.onSuccess((PNWhereNowResult res) -> {
                System.out.println("some-other-uuid is currently subscribed to following channels: " + res.getChannels());
            }).onFailure((PubNubException exception) -> {
                // Handle errors if the request fails
                System.err.println("Error retrieving whereNow data: " + exception.getMessage());
                exception.getPubnubError();
                exception.getCause();
                exception.getStatusCode();
            });
        });
        // snippet.end
    }
}

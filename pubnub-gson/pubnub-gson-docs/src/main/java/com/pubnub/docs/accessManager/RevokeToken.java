package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.docs.SnippetBase;

public class RevokeToken extends SnippetBase {
    private void revokeToken() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#basic-usage-2

        PubNub pubnub = createPubNub();

        // snippet.revokeToken
        pubnub.revokeToken()
            .token("p0thisAkFl043rhDdHRsCkNyZXisRGNoYW6hanNlY3JldAFDZ3Jwsample3KgQ3NwY6BDcGF0pERjaGFuoENnctokenVzcqBDc3BjoERtZXRhoENzaWdYIGOAeTyWGJI")
            .async(result -> { /* check result */ });
        // snippet.end
    }
}

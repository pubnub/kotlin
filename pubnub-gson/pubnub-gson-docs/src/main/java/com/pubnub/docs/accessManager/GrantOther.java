package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.docs.SnippetBase;

import java.util.Collections;

public class GrantOther extends SnippetBase {
    private void grantWithGetAllMetadataAccess() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager-v2#category-permissions

        PubNub pubnub = createPubNub();

        // snippet.grantWithGetAllMetadataAccess
        // Allow the auth keys to list all channel and uuid metadata on the keyset.
        // The `get` permission is sent automatically, so no other permission can be set.
        pubnub.grant()
                .authKeys(Collections.singletonList("my-auth-key"))
                .getAllChannels(true)
                .getAllUUIDs(true)
                .async(result -> { /* check result */ });
        // snippet.end
    }
}

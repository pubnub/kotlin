package com.pubnub.docs.appContext;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import com.pubnub.docs.SnippetBase;

public class UserMetadataOthers extends SnippetBase {
    private void getUUIDMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-1

        PubNub pubNub = createPubNub();

        // snippet.getUUIDMetadataBasic
        pubNub.getUUIDMetadata().async(result -> { /* check result */ });
        // snippet.end
    }

    private void setUUIDMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-2

        PubNub pubNub = createPubNub();

        // snippet.setUUIDMetadataBasic
        PNSetUUIDMetadataResult pnSetUUIDMetadataResult = pubNub.setUUIDMetadata()
                .name("Foo")
                .profileUrl("http://example.com")
                .email("foo@example.com")
                .includeCustom(true)
                .sync();
        // snippet.end
    }

    private void removeUUIDMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-3

        PubNub pubNub = createPubNub();

        // snippet.removeUUIDMetadataBasic
        pubNub.removeUUIDMetadata().async(result -> { /* check result */ });
        // snippet.end
    }
}

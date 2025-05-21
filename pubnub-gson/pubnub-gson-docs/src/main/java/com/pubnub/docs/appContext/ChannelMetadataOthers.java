package com.pubnub.docs.appContext;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.docs.SnippetBase;

public class ChannelMetadataOthers extends SnippetBase {
    private void getAllChannelsMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-4

        PubNub pubNub = createPubNub();

        // snippet.getAllChannelsMetadataBasic
        PNGetAllChannelsMetadataResult pnGetAllChannelsMetadataResult = pubNub.getAllChannelsMetadata().sync();
        // snippet.end
    }

    private void getChannelMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-5

        PubNub pubNub = createPubNub();

        // snippet.getChannelMetadataBasic
        PNGetChannelMetadataResult pnGetChannelMetadataResult = pubNub.getChannelMetadata()
                .channel("myChannel")
                .sync();
        // snippet.end

    }

    private void setChannelMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-6

        PubNub pubNub = createPubNub();

        // snippet.setChannelMetadataBasic
        pubNub.setChannelMetadata()
                .channel("myChannel")
                .name("Some Name")
                .includeCustom(true)
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void removeChannelMetadataBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-7

        PubNub pubNub = createPubNub();

        // snippet.removeChannelMetadataBasic
        pubNub.removeChannelMetadata()
                .channel("myChannel")
                .async(result -> { /* check result */ });
        // snippet.end
    }
}

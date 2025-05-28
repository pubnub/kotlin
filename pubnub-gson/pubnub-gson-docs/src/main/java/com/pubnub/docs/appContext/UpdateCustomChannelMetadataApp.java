package com.pubnub.docs.appContext;
// https://www.pubnub.com/docs/sdks/java/api-reference/objects#iteratively-update-existing-metadata

// snippet.iterativelyUpdateExistingMetadataMain
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.api.java.v2.PNConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;



public class UpdateCustomChannelMetadataApp {
    public static void main(String[] args) throws PubNubException{
        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("user01"), "demo").publishKey("demo");

        PubNub pubnub = PubNub.create(configBuilder.build());

        final String channel = "channelId";
        final String channelName = "Channel1on1";
        final String channelDescription = "Channel for 1on1 conversation";
        final String status = "active";
        final String type = "1on1";
        final Map initialCustom = new HashMap();
        initialCustom.put("Days", "Mon-Fri");

        final PNSetChannelMetadataResult setChannelMetadataResult = pubnub.setChannelMetadata()
                .channel(channel)
                .description(channelDescription)
                .name(channelName)
                .custom(initialCustom)
                .includeCustom(true)
                .status(status)
                .type(type)
                .sync();

        final PNChannelMetadata channelMetadataAfterGet = pubnub.getChannelMetadata()
                .channel(channel)
                .includeCustom(true)
                .sync().getData();

        Map<String, Object> updatedCustomMetadata = channelMetadataAfterGet.getCustom().getValue();
        Map<String, Object> additionalMetadata = new HashMap<>();
        additionalMetadata.put("Months", "Jan-May");

        updatedCustomMetadata.putAll(additionalMetadata);

        final PNSetChannelMetadataResult updatedChannelMetadata = pubnub.setChannelMetadata()
                .channel(channel)
                .custom(updatedCustomMetadata)
                .includeCustom(true)
                .sync();

        PNChannelMetadata updatedData = updatedChannelMetadata.getData();

        assertThat(updatedData.getId(),               is(equalTo(channel)));
        assertThat(updatedData.getName().getValue(),  is(equalTo(channelName)));
        assertThat(updatedData.getDescription().getValue(), is(equalTo(channelDescription)));
        assertThat(updatedData.getStatus().getValue(), is(equalTo(status)));
        assertThat(updatedData.getType().getValue(),   is(equalTo(type)));

        Map<String, Object> expectedCustom = new HashMap<>();
        expectedCustom.put("Days",   "Mon-Fri");
        expectedCustom.put("Months", "Jan-May");
        assertThat(updatedData.getCustom().getValue(), is(equalTo(expectedCustom)));

        // clean up
        pubnub.removeChannelMetadata().channel(channel).sync();
        System.exit(0);
    }
}
// snippet.end

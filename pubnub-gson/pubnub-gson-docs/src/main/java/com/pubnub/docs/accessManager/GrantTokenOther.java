package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Collections;

public class GrantTokenOther extends SnippetBase {
    private void grantTokenDifferentAccessLevels() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-in-a-single-call

        PubNub pubNub = createPubNub();

        // snippet.grantTokenDifferentAccessLevels
        PNGrantTokenResult pnGrantTokenResult = pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.name("channel-a").read(),
                        ChannelGrant.name("channel-b").read().write(),
                        ChannelGrant.name("channel-c").read().write(),
                        ChannelGrant.name("channel-d").read().write()))
                .channelGroups(Arrays.asList(
                        ChannelGroupGrant.id("channel-group-b").read()))
                .uuids(Arrays.asList(
                        UUIDGrant.id("uuid-c").get(),
                        UUIDGrant.id("uuid-d").get().update()))
                .sync();

        String token = pnGrantTokenResult.getToken();
        // snippet.end
    }

    private void grantTokenWithRegEx() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-an-authorized-client-read-access-to-multiple-channels-using-regex

        PubNub pubNub = createPubNub();

        // snippet.grantTokenWithRegEx
        pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.pattern("^channel-[A-Za-z0-9]*$").read()))
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void grantTokenDifferentAccessLevelsWithRegEx() throws PubNubException {
        //https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-and-read-access-to-spaces-using-regex-in-a-single-call

        PubNub pubNub = createPubNub();

        // snippet.grantTokenDifferentAccessLevelsWithRegEx
        pubNub.grantToken(15)
                .authorizedUUID("my-authorized-uuid")
                .channels(Arrays.asList(
                        ChannelGrant.name("channel-a").read(),
                        ChannelGrant.name("channel-b").read().write(),
                        ChannelGrant.name("channel-c").read().write(),
                        ChannelGrant.name("channel-d").read().write(),
                        ChannelGrant.pattern("^channel-[A-Za-z0-9]*$").read()))
                .channelGroups(Collections.singletonList(
                        ChannelGroupGrant.id("channel-group-b").read()))
                .uuids(Arrays.asList(
                        UUIDGrant.id("uuid-c").get(),
                        UUIDGrant.id("uuid-d").get().update()))
                .async(result -> { /* check result */ });
        // snippet.end
    }
}

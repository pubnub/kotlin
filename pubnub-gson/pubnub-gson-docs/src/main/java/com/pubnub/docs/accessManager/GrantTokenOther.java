package com.pubnub.docs.accessManager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.SpaceId;
import com.pubnub.api.java.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.java.models.consumer.access_manager.sum.UserPermissions;
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
        PNGrantTokenResult pnGrantTokenResult = pubNub.grantToken()
                .ttl(15)
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
        pubNub.grantToken()
                .ttl(15)
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
        pubNub.grantToken()
                .ttl(15)
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

    private void grantTokenUsersAndSpacesBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#basic-usage-1

        PubNub pubnub = createPubNub();

        // snippet.grantTokenUsersAndSpacesBasic
        PNGrantTokenResult pnGrantTokenResult = pubnub.grantToken()
                .ttl(15)
                .authorizedUserId(new UserId("my-authorized-userId"))
                .spacesPermissions(Arrays.asList(SpacePermissions.id(new SpaceId("space-id")).read()))
                .sync();

        String token = pnGrantTokenResult.getToken();
        // snippet.end
    }

    private void grantTokenUsersAndSpacesDifferentLevels() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-in-a-single-call-1

        PubNub pubnub = createPubNub();

        // snippet.grantTokenUsersAndSpacesDifferentLevels
        pubnub.grantToken()
                .ttl(15)
                .authorizedUserId(new UserId("my-authorized-userId"))
                .spacesPermissions(Arrays.asList(
                        SpacePermissions.id(new SpaceId("space-a")).read(),
                        SpacePermissions.id(new SpaceId("space-b")).read().write(),
                        SpacePermissions.id(new SpaceId("space-c")).read().write(),
                        SpacePermissions.id(new SpaceId("space-d")).read().write()))
                .usersPermissions(Arrays.asList(
                        UserPermissions.id(new UserId("userId-c")).get(),
                        UserPermissions.id(new UserId("userId-d")).get().update()))
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void grantTokenUsersAndSpacesWithRegEx() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-an-authorized-client-read-access-to-multiple-channels-using-regex-1

        PubNub pubnub = createPubNub();

        // snippet.grantTokenUsersAndSpacesWithRegEx
        pubnub.grantToken()
                .ttl(15)
                .authorizedUserId(new UserId("my-authorized-userId"))
                .spacesPermissions(Collections.singletonList(
                        SpacePermissions.pattern("^space-[A-Za-z0-9]*$").read()))
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void grantTokenUsersAndSpacesDifferentLevelsWithRegEx() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/access-manager#grant-an-authorized-client-different-levels-of-access-to-various-resources-and-read-access-to-channels-using-regex-in-a-single-call
        PubNub pubnub = createPubNub();

        // snippet.grantTokenUsersAndSpacesDifferentLevelsWithRegEx
        PNGrantTokenResult pnGrantTokenResult = pubnub.grantToken()
                .ttl(15)
                .authorizedUserId(new UserId("my-authorized-userId"))
                .spacesPermissions(Arrays.asList(
                        SpacePermissions.id(new SpaceId("space-a")).read(),
                        SpacePermissions.id(new SpaceId("space-b")).read().write(),
                        SpacePermissions.id(new SpaceId("space-c")).read().write(),
                        SpacePermissions.id(new SpaceId("space-d")).read().write(),
                        SpacePermissions.pattern("^space-[A-Za-z0-9]*$").read()))
                .usersPermissions(Arrays.asList(
                        UserPermissions.id(new UserId("userId-c")).get(),
                        UserPermissions.id(new UserId("userId-d")).get().update()))
                .sync();

        String token = pnGrantTokenResult.getToken();

        // snippet.end
    }
}

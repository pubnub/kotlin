package com.pubnub.docs.appContext;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUser;
import com.pubnub.docs.SnippetBase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelMembersOthers extends SnippetBase {
    private void getChannelMembersBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-12

        PubNub pubNub = createPubNub();

        // snippet.getChannelMembersBasic
        PNGetChannelMembersResult pnGetChannelMembersResult = pubNub.getChannelMembers("testChannelId")
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sort(Arrays.asList(PNSortKey.desc(PNSortKey.Key.STATUS)))
                .sync();
        // snippet.end
    }

    private void setChannelMembersBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-13

        PubNub pubNub = createPubNub();

        // snippet.setChannelMembersBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("members_param1", "val1");
        customMap.putIfAbsent("members_param2", "val2");

        final Collection<PNUser> channelMembers = Arrays.asList(
                PNUser.builder("userId1").status("status01").type("typeR").build(),
                PNUser.builder("userId2").custom(customMap).status("status02").type("typeS").build());

        pubNub.setChannelMembers("testChannelId", channelMembers)
                .include(MemberInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .includeUserStatus(true)
                        .includeUserType(true)
                        .build())
                .sort(Arrays.asList(PNSortKey.asc(PNSortKey.Key.STATUS)))
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void removeChannelMembersBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-14

        PubNub pubNub = createPubNub();

        // snippet.removeChannelMembersBasic
        pubNub.removeChannelMembers("channelId", Collections.singletonList("userId"))
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void manageChannelMembersBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-15

        PubNub pubNub = createPubNub();

        // snippet.manageChannelMembersBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("members_param1", "val1");
        customMap.putIfAbsent("members_param2", "val2");

        final List<PNUser> channelMembersToSet = Collections.singletonList(
                PNUser.builder("userId02")
                        .custom(customMap)
                        .status("status02")
                        .type("type02")
                        .build());
        final List<String> channelMembersIdsToRemove = Collections.singletonList("userId01");

        pubNub.manageChannelMembers("channelId", channelMembersToSet, channelMembersIdsToRemove)
                .include(MemberInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .includeUserStatus(true)
                        .includeUserType(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end
    }
}

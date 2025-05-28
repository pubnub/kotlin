package com.pubnub.docs.appContext;

import com.pubnub.api.PubNubException;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import com.pubnub.docs.SnippetBase;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelMembershipsOthers extends SnippetBase {
    private void getMembershipsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-8

        PubNub pubNub = createPubNub();

        // snippet.getMembershipsBasic
        PNGetMembershipsResult pnGetMembershipsResult = pubNub.getMemberships()
                .userId("userId01")
                .limit(10)
                .include(MembershipInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .includeChannelType(true)
                        .includeChannelStatus(true)
                        .build())
                .sort(Arrays.asList(PNSortKey.asc(PNSortKey.Key.TYPE)))
                .sync();
        // snippet.end
    }

    private void getMembershipsWithPagination() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-with-pagination

        PubNub pubNub = createPubNub();

        // snippet.getMembershipsWithPagination
        final PNGetMembershipsResult getMembershipsResult = pubNub.getMemberships()
                .limit(3)
                .include(MembershipInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeChannel(true)
                        .build())
                .sync();

        if (getMembershipsResult.getNext() != null) {
            final PNGetMembershipsResult getMembershipsNextPageResult = pubNub.getMemberships()
                    .page(getMembershipsResult.nextPage())
                    .limit(3)
                    .include(MembershipInclude.builder()
                            .includeCustom(true)
                            .includeStatus(true)
                            .includeType(true)
                            .includeTotalCount(true)
                            .includeChannel(true)
                            .build())
                    .sync();
            System.out.println(getMembershipsNextPageResult);
        }
        // snippet.end
    }

    private void setMembershipsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-9

        PubNub pubNub = createPubNub();

        // snippet.setMembershipsBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");


        PNChannelMembership pnChannelMembership = PNChannelMembership.builder("myChannelId")
                .custom(customMap)
                .status("inactive")
                .type("member")
                .build();

        pubNub.setMemberships(Arrays.asList(pnChannelMembership))
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end
    }

    private void removeMembershipsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-10

        PubNub pubNub = createPubNub();

        // snippet.removeMembershipsBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");

        PNRemoveMembershipResult pnRemoveMembershipResult = pubNub.removeMemberships(Arrays.asList("myChannelName"))
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeType(true)
                        .includeStatus(true)
                        .build())
                .sync();
        // snippet.end
    }

    private void manageMembershipsBasic() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/objects#basic-usage-11

        PubNub pubNub = createPubNub();

        // snippet.manageMembershipsBasic
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");

        final List<PNChannelMembership> channelMembershipsToSet = Collections.singletonList(
                PNChannelMembership.builder("channelId02")
                        .custom(customMap)
                        .status("inactive")
                        .type("member")
                        .build());
        final List<String> channelIdsToRemove = Collections.singletonList("channelId01");

        pubNub.manageMemberships(channelMembershipsToSet, channelIdsToRemove)
                .userId("userId01")
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .build())
                .async(result -> { /* check result */ });
        // snippet.end
    }
}

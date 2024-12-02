package com.pubnub.api.java.endpoints.objects_api.memberships;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;

public interface GetMemberships extends Endpoint<PNGetMembershipsResult> {
    /**
     * Set the user ID for fetching memberships.
     *
     * @param userId the user ID
     * @return the current instance of {@code GetMemberships}
     */
    GetMemberships userId(String userId);

    /**
     * @deprecated Use {@link #userId(String)} instead.
     * @param uuid the user ID (previously referred to as UUID)
     * @return the current instance of {@code GetMemberships}
     */
    @Deprecated()
    GetMemberships uuid(String uuid);

    GetMemberships limit(Integer limit);

    GetMemberships page(com.pubnub.api.models.consumer.objects.PNPage page);

    GetMemberships filter(String filter);

    GetMemberships sort(java.util.Collection<PNSortKey> sort);

    /**
     * @deprecated Use {@link #include(MembershipInclude)} instead.
     * @param includeTotalCount specifies in totalCount should be included in response
     * @return the current instance of {@code GetMemberships}
     */
    @Deprecated()
    GetMemberships includeTotalCount(boolean includeTotalCount);

    /**
     * @deprecated Use {@link #include(MembershipInclude)} instead.
     * @param includeCustom specifies in Custom data should be included in response
     * @return the current instance of {@code GetMemberships}
     */
    @Deprecated()
    GetMemberships includeCustom(boolean includeCustom);

    /**
     * @deprecated Use {@link #include(MembershipInclude)} instead.
     * @param includeChannel specifies in ChannelMetadata should be included in response
     * @return the current instance of {@code GetMemberships}
     */
    @Deprecated()
    GetMemberships includeChannel(Include.PNChannelDetailsLevel includeChannel);

    GetMemberships include(MembershipInclude include);
}

package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResult;

import java.util.Collection;

@Deprecated
public interface RemoveChannelMembers extends Endpoint<PNRemoveChannelMembersResult> {

    RemoveChannelMembers limit(Integer limit);

    RemoveChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    RemoveChannelMembers filter(String filter);

    RemoveChannelMembers sort(Collection<PNSortKey> sort);

    RemoveChannelMembers includeTotalCount(boolean includeTotalCount);

    RemoveChannelMembers includeCustom(boolean includeCustom);

    RemoveChannelMembers includeUUID(Include.PNUUIDDetailsLevel includeUUID);

    @Deprecated
    interface Builder extends BuilderSteps.ChannelStep<ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers>> {
        @Override
        ObjectsBuilderSteps.UUIDsStep<RemoveChannelMembers> channel(String channel);
    }
}

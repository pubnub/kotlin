package com.pubnub.api.endpoints.objects_api.members;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.models.consumer.objects_api.member.PNManageChannelMembersResult;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;

import java.util.Collection;

public interface ManageChannelMembers extends Endpoint<PNManageChannelMembersResult> {
    ManageChannelMembers limit(Integer limit);

    ManageChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    ManageChannelMembers filter(String filter);

    ManageChannelMembers sort(Collection<com.pubnub.api.endpoints.objects_api.utils.PNSortKey> sort);

    ManageChannelMembers includeTotalCount(boolean includeTotalCount);

    ManageChannelMembers includeCustom(boolean includeCustom);

    ManageChannelMembers includeUUID(com.pubnub.api.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel includeUUID);

    interface Builder extends ObjectsBuilderSteps.ChannelStep<ObjectsBuilderSteps.RemoveOrSetStep<ManageChannelMembers, PNUUID>> {
        @Override
        ObjectsBuilderSteps.RemoveOrSetStep<ManageChannelMembers, PNUUID> channel(String channel);
    }
}

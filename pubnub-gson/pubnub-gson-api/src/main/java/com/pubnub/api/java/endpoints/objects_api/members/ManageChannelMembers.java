package com.pubnub.api.java.endpoints.objects_api.members;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.endpoints.objects_api.utils.Include;
import com.pubnub.api.java.endpoints.objects_api.utils.ObjectsBuilderSteps;
import com.pubnub.api.java.endpoints.objects_api.utils.PNSortKey;
import com.pubnub.api.java.models.consumer.objects_api.member.PNManageChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUUID;

import java.util.Collection;

public interface ManageChannelMembers extends Endpoint<PNManageChannelMembersResult> {
    ManageChannelMembers limit(Integer limit);

    ManageChannelMembers page(com.pubnub.api.models.consumer.objects.PNPage page);

    ManageChannelMembers filter(String filter);

    ManageChannelMembers sort(Collection<PNSortKey> sort);

    ManageChannelMembers includeTotalCount(boolean includeTotalCount);

    ManageChannelMembers includeCustom(boolean includeCustom);

    ManageChannelMembers includeUUID(Include.PNUUIDDetailsLevel includeUUID);

    interface Builder extends ObjectsBuilderSteps.ChannelStep<ObjectsBuilderSteps.RemoveOrSetStep<ManageChannelMembers, PNUUID>> {
        @Override
        ObjectsBuilderSteps.RemoveOrSetStep<ManageChannelMembers, PNUUID> channel(String channel);
    }
}

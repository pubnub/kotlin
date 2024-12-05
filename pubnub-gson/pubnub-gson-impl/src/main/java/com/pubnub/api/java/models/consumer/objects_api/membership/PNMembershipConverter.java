package com.pubnub.api.java.models.consumer.objects_api.membership;

import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadataConverter;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PNMembershipConverter {
    static List<PNMembership> from(Collection<PNChannelMembership> members) {
        ArrayList<PNMembership> list = new ArrayList<>(members.size());
        for (PNChannelMembership member : members) {
            list.add(from(member));
        }
        return list;
    }

    @Nullable
    static PNMembership from(@Nullable PNChannelMembership data) {
        if (data == null) {
            return null;
        }
        PNChannelMetadata metadata = PNChannelMetadataConverter.from(data.getChannel());
        PNMembership newData = new PNMembership(metadata);
        newData.setCustom(data.getCustom());
//        newData.setUuid(data.get) //TODO where to get this? does it even exist in server responses?
        newData.setUpdated(data.getUpdated());
        newData.setETag(data.getETag());
        newData.setStatus(data.getStatus());
        newData.setType(data.getType());
        return newData;
    }
}

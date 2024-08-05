package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.objects_api.channel.ConvertersKt;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembership;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class PNMembership {
    @NonNull
    private PNChannelMetadata channel;
    private Object custom;
    private String uuid;
    private String updated;
    private String eTag;
    private String status;

    public static List<PNMembership> from(Collection<PNChannelMembership> members) {
        ArrayList<PNMembership> list = new ArrayList<>(members.size());
        for (PNChannelMembership member : members) {
            list.add(from(member));
        }
        return list;
    }

    @Nullable
    public static PNMembership from(@Nullable PNChannelMembership data) {
        if (data == null) {
            return null;
        }
        PNChannelMetadata metadata = ConvertersKt.from(data.getChannel());
        PNMembership newData = new PNMembership(metadata);
        newData.setCustom(data.getCustom());
//        newData.setUuid(data.get) //TODO where to get this? does it even exist in server responses?
        newData.setUpdated(data.getUpdated());
        newData.setETag(data.getETag());
        newData.setStatus(data.getStatus());
        return newData;
    }
}

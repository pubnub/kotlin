package com.pubnub.api.java.models.consumer.objects_api.membership;

import com.pubnub.api.java.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership;
import com.pubnub.api.utils.PatchValue;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
public class PNMembership {
    public PNMembership(@NotNull PNChannelMetadata channel) {
        this.channel = channel;
    }
    @NonNull
    private PNChannelMetadata channel;
    private PatchValue<@Nullable Object> custom;
    private String uuid;
    private String updated;
    private String eTag;
    private PatchValue<@Nullable String> status;

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
        PNChannelMetadata metadata = PNChannelMetadata.from(data.getChannel());
        PNMembership newData = new PNMembership(metadata);
        newData.setCustom(data.getCustom() != null ? PatchValue.of(data.getCustom().getValue()) : null);
//        newData.setUuid(data.get) //TODO where to get this? does it even exist in server responses?
        newData.setUpdated(data.getUpdated());
        newData.setETag(data.getETag());
        newData.setStatus(data.getStatus());
        return newData;
    }
}

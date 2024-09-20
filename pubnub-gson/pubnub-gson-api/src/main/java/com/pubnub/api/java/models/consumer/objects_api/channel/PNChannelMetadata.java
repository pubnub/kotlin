package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.pubnub.api.java.models.consumer.objects_api.PNObject;
import com.pubnub.api.utils.PatchValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PNChannelMetadata extends PNObject {
    private PatchValue<@Nullable String> name;
    private PatchValue<@Nullable String> description;
    private PatchValue<@Nullable String> type;
    private PatchValue<@Nullable String> status;

    public PNChannelMetadata(String id, PatchValue<@Nullable String> name, PatchValue<@Nullable String> description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public PNChannelMetadata(String id, PatchValue<@Nullable String> name) {
        this(id, name, null);
    }

    @Override
    public PNChannelMetadata setCustom(PatchValue<@Nullable Map<String, Object>> custom) {
        super.setCustom(custom);
        return this;
    }

    @NotNull
    public static PNChannelMetadata from(@NotNull com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata data) {
        PNChannelMetadata newData = new PNChannelMetadata(
                data.getId(),
                data.getName(),
                data.getDescription()
        );
        newData.setETag(data.getETag());
        newData.setType(data.getType());
        newData.setStatus(data.getStatus());
        newData.setCustom(data.getCustom());
        newData.setUpdated(data.getUpdated());
        return newData;
    }

    public static List<PNChannelMetadata> from(Collection<com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata> data) {
        List<PNChannelMetadata> channels = new ArrayList<>(data.size());
        for (com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata datum : data) {
            channels.add(from(datum));
        }
        return channels;
    }
}

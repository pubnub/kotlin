package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PNChannelMetadata extends PNObject {
    private String name;
    private String description;
    private String type;
    private String status;

    public PNChannelMetadata(String id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public PNChannelMetadata(String id, String name) {
        this(id, name, null);
    }

    @Override
    public PNChannelMetadata setCustom(Object custom) {
        super.setCustom(custom);
        return this;
    }

    @Nullable
    public static PNChannelMetadata from(@Nullable com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata data) {
        if (data == null) {
            return null;
        }
        PNChannelMetadata newData = new PNChannelMetadata(
                data.getId(), data.getName(), data.getDescription()
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
            channels.add(PNChannelMetadata.from(datum));
        }
        return channels;
    }
}

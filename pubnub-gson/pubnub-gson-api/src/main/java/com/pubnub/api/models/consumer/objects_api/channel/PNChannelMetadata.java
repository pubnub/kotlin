package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public static PNChannelMetadata from(@NotNull com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata data) {
        PNChannelMetadata newData = new PNChannelMetadata(
                data.getId(),
                data.getName() != null ? data.getName().getValue() : null,
                data.getDescription() != null ? data.getDescription().getValue() : null
        );
        newData.setETag(data.getETag() != null ? data.getETag().getValue() : null);
        newData.setType(data.getType() != null ? data.getType().getValue() : null);
        newData.setStatus(data.getStatus() != null ? data.getStatus().getValue() : null);
        newData.setCustom(data.getCustom() != null ? data.getCustom().getValue() : null);
        newData.setUpdated(data.getUpdated() != null ? data.getUpdated().getValue() : null);
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

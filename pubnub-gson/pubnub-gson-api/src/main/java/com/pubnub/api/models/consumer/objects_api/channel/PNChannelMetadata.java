package com.pubnub.api.models.consumer.objects_api.channel;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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

    protected PNChannelMetadata(String id, String name, String description, Object custom, String updated, String eTag, String type, String status) {
        super(id);
        this.name = name;
        this.description = description;
        this.custom = custom;
        this.updated = updated;
        this.eTag = eTag;
        this.type = type;
        this.status = status;
    }

    @Override
    public PNChannelMetadata setCustom(Object custom) {
        super.setCustom(custom);
        return this;
    }

    public static List<PNChannelMetadata> from(Collection<com.pubnub.api.models.consumer.objects.channel.NewPNChannelMetadata> data) {
        List<PNChannelMetadata> channels = new ArrayList<>(data.size());
        for (com.pubnub.api.models.consumer.objects.channel.NewPNChannelMetadata datum : data) {
            channels.add(ConvertersKt.from(datum));
        }
        return channels;
    }
}

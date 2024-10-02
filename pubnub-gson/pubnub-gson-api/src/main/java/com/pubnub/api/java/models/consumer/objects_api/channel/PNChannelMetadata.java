package com.pubnub.api.java.models.consumer.objects_api.channel;

import com.pubnub.api.java.models.consumer.objects_api.PNObject;
import com.pubnub.api.utils.PatchValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

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
}

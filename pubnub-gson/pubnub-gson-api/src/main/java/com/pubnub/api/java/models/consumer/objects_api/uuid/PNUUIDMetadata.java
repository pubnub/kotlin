package com.pubnub.api.java.models.consumer.objects_api.uuid;

import com.pubnub.api.java.models.consumer.objects_api.PNObject;
import com.pubnub.api.utils.PatchValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class PNUUIDMetadata extends PNObject {
    private PatchValue<@Nullable String> name;
    private PatchValue<@Nullable String> email;
    private PatchValue<@Nullable String> externalId;
    private PatchValue<@Nullable String> profileUrl;
    private PatchValue<@Nullable String> type;
    private PatchValue<@Nullable String> status;

    public PNUUIDMetadata(String id, PatchValue<@Nullable String> name) {
        super(id);
        this.name = name;
    }

    @Override
    public PNUUIDMetadata setCustom(PatchValue<@Nullable Map<String, Object>> custom) {
        super.setCustom(custom);
        return this;
    }
}


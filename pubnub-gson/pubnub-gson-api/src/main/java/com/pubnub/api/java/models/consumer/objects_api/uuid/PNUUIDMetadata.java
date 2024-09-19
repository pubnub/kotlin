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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public PNUUIDMetadata setCustom(PatchValue<@Nullable Object> custom) {
        super.setCustom(custom);
        return this;
    }

    public static List<PNUUIDMetadata> from(Collection<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata> data) {
        ArrayList<PNUUIDMetadata> list = new ArrayList<>(data.size());
        for (com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata datum : data) {
            list.add(from(datum));
        }
        return list;
    }

    @Nullable
    public static PNUUIDMetadata from(com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata data) {
        if (data == null) {
            return null;
        }
        PNUUIDMetadata newData = new PNUUIDMetadata(data.getId(), data.getName())
                .setProfileUrl(data.getProfileUrl())
                .setEmail(data.getEmail())
                .setExternalId(data.getExternalId())
                .setStatus(data.getStatus())
                .setType(data.getType())
                .setCustom(data.getCustom() != null ? PatchValue.of(data.getCustom().getValue()) : null);

        newData.setETag(data.getETag());
        newData.setUpdated(data.getUpdated());
        return newData;
    }
}


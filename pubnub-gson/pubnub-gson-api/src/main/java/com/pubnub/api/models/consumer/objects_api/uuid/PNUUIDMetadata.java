package com.pubnub.api.models.consumer.objects_api.uuid;

import com.pubnub.api.models.consumer.objects_api.PNObject;
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
    private String name;
    private String email;
    private String externalId;
    private String profileUrl;
    private String type;
    private String status;

    public PNUUIDMetadata(String id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public PNUUIDMetadata setCustom(Object custom) {
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
        PNUUIDMetadata newData = new PNUUIDMetadata(data.getId(), data.getName() != null ? data.getName().getValue() : null)
                .setProfileUrl(data.getProfileUrl() != null ? data.getProfileUrl().getValue() : null)
                .setEmail(data.getEmail() != null ? data.getEmail().getValue() : null)
                .setExternalId(data.getExternalId() != null ? data.getExternalId().getValue() : null)
                .setStatus(data.getStatus() != null ? data.getStatus().getValue() : null)
                .setType(data.getType() != null ? data.getType().getValue() : null)
                .setCustom(data.getCustom() != null ? data.getCustom().getValue() : null);

        newData.setETag(data.getETag() != null ? data.getETag().getValue() : null);
        newData.setUpdated(data.getUpdated() != null ? data.getUpdated().getValue() : null);
        return newData;
    }
}


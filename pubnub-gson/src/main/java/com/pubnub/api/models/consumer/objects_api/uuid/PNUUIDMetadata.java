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

    public static List<PNUUIDMetadata> from(Collection<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata> data) {
        ArrayList<PNUUIDMetadata> list = new ArrayList<>(data.size());
        for (com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata datum : data) {
            list.add(from(datum));
        }
        return list;
    }

    @Override
    public PNUUIDMetadata setCustom(Object custom) {
        super.setCustom(custom);
        return this;
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
                .setCustom(data.getCustom());

        newData.setETag(data.getETag());
        newData.setUpdated(data.getETag());
        return newData;
    }
}


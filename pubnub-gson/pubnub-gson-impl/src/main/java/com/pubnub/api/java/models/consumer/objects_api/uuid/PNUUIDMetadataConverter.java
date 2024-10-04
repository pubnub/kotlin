package com.pubnub.api.java.models.consumer.objects_api.uuid;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PNUUIDMetadataConverter {
    static List<PNUUIDMetadata> from(Collection<com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata> data) {
        ArrayList<PNUUIDMetadata> list = new ArrayList<>(data.size());
        for (com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata datum : data) {
            list.add(from(datum));
        }
        return list;
    }

    @Nullable
    static PNUUIDMetadata from(com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata data) {
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
        newData.setUpdated(data.getUpdated());
        return newData;
    }
}


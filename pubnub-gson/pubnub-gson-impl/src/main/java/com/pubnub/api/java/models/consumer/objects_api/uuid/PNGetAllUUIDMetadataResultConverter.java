package com.pubnub.api.java.models.consumer.objects_api.uuid;

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult;

public interface PNGetAllUUIDMetadataResultConverter {
    static PNGetAllUUIDMetadataResult from(PNUUIDMetadataArrayResult result) {
        return new PNGetAllUUIDMetadataResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNUUIDMetadataConverter.from(result.getData())
        );
    }
}

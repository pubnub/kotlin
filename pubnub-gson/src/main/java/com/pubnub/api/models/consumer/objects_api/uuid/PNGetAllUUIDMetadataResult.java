package com.pubnub.api.models.consumer.objects_api.uuid;

import com.pubnub.api.models.consumer.objects_api.EntityArrayEnvelope;
import com.pubnub.internal.models.consumer.objects.uuid.PNUUIDMetadataArrayResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNGetAllUUIDMetadataResult extends EntityArrayEnvelope<PNUUIDMetadata> {

    public PNGetAllUUIDMetadataResult(EntityArrayEnvelope<PNUUIDMetadata> envelope) {
        this.status = envelope.getStatus();
        this.totalCount = envelope.getTotalCount();
        this.prev = envelope.getPrev();
        this.next = envelope.getNext();
        this.data = envelope.getData();
    }

    private PNGetAllUUIDMetadataResult(Integer status, Integer totalCount, String prev, String next, List<PNUUIDMetadata> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public static PNGetAllUUIDMetadataResult from(PNUUIDMetadataArrayResult result) {
        return new PNGetAllUUIDMetadataResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNUUIDMetadata.from(result.getData())
        );
    }
}

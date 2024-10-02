package com.pubnub.api.java.models.consumer.objects_api.membership;

import com.pubnub.api.java.models.consumer.objects_api.EntityArrayEnvelope;
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
public class PNManageMembershipResult extends EntityArrayEnvelope<PNMembership> {
    protected PNManageMembershipResult(Integer status, Integer totalCount, String prev, String next, List<PNMembership> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public PNManageMembershipResult(EntityArrayEnvelope<PNMembership> body) {
        this.data = body.getData();
        this.next = body.getNext();
        this.prev = body.getPrev();
        this.status = body.getStatus();
        this.totalCount = body.getTotalCount();
    }
}

package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.objects_api.EntityArrayEnvelope;
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult;
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
public class PNGetMembershipsResult extends EntityArrayEnvelope<PNMembership> {
    private PNGetMembershipsResult(Integer status, Integer totalCount, String prev, String next, List<PNMembership> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public PNGetMembershipsResult(EntityArrayEnvelope<PNMembership> body) {
        this.data = body.getData();
        this.next = body.getNext();
        this.prev = body.getPrev();
        this.status = body.getStatus();
        this.totalCount = body.getTotalCount();
    }

    public static PNGetMembershipsResult from(PNChannelMembershipArrayResult result) {
        return new PNGetMembershipsResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNMembership.from(result.getData())
        );
    }
}

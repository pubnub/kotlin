package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.objects_api.EntityArrayEnvelope;
import com.pubnub.internal.models.consumer.objects.membership.PNChannelMembershipArrayResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@ToString(callSuper = true)
public class PNSetMembershipResult extends EntityArrayEnvelope<PNMembership> {
    private PNSetMembershipResult(Integer status, Integer totalCount, String prev, String next, List<PNMembership> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public PNSetMembershipResult(EntityArrayEnvelope<PNMembership> body) {
        this.data = body.getData();
        this.next = body.getNext();
        this.prev = body.getPrev();
        this.status = body.getStatus();
        this.totalCount = body.getTotalCount();
    }

    @NotNull
    public static PNSetMembershipResult from(PNChannelMembershipArrayResult result) {
        return new PNSetMembershipResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext()!= null ? result.getNext().getPageHash() : null,
                PNMembership.from(result.getData())
        );
    }
}

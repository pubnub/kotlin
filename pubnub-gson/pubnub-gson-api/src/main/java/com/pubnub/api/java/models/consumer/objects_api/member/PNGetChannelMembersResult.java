package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.java.models.consumer.objects_api.EntityArrayEnvelope;
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class PNGetChannelMembersResult extends EntityArrayEnvelope<PNMembers> {
    public PNGetChannelMembersResult(EntityArrayEnvelope<PNMembers> body) {
        this.data = body.getData();
        this.next = body.getNext();
        this.prev = body.getPrev();
        this.status = body.getStatus();
        this.totalCount = body.getTotalCount();
    }

    private PNGetChannelMembersResult(Integer status, Integer totalCount, String prev, String next, List<PNMembers> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public static PNGetChannelMembersResult from(PNMemberArrayResult result) {
        return new PNGetChannelMembersResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNMembers.from(result.getData())
        );
    }
}

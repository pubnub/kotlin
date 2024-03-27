package com.pubnub.api.models.consumer.objects_api.member;

import com.pubnub.api.models.consumer.objects_api.EntityArrayEnvelope;
import com.pubnub.internal.models.consumer.objects.member.PNMemberArrayResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class PNManageChannelMembersResult extends EntityArrayEnvelope<PNMembers> {
    public PNManageChannelMembersResult(EntityArrayEnvelope<PNMembers> body) {
        this.data = body.getData();
        this.next = body.getNext();
        this.prev = body.getPrev();
        this.status = body.getStatus();
        this.totalCount = body.getTotalCount();
    }

    private PNManageChannelMembersResult(Integer status, Integer totalCount, String prev, String next, List<PNMembers> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public static PNManageChannelMembersResult from(PNMemberArrayResult result) {
        return new PNManageChannelMembersResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNMembers.from(result.getData())
        );
    }
}

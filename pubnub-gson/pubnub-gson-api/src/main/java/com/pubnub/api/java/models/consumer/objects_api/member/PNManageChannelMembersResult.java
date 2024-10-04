package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.java.models.consumer.objects_api.EntityArrayEnvelope;
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

    protected PNManageChannelMembersResult(Integer status, Integer totalCount, String prev, String next, List<PNMembers> data) {
        this.status = status;
        this.totalCount = totalCount;
        this.prev = prev;
        this.next = next;
        this.data = data;
    }
}

package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult;

public interface PNGetChannelMembersResultConverter {
    static PNGetChannelMembersResult from(PNMemberArrayResult result) {
        return new PNGetChannelMembersResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNMembersConverter.from(result.getData())
        );
    }
}

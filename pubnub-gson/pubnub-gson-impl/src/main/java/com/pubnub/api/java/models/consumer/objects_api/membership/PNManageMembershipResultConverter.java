package com.pubnub.api.java.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult;

public interface PNManageMembershipResultConverter {
    static PNManageMembershipResult from(PNChannelMembershipArrayResult result) {
        return new PNManageMembershipResult(
                result.getStatus(),
                result.getTotalCount(),
                result.getPrev() != null ? result.getPrev().getPageHash() : null,
                result.getNext() != null ? result.getNext().getPageHash() : null,
                PNMembershipConverter.from(result.getData())
        );
    }
}

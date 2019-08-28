package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.objects_api.PropertyEnvelope;

public class Membership extends PropertyEnvelope<Membership> {

    private Membership() {

    }

    public static Membership spaceId(String spaceId) {
        Membership membership = new Membership();
        membership.id = spaceId;
        return membership;
    }

}

package com.pubnub.api.models.consumer.objects_api.member;

import com.pubnub.api.models.consumer.objects_api.PropertyEnvelope;

public class Member extends PropertyEnvelope<Member> {

    private Member() {

    }

    public static Member userId(String userId) {
        Member member = new Member();
        member.id = userId;
        return member;
    }

}

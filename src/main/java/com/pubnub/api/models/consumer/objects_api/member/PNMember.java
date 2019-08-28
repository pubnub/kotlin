package com.pubnub.api.models.consumer.objects_api.member;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import com.pubnub.api.models.consumer.objects_api.user.PNUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PNMember extends PNObject {

    private PNUser user;

    private PNMember() {
    }
}

package com.pubnub.api.models.consumer.objects_api.membership;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import com.pubnub.api.models.consumer.objects_api.space.PNSpace;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PNMembership extends PNObject {

    private PNSpace space;

    private PNMembership() {
    }
}

package com.pubnub.api.models.consumer.objects_api.user;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PNUser extends PNObject {

    private String name;
    private String externalId;
    private String profileUrl;
    private String email;

    public PNUser(String id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public PNUser setCustom(Object custom) {
        super.setCustom(custom);
        return this;
    }
}

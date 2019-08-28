package com.pubnub.api.models.consumer.objects_api.space;

import com.pubnub.api.models.consumer.objects_api.PNObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PNSpace extends PNObject {

    private String name;
    private String description;

    public PNSpace(String id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public PNSpace setCustom(Object custom) {
        super.setCustom(custom);
        return this;
    }
}

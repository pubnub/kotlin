package com.pubnub.api.models.consumer.objects_api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PNObject {

    @EqualsAndHashCode.Include
    protected String id;

    @Setter
    protected Object custom;

    @Setter
    protected String updated;

    @Setter
    protected String eTag;

    protected PNObject(String id) {
        this.id = id;
    }

    protected PNObject() {
    }
}



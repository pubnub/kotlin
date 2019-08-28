package com.pubnub.api.models.consumer.objects_api;

import com.google.gson.annotations.JsonAdapter;
import com.pubnub.api.models.consumer.objects_api.util.CustomPayloadJsonInterceptor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PNObject {

    @EqualsAndHashCode.Include
    protected String id;

    @JsonAdapter(CustomPayloadJsonInterceptor.class)
    @Setter
    protected Object custom;

    protected String created;
    protected String updated;
    protected String eTag;

    protected PNObject(String id) {
        this.id = id;
    }

    protected PNObject() {
    }
}

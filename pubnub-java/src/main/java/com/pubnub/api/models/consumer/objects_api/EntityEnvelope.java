package com.pubnub.api.models.consumer.objects_api;

import lombok.Getter;

@Getter
public class EntityEnvelope<T> {

    protected int status;

    @Getter
    protected T data;
}

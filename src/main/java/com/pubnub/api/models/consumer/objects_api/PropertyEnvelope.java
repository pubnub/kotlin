package com.pubnub.api.models.consumer.objects_api;

public abstract class PropertyEnvelope<T> {

    protected String id;
    protected Object custom;

    public T custom(Object custom) {
        this.custom = custom;
        return (T) this;
    }

    public Object custom() {
        return custom;
    }

}

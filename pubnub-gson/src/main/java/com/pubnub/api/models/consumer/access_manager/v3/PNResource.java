package com.pubnub.api.models.consumer.access_manager.v3;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public abstract class PNResource<T> {

    @Getter(AccessLevel.NONE)
    protected String resourceName;
    @Getter(AccessLevel.NONE)
    protected String resourcePattern;

    protected boolean read;
    protected boolean write;
    protected boolean create;
    protected boolean delete;
    protected boolean manage;
    protected boolean get;
    protected boolean update;
    protected boolean join;

    protected T read() {
        this.read = true;
        return (T) this;
    }

    protected T write() {
        this.write = true;
        return (T) this;
    }

    protected T create() {
        this.create = true;
        return (T) this;
    }

    protected T delete() {
        this.delete = true;
        return (T) this;
    }

    protected T manage() {
        this.manage = true;
        return (T) this;
    }

    protected T get() {
        this.get = true;
        return (T) this;
    }

    protected T update() {
        this.update = true;
        return (T) this;
    }

    protected T join() {
        this.join = true;
        return (T) this;
    }

    public boolean isPatternResource() {
        return resourcePattern != null;
    }

    public String getId() {
        if (isPatternResource()) {
            return resourcePattern;
        }
        return resourceName;
    }
}

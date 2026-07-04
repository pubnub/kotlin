package com.pubnub.api.java.models.consumer.access_manager.v3;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base for App Context PAM v3 resource grants (UUID/User metadata and DataSync).
 *
 * <p>Unlike {@link PNResource} (used by PubSub grant types such as {@link ChannelGrant}), this base exposes only the
 * object-metadata permission flags {@code get}, {@code update} and {@code delete}. The PubSub-only bits
 * ({@code read}/{@code write}/{@code manage}/{@code join}) are deliberately absent so they never surface on these
 * grant builders. Subclasses that need additional flags (e.g. DataSync's {@code create}) add them themselves.
 *
 * @param <T> the concrete grant type, for fluent chaining.
 */
@Getter
public abstract class PNAppContextResource<T> {

    @Getter(AccessLevel.NONE)
    protected String resourceName;
    @Getter(AccessLevel.NONE)
    protected String resourcePattern;

    protected boolean delete;
    protected boolean get;
    protected boolean update;

    @SuppressWarnings("unchecked")
    protected T delete() {
        this.delete = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    protected T get() {
        this.get = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    protected T update() {
        this.update = true;
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

package com.pubnub.api.java.models.consumer.access_manager.v3;

/**
 * Base for DataSync (App Context v4) PAM v3 resource grants.
 *
 * <p>Extends {@link PNAppContextResource} with the DataSync-only {@code create} flag, so DataSync grants expose
 * exactly the four permission flags that the DataSync permission model defines: {@code get}, {@code create},
 * {@code update} and {@code delete}. The PubSub-only bits ({@code read}/{@code write}/{@code manage}/{@code join})
 * remain absent.
 *
 * @param <T> the concrete grant type, for fluent chaining.
 */
public abstract class PNDataSyncResource<T> extends PNAppContextResource<T> {

    protected boolean create;

    public boolean isCreate() {
        return create;
    }

    @SuppressWarnings("unchecked")
    protected T create() {
        this.create = true;
        return (T) this;
    }
}
package com.pubnub.api.java.models.consumer.access_manager.v3;

public class UserGrant extends PNAppContextResource<UserGrant> {

    /**
     * The DataSync {@code create} permission. Unlike {@code get}/{@code update}/{@code delete} (inherited from
     * {@link PNAppContextResource}), {@code create} is collection-level: it authorizes creating <em>new</em> users
     * under the {@code users} bucket (e.g. {@code POST /v1/datasync/.../users}) and should be granted on a pattern
     * rather than a specific id. It is exposed on {@code UserGrant} only — {@link UUIDGrant} deliberately omits it.
     */
    protected boolean create;

    private UserGrant() {
    }

    public static UserGrant id(String userId) {
        UserGrant userGrant = new UserGrant();
        userGrant.resourceName = userId;
        return userGrant;
    }

    public static UserGrant pattern(String userPattern) {
        UserGrant userGrant = new UserGrant();
        userGrant.resourcePattern = userPattern;
        return userGrant;
    }

    @Override
    public UserGrant get() {
        return super.get();
    }

    @Override
    public UserGrant update() {
        return super.update();
    }

    @Override
    public UserGrant delete() {
        return super.delete();
    }

    public UserGrant create() {
        this.create = true;
        return this;
    }

    public boolean isCreate() {
        return create;
    }
}
package com.pubnub.api.java.models.consumer.access_manager.v3;

public class UserGrant extends PNAppContextResource<UserGrant> implements TokenGrant {

    /**
     * The DataSync {@code create} permission. Unlike {@code get}/{@code update}/{@code delete} (inherited from
     * {@link PNAppContextResource}), {@code create} is collection-level: it authorizes creating <em>new</em> users
     * under the {@code users} bucket (e.g. {@code POST /v1/datasync/.../users}) and should be granted on a pattern
     * rather than a specific id. It is exposed on {@code UserGrant} only — {@link UUIDGrant} deliberately omits it.
     */
    protected boolean create;

    private String projection;

    private UserGrant() {
    }

    /**
     * The DataSync projection the token holder looks through when reading this user's DataSync schema, or
     * {@code null} for the implicit {@code __default__} projection. The user's <em>permission</em> stays in the
     * plain {@code users} bucket; only the projection entry uses the {@code datasync:users:<id>} composite key.
     */
    public String getProjection() {
        return projection;
    }

    /**
     * Sets the DataSync projection the token holder looks through for this user. Fluent; returns {@code this}.
     */
    public UserGrant projection(String projection) {
        this.projection = projection;
        return this;
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
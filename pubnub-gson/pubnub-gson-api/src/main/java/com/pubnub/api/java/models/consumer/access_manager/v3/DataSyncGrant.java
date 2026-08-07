package com.pubnub.api.java.models.consumer.access_manager.v3;

/**
 * Fluent DataSync PAM v3 resource grant, passed to {@code grantToken(...).authorizedUserId(...).dataSync(...)}.
 *
 * <p>Mirrors {@link ChannelGrant}/{@link UUIDGrant} but each instance also carries a {@code namespace} identifying
 * which of the three DataSync resource types it targets. It extends {@link PNDataSyncResource} rather than
 * {@link PNResource}, so only the four DataSync-relevant permission flags
 * ({@code get}/{@code create}/{@code update}/{@code delete}) are exposed — the PubSub-only bits
 * ({@code read}/{@code write}/{@code manage}/{@code join}) are not part of the DataSync permission model.
 *
 * <p>Each grant can also carry an optional {@code projection}: when this client uses the token to access this
 * resource, they see it <em>through</em> this projection. A projection is a named, filtered view of a resource's
 * fields, defined in the entity/relationship class schema under {@code projections}. When set, the SDK emits the
 * matching {@code pn-projections} entry into the token meta automatically. Leave it unset to use the implicit
 * {@code __default__} projection.
 *
 * <pre>{@code
 * pubnub.grantToken(60)
 *     .authorizedUserId(new UserId("my-authorized-user"))
 *     .dataSync(Arrays.asList(
 *         DataSyncGrant.entity("capy-001").get().update().projection("admin"),
 *         DataSyncGrant.entityPattern(".*").get(),
 *         DataSyncGrant.relationship("user.A:channel.X").get().projection("admin"),
 *         DataSyncGrant.membership("user-123:channel-X").get()
 *     ))
 *     .sync();
 * }</pre>
 */
public class DataSyncGrant extends PNDataSyncResource<DataSyncGrant> {

    // Duplicated from com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace, which is the single
    // source of truth but lives in pubnub-kotlin-api (not on this module's compile classpath). Kept in lockstep by
    // DataSyncNamespaceParityTest — do not edit these literals without updating DataSyncNamespace.
    public static final String DATASYNC_ENTITIES = "datasync:entities";
    public static final String DATASYNC_RELATIONSHIPS = "datasync:relationships";
    public static final String DATASYNC_MEMBERSHIPS = "datasync:memberships";

    private final String namespace;
    private String projection;

    private DataSyncGrant(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    /**
     * The projection the token holder looks through for this resource, or {@code null} for the implicit
     * {@code __default__} projection.
     */
    public String getProjection() {
        return projection;
    }

    /**
     * Sets the projection the token holder looks through for this resource. Fluent; returns {@code this}.
     */
    public DataSyncGrant projection(String projection) {
        this.projection = projection;
        return this;
    }

    // entities
    public static DataSyncGrant entity(String name) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_ENTITIES);
        grant.resourceName = name;
        return grant;
    }

    public static DataSyncGrant entityPattern(String pattern) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_ENTITIES);
        grant.resourcePattern = pattern;
        return grant;
    }

    // relationships
    public static DataSyncGrant relationship(String name) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_RELATIONSHIPS);
        grant.resourceName = name;
        return grant;
    }

    public static DataSyncGrant relationshipPattern(String pattern) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_RELATIONSHIPS);
        grant.resourcePattern = pattern;
        return grant;
    }

    // memberships
    public static DataSyncGrant membership(String name) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_MEMBERSHIPS);
        grant.resourceName = name;
        return grant;
    }

    public static DataSyncGrant membershipPattern(String pattern) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_MEMBERSHIPS);
        grant.resourcePattern = pattern;
        return grant;
    }

    @Override
    public DataSyncGrant get() {
        return super.get();
    }

    @Override
    public DataSyncGrant create() {
        return super.create();
    }

    @Override
    public DataSyncGrant update() {
        return super.update();
    }

    @Override
    public DataSyncGrant delete() {
        return super.delete();
    }
}

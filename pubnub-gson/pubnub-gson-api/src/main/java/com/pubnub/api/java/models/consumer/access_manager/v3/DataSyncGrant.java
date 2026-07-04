package com.pubnub.api.java.models.consumer.access_manager.v3;

/**
 * Fluent DataSync (App Context v4) PAM v3 resource grant, passed to {@code grantToken(...).datasync(...)}.
 *
 * <p>Mirrors {@link ChannelGrant}/{@link UUIDGrant} but each instance also carries a {@code namespace} identifying
 * which of the three DataSync resource types it targets. It extends {@link PNDataSyncResource} rather than
 * {@link PNResource}, so only the four DataSync-relevant permission flags
 * ({@code get}/{@code create}/{@code update}/{@code delete}) are exposed — the PubSub-only bits
 * ({@code read}/{@code write}/{@code manage}/{@code join}) are not part of the DataSync permission model.
 *
 * <pre>{@code
 * pubnub.grantToken(60)
 *     .datasync(Arrays.asList(
 *         DataSyncGrant.entity("capy-001").get().update(),
 *         DataSyncGrant.entityPattern(".*").get(),
 *         DataSyncGrant.relationshipPattern(".*").get(),
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

    private DataSyncGrant(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
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

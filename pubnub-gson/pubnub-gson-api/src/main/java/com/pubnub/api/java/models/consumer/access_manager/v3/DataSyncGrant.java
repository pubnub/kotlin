package com.pubnub.api.java.models.consumer.access_manager.v3;

import java.util.regex.Pattern;

/**
 * Fluent DataSync PAM v3 resource grant, passed to {@code grantToken(...).authorizedUserId(...).grants(...)}.
 *
 * <p>Every DataSync resource is granted here, on an exact resource id or on a regex pattern:
 * <ul>
 *     <li>{@link #entity(String)} / {@link #entityPattern(String)} → {@code datasync:entities} bucket;</li>
 *     <li>{@link #relationship(String)} / {@link #relationshipPattern(String)} → {@code datasync:relationships}
 *     bucket;</li>
 *     <li>{@link #membership(String)} / {@link #membershipPattern(String)} → {@code datasync:memberships} bucket;</li>
 *     <li>{@link #channel(String)} / {@link #channelPattern(String)} → the plain {@code channels} bucket;</li>
 *     <li>{@link #user(String)} / {@link #userPattern(String)} → the plain {@code users} bucket.</li>
 * </ul>
 *
 * <p>It extends {@link PNDataSyncResource} rather than {@link PNResource}, so only the four DataSync-relevant
 * permission flags ({@code get}/{@code create}/{@code update}/{@code delete}) are exposed — the PubSub-only bits
 * ({@code read}/{@code write}/{@code manage}/{@code join}) are not part of the DataSync permission model.
 *
 * <p><b>Shared {@code channels} bucket:</b> DataSync channels share the {@code channels} bucket with pub/sub and App
 * Context v2, so {@code channel(id).update()} also authorizes App Context v2 {@code setChannelMetadata} for the same
 * id (and {@code get}/{@code delete} likewise). Grants on the same id are OR-merged, so combining a
 * {@link ChannelGrant} and a {@code channel(...)} grant on one id is safe: the token carries the union of both.
 *
 * <p>{@code entity}, {@code relationship}, {@code channel} and {@code user} grants (and their pattern variants) can
 * also carry an optional {@code projection}: when this client uses the token to access this resource, they see it
 * <em>through</em> this projection. A projection is a named, filtered view of a resource's fields, defined in the
 * class schema under {@code projections}. When set, the SDK emits the matching {@code pn-projections} entry into the
 * token meta automatically. Leave it unset to use the implicit {@code __default__} projection. Don't write
 * {@code pn-projections} into the token meta yourself: {@code grantToken} rejects a caller meta that contains it.
 *
 * <p>Which projections a resource has depends on its class:
 * <ul>
 *     <li>entities and relationships: the projections declared by their custom class;</li>
 *     <li>channels and users: only {@code __default__} for the built-in {@code Channel} / {@code User} classes. A
 *     named projection only has an effect for a custom Channel / User subclass that declares it;</li>
 *     <li>memberships: always {@code __default__} (the built-in {@code Membership} class has no named projections),
 *     so {@code projection(...)} on a {@link #membership(String)} grant throws.</li>
 * </ul>
 *
 * <p>These grants authorize DataSync <b>REST CRUD</b> ({@code get}/{@code create}/{@code update}/{@code delete}) on
 * the resource record only. They do <b>not</b> authorize subscribing to realtime events: a realtime subscribe is a
 * plain PubSub read of the resource's ref-channel. Use {@link #subscribe(String, String)} /
 * {@link #subscribePattern(String, String)} for that. They resolve the ref-channel names for you. See the
 * {@code subscription(...)} methods on the DataSync handles ({@link com.pubnub.api.java.v2.entities.DataSyncEntity},
 * {@link com.pubnub.api.java.v2.entities.DataSyncChannel}, {@link com.pubnub.api.java.v2.entities.DataSyncUser}).
 *
 * <pre>{@code
 * pubnub.grantToken(60)
 *     .authorizedUserId(new UserId("my-authorized-user"))
 *     .grants(Arrays.asList(
 *         // entities
 *         DataSyncGrant.entity("capy-001").get().update().projection("admin"),
 *         DataSyncGrant.entityPattern("capy-.*").get(),
 *         // relationships
 *         DataSyncGrant.relationship("user.A:channel.X").get().projection("admin"),
 *         DataSyncGrant.relationshipPattern("user\\.A:.*").get(),
 *         // memberships
 *         DataSyncGrant.membership("user-123:channel-X").get().delete(),
 *         DataSyncGrant.membershipPattern("user-123:.*").get(),
 *         // channels (plain `channels` bucket)
 *         DataSyncGrant.channel("chat-1").get().update().projection("admin"),
 *         DataSyncGrant.channelPattern("chat-.*").get(),
 *         // users (plain `users` bucket)
 *         DataSyncGrant.user("user-123").get().update(),
 *         DataSyncGrant.userPattern("user-.*").get().create(),
 *         // realtime subscribe (pub/sub `read` on the resolved ref-channel)
 *         DataSyncGrant.subscribe("capy-001"),                  // read on "capy-001"
 *         DataSyncGrant.subscribe("chat-1", "admin"),           // read on "__admin__chat-1"
 *         // default projection publishes on the bare id, so no prefix; `^` keeps it off the `__admin__capy-…` mirrors
 *         DataSyncGrant.subscribePattern("capy-.*"),            // read on "^(?:capy-.*)"
 *         DataSyncGrant.subscribePattern("chat-.*", "admin")    // read on "^__admin__(?:chat-.*)"
 *     ))
 *     .sync();
 * }</pre>
 */
public class DataSyncGrant extends PNDataSyncResource<DataSyncGrant> implements TokenGrant {

    // Duplicated from com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace, which is the single
    // source of truth but lives in pubnub-kotlin-api (not on this module's compile classpath). Kept in lockstep by
    // DataSyncNamespaceParityTest — do not edit these literals without updating DataSyncNamespace.
    public static final String DATASYNC_ENTITIES = "datasync:entities";
    public static final String DATASYNC_RELATIONSHIPS = "datasync:relationships";
    public static final String DATASYNC_MEMBERSHIPS = "datasync:memberships";
    // Not permission buckets: the bits of channel(...)/user(...) grants land in the plain `channels`/`users`
    // buckets; these strings only prefix the `pn-projections` key.
    public static final String DATASYNC_CHANNELS = "datasync:channels";
    public static final String DATASYNC_USERS = "datasync:users";
    private static final String DEFAULT_PROJECTION = "__default__";
    private static final String REGEX_META_CHARS = "\\^$.|?*+()[]{}";

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
     *
     * @throws IllegalStateException on a {@link #membership(String)} / {@link #membershipPattern(String)} grant:
     * memberships always use the built-in {@code Membership} class, which has no named projections. For a custom
     * membership-like class with projections, use a relationship class and {@link #relationship(String)}.
     */
    public DataSyncGrant projection(String projection) {
        if (DATASYNC_MEMBERSHIPS.equals(namespace)) {
            throw new IllegalStateException(
                    "Membership grants take no projection: the built-in Membership class has no named projections. "
                            + "Use DataSyncGrant.relationship(...) for a custom relationship class with projections.");
        }
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

    /**
     * Grants DataSync REST CRUD on a membership record. Memberships always use the built-in {@code Membership} class,
     * which declares no named projections, so they are always read through {@code __default__} and
     * {@link #projection(String)} throws on this grant.
     */
    public static DataSyncGrant membership(String name) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_MEMBERSHIPS);
        grant.resourceName = name;
        return grant;
    }

    /**
     * Pattern (regex) variant of {@link #membership(String)}.
     */
    public static DataSyncGrant membershipPattern(String pattern) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_MEMBERSHIPS);
        grant.resourcePattern = pattern;
        return grant;
    }

    // channels

    /**
     * Grants DataSync REST CRUD on a channel record. The permission bits land in the plain {@code channels} bucket,
     * shared with pub/sub and App Context v2: {@code update()} also authorizes App Context v2
     * {@code setChannelMetadata} for the same id. A {@code projection(...)} is emitted into the token meta as
     * {@code datasync:channels:<name>}; it does not affect realtime subscribe (see {@link #subscribe(String, String)}).
     * A named projection only has an effect for channels of a custom Channel subclass that declares it: the built-in
     * {@code Channel} class exposes its fields through {@code __default__} only.
     */
    public static DataSyncGrant channel(String name) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_CHANNELS);
        grant.resourceName = name;
        return grant;
    }

    /**
     * Pattern (regex) variant of {@link #channel(String)}.
     */
    public static DataSyncGrant channelPattern(String pattern) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_CHANNELS);
        grant.resourcePattern = pattern;
        return grant;
    }

    // users

    /**
     * Grants DataSync REST CRUD on a user record. The permission bits land in the plain {@code users} bucket. A
     * {@code projection(...)} is emitted into the token meta as {@code datasync:users:<name>}; it does not affect
     * realtime subscribe (see {@link #subscribe(String, String)}). A named projection only has an effect for users of
     * a custom User subclass that declares it: the built-in {@code User} class exposes its fields through
     * {@code __default__} only.
     */
    public static DataSyncGrant user(String name) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_USERS);
        grant.resourceName = name;
        return grant;
    }

    /**
     * Pattern (regex) variant of {@link #user(String)}.
     */
    public static DataSyncGrant userPattern(String pattern) {
        DataSyncGrant grant = new DataSyncGrant(DATASYNC_USERS);
        grant.resourcePattern = pattern;
        return grant;
    }

    // realtime subscribe

    /**
     * Grants a realtime subscribe on the default-projection ref-channel ({@code id}) of a DataSync entity, user or
     * channel. Same as {@code subscribe(id, null)}.
     */
    public static ChannelGrant subscribe(String id) {
        return subscribe(id, null);
    }

    /**
     * Grants a realtime subscribe on the ref-channel of a DataSync entity, user or channel: a pub/sub {@code read} on
     * {@link #refChannel(String, String)} ({@code id} for the default projection, {@code __{projection}__{id}}
     * otherwise). Relationships and memberships have no ref-channel of their own: their events are published to both
     * endpoint refs.
     *
     * <p>The {@code projection} here only <b>selects the ref-channel name</b>. It adds nothing to the token meta. That
     * differs from {@code projection(...)} on {@code entity}/{@code channel}/{@code user}, which sets the REST read
     * view in {@code meta.pn-projections}. To both read through and subscribe to a projection, grant the pair:
     * <pre>{@code
     * DataSyncGrant.channel("chat-1").get().projection("admin"); // REST reads through "admin"
     * DataSyncGrant.subscribe("chat-1", "admin");                // read on "__admin__chat-1"
     * }</pre>
     *
     * <p>Returns a plain {@link ChannelGrant}, because a {@code DataSyncGrant} has no {@code read} bit.
     *
     * @param projection {@code null} (or {@code "default"} / {@code "__default__"}) for the default projection.
     * @throws IllegalArgumentException if {@code projection} is blank.
     */
    public static ChannelGrant subscribe(String id, String projection) {
        return ChannelGrant.name(refChannel(id, projection)).read();
    }

    /**
     * Pattern variant of {@link #subscribe(String)} for the default projection. Same as
     * {@code subscribePattern(pattern, null)}.
     */
    public static ChannelGrant subscribePattern(String pattern) {
        return subscribePattern(pattern, null);
    }

    /**
     * Pattern variant of {@link #subscribe(String, String)}: a pub/sub {@code read} on every ref-channel whose id
     * matches the regex {@code pattern}, as seen through {@code projection}. The channel regex is built by
     * {@link #refChannelPattern(String, String)}: it is anchored at the start and wraps {@code pattern} in a
     * non-capturing group, e.g. {@code subscribePattern("capy-.*")} → {@code ^(?:capy-.*)} and
     * {@code subscribePattern("capy-.*", "admin")} → {@code ^__admin__(?:capy-.*)}.
     *
     * <p>The anchor matters: a hand-written {@code ChannelGrant.pattern("capy-.*").read()} also matches the projection
     * mirrors ({@code __admin__capy-1}), because PAM does not anchor patterns. A leading {@code ^} in {@code pattern}
     * is moved in front of the prefix. A pattern anchoring several alternatives ({@code ^a|^b}) is not rewritten and
     * won't match under a projection; write it as {@code a|b}. {@code pattern} must be a valid regex on its own, so an
     * unbalanced {@code )} can't close the wrapping group and escape the anchor.
     *
     * <p>Like {@link #subscribe(String, String)}, {@code projection} only selects the channel names and adds nothing
     * to the token meta.
     *
     * @throws IllegalArgumentException if {@code pattern} is blank, only {@code ^} or not a valid regex, or if
     * {@code projection} is blank.
     */
    public static ChannelGrant subscribePattern(String pattern, String projection) {
        return ChannelGrant.pattern(refChannelPattern(pattern, projection)).read();
    }

    // Duplicated from com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace.refChannel /
    // refChannelPattern (not on this module's compile classpath). Kept in lockstep by DataSyncNamespaceParityTest.

    /**
     * Resolves the ref-channel a DataSync resource publishes its realtime events on. {@code null}, {@code "default"}
     * and {@code "__default__"} resolve to the bare {@code id}; any other projection resolves to
     * {@code __{projection}__{id}}.
     *
     * @throws IllegalArgumentException if {@code projection} is blank.
     */
    public static String refChannel(String id, String projection) {
        return isDefaultProjection(projection) ? id : "__" + projection + "__" + id;
    }

    /**
     * Builds the PAM channel regex matching the ref-channels of every DataSync resource whose id matches
     * {@code pattern}, as seen through {@code projection}. See {@link #subscribePattern(String, String)}.
     *
     * <p>{@code pattern} must be a valid regex on its own. Otherwise the wrapper could be closed early:
     * {@code a)|.*} would become {@code ^(?:a)|.*}, whose unanchored {@code .*} branch matches every channel,
     * projection mirrors included.
     *
     * @throws IllegalArgumentException if {@code pattern} is blank, only {@code ^} or not a valid regex (a
     * {@link java.util.regex.PatternSyntaxException}), or if {@code projection} is blank.
     */
    public static String refChannelPattern(String pattern, String projection) {
        String body = pattern == null ? null : (pattern.startsWith("^") ? pattern.substring(1) : pattern);
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("pattern must not be blank");
        }
        // Throws PatternSyntaxException (an IllegalArgumentException) when the pattern is not a valid regex.
        Pattern.compile(body);
        String prefix = isDefaultProjection(projection) ? "" : escapeRegex("__" + projection + "__");
        return "^" + prefix + "(?:" + body + ")";
    }

    private static boolean isDefaultProjection(String projection) {
        if (projection == null) {
            return true;
        }
        if (projection.trim().isEmpty()) {
            throw new IllegalArgumentException("projection must not be blank");
        }
        return projection.equals("default") || projection.equals(DEFAULT_PROJECTION);
    }

    private static String escapeRegex(String literal) {
        StringBuilder sb = new StringBuilder(literal.length());
        for (int i = 0; i < literal.length(); i++) {
            char c = literal.charAt(i);
            if (REGEX_META_CHARS.indexOf(c) >= 0) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
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

package com.pubnub.api.models.consumer.access_manager.v3

interface PNGrant {
    val read: Boolean
    val write: Boolean
    val manage: Boolean
    val delete: Boolean
    val create: Boolean
    val get: Boolean
    val join: Boolean
    val update: Boolean
    val id: String
}

sealed class PNAbstractGrant protected constructor(
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false,
) : PNGrant

sealed class PNResourceGrant : PNAbstractGrant()

sealed class PNPatternGrant : PNAbstractGrant()

internal data class PNChannelResourceGrant(
    override val id: String,
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false,
) : PNResourceGrant(), ChannelGrant

internal data class PNChannelPatternGrant(
    override val id: String,
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false,
) : PNPatternGrant(), ChannelGrant

internal data class PNChannelGroupResourceGrant(
    override val id: String,
    override val read: Boolean = false,
    override val manage: Boolean = false,
) : PNResourceGrant(), ChannelGroupGrant

internal data class PNChannelGroupPatternGrant(
    override val id: String,
    override val read: Boolean = false,
    override val manage: Boolean = false,
) : PNPatternGrant(), ChannelGroupGrant

internal data class PNUUIDResourceGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
) : PNResourceGrant(), UUIDGrant

internal data class PNUUIDPatternGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
) : PNPatternGrant(), UUIDGrant

/**
 * DataSync PAM v3 namespaces and helpers.
 *
 * [ENTITIES], [RELATIONSHIPS] and [MEMBERSHIPS] appear literally as keys under the token's `res`/`pat` blocks, as
 * siblings of `chan`/`grp`/`uuid`. [USERS_PROJECTION] and [CHANNELS_PROJECTION] do **not**: see their docs.
 */
object DataSyncNamespace {
    const val ENTITIES = "datasync:entities"
    const val RELATIONSHIPS = "datasync:relationships"
    const val MEMBERSHIPS = "datasync:memberships"

    /**
     * Namespace of [DataSyncGrant.user] / [DataSyncGrant.userPattern]. It is **not** a permission bucket: the
     * permission bits of those grants go into the plain `users` bucket, and this string appears only as the prefix of
     * the `pn-projections` composite key (`datasync:users:<id>`) (server team, 2026-08-07). Note this contradicts the
     * ADR examples, which key every projection under `datasync:entities:<id>`.
     */
    const val USERS_PROJECTION = "datasync:users"

    /**
     * Namespace of [DataSyncGrant.channel] / [DataSyncGrant.channelPattern]. Like [USERS_PROJECTION] it is **not** a
     * permission bucket: the permission bits go into the plain `channels` bucket (shared with pub/sub and App Context
     * v2), and this string appears only as the prefix of the `pn-projections` composite key (`datasync:channels:<id>`).
     */
    const val CHANNELS_PROJECTION = "datasync:channels"

    /**
     * The projection a resource uses when its [DataSyncGrantType.projection] is left unset. Pass this explicitly
     * to a grant's `projection` when you want the entry written out rather than left implicit.
     */
    const val DEFAULT_PROJECTION = "__default__"

    /** The `meta` key the DataSync backend reads per-resource projection assignments from. */
    const val PN_PROJECTIONS = "pn-projections"

    /**
     * Resolves the ref-channel a DataSync resource publishes its realtime events on. `null`, `"default"` and
     * [DEFAULT_PROJECTION] resolve to the bare [id]; any other projection resolves to `__{projection}__{id}`.
     *
     * The rule has no resource type, so it applies to any ref (entity, user, channel).
     *
     * @throws IllegalArgumentException if [projection] is blank.
     */
    fun refChannel(
        id: String,
        projection: String? = null,
    ): String =
        if (isDefaultProjection(projection)) {
            id
        } else {
            "__${projection}__$id"
        }

    /**
     * Builds the PAM channel regex matching the ref-channels of every DataSync resource whose id matches [pattern],
     * as seen through [projection] (resolved like [refChannel]).
     *
     * The result is always anchored at the start and wraps [pattern] in a non-capturing group, so a top-level `|`
     * stays behind the prefix:
     * - `("capy-.*", null)` → `^(?:capy-.*)`. The anchor keeps it from also matching the projection mirrors
     *   (`__admin__capy-1`), which PAM's unanchored matching of a bare `capy-.*` would.
     * - `("capy-.*", "admin")` → `^__admin__(?:capy-.*)`.
     * - A leading `^` in [pattern] is moved to the front: `("^capy-.*", "admin")` → `^__admin__(?:capy-.*)`.
     *
     * Only the leading `^` is moved. A pattern that anchors several alternatives (`^a|^b`) is not rewritten, so its
     * inner `^` can never match after the prefix: write it as `a|b` instead.
     *
     * [pattern] must be a valid regex on its own. Otherwise the wrapper could be closed early: `a)|.*` would become
     * `^(?:a)|.*`, whose unanchored `.*` branch matches every channel, projection mirrors included.
     *
     * @throws IllegalArgumentException if [pattern] is blank or only `^` (either would match every channel), if it is
     * not a valid regex, or if [projection] is blank.
     */
    fun refChannelPattern(
        pattern: String,
        projection: String? = null,
    ): String {
        val body = pattern.removePrefix("^")
        require(body.isNotBlank()) { "pattern must not be blank" }
        require(runCatching { Regex(body) }.isSuccess) { "pattern must be a valid regex: $pattern" }
        val prefix = if (isDefaultProjection(projection)) {
            ""
        } else {
            escapeRegex("__${projection}__")
        }
        return "^$prefix(?:$body)"
    }

    private fun isDefaultProjection(projection: String?): Boolean {
        if (projection == null) {
            return true
        }
        require(projection.isNotBlank()) { "projection must not be blank" }
        return projection == "default" || projection == DEFAULT_PROJECTION
    }

    private fun escapeRegex(literal: String): String =
        buildString {
            literal.forEach { c ->
                if (c in REGEX_META_CHARS) {
                    append('\\')
                }
                append(c)
            }
        }

    private const val REGEX_META_CHARS = "\\^$.|?*+()[]{}"
}

/**
 * Marker type accepted by the `dataSync` parameter of [com.pubnub.api.PubNub.grantToken].
 *
 * This interface is `sealed`: only the SDK's own concrete grant classes may implement it. External callers create
 * instances through the [DataSyncGrant] factory. Sealing prevents a caller from supplying a grant with an unknown
 * [namespace] (e.g. a typo) that the serializer would otherwise silently drop from the minted token.
 */
sealed interface DataSyncGrantType : TokenGrant {
    val namespace: String

    /**
     * The single DataSync projection the token holder looks *through* for this resource, or `null` for the implicit
     * `__default__` projection. This is the token-level viewing projection (the ADR's `pn-projections` value is a
     * single projection name); it is distinct from the schema-level field→projections mapping.
     *
     * When set, the SDK adds a `pn-projections` entry to the token meta keyed by `"$namespace:$id"`. The [id] is
     * passed through verbatim — the SDK imposes no separator convention on it.
     *
     * Because projections are folded into the token meta, the `meta` passed to `grantToken` must be `null` or a
     * map whenever any grant carries a projection. Passing a non-map meta
     * alongside a projection throws a `PubNubException` rather than silently dropping the meta.
     */
    val projection: String?
}

internal data class PNDataSyncResourceGrant(
    override val namespace: String,
    override val id: String,
    override val get: Boolean = false,
    override val create: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
    override val projection: String? = null,
) : PNResourceGrant(), DataSyncGrantType

internal data class PNDataSyncPatternGrant(
    override val namespace: String,
    override val id: String,
    override val get: Boolean = false,
    override val create: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
    override val projection: String? = null,
) : PNPatternGrant(), DataSyncGrantType

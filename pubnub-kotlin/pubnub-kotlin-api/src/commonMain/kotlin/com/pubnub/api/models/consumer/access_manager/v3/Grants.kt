package com.pubnub.api.models.consumer.access_manager.v3

import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions

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
    override val projection: String? = null,
) : PNResourceGrant(), ChannelGrant {
    constructor(spacePermissions: SpacePermissions) : this(
        id = spacePermissions.id,
        read = spacePermissions.read,
        write = spacePermissions.write,
        manage = spacePermissions.manage,
        delete = spacePermissions.delete,
        create = false,
        get = spacePermissions.get,
        join = spacePermissions.join,
        update = spacePermissions.update,
    )
}

internal data class PNSpacePermissionsGrant(
    override val id: String,
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false,
) : PNResourceGrant(), SpacePermissions

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
    override val projection: String? = null,
) : PNPatternGrant(), ChannelGrant {
    constructor(spacePermissions: SpacePermissions) : this(
        id = spacePermissions.id,
        read = spacePermissions.read,
        write = spacePermissions.write,
        manage = spacePermissions.manage,
        delete = spacePermissions.delete,
        create = false,
        get = spacePermissions.get,
        join = spacePermissions.join,
        update = spacePermissions.update,
    )
}

internal data class PNSpacePatternPermissionsGrant(
    override val id: String,
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false,
) : PNPatternGrant(), SpacePermissions

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
) : PNResourceGrant(), UUIDGrant {
    constructor(userPermissions: UserPermissions) : this(
        id = userPermissions.id,
        get = userPermissions.get,
        update = userPermissions.update,
        delete = userPermissions.delete,
    )
}

internal data class PNUserPermissionsGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
) : PNResourceGrant(), UserPermissions

internal data class PNUUIDPatternGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
) : PNPatternGrant(), UUIDGrant {
    constructor(userPermissions: UserPermissions) : this(
        id = userPermissions.id,
        get = userPermissions.get,
        update = userPermissions.update,
        delete = userPermissions.delete,
    )
}

internal data class PNUserPatternPermissionsGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
) : PNPatternGrant(), UserPermissions

internal data class PNUserResourceGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val projection: String? = null,
) : PNResourceGrant(), UserGrant

internal data class PNUserPatternGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val projection: String? = null,
) : PNPatternGrant(), UserGrant

/**
 * The three DataSync PAM v3 resource namespaces. They appear literally as keys under the
 * token's `res`/`pat` blocks, as siblings of `chan`/`grp`/`uuid`.
 */
object DataSyncNamespace {
    const val ENTITIES = "datasync:entities"
    const val RELATIONSHIPS = "datasync:relationships"
    const val MEMBERSHIPS = "datasync:memberships"

    /**
     * Projection-key namespace for User instances. This prefix is used **only** to build the `pn-projections`
     * composite key (`datasync:users:<id>`) for a [UserGrant] that carries a projection — it is **not** a permission
     * bucket. A user's permission continues to live in the plain `users` bucket (server team, 2026-08-07). Note this
     * contradicts the ADR examples, which key every projection under `datasync:entities:<id>`; the server team
     * confirmed `datasync:users` / `datasync:channels` for User/Channel instances.
     */
    const val USERS_PROJECTION = "datasync:users"

    /**
     * Projection-key namespace for Channel instances. Projection-key-only, like [USERS_PROJECTION]: a channel's
     * permission stays in the plain `channels` bucket; only its `pn-projections` composite key uses the
     * `datasync:channels:<id>` prefix (server team, 2026-08-07).
     */
    const val CHANNELS_PROJECTION = "datasync:channels"

    /**
     * The projection a resource uses when its [DataSyncGrantType.projection] is left unset. Pass this explicitly
     * to a grant's `projection` when you want the entry written out rather than left implicit.
     */
    const val DEFAULT_PROJECTION = "__default__"

    /** The `meta` key the DataSync backend reads per-resource projection assignments from. */
    const val PN_PROJECTIONS = "pn-projections"
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

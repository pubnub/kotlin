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

internal sealed class PNAbstractGrant(
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false
) : PNGrant

internal sealed class PNResourceGrant : PNAbstractGrant()

internal sealed class PNPatternGrant : PNAbstractGrant()

internal data class PNChannelResourceGrant(
    override val id: String,
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val create: Boolean = false,
    override val get: Boolean = false,
    override val join: Boolean = false,
    override val update: Boolean = false
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
    override val update: Boolean = false
) : PNPatternGrant(), ChannelGrant

internal data class PNChannelGroupResourceGrant(
    override val id: String,
    override val read: Boolean = false,
    override val manage: Boolean = false
) : PNResourceGrant(), ChannelGroupGrant

internal data class PNChannelGroupPatternGrant(
    override val id: String,
    override val read: Boolean = false,
    override val manage: Boolean = false
) : PNPatternGrant(), ChannelGroupGrant

internal data class PNUUIDResourceGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false
) : PNResourceGrant(), UUIDGrant

internal data class PNUUIDPatternGrant(
    override val id: String,
    override val get: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false
) : PNPatternGrant(), UUIDGrant

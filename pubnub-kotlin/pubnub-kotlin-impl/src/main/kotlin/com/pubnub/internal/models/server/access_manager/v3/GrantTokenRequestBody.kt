package com.pubnub.internal.models.server.access_manager.v3

import com.google.gson.annotations.SerializedName
import com.pubnub.api.PubNubException
import com.pubnub.api.models.TokenBitmask
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrantType
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncNamespace
import com.pubnub.api.models.consumer.access_manager.v3.PNGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNPatternGrant
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant

data class GrantTokenRequestBody(
    val ttl: Int,
    val permissions: GrantTokenPermissions,
) {
    data class GrantTokenPermissions(
        val resources: GrantTokenPermission,
        val patterns: GrantTokenPermission,
        val meta: Any? = null,
        val uuid: String? = null,
    )

    data class GrantTokenPermission(
        val channels: Map<String, Int> = emptyMap(),
        val groups: Map<String, Int> = emptyMap(),
        val uuids: Map<String, Int> = emptyMap(),
        val spaces: Map<String, Int> = emptyMap(),
        val users: Map<String, Int> = emptyMap(),
        @SerializedName(DataSyncNamespace.ENTITIES)
        val datasyncEntities: Map<String, Int> = emptyMap(),
        @SerializedName(DataSyncNamespace.RELATIONSHIPS)
        val datasyncRelationships: Map<String, Int> = emptyMap(),
        @SerializedName(DataSyncNamespace.MEMBERSHIPS)
        val datasyncMemberships: Map<String, Int> = emptyMap(),
    )

    companion object {
        @Throws(PubNubException::class)
        fun of(
            ttl: Int,
            channels: List<ChannelGrant>,
            groups: List<ChannelGroupGrant>,
            uuids: List<UUIDGrant>,
            meta: Any?,
            uuid: String?,
            datasync: List<DataSyncGrantType> = emptyList(),
        ): GrantTokenRequestBody {
            val entities = datasync.filter { it.namespace == DataSyncNamespace.ENTITIES }
            val relationships = datasync.filter { it.namespace == DataSyncNamespace.RELATIONSHIPS }
            val memberships = datasync.filter { it.namespace == DataSyncNamespace.MEMBERSHIPS }

            val resources =
                GrantTokenPermission(
                    channels = getResources(channels),
                    groups = getResources(groups),
                    uuids = getResources(uuids),
                    datasyncEntities = getResources(entities),
                    datasyncRelationships = getResources(relationships),
                    datasyncMemberships = getResources(memberships),
                )
            val patterns =
                GrantTokenPermission(
                    channels = getPatterns(channels),
                    groups = getPatterns(groups),
                    uuids = getPatterns(uuids),
                    datasyncEntities = getPatterns(entities),
                    datasyncRelationships = getPatterns(relationships),
                    datasyncMemberships = getPatterns(memberships),
                )
            val permissions = GrantTokenPermissions(resources, patterns, meta ?: emptyMap<Any, Any>(), uuid)
            return GrantTokenRequestBody(ttl, permissions)
        }

        private fun <T : PNGrant> getResources(resources: List<T>): Map<String, Int> {
            return resources
                .filter { it !is PNPatternGrant }
                .associate { it.id to calculateBitmask(it) }
        }

        private fun <T : PNGrant> getPatterns(resources: List<T>): Map<String, Int> {
            return resources
                .filterIsInstance<PNPatternGrant>()
                .associate { it.id to calculateBitmask(it) }
        }

        private fun calculateBitmask(resource: PNGrant): Int {
            var sum = 0
            if (resource.read) {
                sum = sum or TokenBitmask.READ
            }
            if (resource.write) {
                sum = sum or TokenBitmask.WRITE
            }
            if (resource.manage) {
                sum = sum or TokenBitmask.MANAGE
            }
            if (resource.delete) {
                sum = sum or TokenBitmask.DELETE
            }
            if (resource.create) {
                sum = sum or TokenBitmask.CREATE
            }
            if (resource.get) {
                sum = sum or TokenBitmask.GET
            }
            if (resource.join) {
                sum = sum or TokenBitmask.JOIN
            }
            if (resource.update) {
                sum = sum or TokenBitmask.UPDATE
            }
            return sum
        }
    }
}

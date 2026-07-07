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
            val metaWithProjections = mergeProjectionsIntoMeta(meta, datasync)
            val permissions = GrantTokenPermissions(resources, patterns, metaWithProjections, uuid)
            return GrantTokenRequestBody(ttl, permissions)
        }

        /**
         * Fold any per-grant [DataSyncGrantType.projection] into the token [meta] as a `pn-projections` block.
         *
         * The composite key is `"$namespace:$id"` with the id passed through verbatim (no separator normalization);
         * pattern grants land under `pat`, exact grants under `res`. If the caller already supplied a
         * `pn-projections` entry inside their own [meta] map it is preserved and the grant-derived entries are merged
         * on top of it. Returns the original meta untouched when no grant carries a projection.
         */
        private fun mergeProjectionsIntoMeta(meta: Any?, datasync: List<DataSyncGrantType>): Any {
            val withProjection = datasync.filter { it.projection != null }
            if (withProjection.isEmpty()) {
                return meta ?: emptyMap<Any, Any>()
            }

            // Build { "res": { key -> projection }, "pat": { key -> projection } } with the composite key
            // "$namespace:$id" and the id passed through verbatim (no separator normalization).
            val res = LinkedHashMap<String, String>()
            val pat = LinkedHashMap<String, String>()
            withProjection.forEach { grant ->
                val key = "${grant.namespace}:${grant.id}"
                val target = if (grant is PNPatternGrant) {
                    pat
                } else {
                    res
                }
                target[key] = grant.projection!!
            }
            val generatedBlock = LinkedHashMap<String, Any?>()
            if (res.isNotEmpty()) {
                generatedBlock["res"] = res
            }
            if (pat.isNotEmpty()) {
                generatedBlock["pat"] = pat
            }

            // Merge with caller-supplied meta. If it is a map, overlay pn-projections onto a copy; otherwise the
            // generated projection meta wins (a non-map meta cannot carry pn-projections anyway).
            @Suppress("UNCHECKED_CAST")
            val callerMeta = meta as? Map<String, Any?>
                ?: return mapOf(DataSyncNamespace.PN_PROJECTIONS to generatedBlock)

            val merged = LinkedHashMap<String, Any?>(callerMeta)
            val existing = callerMeta[DataSyncNamespace.PN_PROJECTIONS]
            merged[DataSyncNamespace.PN_PROJECTIONS] = deepMergeProjectionBlocks(existing, generatedBlock)
            return merged
        }

        /**
         * Merge two `pn-projections` blocks (each `{ "res": {...}, "pat": {...} }`), with [generated] grant-derived
         * entries overriding any colliding key in the caller's [existing] block.
         */
        private fun deepMergeProjectionBlocks(existing: Any?, generated: Any?): Any? {
            @Suppress("UNCHECKED_CAST")
            val existingBlock = existing as? Map<String, Any?> ?: return generated

            @Suppress("UNCHECKED_CAST")
            val generatedBlock = generated as? Map<String, Any?> ?: return existing

            val result = LinkedHashMap<String, Any?>(existingBlock)
            for (subKey in listOf("res", "pat")) {
                @Suppress("UNCHECKED_CAST")
                val existingSub = existingBlock[subKey] as? Map<String, Any?>

                @Suppress("UNCHECKED_CAST")
                val generatedSub = generatedBlock[subKey] as? Map<String, Any?> ?: continue
                val mergedSub = LinkedHashMap<String, Any?>()
                if (existingSub != null) {
                    mergedSub.putAll(existingSub)
                }
                mergedSub.putAll(generatedSub)
                result[subKey] = mergedSub
            }
            return result
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

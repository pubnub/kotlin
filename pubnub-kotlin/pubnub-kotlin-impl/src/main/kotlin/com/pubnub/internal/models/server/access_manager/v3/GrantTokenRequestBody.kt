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
import com.pubnub.api.models.consumer.access_manager.v3.UserGrant

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
            users: List<UserGrant> = emptyList(),
            dataSync: List<DataSyncGrantType> = emptyList(),
        ): GrantTokenRequestBody {
            val entities = dataSync.filter { it.namespace == DataSyncNamespace.ENTITIES }
            val relationships = dataSync.filter { it.namespace == DataSyncNamespace.RELATIONSHIPS }
            val memberships = dataSync.filter { it.namespace == DataSyncNamespace.MEMBERSHIPS }

            val resources =
                GrantTokenPermission(
                    channels = getResources(channels),
                    groups = getResources(groups),
                    uuids = getResources(uuids),
                    users = getResources(users),
                    datasyncEntities = getResources(entities),
                    datasyncRelationships = getResources(relationships),
                    datasyncMemberships = getResources(memberships),
                )
            val patterns =
                GrantTokenPermission(
                    channels = getPatterns(channels),
                    groups = getPatterns(groups),
                    uuids = getPatterns(uuids),
                    users = getPatterns(users),
                    datasyncEntities = getPatterns(entities),
                    datasyncRelationships = getPatterns(relationships),
                    datasyncMemberships = getPatterns(memberships),
                )
            val metaWithProjections = mergeProjectionsIntoMeta(meta, channels, users, dataSync)
            val permissions = GrantTokenPermissions(resources, patterns, metaWithProjections, uuid)
            return GrantTokenRequestBody(ttl, permissions)
        }

        /**
         * Fold any per-grant projection into the token [meta] as a `pn-projections` block.
         *
         * A projection can come from three grant types, each with its own composite-key namespace:
         * - [DataSyncGrantType] → `"${grant.namespace}:${grant.id}"` (`datasync:entities`/`relationships`/`memberships`);
         * - [UserGrant] with a projection → `"${DataSyncNamespace.USERS_PROJECTION}:${grant.id}"` (`datasync:users:<id>`);
         * - [ChannelGrant] with a projection → `"${DataSyncNamespace.CHANNELS_PROJECTION}:${grant.id}"`
         *   (`datasync:channels:<id>`).
         *
         * Note the User/Channel projection namespaces (`datasync:users`/`datasync:channels`) are projection-key-only:
         * the *permissions* for those grants still land in the plain `users`/`channels` buckets. The id is passed
         * through verbatim (no separator normalization); pattern grants land under `pat`, exact grants under `res`. If
         * the caller already supplied a `pn-projections` entry inside their own [meta] map it is preserved and the
         * grant-derived entries are merged on top of it (last-wins on a colliding composite key). Returns the original
         * meta untouched when no grant carries a projection.
         *
         * @throws PubNubException if a grant carries a projection but [meta] is a non-null, non-map value. Projections
         * must live inside a map-shaped meta, so the SDK cannot merge them into an arbitrary object without silently
         * discarding it — pass `null` or a map (e.g. via `createCustomObject(mapOf(...))`) instead.
         */
        @Throws(PubNubException::class)
        private fun mergeProjectionsIntoMeta(
            meta: Any?,
            channels: List<ChannelGrant>,
            users: List<UserGrant>,
            dataSync: List<DataSyncGrantType>,
        ): Any {
            // Each entry pairs a composite key with its projection; pattern grants route to `pat`, the rest to `res`.
            data class ProjectionEntry(val key: String, val projection: String, val isPattern: Boolean)

            val entries = ArrayList<ProjectionEntry>()
            dataSync.forEach { grant ->
                grant.projection?.let { entries.add(ProjectionEntry("${grant.namespace}:${grant.id}", it, grant is PNPatternGrant)) }
            }
            users.forEach { grant ->
                grant.projection?.let {
                    entries.add(ProjectionEntry("${DataSyncNamespace.USERS_PROJECTION}:${grant.id}", it, grant is PNPatternGrant))
                }
            }
            channels.forEach { grant ->
                grant.projection?.let {
                    entries.add(ProjectionEntry("${DataSyncNamespace.CHANNELS_PROJECTION}:${grant.id}", it, grant is PNPatternGrant))
                }
            }
            if (entries.isEmpty()) {
                return meta ?: emptyMap<Any, Any>()
            }

            // Build { "res": { key -> projection }, "pat": { key -> projection } } with the id passed through verbatim.
            val res = LinkedHashMap<String, String>()
            val pat = LinkedHashMap<String, String>()
            entries.forEach { entry ->
                val target = if (entry.isPattern) {
                    pat
                } else {
                    res
                }
                target[entry.key] = entry.projection
            }
            val generatedBlock = LinkedHashMap<String, Any?>()
            if (res.isNotEmpty()) {
                generatedBlock["res"] = res
            }
            if (pat.isNotEmpty()) {
                generatedBlock["pat"] = pat
            }

            // Merge with caller-supplied meta. A null meta simply becomes the generated pn-projections block. A
            // non-map meta cannot carry pn-projections, so rather than silently discard the caller's object we fail
            // loudly — the caller must pass a map (e.g. createCustomObject(mapOf(...))) when using projections.
            if (meta != null && meta !is Map<*, *>) {
                throw PubNubException(
                    "DataSync projections require `meta` to be null or a map " +
                        "got a non-map meta of type ${meta::class.simpleName}, which cannot carry pn-projections.",
                )
            }

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

        // Duplicate ids are OR-merged (not last-wins): a caller may naturally append two grants for the same id from
        // a flat `grants` list, and each is expected to contribute its bits to the single token entry.
        private fun <T : PNGrant> getResources(resources: List<T>): Map<String, Int> =
            resources
                .filter { it !is PNPatternGrant }
                .groupingBy { it.id }
                .fold(0) { acc, grant -> acc or calculateBitmask(grant) }

        private fun <T : PNGrant> getPatterns(resources: List<T>): Map<String, Int> =
            resources
                .filterIsInstance<PNPatternGrant>()
                .groupingBy { it.id }
                .fold(0) { acc, grant -> acc or calculateBitmask(grant) }

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

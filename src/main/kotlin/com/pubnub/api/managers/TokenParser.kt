package com.pubnub.api.managers

import co.nstant.`in`.cbor.CborDecoder
import co.nstant.`in`.cbor.model.ByteString
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.vendor.Base64
import java.nio.charset.StandardCharsets
import kotlin.collections.Map
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import co.nstant.`in`.cbor.model.Map as CborMap

internal class TokenParser {

    fun unwrapToken(token: String): PNToken {
        val byteArray = Base64.decode(token.toByteArray(StandardCharsets.UTF_8), Base64.URL_SAFE)
        val dataItems = CborDecoder(byteArray.inputStream()).decode()
        return (dataItems.firstOrNull() as? CborMap)?.let {
            val firstLevelMap = it.toJvmMap()
            val resourcesValue = firstLevelMap[RESOURCES_KEY] as? Map<*, *>
            val patternsValue = firstLevelMap[PATTERNS_KEY] as? Map<*, *>
            PNToken(
                version = firstLevelMap[V_KEY].toString().toInt(),
                timestamp = firstLevelMap[TIMESTAMP_KEY].toString().toLong(),
                ttl = firstLevelMap[TTL_KEY].toString().toLong(),
                authorizedUUID = firstLevelMap[AUTHORIZED_UUID_KEY].toString(),
                resources = resourcesValue.toPNTokenResources(),
                patterns = patternsValue.toPNTokenResources(),
                meta = firstLevelMap[META_KEY]
            )
        }!!
    }

    private fun CborMap.toJvmMap(depth: Int = 0): MutableMap<String, Any> {
        if (depth > 3) {
            throw PubNubException(pubnubError = PubNubError.INVALID_ACCESS_TOKEN, errorMessage = "Token is too deep")
        }
        val result = mutableMapOf<String, Any>()
        for (key in keys) {
            val value = get(key)
            val keyString = when (key) {
                is ByteString -> key.bytes.toString(StandardCharsets.UTF_8)
                else -> key.toString()
            }

            when (value) {
                is CborMap -> result[keyString] = value.toJvmMap(depth + 1)
                is ByteString -> result[keyString] = value.bytes
                is List<*> -> result[keyString] = value.map { it.toString() }
                else -> result[keyString] = value.toString()
            }
        }
        return result
    }

    private fun Map<*, *>?.toPNTokenResources(): PNToken.PNTokenResources {
        val channels = this?.get(CHANNELS_KEY) as? Map<*, *>
        val groups = this?.get(GROUPS_KEY) as? Map<*, *>
        val uuids = this?.get(UUIDS_KEY) as? Map<*, *>

        return PNToken.PNTokenResources(
            channels = channels?.map { (k, v) -> k.toString() to PNToken.PNResourcePermissions(v.toString().toInt()) }
                ?.toMap() ?: emptyMap(),
            channelGroups = groups?.map { (k, v) -> k.toString() to PNToken.PNResourcePermissions(v.toString().toInt()) }
                ?.toMap() ?: emptyMap(),
            uuids = uuids?.map { (k, v) -> k.toString() to PNToken.PNResourcePermissions(v.toString().toInt()) }
                ?.toMap() ?: emptyMap(),
        )
    }

    companion object {
        private const val V_KEY = "v"
        private const val TIMESTAMP_KEY = "t"
        private const val TTL_KEY = "ttl"
        private const val AUTHORIZED_UUID_KEY = "uuid"
        private const val RESOURCES_KEY = "res"
        private const val PATTERNS_KEY = "pat"
        private const val META_KEY = "meta"
        private const val CHANNELS_KEY = "chan"
        private const val GROUPS_KEY = "grp"
        private const val UUIDS_KEY = "uuid"
    }
}

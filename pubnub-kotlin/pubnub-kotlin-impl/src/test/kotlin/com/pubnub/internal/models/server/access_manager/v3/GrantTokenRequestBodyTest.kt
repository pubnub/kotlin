package com.pubnub.internal.models.server.access_manager.v3

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import org.junit.Assert.assertEquals
import org.junit.Test

class GrantTokenRequestBodyTest {
    private val gson = Gson()

    @Test
    fun serializesDataSyncNamespacesAsLiteralColonKeys() {
        // given a mix of exact-resource and pattern DataSync grants across all three namespaces
        val body =
            GrantTokenRequestBody.of(
                ttl = 60,
                channels = emptyList(),
                groups = emptyList(),
                uuids = emptyList(),
                meta = null,
                uuid = "pam-debug-admin",
                datasync =
                    listOf(
                        DataSyncGrant.entity("capy-001", get = true, update = true),
                        DataSyncGrant.entityPattern(".*", get = true, create = true),
                        DataSyncGrant.relationship("rel-1", delete = true),
                        DataSyncGrant.membership("user-123:channel-X", get = true),
                    ),
            )

        // when
        val json = gson.toJsonTree(body).asJsonObject
        val resources = json["permissions"].asJsonObject["resources"].asJsonObject
        val patterns = json["permissions"].asJsonObject["patterns"].asJsonObject

        // then — literal colon keys with the right bitmask ints (get=32, create=16, update=64, delete=8)
        assertEquals(32 + 64, intAt(resources, "datasync:entities", "capy-001"))
        assertEquals(8, intAt(resources, "datasync:relationships", "rel-1"))
        assertEquals(32, intAt(resources, "datasync:memberships", "user-123:channel-X"))
        assertEquals(32 + 16, intAt(patterns, "datasync:entities", ".*"))
    }

    private fun intAt(
        obj: JsonObject,
        namespaceKey: String,
        id: String,
    ): Int = obj[namespaceKey].asJsonObject[id].asInt
}

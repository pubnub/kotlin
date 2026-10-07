package com.pubnub.docs.accessManager

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.DataSyncGrant
import com.pubnub.api.v2.PNConfiguration

class GrantTokenDataSync {
    private fun grantTokenFlatList() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-token-flat-list

        // snippet.grantTokenFlatList
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                ChannelGrant.name(name = "channel-summer-sale", read = true, write = true),
                ChannelGrant.pattern(pattern = "^announcements-.*$", read = true)
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
    }

    private fun grantTokenDataSync() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-token-datasync

        // snippet.grantTokenDataSync
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                DataSyncGrant.entity(name = "product-sneaker-42", get = true, update = true),
                DataSyncGrant.relationship(name = "rel-bob-owns-sneaker-42", get = true),
                DataSyncGrant.membership(name = "membership-alice-summer-sale", get = true, update = true),
                // User and channel grants share the regular users and channels buckets of the token
                DataSyncGrant.user(name = "user-alice", get = true, update = true),
                DataSyncGrant.channel(name = "channel-summer-sale", get = true),
                // Real-time events are delivered on the resource id, so grant read access to it
                DataSyncGrant.subscribe(id = "product-sneaker-42"),
                DataSyncGrant.subscribe(id = "user-alice")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
    }

    private fun grantTokenDataSyncProjection() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-token-datasync-projection

        // snippet.grantTokenDataSyncProjection
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                DataSyncGrant.entity(name = "product-sneaker-42", get = true, projection = "admin"),
                DataSyncGrant.subscribe(id = "product-sneaker-42", projection = "admin")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
    }

    private fun grantTokenDataSyncPattern() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#grant-token-datasync-pattern

        // snippet.grantTokenDataSyncPattern
        // Only server-side applications should use the secret key, as it is required to grant tokens
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myServerUserId"), "demo").apply {
                publishKey = "demo"
                secretKey = "mySecretKey"
            }.build()
        )

        pubnub.grantToken(
            ttl = 15,
            authorizedUserId = UserId("user-alice"),
            grants = listOf(
                DataSyncGrant.entityPattern(pattern = "^product-.*$", get = true, update = true),
                DataSyncGrant.subscribePattern(pattern = "^product-.*$")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed to grant token: ${exception.message}")
            }.onSuccess { value ->
                println("Token: ${value.token}")
            }
        }
        // snippet.end
    }

    private fun parseTokenDataSync() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/access-manager#parse-token-datasync

        // snippet.parseTokenDataSync
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").build()
        )

        val token = pubnub.parseToken(
            "qGF2AmF0GmqXSuBjdHRsD2R1dWlkanVzZXItYWxpY2VjcmVzpWRjaGFuoXNjaGFubmVsLXN1bW1lci1zYWxlAWNncnCgZHV1aWSgY3VzcqFqdXNlci1hbGljZRhgcWRhdGFzeW5jOmVudGl0aWVzoXJwcm9kdWN0LXNuZWFrZXItNDIYcGNwYXSjZGNoYW6gY2dycKBkdXVpZKBkbWV0YaFucG4tcHJvamVjdGlvbnOhY3Jlc6F4JGRhdGFzeW5jOmVudGl0aWVzOnByb2R1Y3Qtc25lYWtlci00MmVhZG1pbmNzaWdYIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        )

        val entityPermissions = token.resources.datasyncEntities["product-sneaker-42"]
        println("Can create entities: ${entityPermissions?.create}")
        println("Can update entities: ${entityPermissions?.update}")
        println("User permissions: ${token.resources.users["user-alice"]}")
        println("Projection: ${token.projections?.resources?.entities?.get("product-sneaker-42")}")
        // snippet.end
    }
}

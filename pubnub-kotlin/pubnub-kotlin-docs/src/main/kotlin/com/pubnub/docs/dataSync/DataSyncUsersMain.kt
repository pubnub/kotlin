package com.pubnub.docs.dataSync

import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.datasync.entity.PNJsonPatchOperation
import com.pubnub.api.v2.PNConfiguration

class DataSyncUsersMain {
    private fun createUserBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#create-user

        // snippet.createUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.createUser(
            userId = "user-alice",
            classVersion = 1,
            payload = mapOf("name" to "Alice", "type" to "shopper")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Created user ${value.data.id} with eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun getUserBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-user

        // snippet.getUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getUser("user-alice").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Fetched user ${value.data.id} with payload ${value.data.payload}")
            }
        }
        // snippet.end
    }

    private fun getUsersBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#get-users

        // snippet.getUsersBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.getUsers(
            limit = 10
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                value.data.forEach { println(it.id) }
                println("More pages available: ${value.next.hasNext}")
            }
        }
        // snippet.end
    }

    private fun setUserBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#set-user

        // snippet.setUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.setUser(
            userId = "user-alice",
            classVersion = 1,
            payload = mapOf("name" to "Alice B.", "type" to "shopper")
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Replaced user ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun updateUserBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#update-user

        // snippet.updateUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.updateUser(
            userId = "user-alice",
            operations = listOf(
                PNJsonPatchOperation(op = "replace", path = "/payload/name", value = "Alice B.")
            )
        ).async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Updated user ${value.data.id}, new eTag ${value.data.eTag}")
            }
        }
        // snippet.end
    }

    private fun removeUserBasicUsage() {
        // https://www.pubnub.com/docs/sdks/kotlin/api-reference/data-sync#remove-user

        // snippet.removeUserBasicUsage
        val pubnub = PubNub.create(
            PNConfiguration.builder(UserId("myUserId"), "demo").apply {
                publishKey = "demo"
            }.build()
        )

        pubnub.dataSync.removeUser("user-alice").async { result ->
            result.onFailure { exception ->
                println("Failed: ${exception.message}")
            }.onSuccess { value ->
                println("Removed user, HTTP status ${value.status}")
            }
        }
        // snippet.end
    }
}

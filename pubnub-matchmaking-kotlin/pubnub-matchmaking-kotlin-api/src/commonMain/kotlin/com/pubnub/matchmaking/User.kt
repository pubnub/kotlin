package com.pubnub.matchmaking

import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture

// todo add kdoc
interface User {
    val matchmaking: Matchmaking
    val id: String
    val name: String?
    val externalId: String?
    val profileUrl: String?
    val email: String?
    val custom: Map<String, Any?>?
    val status: String?
    val type: String?
    val updated: String?
    val eTag: String?

    fun update(
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: CustomObject? = null,
        status: String? = null,
        type: String? = null,
    ): PNFuture<User>

    fun update(
        updateAction: UpdatableValues.(
            user: User
        ) -> Unit
    ): PNFuture<User>

    fun delete(soft: Boolean = false): PNFuture<User?>

    class UpdatableValues(
        /**
         * The new value for [User.name].
         */
        var name: String? = null,
        /**
         * The new value for [User.externalId].
         */
        var externalId: String? = null,
        /**
         * The new value for [User.profileUrl].
         */
        var profileUrl: String? = null,
        /**
         * The new value for [User.email].
         */
        var email: String? = null,
        /**
         * The new value for [User.custom].
         */
        var custom: CustomObject? = null,
        /**
         * The new value for [User.status].
         */
        var status: String? = null,
        /**
         * The new value for [User.type].
         */
        var type: String? = null,
    )

    companion object
}

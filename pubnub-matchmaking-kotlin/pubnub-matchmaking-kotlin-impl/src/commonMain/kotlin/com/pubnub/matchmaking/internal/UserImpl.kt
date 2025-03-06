package com.pubnub.matchmaking.internal

import com.pubnub.kmp.PNFuture
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User

data class UserImpl(
    override val matchmaking: Matchmaking,
    override val id: String,
    override val name: String?,
    override val externalId: String?,
    override val profileUrl: String?,
    override val email: String,
    override val custom: Map<String, Any?>?,
    override val status: String?,
    override val type: String?,
    override val updated: String?,
    override val eTag: String?,
): User {
    override fun update(
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        status: String?,
        type: String?
    ): PNFuture<User> {
        return matchmaking.updateUser(
            id,
            name,
            externalId,
            profileUrl,
            email,
            custom,
            status,
            type
        )
    }

    override fun update(updateAction: User.UpdatableValues.(user: User) -> Unit): PNFuture<User> {
        TODO("Not yet implemented")
    }

    override fun delete(soft: Boolean): PNFuture<User?> {
        TODO("Not yet implemented")
    }
}
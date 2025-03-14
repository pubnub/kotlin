package com.pubnub.matchmaking.internal

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User

data class UserImpl(
    override val matchmaking: Matchmaking,
    override val id: String,
    override val name: String?,
    override val externalId: String?,
    override val profileUrl: String?,
    override val email: String?,
    override val custom: Map<String, Any?>?,
    override val status: String?,
    override val type: String?,
    override val updated: String?,
    override val eTag: String?,
) : User {
    override fun update(
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: CustomObject?,
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

    companion object {
        internal fun fromDTO(matchmaking: Matchmaking, user: PNUUIDMetadata): User = UserImpl(
            matchmaking = matchmaking,
            id = user.id,
            name = user.name?.value,
            externalId = user.externalId?.value,
            profileUrl = user.profileUrl?.value,
            email = user.email?.value,
            custom = user.custom?.value,
            status = user.updated?.value,
            type = user.type?.value,
            updated = user.updated?.value,
            eTag = user.eTag?.value
        )
    }
}

package com.pubnub.matchmaking.internal

import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.kmp.CustomObject
import com.pubnub.kmp.PNFuture
import com.pubnub.matchmaking.Matchmaking
import com.pubnub.matchmaking.User

data class UserImpl(
    override val matchmaking: Matchmaking,
    override val id: String,
    override val name: String? = null,
    override val externalId: String? = null,
    override val profileUrl: String? = null,
    override val email: String? = null,
    override val custom: Map<String, Any?>? = null,
    override val status: String? = null,
    override val type: String? = null,
    override val updated: String? = null,
    override val eTag: String? = null,
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
            updated = user.updated?.value,
            status = user.status?.value,
            type = user.type?.value,
            eTag = user.eTag?.value
        )
    }
}

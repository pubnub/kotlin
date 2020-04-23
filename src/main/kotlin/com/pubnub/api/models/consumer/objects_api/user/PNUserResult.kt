package com.pubnub.api.models.consumer.objects_api.user

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope

class PNCreateUserResult internal constructor(val user: PNUser)

class PNDeleteUserResult

class PNGetUserResult internal constructor(val user: PNUser)

class PNGetUsersResult : EntityArrayEnvelope<PNUser>() {

    fun create(envelope: EntityArrayEnvelope<PNUser>): PNGetUsersResult {
        val result = PNGetUsersResult()
        result.totalCount = envelope.totalCount
        result.next = envelope.next
        result.prev = envelope.prev
        result.data = envelope.data
        return result
    }

    fun create(): PNGetUsersResult {
        return PNGetUsersResult()
    }
}

class PNUpdateUserResult internal constructor(val user: PNUser)
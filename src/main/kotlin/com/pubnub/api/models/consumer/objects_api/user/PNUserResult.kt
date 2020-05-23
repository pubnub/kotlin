package com.pubnub.api.models.consumer.objects_api.user

import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope

class PNCreateUserResult internal constructor(val user: PNUser)

class PNDeleteUserResult

class PNGetUserResult internal constructor(val user: PNUser)

class PNGetUsersResult private constructor() : EntityArrayEnvelope<PNUser>() {

    internal constructor(envelope: EntityArrayEnvelope<PNUser>) : this() {
        totalCount = envelope.totalCount
        next = envelope.next
        prev = envelope.prev
        data = envelope.data
    }
}

class PNUpdateUserResult internal constructor(val user: PNUser)
package com.pubnub.entities.models.consumer.membership

import com.pubnub.api.models.consumer.objects.member.PNMember

fun List<Membership.Partial>.toPNUUIDWithCustomList(): List<PNMember.Partial> {
    return map { it.toPNUUIDWithCustom() }
}

fun Membership.Partial.toPNUUIDWithCustom(): PNMember.Partial {
    return PNMember.Partial(uuidId = userId!!, custom = custom, status = status)
}

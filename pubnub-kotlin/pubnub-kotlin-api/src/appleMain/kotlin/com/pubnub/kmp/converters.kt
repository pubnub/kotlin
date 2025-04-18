package com.pubnub.kmp

import cocoapods.PubNubSwift.KMPChannelMetadata
import cocoapods.PubNubSwift.KMPHashedPage
import cocoapods.PubNubSwift.KMPMembershipMetadata
import cocoapods.PubNubSwift.KMPObjectSortProperty
import cocoapods.PubNubSwift.KMPUserMetadata
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal fun createPubNubHashedPage(from: PNPage?): KMPHashedPage {
    return KMPHashedPage(
        start = if (from is PNPage.PNNext) {
            from.pageHash
        } else {
            null
        },
        end = if (from is PNPage.PNPrev) {
            from.pageHash
        } else {
            null
        },
        totalCount = null
    )
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNUUIDMetadata(from: KMPUserMetadata?): PNUUIDMetadata {
    return PNUUIDMetadata(
        id = from!!.id(),
        name = PatchValue.of(from.name()),
        externalId = PatchValue.of(from.externalId()),
        profileUrl = PatchValue.of(from.profileUrl()),
        email = PatchValue.of(from.email()),
        custom = from.custom()?.safeCast<String, Any>()?.let { PatchValue.of(it) },
        updated = from.updated()?.let { PatchValue.of(it) },
        eTag = from.eTag()?.let { PatchValue.of(it) },
        type = PatchValue.of(from.type()),
        status = PatchValue.of(from.status())
    )
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNChannelMetadata(from: KMPChannelMetadata?): PNChannelMetadata {
    return PNChannelMetadata(
        id = from!!.id(),
        name = PatchValue.of(from.name()),
        description = PatchValue.of(from.descr()),
        custom = from.custom()?.safeCast<String, Any>()?.let { PatchValue.of(it) },
        updated = from.updated()?.let { PatchValue.of(it) },
        eTag = from.eTag()?.let { PatchValue.of(it) },
        type = PatchValue.of(from.type()),
        status = PatchValue.of(from.status())
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun createObjectSortProperties(from: Collection<PNSortKey<PNKey>>): List<KMPObjectSortProperty> {
    return from.map {
        KMPObjectSortProperty(
            key = it.key.fieldName,
            direction = it.dir
        )
    }
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNChannelMembership(from: KMPMembershipMetadata): PNChannelMembership {
    return PNChannelMembership(
        channel = PNChannelMetadata(
            id = from.channelMetadataId(),
            name = PatchValue.of(from.channel()?.name()),
            description = PatchValue.of(from.channel()?.descr()),
            custom = from.channel()?.custom()?.safeCast<String, Any>()?.let { PatchValue.of(it) },
            updated = from.channel()?.updated()?.let { PatchValue.of(it) },
            eTag = from.channel()?.eTag()?.let { PatchValue.of(it) },
            type = PatchValue.of(from.channel()?.type()),
            status = PatchValue.of(from.channel()?.status())
        ),
        custom = PatchValue.of(from.custom()?.safeCast()),
        updated = from.updated().orEmpty(),
        eTag = from.eTag().orEmpty(),
        status = PatchValue.of(from.status()),
        type = PatchValue.of(from.type())
    )
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNMember(from: KMPMembershipMetadata?): PNMember {
    return PNMember(
        uuid = createPNUUIDMetadata(from = from!!.user()),
        custom = from.custom()?.safeCast<String, Any>()?.let { PatchValue.of(it) },
        updated = from.updated().orEmpty(),
        eTag = from.eTag().orEmpty(),
        status = PatchValue.of(from.status()),
        type = PatchValue.of(from.type())
    )
}

internal inline fun <reified T, U> List<*>?.filterAndMap(mapper: (T) -> U): Collection<U> {
    return this?.filterIsInstance<T>()?.map(mapper) ?: emptyList()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified K, reified V> Map<*, *>.safeCast(): Map<K, V> {
    return this as? Map<K, V> ?: error("Cannot make the cast")
}

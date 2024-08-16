package com.pubnub.kmp

import cocoapods.PubNubSwift.PubNubChannelMetadataObjC
import cocoapods.PubNubSwift.PubNubHashedPageObjC
import cocoapods.PubNubSwift.PubNubMembershipMetadataObjC
import cocoapods.PubNubSwift.PubNubObjectSortPropertyObjC
import cocoapods.PubNubSwift.PubNubUUIDMetadataObjC
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.member.PNMember
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembership
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.utils.PatchValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding

internal fun String.toNSData(): NSData? {
    return (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)
}

@OptIn(ExperimentalForeignApi::class)
internal fun createPubNubHashedPage(from: PNPage?): PubNubHashedPageObjC {
    return PubNubHashedPageObjC(
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
internal fun createPNUUIDMetadata(from: PubNubUUIDMetadataObjC?): PNUUIDMetadata {
    return PNUUIDMetadata(
        id = from!!.id(),
        name = PatchValue.of(from.name()),
        externalId = PatchValue.of(from.externalId()),
        profileUrl = PatchValue.of(from.profileUrl()),
        email = PatchValue.of(from.email()),
        custom = PatchValue.of(from.custom()?.safeCast()),
        updated = PatchValue.of(from.updated().orEmpty()),
        eTag = PatchValue.of(from.eTag().orEmpty()),
        type = PatchValue.of(from.type()),
        status = PatchValue.of(from.status())
    )
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNChannelMetadata(from: PubNubChannelMetadataObjC?): PNChannelMetadata {
    return PNChannelMetadata(
        id = from!!.id(),
        name = PatchValue.of(from.name()),
        description = PatchValue.of(from.descr()),
        custom = PatchValue.of(from.custom()?.safeCast()),
        updated = PatchValue.of(from.updated().orEmpty()),
        eTag = PatchValue.of(from.eTag().orEmpty()),
        type = PatchValue.of(from.type()),
        status = PatchValue.of(from.status())
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun createObjectSortProperties(from: Collection<PNSortKey<PNKey>>): List<PubNubObjectSortPropertyObjC> {
    return from.map {
        PubNubObjectSortPropertyObjC(
            key = it.key.fieldName,
            direction = it.dir
        )
    }
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNChannelMembership(from: PubNubMembershipMetadataObjC): PNChannelMembership {
    return PNChannelMembership(
        channel = PNChannelMetadata(
            id = from.channelMetadataId(),
            name = PatchValue.of(from.channel()?.name()),
            description = PatchValue.of(from.channel()?.descr()),
            custom = PatchValue.of(from.channel()?.custom()?.safeCast()),
            updated = PatchValue.of(from.channel()?.updated().orEmpty()),
            eTag = PatchValue.of(from.channel()?.eTag().orEmpty()),
            type = PatchValue.of(from.channel()?.type()),
            status = PatchValue.of(from.channel()?.status())
        ),
        custom = PatchValue.of(from.custom()?.safeCast()),
        updated = from.updated().orEmpty(),
        eTag = from.eTag().orEmpty(),
        status = PatchValue.of(from.status())
    )
}

// TODO: PatchValue should consider cases where there is no response for a given field
@OptIn(ExperimentalForeignApi::class)
internal fun createPNMember(from: PubNubMembershipMetadataObjC?): PNMember {
    return PNMember(
        uuid = createPNUUIDMetadata(from = from!!.uuid()),
        custom = PatchValue.of(from.custom()?.safeCast()),
        updated = from.updated().orEmpty(),
        eTag = from.eTag().orEmpty(),
        status = PatchValue.of(from.status())
    )
}

internal inline fun <reified T, U> List<*>?.filterAndMap(mapper: (T) -> U): Collection<U> {
    return this?.filterIsInstance<T>()?.map(mapper) ?: emptyList()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified K, reified V> Map<*, *>.safeCast(): Map<K, V> {
    return this as? Map<K, V> ?: error("Cannot make the cast")
}

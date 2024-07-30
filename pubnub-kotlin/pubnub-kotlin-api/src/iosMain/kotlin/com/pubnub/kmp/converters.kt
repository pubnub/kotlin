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

@OptIn(ExperimentalForeignApi::class)
internal fun createPNUUIDMetadata(from: PubNubUUIDMetadataObjC): PNUUIDMetadata {
    return PNUUIDMetadata(
        id = from.id(),
        name = from.name(),
        externalId = from.externalId(),
        profileUrl = from.profileUrl(),
        email = from.email(),
        custom = from.custom()?.safeCast(),
        updated = from.updated()!!,
        eTag = from.eTag()!!,
        type = from.type(),
        status = from.status()
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun createPNChannelMetadata(from: PubNubChannelMetadataObjC): PNChannelMetadata {
    return PNChannelMetadata(
        id = from.id(),
        name = from.name(),
        description = from.descr(),
        custom = from.custom()?.safeCast(),
        updated = from.updated()!!,
        eTag = from.eTag()!!,
        type = from.type(),
        status = from.status()
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

@OptIn(ExperimentalForeignApi::class)
internal fun createPNChannelMembership(from: PubNubMembershipMetadataObjC): PNChannelMembership {
    return PNChannelMembership(
        channel = PNChannelMetadata(
            id = from.channel()?.id().orEmpty(),
            name = from.channel()?.name().orEmpty(),
            description = from.channel()?.descr().orEmpty(),
            custom = from.channel()?.custom()?.safeCast(),
            updated = from.channel()?.updated()!!,
            eTag = from.channel()?.eTag()!!,
            type = from.channel()?.type(),
            status = from.channel()?.status()
        ),
        custom = from.custom()?.safeCast(),
        updated = from.updated().orEmpty(),
        eTag = from.eTag().orEmpty(),
        status = from.status()
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun createPNMember(from: PubNubMembershipMetadataObjC): PNMember {
    return PNMember(
        uuid = from.uuid()?.let { createPNUUIDMetadata(from = it) },
        custom = from.custom()?.safeCast(),
        updated = from.updated().orEmpty(),
        eTag = from.eTag().orEmpty(),
        status = from.status()
    )
}

internal inline fun <reified T, U> List<*>?.filterAndMap(mapper: (T) -> U): Collection<U> {
    return this?.filterIsInstance<T>()?.map(mapper) ?: emptyList()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified K, reified V> Map<*, *>.safeCast(): Map<K, V> {
    return this as? Map<K, V> ?: error("Cannot make the cast")
}

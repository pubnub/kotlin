package com.pubnub.kmp

import cocoapods.PubNubSwift.PubNubChannelMetadataObjC
import cocoapods.PubNubSwift.PubNubHashedPageObjC
import cocoapods.PubNubSwift.PubNubMembershipMetadataObjC
import cocoapods.PubNubSwift.PubNubSortPropertyObjC
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

fun String.toNSData(): NSData? {
    return (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)
}

@OptIn(ExperimentalForeignApi::class)
fun createPubNubHashedPage(from: PNPage?): PubNubHashedPageObjC {
    return PubNubHashedPageObjC(
        start = if (from is PNPage.PNNext) { from.pageHash } else { null },
        end = if (from is PNPage.PNPrev) { from.pageHash } else { null },
        totalCount = null
    )
}

@OptIn(ExperimentalForeignApi::class)
fun createPNUUIDMetadata(from: PubNubUUIDMetadataObjC): PNUUIDMetadata {
    return PNUUIDMetadata(
        id = from.id(),
        name = from.name(),
        externalId = from.externalId(),
        profileUrl = from.profileUrl(),
        email = from.email(),
        custom = from.custom() as? Map<String, Any?>, // TODO: Verify
        updated = from.updated(),
        eTag = from.eTag(),
        type = from.type(),
        status = from.status()
    )
}

@OptIn(ExperimentalForeignApi::class)
fun createPNChannelMetadata(from: PubNubChannelMetadataObjC): PNChannelMetadata {
    return PNChannelMetadata(
        id = from.id(),
        name = from.name(),
        description = from.descr(),
        custom = from.custom() as? Map<String, Any?>, // TODO: Verify
        updated = from.updated(),
        eTag = from.eTag(),
        type = from.type(),
        status = from.status()
    )
}

@OptIn(ExperimentalForeignApi::class)
fun createObjectSortProperties(from: Collection<PNSortKey<PNKey>>) : List<PubNubSortPropertyObjC> {
    return from.map {
        PubNubSortPropertyObjC(
            key = it.key.fieldName,
            direction = it.dir
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
fun createPNChannelMembership(from: PubNubMembershipMetadataObjC): PNChannelMembership {
    return PNChannelMembership(
        channel = PNChannelMetadata(
            id = from.channel()?.id() ?: "",
            name = from.channel()?.name() ?: "",
            description = from.channel()?.descr() ?: "",
            custom = from.channel()?.custom()?.asMap() as? Map<String, Any?>,
            updated = from.channel()?.updated(),
            eTag = from.channel()?.eTag(),
            type = from.channel()?.type(),
            status = from.channel()?.status()
        ),
        custom = from.custom() as Map<String, Any>,
        updated = from.updated() ?: "",
        eTag = from.eTag() ?: "",
        status = from.status()
    )
}

@OptIn(ExperimentalForeignApi::class)
fun createPNMember(from: PubNubMembershipMetadataObjC): PNMember {
    return PNMember(
        uuid = from.uuid()?.let { createPNUUIDMetadata(from = it) },
        custom = from.custom() as Map<String, Any>, // TODO: Check
        updated = from.updated() ?: "",
        eTag = from.eTag() ?: "",
        status = from.status()
    )
}

inline fun <reified T, U> filteredList(from: List<*>?, mapper: (T) -> U): Collection<U> {
    return from?.filterIsInstance<T>()?.map { mapper(it) } ?: emptyList()
}
@OptIn(ExperimentalForeignApi::class)
fun createPNChannelMembershipArray(from: List<PubNubChannelMetadataObjC>): Collection<PNChannelMetadata> {
    return from.map {
        createPNChannelMetadata(from = it)
    }
}
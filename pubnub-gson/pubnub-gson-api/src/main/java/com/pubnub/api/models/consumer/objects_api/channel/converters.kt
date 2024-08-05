package com.pubnub.api.models.consumer.objects_api.channel

import com.pubnub.api.models.consumer.objects.channel.PartialPNChannelMetadata

fun from(data: PartialPNChannelMetadata): PNChannelMetadata {
    val newData = PNChannelMetadata(
        // id =
        data.id,
        // name =
        data.name?.value,
        // description =
        data.description?.value,
        // custom =
        data.custom?.value,
        // updated =
        data.updated?.value,
        // eTag =
        data.eTag?.value,
        // type =
        data.type?.value,
        // status =
        data.status?.value
    )

    return newData
}

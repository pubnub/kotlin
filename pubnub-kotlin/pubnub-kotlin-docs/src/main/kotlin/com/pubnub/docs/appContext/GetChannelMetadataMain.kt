package com.pubnub.docs.appContext
// https://www.pubnub.com/docs/sdks/kotlin/api-reference/objects#iteratively-update-existing-metadata

// snippet.iterativelyUpdateExistingMetadataMain
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.v2.PNConfiguration
import kotlin.test.assertEquals

fun main() {
    // create configuration
    val pnConfiguration = PNConfiguration.builder(userId = UserId("user01"), subscribeKey = "demo") {
        publishKey = "demo"
    }.build()

    // create PubNub
    val pubNub = PubNub.create(pnConfiguration)

    val channel = "channel01"
    val channelName = "Channel1on1"
    val channelDescription = "Channel for 1on1 conversation"
    val status = "active"
    val type = "1on1"
    val initialCustom = mapOf("Days" to "Mon-Fri")

    // set channelMetadata
    val channelMetadataAfterSet: PNChannelMetadataResult = pubNub.setChannelMetadata(
        channel = channel,
        name = channelName,
        description = channelDescription,
        status = status,
        type = type,
        custom = initialCustom
    ).sync()

    // get channelMetadata
    val channelMetadataAfterGet = pubNub.getChannelMetadata(channel = channel, includeCustom = true).sync().data

    //  Update metadata with additional custom data
    val updatedCustomMetadata = (channelMetadataAfterGet.custom?.value ?: emptyMap()) + mapOf("Months" to "Jan-May")
    val updatedChannelMetadata = pubNub.setChannelMetadata(
        channel = channelMetadataAfterGet.id,
        custom = updatedCustomMetadata,
        includeCustom = true
    ).sync()

    val updatedData = updatedChannelMetadata.data
    assertEquals(channel, updatedData.id)
    assertEquals(channelName, updatedData.name?.value)
    assertEquals(channelDescription, updatedData.description?.value)
    assertEquals(status, updatedData.status?.value)
    assertEquals(type, updatedData.type?.value)
    val expectedCustom = mapOf("Days" to "Mon-Fri", "Months" to "Jan-May")
    assertEquals(expectedCustom, updatedData.custom?.value)

    // cleanup
    pubNub.removeChannelMetadata(channel = channel).sync()
    kotlin.system.exitProcess(0)
}
// snippet.end

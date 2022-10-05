package com.pubnub.contract

import com.google.gson.Gson
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import java.io.File
import java.util.Locale

val gson = Gson()

fun readDataFile(personaName: String) = File(
    "src/test/resources/features/data/${
    personaName.lowercase(
        Locale.getDefault()
    )
    }.json"
).readText(Charsets.UTF_8)

inline fun <reified T> readResourceFromDataFile(dataFileName: String): T = gson.fromJson(readDataFile(dataFileName), T::class.java)

fun loadPersonaUUIDMetadata(personaName: String): PNUUIDMetadata = readResourceFromDataFile(personaName)
fun loadChannelMetadata(channelName: String): PNChannelMetadata = readResourceFromDataFile(channelName)

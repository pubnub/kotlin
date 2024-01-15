package com.pubnub.contract.objectV2.uuidmetadata.step

import com.google.gson.Gson
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata
import com.pubnub.api.models.consumer.objects_api.member.PNMembers
import com.pubnub.api.models.consumer.objects_api.membership.PNMembership
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata
import com.pubnub.contract.CONTRACT_TEST_CONFIG
import java.io.File

const val JSON_FILE_EXTENSION = "json"

inline fun <reified T> readResourceFromDataFile(fileName: String) : T {
    val resourceFileName = fileName.lowercase() + ".$JSON_FILE_EXTENSION"
    val personasLocation = CONTRACT_TEST_CONFIG.dataFileLocation()
    val personaAsString = File("$personasLocation/$resourceFileName").readText(Charsets.UTF_8)
    return Gson().fromJson(personaAsString, T::class.java)
}

fun loadPersonaUUIDMetadata(personaName: String): PNUUIDMetadata = readResourceFromDataFile(personaName)
fun loadChannelMetadata(channelFileName: String) : PNChannelMetadata = readResourceFromDataFile(channelFileName)
fun loadMember(memberFileName: String) : PNMembers = readResourceFromDataFile(memberFileName)
fun loadChannelMembership(membershipFilename: String) : PNMembership = readResourceFromDataFile(membershipFilename)

package com.pubnub.docs.support

import com.pubnub.docs.support.resources.AccessManager
import com.pubnub.docs.support.resources.AppContext
import com.pubnub.docs.support.resources.DataSync
import com.pubnub.docs.support.resources.GettingStarted
import com.pubnub.docs.support.resources.Messaging

/** The ids of DataSync objects a test touches. The janitor removes exactly these, see [JanitorDataSync]. Classes are never touched. */
data class DataSyncNames(
    val relationships: List<String> = emptyList(),
    val memberships: List<String> = emptyList(),
    val entities: List<String> = emptyList(),
    val users: List<String> = emptyList(),
    val channels: List<String> = emptyList(),
) {
    fun names(): List<String> = relationships + memberships + entities + users + channels
}

/** The literal names of one docs area. Data only. */
data class AreaResources(
    /** Channels with history, files, presence occupants or metadata. A channel of a channel group is listed here too. */
    val channels: List<String> = emptyList(),
    val channelGroups: List<String> = emptyList(),
    /** User ids: App Context users and presence user ids. */
    val users: List<String> = emptyList(),
    /** Push device tokens. */
    val pushDevices: List<String> = emptyList(),
    val dataSync: DataSyncNames = DataSyncNames(),
) {
    fun names(): List<String> = channels + channelGroups + users + dataSync.names()
}

/**
 * Every literal name the tests touch. The janitor cleans exactly these names and nothing else, and the marker lint fails on
 * a literal channel, group or user id in a snippet region that is not listed here. Add a name here in the same change that
 * adds the test.
 */
object Resources {
    /** The topic the push samples use. Set it together with [AreaResources.pushDevices]. */
    const val PUSH_TOPIC = "myapptopic"

    val areas: Map<String, AreaResources> =
        linkedMapOf(
            "messaging" to Messaging.resources,
            "getting-started" to GettingStarted.resources,
            "access-manager" to AccessManager.resources,
            "app-context" to AppContext.resources,
            "datasync" to DataSync.resources,
        )

    @JvmStatic
    fun channels(): List<String> = areas.values.flatMap { it.channels }.distinct().sorted()

    @JvmStatic
    fun channelGroups(): List<String> = areas.values.flatMap { it.channelGroups }.distinct().sorted()

    @JvmStatic
    fun users(): List<String> = areas.values.flatMap { it.users }.distinct().sorted()

    @JvmStatic
    fun pushDevices(): List<String> = areas.values.flatMap { it.pushDevices }.distinct().sorted()

    @JvmStatic
    fun dataSync(): DataSyncNames =
        DataSyncNames(
            relationships = areas.values.flatMap { it.dataSync.relationships }.distinct(),
            memberships = areas.values.flatMap { it.dataSync.memberships }.distinct(),
            entities = areas.values.flatMap { it.dataSync.entities }.distinct(),
            users = areas.values.flatMap { it.dataSync.users }.distinct(),
            channels = areas.values.flatMap { it.dataSync.channels }.distinct(),
        )

    /** Every name of every kind, for the lint. */
    @JvmStatic
    fun allNames(): Set<String> = areas.values.flatMap { it.names() }.toSet()
}

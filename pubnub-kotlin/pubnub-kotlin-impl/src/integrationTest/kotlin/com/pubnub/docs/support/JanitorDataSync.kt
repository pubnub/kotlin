package com.pubnub.docs.support

import com.pubnub.api.PubNub

/**
 * Cleans the DataSync objects named in [Resources]: relationships and memberships first, then entities, users and
 * channels. It removes by listed id only, never lists a class and never touches a class or the keyset configuration.
 * A missing object (404) is not a failure, so a second pass removes nothing.
 */
object JanitorDataSync {
    fun clean(
        client: PubNub,
        report: Janitor.Report,
    ) {
        val names = Resources.dataSync()
        val dataSync = client.dataSync
        names.relationships.forEach { id ->
            report.remove("datasync_relationships", "remove relationship $id") { dataSync.removeRelationship(id).sync() }
        }
        names.memberships.forEach { id ->
            report.remove("datasync_memberships", "remove membership $id") { dataSync.removeMembership(id).sync() }
        }
        names.entities.forEach { id ->
            report.remove("datasync_entities", "remove entity $id") { dataSync.removeEntity(id).sync() }
        }
        names.users.forEach { id ->
            report.remove("datasync_users", "remove user $id") { dataSync.removeUser(id).sync() }
        }
        names.channels.forEach { id ->
            report.remove("datasync_channels", "remove channel $id") { dataSync.removeChannel(id).sync() }
        }
    }
}

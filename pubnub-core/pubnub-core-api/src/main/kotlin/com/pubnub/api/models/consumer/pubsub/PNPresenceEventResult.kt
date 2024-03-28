package com.pubnub.api.models.consumer.pubsub

import com.google.gson.JsonElement

/**
 * Wrapper around a presence event.
 *
 * @property event The presence event. Could be `join`, `leave`, `state-change` or `interval`.
 * @property uuid The UUID which the presence event is related to.
 * @property timestamp The timestamp of the event.
 * @property occupancy Total number of users currently present in the `channel` in question.
 * @property state Presence state of the related UUID, if any.
 * @property channel The channel which the `event` is performed on.
 * @property subscription The related subscriptions.
 * @property timetoken The timetoken of the event.
 * @property join List of users that have *joined* the `channel` if the `event` is an `interval`.
 * This needs to be enabled under **presence_deltas** at the Admin Dashboard.
 * @property leave List of users that have *left* the `channel` if the `event` is an `interval`.
 * This needs to be enabled under **presence_deltas** at the Admin Dashboard.
 * @property timeout List of users that have *timed out* of the `channel` if the `event` is an `interval`.
 * This needs to be enabled under **presence_deltas** at the Admin Dashboard.
 * @property hereNowRefresh Indicates to the user that a manual HereNow should be called to get
 * the complete list of users present in the channel.
 * @property userMetadata User metadata if any.
 */
class PNPresenceEventResult(
    val event: String? = null,
    val uuid: String? = null,
    val timestamp: Long? = null,
    val occupancy: Int? = null,
    val state: JsonElement? = null,
    override val channel: String,
    override val subscription: String? = null,
    override val timetoken: Long? = null,
    val join: List<String>? = null,
    val leave: List<String>? = null,
    val timeout: List<String>? = null,
    val hereNowRefresh: Boolean? = null,
    val userMetadata: Any? = null,
) : PNEvent {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as PNPresenceEventResult

        if (event != other.event) {
            return false
        }
        if (uuid != other.uuid) {
            return false
        }
        if (timestamp != other.timestamp) {
            return false
        }
        if (occupancy != other.occupancy) {
            return false
        }
        if (state != other.state) {
            return false
        }
        if (channel != other.channel) {
            return false
        }
        if (subscription != other.subscription) {
            return false
        }
        if (timetoken != other.timetoken) {
            return false
        }
        if (join != other.join) {
            return false
        }
        if (leave != other.leave) {
            return false
        }
        if (timeout != other.timeout) {
            return false
        }
        if (hereNowRefresh != other.hereNowRefresh) {
            return false
        }
        if (userMetadata != other.userMetadata) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = event?.hashCode() ?: 0
        result = 31 * result + (uuid?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        result = 31 * result + (occupancy ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + channel.hashCode()
        result = 31 * result + (subscription?.hashCode() ?: 0)
        result = 31 * result + (timetoken?.hashCode() ?: 0)
        result = 31 * result + (join?.hashCode() ?: 0)
        result = 31 * result + (leave?.hashCode() ?: 0)
        result = 31 * result + (timeout?.hashCode() ?: 0)
        result = 31 * result + (hereNowRefresh?.hashCode() ?: 0)
        result = 31 * result + (userMetadata?.hashCode() ?: 0)
        return result
    }
}

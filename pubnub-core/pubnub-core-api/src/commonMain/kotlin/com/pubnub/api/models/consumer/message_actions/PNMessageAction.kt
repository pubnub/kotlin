package com.pubnub.api.models.consumer.message_actions

/**
 * Concrete implementation of a message action.
 *
 * Add or remove actions on published messages to build features like receipts,
 * reactions or to associate custom metadata to messages.
 *
 * Clients can subscribe to a channel to receive message action events on that channel.
 * They can also fetch past message actions from PubNub Storage independently or when they fetch original messages.
 *
 * @property type Message action's type.
 * @property value Message action's value.
 * @property messageTimetoken Timestamp when the actual message was created the message action belongs to.
 */

open class PNMessageAction(
    val type: String,
    val value: String,
    val messageTimetoken: Long,
) {
    class Builder {
        private var type: String? = null
        private var value: String? = null
        private var messageTimetoken: Long? = null

        fun setMessageTimetoken(timetoken: Long) = apply {
            this.messageTimetoken = timetoken
        }

        fun setValue(value: String) = apply {
            this.value = value
        }

        fun setType(type: String) = apply {
            this.type = type
        }

        fun build() = PNMessageAction(type!!, value!!, messageTimetoken!!)
    }

    constructor() : this("", "", 0L)

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || this::class != other::class) {
            return false
        }

        other as PNMessageAction

        if (type != other.type) {
            return false
        }
        if (value != other.value) {
            return false
        }
        if (messageTimetoken != other.messageTimetoken) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + messageTimetoken.hashCode()
        return result
    }
}

class PNSavedMessageAction(
    type: String,
    value: String,
    messageTimetoken: Long,
    val uuid: String,
    val actionTimetoken: Long,
) : PNMessageAction(type, value, messageTimetoken) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || this::class != other::class) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }

        other as PNSavedMessageAction

        if (uuid != other.uuid) {
            return false
        }
        if (actionTimetoken != other.actionTimetoken) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + uuid.hashCode()
        result = 31 * result + actionTimetoken.hashCode()
        return result
    }
}

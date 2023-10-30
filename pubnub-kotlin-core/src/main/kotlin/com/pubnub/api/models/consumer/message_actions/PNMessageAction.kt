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
    @JvmField var type: String,
    @JvmField var value: String,
    @JvmField var messageTimetoken: Long
) {

    constructor() : this ("", "", 0L)

    internal constructor(pnMessageAction: PNMessageAction) : this(
        pnMessageAction.type,
        pnMessageAction.value,
        pnMessageAction.messageTimetoken
    ) {
        this.uuid = pnMessageAction.uuid
        this.actionTimetoken = pnMessageAction.actionTimetoken
    }

    /**
     * Message action's author.
     */
    @JvmField var uuid: String? = null

    /**
     * Timestamp when the message action was created.
     */
    @JvmField var actionTimetoken: Long? = null

    fun setType(type: String): PNMessageAction {
        this.type = type
        return this
    }

    fun setValue(value: String): PNMessageAction {
        this.value = value
        return this
    }

    fun setMessageTimetoken(messageTimetoken: Long) : PNMessageAction {
        this.messageTimetoken = messageTimetoken
        return this
    }

    fun setUuid(uuid: String): PNMessageAction {
        this.uuid = uuid
        return this
    }

    fun setActionTimetoken(actionTimetoken: Long) : PNMessageAction {
        this.actionTimetoken = actionTimetoken
        return this
    }

    fun getType(): String {
        return this.type
    }

    fun getValue(): String {
        return this.value
    }

    fun getMessageTimetoken() : Long? {
        return this.messageTimetoken
    }

    fun getUuid(): String? {
        return this.uuid
    }

    fun getActionTimetoken() : Long? {
        return this.actionTimetoken
    }
}
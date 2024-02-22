package com.pubnub.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.History
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.access.Grant
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.DownloadFile
import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.endpoints.files.PublishFileMessage
import com.pubnub.api.endpoints.files.SendFile
import com.pubnub.api.endpoints.message_actions.AddMessageAction
import com.pubnub.api.endpoints.message_actions.GetMessageActions
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadata
import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
import com.pubnub.api.endpoints.objects.member.GetChannelMembers
import com.pubnub.api.endpoints.objects.member.ManageChannelMembers
import com.pubnub.api.endpoints.objects.membership.GetMemberships
import com.pubnub.api.endpoints.objects.membership.ManageMemberships
import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.endpoints.presence.WhereNow
import com.pubnub.api.endpoints.pubsub.Publish
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNToken
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import com.pubnub.api.models.consumer.pubsub.PNSignalResult
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult
import com.pubnub.api.models.consumer.pubsub.objects.PNObjectEventResult
import com.pubnub.api.v2.callbacks.EventEmitter
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.api.v2.entities.BaseChannel
import com.pubnub.api.v2.entities.BaseChannelGroup
import com.pubnub.api.v2.entities.BaseChannelMetadata
import com.pubnub.api.v2.entities.BaseUserMetadata
import com.pubnub.api.v2.entities.Channel
import com.pubnub.api.v2.entities.ChannelGroup
import com.pubnub.api.v2.entities.ChannelMetadata
import com.pubnub.api.v2.entities.UserMetadata
import com.pubnub.api.v2.subscription.Subscription
import com.pubnub.api.v2.subscription.SubscriptionSet
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.internal.callbacks.DelegatingEventListener
import com.pubnub.internal.callbacks.DelegatingStatusListener
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback
import com.pubnub.internal.models.consumer.objects.toInternal
import com.pubnub.internal.models.consumer.objects.toInternalChannelGrants
import com.pubnub.internal.models.consumer.objects.toInternalChannelGroupGrants
import com.pubnub.internal.models.consumer.objects.toInternalChannelMemberships
import com.pubnub.internal.models.consumer.objects.toInternalMemberInputs
import com.pubnub.internal.models.consumer.objects.toInternalSortKeys
import com.pubnub.internal.models.consumer.objects.toInternalSpacePermissions
import com.pubnub.internal.models.consumer.objects.toInternalUserPermissions
import com.pubnub.internal.models.consumer.objects.toInternalUuidGrants
import com.pubnub.internal.v2.entities.ChannelGroupImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelImpl
import com.pubnub.internal.v2.entities.ChannelMetadataImpl
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.entities.UserMetadataImpl
import com.pubnub.internal.v2.subscription.EmitterHelper
import com.pubnub.internal.v2.subscription.SubscriptionImpl
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl
import java.io.InputStream

class PubNubImpl(
    val configuration: PNConfiguration,
) : BasePubNubImpl<EventListener, Subscription, Channel, ChannelGroup, ChannelMetadata, UserMetadata, SubscriptionSet, StatusListener>(configuration), PubNub, EventEmitter {
    companion object {
        fun generateUUID() = BasePubNubImpl.generateUUID()
    }

    private val emitterHelper = EmitterHelper(listenerManager)
    override var onMessage: ((PNMessageResult) -> Unit)? by emitterHelper::onMessage
    override var onPresence: ((PNPresenceEventResult) -> Unit)? by emitterHelper::onPresence
    override var onSignal: ((PNSignalResult) -> Unit)? by emitterHelper::onSignal
    override var onMessageAction: ((PNMessageActionResult) -> Unit)? by emitterHelper::onMessageAction
    override var onObjects: ((PNObjectEventResult) -> Unit)? by emitterHelper::onObjects
    override var onFile: ((PNFileEventResult) -> Unit)? by emitterHelper::onFile


    override fun addListener(listener: SubscribeCallback) {
        listenerManager.addListener(DelegatingSubscribeCallback(listener))
    }

    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: StatusListener) {
        listenerManager.addListener(DelegatingStatusListener(listener))
    }

    /**
     * Add a listener.
     *
     * @param listener The listener to be added.
     */
    override fun addListener(listener: EventListener) {
        listenerManager.addListener(DelegatingEventListener(listener))
    }

    /**
     * Remove a listener.
     *
     * @param listener The listener to be removed, previously added with [addListener].
     */
    override fun removeListener(listener: Listener) {
        listenerManager.removeListener(listener)
    }

    /**
     * Removes all listeners.
     */
    override fun removeAllListeners() {
        listenerManager.removeAllListeners()
    }

    /**
     * Create a handle to a [BaseChannel] that can be used to obtain a [BaseSubscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel is required.
     *
     * The returned [BaseChannel] holds a reference to this [PubNubImpl] instance internally.
     *
     * @param name the name of the channel to return. Supports wildcards by ending it with ".*". See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/overview)
     *
     * @return a [BaseChannel] instance representing the channel with the given [name]
     */
    override fun channel(name: String): Channel {
        return ChannelImpl(internalPubnub, ChannelName(name))
    }

    /**
     * Create a handle to a [BaseChannelGroup] that can be used to obtain a [BaseSubscription].
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a channel group is required.
     *
     * The returned [BaseChannelGroup] holds a reference to this [PubNubImpl] instance internally.
     *
     * @param name the name of the channel group to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/channels/subscribe#channel-groups)
     *
     * @return a [BaseChannelGroup] instance representing the channel group with the given [name]
     */
    override fun channelGroup(name: String): ChannelGroup {
        return ChannelGroupImpl(internalPubnub, ChannelGroupName(name))
    }

    /**
     * Create a handle to a [BaseChannelMetadata] object that can be used to obtain a [BaseSubscription] to metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a metadata channel is required.
     *
     * The returned [BaseChannelMetadata] holds a reference to this [PubNubImpl] instance internally.
     *
     * @param id the id of the channel metadata to return. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/channel-metadata)
     *
     * @return a [BaseChannelMetadata] instance representing the channel metadata with the given [id]
     */
    override fun channelMetadata(id: String): ChannelMetadata {
        return ChannelMetadataImpl(internalPubnub, ChannelName(id))
    }

    /**
     * Create a handle to a [BaseUserMetadata] object that can be used to obtain a [BaseSubscription] to user metadata events.
     *
     * The function is cheap to call, and the returned object is lightweight, as it doesn't change any client or server
     * state. It is therefore permitted to use this method whenever a representation of a user metadata is required.
     *
     * The returned [BaseUserMetadata] holds a reference to this [PubNubImpl] instance internally.
     *
     * @param id the id of the user. See more in the
     * [documentation](https://www.pubnub.com/docs/general/metadata/users-metadata)
     *
     * @return a [BaseUserMetadata] instance representing the channel metadata with the given [id]
     */
    override fun userMetadata(id: String): UserMetadata {
        return UserMetadataImpl(internalPubnub, ChannelName(id))
    }

    /**
     * Create a [BaseSubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [BaseSubscriptionSet]
     * @return a [BaseSubscriptionSet] containing all [subscriptions]
     */
    override fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet {
        return SubscriptionSetImpl(internalPubnub, subscriptions as Set<SubscriptionImpl>)
    }

    /**
     * Create a [BaseSubscriptionSet] containing [BaseSubscription] objects for the given sets of [channels] and
     * [channelGroups].
     *
     * Please note that the subscriptions are not active until you call [BaseSubscriptionSet.subscribe].
     *
     * This is a convenience method, and it is equal to calling [PubNubImpl.channel] followed by [BaseChannel.subscription] for
     * each channel, then creating a [subscriptionSetOf] using the returned [BaseSubscription] objects (and similarly for
     * channel groups).
     *
     * @param channels the channels to create subscriptions for
     * @param channelGroups the channel groups to create subscriptions for
     * @param options the [SubscriptionOptions] to pass for each subscription. Refer to supported options in [BaseChannel] and
     * [BaseChannelGroup] documentation.
     * @return a [BaseSubscriptionSet] containing subscriptions for the given [channels] and [channelGroups]
     */
    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions
    ): SubscriptionSet {
        return super.subscriptionSetOf(channels, channelGroups, options) as SubscriptionSet
    }

    fun publish(
        channel: String,
        message: Any,
        meta: Any? = null,
        shouldStore: Boolean? = null,
        usePost: Boolean = false,
        replicate: Boolean = true,
        ttl: Int? = null
    ): Publish {
        return Publish(
            internalPubnub.publish(
                channel,
                message,
                meta,
                shouldStore,
                usePost,
                replicate,
                ttl
            )
        )
    }

    fun fire(
        channel: String,
        message: Any,
        meta: Any? = null,
        usePost: Boolean = false,
        ttl: Int? = null
    ): Publish {
        return Publish(internalPubnub.fire(channel, message, meta, usePost, ttl))
    }

    fun signal(channel: String, message: Any): Signal {
        return Signal(internalPubnub.signal(channel, message))
    }

    fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): AddChannelsToPush {
        return AddChannelsToPush(
            internalPubnub.addPushNotificationsOnChannels(
                pushType, channels, deviceId, topic, environment
            )
        )
    }

    fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): ListPushProvisions {
        return ListPushProvisions(internalPubnub.auditPushChannelProvisions(pushType, deviceId, topic, environment))
    }

    fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): RemoveChannelsFromPush {
        return RemoveChannelsFromPush(
            internalPubnub.removePushNotificationsFromChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment
            )
        )
    }

    fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String? = null,
        environment: PNPushEnvironment = PNPushEnvironment.DEVELOPMENT
    ): RemoveAllPushChannelsForDevice {
        return RemoveAllPushChannelsForDevice(
            internalPubnub.removeAllPushNotificationsFromDeviceWithPushToken(pushType, deviceId, topic, environment)
        )
    }

    fun history(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        count: Int = com.pubnub.internal.endpoints.History.MAX_COUNT,
        reverse: Boolean = false,
        includeTimetoken: Boolean = false,
        includeMeta: Boolean = false
    ): History {
        return History(internalPubnub.history(channel, start, end, count, reverse, includeTimetoken, includeMeta))
    }

    fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage = PNBoundedPage(),
        includeUUID: Boolean = true,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true
    ): FetchMessages {
        return FetchMessages(
            internalPubnub.fetchMessages(
                channels,
                page,
                includeUUID,
                includeMeta,
                includeMessageActions,
                includeMessageType
            )
        )
    }

    fun deleteMessages(
        channels: List<String>,
        start: Long? = null,
        end: Long? = null
    ): DeleteMessages {
        return DeleteMessages(internalPubnub.deleteMessages(channels, start, end))
    }

    fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
        return MessageCounts(internalPubnub.messageCounts(channels, channelsTimetoken))
    }

    fun hereNow(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        includeState: Boolean = false,
        includeUUIDs: Boolean = true
    ): HereNow {
        return HereNow(internalPubnub.hereNow(channels, channelGroups, includeState, includeUUIDs))
    }

    fun whereNow(uuid: String = configuration.userId.value): WhereNow {
        return WhereNow(internalPubnub.whereNow(uuid))
    }

    fun setPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        state: Any,
        uuid: String = configuration.userId.value
    ): SetState {
        return SetState(internalPubnub.setPresenceState(channels, channelGroups, state, uuid))
    }

    fun getPresenceState(
        channels: List<String> = listOf(),
        channelGroups: List<String> = listOf(),
        uuid: String = configuration.userId.value
    ): GetState {
        return GetState(internalPubnub.getPresenceState(channels, channelGroups, uuid))
    }

    fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction
    ): AddMessageAction {
        return AddMessageAction(internalPubnub.addMessageAction(channel, messageAction))
    }

    fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction {
        return RemoveMessageAction(internalPubnub.removeMessageAction(channel, messageTimetoken, actionTimetoken))
    }

    fun getMessageActions(
        channel: String,
        page: PNBoundedPage = PNBoundedPage()
    ): GetMessageActions {
        return GetMessageActions(internalPubnub.getMessageActions(channel, page))
    }

    fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
        return AddChannelChannelGroup(
            internalPubnub.addChannelsToChannelGroup(channels, channelGroup)
        )
    }

    fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        return AllChannelsChannelGroup(
            internalPubnub.listChannelsForChannelGroup(channelGroup)
        )
    }

    fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup {
        return RemoveChannelChannelGroup(
            internalPubnub.removeChannelsFromChannelGroup(channels, channelGroup)
        )
    }

    fun listAllChannelGroups(): ListAllChannelGroup {
        return ListAllChannelGroup(internalPubnub.listAllChannelGroups())
    }

    fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        return DeleteChannelGroup(internalPubnub.deleteChannelGroup(channelGroup))
    }

    fun grant(
        read: Boolean = false,
        write: Boolean = false,
        manage: Boolean = false,
        delete: Boolean = false,
        ttl: Int = -1,
        authKeys: List<String> = emptyList(),
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList()
    ): Grant = Grant(internalPubnub.grant(read, write, manage, delete, ttl, authKeys, channels, channelGroups))

    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUUID: String? = null,
        channels: List<ChannelGrant> = emptyList(),
        channelGroups: List<ChannelGroupGrant> = emptyList(),
        uuids: List<UUIDGrant> = emptyList()
    ): GrantToken {
        return GrantToken(
            internalPubnub.grantToken(
                ttl,
                meta,
                authorizedUUID,
                channels.toInternalChannelGrants(),
                channelGroups.toInternalChannelGroupGrants(),
                uuids.toInternalUuidGrants()
            )
        )
    }

    fun grantToken(
        ttl: Int,
        meta: Any? = null,
        authorizedUserId: UserId? = null,
        spacesPermissions: List<SpacePermissions> = emptyList(),
        usersPermissions: List<UserPermissions> = emptyList()
    ): GrantToken {
        return GrantToken(
            internalPubnub.grantToken(
                ttl,
                meta,
                authorizedUserId,
                spacesPermissions.toInternalSpacePermissions(),
                usersPermissions.toInternalUserPermissions(),
            )
        )
    }

    fun revokeToken(token: String): RevokeToken {
        return RevokeToken(
            internalPubnub.revokeToken(token)
        )
    }

    fun time(): Time {
        return Time(internalPubnub.time())
    }

    fun getAllChannelMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ): GetAllChannelMetadata {
        return GetAllChannelMetadata(
            internalPubnub.getAllChannelMetadata(
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom
            )
        )
    }

    fun getChannelMetadata(channel: String, includeCustom: Boolean = false): GetChannelMetadata {
        return GetChannelMetadata(
            internalPubnub.getChannelMetadata(channel, includeCustom)
        )
    }

    fun setChannelMetadata(
        channel: String,
        name: String? = null,
        description: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null
    ): SetChannelMetadata {
        return SetChannelMetadata(
            internalPubnub.setChannelMetadata(channel, name, description, custom, includeCustom, type, status)
        )
    }

    fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        return RemoveChannelMetadata(
            internalPubnub.removeChannelMetadata(channel)
        )
    }

    fun getAllUUIDMetadata(
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false
    ): GetAllUUIDMetadata {
        return GetAllUUIDMetadata(
            internalPubnub.getAllUUIDMetadata(limit, page, filter, sort.toInternalSortKeys(), includeCount, includeCustom)
        )
    }

    fun getUUIDMetadata(
        uuid: String? = null,
        includeCustom: Boolean = false
    ): GetUUIDMetadata {
        return GetUUIDMetadata(
            internalPubnub.getUUIDMetadata(uuid, includeCustom)
        )
    }

    fun setUUIDMetadata(
        uuid: String? = null,
        name: String? = null,
        externalId: String? = null,
        profileUrl: String? = null,
        email: String? = null,
        custom: Any? = null,
        includeCustom: Boolean = false,
        type: String? = null,
        status: String? = null
    ): SetUUIDMetadata {
        return SetUUIDMetadata(
            internalPubnub.setUUIDMetadata(uuid, name, externalId, profileUrl, email, custom, includeCustom, type, status)
        )
    }

    fun removeUUIDMetadata(uuid: String? = null): RemoveUUIDMetadata {
        return RemoveUUIDMetadata(
            internalPubnub.removeUUIDMetadata(uuid)
        )
    }

    fun getMemberships(
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): GetMemberships {
        return GetMemberships(
            internalPubnub.getMemberships(
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun setMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
    ): ManageMemberships {
        return ManageMemberships(
            internalPubnub.setMemberships(
                channels.toInternalChannelMemberships(),
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun removeMemberships(
        channels: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships {
        return ManageMemberships(
            internalPubnub.removeMemberships(
                channels,
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun manageMemberships(
        channelsToSet: List<ChannelMembershipInput>,
        channelsToRemove: List<String>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null,
    ): ManageMemberships {
        return ManageMemberships(
            internalPubnub.manageMemberships(
                channelsToSet.toInternalChannelMemberships(),
                channelsToRemove,
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    fun getChannelMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): GetChannelMembers {
        return GetChannelMembers(
            internalPubnub.getChannelMembers(
                channel,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    @Deprecated(
        replaceWith = ReplaceWith(
            "fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end, limit = maximumPerChannel),includeMeta = includeMeta, includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)",
            "com.pubnub.api.models.consumer.PNBoundedPage"
        ),
        level = DeprecationLevel.ERROR,
        message = "Use fetchMessages(String, PNBoundedPage, Boolean, Boolean, Boolean) instead"
    )
    fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int = 0,
        start: Long? = null,
        end: Long? = null,
        includeMeta: Boolean = false,
        includeMessageActions: Boolean = false,
        includeMessageType: Boolean = true
    ): FetchMessages {
        return FetchMessages(
            internalPubnub.fetchMessages(
                channels, maximumPerChannel, start, end, includeMeta, includeMessageActions, includeMessageType
            )
        )
    }

    @Deprecated(
        replaceWith = ReplaceWith(
            "getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))",
            "com.pubnub.api.models.consumer.PNBoundedPage"
        ),
        level = DeprecationLevel.ERROR,
        message = "Use getMessageActions(String, PNBoundedPage) instead"
    )
    fun getMessageActions(
        channel: String,
        start: Long? = null,
        end: Long? = null,
        limit: Int? = null
    ): GetMessageActions {
        return GetMessageActions(
            internalPubnub.getMessageActions(channel, start, end, limit)
        )
    }

    @Deprecated(
        replaceWith = ReplaceWith(
            "setMemberships(channels = channels, uuid = uuid, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                    "includeChannelDetails = includeChannelDetails)"
        ),
        level = DeprecationLevel.ERROR,
        message = "Use setMemberships instead"
    )
    fun addMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String? = null,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMembershipKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeChannelDetails: PNChannelDetailsLevel? = null
    ): ManageMemberships {
        return ManageMemberships(
            internalPubnub.addMemberships(
                channels.toInternalChannelMemberships(),
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal()
            )
        )
    }

    @Deprecated(
        "Use getChannelMembers instead",
        replaceWith = ReplaceWith("getChannelMembers(channel = channel, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)"),
        level = DeprecationLevel.ERROR
    )
    fun getMembers(
        channel: String,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): GetChannelMembers {
        return GetChannelMembers(
            internalPubnub.getMembers(
                channel,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    @Deprecated(
        "Use setChannelMembers instead",
        replaceWith = ReplaceWith("setChannelMembers(channel = channel, uuids = uuids, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)"),
        level = DeprecationLevel.ERROR
    )
    fun addMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers {
        return ManageChannelMembers(
            internalPubnub.addMembers(
                channel,
                uuids.toInternalMemberInputs(),
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun setChannelMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): ManageChannelMembers {
        return ManageChannelMembers(
            internalPubnub.setChannelMembers(
                channel,
                uuids.toInternalMemberInputs(),
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    @Deprecated(
        "Use removeChannelMembers instead",
        replaceWith = ReplaceWith("removeChannelMembers(channel = channel, uuids = uuids, limit = limit, page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)"),
        level = DeprecationLevel.ERROR
    )
    fun removeMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers {
        return ManageChannelMembers(
            internalPubnub.removeMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null
    ): ManageChannelMembers {
        return ManageChannelMembers(
            internalPubnub.removeChannelMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun manageChannelMembers(
        channel: String,
        uuidsToSet: Collection<MemberInput>,
        uuidsToRemove: Collection<String>,
        limit: Int? = null,
        page: PNPage? = null,
        filter: String? = null,
        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
        includeCount: Boolean = false,
        includeCustom: Boolean = false,
        includeUUIDDetails: PNUUIDDetailsLevel? = null,
    ): ManageChannelMembers {
        return ManageChannelMembers(
            internalPubnub.manageChannelMembers(
                channel,
                uuidsToSet.toInternalMemberInputs(),
                uuidsToRemove,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal()
            )
        )
    }

    fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null,
        cipherKey: String? = null
    ): SendFile {
        return SendFile(
            internalPubnub.sendFile(
                channel, fileName, inputStream, message, meta, ttl, shouldStore, cipherKey
            )
        )
    }

    fun listFiles(
        channel: String,
        limit: Int? = null,
        next: PNPage.PNNext? = null
    ): ListFiles {
        return ListFiles(
            internalPubnub.listFiles(channel, limit, next)
        )
    }

    fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
        return GetFileUrl(
            internalPubnub.getFileUrl(channel, fileName, fileId)
        )
    }

    fun downloadFile(channel: String, fileName: String, fileId: String, cipherKey: String? = null): DownloadFile {
        return DownloadFile(
            internalPubnub.downloadFile(channel, fileName, fileId, cipherKey)
        )
    }

    fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
        return DeleteFile(
            internalPubnub.deleteFile(channel, fileName, fileId)
        )
    }

    fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any? = null,
        meta: Any? = null,
        ttl: Int? = null,
        shouldStore: Boolean? = null
    ): PublishFileMessage {
        return PublishFileMessage(
            internalPubnub.publishFileMessage(channel, fileName, fileId, message, meta, ttl, shouldStore)
        )
    }

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    fun getSubscribedChannels(): List<String> = internalPubnub.getSubscribedChannels()

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    fun getSubscribedChannelGroups(): List<String> = internalPubnub.getSubscribedChannelGroups()

    /**
     * Track the online and offline status of users and devices in real time and store custom state information.
     * When you have Presence enabled, PubNub automatically creates a presence channel for each channel.
     *
     * Subscribing to a presence channel or presence channel group will only return presence events
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    fun presence(
        channels: List<String> = emptyList(),
        channelGroups: List<String> = emptyList(),
        connected: Boolean = false
    ) = internalPubnub.presence(channels, channelGroups, connected)

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
     *
     * @param inputString String to be decrypted.
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    fun decrypt(inputString: String): String = internalPubnub.decrypt(inputString)

    /**
     * Perform Cryptographic decryption of an input string using a cipher key.
     *
     * @param inputString String to be decrypted.
     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    fun decrypt(inputString: String, cipherKey: String? = null): String = internalPubnub.decrypt(inputString, cipherKey)

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for decryption. If not provided [PNConfiguration.cipherKey] is used.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    fun decryptInputStream(inputStream: InputStream, cipherKey: String? = null): InputStream =
        internalPubnub.decryptInputStream(inputStream, cipherKey)

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the encryption of `inputString` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    fun encrypt(inputString: String, cipherKey: String? = null): String = internalPubnub.encrypt(inputString, cipherKey)

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. If not provided [PNConfiguration.cipherKey] is used.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    fun encryptInputStream(inputStream: InputStream, cipherKey: String? = null): InputStream =
        internalPubnub.encryptInputStream(inputStream, cipherKey)

    /**
     * Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status]
     */
    fun reconnect(timetoken: Long = 0L) = internalPubnub.reconnect(timetoken)

    /**
     * Cancel any subscribe and heartbeat loops or ongoing re-connections.
     *
     * Monitor the results in [SubscribeCallback.status]
     */
    fun disconnect() = internalPubnub.disconnect()

    fun parseToken(token: String): PNToken {
        return internalPubnub.parseToken(token)
    }

    fun setToken(token: String?) {
        return internalPubnub.setToken(token)
    }
}


package com.pubnub.internal

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.UserId
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.crypto.CryptoModule
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
import com.pubnub.api.v2.BasePNConfiguration
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
import com.pubnub.api.v2.subscriptions.BaseSubscription
import com.pubnub.api.v2.subscriptions.BaseSubscriptionSet
import com.pubnub.api.v2.subscriptions.Subscription
import com.pubnub.api.v2.subscriptions.SubscriptionOptions
import com.pubnub.api.v2.subscriptions.SubscriptionSet
import com.pubnub.internal.models.toInternal
import com.pubnub.internal.models.toInternalChannelGrants
import com.pubnub.internal.models.toInternalChannelGroupGrants
import com.pubnub.internal.models.toInternalChannelMemberships
import com.pubnub.internal.models.toInternalMemberInputs
import com.pubnub.internal.models.toInternalSortKeys
import com.pubnub.internal.models.toInternalSpacePermissions
import com.pubnub.internal.models.toInternalUserPermissions
import com.pubnub.internal.models.toInternalUuidGrants
import com.pubnub.internal.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.v2.callbacks.DelegatingStatusListener
import com.pubnub.internal.v2.callbacks.DelegatingSubscribeCallback
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

private const val PNSDK_PUBNUB_KOTLIN = "PubNub-Kotlin"

class PubNubImpl(
    override val configuration: BasePNConfiguration,
) : BasePubNubImpl<EventListener, Subscription, Channel, ChannelGroup, ChannelMetadata, UserMetadata, SubscriptionSet, StatusListener>(
        configuration,
        PNSDK_PUBNUB_KOTLIN,
    ),
    PubNub {
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

    override fun addListener(listener: StatusListener) {
        listenerManager.addListener(DelegatingStatusListener(listener))
    }

    override fun addListener(listener: EventListener) {
        listenerManager.addListener(DelegatingEventListener(listener))
    }

    override fun removeListener(listener: Listener) {
        when (listener) {
            is SubscribeCallback -> {
                super.removeListener(DelegatingSubscribeCallback(listener))
            }

            is EventListener -> {
                super.removeListener(DelegatingEventListener(listener))
            }

            else -> {
                super.removeListener(listener)
            }
        }
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
        return ChannelImpl(this, ChannelName(name))
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
        return ChannelGroupImpl(this, ChannelGroupName(name))
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
        return ChannelMetadataImpl(this, ChannelName(id))
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
        return UserMetadataImpl(this, ChannelName(id))
    }

    /**
     * Create a [BaseSubscriptionSet] from the given [subscriptions].
     *
     * @param subscriptions the subscriptions that will be added to the returned [BaseSubscriptionSet]
     * @return a [BaseSubscriptionSet] containing all [subscriptions]
     */
    override fun subscriptionSetOf(subscriptions: Set<Subscription>): SubscriptionSet {
        return SubscriptionSetImpl(pubNubCore, subscriptions as Set<SubscriptionImpl>)
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
     * @param options the [SubscriptionOptions] to pass for each subscriptions. Refer to supported options in [BaseChannel] and
     * [BaseChannelGroup] documentation.
     * @return a [BaseSubscriptionSet] containing subscriptions for the given [channels] and [channelGroups]
     */
    override fun subscriptionSetOf(
        channels: Set<String>,
        channelGroups: Set<String>,
        options: SubscriptionOptions,
    ): SubscriptionSet {
        return super.subscriptionSetOf(channels, channelGroups, options) as SubscriptionSet
    }

    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?,
    ): Publish {
        return com.pubnub.internal.endpoints.pubsub.PublishImpl(
            pubNubCore.publish(
                channel,
                message,
                meta,
                shouldStore,
                usePost,
                replicate,
                ttl,
            ),
        )
    }

    override fun fire(
        channel: String,
        message: Any,
        meta: Any?,
        usePost: Boolean,
        ttl: Int?,
    ): Publish {
        return com.pubnub.internal.endpoints.pubsub.PublishImpl(
            pubNubCore.fire(
                channel,
                message,
                meta,
                usePost,
                ttl,
            ),
        )
    }

    override fun signal(
        channel: String,
        message: Any,
    ): Signal {
        return com.pubnub.internal.endpoints.pubsub.SignalImpl(pubNubCore.signal(channel, message))
    }

    /**
     * Unsubscribe from all channels and all channel groups
     */
    override fun unsubscribeAll() {
        pubNubCore.unsubscribeAll()
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long,
    ) {
        pubNubCore.subscribe(channels, channelGroups, withPresence, withTimetoken)
    }

    /**
     * When subscribed to a single channel, this function causes the client to issue a leave from the channel
     * and close any open socket to the PubNub Network.
     *
     * For multiplexed channels, the specified channel(s) will be removed and the socket remains open
     * until there are no more channels remaining in the list.
     *
     * * **WARNING**
     * Unsubscribing from all the channel(s) and then subscribing to a new channel Y isn't the same as
     * Subscribing to channel Y and then unsubscribing from the previously subscribed channel(s).
     *
     * Unsubscribing from all the channels resets the timetoken and thus,
     * there could be some gaps in the subscriptions that may lead to a message loss.
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    override fun unsubscribe(
        channels: List<String>,
        channelGroups: List<String>,
    ) {
        pubNubCore.unsubscribe(channels, channelGroups)
    }

    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): AddChannelsToPush {
        return com.pubnub.internal.endpoints.push.AddChannelsToPushImpl(
            pubNubCore.addPushNotificationsOnChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment,
            ),
        )
    }

    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): ListPushProvisions {
        return com.pubnub.internal.endpoints.push.ListPushProvisionsImpl(
            pubNubCore.auditPushChannelProvisions(
                pushType,
                deviceId,
                topic,
                environment,
            ),
        )
    }

    override fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): RemoveChannelsFromPush {
        return com.pubnub.internal.endpoints.push.RemoveChannelsFromPushImpl(
            pubNubCore.removePushNotificationsFromChannels(
                pushType,
                channels,
                deviceId,
                topic,
                environment,
            ),
        )
    }

    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment,
    ): RemoveAllPushChannelsForDevice {
        return com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDeviceImpl(
            pubNubCore.removeAllPushNotificationsFromDeviceWithPushToken(
                pushType,
                deviceId,
                topic,
                environment,
            ),
        )
    }

    override fun history(
        channel: String,
        start: Long?,
        end: Long?,
        count: Int,
        reverse: Boolean,
        includeTimetoken: Boolean,
        includeMeta: Boolean,
    ): History {
        return com.pubnub.internal.endpoints.HistoryImpl(
            pubNubCore.history(
                channel,
                start,
                end,
                count,
                reverse,
                includeTimetoken,
                includeMeta,
            ),
        )
    }

    override fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean,
    ): FetchMessages {
        return com.pubnub.internal.endpoints.FetchMessagesImpl(
            pubNubCore.fetchMessages(
                channels,
                page,
                includeUUID,
                includeMeta,
                includeMessageActions,
                includeMessageType,
            ),
        )
    }

    override fun deleteMessages(
        channels: List<String>,
        start: Long?,
        end: Long?,
    ): DeleteMessages {
        return com.pubnub.internal.endpoints.DeleteMessagesImpl(
            pubNubCore.deleteMessages(
                channels,
                start,
                end,
            ),
        )
    }

    override fun messageCounts(
        channels: List<String>,
        channelsTimetoken: List<Long>,
    ): MessageCounts {
        return com.pubnub.internal.endpoints.MessageCountsImpl(
            pubNubCore.messageCounts(
                channels,
                channelsTimetoken,
            ),
        )
    }

    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean,
    ): HereNow {
        return com.pubnub.internal.endpoints.presence.HereNowImpl(
            pubNubCore.hereNow(
                channels,
                channelGroups,
                includeState,
                includeUUIDs,
            ),
        )
    }

    override fun whereNow(uuid: String): WhereNow {
        return com.pubnub.internal.endpoints.presence.WhereNowImpl(pubNubCore.whereNow(uuid))
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
        uuid: String,
    ): SetState {
        return com.pubnub.internal.endpoints.presence.SetStateImpl(
            pubNubCore.setPresenceState(
                channels,
                channelGroups,
                state,
                uuid,
            ),
        )
    }

    override fun getPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        uuid: String,
    ): GetState {
        return com.pubnub.internal.endpoints.presence.GetStateImpl(
            pubNubCore.getPresenceState(
                channels,
                channelGroups,
                uuid,
            ),
        )
    }

    override fun addMessageAction(
        channel: String,
        messageAction: PNMessageAction,
    ): AddMessageAction {
        return com.pubnub.internal.endpoints.message_actions.AddMessageActionImpl(
            pubNubCore.addMessageAction(
                channel,
                messageAction,
            ),
        )
    }

    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long,
    ): RemoveMessageAction {
        return com.pubnub.internal.endpoints.message_actions.RemoveMessageActionImpl(
            pubNubCore.removeMessageAction(
                channel,
                messageTimetoken,
                actionTimetoken,
            ),
        )
    }

    override fun getMessageActions(
        channel: String,
        page: PNBoundedPage,
    ): GetMessageActions {
        return com.pubnub.internal.endpoints.message_actions.GetMessageActionsImpl(
            pubNubCore.getMessageActions(
                channel,
                page,
            ),
        )
    }

    override fun addChannelsToChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): AddChannelChannelGroup {
        return com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroupImpl(
            pubNubCore.addChannelsToChannelGroup(channels, channelGroup),
        )
    }

    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        return com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroupImpl(
            pubNubCore.listChannelsForChannelGroup(channelGroup),
        )
    }

    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String,
    ): RemoveChannelChannelGroup {
        return com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroupImpl(
            pubNubCore.removeChannelsFromChannelGroup(channels, channelGroup),
        )
    }

    override fun listAllChannelGroups(): ListAllChannelGroup {
        return com.pubnub.internal.endpoints.channel_groups.ListAllChannelGroupImpl(pubNubCore.listAllChannelGroups())
    }

    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        return com.pubnub.internal.endpoints.channel_groups.DeleteChannelGroupImpl(
            pubNubCore.deleteChannelGroup(
                channelGroup,
            ),
        )
    }

    override fun grant(
        read: Boolean,
        write: Boolean,
        manage: Boolean,
        delete: Boolean,
        get: Boolean,
        update: Boolean,
        join: Boolean,
        ttl: Int,
        authKeys: List<String>,
        channels: List<String>,
        channelGroups: List<String>,
        uuids: List<String>,
    ): Grant =
        com.pubnub.internal.endpoints.access.GrantImpl(
            pubNubCore.grant(
                read,
                write,
                manage,
                delete,
                get,
                update,
                join,
                ttl,
                authKeys,
                channels,
                channelGroups,
                uuids,
            ),
        )

    override fun grant(
        read: Boolean,
        write: Boolean,
        manage: Boolean,
        delete: Boolean,
        ttl: Int,
        authKeys: List<String>,
        channels: List<String>,
        channelGroups: List<String>,
        uuids: List<String>,
    ): Grant =
        com.pubnub.internal.endpoints.access.GrantImpl(
            pubNubCore.grant(
                read,
                write,
                manage,
                delete,
                ttl,
                authKeys,
                channels,
                channelGroups,
                uuids,
            ),
        )

    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>,
    ): GrantToken {
        return com.pubnub.internal.endpoints.access.GrantTokenImpl(
            pubNubCore.grantToken(
                ttl,
                meta,
                authorizedUUID,
                channels.toInternalChannelGrants(),
                channelGroups.toInternalChannelGroupGrants(),
                uuids.toInternalUuidGrants(),
            ),
        )
    }

    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUserId: UserId?,
        spacesPermissions: List<SpacePermissions>,
        usersPermissions: List<UserPermissions>,
    ): GrantToken {
        return com.pubnub.internal.endpoints.access.GrantTokenImpl(
            pubNubCore.grantToken(
                ttl,
                meta,
                authorizedUserId,
                spacesPermissions.toInternalSpacePermissions(),
                usersPermissions.toInternalUserPermissions(),
            ),
        )
    }

    override fun revokeToken(token: String): RevokeToken {
        return com.pubnub.internal.endpoints.access.RevokeTokenImpl(
            pubNubCore.revokeToken(token),
        )
    }

    override fun time(): Time {
        return com.pubnub.internal.endpoints.TimeImpl(pubNubCore.time())
    }

    override fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
    ): GetAllChannelMetadata {
        return com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadataImpl(
            pubNubCore.getAllChannelMetadata(
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
            ),
        )
    }

    override fun getChannelMetadata(
        channel: String,
        includeCustom: Boolean,
    ): GetChannelMetadata {
        return com.pubnub.internal.endpoints.objects.channel.GetChannelMetadataImpl(
            pubNubCore.getChannelMetadata(channel, includeCustom),
        )
    }

    override fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?,
    ): SetChannelMetadata {
        return com.pubnub.internal.endpoints.objects.channel.SetChannelMetadataImpl(
            pubNubCore.setChannelMetadata(channel, name, description, custom, includeCustom, type, status),
        )
    }

    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        return com.pubnub.internal.endpoints.objects.channel.RemoveChannelMetadataImpl(
            pubNubCore.removeChannelMetadata(channel),
        )
    }

    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
    ): GetAllUUIDMetadata {
        return com.pubnub.internal.endpoints.objects.uuid.GetAllUUIDMetadataImpl(
            pubNubCore.getAllUUIDMetadata(
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
            ),
        )
    }

    override fun getUUIDMetadata(
        uuid: String?,
        includeCustom: Boolean,
    ): GetUUIDMetadata {
        return com.pubnub.internal.endpoints.objects.uuid.GetUUIDMetadataImpl(
            pubNubCore.getUUIDMetadata(uuid, includeCustom),
        )
    }

    override fun setUUIDMetadata(
        uuid: String?,
        name: String?,
        externalId: String?,
        profileUrl: String?,
        email: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?,
    ): SetUUIDMetadata {
        return com.pubnub.internal.endpoints.objects.uuid.SetUUIDMetadataImpl(
            pubNubCore.setUUIDMetadata(
                uuid,
                name,
                externalId,
                profileUrl,
                email,
                custom,
                includeCustom,
                type,
                status,
            ),
        )
    }

    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
        return com.pubnub.internal.endpoints.objects.uuid.RemoveUUIDMetadataImpl(
            pubNubCore.removeUUIDMetadata(uuid),
        )
    }

    override fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
    ): GetMemberships {
        return com.pubnub.internal.endpoints.objects.membership.GetMembershipsImpl(
            pubNubCore.getMemberships(
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal(),
            ),
        )
    }

    override fun setMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
    ): ManageMemberships {
        return com.pubnub.internal.endpoints.objects.membership.ManageMembershipsImpl(
            pubNubCore.setMemberships(
                channels.toInternalChannelMemberships(),
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal(),
            ),
        )
    }

    override fun removeMemberships(
        channels: List<String>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
    ): ManageMemberships {
        return com.pubnub.internal.endpoints.objects.membership.ManageMembershipsImpl(
            pubNubCore.removeMemberships(
                channels,
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal(),
            ),
        )
    }

    override fun manageMemberships(
        channelsToSet: List<ChannelMembershipInput>,
        channelsToRemove: List<String>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
    ): ManageMemberships {
        return com.pubnub.internal.endpoints.objects.membership.ManageMembershipsImpl(
            pubNubCore.manageMemberships(
                channelsToSet.toInternalChannelMemberships(),
                channelsToRemove,
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal(),
            ),
        )
    }

    override fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): GetChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.GetChannelMembersImpl(
            pubNubCore.getChannelMembers(
                channel,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    @Deprecated(
        replaceWith =
            ReplaceWith(
                "fetchMessages(channels = channels, page = PNBoundedPage(start = start, end = end," +
                    " limit = maximumPerChannel),includeMeta = includeMeta," +
                    " includeMessageActions = includeMessageActions, includeMessageType = includeMessageType)",
                "com.pubnub.api.models.consumer.PNBoundedPage",
            ),
        level = DeprecationLevel.ERROR,
        message = "Use fetchMessages(String, PNBoundedPage, Boolean, Boolean, Boolean) instead",
    )
    override fun fetchMessages(
        channels: List<String>,
        maximumPerChannel: Int,
        start: Long?,
        end: Long?,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean,
    ): FetchMessages {
        return com.pubnub.internal.endpoints.FetchMessagesImpl(
            pubNubCore.fetchMessages(
                channels,
                maximumPerChannel,
                start,
                end,
                includeMeta,
                includeMessageActions,
                includeMessageType,
            ),
        )
    }

    @Deprecated(
        replaceWith =
            ReplaceWith(
                "getMessageActions(channel = channel, page = PNBoundedPage(start = start, end = end, limit = limit))",
                "com.pubnub.api.models.consumer.PNBoundedPage",
            ),
        level = DeprecationLevel.ERROR,
        message = "Use getMessageActions(String, PNBoundedPage) instead",
    )
    override fun getMessageActions(
        channel: String,
        start: Long?,
        end: Long?,
        limit: Int?,
    ): GetMessageActions {
        return com.pubnub.internal.endpoints.message_actions.GetMessageActionsImpl(
            pubNubCore.getMessageActions(channel, start, end, limit),
        )
    }

    @Deprecated(
        replaceWith =
            ReplaceWith(
                "setMemberships(channels = channels, uuid = uuid, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
                    "includeChannelDetails = includeChannelDetails)",
            ),
        level = DeprecationLevel.ERROR,
        message = "Use setMemberships instead",
    )
    override fun addMemberships(
        channels: List<ChannelMembershipInput>,
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?,
    ): ManageMemberships {
        return com.pubnub.internal.endpoints.objects.membership.ManageMembershipsImpl(
            pubNubCore.addMemberships(
                channels.toInternalChannelMemberships(),
                uuid,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeChannelDetails.toInternal(),
            ),
        )
    }

    @Deprecated(
        "Use getChannelMembers instead",
        replaceWith =
            ReplaceWith(
                "getChannelMembers(channel = channel, limit = limit, page = page, " +
                    "filter = filter, sort = sort, includeCount = includeCount, " +
                    "includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.ERROR,
    )
    override fun getMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): GetChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.GetChannelMembersImpl(
            pubNubCore.getMembers(
                channel,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    @Deprecated(
        "Use setChannelMembers instead",
        replaceWith =
            ReplaceWith(
                "setChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount," +
                    " includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.ERROR,
    )
    override fun addMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.ManageChannelMembersImpl(
            pubNubCore.addMembers(
                channel,
                uuids.toInternalMemberInputs(),
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    override fun setChannelMembers(
        channel: String,
        uuids: List<MemberInput>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.ManageChannelMembersImpl(
            pubNubCore.setChannelMembers(
                channel,
                uuids.toInternalMemberInputs(),
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    @Deprecated(
        "Use removeChannelMembers instead",
        replaceWith =
            ReplaceWith(
                "removeChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
                    "page = page, filter = filter, sort = sort, includeCount = includeCount, " +
                    "includeCustom = includeCustom,includeUUIDDetails = includeUUIDDetails)",
            ),
        level = DeprecationLevel.ERROR,
    )
    override fun removeMembers(
        channel: String,
        uuids: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.ManageChannelMembersImpl(
            pubNubCore.removeMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    override fun removeChannelMembers(
        channel: String,
        uuids: List<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.ManageChannelMembersImpl(
            pubNubCore.removeChannelMembers(
                channel,
                uuids,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    override fun manageChannelMembers(
        channel: String,
        uuidsToSet: Collection<MemberInput>,
        uuidsToRemove: Collection<String>,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?,
    ): ManageChannelMembers {
        return com.pubnub.internal.endpoints.objects.member.ManageChannelMembersImpl(
            pubNubCore.manageChannelMembers(
                channel,
                uuidsToSet.toInternalMemberInputs(),
                uuidsToRemove,
                limit,
                page,
                filter,
                sort.toInternalSortKeys(),
                includeCount,
                includeCustom,
                includeUUIDDetails.toInternal(),
            ),
        )
    }

    override fun sendFile(
        channel: String,
        fileName: String,
        inputStream: InputStream,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
        cipherKey: String?,
    ): SendFile {
        return com.pubnub.internal.endpoints.files.SendFileImpl(
            pubNubCore.sendFile(
                channel,
                fileName,
                inputStream,
                message,
                meta,
                ttl,
                shouldStore,
                cipherKey,
            ),
        )
    }

    override fun listFiles(
        channel: String,
        limit: Int?,
        next: PNPage.PNNext?,
    ): ListFiles {
        return com.pubnub.internal.endpoints.files.ListFilesImpl(
            pubNubCore.listFiles(channel, limit, next),
        )
    }

    override fun getFileUrl(
        channel: String,
        fileName: String,
        fileId: String,
    ): GetFileUrl {
        return com.pubnub.internal.endpoints.files.GetFileUrlImpl(
            pubNubCore.getFileUrl(channel, fileName, fileId),
        )
    }

    override fun downloadFile(
        channel: String,
        fileName: String,
        fileId: String,
        cipherKey: String?,
    ): DownloadFile {
        return com.pubnub.internal.endpoints.files.DownloadFileImpl(
            pubNubCore.downloadFile(channel, fileName, fileId, cipherKey),
        )
    }

    override fun deleteFile(
        channel: String,
        fileName: String,
        fileId: String,
    ): DeleteFile {
        return com.pubnub.internal.endpoints.files.DeleteFileImpl(
            pubNubCore.deleteFile(channel, fileName, fileId),
        )
    }

    override fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?,
    ): PublishFileMessage {
        return com.pubnub.internal.endpoints.files.PublishFileMessageImpl(
            pubNubCore.publishFileMessage(channel, fileName, fileId, message, meta, ttl, shouldStore),
        )
    }

    /**
     * Queries the local subscribe loop for channels currently in the mix.
     *
     * @return A list of channels the client is currently subscribed to.
     */
    override fun getSubscribedChannels(): List<String> = pubNubCore.getSubscribedChannels()

    /**
     * Queries the local subscribe loop for channel groups currently in the mix.
     *
     * @return A list of channel groups the client is currently subscribed to.
     */
    override fun getSubscribedChannelGroups(): List<String> = pubNubCore.getSubscribedChannelGroups()

    /**
     * Track the online and offline status of users and devices in real time and store custom state information.
     * When you have Presence enabled, PubNub automatically creates a presence channel for each channel.
     *
     * Subscribing to a presence channel or presence channel group will only return presence events
     *
     * @param channels Channels to subscribe/unsubscribe. Either `channel` or [channelGroups] are required.
     * @param channelGroups Channel groups to subscribe/unsubscribe. Either `channelGroups` or [channels] are required.
     */
    override fun presence(
        channels: List<String>,
        channelGroups: List<String>,
        connected: Boolean,
    ) = pubNubCore.presence(channels, channelGroups, connected)

    private fun getCryptoModuleOrThrow(cipherKey: String? = null): CryptoModule {
        return cipherKey?.let { cipherKeyNotNull ->
            (configuration as? PNConfiguration)?.let {
                CryptoModule.createLegacyCryptoModule(cipherKeyNotNull, it.useRandomInitializationVector)
            } ?: CryptoModule.createLegacyCryptoModule(cipherKeyNotNull)
        } ?: configuration.cryptoModule ?: throw PubNubException("Crypto module is not initialized")
    }

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
     *
     * @param inputString String to be decrypted.
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    override fun decrypt(inputString: String): String = pubNubCore.decrypt(inputString)

    /**
     * Perform Cryptographic decryption of an input string using a cipher key.
     *
     * @param inputString String to be decrypted.
     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the decryption of `inputString` using `cipherKey`.
     * @throws PubNubException throws exception in case of failed decryption.
     */
    override fun decrypt(
        inputString: String,
        cipherKey: String?,
    ): String = pubNubCore.decrypt(inputString, getCryptoModuleOrThrow(cipherKey))

    /**
     * Perform Cryptographic decryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for decryption.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed decryption.
     */
    override fun decryptInputStream(
        inputStream: InputStream,
        cipherKey: String?,
    ): InputStream = pubNubCore.decryptInputStream(inputStream, getCryptoModuleOrThrow(cipherKey))

    /**
     * Perform Cryptographic encryption of an input string and a cipher key.
     *
     * @param inputString String to be encrypted.
     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
     *
     * @return String containing the encryption of `inputString` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    override fun encrypt(
        inputString: String,
        cipherKey: String?,
    ): String = pubNubCore.encrypt(inputString, getCryptoModuleOrThrow(cipherKey))

    /**
     * Perform Cryptographic encryption of an input stream using provided cipher key.
     *
     * @param inputStream InputStream to be encrypted.
     * @param cipherKey Cipher key to be used for encryption.
     *
     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
     * @throws PubNubException Throws exception in case of failed encryption.
     */
    override fun encryptInputStream(
        inputStream: InputStream,
        cipherKey: String?,
    ): InputStream = pubNubCore.encryptInputStream(inputStream, getCryptoModuleOrThrow(cipherKey))
}

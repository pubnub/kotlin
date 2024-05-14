package com.pubnub.api

import ObjectsResponse
import com.pubnub.api.callbacks.Listener
import com.pubnub.api.endpoints.DeleteMessages
import com.pubnub.api.endpoints.FetchMessages
import com.pubnub.api.endpoints.MessageCounts
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.access.GrantToken
import com.pubnub.api.endpoints.access.RevokeToken
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.api.endpoints.files.GetFileUrl
import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.api.endpoints.files.PublishFileMessage
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
import com.pubnub.api.endpoints.pubsub.PublishImpl
import com.pubnub.api.endpoints.pubsub.Signal
import com.pubnub.api.endpoints.push.AddChannelsToPush
import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.enums.PNPushEnvironment
import com.pubnub.api.enums.PNPushType
import com.pubnub.api.models.consumer.PNBoundedPage
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNTimeResult
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAllChannelsResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsDeleteGroupResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsListAllResult
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsRemoveChannelResult
import com.pubnub.api.models.consumer.files.PNDeleteFileResult
import com.pubnub.api.models.consumer.files.PNFileUrlResult
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.api.models.consumer.files.PNPublishFileMessageResult
import com.pubnub.api.models.consumer.history.HistoryMessageType
import com.pubnub.api.models.consumer.history.PNDeleteMessagesResult
import com.pubnub.api.models.consumer.history.PNFetchMessageItem
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult
import com.pubnub.api.models.consumer.history.PNMessageCountResult
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNMessageAction
import com.pubnub.api.models.consumer.message_actions.PNRemoveMessageActionResult
import com.pubnub.api.models.consumer.objects.PNKey
import com.pubnub.api.models.consumer.objects.PNMemberKey
import com.pubnub.api.models.consumer.objects.PNMembershipKey
import com.pubnub.api.models.consumer.objects.PNPage
import com.pubnub.api.models.consumer.objects.PNRemoveMetadataResult
import com.pubnub.api.models.consumer.objects.PNSortKey
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataArrayResult
import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadataResult
import com.pubnub.api.models.consumer.objects.member.MemberInput
import com.pubnub.api.models.consumer.objects.member.PNMemberArrayResult
import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
import com.pubnub.api.models.consumer.objects.membership.PNChannelMembershipArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataArrayResult
import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
import com.pubnub.api.models.consumer.presence.PNGetStateResult
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.models.consumer.presence.PNSetStateResult
import com.pubnub.api.models.consumer.presence.PNWhereNowResult
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult
import com.pubnub.api.models.consumer.push.PNPushListProvisionsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.v2.callbacks.EventListener
import com.pubnub.api.v2.callbacks.StatusListener
import com.pubnub.kmp.Optional
import com.pubnub.kmp.toOptional

import toMap
import PubNub as PubNubJs

actual fun createCommonPubNub(config: PNConfiguration): PubNub {
    return PubNubImpl(config)
}

class PubNubImpl(override val configuration: PNConfiguration) : PubNub {
    override fun addListener(listener: EventListener) {
        TODO("Not yet implemented")
    }

    override fun addListener(listener: StatusListener) {
        TODO("Not yet implemented")
    }

    override fun removeListener(listener: Listener) {
        TODO("Not yet implemented")
    }

    override fun removeAllListeners() {
        TODO("Not yet implemented")
    }

    override fun publish(
        channel: String,
        message: Any,
        meta: Any?,
        shouldStore: Boolean?,
        usePost: Boolean,
        replicate: Boolean,
        ttl: Int?
    ): Publish {
        TODO("Not yet implemented")
    }

    override fun fire(channel: String, message: Any, meta: Any?, usePost: Boolean, ttl: Int?): Publish {
        TODO("Not yet implemented")
    }

    override fun signal(channel: String, message: Any): Signal {
        TODO("Not yet implemented")
    }

    override fun getSubscribedChannels(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getSubscribedChannelGroups(): List<String> {
        TODO("Not yet implemented")
    }

    override fun addPushNotificationsOnChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): AddChannelsToPush {
        TODO("Not yet implemented")
    }

    override fun auditPushChannelProvisions(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): ListPushProvisions {
        TODO("Not yet implemented")
    }

    override fun removePushNotificationsFromChannels(
        pushType: PNPushType,
        channels: List<String>,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveChannelsFromPush {
        TODO("Not yet implemented")
    }

    override fun removeAllPushNotificationsFromDeviceWithPushToken(
        pushType: PNPushType,
        deviceId: String,
        topic: String?,
        environment: PNPushEnvironment
    ): RemoveAllPushChannelsForDevice {
        TODO("Not yet implemented")
    }

    override fun fetchMessages(
        channels: List<String>,
        page: PNBoundedPage,
        includeUUID: Boolean,
        includeMeta: Boolean,
        includeMessageActions: Boolean,
        includeMessageType: Boolean
    ): FetchMessages {
        TODO("Not yet implemented")
    }

    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): DeleteMessages {
        TODO("Not yet implemented")
    }

    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
        TODO("Not yet implemented")
    }

    override fun hereNow(
        channels: List<String>,
        channelGroups: List<String>,
        includeState: Boolean,
        includeUUIDs: Boolean
    ): HereNow {
        TODO("Not yet implemented")
    }

    override fun whereNow(uuid: String): WhereNow {
        TODO("Not yet implemented")
    }

    override fun setPresenceState(
        channels: List<String>,
        channelGroups: List<String>,
        state: Any,
        uuid: String
    ): SetState {
        TODO("Not yet implemented")
    }

    override fun getPresenceState(channels: List<String>, channelGroups: List<String>, uuid: String): GetState {
        TODO("Not yet implemented")
    }

    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
        TODO("Not yet implemented")
    }

    override fun addMessageAction(channel: String, messageAction: PNMessageAction): AddMessageAction {
        TODO("Not yet implemented")
    }

    override fun removeMessageAction(
        channel: String,
        messageTimetoken: Long,
        actionTimetoken: Long
    ): RemoveMessageAction {
        TODO("Not yet implemented")
    }

    override fun getMessageActions(channel: String, page: PNBoundedPage): GetMessageActions {
        TODO("Not yet implemented")
    }

    override fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
        TODO("Not yet implemented")
    }

    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
        TODO("Not yet implemented")
    }

    override fun removeChannelsFromChannelGroup(
        channels: List<String>,
        channelGroup: String
    ): RemoveChannelChannelGroup {
        TODO("Not yet implemented")
    }

    override fun listAllChannelGroups(): ListAllChannelGroup {
        TODO("Not yet implemented")
    }

    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
        TODO("Not yet implemented")
    }

    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUUID: String?,
        channels: List<ChannelGrant>,
        channelGroups: List<ChannelGroupGrant>,
        uuids: List<UUIDGrant>
    ): GrantToken {
        TODO("Not yet implemented")
    }

    override fun grantToken(
        ttl: Int,
        meta: Any?,
        authorizedUserId: UserId?,
        spacesPermissions: List<SpacePermissions>,
        usersPermissions: List<UserPermissions>
    ): GrantToken {
        TODO("Not yet implemented")
    }

    override fun revokeToken(token: String): RevokeToken {
        TODO("Not yet implemented")
    }

    override fun time(): Time {
        TODO("Not yet implemented")
    }

    override fun getAllChannelMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun getChannelMetadata(channel: String, includeCustom: Boolean): GetChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun setChannelMetadata(
        channel: String,
        name: String?,
        description: String?,
        custom: Any?,
        includeCustom: Boolean,
        type: String?,
        status: String?
    ): SetChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
        TODO("Not yet implemented")
    }

    override fun getAllUUIDMetadata(
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNKey>>,
        includeCount: Boolean,
        includeCustom: Boolean
    ): GetAllUUIDMetadata {
        TODO("Not yet implemented")
    }

    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): GetUUIDMetadata {
        TODO("Not yet implemented")
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
        status: String?
    ): SetUUIDMetadata {
        TODO("Not yet implemented")
    }

    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
        TODO("Not yet implemented")
    }

    override fun getMemberships(
        uuid: String?,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMembershipKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeChannelDetails: PNChannelDetailsLevel?
    ): GetMemberships {
        TODO("Not yet implemented")
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
        includeChannelDetails: PNChannelDetailsLevel?
    ): ManageMemberships {
        TODO("Not yet implemented")
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
        includeChannelDetails: PNChannelDetailsLevel?
    ): ManageMemberships {
        TODO("Not yet implemented")
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
        includeChannelDetails: PNChannelDetailsLevel?
    ): ManageMemberships {
        TODO("Not yet implemented")
    }

    override fun getChannelMembers(
        channel: String,
        limit: Int?,
        page: PNPage?,
        filter: String?,
        sort: Collection<PNSortKey<PNMemberKey>>,
        includeCount: Boolean,
        includeCustom: Boolean,
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): GetChannelMembers {
        TODO("Not yet implemented")
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
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): ManageChannelMembers {
        TODO("Not yet implemented")
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
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): ManageChannelMembers {
        TODO("Not yet implemented")
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
        includeUUIDDetails: PNUUIDDetailsLevel?
    ): ManageChannelMembers {
        TODO("Not yet implemented")
    }

    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): ListFiles {
        TODO("Not yet implemented")
    }

    override fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
        TODO("Not yet implemented")
    }

    override fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
        TODO("Not yet implemented")
    }

    override fun publishFileMessage(
        channel: String,
        fileName: String,
        fileId: String,
        message: Any?,
        meta: Any?,
        ttl: Int?,
        shouldStore: Boolean?
    ): PublishFileMessage {
        TODO("Not yet implemented")
    }

    override fun subscribe(
        channels: List<String>,
        channelGroups: List<String>,
        withPresence: Boolean,
        withTimetoken: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
        TODO("Not yet implemented")
    }

    override fun setToken(token: String?) {
        TODO("Not yet implemented")
    }

}

//class PubNubImpl(override val configuration: PNConfiguration) : PubNub {
//
//    private val jsPubNub: PubNubJs = PubNubJs(configuration.toJs())
//
//    override fun addListener(listener: EventListener) {
////        jsPubNub.addListener()
//    }
//
//    override fun addListener(listener: StatusListener) {
//        TODO("Not yet implemented")
//    }
////    override fun addListener(listener: EventListener) {
////        jsPubNub.addListener(object : PubNubJs.ListenerParameters {
////            override val status: ((statusEvent: PubNubJs.StatusEvent) -> Unit)? = null
////            override val message: (messageEvent: PubNubJs.MessageEvent) -> Unit = { it: PubNubJs.MessageEvent ->
////                listener.message(this@PubNub, PNMessageResult(
////                    BasePubSubResult(
////                        it.channel,
////                        it.subscription,
////                        it.timetoken.toLong(),
////                        it.userMetadata as? JsonElement, // TODO kmp
////                        it.publisher
////                    ),
////                    it.message as JsonElement, // TODO kmp
////                    null //TODO it.error
////                ))
////            }
////            override val presence: ((presenceEvent: PubNubJs.PresenceEvent) -> Unit)?
////                get() = TODO("Not yet implemented")
////            override val signal: ((signalEvent: PubNubJs.SignalEvent) -> Unit)?
////                get() = TODO("Not yet implemented")
////            override val messageAction: ((messageActionEvent: PubNubJs.MessageActionEvent) -> Unit)?
////                get() = TODO("Not yet implemented")
////            override val file: ((fileEvent: PubNubJs.FileEvent) -> Unit)?
////                get() = TODO("Not yet implemented")
////
////        })
////    }
////
////    override fun addListener(listener: CommonStatusListener) {
////        TODO("Not yet implemented")
////    }
//
//    /**
//     * Remove a listener.
//     *
//     * @param listener The listener to be removed, previously added with [addListener].
//     */
//    override fun removeListener(listener: Listener) {
//        TODO("Not yet implemented")
//    }
//
//    /**
//     * Removes all listeners.
//     */
//    override fun removeAllListeners() {
//        TODO("Not yet implemented")
//    }
//
//    override fun publish(
//        channel: String,
//        message: Any,
//        meta: Any?,
//        shouldStore: Boolean?,
//        usePost: Boolean,
//        replicate: Boolean,
//        ttl: Int?
//    ): Publish {
//        val params = object : PubNubJs.PublishParameters {
//            override var message: Any = message
//            override var channel: String = channel
//            override var storeInHistory: Boolean? = shouldStore
//            override var meta: Any? = meta
//            override var sendByPost: Boolean? = usePost
//            override var ttl: Number? = ttl
//        }
//        return PublishImpl(jsPubNub, params)
//    }
//
//    override fun fire(
//        channel: String,
//        message: Any,
//        meta: Any?,
//        usePost: Boolean,
//        ttl: Int?
//    ): Publish {
//        TODO()
////        val params = object : PubNubJs.FireParameters {
////            override var message: Any = message
////            override var channel: String = channel
////            override var meta: Any? = meta
////            override var sendByPost: Boolean? = usePost
////        }
////        return Endpoint({jsPubNub.fire(params)}) { it: PubNubJs.PublishResponse ->
////            PNPublishResult(it.timetoken.toLong())
////        }
//    }
//
//    override fun signal(channel: String, message: Any): Signal {
////        val params = object : PubNubJs.SignalParameters {
////            override var message: Any = message
////            override var channel: String = channel
////        }
////        return Endpoint({jsPubNub.signal(params)}) { it: PubNubJs.SignalResponse ->
////            PNPublishResult(it.timetoken.toLong())
////        }
//        TODO()
//    }
//
//    override fun getSubscribedChannels(): List<String> {
//        return jsPubNub.getSubscribedChannels().toList()
//    }
//
//    override fun getSubscribedChannelGroups(): List<String> {
//        return jsPubNub.getSubscribedChannelGroups().toList()
//    }
//
//    override fun addPushNotificationsOnChannels(
//        pushType: PNPushType,
//        channels: List<String>,
//        deviceId: String,
//        topic: String?,
//        environment: PNPushEnvironment
//    ): AddChannelsToPush {
//        TODO("Not yet implemented")
//    }
//
//    override fun auditPushChannelProvisions(
//        pushType: PNPushType,
//        deviceId: String,
//        topic: String?,
//        environment: PNPushEnvironment
//    ): ListPushProvisions {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removePushNotificationsFromChannels(
//        pushType: PNPushType,
//        channels: List<String>,
//        deviceId: String,
//        topic: String?,
//        environment: PNPushEnvironment
//    ): RemoveChannelsFromPush {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removeAllPushNotificationsFromDeviceWithPushToken(
//        pushType: PNPushType,
//        deviceId: String,
//        topic: String?,
//        environment: PNPushEnvironment
//    ): RemoveAllPushChannelsForDevice {
//        TODO("Not yet implemented")
//    }
//
//    override fun fetchMessages(
//        channels: List<String>,
//        page: PNBoundedPage,
//        includeUUID: Boolean,
//        includeMeta: Boolean,
//        includeMessageActions: Boolean,
//        includeMessageType: Boolean
//    ): FetchMessages {
//        val params = createJsObject<PubNubJs.FetchMessagesParameters> {
//            this.channels = channels.toTypedArray()
//            this.start = page.start
//            this.end = page.end
//            this.count = page.limit
//            this.includeUUID = includeUUID
//            this.includeMeta = includeMeta
//            this.withMessageActions = includeMessageActions
//            this.includeMessageType = includeMessageType
//        }
//        TODO()
////        return Endpoint({ jsPubNub.fetchMessages(params) }) { it: PubNubJs.FetchMessagesResponse ->
////            PNFetchMessagesResult(it.channels.toMap().mapValues {
////                it.value.map { item ->
////                    PNFetchMessageItem(
////                        item.uuid,
////                        item.message,
////                        item.meta as JsonElement,
////                        item.timetoken.toString().toLong(),
////                        item.actions.toMap().mapValues { entry: Map.Entry<String, PubNubJs.ActionContentToAction> ->
////                            entry.value.toMap().mapValues { entry2: Map.Entry<String, Array<PubNubJs.Action>> ->
////                                it.value.map { action ->
////                                    PNFetchMessageItem.Action(action.uuid, action.timetoken.toString())
////                                }
////                            }
////                        },
////                        HistoryMessageType.of(item.messageType.toString().toInt()),
////                        null, //TODO item.error
////                    )
////                }
////            }, it.more?.let { PNBoundedPage(it.start.toLong(), null, it.max.toInt()) }
////            )
////        }
//    }
//
//
//    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): DeleteMessages {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun hereNow(
//        channels: List<String>,
//        channelGroups: List<String>,
//        includeState: Boolean,
//        includeUUIDs: Boolean
//    ): HereNow {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun whereNow(uuid: String): WhereNow {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun setPresenceState(
//        channels: List<String>,
//        channelGroups: List<String>,
//        state: Any,
//        uuid: String
//    ): Endpoint<PNSetStateResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getPresenceState(
//        channels: List<String>,
//        channelGroups: List<String>,
//        uuid: String
//    ): Endpoint<PNGetStateResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun addMessageAction(channel: String, messageAction: PNMessageAction): Endpoint<PNAddMessageActionResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removeMessageAction(
//        channel: String,
//        messageTimetoken: Long,
//        actionTimetoken: Long
//    ): Endpoint<PNRemoveMessageActionResult> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getMessageActions(channel: String, page: PNBoundedPage): Endpoint<PNGetMessageActionsResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun addChannelsToChannelGroup(
//        channels: List<String>,
//        channelGroup: String
//    ): Endpoint<PNChannelGroupsAddChannelResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun listChannelsForChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsAllChannelsResult> {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeChannelsFromChannelGroup(
//        channels: List<String>,
//        channelGroup: String
//    ): Endpoint<PNChannelGroupsRemoveChannelResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun listAllChannelGroups(): Endpoint<PNChannelGroupsListAllResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun deleteChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsDeleteGroupResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun grant(
//        read: Boolean,
//        write: Boolean,
//        manage: Boolean,
//        delete: Boolean,
//        ttl: Int,
//        authKeys: List<String>,
//        channels: List<String>,
//        channelGroups: List<String>,
//        uuids: List<String>
//    ): Endpoint<PNAccessManagerGrantResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun grant(
//        read: Boolean,
//        write: Boolean,
//        manage: Boolean,
//        delete: Boolean,
//        get: Boolean,
//        update: Boolean,
//        join: Boolean,
//        ttl: Int,
//        authKeys: List<String>,
//        channels: List<String>,
//        channelGroups: List<String>,
//        uuids: List<String>
//    ): Endpoint<PNAccessManagerGrantResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun grantToken(
//        ttl: Int,
//        meta: Any?,
//        authorizedUUID: String?,
//        channels: List<ChannelGrant>,
//        channelGroups: List<ChannelGroupGrant>,
//        uuids: List<UUIDGrant>
//    ): Endpoint<PNGrantTokenResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun grantToken(
//        ttl: Int,
//        meta: Any?,
//        authorizedUserId: UserId?,
//        spacesPermissions: List<SpacePermissions>,
//        usersPermissions: List<UserPermissions>
//    ): Endpoint<PNGrantTokenResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun revokeToken(token: String): Endpoint<Unit> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun time(): Endpoint<PNTimeResult> {
//        TODO("Not yet implemented")
//    }
//
//    fun setUserMetadata(
//        uuid: String?,
//        name: String?,
//        externalId: String?,
//        profileUrl: String?,
//        email: String?,
//        custom: Map<String, Any?>?,
//        includeCustom: Boolean,
//        type: String?,
//        status: String?
//    ): Endpoint<PNUUIDMetadataResult> {
//        TODO()
////        return Endpoint(promiseFactory = {
////            val params = object : PubNubJs.SetUUIDMetadataParameters {
////                override var data: PubNubJs.UUIDMetadata = UUIDMetadata(
////                    name.toOptional(),
////                    externalId.toOptional(),
////                    profileUrl.toOptional(),
////                    email.toOptional(),
////                    status.toOptional(),
////                    type.toOptional(),
////                    custom.toOptional()
////                )
////
////                override var uuid: String? = uuid
////
////                override var include: PubNubJs.`T$30`? = object : PubNubJs.`T$30` {
////                    override var customFields: Boolean? = includeCustom
////                }
////            }
////
////            jsPubNub.objects.setUUIDMetadata(params)
////        }) { it: ObjectsResponse<PubNubJs.UUIDMetadataObject> ->
////            PNUUIDMetadataResult(
////                it.status.toInt(),
////                with(it.data) {
////                    PNUUIDMetadata(
////                        id,
////                        name,
////                        externalId,
////                        profileUrl,
////                        email,
////                        custom,
////                        updated,
////                        eTag,
////                        type,
////                        status
////                    )
////                }
////            )
////        }
//    }
//
//    fun removeUserMetadata(uuid: String?): Endpoint<PNRemoveMetadataResult> {
//        TODO()
////        return Endpoint({
////            jsPubNub.objects.removeUUIDMetadata(object : PubNubJs.RemoveUUIDMetadataParameters {
////                override var uuid: String? = uuid
////            })
////        }) { response ->
////            PNRemoveMetadataResult(response.status.toInt())
////        }
//    }
//
//    fun getUserMetadata(
//        uuid: String?,
//        includeCustom: Boolean
//    ): Endpoint<PNUUIDMetadataResult> {
//        TODO("Not yet implemented")
//    }
//
//    fun getAllUserMetadata(
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean
//    ): Endpoint<PNUUIDMetadataArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllChannelMetadata(
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean
//    ): Endpoint<PNChannelMetadataArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getChannelMetadata(
//        channel: String,
//        includeCustom: Boolean
//    ): Endpoint<PNChannelMetadataResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun setChannelMetadata(
//        channel: String,
//        name: String?,
//        description: String?,
//        custom: Any?,
//        includeCustom: Boolean,
//        type: String?,
//        status: String?
//    ): Endpoint<PNChannelMetadataResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removeChannelMetadata(channel: String): Endpoint<PNRemoveMetadataResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getAllUUIDMetadata(
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean
//    ): GetAllUUIDMetadata {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): GetUUIDMetadata {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun setUUIDMetadata(
//        uuid: String?,
//        name: String?,
//        externalId: String?,
//        profileUrl: String?,
//        email: String?,
//        custom: Any?,
//        includeCustom: Boolean,
//        type: String?,
//        status: String?
//    ): SetUUIDMetadata {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getMemberships(
//        uuid: String?,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMembershipKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeChannelDetails: PNChannelDetailsLevel?
//    ): Endpoint<PNChannelMembershipArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//    override fun setMemberships(
//        channels: List<ChannelMembershipInput>,
//        uuid: String?,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMembershipKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeChannelDetails: PNChannelDetailsLevel?
//    ): Endpoint<PNChannelMembershipArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removeMemberships(
//        channels: List<String>,
//        uuid: String?,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMembershipKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeChannelDetails: PNChannelDetailsLevel?
//    ): Endpoint<PNChannelMembershipArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun manageMemberships(
//        channelsToSet: List<ChannelMembershipInput>,
//        channelsToRemove: List<String>,
//        uuid: String?,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMembershipKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeChannelDetails: PNChannelDetailsLevel?
//    ): Endpoint<PNChannelMembershipArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getChannelMembers(
//        channel: String,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMemberKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeUUIDDetails: PNUUIDDetailsLevel?
//    ): Endpoint<PNMemberArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun setChannelMembers(
//        channel: String,
//        uuids: List<MemberInput>,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMemberKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeUUIDDetails: PNUUIDDetailsLevel?
//    ): Endpoint<PNMemberArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun removeChannelMembers(
//        channel: String,
//        uuids: List<String>,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMemberKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeUUIDDetails: PNUUIDDetailsLevel?
//    ): Endpoint<PNMemberArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun manageChannelMembers(
//        channel: String,
//        uuidsToSet: Collection<MemberInput>,
//        uuidsToRemove: Collection<String>,
//        limit: Int?,
//        page: PNPage?,
//        filter: String?,
//        sort: Collection<PNSortKey<PNMemberKey>>,
//        includeCount: Boolean,
//        includeCustom: Boolean,
//        includeUUIDDetails: PNUUIDDetailsLevel?
//    ): Endpoint<PNMemberArrayResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): Endpoint<PNListFilesResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun getFileUrl(channel: String, fileName: String, fileId: String): Endpoint<PNFileUrlResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun deleteFile(channel: String, fileName: String, fileId: String): Endpoint<PNDeleteFileResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun publishFileMessage(
//        channel: String,
//        fileName: String,
//        fileId: String,
//        message: Any?,
//        meta: Any?,
//        ttl: Int?,
//        shouldStore: Boolean?
//    ): Endpoint<PNPublishFileMessageResult> {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun subscribe(
//        channels: List<String>,
//        channelGroups: List<String>,
//        withPresence: Boolean,
//        withTimetoken: Long
//    ) {
//        TODO("Not yet implemented")
//    }
//
//
//    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun setToken(token: String?) {
//        TODO("Not yet implemented")
//    }
//}



fun UUIDMetadata(
    name: Optional<String?>,
    externalId: Optional<String?>,
    profileUrl: Optional<String?>,
    email: Optional<String?>,
    status: Optional<String?>,
    type: Optional<String?>,
    custom: Optional<Map<String, Any?>?>
): PubNubJs.UUIDMetadata {
    val result: PubNubJs.UUIDMetadata = createJsObject()
    name.onValue { result.name = it }
    externalId.onValue { result.externalId = it }
    profileUrl.onValue { result.profileUrl = it }
    email.onValue { result.email = it }
    status.onValue { result.status = it }
    type.onValue { result.type = it }
    custom.onValue { result.custom = it?.toCustomObject() }
    return result
}

fun Map<String, Any?>.toCustomObject(): PubNubJs.CustomObject {
    val custom = Any().asDynamic()
    entries.forEach {
        custom[it.key] = it.value
    }
    @Suppress("UnsafeCastFromDynamic")
    return custom
}

fun <T> createJsObject(configure: T.() -> Unit = {}): T = Any().asDynamic() as T

fun PNConfiguration.toJs(): PubNubJs.PNConfiguration {
    val config: PubNubJs.PNConfiguration = createJsObject()
    config.userId = userId.value
    config.subscribeKey = subscribeKey
    config.publishKey = publishKey
    config.cipherKey
//    config.authKeys: String?
//    config.logVerbosity: Boolean?
//    config.ssl: Boolean?
//    config.origin: dynamic /* String? | Array<String>? */
//    config.presenceTimeout: Number?
//    config.heartbeatInterval: Number?
//    config.restore: Boolean?
//    config.keepAlive: Boolean?
//    config.keepAliveSettings: KeepAliveSettings?
//    config.subscribeRequestTimeout: Number?
//    config.suppressLeaveEvents: Boolean?
//    config.secretKey: String?
//    config.requestMessageCountThreshold: Number?
//    config.autoNetworkDetection: Boolean?
//    config.listenToBrowserNetworkEvents: Boolean?
//    config.useRandomIVs: Boolean?
//    config.dedupeOnSubscribe: Boolean?
//    config.cryptoModule: CryptoModule?
//    config.retryConfiguration: dynamic /* LinearRetryPolicyConfiguration? | ExponentialRetryPolicyConfiguration? */
//    config.enableEventEngine: Boolean?
//    config.maintainPresenceState: Boolean?
    return config
}


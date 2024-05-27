//package com.pubnub.test
//
//import com.pubnub.api.JsonElement
//import com.pubnub.api.PubNub
//import com.pubnub.api.callbacks.Listener
//import com.pubnub.api.endpoints.DeleteMessages
//import com.pubnub.api.endpoints.FetchMessages
//import com.pubnub.api.endpoints.MessageCounts
//import com.pubnub.api.endpoints.Time
//import com.pubnub.api.endpoints.access.GrantToken
//import com.pubnub.api.endpoints.access.RevokeToken
//import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup
//import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup
//import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup
//import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup
//import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup
//import com.pubnub.api.endpoints.files.DeleteFile
//import com.pubnub.api.endpoints.files.GetFileUrl
//import com.pubnub.api.endpoints.files.ListFiles
//import com.pubnub.api.endpoints.files.PublishFileMessage
//import com.pubnub.api.endpoints.message_actions.AddMessageAction
//import com.pubnub.api.endpoints.message_actions.GetMessageActions
//import com.pubnub.api.endpoints.message_actions.RemoveMessageAction
//import com.pubnub.api.endpoints.objects.channel.GetAllChannelMetadata
//import com.pubnub.api.endpoints.objects.channel.GetChannelMetadata
//import com.pubnub.api.endpoints.objects.channel.RemoveChannelMetadata
//import com.pubnub.api.endpoints.objects.channel.SetChannelMetadata
//import com.pubnub.api.endpoints.objects.member.GetChannelMembers
//import com.pubnub.api.endpoints.objects.member.ManageChannelMembers
//import com.pubnub.api.endpoints.objects.membership.GetMemberships
//import com.pubnub.api.endpoints.objects.membership.ManageMemberships
//import com.pubnub.api.endpoints.objects.uuid.GetAllUUIDMetadata
//import com.pubnub.api.endpoints.objects.uuid.GetUUIDMetadata
//import com.pubnub.api.endpoints.objects.uuid.RemoveUUIDMetadata
//import com.pubnub.api.endpoints.objects.uuid.SetUUIDMetadata
//import com.pubnub.api.endpoints.presence.GetState
//import com.pubnub.api.endpoints.presence.HereNow
//import com.pubnub.api.endpoints.presence.SetState
//import com.pubnub.api.endpoints.presence.WhereNow
//import com.pubnub.api.endpoints.pubsub.Publish
//import com.pubnub.api.endpoints.pubsub.Signal
//import com.pubnub.api.endpoints.push.AddChannelsToPush
//import com.pubnub.api.endpoints.push.ListPushProvisions
//import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice
//import com.pubnub.api.endpoints.push.RemoveChannelsFromPush
//import com.pubnub.api.enums.PNPushEnvironment
//import com.pubnub.api.enums.PNPushType
//import com.pubnub.api.models.consumer.PNBoundedPage
//import com.pubnub.api.models.consumer.PNPublishResult
//import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
//import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
//import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
//import com.pubnub.api.models.consumer.message_actions.PNMessageAction
//import com.pubnub.api.models.consumer.objects.PNKey
//import com.pubnub.api.models.consumer.objects.PNMemberKey
//import com.pubnub.api.models.consumer.objects.PNMembershipKey
//import com.pubnub.api.models.consumer.objects.PNPage
//import com.pubnub.api.models.consumer.objects.PNSortKey
//import com.pubnub.api.models.consumer.objects.channel.PNChannelMetadata
//import com.pubnub.api.models.consumer.objects.member.MemberInput
//import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
//import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
//import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
//import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadata
//import com.pubnub.api.models.consumer.objects.uuid.PNUUIDMetadataResult
//import com.pubnub.api.models.consumer.pubsub.BasePubSubResult
//import com.pubnub.api.models.consumer.pubsub.PNEvent
//import com.pubnub.api.models.consumer.pubsub.PNMessageResult
//import com.pubnub.api.v2.PNConfiguration
//import com.pubnub.api.v2.callbacks.Consumer
//import com.pubnub.api.v2.callbacks.EventListener
//import com.pubnub.api.v2.callbacks.Result
//import com.pubnub.api.v2.callbacks.StatusListener
//import com.pubnub.kmp.CustomObject
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.mapNotNull
//import kotlinx.coroutines.launch
//import kotlinx.datetime.Clock
//import kotlinx.datetime.Instant
//import kotlin.coroutines.EmptyCoroutineContext
//import kotlin.time.Duration.Companion.minutes
//
//
//class FakePubNub(override val configuration: PNConfiguration) : PubNub {
//
//    val scope = CoroutineScope(SupervisorJob())
//
//    fun Long.toEpochSeconds() = this / 10000000
//    fun generateTimetoken() = Clock.System.now().toEpochMilliseconds() * 10000
//
//    val events = MutableSharedFlow<PNEvent>(100, 10)
//    val subscriptionStream = events.mapNotNull {
//        val tt = it.timetoken ?: return@mapNotNull null
//        if (Clock.System.now() < Instant.fromEpochSeconds(tt.toEpochSeconds()) - 10.minutes) {
//            it
//        } else {
//            null
//        }
//    }
//
//    val messages: MutableList<PNMessageResult> = mutableListOf()
//    val userMetadata: MutableMap<String, PNUUIDMetadata> = mutableMapOf()
//    val channelMetadata: MutableMap<String, PNChannelMetadata> = mutableMapOf()
//
//    override fun addListener(listener: EventListener) {
//        TODO("Not yet implemented")
//    }
//
//    override fun addListener(listener: StatusListener) {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeListener(listener: Listener) {
//        TODO("Not yet implemented")
//    }
//
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
//        return object : Publish {
//            override fun async(callback: Consumer<Result<PNPublishResult>>) {
//                scope.launch {
//                    events.emit(PNMessageResult(
//                        BasePubSubResult(
//                            channel,
//                            null,
//                            generateTimetoken(),
//                            null,
//                            configuration.userId.value
//                        ), message, null
//                    ))
//                }
//            }
//        }
//    }
//
//    override fun fire(channel: String, message: Any, meta: Any?, usePost: Boolean, ttl: Int?): Publish {
//        TODO("Not yet implemented")
//    }
//
//    override fun signal(channel: String, message: Any): Signal {
//        TODO("Not yet implemented")
//    }
//
//    override fun getSubscribedChannels(): List<String> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getSubscribedChannelGroups(): List<String> {
//        TODO("Not yet implemented")
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
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): DeleteMessages {
//        TODO("Not yet implemented")
//    }
//
//    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): MessageCounts {
//        TODO("Not yet implemented")
//    }
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
//    override fun whereNow(uuid: String): WhereNow {
//        TODO("Not yet implemented")
//    }
//
//    override fun getPresenceState(channels: List<String>, channelGroups: List<String>, uuid: String): GetState {
//        TODO("Not yet implemented")
//    }
//
//    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
//        TODO("Not yet implemented")
//    }
//
//    override fun addMessageAction(channel: String, messageAction: PNMessageAction): AddMessageAction {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeMessageAction(
//        channel: String,
//        messageTimetoken: Long,
//        actionTimetoken: Long
//    ): RemoveMessageAction {
//        TODO("Not yet implemented")
//    }
//
//    override fun getMessageActions(channel: String, page: PNBoundedPage): GetMessageActions {
//        TODO("Not yet implemented")
//    }
//
//    override fun addChannelsToChannelGroup(channels: List<String>, channelGroup: String): AddChannelChannelGroup {
//        TODO("Not yet implemented")
//    }
//
//    override fun listChannelsForChannelGroup(channelGroup: String): AllChannelsChannelGroup {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeChannelsFromChannelGroup(
//        channels: List<String>,
//        channelGroup: String
//    ): RemoveChannelChannelGroup {
//        TODO("Not yet implemented")
//    }
//
//    override fun listAllChannelGroups(): ListAllChannelGroup {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteChannelGroup(channelGroup: String): DeleteChannelGroup {
//        TODO("Not yet implemented")
//    }
//
//    override fun revokeToken(token: String): RevokeToken {
//        TODO("Not yet implemented")
//    }
//
//    override fun time(): Time {
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
//    ): GetAllChannelMetadata {
//        TODO("Not yet implemented")
//    }
//
//    override fun getChannelMetadata(channel: String, includeCustom: Boolean): GetChannelMetadata {
//        TODO("Not yet implemented")
//    }
//
//    override fun removeChannelMetadata(channel: String): RemoveChannelMetadata {
//        TODO("Not yet implemented")
//    }
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
//    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): GetUUIDMetadata {
//        TODO("Not yet implemented")
//    }
//
//    override fun setUUIDMetadata(
//        uuid: String?,
//        name: String?,
//        externalId: String?,
//        profileUrl: String?,
//        email: String?,
//        custom: CustomObject?,
//        includeCustom: Boolean,
//        type: String?,
//        status: String?
//    ): SetUUIDMetadata {
//            val actualUuid = uuid ?: configuration.userId.value
//            val result = userMetadata.compute(actualUuid) { a: String, b: PNUUIDMetadata? ->
//                PNUUIDMetadata(
//                    actualUuid,
//                    name ?: b?.name,
//                    externalId ?: b?.externalId,
//                    profileUrl ?: b?.profileUrl,
//                    email ?: b?.email,
//                    custom ?: b?.custom,
//                    Clock.System.now().toString(),
//                    null,
//                    type ?: b?.type,
//                    status ?: b?.status
//                )
//            }
//        return object : SetUUIDMetadata {
//            override fun async(callback: Consumer<Result<PNUUIDMetadataResult>>) {
//                callback.accept(Result.success(PNUUIDMetadataResult(
//                    200,
//                    if (!includeCustom) {
//                        result?.copy(custom = null)
//                    } else {
//                        result
//                    }
//                )
//                ))
//            }
//        }
//    }
//
//    override fun removeUUIDMetadata(uuid: String?): RemoveUUIDMetadata {
//        TODO("Not yet implemented")
//    }
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
//    ): GetMemberships {
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
//    ): ManageMemberships {
//        TODO("Not yet implemented")
//    }
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
//    ): ManageMemberships {
//        TODO("Not yet implemented")
//    }
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
//    ): ManageMemberships {
//        TODO("Not yet implemented")
//    }
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
//    ): GetChannelMembers {
//        TODO("Not yet implemented")
//    }
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
//    ): ManageChannelMembers {
//        TODO("Not yet implemented")
//    }
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
//    ): ManageChannelMembers {
//        TODO("Not yet implemented")
//    }
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
//    ): ManageChannelMembers {
//        TODO("Not yet implemented")
//    }
//
//    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): ListFiles {
//        TODO("Not yet implemented")
//    }
//
//    override fun getFileUrl(channel: String, fileName: String, fileId: String): GetFileUrl {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteFile(channel: String, fileName: String, fileId: String): DeleteFile {
//        TODO("Not yet implemented")
//    }
//
//    override fun publishFileMessage(
//        channel: String,
//        fileName: String,
//        fileId: String,
//        message: Any?,
//        meta: Any?,
//        ttl: Int?,
//        shouldStore: Boolean?
//    ): PublishFileMessage {
//        TODO("Not yet implemented")
//    }
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
//    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun setToken(token: String?) {
//        TODO("Not yet implemented")
//    }
//
//    override fun setPresenceState(channels: List<String>, channelGroups: List<String>, state: Any): SetState {
//        TODO("Not yet implemented")
//    }
//
//    override fun grantToken(
//        ttl: Int,
//        meta: CustomObject?,
//        authorizedUUID: String?,
//        channels: List<ChannelGrant>,
//        channelGroups: List<ChannelGroupGrant>,
//        uuids: List<UUIDGrant>
//    ): GrantToken {
//        TODO("Not yet implemented")
//    }
//
//    override fun setChannelMetadata(
//        channel: String,
//        name: String?,
//        description: String?,
//        custom: CustomObject?,
//        includeCustom: Boolean,
//        type: String?,
//        status: String?
//    ): SetChannelMetadata {
//        TODO("Not yet implemented")
//    }
//
//    override fun unsubscribeAll() {
//        TODO("Not yet implemented")
//    }
//
//    override fun destroy() {
//        TODO("Not yet implemented")
//    }
//
//}
////
//
////
////    /**
////     * Remove a listener.
////     *
////     * @param listener The listener to be removed, previously added with [addListener].
////     */
////    override fun removeListener(listener: Listener) {
////        TODO("Not yet implemented")
////    }
////
////    /**
////     * Removes all listeners.
////     */
////    override fun removeAllListeners() {
////        TODO("Not yet implemented")
////    }
////
////    override fun publish(
////        channel: String,
////        message: Any,
////        meta: Any?,
////        shouldStore: Boolean?,
////        usePost: Boolean,
////        replicate: Boolean,
////        ttl: Int?
////    ): Publish {
////        return createEndpoint {
////            val tt = Clock.System.now().toEpochMilliseconds() / 1000
////            messages.add(
////                PNMessageResult(
////                    BasePubSubResult(channel, null, tt, null, configuration.userId.value),
////                    message as JsonElement,
////                    null
////                )
////            )
////            PNPublishResult(tt)
////        }
////        // TODO emit message
////    }
////
////
////    override fun fire(
////        channel: String,
////        message: Any,
////        meta: Any?,
////        usePost: Boolean,
////        ttl: Int?
////    ): Endpoint<PNPublishResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun signal(channel: String, message: Any): Endpoint<PNPublishResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getSubscribedChannels(): List<String> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getSubscribedChannelGroups(): List<String> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun addPushNotificationsOnChannels(
////        pushType: PNPushType,
////        channels: List<String>,
////        deviceId: String,
////        topic: String?,
////        environment: PNPushEnvironment
////    ): Endpoint<PNPushAddChannelResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun auditPushChannelProvisions(
////        pushType: PNPushType,
////        deviceId: String,
////        topic: String?,
////        environment: PNPushEnvironment
////    ): Endpoint<PNPushListProvisionsResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removePushNotificationsFromChannels(
////        pushType: PNPushType,
////        channels: List<String>,
////        deviceId: String,
////        topic: String?,
////        environment: PNPushEnvironment
////    ): Endpoint<PNPushRemoveChannelResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removeAllPushNotificationsFromDeviceWithPushToken(
////        pushType: PNPushType,
////        deviceId: String,
////        topic: String?,
////        environment: PNPushEnvironment
////    ): Endpoint<PNPushRemoveAllChannelsResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun fetchMessages(
////        channels: List<String>,
////        page: PNBoundedPage,
////        includeUUID: Boolean,
////        includeMeta: Boolean,
////        includeMessageActions: Boolean,
////        includeMessageType: Boolean
////    ): Endpoint<PNFetchMessagesResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun deleteMessages(channels: List<String>, start: Long?, end: Long?): Endpoint<PNDeleteMessagesResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun messageCounts(channels: List<String>, channelsTimetoken: List<Long>): Endpoint<PNMessageCountResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun hereNow(
////        channels: List<String>,
////        channelGroups: List<String>,
////        includeState: Boolean,
////        includeUUIDs: Boolean
////    ): Endpoint<PNHereNowResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun whereNow(uuid: String): Endpoint<PNWhereNowResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun setPresenceState(
////        channels: List<String>,
////        channelGroups: List<String>,
////        state: Any,
////        uuid: String
////    ): Endpoint<PNSetStateResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getPresenceState(
////        channels: List<String>,
////        channelGroups: List<String>,
////        uuid: String
////    ): Endpoint<PNGetStateResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun presence(channels: List<String>, channelGroups: List<String>, connected: Boolean) {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun addMessageAction(channel: String, messageAction: PNMessageAction): Endpoint<PNAddMessageActionResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removeMessageAction(
////        channel: String,
////        messageTimetoken: Long,
////        actionTimetoken: Long
////    ): Endpoint<PNRemoveMessageActionResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getMessageActions(channel: String, page: PNBoundedPage): Endpoint<PNGetMessageActionsResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun addChannelsToChannelGroup(
////        channels: List<String>,
////        channelGroup: String
////    ): Endpoint<PNChannelGroupsAddChannelResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun listChannelsForChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsAllChannelsResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removeChannelsFromChannelGroup(
////        channels: List<String>,
////        channelGroup: String
////    ): Endpoint<PNChannelGroupsRemoveChannelResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun listAllChannelGroups(): Endpoint<PNChannelGroupsListAllResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun deleteChannelGroup(channelGroup: String): Endpoint<PNChannelGroupsDeleteGroupResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun grant(
////        read: Boolean,
////        write: Boolean,
////        manage: Boolean,
////        delete: Boolean,
////        ttl: Int,
////        authKeys: List<String>,
////        channels: List<String>,
////        channelGroups: List<String>,
////        uuids: List<String>
////    ): Endpoint<PNAccessManagerGrantResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun grant(
////        read: Boolean,
////        write: Boolean,
////        manage: Boolean,
////        delete: Boolean,
////        get: Boolean,
////        update: Boolean,
////        join: Boolean,
////        ttl: Int,
////        authKeys: List<String>,
////        channels: List<String>,
////        channelGroups: List<String>,
////        uuids: List<String>
////    ): Endpoint<PNAccessManagerGrantResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun grantToken(
////        ttl: Int,
////        meta: Any?,
////        authorizedUUID: String?,
////        channels: List<ChannelGrant>,
////        channelGroups: List<ChannelGroupGrant>,
////        uuids: List<UUIDGrant>
////    ): Endpoint<PNGrantTokenResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun grantToken(
////        ttl: Int,
////        meta: Any?,
////        authorizedUserId: UserId?,
////        spacesPermissions: List<SpacePermissions>,
////        usersPermissions: List<UserPermissions>
////    ): Endpoint<PNGrantTokenResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun revokeToken(token: String): Endpoint<Unit> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun time(): Endpoint<PNTimeResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getAllChannelMetadata(
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean
////    ): Endpoint<PNChannelMetadataArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getChannelMetadata(channel: String, includeCustom: Boolean): Endpoint<PNChannelMetadataResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun setChannelMetadata(
////        channel: String,
////        name: String?,
////        description: String?,
////        custom: Any?,
////        includeCustom: Boolean,
////        type: String?,
////        status: String?
////    ): Endpoint<PNChannelMetadataResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removeChannelMetadata(channel: String): Endpoint<PNRemoveMetadataResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getAllUUIDMetadata(
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean
////    ): Endpoint<PNUUIDMetadataArrayResult> {
////        return createEndpoint {
////            PNUUIDMetadataArrayResult(200, userMetadata.values, userMetadata.size, null, null)
////        }
////    }
////
////
////    override fun getUUIDMetadata(uuid: String?, includeCustom: Boolean): Endpoint<PNUUIDMetadataResult> {
////        return createEndpoint {
////            val actualUuid = uuid ?: configuration.userId.value
////            val result = userMetadata[actualUuid]
////
////            PNUUIDMetadataResult(
////                200,
////                if (!includeCustom) {
////                    result?.copy(custom = null)
////                } else {
////                    result
////                }
////            )
////        }
////    }
////
////
////
////    override fun removeUUIDMetadata(uuid: String?): Endpoint<PNRemoveMetadataResult> {
////        val actualUuid = uuid ?: configuration.userId.value
////        return createEndpoint {
////            userMetadata.remove(actualUuid)
////            PNRemoveMetadataResult(200)
////        }
////    }
////
////    override fun getMemberships(
////        uuid: String?,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMembershipKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeChannelDetails: PNChannelDetailsLevel?
////    ): Endpoint<PNChannelMembershipArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun setMemberships(
////        channels: List<ChannelMembershipInput>,
////        uuid: String?,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMembershipKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeChannelDetails: PNChannelDetailsLevel?
////    ): Endpoint<PNChannelMembershipArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removeMemberships(
////        channels: List<String>,
////        uuid: String?,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMembershipKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeChannelDetails: PNChannelDetailsLevel?
////    ): Endpoint<PNChannelMembershipArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun manageMemberships(
////        channelsToSet: List<ChannelMembershipInput>,
////        channelsToRemove: List<String>,
////        uuid: String?,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMembershipKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeChannelDetails: PNChannelDetailsLevel?
////    ): Endpoint<PNChannelMembershipArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getChannelMembers(
////        channel: String,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMemberKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeUUIDDetails: PNUUIDDetailsLevel?
////    ): Endpoint<PNMemberArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun setChannelMembers(
////        channel: String,
////        uuids: List<MemberInput>,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMemberKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeUUIDDetails: PNUUIDDetailsLevel?
////    ): Endpoint<PNMemberArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun removeChannelMembers(
////        channel: String,
////        uuids: List<String>,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMemberKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeUUIDDetails: PNUUIDDetailsLevel?
////    ): Endpoint<PNMemberArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun manageChannelMembers(
////        channel: String,
////        uuidsToSet: Collection<MemberInput>,
////        uuidsToRemove: Collection<String>,
////        limit: Int?,
////        page: PNPage?,
////        filter: String?,
////        sort: Collection<PNSortKey<PNMemberKey>>,
////        includeCount: Boolean,
////        includeCustom: Boolean,
////        includeUUIDDetails: PNUUIDDetailsLevel?
////    ): Endpoint<PNMemberArrayResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun listFiles(channel: String, limit: Int?, next: PNPage.PNNext?): Endpoint<PNListFilesResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun getFileUrl(channel: String, fileName: String, fileId: String): Endpoint<PNFileUrlResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun deleteFile(channel: String, fileName: String, fileId: String): Endpoint<PNDeleteFileResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun publishFileMessage(
////        channel: String,
////        fileName: String,
////        fileId: String,
////        message: Any?,
////        meta: Any?,
////        ttl: Int?,
////        shouldStore: Boolean?
////    ): Endpoint<PNPublishFileMessageResult> {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun subscribe(
////        channels: List<String>,
////        channelGroups: List<String>,
////        withPresence: Boolean,
////        withTimetoken: Long
////    ) {
////        TODO("Not yet implemented")
////    }
////
////
////    override fun unsubscribe(channels: List<String>, channelGroups: List<String>) {
////        TODO("Not yet implemented")
////    }
////
////    override fun setToken(token: String?) {
////        TODO("Not yet implemented")
////    }
////
////    override fun addListener(listener: EventListener) {
////        TODO("Not yet implemented")
////    }
////
////    override fun addListener(listener: StatusListener) {
////        TODO("Not yet implemented")
////    }
////}
////
//private fun <K, V> MutableMap<K, V>.compute(
//    key: K,
//    remappingFunction: (K, V?) -> V
//): V? {
//    val oldValue: V? = get(key)
//
//    val newValue: V = remappingFunction(key, oldValue)
//    if (newValue == null) {
//        // delete mapping
//        if (oldValue != null || containsKey(key)) {
//            // something to remove
//            remove(key)
//            return null
//        } else {
//            // nothing to do. Leave things as they were.
//            return null
//        }
//    } else {
//        // add or replace old mapping
//        put(key, newValue)
//        return newValue
//    }
//}
//

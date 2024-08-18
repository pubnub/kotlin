package com.pubnub.internal.java

import com.pubnub.api.callbacks.Listener
import com.pubnub.api.java.PubNub
import com.pubnub.api.java.builder.PresenceBuilder
import com.pubnub.api.java.builder.SubscribeBuilder
import com.pubnub.api.java.builder.UnsubscribeBuilder
import com.pubnub.api.java.callbacks.SubscribeCallback
import com.pubnub.api.java.endpoints.DeleteMessages
import com.pubnub.api.java.endpoints.FetchMessages
import com.pubnub.api.java.endpoints.History
import com.pubnub.api.java.endpoints.MessageCounts
import com.pubnub.api.java.endpoints.access.Grant
import com.pubnub.api.java.endpoints.access.RevokeToken
import com.pubnub.api.java.endpoints.access.builder.GrantTokenBuilder
import com.pubnub.api.java.endpoints.channel_groups.AddChannelChannelGroup
import com.pubnub.api.java.endpoints.channel_groups.AllChannelsChannelGroup
import com.pubnub.api.java.endpoints.channel_groups.DeleteChannelGroup
import com.pubnub.api.java.endpoints.channel_groups.RemoveChannelChannelGroup
import com.pubnub.api.java.endpoints.files.DeleteFile
import com.pubnub.api.java.endpoints.files.DownloadFile
import com.pubnub.api.java.endpoints.files.GetFileUrl
import com.pubnub.api.java.endpoints.files.ListFiles
import com.pubnub.api.java.endpoints.files.PublishFileMessage
import com.pubnub.api.java.endpoints.files.SendFile
import com.pubnub.api.java.endpoints.message_actions.AddMessageAction
import com.pubnub.api.java.endpoints.message_actions.GetMessageActions
import com.pubnub.api.java.endpoints.message_actions.RemoveMessageAction
import com.pubnub.api.java.endpoints.objects_api.channel.GetAllChannelsMetadata
import com.pubnub.api.java.endpoints.objects_api.channel.GetChannelMetadata
import com.pubnub.api.java.endpoints.objects_api.channel.RemoveChannelMetadata
import com.pubnub.api.java.endpoints.objects_api.channel.SetChannelMetadata
import com.pubnub.api.java.endpoints.objects_api.members.GetChannelMembers
import com.pubnub.api.java.endpoints.objects_api.members.ManageChannelMembers
import com.pubnub.api.java.endpoints.objects_api.members.RemoveChannelMembers
import com.pubnub.api.java.endpoints.objects_api.members.SetChannelMembers
import com.pubnub.api.java.endpoints.objects_api.memberships.GetMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.ManageMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.RemoveMemberships
import com.pubnub.api.java.endpoints.objects_api.memberships.SetMemberships
import com.pubnub.api.java.endpoints.objects_api.uuid.GetAllUUIDMetadata
import com.pubnub.api.java.endpoints.objects_api.uuid.GetUUIDMetadata
import com.pubnub.api.java.endpoints.objects_api.uuid.RemoveUUIDMetadata
import com.pubnub.api.java.endpoints.objects_api.uuid.SetUUIDMetadata
import com.pubnub.api.java.endpoints.presence.GetState
import com.pubnub.api.java.endpoints.presence.HereNow
import com.pubnub.api.java.endpoints.presence.SetState
import com.pubnub.api.java.endpoints.presence.WhereNow
import com.pubnub.api.java.endpoints.pubsub.Publish
import com.pubnub.api.java.endpoints.pubsub.Signal
import com.pubnub.api.java.endpoints.push.AddChannelsToPush
import com.pubnub.api.java.endpoints.push.ListPushProvisions
import com.pubnub.api.java.endpoints.push.RemoveAllPushChannelsForDevice
import com.pubnub.api.java.endpoints.push.RemoveChannelsFromPush
import com.pubnub.api.java.v2.PNConfiguration
import com.pubnub.api.java.v2.callbacks.EventListener
import com.pubnub.api.java.v2.callbacks.StatusListener
import com.pubnub.api.java.v2.endpoints.pubsub.PublishBuilder
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.java.endpoints.DeleteMessagesImpl
import com.pubnub.internal.java.endpoints.FetchMessagesImpl
import com.pubnub.internal.java.endpoints.HistoryImpl
import com.pubnub.internal.java.endpoints.MessageCountsImpl
import com.pubnub.internal.java.endpoints.access.GrantImpl
import com.pubnub.internal.java.endpoints.access.GrantTokenImpl
import com.pubnub.internal.java.endpoints.access.RevokeTokenImpl
import com.pubnub.internal.java.endpoints.channel_groups.AddChannelChannelGroupImpl
import com.pubnub.internal.java.endpoints.channel_groups.AllChannelsChannelGroupImpl
import com.pubnub.internal.java.endpoints.channel_groups.DeleteChannelGroupImpl
import com.pubnub.internal.java.endpoints.channel_groups.RemoveChannelChannelGroupImpl
import com.pubnub.internal.java.endpoints.files.DeleteFileImpl
import com.pubnub.internal.java.endpoints.files.DownloadFileImpl
import com.pubnub.internal.java.endpoints.files.GetFileUrlImpl
import com.pubnub.internal.java.endpoints.files.ListFilesImpl
import com.pubnub.internal.java.endpoints.files.PublishFileMessageImpl
import com.pubnub.internal.java.endpoints.files.SendFileImpl
import com.pubnub.internal.java.endpoints.message_actions.AddMessageActionImpl
import com.pubnub.internal.java.endpoints.message_actions.GetMessageActionsImpl
import com.pubnub.internal.java.endpoints.message_actions.RemoveMessageActionImpl
import com.pubnub.internal.java.endpoints.objects_api.channel.GetAllChannelsMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.channel.GetChannelMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.channel.RemoveChannelMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.channel.SetChannelMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.members.GetChannelMembersImpl
import com.pubnub.internal.java.endpoints.objects_api.members.ManageChannelMembersImpl
import com.pubnub.internal.java.endpoints.objects_api.members.RemoveChannelMembersImpl
import com.pubnub.internal.java.endpoints.objects_api.members.SetChannelMembersImpl
import com.pubnub.internal.java.endpoints.objects_api.memberships.GetMembershipsImpl
import com.pubnub.internal.java.endpoints.objects_api.memberships.ManageMembershipsImpl
import com.pubnub.internal.java.endpoints.objects_api.memberships.RemoveMembershipsImpl
import com.pubnub.internal.java.endpoints.objects_api.memberships.SetMembershipsImpl
import com.pubnub.internal.java.endpoints.objects_api.uuid.GetAllUUIDMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.uuid.GetUUIDMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.uuid.RemoveUUIDMetadataImpl
import com.pubnub.internal.java.endpoints.objects_api.uuid.SetUUIDMetadataImpl
import com.pubnub.internal.java.endpoints.presence.GetStateImpl
import com.pubnub.internal.java.endpoints.presence.HereNowImpl
import com.pubnub.internal.java.endpoints.presence.SetStateImpl
import com.pubnub.internal.java.endpoints.presence.WhereNowImpl
import com.pubnub.internal.java.endpoints.pubsub.PublishImpl
import com.pubnub.internal.java.endpoints.pubsub.SignalImpl
import com.pubnub.internal.java.endpoints.push.AddChannelsToPushImpl
import com.pubnub.internal.java.endpoints.push.ListPushProvisionsImpl
import com.pubnub.internal.java.endpoints.push.RemoveAllPushChannelsForDeviceImpl
import com.pubnub.internal.java.endpoints.push.RemoveChannelsFromPushImpl
import com.pubnub.internal.java.v2.callbacks.DelegatingEventListener
import com.pubnub.internal.java.v2.callbacks.DelegatingStatusListener
import com.pubnub.internal.java.v2.callbacks.EventEmitterInternal
import com.pubnub.internal.java.v2.entities.ChannelGroupImpl
import com.pubnub.internal.java.v2.entities.ChannelImpl
import com.pubnub.internal.java.v2.entities.ChannelMetadataImpl
import com.pubnub.internal.java.v2.entities.UserMetadataImpl
import com.pubnub.internal.v2.entities.ChannelGroupName
import com.pubnub.internal.v2.entities.ChannelName
import com.pubnub.internal.v2.subscription.SubscriptionInternal
import java.io.InputStream

class PubNubForJavaImpl(configuration: PNConfiguration) :
    PubNubImpl(
        configuration,
        PNSDK_PUBNUB_JAVA
    ),
    PubNub,
    EventEmitterInternal {
    override fun subscribe(): SubscribeBuilder {
        return SubscribeBuilder(this)
    }

    override fun unsubscribe(): UnsubscribeBuilder {
        return UnsubscribeBuilder(this)
    }

    override fun presence(): PresenceBuilder {
        return PresenceBuilder(this)
    }

    // start push
    override fun addPushNotificationsOnChannels(): AddChannelsToPush {
        return AddChannelsToPushImpl(this)
    }

    override fun removePushNotificationsFromChannels(): RemoveChannelsFromPush {
        return RemoveChannelsFromPushImpl(this)
    }

    override fun removeAllPushNotificationsFromDeviceWithPushToken(): RemoveAllPushChannelsForDevice {
        return RemoveAllPushChannelsForDeviceImpl(this)
    }

    override fun auditPushChannelProvisions(): ListPushProvisions {
        return ListPushProvisionsImpl(this)
    }

    // end push
    override fun whereNow(): WhereNow {
        return WhereNowImpl(this)
    }

    override fun hereNow(): HereNow {
        return HereNowImpl(this)
    }

    override fun history(): History {
        return HistoryImpl(this)
    }

    override fun fetchMessages(): FetchMessages {
        return FetchMessagesImpl(this)
    }

    override fun deleteMessages(): DeleteMessages {
        return DeleteMessagesImpl(this)
    }

    override fun messageCounts(): MessageCounts {
        return MessageCountsImpl(this)
    }

    override fun grant(): Grant {
        return GrantImpl(this)
    }

    @Deprecated("Use {@link #grantToken(int)} instead.")
    override fun grantToken(): GrantTokenBuilder {
        return GrantTokenImpl(this)
    }

    override fun grantToken(ttl: Int): GrantTokenBuilder {
        return GrantTokenImpl(this).ttl(ttl) as GrantTokenBuilder
    }

    override fun revokeToken(): RevokeToken {
        return RevokeTokenImpl(this)
    }

    override fun getPresenceState(): GetState {
        return GetStateImpl(this)
    }

    override fun setPresenceState(): SetState {
        return SetStateImpl(this as PubNubImpl)
    }

    override fun publish(): Publish {
        return PublishImpl(this)
    }

    override fun publish(message: Any, channel: String): PublishBuilder {
        return PublishImpl(this).message(message).channel(channel) as PublishBuilder
    }

    override fun signal(): Signal {
        return SignalImpl(this)
    }

    override fun signal(message: Any, channel: String): com.pubnub.api.endpoints.pubsub.Signal {
        return super.signal(channel, message)
    }

    override fun listChannelsForChannelGroup(): AllChannelsChannelGroup {
        return AllChannelsChannelGroupImpl(this)
    }

    override fun addChannelsToChannelGroup(): AddChannelChannelGroup {
        return AddChannelChannelGroupImpl(this)
    }

    override fun removeChannelsFromChannelGroup(): RemoveChannelChannelGroup {
        return RemoveChannelChannelGroupImpl(this)
    }

    override fun deleteChannelGroup(): DeleteChannelGroup {
        return DeleteChannelGroupImpl(this)
    }

    // Start Objects API
    override fun setUUIDMetadata(): SetUUIDMetadata {
        return SetUUIDMetadataImpl(this)
    }

    override fun getAllUUIDMetadata(): GetAllUUIDMetadata {
        return GetAllUUIDMetadataImpl(this)
    }

    override fun getUUIDMetadata(): GetUUIDMetadata {
        return GetUUIDMetadataImpl(this)
    }

    override fun removeUUIDMetadata(): RemoveUUIDMetadata {
        return RemoveUUIDMetadataImpl(this)
    }

    override fun setChannelMetadata(): SetChannelMetadata.Builder {
        return SetChannelMetadataImpl.Builder(this)
    }

    override fun getAllChannelsMetadata(): GetAllChannelsMetadata {
        return GetAllChannelsMetadataImpl(this)
    }

    override fun getChannelMetadata(): GetChannelMetadata.Builder {
        return GetChannelMetadataImpl.Builder(this)
    }

    override fun removeChannelMetadata(): RemoveChannelMetadata.Builder {
        return RemoveChannelMetadataImpl.Builder(this)
    }

    override fun getMemberships(): GetMemberships {
        return GetMembershipsImpl(this)
    }

    override fun setMemberships(): SetMemberships.Builder {
        return SetMembershipsImpl.Builder(this)
    }

    override fun removeMemberships(): RemoveMemberships.Builder {
        return RemoveMembershipsImpl.Builder(this)
    }

    override fun manageMemberships(): ManageMemberships.Builder {
        return ManageMembershipsImpl.Builder(this)
    }

    override fun getChannelMembers(): GetChannelMembers.Builder {
        return GetChannelMembersImpl.Builder(this)
    }

    override fun setChannelMembers(): SetChannelMembers.Builder {
        return SetChannelMembersImpl.Builder(this)
    }

    override fun removeChannelMembers(): RemoveChannelMembers.Builder {
        return RemoveChannelMembersImpl.Builder(this)
    }

    override fun manageChannelMembers(): ManageChannelMembers.Builder {
        return ManageChannelMembersImpl.Builder(this)
    }

    // End Objects API
    // Start Message Actions API
    override fun addMessageAction(): AddMessageAction {
        return AddMessageActionImpl(this)
    }

    override fun getMessageActions(): GetMessageActions {
        return GetMessageActionsImpl(this)
    }

    override fun removeMessageAction(): RemoveMessageAction {
        return RemoveMessageActionImpl(this)
    }

    // End Message Actions API
    override fun sendFile(): SendFile.Builder {
        return SendFileImpl.Builder(this)
    }

    override fun listFiles(): ListFiles.Builder {
        return ListFilesImpl.Builder(this)
    }

    override fun getFileUrl(): GetFileUrl.Builder {
        return GetFileUrlImpl.builder(this)
    }

    override fun downloadFile(): DownloadFile.Builder {
        return DownloadFileImpl.builder(this)
    }

    override fun deleteFile(): DeleteFile.Builder {
        return DeleteFileImpl.builder(this)
    }

    override fun publishFileMessage(): PublishFileMessage.Builder {
        return PublishFileMessageImpl.builder(this)
    }

    override fun reconnect() {
        reconnect(0)
    }

    override fun decrypt(inputString: String): String {
        return super.decrypt(inputString)
    }

    @Throws(com.pubnub.api.PubNubException::class)
    override fun decryptInputStream(inputStream: InputStream): InputStream {
        return this.decryptInputStream(inputStream, null)
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Throws(com.pubnub.api.PubNubException::class)
    override fun encrypt(inputString: String): String {
        return this.encrypt(inputString, null)
    }

    @Throws(com.pubnub.api.PubNubException::class)
    override fun encryptInputStream(inputStream: InputStream): InputStream {
        return this.encryptInputStream(inputStream, null)
    }

    override fun fire(): Publish {
        return publish().shouldStore(false).replicate(false)
    }

    override fun fire(message: Any, channel: String): PublishBuilder {
        return publish(message, channel).shouldStore(false).replicate(false)
    }

    override fun channel(name: String): ChannelImpl {
        return ChannelImpl(this, ChannelName(name))
    }

    override fun channelGroup(name: String): ChannelGroupImpl {
        return ChannelGroupImpl(this, ChannelGroupName(name))
    }

    override fun channelMetadata(id: String): ChannelMetadataImpl {
        return ChannelMetadataImpl(this, ChannelName(id))
    }

    override fun userMetadata(id: String): UserMetadataImpl {
        return UserMetadataImpl(this, ChannelName(id))
    }

    override fun subscriptionSetOf(subscriptions: Set<com.pubnub.api.java.v2.subscriptions.Subscription>): com.pubnub.api.java.v2.subscriptions.SubscriptionSet {
        return com.pubnub.internal.java.v2.subscription.SubscriptionSetImpl(
            this,
            subscriptions as Set<SubscriptionInternal>
        )
    }

    override fun addListener(listener: SubscribeCallback) {
        addListener(listener as EventListener)
        addListener(listener as StatusListener)
    }

    override fun addListener(listener: EventListener) {
        addListener(DelegatingEventListener(listener, this))
    }

    override fun addListener(listener: StatusListener) {
        addListener(DelegatingStatusListener(listener, this))
    }

    override fun removeListener(listener: Listener) {
        if (listener is EventListener) {
            super.removeListener(DelegatingEventListener(listener, this))
        } // no else here to support SubscribeCallbacks which implement both interfaces
        if (listener is StatusListener) {
            super.removeListener(DelegatingStatusListener(listener, this))
        }
    }

    override fun removeAllListeners() {
        super.removeAllListeners()
    }

    companion object {
        private const val PNSDK_PUBNUB_JAVA = "PubNub-Java"
    }
}

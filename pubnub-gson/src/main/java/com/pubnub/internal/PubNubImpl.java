package com.pubnub.internal;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PresenceBuilder;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
import com.pubnub.api.callbacks.Listener;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.DeleteMessages;
import com.pubnub.api.endpoints.FetchMessages;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.endpoints.MessageCounts;
import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.access.RevokeToken;
import com.pubnub.api.endpoints.access.builder.GrantTokenBuilder;
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup;
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup;
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup;
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup;
import com.pubnub.api.endpoints.files.DeleteFile;
import com.pubnub.api.endpoints.files.DownloadFile;
import com.pubnub.api.endpoints.files.GetFileUrl;
import com.pubnub.api.endpoints.files.ListFiles;
import com.pubnub.api.endpoints.files.PublishFileMessage;
import com.pubnub.api.endpoints.files.SendFile;
import com.pubnub.api.endpoints.message_actions.AddMessageAction;
import com.pubnub.api.endpoints.message_actions.GetMessageActions;
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction;
import com.pubnub.api.endpoints.objects_api.channel.GetAllChannelsMetadata;
import com.pubnub.api.endpoints.objects_api.channel.GetChannelMetadata;
import com.pubnub.api.endpoints.objects_api.channel.RemoveChannelMetadata;
import com.pubnub.api.endpoints.objects_api.channel.SetChannelMetadata;
import com.pubnub.api.endpoints.objects_api.members.GetChannelMembers;
import com.pubnub.api.endpoints.objects_api.members.ManageChannelMembers;
import com.pubnub.api.endpoints.objects_api.members.RemoveChannelMembers;
import com.pubnub.api.endpoints.objects_api.members.SetChannelMembers;
import com.pubnub.api.endpoints.objects_api.memberships.GetMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.ManageMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.RemoveMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.SetMemberships;
import com.pubnub.api.endpoints.objects_api.uuid.GetAllUUIDMetadata;
import com.pubnub.api.endpoints.objects_api.uuid.GetUUIDMetadata;
import com.pubnub.api.endpoints.objects_api.uuid.RemoveUUIDMetadata;
import com.pubnub.api.endpoints.objects_api.uuid.SetUUIDMetadata;
import com.pubnub.api.endpoints.presence.GetState;
import com.pubnub.api.endpoints.presence.HereNow;
import com.pubnub.api.endpoints.presence.SetState;
import com.pubnub.api.endpoints.presence.WhereNow;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.endpoints.pubsub.Signal;
import com.pubnub.api.endpoints.push.AddChannelsToPush;
import com.pubnub.api.endpoints.push.ListPushProvisions;
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice;
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush;
import com.pubnub.api.v2.callbacks.EventListener;
import com.pubnub.api.v2.callbacks.StatusListener;
import com.pubnub.api.v2.entities.Channel;
import com.pubnub.api.v2.entities.ChannelGroup;
import com.pubnub.api.v2.entities.ChannelMetadata;
import com.pubnub.api.v2.entities.UserMetadata;
import com.pubnub.api.v2.subscriptions.Subscription;
import com.pubnub.api.v2.subscriptions.SubscriptionSet;
import com.pubnub.internal.callbacks.DelegatingStatusListener;
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback;
import com.pubnub.internal.endpoints.DeleteMessagesImpl;
import com.pubnub.internal.endpoints.FetchMessagesImpl;
import com.pubnub.internal.endpoints.HistoryImpl;
import com.pubnub.internal.endpoints.MessageCountsImpl;
import com.pubnub.internal.endpoints.TimeImpl;
import com.pubnub.internal.endpoints.access.GrantImpl;
import com.pubnub.internal.endpoints.access.GrantTokenImpl;
import com.pubnub.internal.endpoints.access.RevokeTokenImpl;
import com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroupImpl;
import com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroupImpl;
import com.pubnub.internal.endpoints.channel_groups.DeleteChannelGroupImpl;
import com.pubnub.internal.endpoints.channel_groups.ListAllChannelGroupImpl;
import com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroupImpl;
import com.pubnub.internal.endpoints.files.DeleteFileImpl;
import com.pubnub.internal.endpoints.files.DownloadFileImpl;
import com.pubnub.internal.endpoints.files.GetFileUrlImpl;
import com.pubnub.internal.endpoints.files.ListFilesImpl;
import com.pubnub.internal.endpoints.files.PublishFileMessageImpl;
import com.pubnub.internal.endpoints.files.SendFileImpl;
import com.pubnub.internal.endpoints.message_actions.AddMessageActionImpl;
import com.pubnub.internal.endpoints.message_actions.GetMessageActionsImpl;
import com.pubnub.internal.endpoints.message_actions.RemoveMessageActionImpl;
import com.pubnub.internal.endpoints.objects_api.channel.GetAllChannelsMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.channel.GetChannelMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.channel.RemoveChannelMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.channel.SetChannelMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.members.GetChannelMembersImpl;
import com.pubnub.internal.endpoints.objects_api.members.ManageChannelMembersImpl;
import com.pubnub.internal.endpoints.objects_api.members.RemoveChannelMembersImpl;
import com.pubnub.internal.endpoints.objects_api.members.SetChannelMembersImpl;
import com.pubnub.internal.endpoints.objects_api.memberships.GetMembershipsImpl;
import com.pubnub.internal.endpoints.objects_api.memberships.ManageMembershipsImpl;
import com.pubnub.internal.endpoints.objects_api.memberships.RemoveMembershipsImpl;
import com.pubnub.internal.endpoints.objects_api.memberships.SetMembershipsImpl;
import com.pubnub.internal.endpoints.objects_api.uuid.GetAllUUIDMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.uuid.GetUUIDMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.uuid.RemoveUUIDMetadataImpl;
import com.pubnub.internal.endpoints.objects_api.uuid.SetUUIDMetadataImpl;
import com.pubnub.internal.endpoints.presence.GetStateImpl;
import com.pubnub.internal.endpoints.presence.HereNowImpl;
import com.pubnub.internal.endpoints.presence.SetStateImpl;
import com.pubnub.internal.endpoints.presence.WhereNowImpl;
import com.pubnub.internal.endpoints.pubsub.PublishImpl;
import com.pubnub.internal.endpoints.pubsub.SignalImpl;
import com.pubnub.internal.endpoints.push.AddChannelsToPushImpl;
import com.pubnub.internal.endpoints.push.ListPushProvisionsImpl;
import com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDeviceImpl;
import com.pubnub.internal.endpoints.push.RemoveChannelsFromPushImpl;
import com.pubnub.internal.v2.callbacks.DelegatingEventListener;
import com.pubnub.internal.v2.entities.ChannelGroupImpl;
import com.pubnub.internal.v2.entities.ChannelImpl;
import com.pubnub.internal.v2.entities.ChannelMetadataImpl;
import com.pubnub.internal.v2.entities.UserMetadataImpl;
import com.pubnub.internal.v2.subscription.SubscriptionImpl;
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class PubNubImpl extends BasePubNubImpl<
        EventListener,
        Subscription,
        Channel,
        ChannelGroup,
        ChannelMetadata,
        UserMetadata,
        SubscriptionSet,
        StatusListener> implements PubNub {

    private final com.pubnub.api.PNConfiguration configuration;

    public PubNubImpl(@NotNull com.pubnub.api.PNConfiguration configuration) {
        super(configuration.getPnConfigurationCore());
        this.configuration = configuration;
    }

    @Override
    @NotNull
    public PNConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    @NotNull
    public SubscribeBuilder subscribe() {
        return new SubscribeBuilder(getPubNubCore());
    }

    @Override
    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(getPubNubCore());
    }

    @Override
    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(getPubNubCore());
    }

    // start push

    @Override
    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPushImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPushImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDeviceImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisionsImpl(getPubNubCore());
    }

    // end push

    @Override
    @NotNull
    public WhereNow whereNow() {
        return new WhereNowImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public HereNow hereNow() {
        return new HereNowImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public Time time() {
        return new TimeImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public History history() {
        return new HistoryImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessagesImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessagesImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCountsImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public Grant grant() {
        return new GrantImpl(getPubNubCore());
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @Override
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(getPubNubCore(), new GrantTokenImpl(getPubNubCore()));
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(getPubNubCore(), new GrantTokenImpl(getPubNubCore()).ttl(ttl));
    }

    @Override
    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeTokenImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public GetState getPresenceState() {
        return new GetStateImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public SetState setPresenceState() {
        return new SetStateImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public Publish publish() {
        return new PublishImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public Signal signal() {
        return new SignalImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroupImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroupImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroupImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroupImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroupImpl(getPubNubCore());
    }

    // Start Objects API

    @Override
    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadataImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadataImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadataImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadataImpl(getPubNubCore());
    }

    @Override
    public SetChannelMetadata.Builder setChannelMetadata() {
        return new SetChannelMetadataImpl.Builder(getPubNubCore());
    }

    @Override
    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadataImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return new GetChannelMetadataImpl.Builder(getPubNubCore());
    }

    @Override
    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return new RemoveChannelMetadataImpl.Builder(getPubNubCore());
    }

    @Override
    @NotNull
    public GetMemberships getMemberships() {
        return new GetMembershipsImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public SetMemberships.Builder setMemberships() {
        return new SetMembershipsImpl.Builder(getPubNubCore());
    }

    @Override
    public RemoveMemberships.Builder removeMemberships() {
        return new RemoveMembershipsImpl.Builder(getPubNubCore());
    }

    @Override
    public ManageMemberships.Builder manageMemberships() {
        return new ManageMembershipsImpl.Builder(getPubNubCore());
    }

    @Override
    public GetChannelMembers.Builder getChannelMembers() {
        return new GetChannelMembersImpl.Builder(getPubNubCore());
    }

    @Override
    public SetChannelMembers.Builder setChannelMembers() {
        return new SetChannelMembersImpl.Builder(getPubNubCore());
    }

    @Override
    public RemoveChannelMembers.Builder removeChannelMembers() {
        return new RemoveChannelMembersImpl.Builder(getPubNubCore());
    }

    @Override
    public ManageChannelMembers.Builder manageChannelMembers() {
        return new ManageChannelMembersImpl.Builder(getPubNubCore());
    }

    // End Objects API

    // Start Message Actions API

    @Override
    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageActionImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActionsImpl(getPubNubCore());
    }

    @Override
    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageActionImpl(getPubNubCore());
    }

    // End Message Actions API

    @Override
    public SendFile.Builder sendFile() {
        return new SendFileImpl.Builder(getPubNubCore());
    }

    @Override
    public ListFiles.Builder listFiles() {
        return new ListFilesImpl.Builder(getPubNubCore());
    }

    @Override
    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrlImpl.builder(getPubNubCore());
    }

    @Override
    public DownloadFile.Builder downloadFile() {
        return DownloadFileImpl.builder(getPubNubCore());
    }

    @Override
    public DeleteFile.Builder deleteFile() {
        return DeleteFileImpl.builder(getPubNubCore());
    }

    @Override
    public PublishFileMessage.Builder publishFileMessage() {
        return PublishFileMessageImpl.builder(getPubNubCore());
    }

    @Override
    public void reconnect() {
        super.reconnect(0);
    }

    // public methods

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Override
    public @NotNull String decrypt(@NotNull String inputString) throws PubNubException {
        return decrypt(inputString, null);
    }

    /**
     * Perform Cryptographic decryption of an input string using the cipher key
     *
     * @param inputString String to be encrypted
     * @param cipherKey   cipher key to be used for encryption
     * @return String containing the encryption of inputString using cipherKey
     * @throws PubNubException throws exception in case of failed encryption
     */
    @Override
    public @NotNull String decrypt(@NotNull String inputString, String cipherKey) throws PubNubException {
        if (inputString == null) {
            throw new PubNubException(PubNubError.INVALID_ARGUMENTS);
        }
        return getPubNubCore().decrypt(inputString, cipherKey);
    }

    @Override
    public @NotNull InputStream decryptInputStream(@NotNull InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, null);
    }

    @Override
    public @NotNull InputStream decryptInputStream(@NotNull InputStream inputStream, @Nullable String cipherKey) throws PubNubException {
        if (inputStream == null) {
            throw new PubNubException(PubNubError.INVALID_ARGUMENTS);
        }
        return getPubNubCore().decryptInputStream(inputStream, cipherKey);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Override
    public @NotNull String encrypt(@NotNull String inputString) throws PubNubException {
        return encrypt(inputString, null);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key.
     *
     * @param inputString String to be encrypted
     * @param cipherKey   cipher key to be used for encryption
     * @return String containing the encryption of inputString using cipherKey
     * @throws PubNubException throws exception in case of failed encryption
     */
    @Override
    public @NotNull String encrypt(@NotNull String inputString, String cipherKey) throws PubNubException {
        if (inputString == null) {
            throw new PubNubException(PubNubError.INVALID_ARGUMENTS);
        }
        return getPubNubCore().encrypt(inputString, cipherKey);
    }

    @Override
    public @NotNull InputStream encryptInputStream(@NotNull InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, null);
    }

    @Override
    public @NotNull InputStream encryptInputStream(@NotNull InputStream inputStream, String cipherKey) throws PubNubException {
        return getPubNubCore().encryptInputStream(inputStream, cipherKey);
    }

    @Override
    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @Override
    @NotNull
    public List<String> getSubscribedChannels() {
        return getPubNubCore().getSubscribedChannels();
    }

    @Override
    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return getPubNubCore().getSubscribedChannelGroups();
    }

    @NotNull
    @Override
    public Channel channel(@NotNull String name) {
        return new ChannelImpl(this, name);
    }

    @NotNull
    @Override
    public ChannelGroup channelGroup(@NotNull String name) {
        return new ChannelGroupImpl(this, name);
    }

    @NotNull
    @Override
    public ChannelMetadata channelMetadata(@NotNull String id) {
        return new ChannelMetadataImpl(this, id);
    }

    @NotNull
    @Override
    public UserMetadata userMetadata(@NotNull String id) {
        return new UserMetadataImpl(this, id);
    }

    @NotNull
    @Override
    public SubscriptionSet subscriptionSetOf(@NotNull Set<? extends Subscription> subscriptions) {
        return new SubscriptionSetImpl(getPubNubCore(), (Set<SubscriptionImpl>) subscriptions);
    }

    @Override
    public void addListener(@NotNull EventListener listener) {
        getListenerManager().addListener(new DelegatingEventListener(listener));
    }

    @Override
    public void addListener(@NotNull StatusListener listener) {
        getListenerManager().addListener(new DelegatingStatusListener(listener));
    }

    @Override
    public void addListener(@NotNull SubscribeCallback listener) {
        getListenerManager().addListener(new DelegatingSubscribeCallback(listener));
    }

    @Override
    public void removeListener(@NotNull Listener listener) {
        if (listener instanceof SubscribeCallback) {
            super.removeListener(new DelegatingSubscribeCallback((SubscribeCallback) listener));
        } else if (listener instanceof EventListener) {
            super.removeListener(new DelegatingEventListener((EventListener) listener));
        } else if (listener instanceof StatusListener) {
            super.removeListener(new DelegatingStatusListener((StatusListener) listener));
        } else {
            super.removeListener(listener);
        }
    }
}

package com.pubnub.internal;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PresenceBuilder;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.DeleteMessages;
import com.pubnub.api.endpoints.FetchMessages;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.endpoints.MessageCounts;
import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.access.GrantToken;
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
import com.pubnub.internal.v2.subscription.SubscriptionSetImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Collections;
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
        return new SubscribeBuilder(getCorePubNubClient());
    }

    @Override
    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(getCorePubNubClient());
    }

    @Override
    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(getCorePubNubClient());
    }

    // start push

    @Override
    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPushImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPushImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDeviceImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisionsImpl(getCorePubNubClient());
    }

    // end push

    @Override
    @NotNull
    public WhereNow whereNow() {
        return new WhereNowImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public HereNow hereNow() {
        return new HereNowImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public Time time() {
        return new TimeImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public History history() {
        return new HistoryImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessagesImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessagesImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCountsImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public Grant grant() {
        return new Grant(getCorePubNubClient());
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @Override
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(getCorePubNubClient(), new GrantToken(getCorePubNubClient()));
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(getCorePubNubClient(), new GrantToken(getCorePubNubClient()).ttl(ttl));
    }

    @Override
    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeToken(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetState getPresenceState() {
        return new GetStateImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public SetState setPresenceState() {
        return new SetStateImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public Publish publish() {
        return new PublishImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public Signal signal() {
        return new SignalImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(getCorePubNubClient());
    }

    @Override
    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(getCorePubNubClient());
    }

    @Override
    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(getCorePubNubClient());
    }

    @Override
    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(getCorePubNubClient());
    }

    @Override
    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(getCorePubNubClient());
    }

    // Start Objects API

    @Override
    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadataImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadataImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadataImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadataImpl(getCorePubNubClient());
    }

    @Override
    public SetChannelMetadata.Builder setChannelMetadata() {
        return new SetChannelMetadataImpl.Builder(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadataImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return new GetChannelMetadataImpl.Builder(getCorePubNubClient());
    }

    @Override
    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return new RemoveChannelMetadataImpl.Builder(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetMemberships getMemberships() {
        return new GetMembershipsImpl(getCorePubNubClient());
    }

    @Override
    @NotNull
    public SetMemberships.Builder setMemberships() {
        return new SetMembershipsImpl.Builder(getCorePubNubClient());
    }

    @Override
    public RemoveMemberships.Builder removeMemberships() {
        return new RemoveMembershipsImpl.Builder(getCorePubNubClient());
    }

    @Override
    public ManageMemberships.Builder manageMemberships() {
        return new ManageMembershipsImpl.Builder(getCorePubNubClient());
    }

    @Override
    public GetChannelMembers.Builder getChannelMembers() {
        return new GetChannelMembersImpl.Builder(getCorePubNubClient());
    }

    @Override
    public SetChannelMembers.Builder setChannelMembers() {
        return new SetChannelMembersImpl.Builder(getCorePubNubClient());
    }

    @Override
    public RemoveChannelMembers.Builder removeChannelMembers() {
        return new RemoveChannelMembersImpl.Builder(getCorePubNubClient());
    }

    @Override
    public ManageChannelMembers.Builder manageChannelMembers() {
        return new ManageChannelMembersImpl.Builder(getCorePubNubClient());
    }

    // End Objects API

    // Start Message Actions API

    @Override
    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageAction(getCorePubNubClient());
    }

    @Override
    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActions(getCorePubNubClient());
    }

    @Override
    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageAction(getCorePubNubClient());
    }

    // End Message Actions API

    @Override
    public SendFile.Builder sendFile() {
        return SendFile.builder(getCorePubNubClient());
    }

    @Override
    public ListFiles.Builder listFiles() {
        return new ListFiles.Builder(getCorePubNubClient());
    }

    @Override
    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrl.builder(getCorePubNubClient());
    }

    @Override
    public DownloadFile.Builder downloadFile() {
        return DownloadFile.builder(getCorePubNubClient());
    }

    @Override
    public DeleteFile.Builder deleteFile() {
        return DeleteFile.builder(getCorePubNubClient());
    }

    @Override
    public PublishFileMessage.Builder publishFileMessage() {
        return PublishFileMessage.builder(getCorePubNubClient());
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
        return getCorePubNubClient().decrypt(inputString, cipherKey);
    }

    @Override
    public @NotNull InputStream decryptInputStream(@NotNull InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, null);
    }

    @Override
    public @NotNull InputStream decryptInputStream(@NotNull InputStream inputStream, @Nullable String cipherKey) throws PubNubException {
        return getCorePubNubClient().decryptInputStream(inputStream, cipherKey);
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
        return getCorePubNubClient().encrypt(inputString, cipherKey);
    }

    @Override
    public @NotNull InputStream encryptInputStream(@NotNull InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, null);
    }

    @Override
    public @NotNull InputStream encryptInputStream(@NotNull InputStream inputStream, String cipherKey) throws PubNubException {
        return getCorePubNubClient().encryptInputStream(inputStream, cipherKey);
    }

    @Override
    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @Override
    @NotNull
    public List<String> getSubscribedChannels() {
        return getCorePubNubClient().getSubscribedChannels();
    }

    @Override
    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return getCorePubNubClient().getSubscribedChannelGroups();
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
        return new SubscriptionSetImpl(getCorePubNubClient(), Collections.emptySet());
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
}

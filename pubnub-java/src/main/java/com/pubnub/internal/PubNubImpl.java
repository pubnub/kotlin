package com.pubnub.internal;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
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
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
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
import java.util.UUID;

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
        super(configuration.getPnConfiguration());
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
        return new SubscribeBuilder(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(getInternalPubNubClient());
    }

    // start push

    @Override
    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPushImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPushImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDeviceImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisionsImpl(getInternalPubNubClient());
    }

    // end push

    @Override
    @NotNull
    public WhereNow whereNow() {
        return new WhereNowImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public HereNow hereNow() {
        return new HereNowImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public Time time() {
        return new TimeImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public History history() {
        return new HistoryImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessagesImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessagesImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCountsImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public Grant grant() {
        return new Grant(getInternalPubNubClient());
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @Override
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(getInternalPubNubClient(), new GrantToken(getInternalPubNubClient()));
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(getInternalPubNubClient(), new GrantToken(getInternalPubNubClient()).ttl(ttl));
    }

    @Override
    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeToken(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetState getPresenceState() {
        return new GetStateImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public SetState setPresenceState() {
        return new SetStateImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public Publish publish() {
        return new PublishImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public Signal signal() {
        return new SignalImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(getInternalPubNubClient());
    }

    // Start Objects API

    @Override
    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadata(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadata(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadata(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadata(getInternalPubNubClient());
    }

    @Override
    public SetChannelMetadata.Builder setChannelMetadata() {
        return new SetChannelMetadataImpl.Builder(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadataImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return new GetChannelMetadataImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return new RemoveChannelMetadataImpl.Builder(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetMemberships getMemberships() {
        return new GetMembershipsImpl(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public SetMemberships.Builder setMemberships() {
        return new SetMembershipsImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public RemoveMemberships.Builder removeMemberships() {
        return new RemoveMembershipsImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public ManageMemberships.Builder manageMemberships() {
        return new ManageMembershipsImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public GetChannelMembers.Builder getChannelMembers() {
        return new GetChannelMembersImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public SetChannelMembers.Builder setChannelMembers() {
        return new SetChannelMembersImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public RemoveChannelMembers.Builder removeChannelMembers() {
        return new RemoveChannelMembersImpl.Builder(getInternalPubNubClient());
    }

    @Override
    public ManageChannelMembers.Builder manageChannelMembers() {
        return new ManageChannelMembersImpl.Builder(getInternalPubNubClient());
    }

    // End Objects API

    // Start Message Actions API

    @Override
    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageAction(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActions(getInternalPubNubClient());
    }

    @Override
    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageAction(getInternalPubNubClient());
    }

    // End Message Actions API

    @Override
    public SendFile.Builder sendFile() {
        return SendFile.builder(getInternalPubNubClient());
    }

    @Override
    public ListFiles.Builder listFiles() {
        return new ListFiles.Builder(getInternalPubNubClient());
    }

    @Override
    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrl.builder(getInternalPubNubClient());
    }

    @Override
    public DownloadFile.Builder downloadFile() {
        return DownloadFile.builder(getInternalPubNubClient());
    }

    @Override
    public DeleteFile.Builder deleteFile() {
        return DeleteFile.builder(getInternalPubNubClient());
    }

    @Override
    public PublishFileMessage.Builder publishFileMessage() {
        return PublishFileMessage.builder(getInternalPubNubClient());
    }

    // public methods

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Override
    @Nullable
    public String decrypt(String inputString) throws PubNubException {
        return decrypt(inputString, getInternalPubNubClient().getConfiguration().getCipherKey());
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
    @Nullable
    public String decrypt(String inputString, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().decrypt(inputString, cipherKey);
    }

    @Override
    public InputStream decryptInputStream(InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, getInternalPubNubClient().getConfiguration().getCipherKey());
    }

    @Override
    public InputStream decryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().decryptInputStream(inputStream, cipherKey);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Override
    @Nullable
    public String encrypt(String inputString) throws PubNubException {
        return encrypt(inputString, getInternalPubNubClient().getConfiguration().getCipherKey());
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
    @Nullable
    public String encrypt(String inputString, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().encrypt(inputString, cipherKey);
    }

    @Override
    public InputStream encryptInputStream(InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, getInternalPubNubClient().getConfiguration().getCipherKey());
    }

    @Override
    public InputStream encryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().encryptInputStream(inputStream, cipherKey);
    }

    /**
     * @return request uuid.
     */
    @Override
    @NotNull
    public String getRequestId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Perform a Reconnect to the network
     */
    @Override
    public void reconnect() {
        getInternalPubNubClient().reconnect(0L);
    }

    /**
     * Perform a disconnect from the listeners
     */
    @Override
    public void disconnect() {
        getInternalPubNubClient().disconnect();
    }

    @Override
    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @Override
    @NotNull
    public List<String> getSubscribedChannels() {
        return getInternalPubNubClient().getSubscribedChannels();
    }

    @Override
    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return getInternalPubNubClient().getSubscribedChannelGroups();
    }

    @Override
    public void unsubscribeAll() {
        getInternalPubNubClient().unsubscribeAll();
    }

    @Override
    public PNToken parseToken(String token) throws PubNubException {
        return getInternalPubNubClient().parseToken(token);
    }

    @Override
    public void setToken(String token) {
        getInternalPubNubClient().setToken(token);
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
        return new SubscriptionSetImpl(getInternalPubNubClient(), Collections.emptySet());
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

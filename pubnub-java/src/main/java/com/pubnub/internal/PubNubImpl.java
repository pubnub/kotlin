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
import com.pubnub.api.v2.subscription.Subscription;
import com.pubnub.api.v2.subscription.SubscriptionSet;
import com.pubnub.internal.callbacks.DelegatingStatusListener;
import com.pubnub.internal.callbacks.DelegatingSubscribeCallback;
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

    @NotNull
    public PNConfiguration getConfiguration() {
        return configuration;
    }

    @NotNull
    public SubscribeBuilder subscribe() {
        return new SubscribeBuilder(getInternalPubNubClient());
    }

    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(getInternalPubNubClient());
    }

    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(getInternalPubNubClient());
    }

    // start push

    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPush(getInternalPubNubClient());
    }

    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPush(getInternalPubNubClient());
    }

    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDevice(getInternalPubNubClient());
    }

    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisions(getInternalPubNubClient());
    }

    // end push

    @NotNull
    public WhereNow whereNow() {
        return new WhereNow(getInternalPubNubClient());
    }

    @NotNull
    public HereNow hereNow() {
        return new HereNow(getInternalPubNubClient());
    }

    @NotNull
    public Time time() {
        return new Time(getInternalPubNubClient());
    }

    @NotNull
    public History history() {
        return new History(getInternalPubNubClient());
    }

    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessages(getInternalPubNubClient());
    }

    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessages(getInternalPubNubClient());
    }

    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCounts(getInternalPubNubClient());
    }

    @NotNull
    public Grant grant() {
        return new Grant(getInternalPubNubClient());
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(getInternalPubNubClient(), new GrantToken(getInternalPubNubClient()));
    }

    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(getInternalPubNubClient(), new GrantToken(getInternalPubNubClient()).ttl(ttl));
    }

    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeToken(getInternalPubNubClient());
    }

    @NotNull
    public GetState getPresenceState() {
        return new GetState(getInternalPubNubClient());
    }

    @NotNull
    public SetState setPresenceState() {
        return new SetState(getInternalPubNubClient());
    }

    @NotNull
    public Publish publish() {
        return new Publish(getInternalPubNubClient());
    }

    @NotNull
    public Signal signal() {
        return new Signal(getInternalPubNubClient());
    }

    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(getInternalPubNubClient());
    }

    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(getInternalPubNubClient());
    }

    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(getInternalPubNubClient());
    }

    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(getInternalPubNubClient());
    }

    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(getInternalPubNubClient());
    }

    // Start Objects API

    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadata(getInternalPubNubClient());
    }

    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadata(getInternalPubNubClient());
    }

    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadata(getInternalPubNubClient());
    }

    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadata(getInternalPubNubClient());
    }

    public SetChannelMetadata.Builder setChannelMetadata() {
        return SetChannelMetadata.builder(getInternalPubNubClient());
    }

    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadata(getInternalPubNubClient());
    }

    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return GetChannelMetadata.builder(getInternalPubNubClient());
    }

    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return RemoveChannelMetadata.builder(getInternalPubNubClient());
    }

    @NotNull
    public GetMemberships getMemberships() {
        return new GetMemberships(getInternalPubNubClient());
    }

    @NotNull
    public SetMemberships.Builder setMemberships() {
        return SetMemberships.builder(getInternalPubNubClient());
    }

    public RemoveMemberships.Builder removeMemberships() {
        return RemoveMemberships.builder(getInternalPubNubClient());
    }

    public ManageMemberships.Builder manageMemberships() {
        return ManageMemberships.builder(getInternalPubNubClient());
    }

    public GetChannelMembers.Builder getChannelMembers() {
        return GetChannelMembers.builder(getInternalPubNubClient());
    }

    public SetChannelMembers.Builder setChannelMembers() {
        return SetChannelMembers.builder(getInternalPubNubClient());
    }

    public RemoveChannelMembers.Builder removeChannelMembers() {
        return RemoveChannelMembers.builder(getInternalPubNubClient());
    }

    public ManageChannelMembers.Builder manageChannelMembers() {
        return ManageChannelMembers.builder(getInternalPubNubClient());
    }

    // End Objects API

    // Start Message Actions API

    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageAction(getInternalPubNubClient());
    }

    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActions(getInternalPubNubClient());
    }

    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageAction(getInternalPubNubClient());
    }

    // End Message Actions API

    public SendFile.Builder sendFile() {
        return SendFile.builder(getInternalPubNubClient());
    }

    public ListFiles.Builder listFiles() {
        return new ListFiles.Builder(getInternalPubNubClient());
    }

    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrl.builder(getInternalPubNubClient());
    }

    public DownloadFile.Builder downloadFile() {
        return DownloadFile.builder(getInternalPubNubClient());
    }

    public DeleteFile.Builder deleteFile() {
        return DeleteFile.builder(getInternalPubNubClient());
    }

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
    @Nullable
    public String decrypt(String inputString, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().decrypt(inputString, cipherKey);
    }

    public InputStream decryptInputStream(InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, getInternalPubNubClient().getConfiguration().getCipherKey());
    }

    public InputStream decryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().decryptInputStream(inputStream, cipherKey);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
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
    @Nullable
    public String encrypt(String inputString, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().encrypt(inputString, cipherKey);
    }

    public InputStream encryptInputStream(InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, getInternalPubNubClient().getConfiguration().getCipherKey());
    }

    public InputStream encryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getInternalPubNubClient().encryptInputStream(inputStream, cipherKey);
    }

    /**
     * @return request uuid.
     */
    @NotNull
    public String getRequestId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Perform a Reconnect to the network
     */
    public void reconnect() {
        getInternalPubNubClient().reconnect(0L);
    }

    /**
     * Perform a disconnect from the listeners
     */
    public void disconnect() {
        getInternalPubNubClient().disconnect();
    }

    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @NotNull
    public List<String> getSubscribedChannels() {
        return getInternalPubNubClient().getSubscribedChannels();
    }

    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return getInternalPubNubClient().getSubscribedChannelGroups();
    }

    public void unsubscribeAll() {
        getInternalPubNubClient().unsubscribeAll();
    }

    public PNToken parseToken(String token) throws PubNubException {
        return getInternalPubNubClient().parseToken(token);
    }

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
        return new ChannelGroupImpl(this, name);    }

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

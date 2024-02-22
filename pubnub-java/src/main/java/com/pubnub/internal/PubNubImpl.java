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
        return new SubscribeBuilder(getInternalPubnub());
    }

    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(getInternalPubnub());
    }

    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(getInternalPubnub());
    }

    // start push

    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPush(getInternalPubnub());
    }

    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPush(getInternalPubnub());
    }

    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDevice(getInternalPubnub());
    }

    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisions(getInternalPubnub());
    }

    // end push

    @NotNull
    public WhereNow whereNow() {
        return new WhereNow(getInternalPubnub());
    }

    @NotNull
    public HereNow hereNow() {
        return new HereNow(getInternalPubnub());
    }

    @NotNull
    public Time time() {
        return new Time(getInternalPubnub());
    }

    @NotNull
    public History history() {
        return new History(getInternalPubnub());
    }

    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessages(getInternalPubnub());
    }

    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessages(getInternalPubnub());
    }

    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCounts(getInternalPubnub());
    }

    @NotNull
    public Grant grant() {
        return new Grant(getInternalPubnub());
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(getInternalPubnub(), new GrantToken(getInternalPubnub()));
    }

    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(getInternalPubnub(), new GrantToken(getInternalPubnub()).ttl(ttl));
    }

    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeToken(getInternalPubnub());
    }

    @NotNull
    public GetState getPresenceState() {
        return new GetState(getInternalPubnub());
    }

    @NotNull
    public SetState setPresenceState() {
        return new SetState(getInternalPubnub());
    }

    @NotNull
    public Publish publish() {
        return new Publish(getInternalPubnub());
    }

    @NotNull
    public Signal signal() {
        return new Signal(getInternalPubnub());
    }

    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(getInternalPubnub());
    }

    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(getInternalPubnub());
    }

    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(getInternalPubnub());
    }

    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(getInternalPubnub());
    }

    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(getInternalPubnub());
    }

    // Start Objects API

    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadata(getInternalPubnub());
    }

    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadata(getInternalPubnub());
    }

    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadata(getInternalPubnub());
    }

    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadata(getInternalPubnub());
    }

    public SetChannelMetadata.Builder setChannelMetadata() {
        return SetChannelMetadata.builder(getInternalPubnub());
    }

    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadata(getInternalPubnub());
    }

    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return GetChannelMetadata.builder(getInternalPubnub());
    }

    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return RemoveChannelMetadata.builder(getInternalPubnub());
    }

    @NotNull
    public GetMemberships getMemberships() {
        return new GetMemberships(getInternalPubnub());
    }

    @NotNull
    public SetMemberships.Builder setMemberships() {
        return SetMemberships.builder(getInternalPubnub());
    }

    public RemoveMemberships.Builder removeMemberships() {
        return RemoveMemberships.builder(getInternalPubnub());
    }

    public ManageMemberships.Builder manageMemberships() {
        return ManageMemberships.builder(getInternalPubnub());
    }

    public GetChannelMembers.Builder getChannelMembers() {
        return GetChannelMembers.builder(getInternalPubnub());
    }

    public SetChannelMembers.Builder setChannelMembers() {
        return SetChannelMembers.builder(getInternalPubnub());
    }

    public RemoveChannelMembers.Builder removeChannelMembers() {
        return RemoveChannelMembers.builder(getInternalPubnub());
    }

    public ManageChannelMembers.Builder manageChannelMembers() {
        return ManageChannelMembers.builder(getInternalPubnub());
    }

    // End Objects API

    // Start Message Actions API

    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageAction(getInternalPubnub());
    }

    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActions(getInternalPubnub());
    }

    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageAction(getInternalPubnub());
    }

    // End Message Actions API

    public SendFile.Builder sendFile() {
        return SendFile.builder(getInternalPubnub());
    }

    public ListFiles.Builder listFiles() {
        return new ListFiles.Builder(getInternalPubnub());
    }

    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrl.builder(getInternalPubnub());
    }

    public DownloadFile.Builder downloadFile() {
        return DownloadFile.builder(getInternalPubnub());
    }

    public DeleteFile.Builder deleteFile() {
        return DeleteFile.builder(getInternalPubnub());
    }

    public PublishFileMessage.Builder publishFileMessage() {
        return PublishFileMessage.builder(getInternalPubnub());
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
        return decrypt(inputString, getInternalPubnub().getConfiguration().getCipherKey());
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
        return getInternalPubnub().decrypt(inputString, cipherKey);
    }

    public InputStream decryptInputStream(InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, getInternalPubnub().getConfiguration().getCipherKey());
    }

    public InputStream decryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getInternalPubnub().decryptInputStream(inputStream, cipherKey);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Nullable
    public String encrypt(String inputString) throws PubNubException {
        return encrypt(inputString, getInternalPubnub().getConfiguration().getCipherKey());
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
        return getInternalPubnub().encrypt(inputString, cipherKey);
    }

    public InputStream encryptInputStream(InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, getInternalPubnub().getConfiguration().getCipherKey());
    }

    public InputStream encryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getInternalPubnub().encryptInputStream(inputStream, cipherKey);
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
        getInternalPubnub().reconnect(0L);
    }

    /**
     * Perform a disconnect from the listeners
     */
    public void disconnect() {
        getInternalPubnub().disconnect();
    }

    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @NotNull
    public List<String> getSubscribedChannels() {
        return getInternalPubnub().getSubscribedChannels();
    }

    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return getInternalPubnub().getSubscribedChannelGroups();
    }

    public void unsubscribeAll() {
        getInternalPubnub().unsubscribeAll();
    }

    public PNToken parseToken(String token) throws PubNubException {
        return getInternalPubnub().parseToken(token);
    }

    public void setToken(String token) {
        getInternalPubnub().setToken(token);
    }

    @NotNull
    @Override
    public Channel channel(@NotNull String name) {
        return new ChannelImpl(getInternalPubnub(), name);
    }

    @NotNull
    @Override
    public ChannelGroup channelGroup(@NotNull String name) {
        return new ChannelGroupImpl(getInternalPubnub(), name);    }

    @NotNull
    @Override
    public ChannelMetadata channelMetadata(@NotNull String id) {
        return new ChannelMetadataImpl(getInternalPubnub(), id);
    }

    @NotNull
    @Override
    public UserMetadata userMetadata(@NotNull String id) {
        return new UserMetadataImpl(getInternalPubnub(), id);
    }

    @NotNull
    @Override
    public SubscriptionSet subscriptionSetOf(@NotNull Set<? extends Subscription> subscriptions) {
        return new SubscriptionSetImpl(getInternalPubnub(), Collections.emptySet());
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

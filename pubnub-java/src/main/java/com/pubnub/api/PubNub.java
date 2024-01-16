package com.pubnub.api;

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
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.internal.managers.ListenerManager;
import com.pubnub.internal.models.consumer.pubsub.objects.PNObjectEventResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class PubNub {

    private final com.pubnub.internal.PubNub pubnubImpl;
    private final com.pubnub.api.PNConfiguration config;
    private final ListenerManager<PubNub> listenerManager = new ListenerManager<>(this);

    PubNub(@NotNull com.pubnub.api.PNConfiguration initialConfig, @NotNull com.pubnub.internal.PubNub pubnubImpl) {
        this.config = initialConfig;
        this.pubnubImpl = pubnubImpl;
        com.pubnub.internal.callbacks.SubscribeCallback<com.pubnub.internal.PubNub> listener = new com.pubnub.internal.callbacks.SubscribeCallback<com.pubnub.internal.PubNub>() {
            @Override
            public void status(com.pubnub.internal.PubNub pubnub, @NotNull PNStatus pnStatus) {
                listenerManager.announce(pnStatus);
            }

            @Override
            public void message(com.pubnub.internal.PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                listenerManager.announce(pnMessageResult);
            }

            @Override
            public void presence(com.pubnub.internal.PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult) {
                listenerManager.announce(pnPresenceEventResult);
            }

            @Override
            public void signal(com.pubnub.internal.PubNub pubnub, @NotNull PNSignalResult pnSignalResult) {
                listenerManager.announce(pnSignalResult);
            }

            @Override
            public void messageAction(com.pubnub.internal.PubNub pubnub, @NotNull PNMessageActionResult pnMessageActionResult) {
                listenerManager.announce(pnMessageActionResult);
            }

            @Override
            public void objects(com.pubnub.internal.PubNub pubnub, @NotNull PNObjectEventResult objectEvent) {
                listenerManager.announce(objectEvent);
            }

            @Override
            public void file(com.pubnub.internal.PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {
                listenerManager.announce(pnFileEventResult);
            }
        };
        pubnubImpl.addListener(listener);
    }

    public PubNub(@NotNull com.pubnub.api.PNConfiguration initialConfig) {
        this(initialConfig, new com.pubnub.internal.PubNub(initialConfig.getInternalConfig()));
    }

    public @NotNull PNConfiguration getConfiguration() {
        return config;
    }

    public void addListener(@NotNull SubscribeCallback listener) {
        listenerManager.addListener(listener);
    }

    public void removeListener(@NotNull SubscribeCallback listener) {
        listenerManager.removeListener(listener);
    }

    @NotNull
    public SubscribeBuilder subscribe() {
        return new SubscribeBuilder(pubnubImpl);
    }

    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(pubnubImpl);
    }

    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(pubnubImpl);
    }

    // start push

    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPush(pubnubImpl);
    }

    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPush(pubnubImpl);
    }

    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDevice(pubnubImpl);
    }

    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisions(pubnubImpl);
    }

    // end push

    @NotNull
    public WhereNow whereNow() {
        return new WhereNow(pubnubImpl);
    }

    @NotNull
    public HereNow hereNow() {
        return new HereNow(pubnubImpl);
    }

    @NotNull
    public Time time() {
        return new Time(pubnubImpl);
    }

    @NotNull
    public History history() {
        return new History(pubnubImpl);
    }

    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessages(pubnubImpl);
    }

    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessages(pubnubImpl);
    }

    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCounts(pubnubImpl);
    }

    @NotNull
    public Grant grant() {
        return new Grant(pubnubImpl);
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(pubnubImpl, new GrantToken(pubnubImpl));
    }

    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(pubnubImpl, new GrantToken(pubnubImpl).ttl(ttl));
    }

    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeToken(pubnubImpl);
    }

    @NotNull
    public GetState getPresenceState() {
        return new GetState(pubnubImpl);
    }

    @NotNull
    public SetState setPresenceState() {
        return new SetState(pubnubImpl);
    }

    @NotNull
    public Publish publish() {
        return new Publish(pubnubImpl);
    }

    @NotNull
    public Signal signal() {
        return new Signal(pubnubImpl);
    }

    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(pubnubImpl);
    }

    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(pubnubImpl);
    }

    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(pubnubImpl);
    }

    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(pubnubImpl);
    }

    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(pubnubImpl);
    }

    // Start Objects API

    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadata(pubnubImpl);
    }

    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadata(pubnubImpl);
    }

    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadata(pubnubImpl);
    }

    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadata(pubnubImpl);
    }

    public SetChannelMetadata.Builder setChannelMetadata() {
        return SetChannelMetadata.builder(pubnubImpl);
    }

    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadata(pubnubImpl);
    }

    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return GetChannelMetadata.builder(pubnubImpl);
    }

    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return RemoveChannelMetadata.builder(pubnubImpl);
    }

    @NotNull
    public GetMemberships getMemberships() {
        return new GetMemberships(pubnubImpl);
    }

    @NotNull
    public SetMemberships.Builder setMemberships() {
        return SetMemberships.builder(pubnubImpl);
    }

    public RemoveMemberships.Builder removeMemberships() {
        return RemoveMemberships.builder(pubnubImpl);
    }

    public ManageMemberships.Builder manageMemberships() {
        return ManageMemberships.builder(pubnubImpl);
    }

    public GetChannelMembers.Builder getChannelMembers() {
        return GetChannelMembers.builder(pubnubImpl);
    }

    public SetChannelMembers.Builder setChannelMembers() {
        return SetChannelMembers.builder(pubnubImpl);
    }

    public RemoveChannelMembers.Builder removeChannelMembers() {
        return RemoveChannelMembers.builder(pubnubImpl);
    }

    public ManageChannelMembers.Builder manageChannelMembers() {
        return ManageChannelMembers.builder(pubnubImpl);
    }

    // End Objects API

    // Start Message Actions API

    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageAction(pubnubImpl);
    }

    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActions(pubnubImpl);
    }

    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageAction(pubnubImpl);
    }

    // End Message Actions API

    public SendFile.Builder sendFile() {
        return SendFile.builder(pubnubImpl);
    }

    public ListFiles.Builder listFiles() {
        return new ListFiles.Builder(pubnubImpl);
    }

    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrl.builder(pubnubImpl);
    }

    public DownloadFile.Builder downloadFile() {
        return DownloadFile.builder(pubnubImpl);
    }

    public DeleteFile.Builder deleteFile() {
        return DeleteFile.builder(pubnubImpl);
    }

    public PublishFileMessage.Builder publishFileMessage() {
        return PublishFileMessage.builder(pubnubImpl);
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
        return decrypt(inputString, pubnubImpl.getConfiguration().getCipherKey());
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
        return pubnubImpl.decrypt(inputString, cipherKey);
    }

    public InputStream decryptInputStream(InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, pubnubImpl.getConfiguration().getCipherKey());
    }

    public InputStream decryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return pubnubImpl.decryptInputStream(inputStream, cipherKey);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Nullable
    public String encrypt(@NotNull String inputString) throws PubNubException {
        return encrypt(inputString, pubnubImpl.getConfiguration().getCipherKey());
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
        return pubnubImpl.encrypt(inputString, cipherKey);
    }

    public InputStream encryptInputStream(InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, pubnubImpl.getConfiguration().getCipherKey());
    }

    public InputStream encryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return pubnubImpl.encryptInputStream(inputStream, cipherKey);
    }

//    TODO bring back?
//    public int getTimestamp() {
//        return (int) ((new Date().getTime()) / TIMESTAMP_DIVIDER);
//    }

    /**
     * @return instance uuid.
     */
    @NotNull
    public String getInstanceId() {
        return pubnubImpl.getInstanceId();
    }

    /**
     * @return request uuid.
     */
    @NotNull
    public String getRequestId() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return version of the SDK.
     */
    @NotNull
    public String getVersion() {
        return pubnubImpl.getVersion();
    }

    /**
     * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
     */
    public void destroy() {
        pubnubImpl.destroy();
    }

    /**
     * Force destroy the SDK to evict the connection pools and close executors.
     */
    public void forceDestroy() {
        pubnubImpl.forceDestroy();
    }

    /**
     * Perform a Reconnect to the network
     */
    public void reconnect() {
        pubnubImpl.reconnect(0L);
    }

    /**
     * Perform a disconnect from the listeners
     */
    public void disconnect() {
        pubnubImpl.disconnect();
    }

    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @NotNull
    public List<String> getSubscribedChannels() {
        return pubnubImpl.getSubscribedChannels();
    }

    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return pubnubImpl.getSubscribedChannelGroups();
    }

    public void unsubscribeAll() {
        pubnubImpl.unsubscribeAll();
    }

    public PNToken parseToken(String token) throws PubNubException {
        return pubnubImpl.parseToken(token);
    }

    public void setToken(String token) {
        pubnubImpl.setToken(token);
    }

    public int getTimestamp() {
        return pubnubImpl.timestamp$core();
    }

}

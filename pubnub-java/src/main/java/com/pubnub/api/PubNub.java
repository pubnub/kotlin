package com.pubnub.api;

import com.pubnub.api.builder.PresenceBuilder;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
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
import com.pubnub.internal.BasePubNub;
import com.pubnub.internal.v2.subscription.BaseSubscriptionImpl;
import com.pubnub.internal.v2.subscription.BaseSubscriptionSetImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class PubNub extends BasePubNub {

    private final PNConfiguration configuration;

    public PubNub(@NotNull PNConfiguration configuration) {
        super(configuration.getPnConfiguration(), BaseSubscriptionImpl::new, BaseSubscriptionSetImpl::new);
        this.configuration = configuration;
    }

    @NotNull
    public PNConfiguration getConfiguration() {
        return configuration;
    }

    @NotNull
    public SubscribeBuilder subscribe() {
        return new SubscribeBuilder(getPubNubImpl());
    }

    @NotNull
    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(getPubNubImpl());
    }

    @NotNull
    public PresenceBuilder presence() {
        return new PresenceBuilder(getPubNubImpl());
    }

    // start push

    @NotNull
    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPush(getPubNubImpl());
    }

    @NotNull
    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPush(getPubNubImpl());
    }

    @NotNull
    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDevice(getPubNubImpl());
    }

    @NotNull
    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisions(getPubNubImpl());
    }

    // end push

    @NotNull
    public WhereNow whereNow() {
        return new WhereNow(getPubNubImpl());
    }

    @NotNull
    public HereNow hereNow() {
        return new HereNow(getPubNubImpl());
    }

    @NotNull
    public Time time() {
        return new Time(getPubNubImpl());
    }

    @NotNull
    public History history() {
        return new History(getPubNubImpl());
    }

    @NotNull
    public FetchMessages fetchMessages() {
        return new FetchMessages(getPubNubImpl());
    }

    @NotNull
    public DeleteMessages deleteMessages() {
        return new DeleteMessages(getPubNubImpl());
    }

    @NotNull
    public MessageCounts messageCounts() {
        return new MessageCounts(getPubNubImpl());
    }

    @NotNull
    public Grant grant() {
        return new Grant(getPubNubImpl());
    }

    /**
     * @deprecated Use {@link #grantToken(Integer)} instead.
     */
    @NotNull
    public GrantTokenBuilder grantToken() {
        return new GrantTokenBuilder(getPubNubImpl(), new GrantToken(getPubNubImpl()));
    }

    @NotNull
    @SuppressWarnings("deprecation")
    public GrantTokenBuilder grantToken(Integer ttl) {
        return new GrantTokenBuilder(getPubNubImpl(), new GrantToken(getPubNubImpl()).ttl(ttl));
    }

    @NotNull
    public RevokeToken revokeToken() {
        return new RevokeToken(getPubNubImpl());
    }

    @NotNull
    public GetState getPresenceState() {
        return new GetState(getPubNubImpl());
    }

    @NotNull
    public SetState setPresenceState() {
        return new SetState(getPubNubImpl());
    }

    @NotNull
    public Publish publish() {
        return new Publish(getPubNubImpl());
    }

    @NotNull
    public Signal signal() {
        return new Signal(getPubNubImpl());
    }

    @NotNull
    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(getPubNubImpl());
    }

    @NotNull
    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(getPubNubImpl());
    }

    @NotNull
    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(getPubNubImpl());
    }

    @NotNull
    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(getPubNubImpl());
    }

    @NotNull
    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(getPubNubImpl());
    }

    // Start Objects API

    public SetUUIDMetadata setUUIDMetadata() {
        return new SetUUIDMetadata(getPubNubImpl());
    }

    @NotNull
    public GetAllUUIDMetadata getAllUUIDMetadata() {
        return new GetAllUUIDMetadata(getPubNubImpl());
    }

    @NotNull
    public GetUUIDMetadata getUUIDMetadata() {
        return new GetUUIDMetadata(getPubNubImpl());
    }

    @NotNull
    public RemoveUUIDMetadata removeUUIDMetadata() {
        return new RemoveUUIDMetadata(getPubNubImpl());
    }

    public SetChannelMetadata.Builder setChannelMetadata() {
        return SetChannelMetadata.builder(getPubNubImpl());
    }

    @NotNull
    public GetAllChannelsMetadata getAllChannelsMetadata() {
        return new GetAllChannelsMetadata(getPubNubImpl());
    }

    @NotNull
    public GetChannelMetadata.Builder getChannelMetadata() {
        return GetChannelMetadata.builder(getPubNubImpl());
    }

    public RemoveChannelMetadata.Builder removeChannelMetadata() {
        return RemoveChannelMetadata.builder(getPubNubImpl());
    }

    @NotNull
    public GetMemberships getMemberships() {
        return new GetMemberships(getPubNubImpl());
    }

    @NotNull
    public SetMemberships.Builder setMemberships() {
        return SetMemberships.builder(getPubNubImpl());
    }

    public RemoveMemberships.Builder removeMemberships() {
        return RemoveMemberships.builder(getPubNubImpl());
    }

    public ManageMemberships.Builder manageMemberships() {
        return ManageMemberships.builder(getPubNubImpl());
    }

    public GetChannelMembers.Builder getChannelMembers() {
        return GetChannelMembers.builder(getPubNubImpl());
    }

    public SetChannelMembers.Builder setChannelMembers() {
        return SetChannelMembers.builder(getPubNubImpl());
    }

    public RemoveChannelMembers.Builder removeChannelMembers() {
        return RemoveChannelMembers.builder(getPubNubImpl());
    }

    public ManageChannelMembers.Builder manageChannelMembers() {
        return ManageChannelMembers.builder(getPubNubImpl());
    }

    // End Objects API

    // Start Message Actions API

    @NotNull
    public AddMessageAction addMessageAction() {
        return new AddMessageAction(getPubNubImpl());
    }

    @NotNull
    public GetMessageActions getMessageActions() {
        return new GetMessageActions(getPubNubImpl());
    }

    @NotNull
    public RemoveMessageAction removeMessageAction() {
        return new RemoveMessageAction(getPubNubImpl());
    }

    // End Message Actions API

    public SendFile.Builder sendFile() {
        return SendFile.builder(getPubNubImpl());
    }

    public ListFiles.Builder listFiles() {
        return new ListFiles.Builder(getPubNubImpl());
    }

    public GetFileUrl.Builder getFileUrl() {
        return GetFileUrl.builder(getPubNubImpl());
    }

    public DownloadFile.Builder downloadFile() {
        return DownloadFile.builder(getPubNubImpl());
    }

    public DeleteFile.Builder deleteFile() {
        return DeleteFile.builder(getPubNubImpl());
    }

    public PublishFileMessage.Builder publishFileMessage() {
        return PublishFileMessage.builder(getPubNubImpl());
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
        return decrypt(inputString, getPubNubImpl().getConfiguration().getCipherKey());
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
        return getPubNubImpl().decrypt(inputString, cipherKey);
    }

    public InputStream decryptInputStream(InputStream inputStream) throws PubNubException {
        return decryptInputStream(inputStream, getPubNubImpl().getConfiguration().getCipherKey());
    }

    public InputStream decryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getPubNubImpl().decryptInputStream(inputStream, cipherKey);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    @Nullable
    public String encrypt(String inputString) throws PubNubException {
        return encrypt(inputString, getPubNubImpl().getConfiguration().getCipherKey());
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
        return getPubNubImpl().encrypt(inputString, cipherKey);
    }

    public InputStream encryptInputStream(InputStream inputStream) throws PubNubException {
        return encryptInputStream(inputStream, getPubNubImpl().getConfiguration().getCipherKey());
    }

    public InputStream encryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException {
        return getPubNubImpl().encryptInputStream(inputStream, cipherKey);
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
        getPubNubImpl().reconnect(0L);
    }

    /**
     * Perform a disconnect from the listeners
     */
    public void disconnect() {
        getPubNubImpl().disconnect();
    }

    @NotNull
    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    @NotNull
    public List<String> getSubscribedChannels() {
        return getPubNubImpl().getSubscribedChannels();
    }

    @NotNull
    public List<String> getSubscribedChannelGroups() {
        return getPubNubImpl().getSubscribedChannelGroups();
    }

    public void unsubscribeAll() {
        getPubNubImpl().unsubscribeAll();
    }

    public PNToken parseToken(String token) throws PubNubException {
        return getPubNubImpl().parseToken(token);
    }

    public void setToken(String token) {
        getPubNubImpl().setToken(token);
    }

}

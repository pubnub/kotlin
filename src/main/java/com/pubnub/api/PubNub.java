package com.pubnub.api;

import com.pubnub.api.builder.PresenceBuilder;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.DeleteMessages;
import com.pubnub.api.endpoints.FetchMessages;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.endpoints.MessageCounts;
import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.access.Audit;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.access.GrantToken;
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup;
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup;
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup;
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup;
import com.pubnub.api.endpoints.objects_api.members.GetMembers;
import com.pubnub.api.endpoints.objects_api.members.ManageMembers;
import com.pubnub.api.endpoints.objects_api.memberships.GetMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.ManageMemberships;
import com.pubnub.api.endpoints.objects_api.spaces.CreateSpace;
import com.pubnub.api.endpoints.objects_api.spaces.DeleteSpace;
import com.pubnub.api.endpoints.objects_api.spaces.GetSpace;
import com.pubnub.api.endpoints.objects_api.spaces.GetSpaces;
import com.pubnub.api.endpoints.objects_api.spaces.UpdateSpace;
import com.pubnub.api.endpoints.objects_api.users.CreateUser;
import com.pubnub.api.endpoints.objects_api.users.DeleteUser;
import com.pubnub.api.endpoints.objects_api.users.GetUser;
import com.pubnub.api.endpoints.objects_api.users.GetUsers;
import com.pubnub.api.endpoints.objects_api.users.UpdateUser;
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
import com.pubnub.api.managers.BasePathManager;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.managers.PublishSequenceManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.SubscriptionManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.PNResourceType;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.managers.token_manager.TokenManagerProperties;
import com.pubnub.api.vendor.Crypto;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class PubNub {

    @Getter
    private PNConfiguration configuration;

    @Getter
    private MapperManager mapper;

    private String instanceId;

    private SubscriptionManager subscriptionManager;

    private BasePathManager basePathManager;

    private PublishSequenceManager publishSequenceManager;

    private TelemetryManager telemetryManager;

    private RetrofitManager retrofitManager;

    private TokenManager tokenManager;

    private static final int TIMESTAMP_DIVIDER = 1000;
    private static final int MAX_SEQUENCE = 65535;

    private static final String SDK_VERSION = "4.28.0";

    public PubNub(PNConfiguration initialConfig) {
        this.configuration = initialConfig;
        this.mapper = new MapperManager();
        this.telemetryManager = new TelemetryManager();
        this.basePathManager = new BasePathManager(initialConfig);
        this.retrofitManager = new RetrofitManager(this);
        this.tokenManager = new TokenManager();
        this.subscriptionManager = new SubscriptionManager(this, retrofitManager, this.telemetryManager);
        this.publishSequenceManager = new PublishSequenceManager(MAX_SEQUENCE);
        instanceId = UUID.randomUUID().toString();
    }

    public String getBaseUrl() {
        return this.basePathManager.getBasePath();
    }


    //
    public void addListener(SubscribeCallback listener) {
        subscriptionManager.addListener(listener);
    }

    public void removeListener(SubscribeCallback listener) {
        subscriptionManager.removeListener(listener);
    }

    public SubscribeBuilder subscribe() {
        return new SubscribeBuilder(this.subscriptionManager);
    }

    public UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(this.subscriptionManager);
    }

    public PresenceBuilder presence() {
        return new PresenceBuilder(this.subscriptionManager);
    }

    // start push

    public AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPush(this, this.telemetryManager, this.retrofitManager);
    }

    public RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPush(this, this.telemetryManager, this.retrofitManager);
    }

    public RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDevice(this, this.telemetryManager, this.retrofitManager);
    }

    public ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisions(this, this.telemetryManager, this.retrofitManager);
    }

    // end push

    public WhereNow whereNow() {
        return new WhereNow(this, this.telemetryManager, this.retrofitManager);
    }

    public HereNow hereNow() {
        return new HereNow(this, this.telemetryManager, this.retrofitManager);
    }

    public Time time() {
        return new Time(this, this.telemetryManager, this.retrofitManager);
    }

    public History history() {
        return new History(this, this.telemetryManager, this.retrofitManager);
    }

    public FetchMessages fetchMessages() {
        return new FetchMessages(this, this.telemetryManager, this.retrofitManager);
    }

    public DeleteMessages deleteMessages() {
        return new DeleteMessages(this, this.telemetryManager, this.retrofitManager);
    }

    public MessageCounts messageCounts() {
        return new MessageCounts(this, this.telemetryManager, this.retrofitManager);
    }

    public Audit audit() {
        return new Audit(this, this.telemetryManager, this.retrofitManager);
    }

    /**
     * @deprecated This method will soon be obsoleted.
     * <p> Use {@link PubNub#grantToken()} instead.
     */
    @Deprecated
    public Grant grant() {
        return new Grant(this, this.telemetryManager, this.retrofitManager);
    }

    public GrantToken grantToken() {
        return new GrantToken(this, this.telemetryManager, this.retrofitManager);
    }

    public GetState getPresenceState() {
        return new GetState(this, this.telemetryManager, this.retrofitManager);
    }

    public SetState setPresenceState() {
        return new SetState(this, subscriptionManager, this.telemetryManager, this.retrofitManager);
    }

    public Publish publish() {
        return new Publish(this, publishSequenceManager, this.telemetryManager, this.retrofitManager);
    }

    public Signal signal() {
        return new Signal(this, this.telemetryManager, this.retrofitManager);
    }

    public ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(this, this.telemetryManager, this.retrofitManager);
    }

    public AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(this, this.telemetryManager, this.retrofitManager);
    }

    public AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(this, this.telemetryManager, this.retrofitManager);
    }

    public RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(this, this.telemetryManager, this.retrofitManager);
    }

    public DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(this, this.telemetryManager, this.retrofitManager);
    }

    public GetUsers getUsers() {
        return new GetUsers(this, this.telemetryManager, this.retrofitManager);
    }

    public GetUser getUser() {
        return new GetUser(this, this.telemetryManager, this.retrofitManager);
    }

    public CreateUser createUser() {
        return new CreateUser(this, this.telemetryManager, this.retrofitManager);
    }

    public UpdateUser updateUser() {
        return new UpdateUser(this, this.telemetryManager, this.retrofitManager);
    }

    public DeleteUser deleteUser() {
        return new DeleteUser(this, this.telemetryManager, this.retrofitManager);
    }

    public GetSpaces getSpaces() {
        return new GetSpaces(this, this.telemetryManager, this.retrofitManager);
    }

    public GetSpace getSpace() {
        return new GetSpace(this, this.telemetryManager, this.retrofitManager);
    }

    public CreateSpace createSpace() {
        return new CreateSpace(this, this.telemetryManager, this.retrofitManager);
    }

    public UpdateSpace updateSpace() {
        return new UpdateSpace(this, this.telemetryManager, this.retrofitManager);
    }

    public DeleteSpace deleteSpace() {
        return new DeleteSpace(this, this.telemetryManager, this.retrofitManager);
    }

    public GetMemberships getMemberships() {
        return new GetMemberships(this, this.telemetryManager, this.retrofitManager);
    }

    public GetMembers getMembers() {
        return new GetMembers(this, this.telemetryManager, this.retrofitManager);
    }

    public ManageMemberships manageMemberships() {
        return new ManageMemberships(this, this.telemetryManager, this.retrofitManager);
    }

    public ManageMembers manageMembers() {
        return new ManageMembers(this, this.telemetryManager, this.retrofitManager);
    }

    // public methods

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    public String decrypt(String inputString) throws PubNubException {
        if (inputString == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS).build();
        }

        return decrypt(inputString, this.getConfiguration().getCipherKey());
    }

    /**
     * Perform Cryptographic decryption of an input string using the cipher key
     *
     * @param inputString String to be encrypted
     * @param cipherKey   cipher key to be used for encryption
     * @return String containing the encryption of inputString using cipherKey
     * @throws PubNubException throws exception in case of failed encryption
     */
    public String decrypt(String inputString, String cipherKey) throws PubNubException {
        if (inputString == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS).build();
        }

        return new Crypto(cipherKey).decrypt(inputString);
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    public String encrypt(String inputString) throws PubNubException {
        if (inputString == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS).build();
        }

        return encrypt(inputString, this.getConfiguration().getCipherKey());
    }

    /**
     * Perform Cryptographic encryption of an input string and the cipher key.
     *
     * @param inputString String to be encrypted
     * @param cipherKey   cipher key to be used for encryption
     * @return String containing the encryption of inputString using cipherKey
     * @throws PubNubException throws exception in case of failed encryption
     */
    public String encrypt(String inputString, String cipherKey) throws PubNubException {
        if (inputString == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS).build();
        }

        return new Crypto(cipherKey).encrypt(inputString);
    }

    public int getTimestamp() {
        return (int) ((new Date().getTime()) / TIMESTAMP_DIVIDER);
    }

    /**
     * @return instance uuid.
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @return request uuid.
     */
    public String getRequestId() {
        return UUID.randomUUID().toString();
    }

    /**
     * @return version of the SDK.
     */
    public String getVersion() {
        return SDK_VERSION;
    }

    /**
     * Stop the SDK and terminate all listeners.
     */
    @Deprecated
    public void stop() {
        subscriptionManager.stop();
    }

    /**
     * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
     */
    public void destroy() {
        try {
            subscriptionManager.destroy(false);
            retrofitManager.destroy(false);
        } catch (Exception error) {
            //
        }
    }

    /**
     * Force destroy the SDK to evict the connection pools and close executors.
     */
    public void forceDestroy() {
        try {
            subscriptionManager.destroy(true);
            retrofitManager.destroy(true);
            telemetryManager.stopCleanUpTimer();
        } catch (Exception error) {
            //
        }
    }

    /**
     * Perform a Reconnect to the network
     */
    public void reconnect() {
        subscriptionManager.reconnect();
    }

    /**
     * Perform a disconnect from the listeners
     */
    public void disconnect() {
        subscriptionManager.disconnect();
    }

    public Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    public List<String> getSubscribedChannels() {
        return subscriptionManager.getSubscribedChannels();
    }

    public List<String> getSubscribedChannelGroups() {
        return subscriptionManager.getSubscribedChannelGroups();
    }

    public void unsubscribeAll() {
        subscriptionManager.unsubscribeAll();
    }

    public void setToken(String token) throws PubNubException {
        tokenManager.setToken(token);
    }

    public void setTokens(List<String> tokens) throws PubNubException {
        tokenManager.setTokens(tokens);
    }

    public String getToken(String resourceId, PNResourceType resourceType) {
        return tokenManager.getToken(TokenManagerProperties.builder()
                .resourceId(resourceId)
                .pnResourceType(resourceType)
                .build());
    }

    public String getToken(TokenManagerProperties tokenManagerProperties) {
        return tokenManager.getToken(tokenManagerProperties);
    }

    public HashMap<String, HashMap<String, String>> getTokensByResource(PNResourceType resourceType) {
        return tokenManager.getTokensByResource(resourceType);
    }
}

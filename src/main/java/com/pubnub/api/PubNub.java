package com.pubnub.api;

import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.access.Audit;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup;
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup;
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup;
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup;
import com.pubnub.api.endpoints.presence.GetState;
import com.pubnub.api.endpoints.presence.HereNow;
import com.pubnub.api.endpoints.presence.SetState;
import com.pubnub.api.endpoints.presence.WhereNow;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.endpoints.push.AddChannelsToPush;
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush;
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice;
import com.pubnub.api.endpoints.push.ListPushProvisions;
import com.pubnub.api.managers.BasePathManager;
import com.pubnub.api.managers.PublishSequenceManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.SubscriptionManager;
import com.pubnub.api.vendor.Crypto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;


@Getter
@Slf4j
public class PubNub {

    private PNConfiguration configuration;

    @Getter(AccessLevel.NONE)
    private SubscriptionManager subscriptionManager;
    @Getter(AccessLevel.NONE)
    private BasePathManager basePathManager;
    @Getter(AccessLevel.NONE)
    private PublishSequenceManager publishSequenceManager;
    @Getter(AccessLevel.NONE)
    private RetrofitManager retrofitManager;


    private static final int TIMESTAMP_DIVIDER = 1000;
    private static final int MAX_SEQUENCE = 65535;

    private static final String SDK_VERSION = "4.0.9";

    public PubNub(final PNConfiguration initialConfig) {
        this.configuration = initialConfig;
        this.basePathManager = new BasePathManager(initialConfig);
        this.retrofitManager = new RetrofitManager(this);
        this.subscriptionManager = new SubscriptionManager(this, retrofitManager);
        this.publishSequenceManager = new PublishSequenceManager(MAX_SEQUENCE);
    }

    public String getBaseUrl() {
        return this.basePathManager.getBasePath();
    }


    //
    public final void addListener(SubscribeCallback listener) {
        subscriptionManager.addListener(listener);
    }

    public final void removeListener(SubscribeCallback listener) {
        subscriptionManager.removeListener(listener);
    }

    public final SubscribeBuilder subscribe() {
        return new SubscribeBuilder(this.subscriptionManager);
    }

    public final UnsubscribeBuilder unsubscribe() {
        return new UnsubscribeBuilder(this.subscriptionManager);
    }

    // start push

    public final AddChannelsToPush addPushNotificationsOnChannels() {
        return new AddChannelsToPush(this, this.retrofitManager.getTransactionInstance());
    }

    public final RemoveChannelsFromPush removePushNotificationsFromChannels() {
        return new RemoveChannelsFromPush(this, this.retrofitManager.getTransactionInstance());
    }

    public final RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken() {
        return new RemoveAllPushChannelsForDevice(this, this.retrofitManager.getTransactionInstance());
    }

    public final ListPushProvisions auditPushChannelProvisions() {
        return new ListPushProvisions(this, this.retrofitManager.getTransactionInstance());
    }

    // end push

    public final WhereNow whereNow() {
        return new WhereNow(this, this.retrofitManager.getTransactionInstance());
    }

    public final HereNow hereNow() {
        return new HereNow(this, this.retrofitManager.getTransactionInstance());
    }

    public final Time time() {
        return new Time(this, this.retrofitManager.getTransactionInstance());
    }

    public final History history() {
        return new History(this, this.retrofitManager.getTransactionInstance());
    }


    public final Audit audit() {
        return new Audit(this, this.retrofitManager.getTransactionInstance());
    }

    public final Grant grant() {
        return new Grant(this, this.retrofitManager.getTransactionInstance());
    }

    public final GetState getPresenceState() {
        return new GetState(this, this.retrofitManager.getTransactionInstance());
    }

    public final SetState setPresenceState() {
        return new SetState(this, subscriptionManager, this.retrofitManager.getTransactionInstance());
    }

    public final Publish publish() {
        return new Publish(this, publishSequenceManager, this.retrofitManager.getTransactionInstance());
    }

    public final ListAllChannelGroup listAllChannelGroups() {
        return new ListAllChannelGroup(this, this.retrofitManager.getTransactionInstance());
    }

    public final AllChannelsChannelGroup listChannelsForChannelGroup() {
        return new AllChannelsChannelGroup(this, this.retrofitManager.getTransactionInstance());
    }

    public final AddChannelChannelGroup addChannelsToChannelGroup() {
        return new AddChannelChannelGroup(this, this.retrofitManager.getTransactionInstance());
    }

    public final RemoveChannelChannelGroup removeChannelsFromChannelGroup() {
        return new RemoveChannelChannelGroup(this, this.retrofitManager.getTransactionInstance());
    }

    public final DeleteChannelGroup deleteChannelGroup() {
        return new DeleteChannelGroup(this, this.retrofitManager.getTransactionInstance());
    }

    // public methods

    /**
     * Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration
     *
     * @param inputString String to be encrypted
     * @return String containing the encryption of inputString using cipherKey
     */
    public final String decrypt(String inputString) throws PubNubException {
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
    public final String decrypt(final String inputString, final String cipherKey) throws PubNubException {
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
    public final String encrypt(final String inputString) throws PubNubException {
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
    public final String encrypt(final String inputString, final String cipherKey) throws PubNubException {
        if (inputString == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS).build();
        }

        return new Crypto(cipherKey).encrypt(inputString);
    }

    public int getTimestamp() {
        return (int) ((new Date().getTime()) / TIMESTAMP_DIVIDER);
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
    public final void stop() {
        subscriptionManager.stop();
    }

    /**
     * Perform a Reconnect to the network
     */
    public final void reconnect() {
        subscriptionManager.reconnect();
    }

    public final Publish fire() {
        return publish().shouldStore(false).replicate(false);
    }

    public final List<String> getSubscribedChannels() {
        return subscriptionManager.getSubscribedChannels();
    }

    public final List<String> getSubscribedChannelGroups() {
        return subscriptionManager.getSubscribedChannelGroups();
    }

    public final void unsubscribeAll() {
        subscriptionManager.unsubscribeAll();
    }
}

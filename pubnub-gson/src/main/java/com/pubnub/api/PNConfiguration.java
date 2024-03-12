package com.pubnub.api;


import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.retry.RetryConfiguration;
import kotlin.Pair;
import lombok.Setter;
import lombok.extern.java.Log;
import okhttp3.Authenticator;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionSpec;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Proxy;
import java.net.ProxySelector;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Log
@SuppressWarnings("UnusedReturnValue")
public class PNConfiguration {
    private final com.pubnub.internal.PNConfigurationCore pnConfigurationCore;

    public com.pubnub.internal.PNConfigurationCore getPnConfigurationCore() {
        return pnConfigurationCore;
    }

    private static final int MINIMUM_PRESENCE_TIMEOUT = 20;

    /**
     * Custom origin if needed.
     * <p>
     * Defaults to `ps.pndsn.com`
     */
    public String getOrigin() {
        return pnConfigurationCore.getOrigin();
    }

    /**
     * Custom origin if needed.
     * <p>
     * Defaults to `ps.pndsn.com`
     */
    public PNConfiguration setOrigin(@NotNull String s) {
        pnConfigurationCore.setOrigin(s);
        return this;
    }

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    public boolean getSecure() {
        return pnConfigurationCore.getSecure();
    }

    /**
     * If set to `true`,  requests will be made over HTTPS.
     *
     * Deafults to `true`.
     */
    public PNConfiguration setSecure(boolean b) {
        pnConfigurationCore.setSecure(b);
        return this;
    }

    /**
     * Set to {@link PNLogVerbosity#BODY} to enable logging of network traffic, otherwise se to {@link PNLogVerbosity#NONE}.
     */
    public PNLogVerbosity getLogVerbosity() {
        return pnConfigurationCore.getLogVerbosity();
    }

    /**
     * Set to {@link PNLogVerbosity#BODY} to enable logging of network traffic, otherwise se to {@link PNLogVerbosity#NONE}.
     */
    public PNConfiguration setLogVerbosity(@NotNull PNLogVerbosity pnLogVerbosity) {
        pnConfigurationCore.setLogVerbosity(pnLogVerbosity);
        return this;
    }

    /**
     * Set Heartbeat notification options.
     * <p>
     * By default, the SDK alerts on failed heartbeats (equivalent to {@link PNHeartbeatNotificationOptions#FAILURES}).
     */
    public PNHeartbeatNotificationOptions getHeartbeatNotificationOptions() {
        return pnConfigurationCore.getHeartbeatNotificationOptions();
    }

    /**
     * Set Heartbeat notification options.
     * <p>
     * By default, the SDK alerts on failed heartbeats (equivalent to {@link PNHeartbeatNotificationOptions#FAILURES}).
     */
    public PNConfiguration setHeartbeatNotificationOptions(@NotNull PNHeartbeatNotificationOptions pnHeartbeatNotificationOptions) {
        pnConfigurationCore.setHeartbeatNotificationOptions(pnHeartbeatNotificationOptions);
        return this;
    }

    /**
     * Set to [PNReconnectionPolicy.LINEAR] for automatic reconnects.
     * <p>
     * Use [PNReconnectionPolicy.NONE] to disable automatic reconnects.
     * <p>
     * Use [PNReconnectionPolicy.EXPONENTIAL] to set exponential retry interval.
     * <p>
     * Defaults to [PNReconnectionPolicy.NONE].
     *
     * @deprecated use {@link #setRetryConfiguration(RetryConfiguration)} instead
     */
    @Deprecated
    public PNReconnectionPolicy getReconnectionPolicy() {
        return pnConfigurationCore.getReconnectionPolicy();
    }

    /**
     * Set to [PNReconnectionPolicy.LINEAR] for automatic reconnects.
     * <p>
     * Use [PNReconnectionPolicy.NONE] to disable automatic reconnects.
     * <p>
     * Use [PNReconnectionPolicy.EXPONENTIAL] to set exponential retry interval.
     * <p>
     * Defaults to [PNReconnectionPolicy.NONE].
     *
     * @deprecated use {@link #setRetryConfiguration(RetryConfiguration)} instead
     */
    @Deprecated
    public PNConfiguration setReconnectionPolicy(@NotNull PNReconnectionPolicy pnReconnectionPolicy) {
        pnConfigurationCore.setReconnectionPolicy(pnReconnectionPolicy);
        return this;
    }

    /**
     * Sets the custom presence server timeout.
     * <p>
     * The value is in seconds, and the minimum value is 20 seconds.
     */
    public int getPresenceTimeout() {
        return pnConfigurationCore.getPresenceTimeout();
    }

    /**
     * @deprecated Use {@link #setUserId(UserId)} instead.
     */
    @Deprecated
    public PNConfiguration setUuid(@NotNull String uuid) {
        pnConfigurationCore.setUuid(uuid);
        return this;
    }

    /**
     * The subscribe key from the admin panel.
     */
    public String getSubscribeKey() {
        return pnConfigurationCore.getSubscribeKey();
    }

    /**
     * The subscribe key from the admin panel.
     */
    public PNConfiguration setSubscribeKey(@NotNull String s) {
        pnConfigurationCore.setSubscribeKey(s);
        return this;
    }

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    public String getPublishKey() {
        return pnConfigurationCore.getPublishKey();
    }

    /**
     * The publish key from the admin panel (only required if publishing).
     */
    public PNConfiguration setPublishKey(@NotNull String s) {
        pnConfigurationCore.setPublishKey(s);
        return this;
    }

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     * <p>
     * Keep away from Android.
     */
    public String getSecretKey() {
        return pnConfigurationCore.getSecretKey();
    }

    /**
     * The secret key from the admin panel (only required for modifying/revealing access permissions).
     * <p>
     * Keep away from Android.
     */
    public PNConfiguration setSecretKey(@NotNull String s) {
        pnConfigurationCore.setSecretKey(s);
        return this;
    }

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    public String getAuthKey() {
        return pnConfigurationCore.getAuthKey();
    }

    /**
     * If Access Manager is utilized, client will use this authKey in all restricted requests.
     */
    public PNConfiguration setAuthKey(@NotNull String s) {
        pnConfigurationCore.setAuthKey(s);
        return this;
    }

    /**
     * If set, all communications to and from PubNub will be encrypted.
     *
     * @deprecated Instead of `cipherKey` and `useRandomInitializationVector` use {@link #setCryptoModule(CryptoModule)} instead
     */
    @Deprecated
    public String getCipherKey() {
        return pnConfigurationCore.getCipherKey();
    }

    /**
     * If set, all communications to and from PubNub will be encrypted.
     *
     * @deprecated Instead of `cipherKey` and `useRandomInitializationVector` use {@link #setCryptoModule(CryptoModule)} instead
     */
    @Deprecated
    public PNConfiguration setCipherKey(String s) {
        pnConfigurationCore.setCipherKey(s != null ? s : "");
        return this;
    }

    /**
     * Should initialization vector for encrypted messages be random.
     * <p>
     * Defaults to `false`.
     *
     * @deprecated Instead of `cipherKey` and `useRandomInitializationVector` use {@link #setCryptoModule(CryptoModule)} instead
     */
    @Deprecated
    public boolean getUseRandomInitializationVector() {
        return pnConfigurationCore.getUseRandomInitializationVector();
    }

    /**
     * Should initialization vector for encrypted messages be random.
     * <p>
     * Defaults to `false`.
     *
     * @deprecated Instead of `cipherKey` and `useRandomInitializationVector` use {@link #setCryptoModule(CryptoModule)} instead
     */
    @Deprecated
    public PNConfiguration setUseRandomInitializationVector(boolean b) {
        pnConfigurationCore.setUseRandomInitializationVector(b);
        return this;
    }

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    public CryptoModule getCryptoModule() {
        return pnConfigurationCore.getCryptoModule();
    }

    /**
     * CryptoModule is responsible for handling encryption and decryption.
     * If set, all communications to and from PubNub will be encrypted.
     */
    public PNConfiguration setCryptoModule(@Nullable CryptoModule cryptoModule) {
        pnConfigurationCore.setCryptoModule(cryptoModule);
        return this;
    }

    public UserId getUserId() {
        return pnConfigurationCore.getUserId();
    }

    public PNConfiguration setUserId(@NotNull UserId userId) {
        pnConfigurationCore.setUserId(userId);
        return this;
    }

    public String getUuid() {
        return pnConfigurationCore.getUuid();
    }

    /**
     * Enables explicit presence control.
     * When set to true heartbeat calls will contain only channels and groups added explicitly
     * using {@link PubNub#presence()}. Should be used only with ACL set on the server side.
     * For more information please contact PubNub support
     *
     * @see PubNub#presence()
     * @see PNConfiguration#setHeartbeatInterval(int)
     */
    @Deprecated
    @Setter
    private boolean managePresenceListManually; // TODO what to do with this?

    /**
     * Initialize the PNConfiguration with default values
     *
     * @param userId
     */
    public PNConfiguration(@NotNull UserId userId) throws PubNubException {
        pnConfigurationCore = new com.pubnub.internal.PNConfigurationCore(userId);
        managePresenceListManually = false;
    }

    /**
     * Initialize the PNConfiguration with default values
     *
     * @param uuid
     * @deprecated Use {@link com.pubnub.internal.PNConfigurationCore (UserId)} instead.
     */
    @Deprecated
    public PNConfiguration(@NotNull String uuid) throws PubNubException {
        this(new UserId(uuid));
    }

    /**
     * set presence configurations for timeout and announce interval.
     *
     * @param timeout  presence timeout; how long before the server considers this client to be gone.
     * @param interval presence announce interval, how often the client should announce itself.
     * @return returns itself.
     */
    public PNConfiguration setPresenceTimeoutWithCustomInterval(int timeout, int interval) {
        timeout = validatePresenceTimeout(timeout);
        pnConfigurationCore.setPresenceTimeout(timeout);
        pnConfigurationCore.setHeartbeatInterval(interval);

        return this;
    }

    /**
     * Set presence configurations for timeout and allow the client to pick the best presence interval
     *
     * @param timeout presence timeout; how long before the server considers this client to be gone.
     * @return returns itself.
     */
    public PNConfiguration setPresenceTimeout(int timeout) {
        timeout = validatePresenceTimeout(timeout);
        pnConfigurationCore.setPresenceTimeout(timeout);
        return this;
    }

    /**
     * How often the client will announce itself to server.
     * <p>
     * The value is in seconds.
     */
    public int getHeartbeatInterval() {
        return pnConfigurationCore.getHeartbeatInterval();
    }

    /**
     * How often the client will announce itself to server.
     * <p>
     * The value is in seconds.
     */
    public void setHeartbeatInterval(int i) {
        pnConfigurationCore.setHeartbeatInterval(i);
    }

    /**
     * The subscribe request timeout.
     * <p>
     * The value is in seconds.
     * <p>
     * Defaults to 310.
     */
    public int getSubscribeTimeout() {
        return pnConfigurationCore.getSubscribeTimeout();
    }

    /**
     * The subscribe request timeout.
     * <p>
     * The value is in seconds.
     * <p>
     * Defaults to 310.
     */
    public PNConfiguration setSubscribeTimeout(int i) {
        pnConfigurationCore.setSubscribeTimeout(i);
        return this;
    }

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     * <p>
     * The value is in seconds.
     * <p>
     * Defaults to 5.
     */
    public int getConnectTimeout() {
        return pnConfigurationCore.getConnectTimeout();
    }

    /**
     * How long before the client gives up trying to connect with a subscribe call.
     * <p>
     * The value is in seconds.
     * <p>
     * Defaults to 5.
     */
    public PNConfiguration setConnectTimeout(int i) {
        pnConfigurationCore.setConnectTimeout(i);
        return this;
    }

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     * <p>
     * The value is in seconds.
     * <p>
     * Defaults to 10.
     */
    public int getNonSubscribeRequestTimeout() {
        return pnConfigurationCore.getNonSubscribeRequestTimeout();
    }

    /**
     * For non subscribe operations (publish, herenow, etc),
     * how long to wait to connect to PubNub before giving up with a connection timeout error.
     * <p>
     * The value is in seconds.
     * <p>
     * Defaults to 10.
     */
    public PNConfiguration setNonSubscribeRequestTimeout(int i) {
        pnConfigurationCore.setNonSubscribeRequestTimeout(i);
        return this;
    }

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     * <p>
     * Defaults to `false`.
     */
    public boolean getCacheBusting() {
        return pnConfigurationCore.getCacheBusting();
    }

    /**
     * If operating behind a misbehaving proxy, allow the client to shuffle the subdomains.
     * <p>
     * Defaults to `false`.
     */
    public PNConfiguration setCacheBusting(boolean b) {
        pnConfigurationCore.setCacheBusting(b);
        return this;
    }

    public boolean getSuppressLeaveEvents() {
        return pnConfigurationCore.getSuppressLeaveEvents();
    }

    public PNConfiguration setSuppressLeaveEvents(boolean b) {
        pnConfigurationCore.setSuppressLeaveEvents(b);
        return this;
    }

    public boolean getMaintainPresenceState() {
        return pnConfigurationCore.getMaintainPresenceState();
    }

    public PNConfiguration setMaintainPresenceState(boolean b) {
        pnConfigurationCore.setMaintainPresenceState(b);
        return this;
    }

    public String getFilterExpression() {
        return pnConfigurationCore.getFilterExpression();
    }

    public PNConfiguration setFilterExpression(@NotNull String s) {
        pnConfigurationCore.setFilterExpression(s);
        return this;
    }

    public boolean getIncludeInstanceIdentifier() {
        return pnConfigurationCore.getIncludeInstanceIdentifier();
    }

    public PNConfiguration setIncludeInstanceIdentifier(boolean b) {
        pnConfigurationCore.setIncludeInstanceIdentifier(b);
        return this;
    }

    public boolean getIncludeRequestIdentifier() {
        return pnConfigurationCore.getIncludeRequestIdentifier();
    }

    public PNConfiguration setIncludeRequestIdentifier(boolean b) {
        pnConfigurationCore.setIncludeRequestIdentifier(b);
        return this;
    }

    public int getMaximumReconnectionRetries() {
        return pnConfigurationCore.getMaximumReconnectionRetries();
    }

    public PNConfiguration setMaximumReconnectionRetries(int i) {
        pnConfigurationCore.setMaximumReconnectionRetries(i);
        return this;
    }

    public Integer getMaximumConnections() {
        return pnConfigurationCore.getMaximumConnections();
    }

    public PNConfiguration setMaximumConnections(@Nullable Integer integer) {
        pnConfigurationCore.setMaximumConnections(integer);
        return this;
    }

    public boolean getGoogleAppEngineNetworking() {
        return pnConfigurationCore.getGoogleAppEngineNetworking();
    }

    public PNConfiguration setGoogleAppEngineNetworking(boolean b) {
        pnConfigurationCore.setGoogleAppEngineNetworking(b);
        return this;
    }

    public Proxy getProxy() {
        return pnConfigurationCore.getProxy();
    }

    public PNConfiguration setProxy(@Nullable Proxy proxy) {
        pnConfigurationCore.setProxy(proxy);
        return this;
    }

    public ProxySelector getProxySelector() {
        return pnConfigurationCore.getProxySelector();
    }

    public PNConfiguration setProxySelector(@Nullable ProxySelector proxySelector) {
        pnConfigurationCore.setProxySelector(proxySelector);
        return this;
    }

    public Authenticator getProxyAuthenticator() {
        return pnConfigurationCore.getProxyAuthenticator();
    }

    public PNConfiguration setProxyAuthenticator(@Nullable Authenticator authenticator) {
        pnConfigurationCore.setProxyAuthenticator(authenticator);
        return this;
    }

    public CertificatePinner getCertificatePinner() {
        return pnConfigurationCore.getCertificatePinner();
    }

    public PNConfiguration setCertificatePinner(@Nullable CertificatePinner certificatePinner) {
        pnConfigurationCore.setCertificatePinner(certificatePinner);
        return this;
    }

    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return pnConfigurationCore.getHttpLoggingInterceptor();
    }

    public PNConfiguration setHttpLoggingInterceptor(@Nullable HttpLoggingInterceptor httpLoggingInterceptor) {
        pnConfigurationCore.setHttpLoggingInterceptor(httpLoggingInterceptor);
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return pnConfigurationCore.getSslSocketFactory();
    }

    public PNConfiguration setSslSocketFactory(@Nullable SSLSocketFactory sslSocketFactory) {
        pnConfigurationCore.setSslSocketFactory(sslSocketFactory);
        return this;
    }

    public X509ExtendedTrustManager getX509ExtendedTrustManager() {
        return pnConfigurationCore.getX509ExtendedTrustManager();
    }

    public PNConfiguration setX509ExtendedTrustManager(@Nullable X509ExtendedTrustManager x509ExtendedTrustManager) {
        pnConfigurationCore.setX509ExtendedTrustManager(x509ExtendedTrustManager);
        return this;
    }

    public ConnectionSpec getConnectionSpec() {
        return pnConfigurationCore.getConnectionSpec();
    }

    public PNConfiguration setConnectionSpec(@Nullable ConnectionSpec connectionSpec) {
        pnConfigurationCore.setConnectionSpec(connectionSpec);
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return pnConfigurationCore.getHostnameVerifier();
    }

    public PNConfiguration setHostnameVerifier(@Nullable HostnameVerifier hostnameVerifier) {
        pnConfigurationCore.setHostnameVerifier(hostnameVerifier);
        return this;
    }

    public int getFileMessagePublishRetryLimit() {
        return pnConfigurationCore.getFileMessagePublishRetryLimit();
    }

    public PNConfiguration setFileMessagePublishRetryLimit(int i) {
        pnConfigurationCore.setFileMessagePublishRetryLimit(i);
        return this;
    }

    public boolean getDedupOnSubscribe() {
        return pnConfigurationCore.getDedupOnSubscribe();
    }

    public PNConfiguration setDedupOnSubscribe(boolean b) {
        pnConfigurationCore.setDedupOnSubscribe(b);
        return this;
    }

    public int getMaximumMessagesCacheSize() {
        return pnConfigurationCore.getMaximumMessagesCacheSize();
    }

    public PNConfiguration setMaximumMessagesCacheSize(int i) {
        pnConfigurationCore.setMaximumMessagesCacheSize(i);
        return this;
    }

    public ConcurrentMap<String, String> getPnsdkSuffixes() {
        return pnConfigurationCore.getPnsdkSuffixes();
    }

    public String generatePnsdk(@NotNull String version) {
        return pnConfigurationCore.generatePnsdk(version);
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Pair<String, String>... nameToSuffixes) {
        pnConfigurationCore.addPnsdkSuffix(nameToSuffixes);
        return this;
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Map<String, String> nameToSuffixes) {
        pnConfigurationCore.addPnsdkSuffix(nameToSuffixes);
        return this;
    }

    private static int validatePresenceTimeout(int timeout) {
        int validTimeout = timeout;
        if (timeout < MINIMUM_PRESENCE_TIMEOUT) {
            validTimeout = MINIMUM_PRESENCE_TIMEOUT;
            log.warning("Presence timeout is too low. Defaulting to: " + MINIMUM_PRESENCE_TIMEOUT);
        }
        return validTimeout;
    }

    public PNConfiguration setRetryConfiguration(RetryConfiguration configuration) {
        pnConfigurationCore.setRetryConfiguration(configuration);
        return this;
    }

    public RetryConfiguration getRetryConfiguration() {
        return pnConfigurationCore.getRetryConfiguration();
    }

}

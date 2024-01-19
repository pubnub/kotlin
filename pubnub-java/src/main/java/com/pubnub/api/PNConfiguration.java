package com.pubnub.api;


import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNReconnectionPolicy;
import kotlin.Pair;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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

@Getter
@Setter
@Accessors(chain = true)

@Log
public class PNConfiguration {
    @Getter(AccessLevel.PACKAGE)
    private com.pubnub.internal.PNConfiguration internalConfig;

    private static final int DEFAULT_DEDUPE_SIZE = 100;
    private static final int PRESENCE_TIMEOUT = 300;
    private static final int MINIMUM_PRESENCE_TIMEOUT = 20;
    private static final int NON_SUBSCRIBE_REQUEST_TIMEOUT = 10;
    private static final int SUBSCRIBE_TIMEOUT = 310;
    private static final int CONNECT_TIMEOUT = 5;
    private static final int FILE_MESSAGE_PUBLISH_RETRY_LIMIT = 5;

    public String getOrigin() {
        return internalConfig.getOrigin();
    }

    public PNConfiguration setOrigin(@NotNull String s) {
        internalConfig.setOrigin(s);
        return this;
    }

    public boolean isSecure() {
        return internalConfig.getSecure();
    }

    public PNConfiguration setSecure(boolean b) {
        internalConfig.setSecure(b);
        return this;
    }

    public PNLogVerbosity getLogVerbosity() {
        return internalConfig.getLogVerbosity();
    }

    public PNConfiguration setLogVerbosity(@NotNull PNLogVerbosity pnLogVerbosity) {
        internalConfig.setLogVerbosity(pnLogVerbosity);
        return this;
    }

    public PNHeartbeatNotificationOptions getHeartbeatNotificationOptions() {
        return internalConfig.getHeartbeatNotificationOptions();
    }

    public PNConfiguration setHeartbeatNotificationOptions(@NotNull PNHeartbeatNotificationOptions pnHeartbeatNotificationOptions) {
        internalConfig.setHeartbeatNotificationOptions(pnHeartbeatNotificationOptions);
        return this;
    }

    public PNReconnectionPolicy getReconnectionPolicy() {
        return internalConfig.getReconnectionPolicy();
    }

    public PNConfiguration setReconnectionPolicy(@NotNull PNReconnectionPolicy pnReconnectionPolicy) {
        internalConfig.setReconnectionPolicy(pnReconnectionPolicy);
        return this;
    }

    public int getPresenceTimeout() {
        return internalConfig.getPresenceTimeout();
    }

    /**
     * @return
     * @deprecated Use {@link #setUserId(UserId)} instead.
     */
    @Deprecated
    public PNConfiguration setUuid(@NotNull String uuid) {
        internalConfig.setUuid(uuid);
        return this;
    }

    public boolean getEnableEventEngine() {
        return internalConfig.getEnableEventEngine();
    }

    public PNConfiguration setEnableEventEngine(boolean b) {
        internalConfig.setEnableEventEngine(b);
        return this;
    }

    public String getSubscribeKey() {
        return internalConfig.getSubscribeKey();
    }

    public PNConfiguration setSubscribeKey(@NotNull String s) {
        internalConfig.setSubscribeKey(s);
        return this;
    }

    public String getPublishKey() {
        return internalConfig.getPublishKey();
    }

    public PNConfiguration setPublishKey(@NotNull String s) {
        internalConfig.setPublishKey(s);
        return this;
    }

    public String getSecretKey() {
        return internalConfig.getSecretKey();
    }

    public PNConfiguration setSecretKey(@NotNull String s) {
        internalConfig.setSecretKey(s);
        return this;
    }

    public String getAuthKey() {
        return internalConfig.getAuthKey();
    }

    public PNConfiguration setAuthKey(@NotNull String s) {
        internalConfig.setAuthKey(s);
        return this;
    }

    @Deprecated
    public String getCipherKey() {
        return internalConfig.getCipherKey();
    }

    @Deprecated
    public PNConfiguration setCipherKey(String s) {
        internalConfig.setCipherKey(s != null ? s : "");
        return this;
    }

    @Deprecated
    public boolean getUseRandomInitializationVector() {
        return internalConfig.getUseRandomInitializationVector();
    }

    @Deprecated
    public PNConfiguration setUseRandomInitializationVector(boolean b) {
        internalConfig.setUseRandomInitializationVector(b);
        return this;
    }

    public CryptoModule getCryptoModule() {
        return internalConfig.getCryptoModule();
    }

    public PNConfiguration setCryptoModule(@Nullable CryptoModule cryptoModule) {
        internalConfig.setCryptoModule(cryptoModule);
        return this;
    }

    public UserId getUserId() {
        return internalConfig.getUserId();
    }

    public PNConfiguration setUserId(@NotNull UserId userId) {
        internalConfig.setUserId(userId);
        return this;
    }

    public String getUuid() {
        return internalConfig.getUuid();
    }


    /**
     * if set, the SDK will alert once the number of messages arrived in one call equal to the threshold
     */
    private Integer requestMessageCountThreshold;

    /**
     * Enables explicit presence control.
     * When set to true heartbeat calls will contain only channels and groups added explicitly
     * using {@link PubNub#presence()}. Should be used only with ACL set on the server side.
     * For more information please contact PubNub support
     *
     * @see PubNub#presence()
     * @see com.pubnub.internal.PNConfiguration#heartbeatInterval
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
        internalConfig = new com.pubnub.internal.PNConfiguration(userId);
        managePresenceListManually = false;
    }

    /**
     * Initialize the PNConfiguration with default values
     *
     * @param uuid
     * @deprecated Use {@link com.pubnub.internal.PNConfiguration (UserId)} instead.
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
        internalConfig.setPresenceTimeout(timeout);
        internalConfig.setHeartbeatInterval(interval);

        return this;
    }

    /**
     * set presence configurations for timeout and allow the client to pick the best interval
     *
     * @param timeout presence timeout; how long before the server considers this client to be gone.
     * @return returns itself.
     */
    public PNConfiguration setPresenceTimeout(int timeout) {
        timeout = validatePresenceTimeout(timeout);
        internalConfig.setPresenceTimeout(timeout);
        return this;
    }

    public int getHeartbeatInterval() {
        return internalConfig.getHeartbeatInterval();
    }

    public void setHeartbeatInterval(int i) {
        internalConfig.setHeartbeatInterval(i);
    }

    public int getSubscribeTimeout() {
        return internalConfig.getSubscribeTimeout();
    }

    public PNConfiguration setSubscribeTimeout(int i) {
        internalConfig.setSubscribeTimeout(i);
        return this;
    }

    public int getConnectTimeout() {
        return internalConfig.getConnectTimeout();
    }

    public PNConfiguration setConnectTimeout(int i) {
        internalConfig.setConnectTimeout(i);
        return this;
    }

    public int getNonSubscribeRequestTimeout() {
        return internalConfig.getNonSubscribeRequestTimeout();
    }

    public PNConfiguration setNonSubscribeRequestTimeout(int i) {
        internalConfig.setNonSubscribeRequestTimeout(i);
        return this;
    }

    public boolean getCacheBusting() {
        return internalConfig.getCacheBusting();
    }

    public PNConfiguration setCacheBusting(boolean b) {
        internalConfig.setCacheBusting(b);
        return this;
    }

    public boolean getSuppressLeaveEvents() {
        return internalConfig.getSuppressLeaveEvents();
    }

    public PNConfiguration setSuppressLeaveEvents(boolean b) {
        internalConfig.setSuppressLeaveEvents(b);
        return this;
    }

    public boolean getMaintainPresenceState() {
        return internalConfig.getMaintainPresenceState();
    }

    public PNConfiguration setMaintainPresenceState(boolean b) {
        internalConfig.setMaintainPresenceState(b);
        return this;
    }

    public String getFilterExpression() {
        return internalConfig.getFilterExpression();
    }

    public PNConfiguration setFilterExpression(@NotNull String s) {
        internalConfig.setFilterExpression(s);
        return this;
    }

    public boolean getIncludeInstanceIdentifier() {
        return internalConfig.getIncludeInstanceIdentifier();
    }

    public PNConfiguration setIncludeInstanceIdentifier(boolean b) {
        internalConfig.setIncludeInstanceIdentifier(b);
        return this;
    }

    public boolean getIncludeRequestIdentifier() {
        return internalConfig.getIncludeRequestIdentifier();
    }

    public PNConfiguration setIncludeRequestIdentifier(boolean b) {
        internalConfig.setIncludeRequestIdentifier(b);
        return this;
    }

    public int getMaximumReconnectionRetries() {
        return internalConfig.getMaximumReconnectionRetries();
    }

    public PNConfiguration setMaximumReconnectionRetries(int i) {
        internalConfig.setMaximumReconnectionRetries(i);
        return this;
    }

    public Integer getMaximumConnections() {
        return internalConfig.getMaximumConnections();
    }

    public PNConfiguration setMaximumConnections(@Nullable Integer integer) {
        internalConfig.setMaximumConnections(integer);
        return this;
    }

    public Integer getRequestMessageCountThreshold() {
        return internalConfig.getRequestMessageCountThreshold();
    }

    public PNConfiguration setRequestMessageCountThreshold(@Nullable Integer integer) {
        internalConfig.setRequestMessageCountThreshold(integer);
        return this;
    }

    public boolean getGoogleAppEngineNetworking() {
        return internalConfig.getGoogleAppEngineNetworking();
    }

    public PNConfiguration setGoogleAppEngineNetworking(boolean b) {
        internalConfig.setGoogleAppEngineNetworking(b);
        return this;
    }

    public boolean getStartSubscriberThread() {
        return internalConfig.getStartSubscriberThread();
    }

    public PNConfiguration setStartSubscriberThread(boolean b) {
        internalConfig.setStartSubscriberThread(b);
        return this;
    }

    public Proxy getProxy() {
        return internalConfig.getProxy();
    }

    public PNConfiguration setProxy(@Nullable Proxy proxy) {
        internalConfig.setProxy(proxy);
        return this;
    }

    public ProxySelector getProxySelector() {
        return internalConfig.getProxySelector();
    }

    public PNConfiguration setProxySelector(@Nullable ProxySelector proxySelector) {
        internalConfig.setProxySelector(proxySelector);
        return this;
    }

    public Authenticator getProxyAuthenticator() {
        return internalConfig.getProxyAuthenticator();
    }

    public PNConfiguration setProxyAuthenticator(@Nullable Authenticator authenticator) {
        internalConfig.setProxyAuthenticator(authenticator);
        return this;
    }

    public CertificatePinner getCertificatePinner() {
        return internalConfig.getCertificatePinner();
    }

    public PNConfiguration setCertificatePinner(@Nullable CertificatePinner certificatePinner) {
        internalConfig.setCertificatePinner(certificatePinner);
        return this;
    }

    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return internalConfig.getHttpLoggingInterceptor();
    }

    public PNConfiguration setHttpLoggingInterceptor(@Nullable HttpLoggingInterceptor httpLoggingInterceptor) {
        internalConfig.setHttpLoggingInterceptor(httpLoggingInterceptor);
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return internalConfig.getSslSocketFactory();
    }

    public PNConfiguration setSslSocketFactory(@Nullable SSLSocketFactory sslSocketFactory) {
        internalConfig.setSslSocketFactory(sslSocketFactory);
        return this;
    }

    public X509ExtendedTrustManager getX509ExtendedTrustManager() {
        return internalConfig.getX509ExtendedTrustManager();
    }

    public PNConfiguration setX509ExtendedTrustManager(@Nullable X509ExtendedTrustManager x509ExtendedTrustManager) {
        internalConfig.setX509ExtendedTrustManager(x509ExtendedTrustManager);
        return this;
    }

    public ConnectionSpec getConnectionSpec() {
        return internalConfig.getConnectionSpec();
    }

    public PNConfiguration setConnectionSpec(@Nullable ConnectionSpec connectionSpec) {
        internalConfig.setConnectionSpec(connectionSpec);
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return internalConfig.getHostnameVerifier();
    }

    public PNConfiguration setHostnameVerifier(@Nullable HostnameVerifier hostnameVerifier) {
        internalConfig.setHostnameVerifier(hostnameVerifier);
        return this;
    }

    public int getFileMessagePublishRetryLimit() {
        return internalConfig.getFileMessagePublishRetryLimit();
    }

    public PNConfiguration setFileMessagePublishRetryLimit(int i) {
        internalConfig.setFileMessagePublishRetryLimit(i);
        return this;
    }

    public boolean getDedupOnSubscribe() {
        return internalConfig.getDedupOnSubscribe();
    }

    public PNConfiguration setDedupOnSubscribe(boolean b) {
        internalConfig.setDedupOnSubscribe(b);
        return this;
    }

    public int getMaximumMessagesCacheSize() {
        return internalConfig.getMaximumMessagesCacheSize();
    }

    public PNConfiguration setMaximumMessagesCacheSize(int i) {
        internalConfig.setMaximumMessagesCacheSize(i);
        return this;
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Pair<String, String>... nameToSuffixes) {
        internalConfig.addPnsdkSuffix(nameToSuffixes);
        return this;
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Map<String, String> nameToSuffixes) {
        internalConfig.addPnsdkSuffix(nameToSuffixes);
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

    private static void require(boolean value, PubNubError error) {
        if (!value) {
            throw PubNubRuntimeException.builder().pubnubError(error).build();
        }
    }

}

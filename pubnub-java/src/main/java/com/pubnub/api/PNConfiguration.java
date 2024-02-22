package com.pubnub.api;


import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNReconnectionPolicy;
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
    private final com.pubnub.internal.PNConfiguration pnConfiguration;

    public com.pubnub.internal.PNConfiguration getPnConfiguration() {
        return pnConfiguration;
    }

    private static final int DEFAULT_DEDUPE_SIZE = 100;
    private static final int PRESENCE_TIMEOUT = 300;
    private static final int MINIMUM_PRESENCE_TIMEOUT = 20;
    private static final int NON_SUBSCRIBE_REQUEST_TIMEOUT = 10;
    private static final int SUBSCRIBE_TIMEOUT = 310;
    private static final int CONNECT_TIMEOUT = 5;
    private static final int FILE_MESSAGE_PUBLISH_RETRY_LIMIT = 5;

    public String getOrigin() {
        return pnConfiguration.getOrigin();
    }

    public PNConfiguration setOrigin(@NotNull String s) {
        pnConfiguration.setOrigin(s);
        return this;
    }

    public boolean getSecure() {
        return pnConfiguration.getSecure();
    }

    public PNConfiguration setSecure(boolean b) {
        pnConfiguration.setSecure(b);
        return this;
    }

    public PNLogVerbosity getLogVerbosity() {
        return pnConfiguration.getLogVerbosity();
    }

    public PNConfiguration setLogVerbosity(@NotNull PNLogVerbosity pnLogVerbosity) {
        pnConfiguration.setLogVerbosity(pnLogVerbosity);
        return this;
    }

    public PNHeartbeatNotificationOptions getHeartbeatNotificationOptions() {
        return pnConfiguration.getHeartbeatNotificationOptions();
    }

    public PNConfiguration setHeartbeatNotificationOptions(@NotNull PNHeartbeatNotificationOptions pnHeartbeatNotificationOptions) {
        pnConfiguration.setHeartbeatNotificationOptions(pnHeartbeatNotificationOptions);
        return this;
    }

    public PNReconnectionPolicy getReconnectionPolicy() {
        return pnConfiguration.getReconnectionPolicy();
    }

    public PNConfiguration setReconnectionPolicy(@NotNull PNReconnectionPolicy pnReconnectionPolicy) {
        pnConfiguration.setReconnectionPolicy(pnReconnectionPolicy);
        return this;
    }

    public int getPresenceTimeout() {
        return pnConfiguration.getPresenceTimeout();
    }

    /**
     * @return
     * @deprecated Use {@link #setUserId(UserId)} instead.
     */
    @Deprecated
    public PNConfiguration setUuid(@NotNull String uuid) {
        pnConfiguration.setUuid(uuid);
        return this;
    }

    public String getSubscribeKey() {
        return pnConfiguration.getSubscribeKey();
    }

    public PNConfiguration setSubscribeKey(@NotNull String s) {
        pnConfiguration.setSubscribeKey(s);
        return this;
    }

    public String getPublishKey() {
        return pnConfiguration.getPublishKey();
    }

    public PNConfiguration setPublishKey(@NotNull String s) {
        pnConfiguration.setPublishKey(s);
        return this;
    }

    public String getSecretKey() {
        return pnConfiguration.getSecretKey();
    }

    public PNConfiguration setSecretKey(@NotNull String s) {
        pnConfiguration.setSecretKey(s);
        return this;
    }

    public String getAuthKey() {
        return pnConfiguration.getAuthKey();
    }

    public PNConfiguration setAuthKey(@NotNull String s) {
        pnConfiguration.setAuthKey(s);
        return this;
    }

    @Deprecated
    public String getCipherKey() {
        return pnConfiguration.getCipherKey();
    }

    @Deprecated
    public PNConfiguration setCipherKey(String s) {
        pnConfiguration.setCipherKey(s != null ? s : "");
        return this;
    }

    @Deprecated
    public boolean getUseRandomInitializationVector() {
        return pnConfiguration.getUseRandomInitializationVector();
    }

    @Deprecated
    public PNConfiguration setUseRandomInitializationVector(boolean b) {
        pnConfiguration.setUseRandomInitializationVector(b);
        return this;
    }

    public CryptoModule getCryptoModule() {
        return pnConfiguration.getCryptoModule();
    }

    public PNConfiguration setCryptoModule(@Nullable CryptoModule cryptoModule) {
        pnConfiguration.setCryptoModule(cryptoModule);
        return this;
    }

    public UserId getUserId() {
        return pnConfiguration.getUserId();
    }

    public PNConfiguration setUserId(@NotNull UserId userId) {
        pnConfiguration.setUserId(userId);
        return this;
    }

    public String getUuid() {
        return pnConfiguration.getUuid();
    }


    /**
     * if set, the SDK will alert once the number of messages arrived in one call equal to the threshold
     */
    private Integer requestMessageCountThreshold; // TODO not supported in EE

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
        pnConfiguration = new com.pubnub.internal.PNConfiguration(userId);
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
        pnConfiguration.setPresenceTimeout(timeout);
        pnConfiguration.setHeartbeatInterval(interval);

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
        pnConfiguration.setPresenceTimeout(timeout);
        return this;
    }

    public int getHeartbeatInterval() {
        return pnConfiguration.getHeartbeatInterval();
    }

    public void setHeartbeatInterval(int i) {
        pnConfiguration.setHeartbeatInterval(i);
    }

    public int getSubscribeTimeout() {
        return pnConfiguration.getSubscribeTimeout();
    }

    public PNConfiguration setSubscribeTimeout(int i) {
        pnConfiguration.setSubscribeTimeout(i);
        return this;
    }

    public int getConnectTimeout() {
        return pnConfiguration.getConnectTimeout();
    }

    public PNConfiguration setConnectTimeout(int i) {
        pnConfiguration.setConnectTimeout(i);
        return this;
    }

    public int getNonSubscribeRequestTimeout() {
        return pnConfiguration.getNonSubscribeRequestTimeout();
    }

    public PNConfiguration setNonSubscribeRequestTimeout(int i) {
        pnConfiguration.setNonSubscribeRequestTimeout(i);
        return this;
    }

    public boolean getCacheBusting() {
        return pnConfiguration.getCacheBusting();
    }

    public PNConfiguration setCacheBusting(boolean b) {
        pnConfiguration.setCacheBusting(b);
        return this;
    }

    public boolean getSuppressLeaveEvents() {
        return pnConfiguration.getSuppressLeaveEvents();
    }

    public PNConfiguration setSuppressLeaveEvents(boolean b) {
        pnConfiguration.setSuppressLeaveEvents(b);
        return this;
    }

    public boolean getMaintainPresenceState() {
        return pnConfiguration.getMaintainPresenceState();
    }

    public PNConfiguration setMaintainPresenceState(boolean b) {
        pnConfiguration.setMaintainPresenceState(b);
        return this;
    }

    public String getFilterExpression() {
        return pnConfiguration.getFilterExpression();
    }

    public PNConfiguration setFilterExpression(@NotNull String s) {
        pnConfiguration.setFilterExpression(s);
        return this;
    }

    public boolean getIncludeInstanceIdentifier() {
        return pnConfiguration.getIncludeInstanceIdentifier();
    }

    public PNConfiguration setIncludeInstanceIdentifier(boolean b) {
        pnConfiguration.setIncludeInstanceIdentifier(b);
        return this;
    }

    public boolean getIncludeRequestIdentifier() {
        return pnConfiguration.getIncludeRequestIdentifier();
    }

    public PNConfiguration setIncludeRequestIdentifier(boolean b) {
        pnConfiguration.setIncludeRequestIdentifier(b);
        return this;
    }

    public int getMaximumReconnectionRetries() {
        return pnConfiguration.getMaximumReconnectionRetries();
    }

    public PNConfiguration setMaximumReconnectionRetries(int i) {
        pnConfiguration.setMaximumReconnectionRetries(i);
        return this;
    }

    public Integer getMaximumConnections() {
        return pnConfiguration.getMaximumConnections();
    }

    public PNConfiguration setMaximumConnections(@Nullable Integer integer) {
        pnConfiguration.setMaximumConnections(integer);
        return this;
    }

    public Integer getRequestMessageCountThreshold() {
        return pnConfiguration.getRequestMessageCountThreshold();
    }

    @Deprecated // TODO not supported in EE
    public PNConfiguration setRequestMessageCountThreshold(@Nullable Integer integer) {
        pnConfiguration.setRequestMessageCountThreshold(integer);
        return this;
    }

    public boolean getGoogleAppEngineNetworking() {
        return pnConfiguration.getGoogleAppEngineNetworking();
    }

    public PNConfiguration setGoogleAppEngineNetworking(boolean b) {
        pnConfiguration.setGoogleAppEngineNetworking(b);
        return this;
    }

    public boolean getStartSubscriberThread() {
        return pnConfiguration.getStartSubscriberThread();
    }

    public PNConfiguration setStartSubscriberThread(boolean b) {
        pnConfiguration.setStartSubscriberThread(b);
        return this;
    }

    public Proxy getProxy() {
        return pnConfiguration.getProxy();
    }

    public PNConfiguration setProxy(@Nullable Proxy proxy) {
        pnConfiguration.setProxy(proxy);
        return this;
    }

    public ProxySelector getProxySelector() {
        return pnConfiguration.getProxySelector();
    }

    public PNConfiguration setProxySelector(@Nullable ProxySelector proxySelector) {
        pnConfiguration.setProxySelector(proxySelector);
        return this;
    }

    public Authenticator getProxyAuthenticator() {
        return pnConfiguration.getProxyAuthenticator();
    }

    public PNConfiguration setProxyAuthenticator(@Nullable Authenticator authenticator) {
        pnConfiguration.setProxyAuthenticator(authenticator);
        return this;
    }

    public CertificatePinner getCertificatePinner() {
        return pnConfiguration.getCertificatePinner();
    }

    public PNConfiguration setCertificatePinner(@Nullable CertificatePinner certificatePinner) {
        pnConfiguration.setCertificatePinner(certificatePinner);
        return this;
    }

    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return pnConfiguration.getHttpLoggingInterceptor();
    }

    public PNConfiguration setHttpLoggingInterceptor(@Nullable HttpLoggingInterceptor httpLoggingInterceptor) {
        pnConfiguration.setHttpLoggingInterceptor(httpLoggingInterceptor);
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return pnConfiguration.getSslSocketFactory();
    }

    public PNConfiguration setSslSocketFactory(@Nullable SSLSocketFactory sslSocketFactory) {
        pnConfiguration.setSslSocketFactory(sslSocketFactory);
        return this;
    }

    public X509ExtendedTrustManager getX509ExtendedTrustManager() {
        return pnConfiguration.getX509ExtendedTrustManager();
    }

    public PNConfiguration setX509ExtendedTrustManager(@Nullable X509ExtendedTrustManager x509ExtendedTrustManager) {
        pnConfiguration.setX509ExtendedTrustManager(x509ExtendedTrustManager);
        return this;
    }

    public ConnectionSpec getConnectionSpec() {
        return pnConfiguration.getConnectionSpec();
    }

    public PNConfiguration setConnectionSpec(@Nullable ConnectionSpec connectionSpec) {
        pnConfiguration.setConnectionSpec(connectionSpec);
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return pnConfiguration.getHostnameVerifier();
    }

    public PNConfiguration setHostnameVerifier(@Nullable HostnameVerifier hostnameVerifier) {
        pnConfiguration.setHostnameVerifier(hostnameVerifier);
        return this;
    }

    public int getFileMessagePublishRetryLimit() {
        return pnConfiguration.getFileMessagePublishRetryLimit();
    }

    public PNConfiguration setFileMessagePublishRetryLimit(int i) {
        pnConfiguration.setFileMessagePublishRetryLimit(i);
        return this;
    }

    public boolean getDedupOnSubscribe() {
        return pnConfiguration.getDedupOnSubscribe();
    }

    public PNConfiguration setDedupOnSubscribe(boolean b) {
        pnConfiguration.setDedupOnSubscribe(b);
        return this;
    }

    public int getMaximumMessagesCacheSize() {
        return pnConfiguration.getMaximumMessagesCacheSize();
    }

    public PNConfiguration setMaximumMessagesCacheSize(int i) {
        pnConfiguration.setMaximumMessagesCacheSize(i);
        return this;
    }

    public ConcurrentMap<String, String> getPnsdkSuffixes() {
        return pnConfiguration.getPnsdkSuffixes();
    }

    public String generatePnsdk(@NotNull String version) {
        return pnConfiguration.generatePnsdk(version);
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Pair<String, String>... nameToSuffixes) {
        pnConfiguration.addPnsdkSuffix(nameToSuffixes);
        return this;
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Map<String, String> nameToSuffixes) {
        pnConfiguration.addPnsdkSuffix(nameToSuffixes);
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

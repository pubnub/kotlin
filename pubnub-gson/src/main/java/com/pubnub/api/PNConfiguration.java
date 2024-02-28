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
    private final com.pubnub.internal.CorePNConfiguration corePnConfiguration;

    public com.pubnub.internal.CorePNConfiguration getCorePnConfiguration() {
        return corePnConfiguration;
    }

    private static final int DEFAULT_DEDUPE_SIZE = 100;
    private static final int PRESENCE_TIMEOUT = 300;
    private static final int MINIMUM_PRESENCE_TIMEOUT = 20;
    private static final int NON_SUBSCRIBE_REQUEST_TIMEOUT = 10;
    private static final int SUBSCRIBE_TIMEOUT = 310;
    private static final int CONNECT_TIMEOUT = 5;
    private static final int FILE_MESSAGE_PUBLISH_RETRY_LIMIT = 5;

    public String getOrigin() {
        return corePnConfiguration.getOrigin();
    }

    public PNConfiguration setOrigin(@NotNull String s) {
        corePnConfiguration.setOrigin(s);
        return this;
    }

    public boolean getSecure() {
        return corePnConfiguration.getSecure();
    }

    public PNConfiguration setSecure(boolean b) {
        corePnConfiguration.setSecure(b);
        return this;
    }

    public PNLogVerbosity getLogVerbosity() {
        return corePnConfiguration.getLogVerbosity();
    }

    public PNConfiguration setLogVerbosity(@NotNull PNLogVerbosity pnLogVerbosity) {
        corePnConfiguration.setLogVerbosity(pnLogVerbosity);
        return this;
    }

    public PNHeartbeatNotificationOptions getHeartbeatNotificationOptions() {
        return corePnConfiguration.getHeartbeatNotificationOptions();
    }

    public PNConfiguration setHeartbeatNotificationOptions(@NotNull PNHeartbeatNotificationOptions pnHeartbeatNotificationOptions) {
        corePnConfiguration.setHeartbeatNotificationOptions(pnHeartbeatNotificationOptions);
        return this;
    }

    public PNReconnectionPolicy getReconnectionPolicy() {
        return corePnConfiguration.getReconnectionPolicy();
    }

    public PNConfiguration setReconnectionPolicy(@NotNull PNReconnectionPolicy pnReconnectionPolicy) {
        corePnConfiguration.setReconnectionPolicy(pnReconnectionPolicy);
        return this;
    }

    public int getPresenceTimeout() {
        return corePnConfiguration.getPresenceTimeout();
    }

    /**
     * @return
     * @deprecated Use {@link #setUserId(UserId)} instead.
     */
    @Deprecated
    public PNConfiguration setUuid(@NotNull String uuid) {
        corePnConfiguration.setUuid(uuid);
        return this;
    }

    public String getSubscribeKey() {
        return corePnConfiguration.getSubscribeKey();
    }

    public PNConfiguration setSubscribeKey(@NotNull String s) {
        corePnConfiguration.setSubscribeKey(s);
        return this;
    }

    public String getPublishKey() {
        return corePnConfiguration.getPublishKey();
    }

    public PNConfiguration setPublishKey(@NotNull String s) {
        corePnConfiguration.setPublishKey(s);
        return this;
    }

    public String getSecretKey() {
        return corePnConfiguration.getSecretKey();
    }

    public PNConfiguration setSecretKey(@NotNull String s) {
        corePnConfiguration.setSecretKey(s);
        return this;
    }

    public String getAuthKey() {
        return corePnConfiguration.getAuthKey();
    }

    public PNConfiguration setAuthKey(@NotNull String s) {
        corePnConfiguration.setAuthKey(s);
        return this;
    }

    @Deprecated
    public String getCipherKey() {
        return corePnConfiguration.getCipherKey();
    }

    @Deprecated
    public PNConfiguration setCipherKey(String s) {
        corePnConfiguration.setCipherKey(s != null ? s : "");
        return this;
    }

    @Deprecated
    public boolean getUseRandomInitializationVector() {
        return corePnConfiguration.getUseRandomInitializationVector();
    }

    @Deprecated
    public PNConfiguration setUseRandomInitializationVector(boolean b) {
        corePnConfiguration.setUseRandomInitializationVector(b);
        return this;
    }

    public CryptoModule getCryptoModule() {
        return corePnConfiguration.getCryptoModule();
    }

    public PNConfiguration setCryptoModule(@Nullable CryptoModule cryptoModule) {
        corePnConfiguration.setCryptoModule(cryptoModule);
        return this;
    }

    public UserId getUserId() {
        return corePnConfiguration.getUserId();
    }

    public PNConfiguration setUserId(@NotNull UserId userId) {
        corePnConfiguration.setUserId(userId);
        return this;
    }

    public String getUuid() {
        return corePnConfiguration.getUuid();
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
        corePnConfiguration = new com.pubnub.internal.CorePNConfiguration(userId);
        managePresenceListManually = false;
    }

    /**
     * Initialize the PNConfiguration with default values
     *
     * @param uuid
     * @deprecated Use {@link com.pubnub.internal.CorePNConfiguration (UserId)} instead.
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
        corePnConfiguration.setPresenceTimeout(timeout);
        corePnConfiguration.setHeartbeatInterval(interval);

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
        corePnConfiguration.setPresenceTimeout(timeout);
        return this;
    }

    public int getHeartbeatInterval() {
        return corePnConfiguration.getHeartbeatInterval();
    }

    public void setHeartbeatInterval(int i) {
        corePnConfiguration.setHeartbeatInterval(i);
    }

    public int getSubscribeTimeout() {
        return corePnConfiguration.getSubscribeTimeout();
    }

    public PNConfiguration setSubscribeTimeout(int i) {
        corePnConfiguration.setSubscribeTimeout(i);
        return this;
    }

    public int getConnectTimeout() {
        return corePnConfiguration.getConnectTimeout();
    }

    public PNConfiguration setConnectTimeout(int i) {
        corePnConfiguration.setConnectTimeout(i);
        return this;
    }

    public int getNonSubscribeRequestTimeout() {
        return corePnConfiguration.getNonSubscribeRequestTimeout();
    }

    public PNConfiguration setNonSubscribeRequestTimeout(int i) {
        corePnConfiguration.setNonSubscribeRequestTimeout(i);
        return this;
    }

    public boolean getCacheBusting() {
        return corePnConfiguration.getCacheBusting();
    }

    public PNConfiguration setCacheBusting(boolean b) {
        corePnConfiguration.setCacheBusting(b);
        return this;
    }

    public boolean getSuppressLeaveEvents() {
        return corePnConfiguration.getSuppressLeaveEvents();
    }

    public PNConfiguration setSuppressLeaveEvents(boolean b) {
        corePnConfiguration.setSuppressLeaveEvents(b);
        return this;
    }

    public boolean getMaintainPresenceState() {
        return corePnConfiguration.getMaintainPresenceState();
    }

    public PNConfiguration setMaintainPresenceState(boolean b) {
        corePnConfiguration.setMaintainPresenceState(b);
        return this;
    }

    public String getFilterExpression() {
        return corePnConfiguration.getFilterExpression();
    }

    public PNConfiguration setFilterExpression(@NotNull String s) {
        corePnConfiguration.setFilterExpression(s);
        return this;
    }

    public boolean getIncludeInstanceIdentifier() {
        return corePnConfiguration.getIncludeInstanceIdentifier();
    }

    public PNConfiguration setIncludeInstanceIdentifier(boolean b) {
        corePnConfiguration.setIncludeInstanceIdentifier(b);
        return this;
    }

    public boolean getIncludeRequestIdentifier() {
        return corePnConfiguration.getIncludeRequestIdentifier();
    }

    public PNConfiguration setIncludeRequestIdentifier(boolean b) {
        corePnConfiguration.setIncludeRequestIdentifier(b);
        return this;
    }

    public int getMaximumReconnectionRetries() {
        return corePnConfiguration.getMaximumReconnectionRetries();
    }

    public PNConfiguration setMaximumReconnectionRetries(int i) {
        corePnConfiguration.setMaximumReconnectionRetries(i);
        return this;
    }

    public Integer getMaximumConnections() {
        return corePnConfiguration.getMaximumConnections();
    }

    public PNConfiguration setMaximumConnections(@Nullable Integer integer) {
        corePnConfiguration.setMaximumConnections(integer);
        return this;
    }

    public boolean getGoogleAppEngineNetworking() {
        return corePnConfiguration.getGoogleAppEngineNetworking();
    }

    public PNConfiguration setGoogleAppEngineNetworking(boolean b) {
        corePnConfiguration.setGoogleAppEngineNetworking(b);
        return this;
    }

    public Proxy getProxy() {
        return corePnConfiguration.getProxy();
    }

    public PNConfiguration setProxy(@Nullable Proxy proxy) {
        corePnConfiguration.setProxy(proxy);
        return this;
    }

    public ProxySelector getProxySelector() {
        return corePnConfiguration.getProxySelector();
    }

    public PNConfiguration setProxySelector(@Nullable ProxySelector proxySelector) {
        corePnConfiguration.setProxySelector(proxySelector);
        return this;
    }

    public Authenticator getProxyAuthenticator() {
        return corePnConfiguration.getProxyAuthenticator();
    }

    public PNConfiguration setProxyAuthenticator(@Nullable Authenticator authenticator) {
        corePnConfiguration.setProxyAuthenticator(authenticator);
        return this;
    }

    public CertificatePinner getCertificatePinner() {
        return corePnConfiguration.getCertificatePinner();
    }

    public PNConfiguration setCertificatePinner(@Nullable CertificatePinner certificatePinner) {
        corePnConfiguration.setCertificatePinner(certificatePinner);
        return this;
    }

    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return corePnConfiguration.getHttpLoggingInterceptor();
    }

    public PNConfiguration setHttpLoggingInterceptor(@Nullable HttpLoggingInterceptor httpLoggingInterceptor) {
        corePnConfiguration.setHttpLoggingInterceptor(httpLoggingInterceptor);
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return corePnConfiguration.getSslSocketFactory();
    }

    public PNConfiguration setSslSocketFactory(@Nullable SSLSocketFactory sslSocketFactory) {
        corePnConfiguration.setSslSocketFactory(sslSocketFactory);
        return this;
    }

    public X509ExtendedTrustManager getX509ExtendedTrustManager() {
        return corePnConfiguration.getX509ExtendedTrustManager();
    }

    public PNConfiguration setX509ExtendedTrustManager(@Nullable X509ExtendedTrustManager x509ExtendedTrustManager) {
        corePnConfiguration.setX509ExtendedTrustManager(x509ExtendedTrustManager);
        return this;
    }

    public ConnectionSpec getConnectionSpec() {
        return corePnConfiguration.getConnectionSpec();
    }

    public PNConfiguration setConnectionSpec(@Nullable ConnectionSpec connectionSpec) {
        corePnConfiguration.setConnectionSpec(connectionSpec);
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return corePnConfiguration.getHostnameVerifier();
    }

    public PNConfiguration setHostnameVerifier(@Nullable HostnameVerifier hostnameVerifier) {
        corePnConfiguration.setHostnameVerifier(hostnameVerifier);
        return this;
    }

    public int getFileMessagePublishRetryLimit() {
        return corePnConfiguration.getFileMessagePublishRetryLimit();
    }

    public PNConfiguration setFileMessagePublishRetryLimit(int i) {
        corePnConfiguration.setFileMessagePublishRetryLimit(i);
        return this;
    }

    public boolean getDedupOnSubscribe() {
        return corePnConfiguration.getDedupOnSubscribe();
    }

    public PNConfiguration setDedupOnSubscribe(boolean b) {
        corePnConfiguration.setDedupOnSubscribe(b);
        return this;
    }

    public int getMaximumMessagesCacheSize() {
        return corePnConfiguration.getMaximumMessagesCacheSize();
    }

    public PNConfiguration setMaximumMessagesCacheSize(int i) {
        corePnConfiguration.setMaximumMessagesCacheSize(i);
        return this;
    }

    public ConcurrentMap<String, String> getPnsdkSuffixes() {
        return corePnConfiguration.getPnsdkSuffixes();
    }

    public String generatePnsdk(@NotNull String version) {
        return corePnConfiguration.generatePnsdk(version);
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Pair<String, String>... nameToSuffixes) {
        corePnConfiguration.addPnsdkSuffix(nameToSuffixes);
        return this;
    }

    @Deprecated
    public PNConfiguration addPnsdkSuffix(@NotNull Map<String, String> nameToSuffixes) {
        corePnConfiguration.addPnsdkSuffix(nameToSuffixes);
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

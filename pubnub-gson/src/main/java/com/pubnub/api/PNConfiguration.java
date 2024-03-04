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
    private final com.pubnub.internal.PNConfigurationCore pnConfigurationCore;

    public com.pubnub.internal.PNConfigurationCore getPnConfigurationCore() {
        return pnConfigurationCore;
    }

    private static final int DEFAULT_DEDUPE_SIZE = 100;
    private static final int PRESENCE_TIMEOUT = 300;
    private static final int MINIMUM_PRESENCE_TIMEOUT = 20;
    private static final int NON_SUBSCRIBE_REQUEST_TIMEOUT = 10;
    private static final int SUBSCRIBE_TIMEOUT = 310;
    private static final int CONNECT_TIMEOUT = 5;
    private static final int FILE_MESSAGE_PUBLISH_RETRY_LIMIT = 5;

    public String getOrigin() {
        return pnConfigurationCore.getOrigin();
    }

    public PNConfiguration setOrigin(@NotNull String s) {
        pnConfigurationCore.setOrigin(s);
        return this;
    }

    public boolean getSecure() {
        return pnConfigurationCore.getSecure();
    }

    public PNConfiguration setSecure(boolean b) {
        pnConfigurationCore.setSecure(b);
        return this;
    }

    public PNLogVerbosity getLogVerbosity() {
        return pnConfigurationCore.getLogVerbosity();
    }

    public PNConfiguration setLogVerbosity(@NotNull PNLogVerbosity pnLogVerbosity) {
        pnConfigurationCore.setLogVerbosity(pnLogVerbosity);
        return this;
    }

    public PNHeartbeatNotificationOptions getHeartbeatNotificationOptions() {
        return pnConfigurationCore.getHeartbeatNotificationOptions();
    }

    public PNConfiguration setHeartbeatNotificationOptions(@NotNull PNHeartbeatNotificationOptions pnHeartbeatNotificationOptions) {
        pnConfigurationCore.setHeartbeatNotificationOptions(pnHeartbeatNotificationOptions);
        return this;
    }

    public PNReconnectionPolicy getReconnectionPolicy() {
        return pnConfigurationCore.getReconnectionPolicy();
    }

    public PNConfiguration setReconnectionPolicy(@NotNull PNReconnectionPolicy pnReconnectionPolicy) {
        pnConfigurationCore.setReconnectionPolicy(pnReconnectionPolicy);
        return this;
    }

    public int getPresenceTimeout() {
        return pnConfigurationCore.getPresenceTimeout();
    }

    /**
     * @return
     * @deprecated Use {@link #setUserId(UserId)} instead.
     */
    @Deprecated
    public PNConfiguration setUuid(@NotNull String uuid) {
        pnConfigurationCore.setUuid(uuid);
        return this;
    }

    public String getSubscribeKey() {
        return pnConfigurationCore.getSubscribeKey();
    }

    public PNConfiguration setSubscribeKey(@NotNull String s) {
        pnConfigurationCore.setSubscribeKey(s);
        return this;
    }

    public String getPublishKey() {
        return pnConfigurationCore.getPublishKey();
    }

    public PNConfiguration setPublishKey(@NotNull String s) {
        pnConfigurationCore.setPublishKey(s);
        return this;
    }

    public String getSecretKey() {
        return pnConfigurationCore.getSecretKey();
    }

    public PNConfiguration setSecretKey(@NotNull String s) {
        pnConfigurationCore.setSecretKey(s);
        return this;
    }

    public String getAuthKey() {
        return pnConfigurationCore.getAuthKey();
    }

    public PNConfiguration setAuthKey(@NotNull String s) {
        pnConfigurationCore.setAuthKey(s);
        return this;
    }

    @Deprecated
    public String getCipherKey() {
        return pnConfigurationCore.getCipherKey();
    }

    @Deprecated
    public PNConfiguration setCipherKey(String s) {
        pnConfigurationCore.setCipherKey(s != null ? s : "");
        return this;
    }

    @Deprecated
    public boolean getUseRandomInitializationVector() {
        return pnConfigurationCore.getUseRandomInitializationVector();
    }

    @Deprecated
    public PNConfiguration setUseRandomInitializationVector(boolean b) {
        pnConfigurationCore.setUseRandomInitializationVector(b);
        return this;
    }

    public CryptoModule getCryptoModule() {
        return pnConfigurationCore.getCryptoModule();
    }

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
     * set presence configurations for timeout and allow the client to pick the best interval
     *
     * @param timeout presence timeout; how long before the server considers this client to be gone.
     * @return returns itself.
     */
    public PNConfiguration setPresenceTimeout(int timeout) {
        timeout = validatePresenceTimeout(timeout);
        pnConfigurationCore.setPresenceTimeout(timeout);
        return this;
    }

    public int getHeartbeatInterval() {
        return pnConfigurationCore.getHeartbeatInterval();
    }

    public void setHeartbeatInterval(int i) {
        pnConfigurationCore.setHeartbeatInterval(i);
    }

    public int getSubscribeTimeout() {
        return pnConfigurationCore.getSubscribeTimeout();
    }

    public PNConfiguration setSubscribeTimeout(int i) {
        pnConfigurationCore.setSubscribeTimeout(i);
        return this;
    }

    public int getConnectTimeout() {
        return pnConfigurationCore.getConnectTimeout();
    }

    public PNConfiguration setConnectTimeout(int i) {
        pnConfigurationCore.setConnectTimeout(i);
        return this;
    }

    public int getNonSubscribeRequestTimeout() {
        return pnConfigurationCore.getNonSubscribeRequestTimeout();
    }

    public PNConfiguration setNonSubscribeRequestTimeout(int i) {
        pnConfigurationCore.setNonSubscribeRequestTimeout(i);
        return this;
    }

    public boolean getCacheBusting() {
        return pnConfigurationCore.getCacheBusting();
    }

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

    private static void require(boolean value, PubNubError error) {
        if (!value) {
            throw PubNubRuntimeException.builder().pubnubError(error).build();
        }
    }

}

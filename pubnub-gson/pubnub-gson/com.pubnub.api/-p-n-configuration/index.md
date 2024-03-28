//[pubnub-gson](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)

# PNConfiguration

[jvm]\
class [~~PNConfiguration~~](index.md)(userId: [UserId](../../../../pubnub-gson/com.pubnub.api/-user-id/index.md)) : [BasePNConfiguration](../../../../pubnub-gson/com.pubnub.api.v2/-base-p-n-configuration/index.md)---

### Deprecated

Use `com.pubnub.api.v2.PNConfiguration.builder()` instead.

#### Replace with

```kotlin
import com.pubnub.api.v2.PNConfiguration

```
```kotlin
PNConfiguration.builder(userId, subscribeKey = )
```
---

## Constructors

| | |
|---|---|
| [PNConfiguration](-p-n-configuration.md) | [jvm]<br>constructor(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))constructor(userId: [UserId](../../../../pubnub-gson/com.pubnub.api/-user-id/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>open override var [authKey](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [cacheBusting](cache-busting.md) | [jvm]<br>open override var [cacheBusting](cache-busting.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [certificatePinner](certificate-pinner.md) | [jvm]<br>open override var [certificatePinner](certificate-pinner.md): CertificatePinner? |
| [cipherKey](cipher-key.md) | [jvm]<br>var [~~cipherKey~~](cipher-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>If set, all communications to and from PubNub will be encrypted. |
| [connectionSpec](connection-spec.md) | [jvm]<br>open override var [connectionSpec](connection-spec.md): ConnectionSpec? |
| [connectTimeout](connect-timeout.md) | [jvm]<br>open override var [connectTimeout](connect-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [cryptoModule](crypto-module.md) | [jvm]<br>open override var [cryptoModule](crypto-module.md): [CryptoModule](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)? |
| [dedupOnSubscribe](dedup-on-subscribe.md) | [jvm]<br>open override var [dedupOnSubscribe](dedup-on-subscribe.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md) | [jvm]<br>open override var [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [filterExpression](filter-expression.md) | [jvm]<br>open override var [filterExpression](filter-expression.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [googleAppEngineNetworking](google-app-engine-networking.md) | [jvm]<br>open override var [googleAppEngineNetworking](google-app-engine-networking.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [heartbeatInterval](heartbeat-interval.md) | [jvm]<br>open override var [heartbeatInterval](heartbeat-interval.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [heartbeatNotificationOptions](heartbeat-notification-options.md) | [jvm]<br>open override var [heartbeatNotificationOptions](heartbeat-notification-options.md): [PNHeartbeatNotificationOptions](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md) |
| [hostnameVerifier](hostname-verifier.md) | [jvm]<br>open override var [hostnameVerifier](hostname-verifier.md): [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)? |
| [httpLoggingInterceptor](http-logging-interceptor.md) | [jvm]<br>open override var [httpLoggingInterceptor](http-logging-interceptor.md): HttpLoggingInterceptor? |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>open override var [includeInstanceIdentifier](include-instance-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>open override var [includeRequestIdentifier](include-request-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [logVerbosity](log-verbosity.md) | [jvm]<br>open override var [logVerbosity](log-verbosity.md): [PNLogVerbosity](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md) |
| [maintainPresenceState](maintain-presence-state.md) | [jvm]<br>open override var [maintainPresenceState](maintain-presence-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [managePresenceListManually](manage-presence-list-manually.md) | [jvm]<br>open override var [managePresenceListManually](manage-presence-list-manually.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [maximumConnections](maximum-connections.md) | [jvm]<br>open override var [maximumConnections](maximum-connections.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [maximumMessagesCacheSize](maximum-messages-cache-size.md) | [jvm]<br>open override var [maximumMessagesCacheSize](maximum-messages-cache-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [maximumReconnectionRetries](maximum-reconnection-retries.md) | [jvm]<br>var [~~maximumReconnectionRetries~~](maximum-reconnection-retries.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Sets how many times to retry to reconnect before giving up. Must be used in combination with [reconnectionPolicy](reconnection-policy.md). |
| [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md) | [jvm]<br>open override var [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [origin](origin.md) | [jvm]<br>open override var [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [pnsdkSuffixes](pnsdk-suffixes.md) | [jvm]<br>open override var [pnsdkSuffixes](pnsdk-suffixes.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [presenceTimeout](presence-timeout.md) | [jvm]<br>open override var [presenceTimeout](presence-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [proxy](proxy.md) | [jvm]<br>open override var [proxy](proxy.md): [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)? |
| [proxyAuthenticator](proxy-authenticator.md) | [jvm]<br>open override var [proxyAuthenticator](proxy-authenticator.md): Authenticator? |
| [proxySelector](proxy-selector.md) | [jvm]<br>open override var [proxySelector](proxy-selector.md): [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)? |
| [publishKey](publish-key.md) | [jvm]<br>open override var [publishKey](publish-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [reconnectionPolicy](reconnection-policy.md) | [jvm]<br>var [~~reconnectionPolicy~~](reconnection-policy.md): [PNReconnectionPolicy](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/index.md)<br>Set to [PNReconnectionPolicy.LINEAR](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r/index.md) for automatic reconnects. |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>open override var [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](secret-key.md) | [jvm]<br>open override var [secretKey](secret-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [secure](secure.md) | [jvm]<br>open override var [secure](secure.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sslSocketFactory](ssl-socket-factory.md) | [jvm]<br>open override var [sslSocketFactory](ssl-socket-factory.md): [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)? |
| [subscribeKey](subscribe-key.md) | [jvm]<br>open override var [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeTimeout](subscribe-timeout.md) | [jvm]<br>open override var [subscribeTimeout](subscribe-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [suppressLeaveEvents](suppress-leave-events.md) | [jvm]<br>open override var [suppressLeaveEvents](suppress-leave-events.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [useRandomInitializationVector](use-random-initialization-vector.md) | [jvm]<br>var [~~useRandomInitializationVector~~](use-random-initialization-vector.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Should initialization vector for encrypted messages be random. |
| [userId](user-id.md) | [jvm]<br>open override var [userId](user-id.md): [UserId](../../../../pubnub-gson/com.pubnub.api/-user-id/index.md) |
| [uuid](uuid.md) | [jvm]<br>open override val [~~uuid~~](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](x509-extended-trust-manager.md) | [jvm]<br>open override var [x509ExtendedTrustManager](x509-extended-trust-manager.md): [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)? |

## Functions

| Name | Summary |
|---|---|
| [generatePnsdk](../../com.pubnub.api.v2/-p-n-configuration/index.md#1588314698%2FFunctions%2F-395131529) | [jvm]<br>open fun [generatePnsdk](../../com.pubnub.api.v2/-p-n-configuration/index.md#1588314698%2FFunctions%2F-395131529)(version: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [setAuthKey](set-auth-key.md) | [jvm]<br>fun [setAuthKey](set-auth-key.md)(authKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfiguration](index.md)<br>If Access Manager is utilized, client will use this authKey in all restricted requests. |
| [setCacheBusting](set-cache-busting.md) | [jvm]<br>fun [setCacheBusting](set-cache-busting.md)(cacheBusting: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>If operating behind a misbehaving proxy, allow the client to shuffle the subdomains. |
| [setCertificatePinner](set-certificate-pinner.md) | [jvm]<br>fun [setCertificatePinner](set-certificate-pinner.md)(certificatePinner: CertificatePinner?): [PNConfiguration](index.md) |
| [setCipherKey](set-cipher-key.md) | [jvm]<br>fun [setCipherKey](set-cipher-key.md)(cipherKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [PNConfiguration](index.md)<br>If set, all communications to and from PubNub will be encrypted. |
| [setConnectionSpec](set-connection-spec.md) | [jvm]<br>fun [setConnectionSpec](set-connection-spec.md)(connectionSpec: ConnectionSpec?): [PNConfiguration](index.md) |
| [setConnectTimeout](set-connect-timeout.md) | [jvm]<br>fun [setConnectTimeout](set-connect-timeout.md)(connectTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>How long before the client gives up trying to connect with the server. |
| [setCryptoModule](set-crypto-module.md) | [jvm]<br>fun [setCryptoModule](set-crypto-module.md)(cryptoModule: [CryptoModule](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)?): [PNConfiguration](index.md)<br>CryptoModule is responsible for handling encryption and decryption. If set, all communications to and from PubNub will be encrypted. |
| [setDedupOnSubscribe](set-dedup-on-subscribe.md) | [jvm]<br>fun [setDedupOnSubscribe](set-dedup-on-subscribe.md)(dedupOnSubscribe: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md) |
| [setFileMessagePublishRetryLimit](set-file-message-publish-retry-limit.md) | [jvm]<br>fun [setFileMessagePublishRetryLimit](set-file-message-publish-retry-limit.md)(fileMessagePublishRetryLimit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>How many times publishing file message should automatically retry before marking the action as failed |
| [setFilterExpression](set-filter-expression.md) | [jvm]<br>fun [setFilterExpression](set-filter-expression.md)(filterExpression: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfiguration](index.md)<br>Feature to subscribe with a custom filter expression. |
| [setGoogleAppEngineNetworking](set-google-app-engine-networking.md) | [jvm]<br>fun [setGoogleAppEngineNetworking](set-google-app-engine-networking.md)(googleAppEngineNetworking: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>Enable Google App Engine networking. |
| [setHeartbeatInterval](set-heartbeat-interval.md) | [jvm]<br>fun [setHeartbeatInterval](set-heartbeat-interval.md)(heartbeatInterval: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>How often the client will announce itself to server. |
| [setHeartbeatNotificationOptions](set-heartbeat-notification-options.md) | [jvm]<br>fun [setHeartbeatNotificationOptions](set-heartbeat-notification-options.md)(heartbeatNotificationOptions: [PNHeartbeatNotificationOptions](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md)): [PNConfiguration](index.md)<br>Set Heartbeat notification options. |
| [setHostnameVerifier](set-hostname-verifier.md) | [jvm]<br>fun [setHostnameVerifier](set-hostname-verifier.md)(hostnameVerifier: [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)?): [PNConfiguration](index.md) |
| [setHttpLoggingInterceptor](set-http-logging-interceptor.md) | [jvm]<br>fun [setHttpLoggingInterceptor](set-http-logging-interceptor.md)(httpLoggingInterceptor: HttpLoggingInterceptor?): [PNConfiguration](index.md)<br>Sets a custom HttpLoggingInterceptor for logging network traffic. |
| [setIncludeInstanceIdentifier](set-include-instance-identifier.md) | [jvm]<br>fun [setIncludeInstanceIdentifier](set-include-instance-identifier.md)(includeInstanceIdentifier: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>Whether to include a PubNubCore.instanceId with every request. |
| [setIncludeRequestIdentifier](set-include-request-identifier.md) | [jvm]<br>fun [setIncludeRequestIdentifier](set-include-request-identifier.md)(includeRequestIdentifier: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>Whether to include a PubNubCore.requestId with every request. |
| [setLogVerbosity](set-log-verbosity.md) | [jvm]<br>fun [setLogVerbosity](set-log-verbosity.md)(logVerbosity: [PNLogVerbosity](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md)): [PNConfiguration](index.md)<br>Set to [PNLogVerbosity.BODY](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/-b-o-d-y/index.md) to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/-n-o-n-e/index.md). |
| [setMaintainPresenceState](set-maintain-presence-state.md) | [jvm]<br>fun [setMaintainPresenceState](set-maintain-presence-state.md)(maintainPresenceState: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState](../-pub-nub/set-presence-state.md) for the current [userId](user-id.md) with every automatic heartbeat (if [heartbeatInterval](heartbeat-interval.md) is greater than 0) and initial subscribe connection (also after e.g. loss of network). |
| [setManagePresenceListManually](set-manage-presence-list-manually.md) | [jvm]<br>fun [setManagePresenceListManually](set-manage-presence-list-manually.md)(managePresenceListManually: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>Enables explicit presence control. When set to true heartbeat calls will contain only channels and groups added explicitly using PubNubCore.presence. Should be used only with ACL set on the server side. For more information please contact PubNub support |
| [setMaximumConnections](set-maximum-connections.md) | [jvm]<br>fun [setMaximumConnections](set-maximum-connections.md)(maximumConnections: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?): [PNConfiguration](index.md) |
| [setMaximumMessagesCacheSize](set-maximum-messages-cache-size.md) | [jvm]<br>fun [setMaximumMessagesCacheSize](set-maximum-messages-cache-size.md)(maximumMessagesCacheSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md) |
| [setMaximumReconnectionRetries](set-maximum-reconnection-retries.md) | [jvm]<br>fun [setMaximumReconnectionRetries](set-maximum-reconnection-retries.md)(maximumReconnectionRetries: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md) |
| [setNonSubscribeRequestTimeout](set-non-subscribe-request-timeout.md) | [jvm]<br>fun [setNonSubscribeRequestTimeout](set-non-subscribe-request-timeout.md)(nonSubscribeRequestTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>For non subscribe operations (publish, herenow, etc), This property relates to a read timeout that is applied from the moment the connection between a client and the server has been successfully established. It defines a maximum time of inactivity between two data packets when waiting for the server’s response. |
| [setOrigin](set-origin.md) | [jvm]<br>fun [setOrigin](set-origin.md)(origin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfiguration](index.md)<br>Custom origin if needed. |
| [setPnsdkSuffixes](set-pnsdk-suffixes.md) | [jvm]<br>fun [setPnsdkSuffixes](set-pnsdk-suffixes.md)(pnSdkSuffixes: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;): [PNConfiguration](index.md) |
| [setPresenceTimeout](set-presence-timeout.md) | [jvm]<br>fun [setPresenceTimeout](set-presence-timeout.md)(presenceTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>Sets the custom presence server timeout. |
| [setPresenceTimeoutWithCustomInterval](set-presence-timeout-with-custom-interval.md) | [jvm]<br>fun [setPresenceTimeoutWithCustomInterval](set-presence-timeout-with-custom-interval.md)(timeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), interval: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>Set presence configurations for timeout and announce interval. |
| [setProxy](set-proxy.md) | [jvm]<br>fun [setProxy](set-proxy.md)(proxy: [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)?): [PNConfiguration](index.md)<br>Instructs the SDK to use a proxy configuration when communicating with PubNub servers. |
| [setProxyAuthenticator](set-proxy-authenticator.md) | [jvm]<br>fun [setProxyAuthenticator](set-proxy-authenticator.md)(proxyAuthenticator: Authenticator?): [PNConfiguration](index.md) |
| [setProxySelector](set-proxy-selector.md) | [jvm]<br>fun [setProxySelector](set-proxy-selector.md)(proxySelector: [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)?): [PNConfiguration](index.md) |
| [setPublishKey](set-publish-key.md) | [jvm]<br>fun [setPublishKey](set-publish-key.md)(publishKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfiguration](index.md)<br>The publish key from the admin panel (only required if publishing). |
| [setReconnectionPolicy](set-reconnection-policy.md) | [jvm]<br>fun [setReconnectionPolicy](set-reconnection-policy.md)(reconnectionPolicy: [PNReconnectionPolicy](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/index.md)): [PNConfiguration](index.md) |
| [setRetryConfiguration](set-retry-configuration.md) | [jvm]<br>fun [setRetryConfiguration](set-retry-configuration.md)(retryConfiguration: [RetryConfiguration](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md)): [PNConfiguration](index.md)<br>Retry configuration for requests. Defaults to [RetryConfiguration.None](../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/-none/index.md). |
| [setSecretKey](set-secret-key.md) | [jvm]<br>fun [setSecretKey](set-secret-key.md)(secretKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfiguration](index.md)<br>The secret key from the admin panel (only required for modifying/revealing access permissions). |
| [setSecure](set-secure.md) | [jvm]<br>fun [setSecure](set-secure.md)(secure: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>If set to `true`,  requests will be made over HTTPS. |
| [setSslSocketFactory](set-ssl-socket-factory.md) | [jvm]<br>fun [setSslSocketFactory](set-ssl-socket-factory.md)(sslSocketFactory: [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)?): [PNConfiguration](index.md) |
| [setSubscribeKey](set-subscribe-key.md) | [jvm]<br>fun [setSubscribeKey](set-subscribe-key.md)(subscribeKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [PNConfiguration](index.md)<br>The subscribe key from the admin panel. |
| [setSubscribeTimeout](set-subscribe-timeout.md) | [jvm]<br>fun [setSubscribeTimeout](set-subscribe-timeout.md)(subscribeTimeout: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [PNConfiguration](index.md)<br>The subscribe request timeout. |
| [setSuppressLeaveEvents](set-suppress-leave-events.md) | [jvm]<br>fun [setSuppressLeaveEvents](set-suppress-leave-events.md)(suppressLeaveEvents: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>When `true` the SDK doesn't send out the leave requests. |
| [setUseRandomInitializationVector](set-use-random-initialization-vector.md) | [jvm]<br>fun [setUseRandomInitializationVector](set-use-random-initialization-vector.md)(useRandomInitializationVector: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)<br>Should initialization vector for encrypted messages be random. |
| [setUserId](set-user-id.md) | [jvm]<br>fun [setUserId](set-user-id.md)(userId: [UserId](../../../../pubnub-gson/com.pubnub.api/-user-id/index.md)): [PNConfiguration](index.md) |
| [setUuid](set-uuid.md) | [jvm]<br>fun [setUuid](set-uuid.md)(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [setX509ExtendedTrustManager](set-x509-extended-trust-manager.md) | [jvm]<br>fun [setX509ExtendedTrustManager](set-x509-extended-trust-manager.md)(x509ExtendedTrustManager: [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)?): [PNConfiguration](index.md) |

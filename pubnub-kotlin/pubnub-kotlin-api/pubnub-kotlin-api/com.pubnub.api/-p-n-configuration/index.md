//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)

# PNConfiguration

[jvm]\
class [~~PNConfiguration~~](index.md)(var userId: [UserId](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md)) : [BasePNConfiguration](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration/index.md)---

### Deprecated

Use `com.pubnub.api.v2.PNConfiguration.builder` instead.

#### Replace with

```kotlin
com.pubnub.api.v2.PNConfiguration.builder(userId, subscribeKey)
```
---

## Constructors

| | |
|---|---|
| [PNConfiguration](-p-n-configuration.md) | [jvm]<br>constructor(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))constructor(userId: [UserId](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>open override var [authKey](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>If Access Manager is utilized, client will use this authKey in all restricted requests. |
| [cacheBusting](cache-busting.md) | [jvm]<br>open override var [cacheBusting](cache-busting.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If operating behind a misbehaving proxy, allow the client to shuffle the subdomains. |
| [certificatePinner](certificate-pinner.md) | [jvm]<br>open override var [certificatePinner](certificate-pinner.md): CertificatePinner? |
| [cipherKey](cipher-key.md) | [jvm]<br>var [~~cipherKey~~](cipher-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>If set, all communications to and from PubNub will be encrypted. |
| [connectionSpec](connection-spec.md) | [jvm]<br>open override var [connectionSpec](connection-spec.md): ConnectionSpec? |
| [connectTimeout](connect-timeout.md) | [jvm]<br>open override var [connectTimeout](connect-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>How long before the client gives up trying to connect with the server. |
| [cryptoModule](crypto-module.md) | [jvm]<br>open override var [cryptoModule](crypto-module.md): [CryptoModule](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)?<br>CryptoModule is responsible for handling encryption and decryption. If set, all communications to and from PubNub will be encrypted. |
| [dedupOnSubscribe](dedup-on-subscribe.md) | [jvm]<br>open override var [dedupOnSubscribe](dedup-on-subscribe.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md) | [jvm]<br>open override var [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>How many times publishing file message should automatically retry before marking the action as failed |
| [filterExpression](filter-expression.md) | [jvm]<br>open override var [filterExpression](filter-expression.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Feature to subscribe with a custom filter expression. |
| [googleAppEngineNetworking](google-app-engine-networking.md) | [jvm]<br>open override var [googleAppEngineNetworking](google-app-engine-networking.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Enable Google App Engine networking. |
| [heartbeatInterval](heartbeat-interval.md) | [jvm]<br>open override var [heartbeatInterval](heartbeat-interval.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>How often the client will announce itself to server. |
| [heartbeatNotificationOptions](heartbeat-notification-options.md) | [jvm]<br>open override var [heartbeatNotificationOptions](heartbeat-notification-options.md): [PNHeartbeatNotificationOptions](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md)<br>Set Heartbeat notification options. |
| [hostnameVerifier](hostname-verifier.md) | [jvm]<br>open override var [hostnameVerifier](hostname-verifier.md): [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)? |
| [httpLoggingInterceptor](http-logging-interceptor.md) | [jvm]<br>open override var [httpLoggingInterceptor](http-logging-interceptor.md): HttpLoggingInterceptor?<br>Sets a custom HttpLoggingInterceptor for logging network traffic. |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>open override var [includeInstanceIdentifier](include-instance-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether to include a instanceId with every request. |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>open override var [includeRequestIdentifier](include-request-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether to include a requestId with every request. |
| [logVerbosity](log-verbosity.md) | [jvm]<br>open override var [logVerbosity](log-verbosity.md): [PNLogVerbosity](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md)<br>Set to [PNLogVerbosity.BODY](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/-b-o-d-y/index.md) to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/-n-o-n-e/index.md). |
| [maintainPresenceState](maintain-presence-state.md) | [jvm]<br>open override var [maintainPresenceState](maintain-presence-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState](../-pub-nub/set-presence-state.md) for the current [userId](user-id.md) with every automatic heartbeat (if [heartbeatInterval](heartbeat-interval.md) is greater than 0) and initial subscribe connection (also after e.g. loss of network). |
| [managePresenceListManually](manage-presence-list-manually.md) | [jvm]<br>open override var [managePresenceListManually](manage-presence-list-manually.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [maximumConnections](maximum-connections.md) | [jvm]<br>open override var [maximumConnections](maximum-connections.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [maximumMessagesCacheSize](maximum-messages-cache-size.md) | [jvm]<br>open override var [maximumMessagesCacheSize](maximum-messages-cache-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [maximumReconnectionRetries](maximum-reconnection-retries.md) | [jvm]<br>var [~~maximumReconnectionRetries~~](maximum-reconnection-retries.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Sets how many times to retry to reconnect before giving up. Must be used in combination with [reconnectionPolicy](reconnection-policy.md). |
| [nonSubscribeReadTimeout](non-subscribe-read-timeout.md) | [jvm]<br>open override var [nonSubscribeReadTimeout](non-subscribe-read-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md) | [jvm]<br>open override var [~~nonSubscribeRequestTimeout~~](non-subscribe-request-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [origin](origin.md) | [jvm]<br>open override var [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Custom origin if needed. |
| [pnsdkSuffixes](pnsdk-suffixes.md) | [jvm]<br>open override val [pnsdkSuffixes](pnsdk-suffixes.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [presenceTimeout](presence-timeout.md) | [jvm]<br>open override var [presenceTimeout](presence-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Sets the custom presence server timeout. |
| [proxy](proxy.md) | [jvm]<br>open override var [proxy](proxy.md): [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)?<br>Instructs the SDK to use a proxy configuration when communicating with PubNub servers. |
| [proxyAuthenticator](proxy-authenticator.md) | [jvm]<br>open override var [proxyAuthenticator](proxy-authenticator.md): Authenticator? |
| [proxySelector](proxy-selector.md) | [jvm]<br>open override var [proxySelector](proxy-selector.md): [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)? |
| [publishKey](publish-key.md) | [jvm]<br>open override var [publishKey](publish-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The publish key from the admin panel (only required if publishing). |
| [reconnectionPolicy](reconnection-policy.md) | [jvm]<br>var [~~reconnectionPolicy~~](reconnection-policy.md): [PNReconnectionPolicy](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/index.md)<br>Set to [PNReconnectionPolicy.LINEAR](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r/index.md) for automatic reconnects. |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>open override var [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](secret-key.md) | [jvm]<br>open override var [secretKey](secret-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The secret key from the admin panel (only required for modifying/revealing access permissions). |
| [secure](secure.md) | [jvm]<br>open override var [secure](secure.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If set to `true`,  requests will be made over HTTPS. |
| [sslSocketFactory](ssl-socket-factory.md) | [jvm]<br>open override var [sslSocketFactory](ssl-socket-factory.md): [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)? |
| [subscribeKey](subscribe-key.md) | [jvm]<br>open override var [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The subscribe key from the admin panel. |
| [subscribeTimeout](subscribe-timeout.md) | [jvm]<br>open override var [subscribeTimeout](subscribe-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The subscribe request timeout. |
| [suppressLeaveEvents](suppress-leave-events.md) | [jvm]<br>open override var [suppressLeaveEvents](suppress-leave-events.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>When `true` the SDK doesn't send out the leave requests. |
| [useRandomInitializationVector](use-random-initialization-vector.md) | [jvm]<br>var [~~useRandomInitializationVector~~](use-random-initialization-vector.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Should initialization vector for encrypted messages be random. |
| [userId](user-id.md) | [jvm]<br>open override var [userId](user-id.md): [UserId](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md) |
| [uuid](uuid.md) | [jvm]<br>open override var [~~uuid~~](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](x509-extended-trust-manager.md) | [jvm]<br>open override var [x509ExtendedTrustManager](x509-extended-trust-manager.md): [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)? |

## Functions

| Name | Summary |
|---|---|
| [addPnsdkSuffix](add-pnsdk-suffix.md) | [jvm]<br>fun [~~addPnsdkSuffix~~](add-pnsdk-suffix.md)(vararg nameToSuffixes: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;)<br>fun [~~addPnsdkSuffix~~](add-pnsdk-suffix.md)(nameToSuffixes: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;) |

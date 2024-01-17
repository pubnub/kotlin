//[pubnub-kotlin](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)

# PNConfiguration

[jvm]\
open class [PNConfiguration](index.md)(var userId: [UserId](../-user-id/index.md))

A storage for user-provided information which describe further PubNub client behaviour. Configuration instance contains additional set of properties which allow performing precise PubNub client configuration.

## Constructors

| | |
|---|---|
| [PNConfiguration](-p-n-configuration.md) | [jvm]<br>~~fun~~ [~~PNConfiguration~~](-p-n-configuration.md)~~(~~uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), enableEventEngine: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false~~)~~ |
| [PNConfiguration](-p-n-configuration.md) | [jvm]<br>fun [PNConfiguration](-p-n-configuration.md)(userId: [UserId](../-user-id/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addPnsdkSuffix](add-pnsdk-suffix.md) | [jvm]<br>~~fun~~ [~~addPnsdkSuffix~~](add-pnsdk-suffix.md)~~(~~vararg nameToSuffixes: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;~~)~~<br>~~fun~~ [~~addPnsdkSuffix~~](add-pnsdk-suffix.md)~~(~~nameToSuffixes: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;~~)~~ |

## Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>var [authKey](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>If Access Manager is utilized, client will use this authKey in all restricted requests. |
| [cacheBusting](cache-busting.md) | [jvm]<br>var [cacheBusting](cache-busting.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>If operating behind a misbehaving proxy, allow the client to shuffle the subdomains. |
| [certificatePinner](certificate-pinner.md) | [jvm]<br>var [certificatePinner](certificate-pinner.md): CertificatePinner? = null |
| [cipherKey](cipher-key.md) | [jvm]<br>~~var~~ [~~cipherKey~~](cipher-key.md)~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>If set, all communications to and from PubNub will be encrypted. |
| [connectionSpec](connection-spec.md) | [jvm]<br>var [connectionSpec](connection-spec.md): ConnectionSpec? = null |
| [connectTimeout](connect-timeout.md) | [jvm]<br>var [connectTimeout](connect-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>How long before the client gives up trying to connect with a subscribe call. |
| [cryptoModule](crypto-module.md) | [jvm]<br>var [cryptoModule](crypto-module.md): [CryptoModule](../../com.pubnub.api.crypto/-crypto-module/index.md)? = null<br>CryptoModule is responsible for handling encryption and decryption. If set, all communications to and from PubNub will be encrypted. |
| [dedupOnSubscribe](dedup-on-subscribe.md) | [jvm]<br>var [dedupOnSubscribe](dedup-on-subscribe.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [enableEventEngine](enable-event-engine.md) | [jvm]<br>var [enableEventEngine](enable-event-engine.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>This controls whether to enable a new, experimental implementation of Subscription and Presence handling. |
| [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md) | [jvm]<br>var [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 5<br>How many times publishing file message should automatically retry before marking the action as failed |
| [filterExpression](filter-expression.md) | [jvm]<br>var [filterExpression](filter-expression.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Feature to subscribe with a custom filter expression. |
| [googleAppEngineNetworking](google-app-engine-networking.md) | [jvm]<br>var [googleAppEngineNetworking](google-app-engine-networking.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Enable Google App Engine networking. |
| [heartbeatInterval](heartbeat-interval.md) | [jvm]<br>var [heartbeatInterval](heartbeat-interval.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0<br>How often the client will announce itself to server. |
| [heartbeatNotificationOptions](heartbeat-notification-options.md) | [jvm]<br>var [heartbeatNotificationOptions](heartbeat-notification-options.md): [PNHeartbeatNotificationOptions](../../com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md)<br>Set Heartbeat notification options. |
| [hostnameVerifier](hostname-verifier.md) | [jvm]<br>var [hostnameVerifier](hostname-verifier.md): [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)? = null |
| [httpLoggingInterceptor](http-logging-interceptor.md) | [jvm]<br>var [httpLoggingInterceptor](http-logging-interceptor.md): HttpLoggingInterceptor? = null<br>Sets a custom HttpLoggingInterceptor for logging network traffic. |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>var [includeInstanceIdentifier](include-instance-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>Whether to include a [PubNub.instanceId](../-pub-nub/instance-id.md) with every request. |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>var [includeRequestIdentifier](include-request-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true<br>Whether to include a PubNub.requestId with every request. |
| [logVerbosity](log-verbosity.md) | [jvm]<br>var [logVerbosity](log-verbosity.md): [PNLogVerbosity](../../com.pubnub.api.enums/-p-n-log-verbosity/index.md)<br>Set to [PNLogVerbosity.BODY](../../com.pubnub.api.enums/-p-n-log-verbosity/-b-o-d-y/index.md) to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE](../../com.pubnub.api.enums/-p-n-log-verbosity/-n-o-n-e/index.md). |
| [maintainPresenceState](maintain-presence-state.md) | [jvm]<br>var [maintainPresenceState](maintain-presence-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true<br>When `true` the SDK will resend the last channel state that was set using [PubNub.setPresenceState](../-pub-nub/set-presence-state.md) for the current [userId](user-id.md) with every automatic heartbeat (if [heartbeatInterval](heartbeat-interval.md) is greater than 0) and initial subscribe connection (also after e.g. loss of network). |
| [maximumConnections](maximum-connections.md) | [jvm]<br>var [maximumConnections](maximum-connections.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null |
| [maximumMessagesCacheSize](maximum-messages-cache-size.md) | [jvm]<br>var [maximumMessagesCacheSize](maximum-messages-cache-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [maximumReconnectionRetries](maximum-reconnection-retries.md) | [jvm]<br>~~var~~ [~~maximumReconnectionRetries~~](maximum-reconnection-retries.md)~~:~~ [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Sets how many times to retry to reconnect before giving up. Must be used in combination with [reconnectionPolicy](reconnection-policy.md). |
| [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md) | [jvm]<br>var [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>For non subscribe operations (publish, herenow, etc), how long to wait to connect to PubNub before giving up with a connection timeout error. |
| [origin](origin.md) | [jvm]<br>var [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Custom origin if needed. |
| [presenceTimeout](presence-timeout.md) | [jvm]<br>var [presenceTimeout](presence-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Sets the custom presence server timeout. |
| [proxy](proxy.md) | [jvm]<br>var [proxy](proxy.md): [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)? = null<br>Instructs the SDK to use a proxy configuration when communicating with PubNub servers. |
| [proxyAuthenticator](proxy-authenticator.md) | [jvm]<br>var [proxyAuthenticator](proxy-authenticator.md): Authenticator? = null |
| [proxySelector](proxy-selector.md) | [jvm]<br>var [proxySelector](proxy-selector.md): [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)? = null |
| [publishKey](publish-key.md) | [jvm]<br>var [publishKey](publish-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The publish key from the admin panel (only required if publishing). |
| [reconnectionPolicy](reconnection-policy.md) | [jvm]<br>~~var~~ [~~reconnectionPolicy~~](reconnection-policy.md)~~:~~ [PNReconnectionPolicy](../../com.pubnub.api.enums/-p-n-reconnection-policy/index.md)<br>Set to [PNReconnectionPolicy.LINEAR](../../com.pubnub.api.enums/-p-n-reconnection-policy/-l-i-n-e-a-r/index.md) for automatic reconnects. |
| [requestMessageCountThreshold](request-message-count-threshold.md) | [jvm]<br>var [requestMessageCountThreshold](request-message-count-threshold.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? = null<br>If the number of messages into the payload is above this value, |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>var [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../com.pubnub.api.retry/-retry-configuration/index.md)<br>Retry configuration for requests. Defaults to [RetryConfiguration.None](../../com.pubnub.api.retry/-retry-configuration/-none/index.md). |
| [secretKey](secret-key.md) | [jvm]<br>var [secretKey](secret-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The secret key from the admin panel (only required for modifying/revealing access permissions). |
| [secure](secure.md) | [jvm]<br>var [secure](secure.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true<br>If set to `true`,  requests will be made over HTTPS. |
| [sslSocketFactory](ssl-socket-factory.md) | [jvm]<br>var [sslSocketFactory](ssl-socket-factory.md): [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)? = null |
| [startSubscriberThread](start-subscriber-thread.md) | [jvm]<br>var [startSubscriberThread](start-subscriber-thread.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true<br>Whether to start a separate subscriber thread when creating the instance. |
| [subscribeKey](subscribe-key.md) | [jvm]<br>var [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The subscribe key from the admin panel. |
| [subscribeTimeout](subscribe-timeout.md) | [jvm]<br>var [subscribeTimeout](subscribe-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The subscribe request timeout. |
| [suppressLeaveEvents](suppress-leave-events.md) | [jvm]<br>var [suppressLeaveEvents](suppress-leave-events.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>When `true` the SDK doesn't send out the leave requests. |
| [useRandomInitializationVector](use-random-initialization-vector.md) | [jvm]<br>~~var~~ [~~useRandomInitializationVector~~](use-random-initialization-vector.md)~~:~~ [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) ~~=~~ ~~true~~<br>Should initialization vector for encrypted messages be random. |
| [userId](user-id.md) | [jvm]<br>var [userId](user-id.md): [UserId](../-user-id/index.md) |
| [uuid](uuid.md) | [jvm]<br>~~var~~ [~~uuid~~](uuid.md)~~:~~ [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](x509-extended-trust-manager.md) | [jvm]<br>var [x509ExtendedTrustManager](x509-extended-trust-manager.md): [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)? = null |

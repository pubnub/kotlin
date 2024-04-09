//[pubnub-kotlin-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)

# Builder

[jvm]\
interface [Builder](index.md) : [BasePNConfiguration.Builder](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration/-builder/index.md)

## Properties

| Name | Summary |
|---|---|
| [authKey](auth-key.md) | [jvm]<br>abstract override var [authKey](auth-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [cacheBusting](cache-busting.md) | [jvm]<br>abstract override var [cacheBusting](cache-busting.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [certificatePinner](certificate-pinner.md) | [jvm]<br>abstract override var [certificatePinner](certificate-pinner.md): CertificatePinner? |
| [connectionSpec](connection-spec.md) | [jvm]<br>abstract override var [connectionSpec](connection-spec.md): ConnectionSpec? |
| [connectTimeout](connect-timeout.md) | [jvm]<br>abstract override var [connectTimeout](connect-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [cryptoModule](crypto-module.md) | [jvm]<br>abstract override var [cryptoModule](crypto-module.md): [CryptoModule](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)? |
| [dedupOnSubscribe](dedup-on-subscribe.md) | [jvm]<br>abstract override var [dedupOnSubscribe](dedup-on-subscribe.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md) | [jvm]<br>abstract override var [fileMessagePublishRetryLimit](file-message-publish-retry-limit.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [filterExpression](filter-expression.md) | [jvm]<br>abstract override var [filterExpression](filter-expression.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [googleAppEngineNetworking](google-app-engine-networking.md) | [jvm]<br>abstract override var [googleAppEngineNetworking](google-app-engine-networking.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [heartbeatInterval](heartbeat-interval.md) | [jvm]<br>abstract override var [heartbeatInterval](heartbeat-interval.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [heartbeatNotificationOptions](heartbeat-notification-options.md) | [jvm]<br>abstract override var [heartbeatNotificationOptions](heartbeat-notification-options.md): [PNHeartbeatNotificationOptions](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md) |
| [hostnameVerifier](hostname-verifier.md) | [jvm]<br>abstract override var [hostnameVerifier](hostname-verifier.md): [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)? |
| [httpLoggingInterceptor](http-logging-interceptor.md) | [jvm]<br>abstract override var [httpLoggingInterceptor](http-logging-interceptor.md): HttpLoggingInterceptor? |
| [includeInstanceIdentifier](include-instance-identifier.md) | [jvm]<br>abstract override var [includeInstanceIdentifier](include-instance-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](include-request-identifier.md) | [jvm]<br>abstract override var [includeRequestIdentifier](include-request-identifier.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [logVerbosity](log-verbosity.md) | [jvm]<br>abstract override var [logVerbosity](log-verbosity.md): [PNLogVerbosity](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md) |
| [maintainPresenceState](maintain-presence-state.md) | [jvm]<br>abstract override var [maintainPresenceState](maintain-presence-state.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [managePresenceListManually](manage-presence-list-manually.md) | [jvm]<br>abstract override var [managePresenceListManually](manage-presence-list-manually.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [maximumConnections](maximum-connections.md) | [jvm]<br>abstract override var [maximumConnections](maximum-connections.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [maximumMessagesCacheSize](maximum-messages-cache-size.md) | [jvm]<br>abstract override var [maximumMessagesCacheSize](maximum-messages-cache-size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeReadTimeout](non-subscribe-read-timeout.md) | [jvm]<br>abstract override var [nonSubscribeReadTimeout](non-subscribe-read-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeRequestTimeout](non-subscribe-request-timeout.md) | [jvm]<br>open override var [~~nonSubscribeRequestTimeout~~](non-subscribe-request-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [origin](origin.md) | [jvm]<br>abstract override var [origin](origin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [pnsdkSuffixes](pnsdk-suffixes.md) | [jvm]<br>abstract override var [pnsdkSuffixes](pnsdk-suffixes.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [presenceTimeout](presence-timeout.md) | [jvm]<br>abstract override var [presenceTimeout](presence-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [proxy](proxy.md) | [jvm]<br>abstract override var [proxy](proxy.md): [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)? |
| [proxyAuthenticator](proxy-authenticator.md) | [jvm]<br>abstract override var [proxyAuthenticator](proxy-authenticator.md): Authenticator? |
| [proxySelector](proxy-selector.md) | [jvm]<br>abstract override var [proxySelector](proxy-selector.md): [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)? |
| [publishKey](publish-key.md) | [jvm]<br>abstract override var [publishKey](publish-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [retryConfiguration](retry-configuration.md) | [jvm]<br>abstract override var [retryConfiguration](retry-configuration.md): [RetryConfiguration](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](secret-key.md) | [jvm]<br>abstract override var [secretKey](secret-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [secure](secure.md) | [jvm]<br>abstract override var [secure](secure.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sslSocketFactory](ssl-socket-factory.md) | [jvm]<br>abstract override var [sslSocketFactory](ssl-socket-factory.md): [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)? |
| [subscribeKey](subscribe-key.md) | [jvm]<br>abstract override var [subscribeKey](subscribe-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeTimeout](subscribe-timeout.md) | [jvm]<br>abstract override var [subscribeTimeout](subscribe-timeout.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [suppressLeaveEvents](suppress-leave-events.md) | [jvm]<br>abstract override var [suppressLeaveEvents](suppress-leave-events.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [userId](user-id.md) | [jvm]<br>abstract override var [userId](user-id.md): [UserId](../../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md) |
| [x509ExtendedTrustManager](x509-extended-trust-manager.md) | [jvm]<br>abstract override var [x509ExtendedTrustManager](x509-extended-trust-manager.md): [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)? |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>abstract fun [build](build.md)(): [PNConfiguration](../index.md)<br>Create a [PNConfiguration](../index.md) object with values from this builder. |

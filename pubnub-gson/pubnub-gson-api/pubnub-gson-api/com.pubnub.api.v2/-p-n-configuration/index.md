//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.v2](../index.md)/[PNConfiguration](index.md)

# PNConfiguration

[jvm]\
interface [PNConfiguration](index.md) : [BasePNConfiguration](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration/index.md), [PNConfigurationOverride](../-p-n-configuration-override/index.md)

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>interface [Builder](-builder/index.md) : [BasePNConfiguration.Builder](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.v2/-base-p-n-configuration/-builder/index.md), [PNConfigurationOverride.Builder](../-p-n-configuration-override/-builder/index.md) |
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [authKey](index.md#53209962%2FProperties%2F126356644) | [jvm]<br>abstract val [authKey](index.md#53209962%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [cacheBusting](index.md#45886669%2FProperties%2F126356644) | [jvm]<br>abstract val [cacheBusting](index.md#45886669%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [certificatePinner](index.md#-647701404%2FProperties%2F126356644) | [jvm]<br>abstract val [certificatePinner](index.md#-647701404%2FProperties%2F126356644): CertificatePinner? |
| [connectionSpec](index.md#1545112128%2FProperties%2F126356644) | [jvm]<br>abstract val [connectionSpec](index.md#1545112128%2FProperties%2F126356644): ConnectionSpec? |
| [connectTimeout](index.md#1565428386%2FProperties%2F126356644) | [jvm]<br>abstract val [connectTimeout](index.md#1565428386%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [cryptoModule](index.md#-1038871316%2FProperties%2F126356644) | [jvm]<br>abstract val [cryptoModule](index.md#-1038871316%2FProperties%2F126356644): [CryptoModule](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.crypto/-crypto-module/index.md)? |
| [dedupOnSubscribe](index.md#1819763724%2FProperties%2F126356644) | [jvm]<br>abstract val [dedupOnSubscribe](index.md#1819763724%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [fileMessagePublishRetryLimit](index.md#-1556855294%2FProperties%2F126356644) | [jvm]<br>abstract val [fileMessagePublishRetryLimit](index.md#-1556855294%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [filterExpression](index.md#1227005609%2FProperties%2F126356644) | [jvm]<br>abstract val [filterExpression](index.md#1227005609%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [googleAppEngineNetworking](index.md#-173071421%2FProperties%2F126356644) | [jvm]<br>abstract val [googleAppEngineNetworking](index.md#-173071421%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [heartbeatInterval](index.md#1038438240%2FProperties%2F126356644) | [jvm]<br>abstract val [heartbeatInterval](index.md#1038438240%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [heartbeatNotificationOptions](index.md#1030972962%2FProperties%2F126356644) | [jvm]<br>abstract val [heartbeatNotificationOptions](index.md#1030972962%2FProperties%2F126356644): [PNHeartbeatNotificationOptions](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-heartbeat-notification-options/index.md) |
| [hostnameVerifier](index.md#1966514384%2FProperties%2F126356644) | [jvm]<br>abstract val [hostnameVerifier](index.md#1966514384%2FProperties%2F126356644): [HostnameVerifier](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html)? |
| [httpLoggingInterceptor](index.md#-122610773%2FProperties%2F126356644) | [jvm]<br>abstract val [httpLoggingInterceptor](index.md#-122610773%2FProperties%2F126356644): HttpLoggingInterceptor? |
| [includeInstanceIdentifier](index.md#-1426199813%2FProperties%2F126356644) | [jvm]<br>abstract val [includeInstanceIdentifier](index.md#-1426199813%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [includeRequestIdentifier](index.md#1858164233%2FProperties%2F126356644) | [jvm]<br>abstract val [includeRequestIdentifier](index.md#1858164233%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [logVerbosity](index.md#-1490047726%2FProperties%2F126356644) | [jvm]<br>abstract val [logVerbosity](index.md#-1490047726%2FProperties%2F126356644): [PNLogVerbosity](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md) |
| [maintainPresenceState](index.md#1913241078%2FProperties%2F126356644) | [jvm]<br>abstract val [maintainPresenceState](index.md#1913241078%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [managePresenceListManually](index.md#-1014508856%2FProperties%2F126356644) | [jvm]<br>abstract val [managePresenceListManually](index.md#-1014508856%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [maximumConnections](index.md#-1205146524%2FProperties%2F126356644) | [jvm]<br>abstract val [maximumConnections](index.md#-1205146524%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [maximumMessagesCacheSize](index.md#-1418706878%2FProperties%2F126356644) | [jvm]<br>abstract val [maximumMessagesCacheSize](index.md#-1418706878%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeReadTimeout](index.md#-827015885%2FProperties%2F126356644) | [jvm]<br>abstract val [nonSubscribeReadTimeout](index.md#-827015885%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [nonSubscribeRequestTimeout](index.md#202498698%2FProperties%2F126356644) | [jvm]<br>open val [~~nonSubscribeRequestTimeout~~](index.md#202498698%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [origin](index.md#-990263437%2FProperties%2F126356644) | [jvm]<br>abstract val [origin](index.md#-990263437%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [pnsdkSuffixes](index.md#-238847194%2FProperties%2F126356644) | [jvm]<br>abstract val [pnsdkSuffixes](index.md#-238847194%2FProperties%2F126356644): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [presenceTimeout](index.md#-1639993861%2FProperties%2F126356644) | [jvm]<br>abstract val [presenceTimeout](index.md#-1639993861%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [proxy](index.md#-1653587597%2FProperties%2F126356644) | [jvm]<br>abstract val [proxy](index.md#-1653587597%2FProperties%2F126356644): [Proxy](https://docs.oracle.com/javase/8/docs/api/java/net/Proxy.html)? |
| [proxyAuthenticator](index.md#-1422447660%2FProperties%2F126356644) | [jvm]<br>abstract val [proxyAuthenticator](index.md#-1422447660%2FProperties%2F126356644): Authenticator? |
| [proxySelector](index.md#1241743444%2FProperties%2F126356644) | [jvm]<br>abstract val [proxySelector](index.md#1241743444%2FProperties%2F126356644): [ProxySelector](https://docs.oracle.com/javase/8/docs/api/java/net/ProxySelector.html)? |
| [publishKey](index.md#-2026020599%2FProperties%2F126356644) | [jvm]<br>abstract val [publishKey](index.md#-2026020599%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [retryConfiguration](index.md#112800363%2FProperties%2F126356644) | [jvm]<br>abstract val [retryConfiguration](index.md#112800363%2FProperties%2F126356644): [RetryConfiguration](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api.retry/-retry-configuration/index.md) |
| [secretKey](index.md#-398936206%2FProperties%2F126356644) | [jvm]<br>abstract val [secretKey](index.md#-398936206%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [secure](index.md#916252738%2FProperties%2F126356644) | [jvm]<br>abstract val [secure](index.md#916252738%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sslSocketFactory](index.md#-1214590002%2FProperties%2F126356644) | [jvm]<br>abstract val [sslSocketFactory](index.md#-1214590002%2FProperties%2F126356644): [SSLSocketFactory](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/SSLSocketFactory.html)? |
| [subscribeKey](index.md#-1947520060%2FProperties%2F126356644) | [jvm]<br>abstract val [subscribeKey](index.md#-1947520060%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [subscribeTimeout](index.md#217110530%2FProperties%2F126356644) | [jvm]<br>abstract val [subscribeTimeout](index.md#217110530%2FProperties%2F126356644): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [suppressLeaveEvents](index.md#2053613766%2FProperties%2F126356644) | [jvm]<br>abstract val [suppressLeaveEvents](index.md#2053613766%2FProperties%2F126356644): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [userId](index.md#-1170764717%2FProperties%2F126356644) | [jvm]<br>abstract val [userId](index.md#-1170764717%2FProperties%2F126356644): [UserId](../../../../../pubnub-core/pubnub-core-api/pubnub-core-api/com.pubnub.api/-user-id/index.md) |
| [uuid](index.md#-473515138%2FProperties%2F126356644) | [jvm]<br>open val [~~uuid~~](index.md#-473515138%2FProperties%2F126356644): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [x509ExtendedTrustManager](index.md#-1970140507%2FProperties%2F126356644) | [jvm]<br>abstract val [x509ExtendedTrustManager](index.md#-1970140507%2FProperties%2F126356644): [X509ExtendedTrustManager](https://docs.oracle.com/javase/8/docs/api/javax/net/ssl/X509ExtendedTrustManager.html)? |

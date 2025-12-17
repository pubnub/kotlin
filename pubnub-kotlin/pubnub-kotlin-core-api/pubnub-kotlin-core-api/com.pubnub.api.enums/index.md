//[pubnub-kotlin-core-api](../../index.md)/[com.pubnub.api.enums](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [LogLevel](-log-level/index.md) | [common]<br>expect class [LogLevel](-log-level/index.md)<br>Platform-specific log level configuration.<br>[apple]<br>actual data class [LogLevel](-log-level/index.md)(val levels: [Set](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-set/index.html)&lt;[LogLevel.Level](-log-level/-level/index.md)&gt;)<br>Swift SDK log level configuration.<br>[js]<br>actual class [LogLevel](-log-level/index.md)<br>JavaScript SDK log level.<br>[jvm]<br>actual class [LogLevel](-log-level/index.md)<br>JVM LogLevel (ignored - use slf4j configuration instead). |
| [PNHeartbeatNotificationOptions](-p-n-heartbeat-notification-options/index.md) | [common]<br>enum [PNHeartbeatNotificationOptions](-p-n-heartbeat-notification-options/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PNHeartbeatNotificationOptions](-p-n-heartbeat-notification-options/index.md)&gt; |
| [PNLogVerbosity](-p-n-log-verbosity/index.md) | [common]<br>enum [PNLogVerbosity](-p-n-log-verbosity/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PNLogVerbosity](-p-n-log-verbosity/index.md)&gt; |
| [PNOperationType](-p-n-operation-type/index.md) | [common]<br>sealed class [PNOperationType](-p-n-operation-type/index.md) |
| [PNPushEnvironment](-p-n-push-environment/index.md) | [common]<br>enum [PNPushEnvironment](-p-n-push-environment/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PNPushEnvironment](-p-n-push-environment/index.md)&gt; |
| [PNPushType](-p-n-push-type/index.md) | [common]<br>enum [PNPushType](-p-n-push-type/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PNPushType](-p-n-push-type/index.md)&gt; |
| [PNStatusCategory](-p-n-status-category/index.md) | [common]<br>enum [PNStatusCategory](-p-n-status-category/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[PNStatusCategory](-p-n-status-category/index.md)&gt; <br>Check the status category via [PNStatus.category](../com.pubnub.api.models.consumer/-p-n-status/category.md) in the com.pubnub.api.v2.callbacks.StatusListener added to a com.pubnub.api.PubNub object. |

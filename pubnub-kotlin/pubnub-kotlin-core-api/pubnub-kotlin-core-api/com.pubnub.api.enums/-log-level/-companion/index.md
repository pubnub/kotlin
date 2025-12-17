//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.enums](../../index.md)/[LogLevel](../index.md)/[Companion](index.md)

# Companion

[common]\
expect object [Companion](index.md)

[apple, js, jvm]\
actual object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [All](-all.md) | [apple]<br>val [All](-all.md): [LogLevel](../index.md)<br>Everything including trace. Warning: Logs sensitive information. Never use in production. |
| [DEBUG](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[jvm]-d-e-b-u-g.md) | [js]<br>val [DEBUG]([js]-d-e-b-u-g.md): [LogLevel](../index.md)<br>Logs debug information and all higher severity levels. Includes: DEBUG, INFO, WARN, ERROR<br>[jvm]<br>val [DEBUG]([jvm]-d-e-b-u-g.md): [LogLevel](../index.md) |
| [Debug](-debug.md) | [apple]<br>val [Debug](-debug.md): [LogLevel](../index.md)<br>Debug and above (excludes TRACE). Warning: May log sensitive information. |
| [DEFAULT](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[js]-d-e-f-a-u-l-t.md) | [apple]<br>val [DEFAULT]([apple]-d-e-f-a-u-l-t.md): [LogLevel](../index.md)<br>Default log level (logging disabled)<br>[js]<br>val [DEFAULT]([js]-d-e-f-a-u-l-t.md): [LogLevel](../index.md) |
| [ERROR](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[jvm]-e-r-r-o-r.md) | [js]<br>val [ERROR]([js]-e-r-r-o-r.md): [LogLevel](../index.md)<br>Logs only error messages. Most restrictive level.<br>[jvm]<br>val [ERROR]([jvm]-e-r-r-o-r.md): [LogLevel](../index.md) |
| [Error](-error.md) | [apple]<br>val [Error](-error.md): [LogLevel](../index.md)<br>Only errors |
| [INFO](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[jvm]-i-n-f-o.md) | [js]<br>val [INFO]([js]-i-n-f-o.md): [LogLevel](../index.md)<br>Logs informational messages, warnings, and errors. Includes: INFO, WARN, ERROR<br>[jvm]<br>val [INFO]([jvm]-i-n-f-o.md): [LogLevel](../index.md) |
| [Info](-info.md) | [apple]<br>val [Info](-info.md): [LogLevel](../index.md)<br>Info, event, warnings, and errors |
| [NONE](-n-o-n-e.md) | [common]<br>expect val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging disabled (default)<br>[apple]<br>actual val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>[js]<br>actual val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging is disabled. Default setting.<br>[jvm]<br>actual val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging disabled (JVM: configure via slf4j) |
| [None](-none.md) | [apple]<br>val [None](-none.md): [LogLevel](../index.md)<br>Logging disabled |
| [Standard](-standard.md) | [apple]<br>val [Standard](-standard.md): [LogLevel](../index.md)<br>Production logging: info, event, warn, error. Recommended for production environments. |
| [TRACE](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[jvm]-t-r-a-c-e.md) | [js]<br>val [TRACE]([js]-t-r-a-c-e.md): [LogLevel](../index.md)<br>Most verbose logging level. Logs all messages including internal traces. Includes: TRACE, DEBUG, INFO, WARN, ERROR<br>[jvm]<br>val [TRACE]([jvm]-t-r-a-c-e.md): [LogLevel](../index.md) |
| [WARN](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[jvm]-w-a-r-n.md) | [js]<br>val [WARN]([js]-w-a-r-n.md): [LogLevel](../index.md)<br>Logs warnings and errors. Includes: WARN, ERROR<br>[jvm]<br>val [WARN]([jvm]-w-a-r-n.md): [LogLevel](../index.md) |
| [Warn](-warn.md) | [apple]<br>val [Warn](-warn.md): [LogLevel](../index.md)<br>Warnings and errors |

## Functions

| Name | Summary |
|---|---|
| [custom](custom.md) | [apple]<br>fun [custom](custom.md)(vararg levels: [LogLevel.Level](../-level/index.md)): [LogLevel](../index.md)<br>Create custom combination of log levels. |

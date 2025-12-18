//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.enums](../../index.md)/[LogLevel](../index.md)/[Companion](index.md)

# Companion

[common]\
expect object [Companion](index.md)

[apple, js, jvm]\
actual object [Companion](index.md)

## Properties

| Name | Summary |
|---|---|
| [All](-all.md) | [apple]<br>val [All](-all.md): [LogLevel](../index.md)<br>All log levels |
| [DEBUG](-d-e-b-u-g.md) | [common]<br>expect val [DEBUG](-d-e-b-u-g.md): [LogLevel](../index.md)<br>Logs debug information and all higher severity levels<br>[apple]<br>actual val [DEBUG](-d-e-b-u-g.md): [LogLevel](../index.md)<br>Debug and above (excludes TRACE). Warning: May log sensitive information.<br>[js]<br>actual val [DEBUG](-d-e-b-u-g.md): [LogLevel](../index.md)<br>Logs debug information and all higher severity levels. Includes: DEBUG, INFO, WARN, ERROR<br>[jvm]<br>actual val [DEBUG](-d-e-b-u-g.md): [LogLevel](../index.md) |
| [DEFAULT](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-log-level/-companion/[js]-d-e-f-a-u-l-t.md) | [apple]<br>val [DEFAULT]([apple]-d-e-f-a-u-l-t.md): [LogLevel](../index.md)<br>Default log level (logging disabled)<br>[js]<br>val [DEFAULT]([js]-d-e-f-a-u-l-t.md): [LogLevel](../index.md) |
| [ERROR](-e-r-r-o-r.md) | [common]<br>expect val [ERROR](-e-r-r-o-r.md): [LogLevel](../index.md)<br>Logs only error messages<br>[apple]<br>actual val [ERROR](-e-r-r-o-r.md): [LogLevel](../index.md)<br>Only errors<br>[js]<br>actual val [ERROR](-e-r-r-o-r.md): [LogLevel](../index.md)<br>Logs only error messages. Most restrictive level.<br>[jvm]<br>actual val [ERROR](-e-r-r-o-r.md): [LogLevel](../index.md) |
| [INFO](-i-n-f-o.md) | [common]<br>expect val [INFO](-i-n-f-o.md): [LogLevel](../index.md)<br>Logs informational messages, warnings, and errors<br>[apple]<br>actual val [INFO](-i-n-f-o.md): [LogLevel](../index.md)<br>Info, event, warnings, and errors<br>[js]<br>actual val [INFO](-i-n-f-o.md): [LogLevel](../index.md)<br>Logs informational messages, warnings, and errors. Includes: INFO, WARN, ERROR<br>[jvm]<br>actual val [INFO](-i-n-f-o.md): [LogLevel](../index.md) |
| [NONE](-n-o-n-e.md) | [common]<br>expect val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging disabled (default)<br>[apple]<br>actual val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging disabled<br>[js]<br>actual val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging is disabled. Default setting.<br>[jvm]<br>actual val [NONE](-n-o-n-e.md): [LogLevel](../index.md)<br>Logging disabled (JVM: configure via slf4j) |
| [Standard](-standard.md) | [apple]<br>val [Standard](-standard.md): [LogLevel](../index.md)<br>Production logging: info, event, warn, error. Recommended for production environments. |
| [TRACE](-t-r-a-c-e.md) | [common]<br>expect val [TRACE](-t-r-a-c-e.md): [LogLevel](../index.md)<br>Most verbose logging level<br>[apple]<br>actual val [TRACE](-t-r-a-c-e.md): [LogLevel](../index.md)<br>Everything including trace. Warning: Logs sensitive information. Never use in production.<br>[js]<br>actual val [TRACE](-t-r-a-c-e.md): [LogLevel](../index.md)<br>Most verbose logging level. Logs all messages including internal traces. Includes: TRACE, DEBUG, INFO, WARN, ERROR<br>[jvm]<br>actual val [TRACE](-t-r-a-c-e.md): [LogLevel](../index.md) |
| [WARN](-w-a-r-n.md) | [common]<br>expect val [WARN](-w-a-r-n.md): [LogLevel](../index.md)<br>Logs warnings and errors<br>[apple]<br>actual val [WARN](-w-a-r-n.md): [LogLevel](../index.md)<br>Warnings and errors<br>[js]<br>actual val [WARN](-w-a-r-n.md): [LogLevel](../index.md)<br>Logs warnings and errors. Includes: WARN, ERROR<br>[jvm]<br>actual val [WARN](-w-a-r-n.md): [LogLevel](../index.md) |

## Functions

| Name | Summary |
|---|---|
| [custom](custom.md) | [apple]<br>fun [custom](custom.md)(vararg levels: [LogLevel.Level](../-level/index.md)): [LogLevel](../index.md)<br>Create custom combination of log levels. |

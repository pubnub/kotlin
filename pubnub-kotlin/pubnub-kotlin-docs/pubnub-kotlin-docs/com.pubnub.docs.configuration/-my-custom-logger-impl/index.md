//[pubnub-kotlin-docs](../../../index.md)/[com.pubnub.docs.configuration](../index.md)/[MyCustomLoggerImpl](index.md)

# MyCustomLoggerImpl

[jvm]\
class [MyCustomLoggerImpl](index.md) : [CustomLogger](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-custom-logger/index.md)

When config options of slf4j implementation of your choice like logback or log4j don't meet you logging requirements implement CustomLogger and pass implementation to PNConfiguration.customLoggers

## Constructors

| | |
|---|---|
| [MyCustomLoggerImpl](-my-custom-logger-impl.md) | [jvm]<br>constructor() |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [jvm]<br>open override val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [debug](debug.md) | [jvm]<br>open override fun [debug](debug.md)(logMessage: [LogMessage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-log-message/index.md))<br>open override fun [debug](debug.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |
| [error](index.md#1364491005%2FFunctions%2F-2080425103) | [jvm]<br>open fun [error](index.md#1364491005%2FFunctions%2F-2080425103)(logMessage: [LogMessage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-log-message/index.md))<br>open fun [error](../../com.pubnub.docs.logging.customlogger/-monitoring-logger/index.md#-407714213%2FFunctions%2F-2080425103)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |
| [info](../../com.pubnub.docs.logging.customlogger/-monitoring-logger/index.md#2011303377%2FFunctions%2F-2080425103) | [jvm]<br>open fun [info](../../com.pubnub.docs.logging.customlogger/-monitoring-logger/index.md#2011303377%2FFunctions%2F-2080425103)(logMessage: [LogMessage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-log-message/index.md))<br>open fun [info](../../com.pubnub.docs.logging.customlogger/-monitoring-logger/index.md#1485773831%2FFunctions%2F-2080425103)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |
| [trace](trace.md) | [jvm]<br>open override fun [trace](trace.md)(message: [LogMessage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-log-message/index.md))<br>open override fun [trace](trace.md)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |
| [warn](../../com.pubnub.docs.logging.customlogger/-monitoring-logger/index.md#1389018537%2FFunctions%2F-2080425103) | [jvm]<br>open fun [warn](../../com.pubnub.docs.logging.customlogger/-monitoring-logger/index.md#1389018537%2FFunctions%2F-2080425103)(logMessage: [LogMessage](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-log-message/index.md))<br>open fun [warn](index.md#-1033663697%2FFunctions%2F-2080425103)(message: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |

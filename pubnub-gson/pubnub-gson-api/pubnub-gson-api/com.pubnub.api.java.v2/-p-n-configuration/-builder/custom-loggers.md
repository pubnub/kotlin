//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.java.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[customLoggers](custom-loggers.md)

# customLoggers

[jvm]\
abstract fun [customLoggers](custom-loggers.md)(customLoggers: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[CustomLogger](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-custom-logger/index.md)&gt;?): [PNConfiguration.Builder](index.md)

abstract val [customLoggers](custom-loggers.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[CustomLogger](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-custom-logger/index.md)&gt;?

Custom loggers list for creating additional logger instances. Use it if your slf4j implementation like logback, log4j2, etc. can't meet your specific logging requirements.

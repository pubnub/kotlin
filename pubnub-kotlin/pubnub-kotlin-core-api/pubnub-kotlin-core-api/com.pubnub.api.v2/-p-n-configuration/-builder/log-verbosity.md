//[pubnub-kotlin-core-api](../../../../index.md)/[com.pubnub.api.v2](../../index.md)/[PNConfiguration](../index.md)/[Builder](index.md)/[logVerbosity](log-verbosity.md)

# logVerbosity

[jvm]\
abstract var [~~logVerbosity~~](log-verbosity.md): [PNLogVerbosity](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-log-verbosity/index.md)

---

### Deprecated

LogVerbosity setting is deprecated and will be removed in future versions. For logging configuration:
1. Use an SLF4J implementation (recommended)
2. Or implement CustomLogger interface and set via customLoggers property. Use CustomLogger if your slf4j implementation like logback, log4j2, etc. can't meet your specific logging requirements.

---

Set to [PNLogVerbosity.BODY](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-log-verbosity/-b-o-d-y/index.md) to enable logging of network traffic, otherwise se to [PNLogVerbosity.NONE](../../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-log-verbosity/-n-o-n-e/index.md).

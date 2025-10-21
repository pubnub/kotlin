//[pubnub-kotlin-docs](../../index.md)/[com.pubnub.docs.configuration](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ConfigurationOther](-configuration-other/index.md) | [jvm]<br>class [ConfigurationOther](-configuration-other/index.md) : [SnippetBase](../com.pubnub.docs/-snippet-base/index.md) |
| [MyCustomLoggerImpl](-my-custom-logger-impl/index.md) | [jvm]<br>class [MyCustomLoggerImpl](-my-custom-logger-impl/index.md) : [CustomLogger](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.logging/-custom-logger/index.md)<br>When config options of slf4j implementation of your choice like logback or log4j don't meet you logging requirements implement CustomLogger and pass implementation to PNConfiguration.customLoggers |

## Functions

| Name | Summary |
|---|---|
| [createAdvancedConfig](create-advanced-config.md) | [jvm]<br>fun [createAdvancedConfig](create-advanced-config.md)(): [PNConfiguration](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)<br>Creates an advanced PubNub configuration with various optional parameters |
| [createBasicConfig](create-basic-config.md) | [jvm]<br>fun [createBasicConfig](create-basic-config.md)(): [PNConfiguration](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)<br>Creates a basic PubNub configuration with only the required parameters |
| [createEncryptedConfig](create-encrypted-config.md) | [jvm]<br>fun [createEncryptedConfig](create-encrypted-config.md)(): [PNConfiguration](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md)<br>Creates a PubNub configuration with encryption enabled |
| [main](main.md) | [jvm]<br>fun [main](main.md)() |
| [printConfigDetails](print-config-details.md) | [jvm]<br>fun [printConfigDetails](print-config-details.md)(config: [PNConfiguration](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.v2/-p-n-configuration/index.md))<br>Prints the details of a configuration object |

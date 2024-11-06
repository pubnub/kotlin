//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api](../index.md)/[PubNub](index.md)/[auditPushChannelProvisions](audit-push-channel-provisions.md)

# auditPushChannelProvisions

[common]\
expect abstract fun [auditPushChannelProvisions](audit-push-channel-provisions.md)(pushType: [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md), deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, environment: [PNPushEnvironment](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-environment/index.md) = PNPushEnvironment.DEVELOPMENT): [ListPushProvisions](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md)actual abstract fun [auditPushChannelProvisions](audit-push-channel-provisions.md)(pushType: [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md), deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, environment: [PNPushEnvironment](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-environment/index.md)): [ListPushProvisions](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md)

[jvm]\
actual abstract fun [auditPushChannelProvisions](audit-push-channel-provisions.md)(pushType: [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md), deviceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, environment: [PNPushEnvironment](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-environment/index.md)): [ListPushProvisions](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md)

Request a list of all channels on which push notifications have been enabled using specified [ListPushProvisions.deviceId](../../com.pubnub.api.endpoints.push/-list-push-provisions/device-id.md).

#### Parameters

jvm

| | |
|---|---|
| pushType | Accepted values: FCM, APNS, MPNS, APNS2. @see [PNPushType](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/index.md) |
| deviceId | The device ID (token) to associate with push notifications. |
| environment | Environment within which device should manage list of channels with enabled notifications     (works only if [pushType](audit-push-channel-provisions.md) set to [PNPushType.APNS2](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2/index.md)). |
| topic | Notifications topic name (usually it is bundle identifier of application for Apple platform).     Required only if pushType set to [PNPushType.APNS2](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.enums/-p-n-push-type/-a-p-n-s2/index.md). |

//[pubnub-core-api](../../../index.md)/[com.pubnub.api.v2](../index.md)/[BasePNConfiguration](index.md)/[managePresenceListManually](manage-presence-list-manually.md)

# managePresenceListManually

[jvm]\
abstract val [managePresenceListManually](manage-presence-list-manually.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Enables explicit presence control. When set to true heartbeat calls will contain only channels and groups added explicitly using PubNubCore.presence. Should be used only with ACL set on the server side. For more information please contact PubNub support

#### See also

| |
|---|
| PubNubCore.presence |
| [BasePNConfiguration.heartbeatInterval](heartbeat-interval.md) |

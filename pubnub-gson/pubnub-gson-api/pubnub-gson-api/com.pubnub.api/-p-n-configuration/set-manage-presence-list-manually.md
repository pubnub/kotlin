//[pubnub-gson-api](../../../index.md)/[com.pubnub.api](../index.md)/[PNConfiguration](index.md)/[setManagePresenceListManually](set-manage-presence-list-manually.md)

# setManagePresenceListManually

[jvm]\
fun [setManagePresenceListManually](set-manage-presence-list-manually.md)(managePresenceListManually: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [PNConfiguration](index.md)

Enables explicit presence control. When set to true heartbeat calls will contain only channels and groups added explicitly using PubNubCore.presence. Should be used only with ACL set on the server side. For more information please contact PubNub support

#### See also

| |
|---|
| PubNubCore.presence |
| BasePNConfigurationImpl.heartbeatInterval |

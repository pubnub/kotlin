//[pubnub-kotlin-core-api](../../../index.md)/[com.pubnub.api.retry](../index.md)/[RetryableEndpointGroup](index.md)

# RetryableEndpointGroup

[common]\
enum [RetryableEndpointGroup](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[RetryableEndpointGroup](index.md)&gt; 

Enum representing various retryable endpoint groups. Each enum constant denotes a specific category of operations that can be retried under certain conditions.

## Entries

| | |
|---|---|
| [SUBSCRIBE](-s-u-b-s-c-r-i-b-e/index.md) | [common]<br>[SUBSCRIBE](-s-u-b-s-c-r-i-b-e/index.md)<br>Represents operation related to subscribing like PubNub.subscribe |
| [PUBLISH](-p-u-b-l-i-s-h/index.md) | [common]<br>[PUBLISH](-p-u-b-l-i-s-h/index.md)<br>Represents the group of operations related to publishing like PubNub.publish, PubNub.publishFileMessage, PubNub.signal, PubNub.fire |
| [PRESENCE](-p-r-e-s-e-n-c-e/index.md) | [common]<br>[PRESENCE](-p-r-e-s-e-n-c-e/index.md)<br>Represents the group of operations related to presence like PubNub.getPresenceState, PubNub.setPresenceState, PubNub.hereNow, PubNub.whereNow, PubNub.presence (Heartbeat), and Leave. |
| [FILE_PERSISTENCE](-f-i-l-e_-p-e-r-s-i-s-t-e-n-c-e/index.md) | [common]<br>[FILE_PERSISTENCE](-f-i-l-e_-p-e-r-s-i-s-t-e-n-c-e/index.md)<br>Represents the group of operations related to file persistence like PubNub.getFileUrl, PubNub.deleteFile, PubNub.listFiles, PubNub.downloadFile, and PubNub.sendFile. |
| [MESSAGE_PERSISTENCE](-m-e-s-s-a-g-e_-p-e-r-s-i-s-t-e-n-c-e/index.md) | [common]<br>[MESSAGE_PERSISTENCE](-m-e-s-s-a-g-e_-p-e-r-s-i-s-t-e-n-c-e/index.md)<br>Represents the group of operations related to message persistence like PubNub.fetchMessages, PubNub.deleteMessages, PubNub.messageCounts, and PubNub.time. |
| [CHANNEL_GROUP](-c-h-a-n-n-e-l_-g-r-o-u-p/index.md) | [common]<br>[CHANNEL_GROUP](-c-h-a-n-n-e-l_-g-r-o-u-p/index.md)<br>Represents the group of operations on channel group like  PubNub.listAllChannelGroups, PubNub.deleteChannelGroup, PubNub.removeChannelsFromChannelGroup, PubNub.listChannelsForChannelGroup, and PubNub.addChannelsToChannelGroup |
| [PUSH_NOTIFICATION](-p-u-s-h_-n-o-t-i-f-i-c-a-t-i-o-n/index.md) | [common]<br>[PUSH_NOTIFICATION](-p-u-s-h_-n-o-t-i-f-i-c-a-t-i-o-n/index.md)<br>Represents the group of operations related to push notification like PubNub.removeAllPushNotificationsFromDeviceWithPushToken, PubNub.addPushNotificationsOnChannels, PubNub.auditPushChannelProvisions and PubNub.removePushNotificationsFromChannels |
| [APP_CONTEXT](-a-p-p_-c-o-n-t-e-x-t/index.md) | [common]<br>[APP_CONTEXT](-a-p-p_-c-o-n-t-e-x-t/index.md)<br>Represents the group of operations related to application context like PubNub.getAllUUIDMetadata, PubNub.getUUIDMetadata, PubNub.setUUIDMetadata, PubNub.removeUUIDMetadata, PubNub.getAllChannelMetadata, PubNub.getChannelMetadata, PubNub.removeChannelMetadata, PubNub.setChannelMetadata, PubNub.getChannelMembers, PubNub.manageChannelMembers, PubNub.getMemberships, and PubNub.manageMemberships |
| [MESSAGE_REACTION](-m-e-s-s-a-g-e_-r-e-a-c-t-i-o-n/index.md) | [common]<br>[MESSAGE_REACTION](-m-e-s-s-a-g-e_-r-e-a-c-t-i-o-n/index.md)<br>Represents the group of operations related to message reaction like PubNub.addMessageAction, PubNub.getMessageActions and PubNub.removeMessageAction |
| [ACCESS_MANAGER](-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md) | [common]<br>[ACCESS_MANAGER](-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md)<br>Represents the group of operations related to access management like PubNub.grant, PubNub.grantToken, PubNub.revokeToken |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.enums/-enum-entries/index.html)&lt;[RetryableEndpointGroup](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F1196661149) | [common]<br>val [name](-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-372974862%2FProperties%2F1196661149): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [ordinal](-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F1196661149) | [common]<br>val [ordinal](-a-c-c-e-s-s_-m-a-n-a-g-e-r/index.md#-739389684%2FProperties%2F1196661149): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [RetryableEndpointGroup](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[RetryableEndpointGroup](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |

---
title: PubNub - pubnub-kotlin
---

[pubnub-kotlin](../../index.html) / [com.pubnub.api](../index.html) / [PubNub](./index.html)

# PubNub

`class PubNub`

### Constructors

| [&lt;init&gt;](-init-.html) | `PubNub(configuration: `[`PNConfiguration`](../-p-n-configuration/index.html)`)` |

### Properties

| [configuration](configuration.html) | `val configuration: `[`PNConfiguration`](../-p-n-configuration/index.html) |
| [instanceId](instance-id.html) | `val instanceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [mapper](mapper.html) | `val mapper: `[`MapperManager`](../../com.pubnub.api.managers/-mapper-manager/index.html) |
| [version](version.html) | `val version: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| [addChannelsToChannelGroup](add-channels-to-channel-group.html) | `fun addChannelsToChannelGroup(): `[`AddChannelChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-add-channel-channel-group/index.html) |
| [addListener](add-listener.html) | `fun addListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addMessageAction](add-message-action.html) | `fun addMessageAction(): `[`AddMessageAction`](../../com.pubnub.api.endpoints.message_actions/-add-message-action/index.html) |
| [addPushNotificationsOnChannels](add-push-notifications-on-channels.html) | `fun addPushNotificationsOnChannels(): `[`AddChannelsToPush`](../../com.pubnub.api.endpoints.push/-add-channels-to-push/index.html) |
| [auditPushChannelProvisions](audit-push-channel-provisions.html) | `fun auditPushChannelProvisions(): `[`ListPushProvisions`](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.html) |
| [baseUrl](base-url.html) | `fun baseUrl(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [decrypt](decrypt.html) | Perform Cryptographic decryption of an input string using cipher key provided by PNConfiguration`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Perform Cryptographic decryption of an input string using the cipher key`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [deleteChannelGroup](delete-channel-group.html) | `fun deleteChannelGroup(): `[`DeleteChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-delete-channel-group/index.html) |
| [deleteMessages](delete-messages.html) | `fun deleteMessages(): `[`DeleteMessages`](../../com.pubnub.api.endpoints/-delete-messages/index.html) |
| [destroy](destroy.html) | `fun destroy(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [disconnect](disconnect.html) | `fun disconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [encrypt](encrypt.html) | Perform Cryptographic encryption of an input string and the cipher key provided by PNConfiguration`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Perform Cryptographic encryption of an input string and the cipher key.`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [fetchMessages](fetch-messages.html) | `fun fetchMessages(): `[`FetchMessages`](../../com.pubnub.api.endpoints/-fetch-messages/index.html) |
| [fire](fire.html) | `fun fire(): `[`Publish`](../../com.pubnub.api.endpoints.pubsub/-publish/index.html) |
| [forceDestroy](force-destroy.html) | `fun forceDestroy(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getMessageActions](get-message-actions.html) | `fun getMessageActions(): `[`GetMessageActions`](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.html) |
| [getPresenceState](get-presence-state.html) | `fun getPresenceState(): `[`GetState`](../../com.pubnub.api.endpoints.presence/-get-state/index.html) |
| [getSubscribedChannelGroups](get-subscribed-channel-groups.html) | `fun getSubscribedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getSubscribedChannels](get-subscribed-channels.html) | `fun getSubscribedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [grant](grant.html) | `fun grant(): `[`Grant`](../../com.pubnub.api.endpoints.access/-grant/index.html) |
| [hereNow](here-now.html) | `fun hereNow(): `[`HereNow`](../../com.pubnub.api.endpoints.presence/-here-now/index.html) |
| [history](history.html) | `fun history(): `[`History`](../../com.pubnub.api.endpoints/-history/index.html) |
| [listAllChannelGroups](list-all-channel-groups.html) | `fun listAllChannelGroups(): `[`ListAllChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.html) |
| [listChannelsForChannelGroup](list-channels-for-channel-group.html) | `fun listChannelsForChannelGroup(): `[`AllChannelsChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-all-channels-channel-group/index.html) |
| [messageCounts](message-counts.html) | `fun messageCounts(): `[`MessageCounts`](../../com.pubnub.api.endpoints/-message-counts/index.html) |
| [presence](presence.html) | `fun presence(): `[`PresenceBuilder`](../../com.pubnub.api.builder/-presence-builder/index.html) |
| [publish](publish.html) | `fun publish(): `[`Publish`](../../com.pubnub.api.endpoints.pubsub/-publish/index.html) |
| [reconnect](reconnect.html) | `fun reconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeAllPushNotificationsFromDeviceWithPushToken](remove-all-push-notifications-from-device-with-push-token.html) | `fun removeAllPushNotificationsFromDeviceWithPushToken(): `[`RemoveAllPushChannelsForDevice`](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/index.html) |
| [removeChannelsFromChannelGroup](remove-channels-from-channel-group.html) | `fun removeChannelsFromChannelGroup(): `[`RemoveChannelChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-remove-channel-channel-group/index.html) |
| [removeListener](remove-listener.html) | `fun removeListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeMessageAction](remove-message-action.html) | `fun removeMessageAction(): `[`RemoveMessageAction`](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.html) |
| [removePushNotificationsFromChannels](remove-push-notifications-from-channels.html) | `fun removePushNotificationsFromChannels(): `[`RemoveChannelsFromPush`](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.html) |
| [requestId](request-id.html) | `fun requestId(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [setPresenceState](set-presence-state.html) | `fun setPresenceState(): `[`SetState`](../../com.pubnub.api.endpoints.presence/-set-state/index.html) |
| [signal](signal.html) | `fun signal(): `[`Signal`](../../com.pubnub.api.endpoints.pubsub/-signal/index.html) |
| [subscribe](subscribe.html) | `fun subscribe(): `[`SubscribeBuilder`](../../com.pubnub.api.builder/-subscribe-builder/index.html) |
| [time](time.html) | `fun time(): `[`Time`](../../com.pubnub.api.endpoints/-time/index.html) |
| [timestamp](timestamp.html) | `fun timestamp(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [unsubscribe](unsubscribe.html) | `fun unsubscribe(): `[`UnsubscribeBuilder`](../../com.pubnub.api.builder/-unsubscribe-builder/index.html) |
| [unsubscribeAll](unsubscribe-all.html) | `fun unsubscribeAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [whereNow](where-now.html) | `fun whereNow(): `[`WhereNow`](../../com.pubnub.api.endpoints.presence/-where-now/index.html) |


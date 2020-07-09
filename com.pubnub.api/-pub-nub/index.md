[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNub](./index.md)

# PubNub

`class PubNub`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PubNub(configuration: `[`PNConfiguration`](../-p-n-configuration/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [configuration](configuration.md) | `val configuration: `[`PNConfiguration`](../-p-n-configuration/index.md) |
| [instanceId](instance-id.md) | Unique id of this PubNub instance.`val instanceId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [mapper](mapper.md) | Manage and parse JSON`val mapper: `[`MapperManager`](../../com.pubnub.api.managers/-mapper-manager/index.md) |
| [version](version.md) | The current version of the Kotlin SDK.`val version: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [addChannelsToChannelGroup](add-channels-to-channel-group.md) | Adds a channel to a channel group.`fun addChannelsToChannelGroup(): `[`AddChannelChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-add-channel-channel-group/index.md) |
| [addListener](add-listener.md) | Add a listener.`fun addListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [addMessageAction](add-message-action.md) | Add an action on a published message. Returns the added action in the response.`fun addMessageAction(): `[`AddMessageAction`](../../com.pubnub.api.endpoints.message_actions/-add-message-action/index.md) |
| [addPushNotificationsOnChannels](add-push-notifications-on-channels.md) | Enable push notifications on provided set of channels.`fun addPushNotificationsOnChannels(): `[`AddChannelsToPush`](../../com.pubnub.api.endpoints.push/-add-channels-to-push/index.md) |
| [auditPushChannelProvisions](audit-push-channel-provisions.md) | Request a list of all channels on which push notifications have been enabled using specified [ListPushProvisions.deviceId](../../com.pubnub.api.endpoints.push/-list-push-provisions/device-id.md).`fun auditPushChannelProvisions(): `[`ListPushProvisions`](../../com.pubnub.api.endpoints.push/-list-push-provisions/index.md) |
| [decrypt](decrypt.md) | Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md).`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Perform Cryptographic decryption of an input string using a cipher key.`fun decrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [deleteChannelGroup](delete-channel-group.md) | Removes the channel group.`fun deleteChannelGroup(): `[`DeleteChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-delete-channel-group/index.md) |
| [deleteMessages](delete-messages.md) | Removes messages from the history of a specific channel.`fun deleteMessages(): `[`DeleteMessages`](../../com.pubnub.api.endpoints/-delete-messages/index.md) |
| [destroy](destroy.md) | Frees up threads and allows for a clean exit.`fun destroy(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [disconnect](disconnect.md) | Cancel any subscribe and heartbeat loops or ongoing re-connections.`fun disconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [encrypt](encrypt.md) | Perform Cryptographic encryption of an input string and the cipher key provided by [PNConfiguration.cipherKey](../-p-n-configuration/cipher-key.md).`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`<br>Perform Cryptographic encryption of an input string and a cipher key.`fun encrypt(inputString: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, cipherKey: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!` |
| [fetchMessages](fetch-messages.md) | Fetch historical messages from multiple channels. The `includeMessageActions` flag also allows you to fetch message actions along with the messages.`fun fetchMessages(): `[`FetchMessages`](../../com.pubnub.api.endpoints/-fetch-messages/index.md) |
| [fire](fire.md) | Send a message to PubNub Functions Event Handlers.`fun fire(): `[`Publish`](../../com.pubnub.api.endpoints.pubsub/-publish/index.md) |
| [forceDestroy](force-destroy.md) | Same as [destroy](destroy.md) but immediately.`fun forceDestroy(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getMessageActions](get-message-actions.md) | Get a list of message actions in a channel. Returns a list of actions in the response.`fun getMessageActions(): `[`GetMessageActions`](../../com.pubnub.api.endpoints.message_actions/-get-message-actions/index.md) |
| [getPresenceState](get-presence-state.md) | Retrieve state information specific to a subscriber UUID.`fun getPresenceState(): `[`GetState`](../../com.pubnub.api.endpoints.presence/-get-state/index.md) |
| [getSubscribedChannelGroups](get-subscribed-channel-groups.md) | Queries the local subscribe loop for channel groups currently in the mix.`fun getSubscribedChannelGroups(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getSubscribedChannels](get-subscribed-channels.md) | Queries the local subscribe loop for channels currently in the mix.`fun getSubscribedChannels(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [grant](grant.md) | This function establishes access permissions for PubNub Access Manager (PAM) by setting the `read` or `write` attribute to `true`. A grant with `read` or `write` set to `false` (or not included) will revoke any previous grants with `read` or `write` set to `true`.`fun grant(): `[`Grant`](../../com.pubnub.api.endpoints.access/-grant/index.md) |
| [hereNow](here-now.md) | Obtain information about the current state of a channel including a list of unique user IDs currently subscribed to the channel and the total occupancy count of the channel.`fun hereNow(): `[`HereNow`](../../com.pubnub.api.endpoints.presence/-here-now/index.md) |
| [history](history.md) | Fetch historical messages of a channel.`fun history(): `[`History`](../../com.pubnub.api.endpoints/-history/index.md) |
| [listAllChannelGroups](list-all-channel-groups.md) | Lists all registered channel groups for the subscribe key.`fun listAllChannelGroups(): `[`ListAllChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-list-all-channel-group/index.md) |
| [listChannelsForChannelGroup](list-channels-for-channel-group.md) | Lists all the channels of the channel group.`fun listChannelsForChannelGroup(): `[`AllChannelsChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-all-channels-channel-group/index.md) |
| [messageCounts](message-counts.md) | Fetches the number of messages published on one or more channels since a given time. The count returned is the number of messages in history with a timetoken value greater than the passed value in the [MessageCounts.channelsTimetoken](../../com.pubnub.api.endpoints/-message-counts/channels-timetoken.md) parameter.`fun messageCounts(): `[`MessageCounts`](../../com.pubnub.api.endpoints/-message-counts/index.md) |
| [presence](presence.md) | `fun presence(): `[`PresenceBuilder`](../../com.pubnub.api.builder/-presence-builder/index.md) |
| [publish](publish.md) | Send a message to all subscribers of a channel.`fun publish(): `[`Publish`](../../com.pubnub.api.endpoints.pubsub/-publish/index.md) |
| [reconnect](reconnect.md) | Force the SDK to try and reach out PubNub. Monitor the results in [SubscribeCallback.status](../../com.pubnub.api.callbacks/-subscribe-callback/status.md)`fun reconnect(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeAllPushNotificationsFromDeviceWithPushToken](remove-all-push-notifications-from-device-with-push-token.md) | Disable push notifications from all channels registered with the specified [RemoveAllPushChannelsForDevice.deviceId](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/device-id.md).`fun removeAllPushNotificationsFromDeviceWithPushToken(): `[`RemoveAllPushChannelsForDevice`](../../com.pubnub.api.endpoints.push/-remove-all-push-channels-for-device/index.md) |
| [removeChannelsFromChannelGroup](remove-channels-from-channel-group.md) | Removes channels from a channel group.`fun removeChannelsFromChannelGroup(): `[`RemoveChannelChannelGroup`](../../com.pubnub.api.endpoints.channel_groups/-remove-channel-channel-group/index.md) |
| [removeListener](remove-listener.md) | Remove a listener.`fun removeListener(listener: `[`SubscribeCallback`](../../com.pubnub.api.callbacks/-subscribe-callback/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeMessageAction](remove-message-action.md) | Remove a previously added action on a published message. Returns an empty response.`fun removeMessageAction(): `[`RemoveMessageAction`](../../com.pubnub.api.endpoints.message_actions/-remove-message-action/index.md) |
| [removePushNotificationsFromChannels](remove-push-notifications-from-channels.md) | Disable push notifications on provided set of channels.`fun removePushNotificationsFromChannels(): `[`RemoveChannelsFromPush`](../../com.pubnub.api.endpoints.push/-remove-channels-from-push/index.md) |
| [setPresenceState](set-presence-state.md) | Set state information specific to a subscriber UUID.`fun setPresenceState(): `[`SetState`](../../com.pubnub.api.endpoints.presence/-set-state/index.md) |
| [signal](signal.md) | Send a signal to all subscribers of a channel.`fun signal(): `[`Signal`](../../com.pubnub.api.endpoints.pubsub/-signal/index.md) |
| [subscribe](subscribe.md) | Causes the client to create an open TCP socket to the PubNub Real-Time Network and begin listening for messages on a specified channel.`fun subscribe(): `[`SubscribeBuilder`](../../com.pubnub.api.builder/-subscribe-builder/index.md) |
| [time](time.md) | Returns a 17 digit precision Unix epoch from the server.`fun time(): `[`Time`](../../com.pubnub.api.endpoints/-time/index.md) |
| [unsubscribe](unsubscribe.md) | When subscribed to a single channel, this function causes the client to issue a leave from the channel and close any open socket to the PubNub Network.`fun unsubscribe(): `[`UnsubscribeBuilder`](../../com.pubnub.api.builder/-unsubscribe-builder/index.md) |
| [unsubscribeAll](unsubscribe-all.md) | Unsubscribe from all channels and all channel groups`fun unsubscribeAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [whereNow](where-now.md) | Obtain information about the current list of channels to which a UUID is subscribed to.`fun whereNow(): `[`WhereNow`](../../com.pubnub.api.endpoints.presence/-where-now/index.md) |

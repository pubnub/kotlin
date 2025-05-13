//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[StatusListener](index.md)

# StatusListener

expect interface [StatusListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)actual interface [StatusListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)actual interface [StatusListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)actual interface [StatusListener](index.md) : [Listener](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.callbacks/-listener/index.md)

Implement this interface and pass it into com.pubnub.api.v2.callbacks.StatusEmitter.addListener to listen for PubNub connection status changes.

Implement this interface and pass it into com.pubnub.api.v2.callbacks.StatusEmitter.addListener to listen for PubNub connection status changes.

Implement this interface and pass it into com.pubnub.api.v2.callbacks.StatusEmitter.addListener to listen for PubNub connection status changes.

Implement this interface and pass it into [com.pubnub.api.v2.callbacks.StatusEmitter.addListener](../-status-emitter/add-listener.md) to listen for PubNub connection status changes.

#### Inheritors

| |
|---|
| [StatusListenerImpl](../-status-listener-impl/index.md) |
| [SubscribeCallback](../../com.pubnub.api.callbacks/-subscribe-callback/index.md) |

## Properties

| Name | Summary |
|---|---|
| [onStatusChange](on-status-change.md) | [apple]<br>abstract val [onStatusChange](on-status-change.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), [PNStatus](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html) |
| [underlying](underlying.md) | [apple]<br>abstract val [underlying](underlying.md): <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- ---> |

## Functions

| Name | Summary |
|---|---|
| [status](status.md) | [jvm]<br>abstract fun [status](status.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), status: [PNStatus](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer/-p-n-status/index.md))<br>Receive status updates from the PubNub client, such as: |

//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.v2.callbacks](../index.md)/[StatusListener](index.md)

# StatusListener

expect interface [StatusListener](index.md) : [Listener](../../com.pubnub.api.callbacks/-listener/index.md)actual interface [StatusListener](index.md)actual interface [StatusListener](index.md) : [Listener](../../com.pubnub.api.callbacks/-listener/index.md)

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
| [onStatusChange](on-status-change.md) | [ios]<br>abstract val [onStatusChange](on-status-change.md): ([PubNub](../../com.pubnub.api/-pub-nub/index.md), <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- --->) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [underlying](underlying.md) | [ios]<br>abstract val [underlying](underlying.md): <!---  GfmCommand {"@class":"org.jetbrains.dokka.gfm.ResolveLinkGfmCommand","dri":{"packageName":"","classNames":"<Error class: unknown class>","callable":null,"target":{"@class":"org.jetbrains.dokka.links.PointingToDeclaration"},"extra":null}} --->&lt;Error class: unknown class&gt;<!--- ---> |

## Functions

| Name | Summary |
|---|---|
| [status](status.md) | [jvm]<br>abstract fun [status](status.md)(pubnub: [PubNub](../../com.pubnub.api/-pub-nub/index.md), status: [PNStatus](../../com.pubnub.api.models.consumer/-p-n-status/index.md))<br>Receive status updates from the PubNub client, such as: |

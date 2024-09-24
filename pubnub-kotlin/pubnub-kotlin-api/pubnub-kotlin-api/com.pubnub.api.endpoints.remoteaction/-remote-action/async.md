//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.endpoints.remoteaction](../index.md)/[RemoteAction](index.md)/[async](async.md)

# async

[common]\
abstract override fun [async](async.md)(callback: [Consumer](../../com.pubnub.api.v2.callbacks/-consumer/index.md)&lt;[Result](../../com.pubnub.api.v2.callbacks/-result/index.md)&lt;[Output](index.md)&gt;&gt;)

Run the action asynchronously, without blocking the calling thread and delivering the result through the [callback](async.md).

The delivered result can be either a success (including a result value if any) or a failure (including a [PubNubException](../../com.pubnub.api/-pub-nub-exception/index.md)).

The recommended pattern to use is:

```kotlin
action.async { result ->
    result.onSuccess { value ->
        // do something with value
    }.onFailure { exception ->
        // do something with exception
    }
}
```

[pubnub-kotlin](../index.md) / [com.pubnub.api](./index.md)

## Package com.pubnub.api

### Types

| Name | Summary |
|---|---|
| [Endpoint](-endpoint/index.md) | Base class for all PubNub API operation implementations.`abstract class Endpoint<Input, Output> : `[`ExtendedRemoteAction`](../com.pubnub.api.endpoints.remoteaction/-extended-remote-action/index.md)`<Output>` |
| [PNConfiguration](-p-n-configuration/index.md) | A storage for user-provided information which describe further PubNub client behaviour. Configuration instance contains additional set of properties which allow to perform precise PubNub client configuration.`open class PNConfiguration` |
| [PubNub](-pub-nub/index.md) | `class PubNub` |
| [PubNubError](-pub-nub-error/index.md) | List of known PubNub errors. Observe them in [PubNubException.pubnubError](-pub-nub-exception/pubnub-error.md) in [PNStatus.exception](../com.pubnub.api.models.consumer/-p-n-status/exception.md).`enum class PubNubError` |

### Exceptions

| Name | Summary |
|---|---|
| [PubNubException](-pub-nub-exception/index.md) | Custom exception wrapper for errors occurred during execution or processing of a PubNub API operation.`data class PubNubException : `[`Exception`](https://docs.oracle.com/javase/6/docs/api/java/lang/Exception.html) |

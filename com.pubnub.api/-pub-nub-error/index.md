[pubnub-kotlin](../../index.md) / [com.pubnub.api](../index.md) / [PubNubError](./index.md)

# PubNubError

`enum class PubNubError`

List of known PubNub errors. Observe them in [PubNubException.pubnubError](../-pub-nub-exception/pubnub-error.md) in [PNStatus.exception](../../com.pubnub.api.models.consumer/-p-n-status/exception.md).

### Enum Values

| Name | Summary |
|---|---|
| [TIMEOUT](-t-i-m-e-o-u-t.md) |  |
| [CONNECT_EXCEPTION](-c-o-n-n-e-c-t_-e-x-c-e-p-t-i-o-n.md) |  |
| [SECRET_KEY_MISSING](-s-e-c-r-e-t_-k-e-y_-m-i-s-s-i-n-g.md) |  |
| [JSON_ERROR](-j-s-o-n_-e-r-r-o-r.md) |  |
| [INTERNAL_ERROR](-i-n-t-e-r-n-a-l_-e-r-r-o-r.md) |  |
| [PARSING_ERROR](-p-a-r-s-i-n-g_-e-r-r-o-r.md) |  |
| [INVALID_ARGUMENTS](-i-n-v-a-l-i-d_-a-r-g-u-m-e-n-t-s.md) |  |
| [CONNECTION_NOT_SET](-c-o-n-n-e-c-t-i-o-n_-n-o-t_-s-e-t.md) |  |
| [GROUP_MISSING](-g-r-o-u-p_-m-i-s-s-i-n-g.md) |  |
| [SUBSCRIBE_KEY_MISSING](-s-u-b-s-c-r-i-b-e_-k-e-y_-m-i-s-s-i-n-g.md) |  |
| [PUBLISH_KEY_MISSING](-p-u-b-l-i-s-h_-k-e-y_-m-i-s-s-i-n-g.md) |  |
| [SUBSCRIBE_TIMEOUT](-s-u-b-s-c-r-i-b-e_-t-i-m-e-o-u-t.md) |  |
| [HTTP_ERROR](-h-t-t-p_-e-r-r-o-r.md) |  |
| [MESSAGE_MISSING](-m-e-s-s-a-g-e_-m-i-s-s-i-n-g.md) |  |
| [CHANNEL_MISSING](-c-h-a-n-n-e-l_-m-i-s-s-i-n-g.md) |  |
| [CRYPTO_ERROR](-c-r-y-p-t-o_-e-r-r-o-r.md) |  |
| [STATE_MISSING](-s-t-a-t-e_-m-i-s-s-i-n-g.md) |  |
| [CHANNEL_AND_GROUP_MISSING](-c-h-a-n-n-e-l_-a-n-d_-g-r-o-u-p_-m-i-s-s-i-n-g.md) |  |
| [PUSH_TYPE_MISSING](-p-u-s-h_-t-y-p-e_-m-i-s-s-i-n-g.md) |  |
| [DEVICE_ID_MISSING](-d-e-v-i-c-e_-i-d_-m-i-s-s-i-n-g.md) |  |
| [TIMETOKEN_MISSING](-t-i-m-e-t-o-k-e-n_-m-i-s-s-i-n-g.md) |  |
| [CHANNELS_TIMETOKEN_MISMATCH](-c-h-a-n-n-e-l-s_-t-i-m-e-t-o-k-e-n_-m-i-s-m-a-t-c-h.md) |  |
| [USER_MISSING](-u-s-e-r_-m-i-s-s-i-n-g.md) |  |
| [USER_ID_MISSING](-u-s-e-r_-i-d_-m-i-s-s-i-n-g.md) |  |
| [USER_NAME_MISSING](-u-s-e-r_-n-a-m-e_-m-i-s-s-i-n-g.md) |  |
| [RESOURCES_MISSING](-r-e-s-o-u-r-c-e-s_-m-i-s-s-i-n-g.md) |  |
| [PERMISSION_MISSING](-p-e-r-m-i-s-s-i-o-n_-m-i-s-s-i-n-g.md) |  |
| [INVALID_ACCESS_TOKEN](-i-n-v-a-l-i-d_-a-c-c-e-s-s_-t-o-k-e-n.md) |  |
| [MESSAGE_ACTION_MISSING](-m-e-s-s-a-g-e_-a-c-t-i-o-n_-m-i-s-s-i-n-g.md) |  |
| [MESSAGE_ACTION_TYPE_MISSING](-m-e-s-s-a-g-e_-a-c-t-i-o-n_-t-y-p-e_-m-i-s-s-i-n-g.md) |  |
| [MESSAGE_ACTION_VALUE_MISSING](-m-e-s-s-a-g-e_-a-c-t-i-o-n_-v-a-l-u-e_-m-i-s-s-i-n-g.md) |  |
| [MESSAGE_TIMETOKEN_MISSING](-m-e-s-s-a-g-e_-t-i-m-e-t-o-k-e-n_-m-i-s-s-i-n-g.md) |  |
| [MESSAGE_ACTION_TIMETOKEN_MISSING](-m-e-s-s-a-g-e_-a-c-t-i-o-n_-t-i-m-e-t-o-k-e-n_-m-i-s-s-i-n-g.md) |  |
| [HISTORY_MESSAGE_ACTIONS_MULTIPLE_CHANNELS](-h-i-s-t-o-r-y_-m-e-s-s-a-g-e_-a-c-t-i-o-n-s_-m-u-l-t-i-p-l-e_-c-h-a-n-n-e-l-s.md) |  |
| [PUSH_TOPIC_MISSING](-p-u-s-h_-t-o-p-i-c_-m-i-s-s-i-n-g.md) |  |

### Properties

| Name | Summary |
|---|---|
| [message](message.md) | The error message.`val message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

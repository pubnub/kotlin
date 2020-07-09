[pubnub-kotlin](../../index.md) / [com.pubnub.api.enums](../index.md) / [PNReconnectionPolicy](./index.md)

# PNReconnectionPolicy

`enum class PNReconnectionPolicy`

### Enum Values

| Name | Summary |
|---|---|
| [NONE](-n-o-n-e.md) | No reconnection policy. If the subscribe loop gets cancelled due to network or other issues, the SDK will not attempt to try to restore it. |
| [LINEAR](-l-i-n-e-a-r.md) | The SDK will try to reconnect to the network by doing so every 3 seconds. |
| [EXPONENTIAL](-e-x-p-o-n-e-n-t-i-a-l.md) | The SDK will try to reconnect to the network by doing so in non-fixed intervals. ie. the interval between retries is another power of 2, starting from 0 to 5. |

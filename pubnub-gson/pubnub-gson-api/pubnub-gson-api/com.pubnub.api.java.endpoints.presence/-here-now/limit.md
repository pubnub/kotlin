//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.presence](../index.md)/[HereNow](index.md)/[limit](limit.md)

# limit

[jvm]\
abstract fun [limit](limit.md)(limit: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [HereNow](index.md)

Set the maximum number of occupants to return per channel. 

 The server enforces a maximum limit of 1000. Values outside this range will be rejected by the server. 

 Special behavior: 

- Use `limit = 0` to retrieve only occupancy counts without individual occupant UUIDs

#### Return

`this` for method chaining

#### Parameters

jvm

| | |
|---|---|
| limit | Maximum number of occupants to return (0-1000). Default: 1000 |

#### See also

| | |
|---|---|
| [offset(Integer)](offset.md) | for pagination support |

//[pubnub-gson-api](../../../index.md)/[com.pubnub.api.java.endpoints.presence](../index.md)/[HereNow](index.md)/[offset](offset.md)

# offset

[jvm]\
abstract fun [offset](offset.md)(offset: [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)): [HereNow](index.md)

Set the zero-based starting index for pagination through occupants. 

 Server-side validation applies: 

- Must be >= 0 (negative values will be rejected)

#### Return

`this` for method chaining

#### Parameters

jvm

| | |
|---|---|
| offset | Zero-based starting position (must be >= 0). Default: null (no offset) |

#### See also

| | |
|---|---|
| [limit(int)](limit.md) | for controlling result size |

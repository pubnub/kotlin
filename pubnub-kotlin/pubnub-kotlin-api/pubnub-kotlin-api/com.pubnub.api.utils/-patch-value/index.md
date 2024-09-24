//[pubnub-kotlin-api](../../../index.md)/[com.pubnub.api.utils](../index.md)/[PatchValue](index.md)

# PatchValue

[common]\
data class [PatchValue](index.md)&lt;out [T](index.md)&gt;

An optional that accepts nullable values. Thus, it can represent two (`PatchValue<T>`) or three (`PatchValue<T>?`) states:

- 
   `PatchValue.of(someValue)` - value is present and that value is `someValue`
- 
   `PatchValue.of(null)` - value is present and that value is `null`
- 
   `null` - lack of information about value (no update for this field)

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>val [value](value.md): [T](index.md) |

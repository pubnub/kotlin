//[pubnub-core-api](../../../index.md)/[com.pubnub.api.models.consumer.history](../index.md)/[Action](index.md)

# Action

open class [Action](index.md)(val uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val actionTimetoken: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Encapsulates a message action in terms of batch history.

#### Inheritors

| |
|---|
| [Action](../-p-n-fetch-message-item/-action/index.md) |

## Constructors

| | |
|---|---|
| [Action](-action.md) | [jvm]<br>constructor(uuid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), actionTimetoken: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [actionTimetoken](action-timetoken.md) | [jvm]<br>val [actionTimetoken](action-timetoken.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The publish timetoken of the message action. |
| [uuid](uuid.md) | [jvm]<br>val [uuid](uuid.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The UUID of the publisher. |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

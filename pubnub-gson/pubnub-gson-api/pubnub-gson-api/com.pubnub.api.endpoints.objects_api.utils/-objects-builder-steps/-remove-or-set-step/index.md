//[pubnub-gson-api](../../../../index.md)/[com.pubnub.api.endpoints.objects_api.utils](../../index.md)/[ObjectsBuilderSteps](../index.md)/[RemoveOrSetStep](index.md)

# RemoveOrSetStep

interface [RemoveOrSetStep](index.md)&lt;[T](index.md), [E](index.md)&gt;

#### Inheritors

| |
|---|
| [Builder](../../../com.pubnub.api.endpoints.objects_api.memberships/-manage-memberships/-builder/index.md) |

## Types

| Name | Summary |
|---|---|
| [RemoveStep](-remove-step/index.md) | [jvm]<br>interface [RemoveStep](-remove-step/index.md)&lt;[T](-remove-step/index.md), [E](-remove-step/index.md)&gt; |
| [SetStep](-set-step/index.md) | [jvm]<br>interface [SetStep](-set-step/index.md)&lt;[T](-set-step/index.md), [E](-set-step/index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [remove](remove.md) | [jvm]<br>abstract fun [remove](remove.md)(entitiesToRemove: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[E](index.md)&gt;): [ObjectsBuilderSteps.RemoveOrSetStep.SetStep](-set-step/index.md)&lt;[T](index.md), [E](index.md)&gt; |
| [set](set.md) | [jvm]<br>abstract fun [set](set.md)(entitiesToSet: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[E](index.md)&gt;): [ObjectsBuilderSteps.RemoveOrSetStep.RemoveStep](-remove-step/index.md)&lt;[T](index.md), [E](index.md)&gt; |

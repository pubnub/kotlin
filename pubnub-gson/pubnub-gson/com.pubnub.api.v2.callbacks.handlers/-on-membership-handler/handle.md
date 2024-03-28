//[pubnub-gson](../../../index.md)/[com.pubnub.api.v2.callbacks.handlers](../index.md)/[OnMembershipHandler](index.md)/[handle](handle.md)

# handle

[jvm]\
abstract fun [handle](handle.md)(pnMembershipResult: [PNMembershipResult](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-membership-result/index.md))

 This interface is designed for implementing custom handlers that respond to membershipMetadata event retrieval operations. It defines a single `handle` method that is called with a [PNMembershipResult](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-membership-result/index.md) instance, which contains the membership metadata. 

 Usage example: 

```kotlin

OnMembershipHandler handler = pnMembershipResult -> {
    System.out.println("Received membership event: " + pnMembershipResult.getEvent());
};

```

#### See also

| | |
|---|---|
| [PNMembershipResult](../../com.pubnub.api.models.consumer.objects_api.membership/-p-n-membership-result/index.md) | for more information about the message result provided to this handler. |

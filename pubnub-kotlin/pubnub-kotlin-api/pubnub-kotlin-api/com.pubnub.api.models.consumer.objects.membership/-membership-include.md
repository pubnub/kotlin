//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.models.consumer.objects.membership](index.md)/[MembershipInclude](-membership-include.md)

# MembershipInclude

[common]\
fun [MembershipInclude](-membership-include.md)(includeCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeStatus: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeTotalCount: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeChannel: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeChannelCustom: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeChannelType: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeChannelStatus: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) = false): [MembershipInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.membership/-membership-include/index.md)

Factory function to create an instance of [MembershipInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.membership/-membership-include/index.md).

This function provides default values for all inclusion flags and returns an implementation of the [MembershipInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.membership/-membership-include/index.md) interface with the specified options.

#### Return

An instance of [MembershipInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.membership/-membership-include/index.md) with the specified inclusion options.

#### Parameters

common

| | |
|---|---|
| includeCustom | Whether to include custom properties in the result. Default is `false`. |
| includeStatus | Whether to include the status of the Memberships in the result. Default is `false`. |
| includeType | Whether to include the type of the Memberships in the result. Default is `false`. |
| includeTotalCount | Whether to include the total count of Memberships in the result. Default is `false`. |
| includeChannel | Whether to include channel information in the result. Default is `false`. |
| includeChannelCustom | Whether to include custom properties of the channel in the result. Default is `false`. |
| includeChannelType | Whether to include the type of the channel in the result. Default is `false`. |
| includeChannelStatus | Whether to include the status of the channel in the result. Default is `false`. |

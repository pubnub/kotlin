//[pubnub-kotlin-api](../../index.md)/[com.pubnub.api.models.consumer.objects.member](index.md)/[MemberInclude](-member-include.md)

# MemberInclude

[common]\
fun [MemberInclude](-member-include.md)(includeCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeStatus: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeType: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeTotalCount: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUser: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUserCustom: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUserType: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false, includeUserStatus: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) = false): [MemberInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md)

Factory function to create an instance of [MemberInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md).

This function provides default values for all inclusion flags and returns an implementation of the [MemberInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md) interface with the specified options.

#### Return

An instance of [MemberInclude](../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.api.models.consumer.objects.member/-member-include/index.md) with the specified inclusion options.

#### Parameters

common

| | |
|---|---|
| includeCustom | Whether to include custom properties in the result. Default is `false`. |
| includeStatus | Whether to include the status of the Members in the result. Default is `false`. |
| includeType | Whether to include the type of the Members in the result. Default is `false`. |
| includeTotalCount | Whether to include the total count of Members in the result. Default is `false`. |
| includeUser | Whether to include user information in the result. Default is `false`. |
| includeUserCustom | Whether to include custom properties of the user in the result. Default is `false`. |
| includeUserType | Whether to include the type of the user in the result. Default is `false`. |
| includeUserStatus | Whether to include the status of the user in the result. Default is `false`. |

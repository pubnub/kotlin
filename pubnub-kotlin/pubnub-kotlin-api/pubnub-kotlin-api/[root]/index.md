//[pubnub-kotlin-api](../../index.md)/[[root]](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AddMessageActionResult](-add-message-action-result/index.md) | [js]<br>external interface [AddMessageActionResult](-add-message-action-result/index.md) |
| [Callback](-callback/index.md) | [js]<br>typealias [Callback](-callback/index.md)&lt;[ResponseType](-callback/index.md)&gt; = (status: [PubNub.PubnubStatus](-pub-nub/-pubnub-status/index.md), response: [ResponseType](-callback/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [Categories](-categories/index.md) | [js]<br>external interface [Categories](-categories/index.md) |
| [Cryptor](-cryptor/index.md) | [js]<br>external interface [Cryptor](-cryptor/index.md) |
| [ExponentialRetryPolicyConfiguration](-exponential-retry-policy-configuration/index.md) | [js]<br>external interface [ExponentialRetryPolicyConfiguration](-exponential-retry-policy-configuration/index.md) |
| [GetAllChannelMetadataResponse](-get-all-channel-metadata-response/index.md) | [js]<br>typealias [GetAllChannelMetadataResponse](-get-all-channel-metadata-response/index.md) = [PagedObjectsResponse](-paged-objects-response/index.md)&lt;[PubNub.ChannelMetadataObject](-pub-nub/-channel-metadata-object/index.md)&gt; |
| [GetAllUUIDMetadataResponse](-get-all-u-u-i-d-metadata-response/index.md) | [js]<br>typealias [GetAllUUIDMetadataResponse](-get-all-u-u-i-d-metadata-response/index.md) = [PagedObjectsResponse](-paged-objects-response/index.md)&lt;[PubNub.UUIDMetadataObject](-pub-nub/-u-u-i-d-metadata-object/index.md)&gt; |
| [GetChannelMetadataResponse](-get-channel-metadata-response/index.md) | [js]<br>typealias [GetChannelMetadataResponse](-get-channel-metadata-response/index.md) = [ObjectsResponse](-objects-response/index.md)&lt;[PubNub.ChannelMetadataObject](-pub-nub/-channel-metadata-object/index.md)&gt; |
| [GetUUIDMetadataResponse](-get-u-u-i-d-metadata-response/index.md) | [js]<br>typealias [GetUUIDMetadataResponse](-get-u-u-i-d-metadata-response/index.md) = [ObjectsResponse](-objects-response/index.md)&lt;[PubNub.UUIDMetadataObject](-pub-nub/-u-u-i-d-metadata-object/index.md)&gt; |
| [LinearRetryPolicyConfiguration](-linear-retry-policy-configuration/index.md) | [js]<br>external interface [LinearRetryPolicyConfiguration](-linear-retry-policy-configuration/index.md) |
| [ManageChannelMembersResponse](-manage-channel-members-response/index.md) | [js]<br>typealias [ManageChannelMembersResponse](-manage-channel-members-response/index.md) = [PagedObjectsResponse](-paged-objects-response/index.md)&lt;[PubNub.UUIDMembershipObject](-pub-nub/-u-u-i-d-membership-object/index.md)&gt; |
| [ManageMembershipsResponse](-manage-memberships-response/index.md) | [js]<br>typealias [ManageMembershipsResponse](-manage-memberships-response/index.md) = [PagedObjectsResponse](-paged-objects-response/index.md)&lt;[PubNub.ChannelMembershipObject](-pub-nub/-channel-membership-object/index.md)&gt; |
| [ObjectsFunctions](-objects-functions/index.md) | [js]<br>external interface [ObjectsFunctions](-objects-functions/index.md) |
| [ObjectsResponse](-objects-response/index.md) | [js]<br>external interface [ObjectsResponse](-objects-response/index.md)&lt;[DataType](-objects-response/index.md)&gt; |
| [Operations](-operations/index.md) | [js]<br>external interface [Operations](-operations/index.md) |
| [PagedObjectsResponse](-paged-objects-response/index.md) | [js]<br>external interface [PagedObjectsResponse](-paged-objects-response/index.md)&lt;[DataType](-paged-objects-response/index.md)&gt; : [ObjectsResponse](-objects-response/index.md)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[DataType](-paged-objects-response/index.md)&gt;&gt; |
| [Partial](-partial/index.md) | [js]<br>external interface [Partial](-partial/index.md) |
| [PubNub](-pub-nub/index.md) | [js]<br>open external class [PubNub](-pub-nub/index.md)(config: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)) |
| [RemoveChannelMetadataResponse](-remove-channel-metadata-response/index.md) | [js]<br>typealias [RemoveChannelMetadataResponse](-remove-channel-metadata-response/index.md) = [ObjectsResponse](-objects-response/index.md)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt; |
| [RemoveMessageActionResult](-remove-message-action-result/index.md) | [js]<br>external interface [RemoveMessageActionResult](-remove-message-action-result/index.md) |
| [RemoveUUIDMetadataResponse](-remove-u-u-i-d-metadata-response/index.md) | [js]<br>typealias [RemoveUUIDMetadataResponse](-remove-u-u-i-d-metadata-response/index.md) = [ObjectsResponse](-objects-response/index.md)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)&gt; |
| [SetChannelMetadataResponse](-set-channel-metadata-response/index.md) | [js]<br>typealias [SetChannelMetadataResponse](-set-channel-metadata-response/index.md) = [ObjectsResponse](-objects-response/index.md)&lt;[PubNub.ChannelMetadataObject](-pub-nub/-channel-metadata-object/index.md)&gt; |
| [SetUUIDMetadataResponse](-set-u-u-i-d-metadata-response/index.md) | [js]<br>typealias [SetUUIDMetadataResponse](-set-u-u-i-d-metadata-response/index.md) = [ObjectsResponse](-objects-response/index.md)&lt;[PubNub.UUIDMetadataObject](-pub-nub/-u-u-i-d-metadata-object/index.md)&gt; |
| [StatusCallback](-status-callback/index.md) | [js]<br>typealias [StatusCallback](-status-callback/index.md) = (status: [PubNub.PubnubStatus](-pub-nub/-pubnub-status/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-unit/index.html) |
| [UserId](-user-id/index.md) | [js]<br>external interface [UserId](-user-id/index.md) |
| [UUID](-u-u-i-d/index.md) | [js]<br>external interface [UUID](-u-u-i-d/index.md) |

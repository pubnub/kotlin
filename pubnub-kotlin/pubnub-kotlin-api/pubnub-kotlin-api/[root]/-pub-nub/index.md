//[pubnub-kotlin-api](../../../index.md)/[[root]](../index.md)/[PubNub](index.md)

# PubNub

[js]\
open external class [PubNub](index.md)(config: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html))

## Constructors

| | |
|---|---|
| [PubNub](-pub-nub.md) | [js]<br>constructor(config: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Action](-action/index.md) | [js]<br>interface [Action](-action/index.md) |
| [ActionContentToAction](-action-content-to-action/index.md) | [js]<br>interface [ActionContentToAction](-action-content-to-action/index.md) : [JsMap](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-js-map/index.md)&lt;[Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[PubNub.Action](-action/index.md)&gt;&gt; |
| [ActionParam](-action-param/index.md) | [js]<br>interface [ActionParam](-action-param/index.md) |
| [ActionTypeToActions](-action-type-to-actions/index.md) | [js]<br>interface [ActionTypeToActions](-action-type-to-actions/index.md) : [JsMap](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-js-map/index.md)&lt;[PubNub.ActionContentToAction](-action-content-to-action/index.md)&gt; |
| [AddChannelParameters](-add-channel-parameters/index.md) | [js]<br>interface [AddChannelParameters](-add-channel-parameters/index.md) |
| [AddMessageActionParameters](-add-message-action-parameters/index.md) | [js]<br>interface [AddMessageActionParameters](-add-message-action-parameters/index.md) |
| [APNS2Configuration](-a-p-n-s2-configuration/index.md) | [js]<br>interface [APNS2Configuration](-a-p-n-s2-configuration/index.md) |
| [APNS2Target](-a-p-n-s2-target/index.md) | [js]<br>interface [APNS2Target](-a-p-n-s2-target/index.md) |
| [APNSNotificationPayload](-a-p-n-s-notification-payload/index.md) | [js]<br>interface [APNSNotificationPayload](-a-p-n-s-notification-payload/index.md) : [PubNub.BaseNotificationPayload](-base-notification-payload/index.md) |
| [BaseNotificationPayload](-base-notification-payload/index.md) | [js]<br>interface [BaseNotificationPayload](-base-notification-payload/index.md) |
| [BaseObjectsEvent](-base-objects-event/index.md) | [js]<br>interface [BaseObjectsEvent](-base-objects-event/index.md) |
| [BufferFileInput](-buffer-file-input/index.md) | [js]<br>interface [BufferFileInput](-buffer-file-input/index.md) |
| [ChannelGroups](-channel-groups/index.md) | [js]<br>interface [ChannelGroups](-channel-groups/index.md) |
| [ChannelMembershipObject](-channel-membership-object/index.md) | [js]<br>interface [ChannelMembershipObject](-channel-membership-object/index.md) : [PubNub.v2ObjectDataOmitId](v2-object-data-omit-id/index.md) |
| [ChannelMembersParameters](-channel-members-parameters/index.md) | [js]<br>interface [ChannelMembersParameters](-channel-members-parameters/index.md) |
| [ChannelMetadata](-channel-metadata/index.md) | [js]<br>interface [ChannelMetadata](-channel-metadata/index.md) : [PubNub.ObjectParam](-object-param/index.md), [PubNub.ChannelMetadataFieldsNullable](-channel-metadata-fields-nullable/index.md) |
| [ChannelMetadataFieldsNullable](-channel-metadata-fields-nullable/index.md) | [js]<br>interface [ChannelMetadataFieldsNullable](-channel-metadata-fields-nullable/index.md) |
| [ChannelMetadataObject](-channel-metadata-object/index.md) | [js]<br>interface [ChannelMetadataObject](-channel-metadata-object/index.md) : [PubNub.v2ObjectData](v2-object-data/index.md), [PubNub.ChannelMetadataFieldsNullable](-channel-metadata-fields-nullable/index.md) |
| [ChannelsToFetchMessageItemsMap](-channels-to-fetch-message-items-map/index.md) | [js]<br>interface [ChannelsToFetchMessageItemsMap](-channels-to-fetch-message-items-map/index.md) : [JsMap](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-js-map/index.md)&lt;[Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[PubNub.FetchMessageItem](-fetch-message-item/index.md)&gt;&gt; |
| [ChannelsToHereNowChannelData](-channels-to-here-now-channel-data/index.md) | [js]<br>interface [ChannelsToHereNowChannelData](-channels-to-here-now-channel-data/index.md) : [JsMap](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-js-map/index.md)&lt;[PubNub.HereNowChannelData](-here-now-channel-data/index.md)&gt; |
| [Companion](-companion/index.md) | [js]<br>object [Companion](-companion/index.md) |
| [CryptoModule](-crypto-module/index.md) | [js]<br>open class [CryptoModule](-crypto-module/index.md)(configuration: [PubNub.CryptoModuleConfiguration](-crypto-module-configuration/index.md)) |
| [CryptoModuleConfiguration](-crypto-module-configuration/index.md) | [js]<br>interface [CryptoModuleConfiguration](-crypto-module-configuration/index.md) |
| [CryptoParameters](-crypto-parameters/index.md) | [js]<br>interface [CryptoParameters](-crypto-parameters/index.md) |
| [CryptorConfiguration](-cryptor-configuration/index.md) | [js]<br>interface [CryptorConfiguration](-cryptor-configuration/index.md) |
| [CustomObject](-custom-object/index.md) | [js]<br>interface [CustomObject](-custom-object/index.md) : [JsMap](../../../../../pubnub-kotlin/pubnub-kotlin-core-api/pubnub-kotlin-core-api/com.pubnub.kmp/-js-map/index.md)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; |
| [DeleteFileResponse](-delete-file-response/index.md) | [js]<br>interface [DeleteFileResponse](-delete-file-response/index.md) |
| [DeleteGroupParameters](-delete-group-parameters/index.md) | [js]<br>interface [DeleteGroupParameters](-delete-group-parameters/index.md) |
| [DeleteMembershipObject](-delete-membership-object/index.md) | [js]<br>interface [DeleteMembershipObject](-delete-membership-object/index.md) |
| [DeleteMessagesParameters](-delete-messages-parameters/index.md) | [js]<br>interface [DeleteMessagesParameters](-delete-messages-parameters/index.md) |
| [DownloadFileParameters](-download-file-parameters/index.md) | [js]<br>interface [DownloadFileParameters](-download-file-parameters/index.md) |
| [EncryptedDataType](-encrypted-data-type/index.md) | [js]<br>interface [EncryptedDataType](-encrypted-data-type/index.md) |
| [FCMNotificationPayload](-f-c-m-notification-payload/index.md) | [js]<br>interface [FCMNotificationPayload](-f-c-m-notification-payload/index.md) : [PubNub.BaseNotificationPayload](-base-notification-payload/index.md) |
| [FetchMessageItem](-fetch-message-item/index.md) | [js]<br>interface [FetchMessageItem](-fetch-message-item/index.md) |
| [FetchMessagesParameters](-fetch-messages-parameters/index.md) | [js]<br>interface [FetchMessagesParameters](-fetch-messages-parameters/index.md) |
| [FetchMessagesResponse](-fetch-messages-response/index.md) | [js]<br>interface [FetchMessagesResponse](-fetch-messages-response/index.md) |
| [FetchTimeResponse](-fetch-time-response/index.md) | [js]<br>interface [FetchTimeResponse](-fetch-time-response/index.md) |
| [FileEvent](-file-event/index.md) | [js]<br>interface [FileEvent](-file-event/index.md) |
| [FileInputParameters](-file-input-parameters/index.md) | [js]<br>interface [FileInputParameters](-file-input-parameters/index.md) |
| [FireParameters](-fire-parameters/index.md) | [js]<br>interface [FireParameters](-fire-parameters/index.md) |
| [GetAllMetadataParameters](-get-all-metadata-parameters/index.md) | [js]<br>interface [GetAllMetadataParameters](-get-all-metadata-parameters/index.md) |
| [GetChannelMembersParameters](-get-channel-members-parameters/index.md) | [js]<br>interface [GetChannelMembersParameters](-get-channel-members-parameters/index.md) : [PubNub.UUIDMembersParameters](-u-u-i-d-members-parameters/index.md) |
| [GetChannelMetadataParameters](-get-channel-metadata-parameters/index.md) | [js]<br>interface [GetChannelMetadataParameters](-get-channel-metadata-parameters/index.md) |
| [GetMembershipsParametersv2](-get-memberships-parametersv2/index.md) | [js]<br>interface [GetMembershipsParametersv2](-get-memberships-parametersv2/index.md) : [PubNub.ChannelMembersParameters](-channel-members-parameters/index.md) |
| [GetMessageActionsParameters](-get-message-actions-parameters/index.md) | [js]<br>interface [GetMessageActionsParameters](-get-message-actions-parameters/index.md) |
| [GetMessageActionsResponse](-get-message-actions-response/index.md) | [js]<br>interface [GetMessageActionsResponse](-get-message-actions-response/index.md) |
| [GetStateParameters](-get-state-parameters/index.md) | [js]<br>interface [GetStateParameters](-get-state-parameters/index.md) |
| [GetStateResponse](-get-state-response/index.md) | [js]<br>interface [GetStateResponse](-get-state-response/index.md) |
| [GetUUIDMetadataParameters](-get-u-u-i-d-metadata-parameters/index.md) | [js]<br>interface [GetUUIDMetadataParameters](-get-u-u-i-d-metadata-parameters/index.md) |
| [GrantParameters](-grant-parameters/index.md) | [js]<br>interface [GrantParameters](-grant-parameters/index.md) |
| [GrantTokenParameters](-grant-token-parameters/index.md) | [js]<br>interface [GrantTokenParameters](-grant-token-parameters/index.md) |
| [GrantTokenPermissions](-grant-token-permissions/index.md) | [js]<br>interface [GrantTokenPermissions](-grant-token-permissions/index.md) |
| [HasId](-has-id/index.md) | [js]<br>interface [HasId](-has-id/index.md) |
| [HasStatus](-has-status/index.md) | [js]<br>interface [HasStatus](-has-status/index.md) |
| [HereNowChannelData](-here-now-channel-data/index.md) | [js]<br>interface [HereNowChannelData](-here-now-channel-data/index.md) |
| [HereNowOccupantData](-here-now-occupant-data/index.md) | [js]<br>interface [HereNowOccupantData](-here-now-occupant-data/index.md) |
| [HereNowParameters](-here-now-parameters/index.md) | [js]<br>interface [HereNowParameters](-here-now-parameters/index.md) |
| [HereNowResponse](-here-now-response/index.md) | [js]<br>interface [HereNowResponse](-here-now-response/index.md) |
| [HistoryMessage](-history-message/index.md) | [js]<br>interface [HistoryMessage](-history-message/index.md) |
| [HistoryParameters](-history-parameters/index.md) | [js]<br>interface [HistoryParameters](-history-parameters/index.md) |
| [HistoryResponse](-history-response/index.md) | [js]<br>interface [HistoryResponse](-history-response/index.md) |
| [IncludeCustomFields](-include-custom-fields/index.md) | [js]<br>interface [IncludeCustomFields](-include-custom-fields/index.md) |
| [IncludeOptions](-include-options/index.md) | [js]<br>interface [IncludeOptions](-include-options/index.md) |
| [KeepAliveSettings](-keep-alive-settings/index.md) | [js]<br>interface [KeepAliveSettings](-keep-alive-settings/index.md) |
| [LegacyCryptor](-legacy-cryptor/index.md) | [js]<br>interface [LegacyCryptor](-legacy-cryptor/index.md)&lt;[T](-legacy-cryptor/index.md)&gt; |
| [ListAllGroupsResponse](-list-all-groups-response/index.md) | [js]<br>interface [ListAllGroupsResponse](-list-all-groups-response/index.md) |
| [ListChannelsParameters](-list-channels-parameters/index.md) | [js]<br>interface [ListChannelsParameters](-list-channels-parameters/index.md) |
| [ListChannelsResponse](-list-channels-response/index.md) | [js]<br>interface [ListChannelsResponse](-list-channels-response/index.md) |
| [ListenerParameters](-listener-parameters/index.md) | [js]<br>interface [ListenerParameters](-listener-parameters/index.md) |
| [ListFilesParameters](-list-files-parameters/index.md) | [js]<br>interface [ListFilesParameters](-list-files-parameters/index.md) |
| [ListFilesResponse](-list-files-response/index.md) | [js]<br>interface [ListFilesResponse](-list-files-response/index.md) |
| [MembershipIncludeOptions](-membership-include-options/index.md) | [js]<br>interface [MembershipIncludeOptions](-membership-include-options/index.md) |
| [MessageAction](-message-action/index.md) | [js]<br>interface [MessageAction](-message-action/index.md) |
| [MessageActionEvent](-message-action-event/index.md) | [js]<br>interface [MessageActionEvent](-message-action-event/index.md) |
| [MessageCountsParameters](-message-counts-parameters/index.md) | [js]<br>interface [MessageCountsParameters](-message-counts-parameters/index.md) |
| [MessageCountsResponse](-message-counts-response/index.md) | [js]<br>interface [MessageCountsResponse](-message-counts-response/index.md) |
| [MessageEvent](-message-event/index.md) | [js]<br>interface [MessageEvent](-message-event/index.md) |
| [MetadataIncludeOptions](-metadata-include-options/index.md) | [js]<br>interface [MetadataIncludeOptions](-metadata-include-options/index.md) |
| [MetadataPage](-metadata-page/index.md) | [js]<br>interface [MetadataPage](-metadata-page/index.md) |
| [NotificationsPayload](-notifications-payload/index.md) | [js]<br>interface [NotificationsPayload](-notifications-payload/index.md) |
| [ObjectParam](-object-param/index.md) | [js]<br>interface [ObjectParam](-object-param/index.md) |
| [Page](-page/index.md) | [js]<br>interface [Page](-page/index.md) |
| [ParsedGrantToken](-parsed-grant-token/index.md) | [js]<br>interface [ParsedGrantToken](-parsed-grant-token/index.md) : [PubNub.GrantTokenParameters](-grant-token-parameters/index.md) |
| [PatternsOrResources](-patterns-or-resources/index.md) | [js]<br>interface [PatternsOrResources](-patterns-or-resources/index.md) |
| [PNConfiguration](-p-n-configuration/index.md) | [js]<br>interface [PNConfiguration](-p-n-configuration/index.md) : [Partial](../-partial/index.md) |
| [PresenceEvent](-presence-event/index.md) | [js]<br>interface [PresenceEvent](-presence-event/index.md) |
| [PublishFileParameters](-publish-file-parameters/index.md) | [js]<br>interface [PublishFileParameters](-publish-file-parameters/index.md) |
| [PublishFileResponse](-publish-file-response/index.md) | [js]<br>interface [PublishFileResponse](-publish-file-response/index.md) |
| [PublishParameters](-publish-parameters/index.md) | [js]<br>interface [PublishParameters](-publish-parameters/index.md) |
| [PublishResponse](-publish-response/index.md) | [js]<br>interface [PublishResponse](-publish-response/index.md) |
| [PubNubFileType](-pub-nub-file-type/index.md) | [js]<br>interface [PubNubFileType](-pub-nub-file-type/index.md) |
| [PubnubStatus](-pubnub-status/index.md) | [js]<br>interface [PubnubStatus](-pubnub-status/index.md) |
| [Push](-push/index.md) | [js]<br>interface [Push](-push/index.md) |
| [PushChannelParameters](-push-channel-parameters/index.md) | [js]<br>interface [PushChannelParameters](-push-channel-parameters/index.md) |
| [PushDeviceParameters](-push-device-parameters/index.md) | [js]<br>interface [PushDeviceParameters](-push-device-parameters/index.md) |
| [PushListChannelsResponse](-push-list-channels-response/index.md) | [js]<br>interface [PushListChannelsResponse](-push-list-channels-response/index.md) |
| [ReconnectParameters](-reconnect-parameters/index.md) | [js]<br>interface [ReconnectParameters](-reconnect-parameters/index.md) |
| [RemoveChannelMembersParameters](-remove-channel-members-parameters/index.md) | [js]<br>interface [RemoveChannelMembersParameters](-remove-channel-members-parameters/index.md) : [PubNub.UUIDMembersParameters](-u-u-i-d-members-parameters/index.md) |
| [RemoveChannelMetadataEvent](-remove-channel-metadata-event/index.md) | [js]<br>interface [RemoveChannelMetadataEvent](-remove-channel-metadata-event/index.md) : [PubNub.BaseObjectsEvent](-base-objects-event/index.md) |
| [RemoveChannelMetadataParameters](-remove-channel-metadata-parameters/index.md) | [js]<br>interface [RemoveChannelMetadataParameters](-remove-channel-metadata-parameters/index.md) |
| [RemoveChannelParameters](-remove-channel-parameters/index.md) | [js]<br>interface [RemoveChannelParameters](-remove-channel-parameters/index.md) |
| [RemoveMembershipEvent](-remove-membership-event/index.md) | [js]<br>interface [RemoveMembershipEvent](-remove-membership-event/index.md) : [PubNub.BaseObjectsEvent](-base-objects-event/index.md) |
| [RemoveMembershipsParameters](-remove-memberships-parameters/index.md) | [js]<br>interface [RemoveMembershipsParameters](-remove-memberships-parameters/index.md) : [PubNub.ChannelMembersParameters](-channel-members-parameters/index.md) |
| [RemoveMessageActionParameters](-remove-message-action-parameters/index.md) | [js]<br>interface [RemoveMessageActionParameters](-remove-message-action-parameters/index.md) |
| [RemoveUUIDMetadataEvent](-remove-u-u-i-d-metadata-event/index.md) | [js]<br>interface [RemoveUUIDMetadataEvent](-remove-u-u-i-d-metadata-event/index.md) : [PubNub.BaseObjectsEvent](-base-objects-event/index.md) |
| [RemoveUUIDMetadataParameters](-remove-u-u-i-d-metadata-parameters/index.md) | [js]<br>interface [RemoveUUIDMetadataParameters](-remove-u-u-i-d-metadata-parameters/index.md) |
| [RevokeTokenResponse](-revoke-token-response/index.md) | [js]<br>interface [RevokeTokenResponse](-revoke-token-response/index.md) |
| [SendFileParameters](-send-file-parameters/index.md) | [js]<br>interface [SendFileParameters](-send-file-parameters/index.md) |
| [SendFileResponse](-send-file-response/index.md) | [js]<br>interface [SendFileResponse](-send-file-response/index.md) |
| [SetChannelMembersParameters](-set-channel-members-parameters/index.md) | [js]<br>interface [SetChannelMembersParameters](-set-channel-members-parameters/index.md) : [PubNub.UUIDMembersParameters](-u-u-i-d-members-parameters/index.md) |
| [SetChannelMetadataEvent](-set-channel-metadata-event/index.md) | [js]<br>interface [SetChannelMetadataEvent](-set-channel-metadata-event/index.md) : [PubNub.BaseObjectsEvent](-base-objects-event/index.md) |
| [SetChannelMetadataParameters](-set-channel-metadata-parameters/index.md) | [js]<br>interface [SetChannelMetadataParameters](-set-channel-metadata-parameters/index.md) |
| [SetCustom](-set-custom/index.md) | [js]<br>interface [SetCustom](-set-custom/index.md) |
| [SetMembershipEvent](-set-membership-event/index.md) | [js]<br>interface [SetMembershipEvent](-set-membership-event/index.md) : [PubNub.BaseObjectsEvent](-base-objects-event/index.md) |
| [SetMembershipObject](-set-membership-object/index.md) | [js]<br>interface [SetMembershipObject](-set-membership-object/index.md) |
| [SetMembershipsParameters](-set-memberships-parameters/index.md) | [js]<br>interface [SetMembershipsParameters](-set-memberships-parameters/index.md) : [PubNub.ChannelMembersParameters](-channel-members-parameters/index.md) |
| [SetStateParameters](-set-state-parameters/index.md) | [js]<br>interface [SetStateParameters](-set-state-parameters/index.md) |
| [SetStateResponse](-set-state-response/index.md) | [js]<br>interface [SetStateResponse](-set-state-response/index.md) |
| [SetUUIDMetadataEvent](-set-u-u-i-d-metadata-event/index.md) | [js]<br>interface [SetUUIDMetadataEvent](-set-u-u-i-d-metadata-event/index.md) : [PubNub.BaseObjectsEvent](-base-objects-event/index.md) |
| [SetUUIDMetadataParameters](-set-u-u-i-d-metadata-parameters/index.md) | [js]<br>interface [SetUUIDMetadataParameters](-set-u-u-i-d-metadata-parameters/index.md) |
| [SignalEvent](-signal-event/index.md) | [js]<br>interface [SignalEvent](-signal-event/index.md) |
| [SignalParameters](-signal-parameters/index.md) | [js]<br>interface [SignalParameters](-signal-parameters/index.md) |
| [SignalResponse](-signal-response/index.md) | [js]<br>interface [SignalResponse](-signal-response/index.md) |
| [StatusEvent](-status-event/index.md) | [js]<br>interface [StatusEvent](-status-event/index.md) |
| [StatusListenerParameters](-status-listener-parameters/index.md) | [js]<br>interface [StatusListenerParameters](-status-listener-parameters/index.md) |
| [StreamFileInput](-stream-file-input/index.md) | [js]<br>interface [StreamFileInput](-stream-file-input/index.md) |
| [SubscribeParameters](-subscribe-parameters/index.md) | [js]<br>interface [SubscribeParameters](-subscribe-parameters/index.md) |
| [SubscriptionOptions](-subscription-options/index.md) | [js]<br>interface [SubscriptionOptions](-subscription-options/index.md) |
| [SubscriptionSetParams](-subscription-set-params/index.md) | [js]<br>interface [SubscriptionSetParams](-subscription-set-params/index.md) |
| [T$10](-t$10/index.md) | [js]<br>interface [T$10](-t$10/index.md) |
| [T$11](-t$11/index.md) | [js]<br>interface [T$11](-t$11/index.md) |
| [T$13](-t$13/index.md) | [js]<br>interface [T$13](-t$13/index.md) |
| [T$15](-t$15/index.md) | [js]<br>interface [T$15](-t$15/index.md) |
| [T$37](-t$37/index.md) | [js]<br>interface [T$37](-t$37/index.md) |
| [T$5](-t$5/index.md) | [js]<br>interface [T$5](-t$5/index.md) |
| [T$6](-t$6/index.md) | [js]<br>interface [T$6](-t$6/index.md) |
| [T$7](-t$7/index.md) | [js]<br>interface [T$7](-t$7/index.md) |
| [T$9](-t$9/index.md) | [js]<br>interface [T$9](-t$9/index.md) |
| [UnsubscribeParameters](-unsubscribe-parameters/index.md) | [js]<br>interface [UnsubscribeParameters](-unsubscribe-parameters/index.md) |
| [UploadedFile](-uploaded-file/index.md) | [js]<br>interface [UploadedFile](-uploaded-file/index.md) |
| [UriFileInput](-uri-file-input/index.md) | [js]<br>interface [UriFileInput](-uri-file-input/index.md) |
| [UuidIncludeCustom](-uuid-include-custom/index.md) | [js]<br>interface [UuidIncludeCustom](-uuid-include-custom/index.md) |
| [UUIDMembershipObject](-u-u-i-d-membership-object/index.md) | [js]<br>interface [UUIDMembershipObject](-u-u-i-d-membership-object/index.md) : [PubNub.v2ObjectDataOmitId](v2-object-data-omit-id/index.md) |
| [UUIDMembersParameters](-u-u-i-d-members-parameters/index.md) | [js]<br>interface [UUIDMembersParameters](-u-u-i-d-members-parameters/index.md) |
| [UUIDMetadata](-u-u-i-d-metadata/index.md) | [js]<br>interface [UUIDMetadata](-u-u-i-d-metadata/index.md) : [PubNub.ObjectParam](-object-param/index.md), [PubNub.UUIDMetadataFieldsNullable](-u-u-i-d-metadata-fields-nullable/index.md), [Partial](../-partial/index.md) |
| [UUIDMetadataFieldsNullable](-u-u-i-d-metadata-fields-nullable/index.md) | [js]<br>interface [UUIDMetadataFieldsNullable](-u-u-i-d-metadata-fields-nullable/index.md) |
| [UUIDMetadataObject](-u-u-i-d-metadata-object/index.md) | [js]<br>interface [UUIDMetadataObject](-u-u-i-d-metadata-object/index.md) : [PubNub.v2ObjectData](v2-object-data/index.md), [PubNub.UUIDMetadataFieldsNullable](-u-u-i-d-metadata-fields-nullable/index.md) |
| [v2ObjectData](v2-object-data/index.md) | [js]<br>interface [v2ObjectData](v2-object-data/index.md) |
| [v2ObjectDataOmitId](v2-object-data-omit-id/index.md) | [js]<br>interface [v2ObjectDataOmitId](v2-object-data-omit-id/index.md) |
| [WhereNowParameters](-where-now-parameters/index.md) | [js]<br>interface [WhereNowParameters](-where-now-parameters/index.md) |
| [WhereNowResponse](-where-now-response/index.md) | [js]<br>interface [WhereNowResponse](-where-now-response/index.md) |

## Properties

| Name | Summary |
|---|---|
| [channelGroups](channel-groups.md) | [js]<br>open var [channelGroups](channel-groups.md): [PubNub.ChannelGroups](-channel-groups/index.md) |
| [objects](objects.md) | [js]<br>open var [objects](objects.md): [ObjectsFunctions](../-objects-functions/index.md) |
| [push](push.md) | [js]<br>open var [push](push.md): [PubNub.Push](-push/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addListener](add-listener.md) | [js]<br>open fun [addListener](add-listener.md)(params: [PubNub.ListenerParameters](-listener-parameters/index.md))<br>open fun [addListener](add-listener.md)(params: [PubNub.StatusListenerParameters](-status-listener-parameters/index.md)) |
| [addMessageAction](add-message-action.md) | [js]<br>open fun [addMessageAction](add-message-action.md)(params: [PubNub.AddMessageActionParameters](-add-message-action-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[AddMessageActionResult](../-add-message-action-result/index.md)&gt;<br>open fun [addMessageAction](add-message-action.md)(params: [PubNub.AddMessageActionParameters](-add-message-action-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[AddMessageActionResult](../-add-message-action-result/index.md)&gt;) |
| [decrypt](decrypt.md) | [js]<br>open fun [decrypt](decrypt.md)(data: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)<br>open fun [decrypt](decrypt.md)(data: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)<br>open fun [decrypt](decrypt.md)(data: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, customCipherKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = definedExternally): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)<br>open fun [decrypt](decrypt.md)(data: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, customCipherKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = definedExternally): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)<br>open fun [decrypt](decrypt.md)(data: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, customCipherKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = definedExternally, options: [PubNub.CryptoParameters](-crypto-parameters/index.md) = definedExternally): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)<br>open fun [decrypt](decrypt.md)(data: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?, customCipherKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = definedExternally, options: [PubNub.CryptoParameters](-crypto-parameters/index.md) = definedExternally): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html) |
| [deleteFile](delete-file.md) | [js]<br>open fun [deleteFile](delete-file.md)(params: [PubNub.FileInputParameters](-file-input-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.DeleteFileResponse](-delete-file-response/index.md)&gt;<br>open fun [deleteFile](delete-file.md)(params: [PubNub.FileInputParameters](-file-input-parameters/index.md), callback: [StatusCallback](../-status-callback/index.md)) |
| [deleteMessages](delete-messages.md) | [js]<br>open fun [deleteMessages](delete-messages.md)(params: [PubNub.DeleteMessagesParameters](-delete-messages-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)&gt;<br>open fun [deleteMessages](delete-messages.md)(params: [PubNub.DeleteMessagesParameters](-delete-messages-parameters/index.md), callback: [StatusCallback](../-status-callback/index.md)) |
| [destroy](destroy.md) | [js]<br>open fun [destroy](destroy.md)() |
| [disconnect](disconnect.md) | [js]<br>open fun [disconnect](disconnect.md)() |
| [downloadFile](download-file.md) | [js]<br>open fun [downloadFile](download-file.md)(params: [PubNub.DownloadFileParameters](-download-file-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;<br>open fun [downloadFile](download-file.md)(params: [PubNub.DownloadFileParameters](-download-file-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;) |
| [encrypt](encrypt.md) | [js]<br>open fun [encrypt](encrypt.md)(data: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), customCipherKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) = definedExternally, options: [PubNub.CryptoParameters](-crypto-parameters/index.md) = definedExternally): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [fetchMessages](fetch-messages.md) | [js]<br>open fun [fetchMessages](fetch-messages.md)(params: [PubNub.FetchMessagesParameters](-fetch-messages-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.FetchMessagesResponse](-fetch-messages-response/index.md)&gt;<br>open fun [fetchMessages](fetch-messages.md)(params: [PubNub.FetchMessagesParameters](-fetch-messages-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.FetchMessagesResponse](-fetch-messages-response/index.md)&gt;) |
| [fire](fire.md) | [js]<br>open fun [fire](fire.md)(params: [PubNub.FireParameters](-fire-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.PublishResponse](-publish-response/index.md)&gt;<br>open fun [fire](fire.md)(params: [PubNub.FireParameters](-fire-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.PublishResponse](-publish-response/index.md)&gt;) |
| [getFileUrl](get-file-url.md) | [js]<br>open fun [getFileUrl](get-file-url.md)(params: [PubNub.FileInputParameters](-file-input-parameters/index.md)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [getFilterExpression](get-filter-expression.md) | [js]<br>open fun [getFilterExpression](get-filter-expression.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [getMessageActions](get-message-actions.md) | [js]<br>open fun [getMessageActions](get-message-actions.md)(params: [PubNub.GetMessageActionsParameters](-get-message-actions-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.GetMessageActionsResponse](-get-message-actions-response/index.md)&gt;<br>open fun [getMessageActions](get-message-actions.md)(params: [PubNub.GetMessageActionsParameters](-get-message-actions-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.GetMessageActionsResponse](-get-message-actions-response/index.md)&gt;) |
| [getState](get-state.md) | [js]<br>open fun [getState](get-state.md)(params: [PubNub.GetStateParameters](-get-state-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.GetStateResponse](-get-state-response/index.md)&gt;<br>open fun [getState](get-state.md)(params: [PubNub.GetStateParameters](-get-state-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.GetStateResponse](-get-state-response/index.md)&gt;) |
| [getSubscribedChannelGroups](get-subscribed-channel-groups.md) | [js]<br>open fun [getSubscribedChannelGroups](get-subscribed-channel-groups.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [getSubscribedChannels](get-subscribed-channels.md) | [js]<br>open fun [getSubscribedChannels](get-subscribed-channels.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |
| [getToken](get-token.md) | [js]<br>open fun [getToken](get-token.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [getUUID](get-u-u-i-d.md) | [js]<br>open fun [getUUID](get-u-u-i-d.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [grant](grant.md) | [js]<br>open fun [grant](grant.md)(params: [PubNub.GrantParameters](-grant-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)&gt;<br>open fun [grant](grant.md)(params: [PubNub.GrantParameters](-grant-parameters/index.md), callback: [StatusCallback](../-status-callback/index.md)) |
| [grantToken](grant-token.md) | [js]<br>open fun [grantToken](grant-token.md)(params: [PubNub.GrantTokenParameters](-grant-token-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>open fun [grantToken](grant-token.md)(params: [PubNub.GrantTokenParameters](-grant-token-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) |
| [hereNow](here-now.md) | [js]<br>open fun [hereNow](here-now.md)(params: [PubNub.HereNowParameters](-here-now-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.HereNowResponse](-here-now-response/index.md)&gt;<br>open fun [hereNow](here-now.md)(params: [PubNub.HereNowParameters](-here-now-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.HereNowResponse](-here-now-response/index.md)&gt;) |
| [history](history.md) | [js]<br>open fun [history](history.md)(params: [PubNub.HistoryParameters](-history-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.HistoryResponse](-history-response/index.md)&gt;<br>open fun [history](history.md)(params: [PubNub.HistoryParameters](-history-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.HistoryResponse](-history-response/index.md)&gt;) |
| [listFiles](list-files.md) | [js]<br>open fun [listFiles](list-files.md)(params: [PubNub.ListFilesParameters](-list-files-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.ListFilesResponse](-list-files-response/index.md)&gt;<br>open fun [listFiles](list-files.md)(params: [PubNub.ListFilesParameters](-list-files-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.ListFilesResponse](-list-files-response/index.md)&gt;) |
| [messageCounts](message-counts.md) | [js]<br>open fun [messageCounts](message-counts.md)(params: [PubNub.MessageCountsParameters](-message-counts-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.MessageCountsResponse](-message-counts-response/index.md)&gt;<br>open fun [messageCounts](message-counts.md)(params: [PubNub.MessageCountsParameters](-message-counts-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.MessageCountsResponse](-message-counts-response/index.md)&gt;) |
| [parseToken](parse-token.md) | [js]<br>open fun [parseToken](parse-token.md)(params: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [PubNub.ParsedGrantToken](-parsed-grant-token/index.md) |
| [publish](publish.md) | [js]<br>open fun [publish](publish.md)(params: [PubNub.PublishParameters](-publish-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.PublishResponse](-publish-response/index.md)&gt;<br>open fun [publish](publish.md)(params: [PubNub.PublishParameters](-publish-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.PublishResponse](-publish-response/index.md)&gt;) |
| [publishFile](publish-file.md) | [js]<br>open fun [publishFile](publish-file.md)(params: [PubNub.PublishFileParameters](-publish-file-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.PublishFileResponse](-publish-file-response/index.md)&gt;<br>open fun [publishFile](publish-file.md)(params: [PubNub.PublishFileParameters](-publish-file-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.PublishFileResponse](-publish-file-response/index.md)&gt;) |
| [reconnect](reconnect.md) | [js]<br>open fun [reconnect](reconnect.md)(params: [PubNub.ReconnectParameters](-reconnect-parameters/index.md)) |
| [removeListener](remove-listener.md) | [js]<br>open fun [removeListener](remove-listener.md)(params: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)) |
| [removeMessageAction](remove-message-action.md) | [js]<br>open fun [removeMessageAction](remove-message-action.md)(params: [PubNub.RemoveMessageActionParameters](-remove-message-action-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[RemoveMessageActionResult](../-remove-message-action-result/index.md)&gt;<br>open fun [removeMessageAction](remove-message-action.md)(params: [PubNub.RemoveMessageActionParameters](-remove-message-action-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[RemoveMessageActionResult](../-remove-message-action-result/index.md)&gt;) |
| [revokeToken](revoke-token.md) | [js]<br>open fun [revokeToken](revoke-token.md)(params: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.RevokeTokenResponse](-revoke-token-response/index.md)&gt;<br>open fun [revokeToken](revoke-token.md)(params: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), callback: [Callback](../-callback/index.md)&lt;[PubNub.RevokeTokenResponse](-revoke-token-response/index.md)&gt;) |
| [sendFile](send-file.md) | [js]<br>open fun [sendFile](send-file.md)(params: [PubNub.SendFileParameters](-send-file-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.SendFileResponse](-send-file-response/index.md)&gt;<br>open fun [sendFile](send-file.md)(params: [PubNub.SendFileParameters](-send-file-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.SendFileResponse](-send-file-response/index.md)&gt;) |
| [setAuthKey](set-auth-key.md) | [js]<br>open fun [setAuthKey](set-auth-key.md)(authKey: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |
| [setFilterExpression](set-filter-expression.md) | [js]<br>open fun [setFilterExpression](set-filter-expression.md)(filterExpression: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |
| [setState](set-state.md) | [js]<br>open fun [setState](set-state.md)(params: [PubNub.SetStateParameters](-set-state-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.SetStateResponse](-set-state-response/index.md)&gt;<br>open fun [setState](set-state.md)(params: [PubNub.SetStateParameters](-set-state-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.SetStateResponse](-set-state-response/index.md)&gt;) |
| [setToken](set-token.md) | [js]<br>open fun [setToken](set-token.md)(params: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?) |
| [setUUID](set-u-u-i-d.md) | [js]<br>open fun [setUUID](set-u-u-i-d.md)(uuid: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |
| [signal](signal.md) | [js]<br>open fun [signal](signal.md)(params: [PubNub.SignalParameters](-signal-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.SignalResponse](-signal-response/index.md)&gt;<br>open fun [signal](signal.md)(params: [PubNub.SignalParameters](-signal-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.SignalResponse](-signal-response/index.md)&gt;) |
| [stop](stop.md) | [js]<br>open fun [stop](stop.md)() |
| [subscribe](subscribe.md) | [js]<br>open fun [subscribe](subscribe.md)(params: [PubNub.SubscribeParameters](-subscribe-parameters/index.md)) |
| [time](time.md) | [js]<br>open fun [time](time.md)(): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.FetchTimeResponse](-fetch-time-response/index.md)&gt;<br>open fun [time](time.md)(callback: [Callback](../-callback/index.md)&lt;[PubNub.FetchTimeResponse](-fetch-time-response/index.md)&gt;) |
| [unsubscribe](unsubscribe.md) | [js]<br>open fun [unsubscribe](unsubscribe.md)(params: [PubNub.UnsubscribeParameters](-unsubscribe-parameters/index.md)) |
| [unsubscribeAll](unsubscribe-all.md) | [js]<br>open fun [unsubscribeAll](unsubscribe-all.md)() |
| [whereNow](where-now.md) | [js]<br>open fun [whereNow](where-now.md)(params: [PubNub.WhereNowParameters](-where-now-parameters/index.md)): [Promise](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.js/-promise/index.html)&lt;[PubNub.WhereNowResponse](-where-now-response/index.md)&gt;<br>open fun [whereNow](where-now.md)(params: [PubNub.WhereNowParameters](-where-now-parameters/index.md), callback: [Callback](../-callback/index.md)&lt;[PubNub.WhereNowResponse](-where-now-response/index.md)&gt;) |

@file:Suppress(
    "INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS"
)

import PubNub.GetAllMetadataParameters
import PubNub.GetChannelMembersParameters
import PubNub.GetChannelMetadataParameters
import PubNub.GetMembershipsParametersv2
import PubNub.GetUUIDMetadataParameters
import PubNub.MessageAction
import PubNub.RemoveChannelMembersParameters
import PubNub.RemoveChannelMetadataParameters
import PubNub.RemoveMembershipsParameters
import PubNub.RemoveUUIDMetadataParameters
import PubNub.SetChannelMembersParameters
import PubNub.SetChannelMetadataParameters
import PubNub.SetMembershipsParameters
import PubNub.SetUUIDMetadataParameters
import com.pubnub.api.JsonElement
import com.pubnub.kmp.JsMap
import com.pubnub.kmp.Optional
import org.khronos.webgl.ArrayBuffer
import org.w3c.files.Blob
import org.w3c.files.File
import kotlin.js.Date
import kotlin.js.Json
import kotlin.js.Promise

typealias SetUUIDMetadataResponse = ObjectsResponse<PubNub.UUIDMetadataObject>

typealias RemoveUUIDMetadataResponse = ObjectsResponse<Any>

typealias GetAllUUIDMetadataResponse = PagedObjectsResponse<PubNub.UUIDMetadataObject>

typealias GetUUIDMetadataResponse = ObjectsResponse<PubNub.UUIDMetadataObject>

typealias SetChannelMetadataResponse = ObjectsResponse<PubNub.ChannelMetadataObject>

typealias RemoveChannelMetadataResponse = ObjectsResponse<Any>

typealias GetAllChannelMetadataResponse = PagedObjectsResponse<PubNub.ChannelMetadataObject>

typealias GetChannelMetadataResponse = ObjectsResponse<PubNub.ChannelMetadataObject>

typealias ManageChannelMembersResponse = PagedObjectsResponse<PubNub.UUIDMembershipObject>

typealias ManageMembershipsResponse = PagedObjectsResponse<PubNub.ChannelMembershipObject>

typealias Callback<ResponseType> = (status: PubNub.PubnubStatus, response: ResponseType) -> Unit

typealias StatusCallback = (status: PubNub.PubnubStatus) -> Unit

external interface ObjectsResponse<DataType> {
    var status: Number
    var data: DataType
}

external interface PagedObjectsResponse<DataType> : ObjectsResponse<Array<DataType>> {
    var prev: String?
    var next: String?
    var totalCount: Number?
}

external interface ObjectsFunctions {
    fun setUUIDMetadata(params: SetUUIDMetadataParameters, callback: Callback<SetUUIDMetadataResponse>)
    fun setUUIDMetadata(params: SetUUIDMetadataParameters): Promise<SetUUIDMetadataResponse>
    fun removeUUIDMetadata(callback: Callback<RemoveUUIDMetadataResponse>)
    fun removeUUIDMetadata(params: RemoveUUIDMetadataParameters): Promise<RemoveUUIDMetadataResponse>
    fun removeUUIDMetadata(): Promise<RemoveUUIDMetadataResponse>
    fun removeUUIDMetadata(params: RemoveUUIDMetadataParameters, callback: Callback<RemoveUUIDMetadataResponse>)
    fun getAllUUIDMetadata(callback: Callback<GetAllUUIDMetadataResponse>)
    fun getAllUUIDMetadata(params: GetAllMetadataParameters): Promise<GetAllUUIDMetadataResponse>
    fun getAllUUIDMetadata(): Promise<GetAllUUIDMetadataResponse>
    fun getAllUUIDMetadata(params: GetAllMetadataParameters, callback: Callback<GetAllUUIDMetadataResponse>)
    fun getUUIDMetadata(callback: Callback<GetUUIDMetadataResponse>)
    fun getUUIDMetadata(params: GetUUIDMetadataParameters): Promise<GetUUIDMetadataResponse>
    fun getUUIDMetadata(): Promise<GetUUIDMetadataResponse>
    fun getUUIDMetadata(params: GetUUIDMetadataParameters, callback: Callback<GetUUIDMetadataResponse>)
    fun setChannelMetadata(params: SetChannelMetadataParameters, callback: Callback<SetChannelMetadataResponse>)
    fun setChannelMetadata(params: SetChannelMetadataParameters): Promise<SetChannelMetadataResponse>
    fun removeChannelMetadata(
        params: RemoveChannelMetadataParameters, callback: Callback<RemoveChannelMetadataResponse>
    )

    fun removeChannelMetadata(params: RemoveChannelMetadataParameters): Promise<RemoveChannelMetadataResponse>
    fun getAllChannelMetadata(callback: Callback<GetAllChannelMetadataResponse>)
    fun getAllChannelMetadata(params: GetAllMetadataParameters): Promise<GetAllChannelMetadataResponse>
    fun getAllChannelMetadata(): Promise<GetAllChannelMetadataResponse>
    fun getAllChannelMetadata(params: GetAllMetadataParameters, callback: Callback<GetAllChannelMetadataResponse>)
    fun getChannelMetadata(params: GetChannelMetadataParameters, callback: Callback<GetChannelMetadataResponse>)
    fun getChannelMetadata(params: GetChannelMetadataParameters): Promise<GetChannelMetadataResponse>
    fun getMemberships(callback: Callback<ManageMembershipsResponse>)
    fun getMemberships(params: GetMembershipsParametersv2): Promise<ManageMembershipsResponse>
    fun getMemberships(): Promise<ManageMembershipsResponse>
    fun getMemberships(params: GetMembershipsParametersv2, callback: Callback<ManageMembershipsResponse>)
    fun setMemberships(params: SetMembershipsParameters, callback: Callback<ManageMembershipsResponse>)
    fun setMemberships(params: SetMembershipsParameters): Promise<ManageMembershipsResponse>
    fun removeMemberships(params: RemoveMembershipsParameters, callback: Callback<ManageMembershipsResponse>)
    fun removeMemberships(params: RemoveMembershipsParameters): Promise<ManageMembershipsResponse>
    fun getChannelMembers(params: GetChannelMembersParameters, callback: Callback<ManageChannelMembersResponse>)
    fun getChannelMembers(params: GetChannelMembersParameters): Promise<ManageChannelMembersResponse>
    fun setChannelMembers(params: SetChannelMembersParameters, callback: Callback<ManageChannelMembersResponse>)
    fun setChannelMembers(params: SetChannelMembersParameters): Promise<ManageChannelMembersResponse>
    fun removeChannelMembers(params: RemoveChannelMembersParameters, callback: Callback<ManageChannelMembersResponse>)
    fun removeChannelMembers(params: RemoveChannelMembersParameters): Promise<ManageChannelMembersResponse>
}

external interface AddMessageActionResult {
    var data: MessageAction
}

external interface RemoveMessageActionResult {
    var data: Any
}

@JsModule("pubnub")
@JsNonModule
open external class PubNub(config: Any /* UUID | UserId */) {
    open var channelGroups: ChannelGroups
    open var push: Push
    open fun setUUID(uuid: String)
    open fun getUUID(): String
    open fun setAuthKey(authKey: String)
    open fun setFilterExpression(filterExpression: String)
    open fun getFilterExpression(): String
    open fun publish(params: PublishParameters, callback: Callback<PublishResponse>)
    open fun publish(params: PublishParameters): Promise<PublishResponse>
    open fun fire(params: FireParameters, callback: Callback<PublishResponse>)
    open fun fire(params: FireParameters): Promise<PublishResponse>
    open fun signal(params: SignalParameters, callback: Callback<SignalResponse>)
    open fun signal(params: SignalParameters): Promise<SignalResponse>
    open fun history(params: HistoryParameters, callback: Callback<HistoryResponse>)
    open fun history(params: HistoryParameters): Promise<HistoryResponse>
    open fun fetchMessages(params: FetchMessagesParameters, callback: Callback<FetchMessagesResponse>)
    open fun fetchMessages(params: FetchMessagesParameters): Promise<FetchMessagesResponse>
    open fun deleteMessages(params: DeleteMessagesParameters, callback: StatusCallback)
    open fun deleteMessages(params: DeleteMessagesParameters): Promise<Unit>
    open fun messageCounts(params: MessageCountsParameters, callback: Callback<MessageCountsResponse>)
    open fun messageCounts(params: MessageCountsParameters): Promise<MessageCountsResponse>
    open fun subscribe(params: SubscribeParameters)
    open fun unsubscribe(params: UnsubscribeParameters)
    open fun unsubscribeAll()
    open fun stop()
    open fun reconnect()
    open fun addListener(params: ListenerParameters)
    open fun addListener(params: StatusListenerParameters)
    open fun removeListener(params: Any)
    open fun getSubscribedChannels(): Array<String>
    open fun getSubscribedChannelGroups(): Array<String>
    open fun hereNow(params: HereNowParameters, callback: Callback<HereNowResponse>)
    open fun hereNow(params: HereNowParameters): Promise<HereNowResponse>
    open fun whereNow(params: WhereNowParameters, callback: Callback<WhereNowResponse>)
    open fun whereNow(params: WhereNowParameters): Promise<WhereNowResponse>
    open fun getState(params: GetStateParameters, callback: Callback<GetStateResponse>)
    open fun getState(params: GetStateParameters): Promise<GetStateResponse>
    open fun setState(params: SetStateParameters, callback: Callback<SetStateResponse>)
    open fun setState(params: SetStateParameters): Promise<SetStateResponse>
    open fun grant(params: GrantParameters, callback: StatusCallback)
    open fun grant(params: GrantParameters): Promise<Unit>
    open fun grantToken(params: GrantTokenParameters, callback: Callback<String>)
    open fun grantToken(params: GrantTokenParameters): Promise<String>
    open fun setToken(params: String?)
    open fun getToken(): String?
    open fun parseToken(params: String): ParsedGrantToken
    open fun revokeToken(params: String, callback: Callback<RevokeTokenResponse>)
    open fun revokeToken(params: String): Promise<RevokeTokenResponse>
    open fun listFiles(params: ListFilesParameters, callback: Callback<ListFilesResponse>)
    open fun listFiles(params: ListFilesParameters): Promise<ListFilesResponse>
    open fun sendFile(params: SendFileParameters, callback: Callback<SendFileResponse>)
    open fun sendFile(params: SendFileParameters): Promise<SendFileResponse>
    open fun downloadFile(params: DownloadFileParameters, callback: Callback<Any>)
    open fun downloadFile(params: DownloadFileParameters): Promise<Any>
    open fun getFileUrl(params: FileInputParameters): String
    open fun deleteFile(params: FileInputParameters, callback: StatusCallback)
    open fun deleteFile(params: FileInputParameters): Promise<DeleteFileResponse>
    open fun publishFile(params: PublishFileParameters, callback: Callback<PublishFileResponse>)
    open fun publishFile(params: PublishFileParameters): Promise<PublishFileResponse>
    open var objects: ObjectsFunctions
    open fun addMessageAction(params: AddMessageActionParameters, callback: Callback<AddMessageActionResult>)
    open fun addMessageAction(params: AddMessageActionParameters): Promise<AddMessageActionResult>
    open fun removeMessageAction(params: RemoveMessageActionParameters, callback: Callback<RemoveMessageActionResult>)
    open fun removeMessageAction(params: RemoveMessageActionParameters): Promise<RemoveMessageActionResult>
    open fun getMessageActions(params: GetMessageActionsParameters, callback: Callback<GetMessageActionsResponse>)
    open fun getMessageActions(params: GetMessageActionsParameters): Promise<GetMessageActionsResponse>
    open fun encrypt(
        data: String, customCipherKey: String = definedExternally, options: CryptoParameters = definedExternally
    ): String

    open fun decrypt(
        data: String?, customCipherKey: String = definedExternally, options: CryptoParameters = definedExternally
    ): Any

    open fun decrypt(data: String?): Any
    open fun decrypt(data: String?, customCipherKey: String = definedExternally): Any
    open fun decrypt(
        data: Any?, customCipherKey: String = definedExternally, options: CryptoParameters = definedExternally
    ): Any

    open fun decrypt(data: Any?): Any
    open fun decrypt(data: Any?, customCipherKey: String = definedExternally): Any
    open fun time(): Promise<FetchTimeResponse>
    open fun time(callback: Callback<FetchTimeResponse>)
    interface KeepAliveSettings {
        var keepAliveMsecs: Number?
        var freeSocketKeepAliveTimeout: Number?
        var timeout: Number?
        var maxSockets: Number?
        var maxFreeSockets: Number?
    }

    interface PNConfiguration : Partial {
        var userId: String
        var subscribeKey: String
        var publishKey: String?
        var cipherKey: String?
        var authKey: String?
        var logVerbosity: Boolean?
        var ssl: Boolean?
        var origin: dynamic /* String? | Array<String>? */
        var presenceTimeout: Number?
        var heartbeatInterval: Number?
        var restore: Boolean?
        var keepAlive: Boolean?
        var keepAliveSettings: KeepAliveSettings?
        var subscribeRequestTimeout: Number?
        var suppressLeaveEvents: Boolean?
        var secretKey: String?
        var requestMessageCountThreshold: Number?
        var autoNetworkDetection: Boolean?
        var listenToBrowserNetworkEvents: Boolean?
        var useRandomIVs: Boolean?
        var dedupeOnSubscribe: Boolean?
        var cryptoModule: CryptoModule?
        var retryConfiguration: dynamic /* LinearRetryPolicyConfiguration? | ExponentialRetryPolicyConfiguration? */
        var enableEventEngine: Boolean?
        var maintainPresenceState: Boolean?
    }

    interface MessageEvent {
        var channel: String
        var subscription: String
        var timetoken: String
        var message: Any
        var publisher: String
        var error: String?
        var actualChannel: String
        var subscribedChannel: String
        var userMetadata: Any?

    }

    interface StatusEvent {
        var category: String
        var operation: String
        var affectedChannels: Array<String>
        var subscribedChannels: Array<String>
        var affectedChannelGroups: Array<String>
        var lastTimetoken: String
        var currentTimetoken: String
        var statusCode: Number?
    }

    interface PresenceEvent {
        var action: String /* "join" | "leave" | "state-change" | "timeout" */
        var channel: String
        var occupancy: Number
        var state: Any?
        var subscription: String
        var timestamp: Number
        var timetoken: String
        var uuid: String
        var actualChannel: String
        var subscribedChannel: String
    }

    interface SignalEvent {
        var channel: String
        var subscription: String
        var timetoken: String
        var message: Any
        var publisher: String
    }

    interface MessageActionEvent {
        var channel: String
        var publisher: String
        var subscription: String?
        var timetoken: String
        var event: String
        var data: MessageAction
    }

    interface `T$5` {
        var id: String
        var name: String
        var url: String
    }

    interface FileEvent {
        var channel: String
        var subscription: String
        var publisher: String
        var timetoken: String
        var message: Any
        var file: `T$5`
        var userMetadata: Any?
    }

    interface `T$6` {
        var source: String
        var version: String
        var event: String /* "set" | "delete" */
        var type: String /* "uuid" | "channel" | "membership" */
        var data: Any?
    }

    interface BaseObjectsEvent {
        var channel: String
        var message: `T$6`
        var subscription: String?
        var publisher: String?
        var timetoken: String
    }

    interface `T$7` {
        var event: String /* "set" */
        var type: String /* "uuid" */
        var data: UUIDMetadataObject
    }

    interface SetUUIDMetadataEvent : BaseObjectsEvent
    interface HasId {
        var id: String
    }

    interface `T$9` {
        var event: String /* "delete" */
        var type: String /* "uuid" */
        var data: HasId
    }

    interface RemoveUUIDMetadataEvent : BaseObjectsEvent
    interface `T$10` {
        var event: String /* "set" */
        var type: String /* "channel" */
        var data: ChannelMetadataObject
    }

    interface SetChannelMetadataEvent : BaseObjectsEvent
    interface `T$11` {
        var event: String /* "delete" */
        var type: String /* "channel" */
        var data: HasId
    }

    interface RemoveChannelMetadataEvent : BaseObjectsEvent
    interface SetMembershipObject {
        var channel: HasId
        var uuid: HasId
        var custom: CustomObject?
        var updated: String
        var eTag: String
    }

    interface `T$13` {
        var event: String /* "set" */
        var type: String /* "membership" */
        var data: SetMembershipObject
    }

    interface SetMembershipEvent : BaseObjectsEvent
    interface DeleteMembershipObject {
        var channel: HasId
        var uuid: HasId
    }

    interface `T$15` {
        var event: String /* "delete" */
        var type: String /* "membership" */
        var data: DeleteMembershipObject
    }

    interface RemoveMembershipEvent : BaseObjectsEvent
    interface PublishParameters {
        var message: Any
        var channel: String
        var storeInHistory: Boolean?
        var sendByPost: Boolean?
        var meta: Any?
        var ttl: Number?
    }

    interface PublishResponse {
        var timetoken: Number
    }

    interface SignalParameters {
        var message: Any
        var channel: String
    }

    interface SignalResponse {
        var timetoken: Number
    }

    interface HistoryParameters {
        var channel: String
        var count: Number
        var stringifiedTimeToken: Boolean?
        var includeTimetoken: Boolean?
        var reverse: Boolean?
        var start: String?
        var end: String?
        var includeMeta: Boolean?

    }

    interface HistoryMessage {
        var entry: Any
        var timetoken: String?
        var meta: Any?
    }

    interface HistoryResponse {
        var endTimeToken: String?
        var startTimeToken: String?
        var messages: Array<HistoryMessage>
    }

    interface FetchMessagesParameters {
        var channels: Array<String>
        var count: Number?
        var stringifiedTimeToken: Boolean?
        var start: String?
        var end: String?
        var withMessageActions: Boolean?
        var includeMessageType: Boolean?
        var includeUUID: Boolean?
        var includeMeta: Boolean?
        var includeMessageActions: Boolean?
    }

    interface Action {
        var uuid: String
        var actionTimetoken: String

    }

    interface ActionContentToAction : JsMap<Array<Action>>
    interface ActionTypeToActions : JsMap<ActionContentToAction>

    interface FetchMessageItem {
        var channel: String
        var message: JsonElement
        var timetoken: String
        var messageType: String?
        var uuid: String
        var error: String?
        var meta: Json?
        var actions: ActionTypeToActions?
    }

    interface ChannelsToFetchMessageItemsMap : JsMap<Array<FetchMessageItem>>

    interface Page {
        var url: String
        var start: String
        var max: Number
    }

    interface FetchMessagesResponse {
        var channels: ChannelsToFetchMessageItemsMap
        var more: Page?
    }

    interface DeleteMessagesParameters {
        var channel: String
        var start: String?
        var end: String?
    }

    interface MessageCountsParameters {
        var channels: Array<String>
        var channelTimetokens: dynamic /* Array<String> | Array<Number> */
    }

    interface MessageCountsResponse {
        var channels: JsMap<Number>
    }

    interface Push {
        fun addChannels(params: PushChannelParameters, callback: StatusCallback)
        fun addChannels(params: PushChannelParameters): Promise<Unit>
        fun listChannels(params: PushDeviceParameters, callback: Callback<PushListChannelsResponse>)
        fun listChannels(params: PushDeviceParameters): Promise<PushListChannelsResponse>
        fun removeChannels(params: PushChannelParameters, callback: StatusCallback)
        fun removeChannels(params: PushChannelParameters): Promise<Unit>
        fun deleteDevice(params: PushDeviceParameters, callback: StatusCallback)
        fun deleteDevice(params: PushDeviceParameters): Promise<Unit>
    }

    interface PushChannelParameters {
        var channels: Array<String>
        var device: String
        var pushGateway: String
        var environment: String?

        var topic: String?

    }

    interface PushDeviceParameters {
        var device: String
        var pushGateway: String
        var environment: String?

        var topic: String?

    }

    interface PushListChannelsResponse {
        var channels: Array<String>
    }

    interface PubnubStatus {
        var error: Boolean
        var category: String?

        var operation: String
        var statusCode: Number
        var errorData: Error?

    }

    interface FireParameters {
        var message: Any
        var channel: String
        var sendByPost: Boolean?

        var meta: Any?

    }

    interface SubscribeParameters {
        var channels: Array<String>?

        var channelGroups: Array<String>?

        var withPresence: Boolean?

        var timetoken: Number?

    }

    interface UnsubscribeParameters {
        var channels: Array<String>?

        var channelGroups: Array<String>?

    }

    interface ChannelGroups {
        fun addChannels(params: AddChannelParameters, callback: StatusCallback)
        fun addChannels(params: AddChannelParameters): Promise<Any>
        fun removeChannels(params: RemoveChannelParameters, callback: StatusCallback)
        fun removeChannels(params: RemoveChannelParameters): Promise<Any>
        fun listChannels(params: ListChannelsParameters, callback: Callback<ListChannelsResponse>)
        fun listChannels(params: ListChannelsParameters): Promise<ListChannelsResponse>
        fun listGroups(callback: Callback<ListAllGroupsResponse>)
        fun listGroups(): Promise<ListAllGroupsResponse>
        fun deleteGroup(params: DeleteGroupParameters, callback: StatusCallback)
        fun deleteGroup(params: DeleteGroupParameters): Promise<Any>
    }

    interface AddChannelParameters {
        var channels: Array<String>
        var channelGroup: String
    }

    interface RemoveChannelParameters {
        var channels: Array<String>
        var channelGroup: String
    }

    interface ListChannelsParameters {
        var channelGroup: String
    }

    interface DeleteGroupParameters {
        var channelGroup: String
    }

    interface ListAllGroupsResponse {
        var groups: Array<String>
    }

    interface ListChannelsResponse {
        var channels: Array<String>
    }

    interface ListenerParameters {
        val message: ((messageEvent: MessageEvent) -> Unit)?
        val presence: ((presenceEvent: PresenceEvent) -> Unit)?
        val signal: ((signalEvent: SignalEvent) -> Unit)?
        val messageAction: ((messageActionEvent: MessageActionEvent) -> Unit)?
        val file: ((fileEvent: FileEvent) -> Unit)?
        val objects: ((objectEvent: BaseObjectsEvent) -> Unit)?
    }

    interface StatusListenerParameters {
        val status: ((statusEvent: StatusEvent) -> Unit)?
    }

    interface HereNowParameters {
        var channels: Array<String>?

        var channelGroups: Array<String>?

        var includeUUIDs: Boolean?

        var includeState: Boolean?

    }

    interface HereNowOccupantData {
        var uuid: String
        var state: Any?

    }

    interface HereNowChannelData {
        var name: String
        var occupancy: Number
        var occupants: Array<HereNowOccupantData>
    }

    interface ChannelsToHereNowChannelData : JsMap<HereNowChannelData>

    interface HereNowResponse {
        var totalChannels: Number
        var totalOccupancy: Number
        var channels: ChannelsToHereNowChannelData
    }

    interface WhereNowParameters {
        var uuid: String?

    }

    interface WhereNowResponse {
        var channels: Array<String>
    }

    interface SetStateParameters {
        var channels: Array<String>?

        var channelGroups: Array<String>?

        var state: Any?

    }

    interface SetStateResponse {
        var state: Any
    }

    interface GetStateParameters {
        var uuid: String?

        var channels: Array<String>?

        var channelGroups: Array<String>?

    }

    interface GetStateResponse {
        var channels: Json
    }

    interface GrantParameters {
        var channels: Array<String>?

        var channelGroups: Array<String>?

        var uuids: Array<String>?

        var authKeys: Array<String>?

        var ttl: Number?

        var read: Boolean?

        var write: Boolean?

        var manage: Boolean?

        var delete: Boolean?

        var get: Boolean?

        var join: Boolean?

        var update: Boolean?

    }

    interface PatternsOrResources {
        var channels: JsMap<GrantTokenPermissions>?

        var groups: JsMap<GrantTokenPermissions>?

        var uuids: JsMap<GrantTokenPermissions>?

    }

    interface GrantTokenParameters {
        var ttl: Number
        var authorized_uuid: String?

        var resources: PatternsOrResources?

        var patterns: PatternsOrResources?

        var meta: Json?

    }

    interface ParsedGrantToken : GrantTokenParameters {
        var version: Number
        var timestamp: Number
        var signature: Any
    }

    interface GrantTokenPermissions {
        var read: Boolean?

        var write: Boolean?

        var manage: Boolean?

        var delete: Boolean?

        var get: Boolean?

        var join: Boolean?

        var update: Boolean?

    }

    interface RevokeTokenResponse {
        var status: Number
        var data: Any?
    }

    interface ActionParam {
        var type: String
        var value: String
    }

    interface AddMessageActionParameters {
        var channel: String
        var messageTimetoken: String
        var action: ActionParam
    }

    interface MessageAction {
        var type: String
        var value: String
        var uuid: String
        var actionTimetoken: String
        var messageTimetoken: String
    }

    interface RemoveMessageActionParameters {
        var channel: String
        var messageTimetoken: String
        var actionTimetoken: String
    }

    interface GetMessageActionsParameters {
        var channel: String
        var start: String?

        var end: String?

        var limit: Number?

    }

    interface GetMessageActionsResponse {
        var data: Array<MessageAction>
        var start: String?

        var end: String?

    }

    interface ListFilesParameters {
        var channel: String
        var limit: Number?

        var next: String?

    }

    interface SendFileParameters {
        var channel: String
        var file: dynamic /* StreamFileInput | BufferFileInput | UriFileInput */

        var message: Any?

        var cipherKey: String?

        var storeInHistory: Boolean?

        var ttl: Number?

        var meta: Any?

    }

    interface StreamFileInput {
        var stream: Any
        var name: String
        var mimeType: String?

    }

    interface BufferFileInput {
        var data: Any
        var name: String
        var mimeType: String?

    }

    interface UriFileInput {
        var uri: String
        var name: String
        var mimeType: String?

    }

    interface DownloadFileParameters {
        var channel: String
        var id: String
        var name: String
        var cipherKey: String?

    }

    interface FileInputParameters {
        var channel: String
        var id: String
        var name: String
    }

    interface PublishFileParameters {
        var channel: String
        var message: Any?

        var fileId: String
        var fileName: String
        var storeInHistory: Boolean?

        var ttl: Number?

        var meta: Any?

    }

    interface `T$29` {
        var name: String
        var id: String
        var size: Number
        var created: String
    }

    interface ListFilesResponse {
        var status: Number
        var data: Array<`T$29`>
        var next: String
        var count: Number
    }

    interface SendFileResponse {
        var timetoken: String
        var name: String
        var id: String
    }

    interface DeleteFileResponse {
        var status: Number
    }

    interface PublishFileResponse {
        var timetoken: String
    }
//    interface ObjectCustom {
//        @nativeGetter
//        operator fun get(key: String): dynamic /* String? | Number? | Boolean? */
//        @nativeSetter
//        operator fun set(key: String, value: String)
//        @nativeSetter
//        operator fun set(key: String, value: Number)
//        @nativeSetter
//        operator fun set(key: String, value: Boolean)
//    }

    interface CustomObject : JsMap<Any>

    interface v2ObjectDataOmitId {
        var eTag: String
        var updated: String
        var custom: CustomObject?

    }

    interface v2ObjectData {
        var id: String
        var eTag: String
        var updated: String
        var custom: CustomObject?

    }

    interface ObjectParam {
        var custom: CustomObject?

    }

    interface UUIDMetadataFieldsNullable {
        var name: String?
        var externalId: String?
        var profileUrl: String?
        var email: String?
        var status: String?
        var type: String?
    }

    interface UUIDMetadata : ObjectParam, UUIDMetadataFieldsNullable, Partial
    interface UUIDMetadataObject : v2ObjectData, UUIDMetadataFieldsNullable
    interface UuidIncludeCustom {
        var customFields: Boolean?

    }

    interface SetUUIDMetadataParameters {
        var uuid: String?

        var data: UUIDMetadata
        var include: UuidIncludeCustom?

    }

    interface RemoveUUIDMetadataParameters {
        var uuid: String?

    }

    interface MetadataIncludeOptions {
        var totalCount: Boolean?

        var customFields: Boolean?

    }

    interface MetadataPage {
        var next: String?

        var prev: String?

    }

    interface GetAllMetadataParameters {
        var include: MetadataIncludeOptions?

        var filter: String?

        var sort: JsMap<String>?

        var limit: Number?

        var page: MetadataPage?

    }

    interface GetUUIDMetadataParameters {
        var uuid: String?

        var include: UuidIncludeCustom?

    }

    interface ChannelMetadataFieldsPartial {
        var name: Optional<String>
        var description: Optional<String>
        var status: Optional<String>
        var type: Optional<String>
    }

    interface ChannelMetadataFieldsNullable {
        var name: String?
        var description: String?
        var status: String?
        var type: String?
    }

    interface ChannelMetadata : ObjectParam, ChannelMetadataFieldsNullable
    interface ChannelMetadataObject : v2ObjectData, ChannelMetadataFieldsNullable
    interface SetChannelMetadataParameters {
        var channel: String
        var data: ChannelMetadata
        var include: UuidIncludeCustom?

    }

    interface RemoveChannelMetadataParameters {
        var channel: String
    }

    interface IncludeCustomFields {
        var customFields: Boolean
    }

    interface GetChannelMetadataParameters {
        var channel: String
        var include: IncludeCustomFields?

    }

    interface HasStatus {
        var status: String?

    }

    interface UUIDMembershipObject : v2ObjectDataOmitId {
        var uuid: UUIDMetadataObject /* UUIDMetadataObject & HasStatus | HasId */
        var status: String?

    }

    interface ChannelMembershipObject : v2ObjectDataOmitId {
        var channel: ChannelMetadataObject?

        var status: String?

    }

    interface IncludeOptions {
        var totalCount: Boolean?

        var customFields: Boolean?

        var UUIDFields: Boolean?

        var customUUIDFields: Boolean?

        var statusField: Boolean?

        var UUIDStatusField: Boolean?

        var UUIDTypeField: Boolean?

    }

    interface UUIDMembersParameters {
        var include: IncludeOptions?

        var filter: String?

        var sort: JsMap<String>?

        var limit: Number?

        var page: MetadataPage?

    }

    interface MembershipIncludeOptions {
        var totalCount: Boolean?

        var customFields: Boolean?

        var channelFields: Boolean?

        var customChannelFields: Boolean?

        var statusField: Boolean?

        var channelStatusField: Boolean?

        var channelTypeField: Boolean?

    }

    interface ChannelMembersParameters {
        var include: MembershipIncludeOptions?

        var filter: String?

        var sort: Any?

        var limit: Number?

        var page: MetadataPage?

    }

    interface GetChannelMembersParameters : UUIDMembersParameters {
        var channel: String
    }

    interface GetMembershipsParametersv2 : ChannelMembersParameters {
        var uuid: String?

    }

    interface SetCustom {
        var id: String
        var custom: CustomObject?
        var status: String?

    }

    interface SetMembershipsParameters : ChannelMembersParameters {
        var uuid: String?

        var channels: Array<SetCustom /* String | SetCustom */>?

    }

    interface RemoveMembershipsParameters : ChannelMembersParameters {
        var uuid: String?

        var channels: Array<String>
    }

    interface SetChannelMembersParameters : UUIDMembersParameters {
        var channel: String
        var uuids: Array<SetCustom>
    }

    interface RemoveChannelMembersParameters : UUIDMembersParameters {
        var channel: String
        var uuids: Array<String>
    }

    interface CryptoParameters {
        var encryptKey: Boolean?

        var keyEncoding: String?

        var keyLength: Number?

        var mode: String?

    }

    interface FetchTimeResponse {
        var timetoken: Number
    }

    interface APNS2Configuration {
        var collapseId: String?

        var expirationDate: Date?

        var targets: Array<APNS2Target>
    }

    interface APNS2Target {
        var topic: String
        var environment: String? /* "development" | "production" */

        var excludedDevices: Array<String>?

    }

    interface BaseNotificationPayload {
        var subtitle: String?

        var payload: Any?
        var badge: Number?

        var sound: String?

        var title: String?

        var body: String?

    }

    interface APNSNotificationPayload : BaseNotificationPayload {
        var configurations: Array<APNS2Configuration>
        var apnsPushType: String?

        var isSilent: Boolean
    }

    interface MPNSNotificationPayload : BaseNotificationPayload {
        var backContent: String?

        var backTitle: String?

        var count: Number?

        var type: String?

    }

    interface FCMNotificationPayload : BaseNotificationPayload {
        var isSilent: Boolean
        var icon: String?

        var tag: String?

    }

    interface `T$37` {
        var apns: Any?
        var mpns: Any?
        var fcm: Any?
    }

    interface NotificationsPayload {
        var payload: `T$37`
        var debugging: Boolean
        var subtitle: String?

        var badge: Number?

        var sound: String?

        var title: String?

        var body: String?

        var apns: APNSNotificationPayload
        var mpns: MPNSNotificationPayload
        var fcm: FCMNotificationPayload
        fun buildPayload(platforms: Array<String>): Any?
    }

    open class CryptoModule(configuration: CryptoModuleConfiguration) {
        open var defaultCryptor: dynamic /* Cryptor | LegacyCryptor<PubNubFileType> */
        open var cryptors: Array<dynamic /* Cryptor | LegacyCryptor<PubNubFileType> */>
        open fun encrypt(data: ArrayBuffer): ArrayBuffer
        open fun encrypt(data: String): ArrayBuffer
        open fun decrypt(data: ArrayBuffer): dynamic /* ArrayBuffer | String */
        open fun decrypt(data: String): dynamic /* ArrayBuffer | String */
        open fun encryptFile(file: PubNubFileType, fd: PubNubFileType): Promise<PubNubFileType>
        open fun decryptFile(file: PubNubFileType, fd: PubNubFileType): Promise<PubNubFileType>

        companion object {
            fun legacyCryptoModule(configuration: CryptorConfiguration): CryptoModule
            fun aesCbcCryptoModule(configuration: CryptorConfiguration): CryptoModule
            fun withDefaultCryptor(defaultCryptor: Cryptor): CryptoModule
            fun withDefaultCryptor(defaultCryptor: LegacyCryptor<PubNubFileType>): CryptoModule
        }
    }

    interface PubNubFileType {
        var data: dynamic /* File | Blob */

        var name: String
        var mimeType: String
        fun create(config: Any): PubNubFileType
        fun toArrayBuffer(): ArrayBuffer
        fun toBlob(): Blob
        override fun toString(): String
        fun toFile(): File
    }

    interface CryptorConfiguration {
        var cipherKey: String
        var useRandomIVs: Boolean?

    }

    interface CryptoModuleConfiguration {
        var default: dynamic /* Cryptor | LegacyCryptor<PubNubFileType> */

        var cryptors: Array<dynamic /* Cryptor | LegacyCryptor<PubNubFileType> */>?

    }

    interface EncryptedDataType {
        var data: ArrayBuffer
        var metadata: ArrayBuffer?
    }

    interface LegacyCryptor<T>

    companion object {
        var CATEGORIES: Any
        var OPERATIONS: Any
        fun generateUUID(): String
        var LinearRetryPolicy: Any
        var ExponentialRetryPolicy: Any
        fun notificationPayload(title: String, body: String): NotificationsPayload
    }
}

external interface Partial

external interface UUID {
    var uuid: String
}

external interface UserId {
    var userId: String
}

external interface Cryptor

external interface Categories {
    var PNNetworkUpCategory: String
    var PNNetworkDownCategory: String
    var PNNetworkIssuesCategory: String
    var PNTimeoutCategory: String
    var PNBadRequestCategory: String
    var PNAccessDeniedCategory: String
    var PNUnknownCategory: String
    var PNReconnectedCategory: String
    var PNConnectedCategory: String
    var PNRequestMessageCountExceedCategory: String
    var PNMalformedResponseCategory: String
    var PNDisconnectedUnexpectedlyCategory: String
    var PNConnectionErrorCategory: String
}

external interface Operations {
    var PNTimeOperation: String
    var PNHistoryOperation: String
    var PNDeleteMessagesOperation: String
    var PNFetchMessagesOperation: String
    var PNMessageCountsOperation: String
    var PNSubscribeOperation: String
    var PNUnsubscribeOperation: String
    var PNPublishOperation: String
    var PNPushNotificationEnabledChannelsOperation: String
    var PNRemoveAllPushNotificationsOperation: String
    var PNWhereNowOperation: String
    var PNSetStateOperation: String
    var PNHereNowOperation: String
    var PNGetStateOperation: String
    var PNHeartbeatOperation: String
    var PNChannelGroupsOperation: String
    var PNRemoveGroupOperation: String
    var PNChannelsForGroupOperation: String
    var PNAddChannelsToGroupOperation: String
    var PNRemoveChannelsFromGroupOperation: String
    var PNAccessManagerGrant: String
    var PNAccessManagerAudit: String
    var PNCreateUserOperation: String
    var PNUpdateUserOperation: String
    var PNDeleteUserOperation: String
    var PNGetUsersOperation: String
    var PNCreateSpaceOperation: String
    var PNUpdateSpaceOperation: String
    var PNDeleteSpaceOperation: String
    var PNGetSpacesOperation: String
    var PNGetMembershipsOperation: String
    var PNGetMembersOperation: String
    var PNUpdateMembershipsOperation: String
    var PNAddMessageActionOperation: String
    var PNRemoveMessageActionOperation: String
    var PNGetMessageActionsOperation: String
}

external interface LinearRetryPolicyConfiguration {
    var delay: Number
    var maximumRetry: Number
}

external interface ExponentialRetryPolicyConfiguration {
    var minimumDelay: Number
    var maximumDelay: Number
    var maximumRetry: Number
}

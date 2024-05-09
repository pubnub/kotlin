@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")

import PubNub.ChannelMembershipObject
import PubNub.ChannelMetadataObject
import PubNub.GetAllMetadataParameters
import PubNub.GetChannelMembersParameters
import PubNub.GetChannelMetadataParameters
import PubNub.GetMembershipsParametersv2
import PubNub.GetUUIDMetadataParameters
import PubNub.MessageAction
import PubNub.MessageEvent
import PubNub.PubnubStatus
import PubNub.RemoveChannelMembersParameters
import PubNub.RemoveChannelMetadataParameters
import PubNub.RemoveMembershipsParameters
import PubNub.RemoveUUIDMetadataParameters
import PubNub.SetChannelMembersParameters
import PubNub.SetChannelMetadataParameters
import PubNub.SetMembershipsParameters
import PubNub.SetUUIDMetadataParameters
import PubNub.UUIDMembershipObject
import PubNub.UUIDMetadataObject
import com.pubnub.kmp.Optional
import org.khronos.webgl.ArrayBuffer
import org.w3c.files.Blob
import org.w3c.files.File
import kotlin.js.Date
import kotlin.js.Json
import kotlin.js.Promise

typealias PubnubData = MessageEvent

typealias SetUUIDMetadataResponse = ObjectsResponse<UUIDMetadataObject>

typealias RemoveUUIDMetadataResponse = ObjectsResponse<Any>

typealias GetAllUUIDMetadataResponse = PagedObjectsResponse<UUIDMetadataObject>

typealias GetUUIDMetadataResponse = ObjectsResponse<UUIDMetadataObject>

typealias SetChannelMetadataResponse = ObjectsResponse<ChannelMetadataObject>

typealias RemoveChannelMetadataResponse = ObjectsResponse<Any>

typealias GetAllChannelMetadataResponse = PagedObjectsResponse<ChannelMetadataObject>

typealias GetChannelMetadataResponse = ObjectsResponse<ChannelMetadataObject>

typealias ManageChannelMembersResponse = PagedObjectsResponse<UUIDMembershipObject>

typealias ManageMembershipsResponse = PagedObjectsResponse<ChannelMembershipObject>

typealias Callback<ResponseType> = (status: PubnubStatus, response: ResponseType) -> Unit

typealias StatusCallback = (status: PubnubStatus) -> Unit

external interface ObjectsResponse<DataType> {
    var status: Number
    var data: DataType
}

external interface PagedObjectsResponse<DataType> : ObjectsResponse<Array<DataType>> {
    var prev: String?
        get() = definedExternally
        set(value) = definedExternally
    var next: String?
        get() = definedExternally
        set(value) = definedExternally
    var totalCount: Number?
        get() = definedExternally
        set(value) = definedExternally
}


external interface ObjectsFunctions {
    fun  setUUIDMetadata(params: SetUUIDMetadataParameters, callback: Callback<SetUUIDMetadataResponse>)
    fun  setUUIDMetadata(params: SetUUIDMetadataParameters): Promise<SetUUIDMetadataResponse>
    fun removeUUIDMetadata(callback: Callback<RemoveUUIDMetadataResponse>)
    fun removeUUIDMetadata(params: RemoveUUIDMetadataParameters = definedExternally): Promise<RemoveUUIDMetadataResponse>
    fun removeUUIDMetadata(): Promise<RemoveUUIDMetadataResponse>
    fun removeUUIDMetadata(params: RemoveUUIDMetadataParameters, callback: Callback<RemoveUUIDMetadataResponse>)
    fun  getAllUUIDMetadata(callback: Callback<GetAllUUIDMetadataResponse>)
    fun  getAllUUIDMetadata(params: GetAllMetadataParameters = definedExternally): Promise<GetAllUUIDMetadataResponse>
    fun  getAllUUIDMetadata(): Promise<GetAllUUIDMetadataResponse>
    fun  getAllUUIDMetadata(params: GetAllMetadataParameters, callback: Callback<GetAllUUIDMetadataResponse>)
    fun  getUUIDMetadata(callback: Callback<GetUUIDMetadataResponse>)
    fun  getUUIDMetadata(params: GetUUIDMetadataParameters = definedExternally): Promise<GetUUIDMetadataResponse>
    fun  getUUIDMetadata(): Promise<GetUUIDMetadataResponse>
    fun  getUUIDMetadata(params: GetUUIDMetadataParameters, callback: Callback<GetUUIDMetadataResponse>)
    fun  setChannelMetadata(params: SetChannelMetadataParameters, callback: Callback<SetChannelMetadataResponse>)
    fun  setChannelMetadata(params: SetChannelMetadataParameters): Promise<SetChannelMetadataResponse>
    fun removeChannelMetadata(params: RemoveChannelMetadataParameters, callback: Callback<RemoveChannelMetadataResponse>)
    fun removeChannelMetadata(params: RemoveChannelMetadataParameters): Promise<RemoveChannelMetadataResponse>
    fun  getAllChannelMetadata(callback: Callback<GetAllChannelMetadataResponse>)
    fun  getAllChannelMetadata(params: GetAllMetadataParameters = definedExternally): Promise<GetAllChannelMetadataResponse>
    fun  getAllChannelMetadata(): Promise<GetAllChannelMetadataResponse>
    fun  getAllChannelMetadata(params: GetAllMetadataParameters, callback: Callback<GetAllChannelMetadataResponse>)
    fun  getChannelMetadata(params: GetChannelMetadataParameters, callback: Callback<GetChannelMetadataResponse>)
    fun  getChannelMetadata(params: GetChannelMetadataParameters): Promise<GetChannelMetadataResponse>
    fun  getMemberships(callback: Callback<ManageMembershipsResponse>)
    fun  getMemberships(params: GetMembershipsParametersv2 = definedExternally): Promise<ManageMembershipsResponse>
    fun  getMemberships(): Promise<ManageMembershipsResponse>
    fun  getMemberships(params: GetMembershipsParametersv2, callback: Callback<ManageMembershipsResponse>)
    fun  setMemberships(params: SetMembershipsParameters, callback: Callback<ManageMembershipsResponse>)
    fun  setMemberships(params: SetMembershipsParameters): Promise<ManageMembershipsResponse>
    fun  removeMemberships(params: RemoveMembershipsParameters, callback: Callback<ManageMembershipsResponse>)
    fun  removeMemberships(params: RemoveMembershipsParameters): Promise<ManageMembershipsResponse>
    fun  getChannelMembers(params: GetChannelMembersParameters, callback: Callback<ManageChannelMembersResponse>)
    fun  getChannelMembers(params: GetChannelMembersParameters): Promise<ManageChannelMembersResponse>
    fun  setChannelMembers(params: SetChannelMembersParameters, callback: Callback<ManageChannelMembersResponse>)
    fun  setChannelMembers(params: SetChannelMembersParameters): Promise<ManageChannelMembersResponse>
    fun  removeChannelMembers(params: RemoveChannelMembersParameters, callback: Callback<ManageChannelMembersResponse>)
    fun  removeChannelMembers(params: RemoveChannelMembersParameters): Promise<ManageChannelMembersResponse>
}

external interface `T$1` {
    var data: MessageAction
}

external interface `T$2` {
    var data: Any
}

//@JsModule("pubnub-js")
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
    open fun removeListener(params: ListenerParameters)
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
    open fun setToken(params: String)
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
    open fun addMessageAction(params: AddMessageActionParameters, callback: Callback<`T$1`>)
    open fun addMessageAction(params: AddMessageActionParameters): Promise<`T$1`>
    open fun removeMessageAction(params: RemoveMessageActionParameters, callback: Callback<`T$2`>)
    open fun removeMessageAction(params: RemoveMessageActionParameters): Promise<`T$2`>
    open fun getMessageActions(params: GetMessageActionsParameters, callback: Callback<GetMessageActionsResponse>)
    open fun getMessageActions(params: GetMessageActionsParameters): Promise<GetMessageActionsResponse>
    open fun encrypt(data: String, customCipherKey: String = definedExternally, options: CryptoParameters = definedExternally): String
    open fun decrypt(data: String?, customCipherKey: String = definedExternally, options: CryptoParameters = definedExternally): Any
    open fun decrypt(data: String?): Any
    open fun decrypt(data: String?, customCipherKey: String = definedExternally): Any
    open fun decrypt(data: Any?, customCipherKey: String = definedExternally, options: CryptoParameters = definedExternally): Any
    open fun decrypt(data: Any?): Any
    open fun decrypt(data: Any?, customCipherKey: String = definedExternally): Any
    open fun time(): Promise<FetchTimeResponse>
    open fun time(callback: Callback<FetchTimeResponse>)
    interface KeepAliveSettings {
        var keepAliveMsecs: Number?
            get() = definedExternally
            set(value) = definedExternally
        var freeSocketKeepAliveTimeout: Number?
            get() = definedExternally
            set(value) = definedExternally
        var timeout: Number?
            get() = definedExternally
            set(value) = definedExternally
        var maxSockets: Number?
            get() = definedExternally
            set(value) = definedExternally
        var maxFreeSockets: Number?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface PNConfiguration: Partial {
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
            get() = definedExternally
            set(value) = definedExternally
        var actualChannel: String
        var subscribedChannel: String
        var userMetadata: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface StatusEvent {
        var category: String
        var operation: String
        var affectedChannels: Array<String>
        var subscribedChannels: Array<String>
        var affectedChannelGroups: Array<String>
        var lastTimetoken: dynamic /* Number | String */
            get() = definedExternally
            set(value) = definedExternally
        var currentTimetoken: dynamic /* Number | String */
            get() = definedExternally
            set(value) = definedExternally
        var statusCode: Number?
    }
    interface PresenceEvent {
        var action: String /* "join" | "leave" | "state-change" | "timeout" */
        var channel: String
        var occupancy: Number
        var state: Any?
            get() = definedExternally
            set(value) = definedExternally
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
            get() = definedExternally
            set(value) = definedExternally
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
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$6` {
        var event: String /* "set" | "delete" */
        var type: String /* "uuid" | "channel" | "membership" */
        var data: Any?
    }
    interface BaseObjectsEvent {
        var channel: String
        var message: `T$6`
        var subscription: String?
        var publisher: String?
            get() = definedExternally
            set(value) = definedExternally
        var timetoken: Number
    }
    interface `T$7` {
        var event: String /* "set" */
        var type: String /* "uuid" */
        var data: UUIDMetadataObject
    }
    interface SetUUIDMetadataEvent : BaseObjectsEvent
    interface `T$8` {
        var id: String
    }
    interface `T$9` {
        var event: String /* "delete" */
        var type: String /* "uuid" */
        var data: `T$8`
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
        var data: `T$8`
    }
    interface RemoveChannelMetadataEvent : BaseObjectsEvent
    interface `T$12` {
        var channel: `T$8`
        var uuid: `T$8`
        var custom: CustomObject?
        var updated: String
        var eTag: String
    }
    interface `T$13` {
        var event: String /* "set" */
        var type: String /* "membership" */
        var data: `T$12`
    }
    interface SetMembershipEvent : BaseObjectsEvent
    interface `T$14` {
        var channel: `T$8`
        var uuid: `T$8`
    }
    interface `T$15` {
        var event: String /* "delete" */
        var type: String /* "membership" */
        var data: `T$14`
    }
    interface RemoveMembershipEvent : BaseObjectsEvent
    interface PublishParameters {
        var message: Any
        var channel: String
        var storeInHistory: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var sendByPost: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var meta: Any?
            get() = definedExternally
            set(value) = definedExternally
        var ttl: Number?
            get() = definedExternally
            set(value) = definedExternally
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
            get() = definedExternally
            set(value) = definedExternally
        var includeTimetoken: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var reverse: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var start: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var end: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var includeMeta: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface HistoryMessage {
        var entry: Any
        var timetoken: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var meta: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface HistoryResponse {
        var endTimeToken: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var startTimeToken: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var messages: Array<HistoryMessage>
    }
    interface FetchMessagesParameters {
        var channels: Array<String>
        var count: Number?
            get() = definedExternally
            set(value) = definedExternally
        var stringifiedTimeToken: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var start: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var end: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var withMessageActions: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var includeMessageType: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var includeUUID: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var includeMeta: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var includeMessageActions: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$16` {
        var uuid: String
        var actionTimetoken: dynamic /* String | Number */
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$17` {
        @nativeGetter
        operator fun get(value: String): Array<`T$16`>?
        @nativeSetter
        operator fun set(value: String, value2: Array<`T$16`>)
    }
    interface `T$18` {
        @nativeGetter
        operator fun get(type: String): `T$17`?
        @nativeSetter
        operator fun set(type: String, value: `T$17`)
    }
    interface `T$19` {
        var channel: String
        var message: Any
        var timetoken: dynamic /* String | Number */
            get() = definedExternally
            set(value) = definedExternally
        var messageType: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var error: String?
            get() = definedExternally
            set(value) = definedExternally
        var meta: Json?
            get() = definedExternally
            set(value) = definedExternally
        var actions: `T$18`
    }
    interface `T$20` {
        @nativeGetter
        operator fun get(channel: String): Array<`T$19`>?
        @nativeSetter
        operator fun set(channel: String, value: Array<`T$19`>)
    }
    interface `T$21` {
        var url: String
        var start: String
        var max: Number
    }
    interface FetchMessagesResponse {
        var channels: `T$20`
        var more: `T$21`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface DeleteMessagesParameters {
        var channel: String
        var start: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
        var end: dynamic /* String? | Number? */
            get() = definedExternally
            set(value) = definedExternally
    }
    interface MessageCountsParameters {
        var channels: Array<String>
        var channelTimetokens: dynamic /* Array<String> | Array<Number> */
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$22` {
        @nativeGetter
        operator fun get(channel: String): Number?
        @nativeSetter
        operator fun set(channel: String, value: Number)
    }
    interface MessageCountsResponse {
        var channels: `T$22`
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
            get() = definedExternally
            set(value) = definedExternally
        var topic: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface PushDeviceParameters {
        var device: String
        var pushGateway: String
        var environment: String?
            get() = definedExternally
            set(value) = definedExternally
        var topic: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface PushListChannelsResponse {
        var channels: Array<String>
    }
    interface PubnubStatus {
        var error: Boolean
        var category: String?
            get() = definedExternally
            set(value) = definedExternally
        var operation: String
        var statusCode: Number
        var errorData: Error?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface FireParameters {
        var message: Any
        var channel: String
        var sendByPost: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var meta: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SubscribeParameters {
        var channels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var channelGroups: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var withPresence: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var timetoken: Number?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface UnsubscribeParameters {
        var channels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var channelGroups: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
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
    interface `L$0` {
        @nativeInvoke
        operator fun invoke(objectsEvent: SetUUIDMetadataEvent)
        @nativeInvoke
        operator fun invoke(objectsEvent: RemoveUUIDMetadataEvent)
        @nativeInvoke
        operator fun invoke(objectsEvent: SetChannelMetadataEvent)
        @nativeInvoke
        operator fun invoke(objectsEvent: RemoveChannelMetadataEvent)
        @nativeInvoke
        operator fun invoke(objectsEvent: SetMembershipEvent)
        @nativeInvoke
        operator fun invoke(objectsEvent: RemoveMembershipEvent)
    }
    interface ListenerParameters {
        val status: ((statusEvent: StatusEvent) -> Unit)?
        val message: ((messageEvent: MessageEvent) -> Unit)?
        val presence: ((presenceEvent: PresenceEvent) -> Unit)?
        val signal: ((signalEvent: SignalEvent) -> Unit)?
        val messageAction: ((messageActionEvent: MessageActionEvent) -> Unit)?
        val file: ((fileEvent: FileEvent) -> Unit)?
        val objects: `L$0`?
            get() = definedExternally
    }
    interface HereNowParameters {
        var channels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var channelGroups: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var includeUUIDs: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var includeState: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$23` {
        var uuid: String
        var state: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$24` {
        var name: String
        var occupancy: Number
        var occupants: Array<`T$23`>
    }
    interface `T$25` {
        @nativeGetter
        operator fun get(channel: String): `T$24`?
        @nativeSetter
        operator fun set(channel: String, value: `T$24`)
    }
    interface HereNowResponse {
        var totalChannels: Number
        var totalOccupancy: Number
        var channels: `T$25`
    }
    interface WhereNowParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface WhereNowResponse {
        var channels: Array<String>
    }
    interface SetStateParameters {
        var channels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var channelGroups: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var state: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SetStateResponse {
        var state: Any
    }
    interface GetStateParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var channels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var channelGroups: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface GetStateResponse {
        var channels: Json
    }
    interface GrantParameters {
        var channels: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var channelGroups: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var uuids: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var authKeys: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
        var ttl: Number?
            get() = definedExternally
            set(value) = definedExternally
        var read: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var write: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var manage: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var delete: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var get: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var join: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var update: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$26` {
        @nativeGetter
        operator fun get(key: String): GrantTokenPermissions?
        @nativeSetter
        operator fun set(key: String, value: GrantTokenPermissions)
    }
    interface `T$27` {
        var channels: `T$26`?
            get() = definedExternally
            set(value) = definedExternally
        var groups: `T$26`?
            get() = definedExternally
            set(value) = definedExternally
        var uuids: `T$26`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface GrantTokenParameters {
        var ttl: Number
        var authorized_uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var resources: `T$27`?
            get() = definedExternally
            set(value) = definedExternally
        var patterns: `T$27`?
            get() = definedExternally
            set(value) = definedExternally
        var meta: Json?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface ParsedGrantToken : GrantTokenParameters {
        var version: Number
        var timestamp: Number
        var signature: Any
    }
    interface GrantTokenPermissions {
        var read: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var write: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var manage: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var delete: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var get: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var join: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var update: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface RevokeTokenResponse {
        var status: Number
        var data: Any?
    }
    interface `T$28` {
        var type: String
        var value: String
    }
    interface AddMessageActionParameters {
        var channel: String
        var messageTimetoken: String
        var action: `T$28`
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
            get() = definedExternally
            set(value) = definedExternally
        var end: String?
            get() = definedExternally
            set(value) = definedExternally
        var limit: Number?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface GetMessageActionsResponse {
        var data: Array<MessageAction>
        var start: String?
            get() = definedExternally
            set(value) = definedExternally
        var end: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface ListFilesParameters {
        var channel: String
        var limit: Number?
            get() = definedExternally
            set(value) = definedExternally
        var next: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SendFileParameters {
        var channel: String
        var file: dynamic /* StreamFileInput | BufferFileInput | UriFileInput */
            get() = definedExternally
            set(value) = definedExternally
        var message: Any?
            get() = definedExternally
            set(value) = definedExternally
        var cipherKey: String?
            get() = definedExternally
            set(value) = definedExternally
        var storeInHistory: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var ttl: Number?
            get() = definedExternally
            set(value) = definedExternally
        var meta: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface StreamFileInput {
        var stream: Any
        var name: String
        var mimeType: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface BufferFileInput {
        var data: Any
        var name: String
        var mimeType: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface UriFileInput {
        var uri: String
        var name: String
        var mimeType: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface DownloadFileParameters {
        var channel: String
        var id: String
        var name: String
        var cipherKey: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface FileInputParameters {
        var channel: String
        var id: String
        var name: String
    }
    interface PublishFileParameters {
        var channel: String
        var message: Any?
            get() = definedExternally
            set(value) = definedExternally
        var fileId: String
        var fileName: String
        var storeInHistory: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var ttl: Number?
            get() = definedExternally
            set(value) = definedExternally
        var meta: Any?
            get() = definedExternally
            set(value) = definedExternally
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

    interface CustomObject

    interface v2ObjectDataOmitId {
        var eTag: String
        var updated: String
        var custom: CustomObject?
            get() = definedExternally
            set(value) = definedExternally
    }

    interface v2ObjectData {
        var id: String
        var eTag: String
        var updated: String
        var custom: CustomObject?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface ObjectParam {
        var custom: CustomObject?
            get() = definedExternally
            set(value) = definedExternally
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
    interface `T$30` {
        var customFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SetUUIDMetadataParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var data: UUIDMetadata
        var include: `T$30`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface RemoveUUIDMetadataParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$31` {
        var totalCount: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var customFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$32` {
        var next: String?
            get() = definedExternally
            set(value) = definedExternally
        var prev: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface GetAllMetadataParameters {
        var include: `T$31`?
            get() = definedExternally
            set(value) = definedExternally
        var filter: String?
            get() = definedExternally
            set(value) = definedExternally
        var sort: Any?
            get() = definedExternally
            set(value) = definedExternally
        var limit: Number?
            get() = definedExternally
            set(value) = definedExternally
        var page: `T$32`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface GetUUIDMetadataParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var include: `T$30`?
            get() = definedExternally
            set(value) = definedExternally
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
    interface ChannelMetadata : ObjectParam, ChannelMetadataFieldsPartial
    interface ChannelMetadataObject : v2ObjectData, ChannelMetadataFieldsNullable
    interface SetChannelMetadataParameters {
        var channel: String
        var data: ChannelMetadata
        var include: `T$30`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface RemoveChannelMetadataParameters {
        var channel: String
    }
    interface `T$33` {
        var customFields: Boolean
    }
    interface GetChannelMetadataParameters {
        var channel: String
        var include: `T$33`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$34` {
        var status: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface UUIDMembershipObject : v2ObjectDataOmitId {
        var uuid: dynamic /* UUIDMetadataObject & `T$34` | `T$8` */
            get() = definedExternally
            set(value) = definedExternally
        var status: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface ChannelMembershipObject : v2ObjectDataOmitId {
        var channel: dynamic /* ChannelMetadataObject & `T$34` | `T$8` */
            get() = definedExternally
            set(value) = definedExternally
        var status: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$35` {
        var totalCount: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var customFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var UUIDFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var customUUIDFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var statusField: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var UUIDStatusField: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var UUIDTypeField: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface UUIDMembersParameters {
        var include: `T$35`?
            get() = definedExternally
            set(value) = definedExternally
        var filter: String?
            get() = definedExternally
            set(value) = definedExternally
        var sort: Any?
            get() = definedExternally
            set(value) = definedExternally
        var limit: Number?
            get() = definedExternally
            set(value) = definedExternally
        var page: `T$32`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$36` {
        var totalCount: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var customFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var channelFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var customChannelFields: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var statusField: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var channelStatusField: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var channelTypeField: Boolean?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface ChannelMembersParameters {
        var include: `T$36`?
            get() = definedExternally
            set(value) = definedExternally
        var filter: String?
            get() = definedExternally
            set(value) = definedExternally
        var sort: Any?
            get() = definedExternally
            set(value) = definedExternally
        var limit: Number?
            get() = definedExternally
            set(value) = definedExternally
        var page: `T$32`?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface GetChannelMembersParameters : UUIDMembersParameters {
        var channel: String
    }
    interface GetMembershipsParametersv2 : ChannelMembersParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SetCustom {
        var id: String
        var custom: CustomObject?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SetMembershipsParameters : ChannelMembersParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var channels: Array<dynamic /* String | SetCustom */>?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface RemoveMembershipsParameters : ChannelMembersParameters {
        var uuid: String?
            get() = definedExternally
            set(value) = definedExternally
        var channels: Array<String>
    }
    interface SetChannelMembersParameters : UUIDMembersParameters {
        var channel: String
        var uuids: Array<dynamic /* String | SetCustom */>
    }
    interface RemoveChannelMembersParameters : UUIDMembersParameters {
        var channel: String
        var uuids: Array<String>
    }
    interface CryptoParameters {
        var encryptKey: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var keyEncoding: String?
            get() = definedExternally
            set(value) = definedExternally
        var keyLength: Number?
            get() = definedExternally
            set(value) = definedExternally
        var mode: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface FetchTimeResponse {
        var timetoken: Number
    }
    interface APNS2Configuration {
        var collapseId: String?
            get() = definedExternally
            set(value) = definedExternally
        var expirationDate: Date?
            get() = definedExternally
            set(value) = definedExternally
        var targets: Array<APNS2Target>
    }
    interface APNS2Target {
        var topic: String
        var environment: String? /* "development" | "production" */
            get() = definedExternally
            set(value) = definedExternally
        var excludedDevices: Array<String>?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface BaseNotificationPayload {
        var subtitle: String?
            get() = definedExternally
            set(value) = definedExternally
        var payload: Any?
        var badge: Number?
            get() = definedExternally
            set(value) = definedExternally
        var sound: String?
            get() = definedExternally
            set(value) = definedExternally
        var title: String?
            get() = definedExternally
            set(value) = definedExternally
        var body: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface APNSNotificationPayload : BaseNotificationPayload {
        var configurations: Array<APNS2Configuration>
        var apnsPushType: String?
            get() = definedExternally
            set(value) = definedExternally
        var isSilent: Boolean
    }
    interface MPNSNotificationPayload : BaseNotificationPayload {
        var backContent: String?
            get() = definedExternally
            set(value) = definedExternally
        var backTitle: String?
            get() = definedExternally
            set(value) = definedExternally
        var count: Number?
            get() = definedExternally
            set(value) = definedExternally
        var type: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface FCMNotificationPayload : BaseNotificationPayload {
        var isSilent: Boolean
        var icon: String?
            get() = definedExternally
            set(value) = definedExternally
        var tag: String?
            get() = definedExternally
            set(value) = definedExternally
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
            get() = definedExternally
            set(value) = definedExternally
        var badge: Number?
            get() = definedExternally
            set(value) = definedExternally
        var sound: String?
            get() = definedExternally
            set(value) = definedExternally
        var title: String?
            get() = definedExternally
            set(value) = definedExternally
        var body: String?
            get() = definedExternally
            set(value) = definedExternally
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
            get() = definedExternally
            set(value) = definedExternally
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
            get() = definedExternally
            set(value) = definedExternally
    }
    interface CryptoModuleConfiguration {
        var default: dynamic /* Cryptor | LegacyCryptor<PubNubFileType> */
            get() = definedExternally
            set(value) = definedExternally
        var cryptors: Array<dynamic /* Cryptor | LegacyCryptor<PubNubFileType> */>?
            get() = definedExternally
            set(value) = definedExternally
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

typealias PubnubData = PubNub.MessageEvent

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


external interface JsMap<V>

fun <V> entriesOf(jsObject: JsMap<V>): List<Pair<String, V>> =
    (js("Object.entries") as (dynamic) -> Array<Array<V>>)
        .invoke(jsObject)
        .map { entry -> entry[0] as String to entry[1] }

fun <V> JsMap<V>.toMap(): Map<String, V> =
    entriesOf(this).toMap()

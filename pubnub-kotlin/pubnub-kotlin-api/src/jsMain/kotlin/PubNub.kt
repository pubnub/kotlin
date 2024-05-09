@file:OptIn(ExperimentalJsExport::class)

import com.pubnub.kmp.Optional

fun <T> T?.toOptional(): Optional<T> = this?.let { Optional.Value(it) } ?: Optional.Absent()
//
//@JsExport
//class ChatJs(private val chatConfig: ChatConfig) {
//    private val chat: Chat = ChatImpl(chatConfig)
//
//    fun createUser(id: String,
//                   name: String?,
//                   externalId: String? = null,
//                   profileUrl: String? = null,
//                   email: String? = null,
//                   custom: Map<String,Any?>? = null,
//                   status: String? = null,
//                   type: String? = null,): Promise<User> {
//        return Promise { resolve: (User) -> Unit, reject: (Throwable) -> Unit ->
//            chat.createUser(id, name, externalId, profileUrl,email,custom, status, type) { result: Result<User> ->
//                result.onSuccess { resolve(it) }.onFailure { reject(it) }
//            }
//        }
//    }
//}




/// var a = new PubNub({ ... })
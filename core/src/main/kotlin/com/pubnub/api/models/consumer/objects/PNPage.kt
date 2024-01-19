package com.pubnub.api.models.consumer.objects

//todo this was in another package in java sdk, need to split impl/api
sealed class PNPage {
    abstract val pageHash: String

    @Deprecated(message = "Use `pageHash` instead.", replaceWith = ReplaceWith("pageHash"))
    val hash: String get() = pageHash

    data class PNNext(override val pageHash: String) : PNPage()
    data class PNPrev(override val pageHash: String) : PNPage()
}

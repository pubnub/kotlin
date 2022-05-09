package com.pubnub.entities.objects

sealed class PNPage : com.pubnub.api.models.consumer.PNPage {
    internal abstract val pageHash: String
    data class PNNext(override val pageHash: String) : PNPage()
    data class PNPrev(override val pageHash: String) : PNPage()
}

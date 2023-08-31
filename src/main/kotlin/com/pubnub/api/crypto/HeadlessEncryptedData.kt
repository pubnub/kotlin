package com.pubnub.api.crypto

internal class HeadlessEncryptedData(
    override val content: ByteArray
) : OutgoingEncryptedData, IncomingEncryptedData {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeadlessEncryptedData

        return this.content.contentEquals(other.content)
    }

    override fun hashCode(): Int {
        return this.content.contentHashCode()
    }
}

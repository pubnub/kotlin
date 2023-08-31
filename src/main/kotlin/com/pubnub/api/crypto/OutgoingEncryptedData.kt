package com.pubnub.api.crypto

sealed interface OutgoingEncryptedData {
    val content: ByteArray
}

class OutgoingEncryptedDataV1 private constructor(
    override val content: ByteArray
) : OutgoingEncryptedData {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OutgoingEncryptedDataV1

        return content.contentEquals(other.content)
    }

    override fun hashCode(): Int {
        return content.contentHashCode()
    }

    companion object {
        private val VERSION_1 = byteArrayOf(1.toByte())

        fun create(
            id: ByteArray,
            headerLength: ByteArray,
            metadata: ByteArray,
            data: ByteArray,
        ): ByteArray {
            val content = ByteArray(SENTINEL.size + 1 + 4 + headerLength.size + metadata.size + data.size)

            var offset = 0

            for (el in listOf(SENTINEL, VERSION_1, id, headerLength, metadata, data)) {
                el.copyInto(content, offset)
                offset += el.size
            }
            return content
        }
    }
}

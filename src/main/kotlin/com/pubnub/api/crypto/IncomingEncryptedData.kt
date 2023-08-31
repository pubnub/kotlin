package com.pubnub.api.crypto

import okhttp3.internal.and

sealed interface IncomingEncryptedData {
    val content: ByteArray
}

class IncomingEncryptedDataV1 private constructor(
    val id: String,
    val metadata: ByteArray,
    override val content: ByteArray,
) : IncomingEncryptedData {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncomingEncryptedDataV1

        if (!id.contentEquals(other.id)) return false
        if (!metadata.contentEquals(other.metadata)) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + metadata.contentHashCode()
        result = 31 * result + content.contentHashCode()
        return result
    }

    companion object {
        fun fromRawEncryptedBytes(rawEncryptedBytes: ByteArray, initialOffset: Int = 5): IncomingEncryptedDataV1 {
            var offset = initialOffset
            val id = rawEncryptedBytes.sliceArray(IntRange(offset, offset + 3)).toString()

            val length: Int = if ((rawEncryptedBytes[offset] and 0x80) == 0x80) {
                rawEncryptedBytes[offset].toInt().also {
                    offset += 1
                }
            } else {
                val b1 = rawEncryptedBytes[offset + 1]
                val b2 = rawEncryptedBytes[offset + 2]

                val i = b1.toInt() and 0xFF shl 8 or (b2.toInt() and 0xFF)
                offset += 3
                i
            }
            val metadata = rawEncryptedBytes.sliceArray(IntRange(offset, offset + length - 1))
            offset += length
            val data = rawEncryptedBytes.sliceArray(IntRange(offset, rawEncryptedBytes.size - 1))

            return IncomingEncryptedDataV1(
                id = id,
                metadata = metadata,
                content = data
            )
        }
    }
}

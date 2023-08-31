package com.pubnub.api.crypto

import com.pubnub.api.crypto.legacy.DynamicIvAESCBCLegacyCryptor
import com.pubnub.api.crypto.legacy.StaticIvAESCBCLegacyCryptor
import com.pubnub.api.vendor.Base64
import java.io.InputStream
import java.nio.charset.Charset

internal val SENTINEL = "PNED".toByteArray()
internal val VERSION_1: ByteArray = byteArrayOf(1.toByte())

class CryptoModule private constructor(
    private val encryptor: Encryptor,
    private val decryptors: Map<String, Cryptor>,
    private val legacyDecryptor: Decryptor,
    private val streamLegacyDecryptor: Decryptor,
    private val key: String
) {
    fun encrypt(input: ByteArray, cipherKey: String? = null): ByteArray = encryptor.encrypt(input, cipherKey ?: key).content

    fun decrypt(input: ByteArray, cipherKey: String? = null): ByteArray = when (val structuredEncryptedData = input.toStructuredIncomingEncryptedData()) {
        is HeadlessEncryptedData -> legacyDecryptor.decrypt(structuredEncryptedData, cipherKey ?: key)
        is IncomingEncryptedDataV1 -> decryptors[structuredEncryptedData.id]
            ?.decrypt(structuredEncryptedData, cipherKey ?: key)
            ?: throw IllegalStateException("No decryptor found for id: ${structuredEncryptedData.id}")
    }

    companion object {
        @JvmStatic
        fun create(
            cipherKey: String,
            encryptor: Encryptor,
            decryptors: Collection<Cryptor>,
            useRandomInitializationVector: Boolean = true,
            customInitializationVector: String? = null
        ): CryptoModule {
            return CryptoModule(
                encryptor = encryptor,
                decryptors = decryptors.associateBy { it.id.toString(Charset.defaultCharset()) },
                legacyDecryptor = if (useRandomInitializationVector) {
                    DynamicIvAESCBCLegacyCryptor.create()
                } else {
                    StaticIvAESCBCLegacyCryptor.create(customInitializationVector)
                },
                streamLegacyDecryptor = DynamicIvAESCBCLegacyCryptor.create(),
                key = cipherKey
            )
        }
    }
}

fun CryptoModule.encryptAndEncodeBase64(input: String, cipherKey: String? = null): String {
    return Base64.encodeToString(encrypt(input.toByteArray(), cipherKey), Base64.NO_WRAP)
}

fun CryptoModule.decodeBase64AndDecrypt(input: String, cipherKey: String? = null): String {
    return decrypt(Base64.decode(input, Base64.NO_WRAP), cipherKey).toString(Charset.defaultCharset())
}

fun CryptoModule.encrypt(inputStream: InputStream, cipherKey: String?): InputStream {
    return encrypt(inputStream.readBytes(), cipherKey).inputStream()
}

fun CryptoModule.decrypt(inputStream: InputStream, cipherKey: String?): InputStream {
    return decrypt(inputStream.readBytes(), cipherKey).inputStream()
}

private fun compareByteArrays(
    a: ByteArray,
    aOffset: Int,
    b: ByteArray,
    aLength: Int = b.size,
    bOffset: Int = 0,
    bLength: Int = b.size
): Boolean {
    if (aLength != bLength) {
        return false
    }
    for (i in (0 until aLength - 1)) {
        if (a[aOffset + i] != b[bOffset + i]) {
            return false
        }
    }
    return true
}

private fun ByteArray.toStructuredIncomingEncryptedData(): IncomingEncryptedData {
    var offset = 0
    if (!compareByteArrays(this, offset, SENTINEL)) {
        return HeadlessEncryptedData(this)
    }
    offset += SENTINEL.size

    if (!compareByteArrays(this, offset, VERSION_1)) {
        throw RuntimeException("Unsupported crypto header version")
    }
    offset += 1

    return IncomingEncryptedDataV1.fromRawEncryptedBytes(this, offset)
}

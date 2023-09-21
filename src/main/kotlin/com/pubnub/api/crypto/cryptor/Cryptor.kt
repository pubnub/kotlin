package com.pubnub.api.crypto.cryptor

import com.pubnub.api.crypto.data.EncryptedData
import com.pubnub.api.crypto.data.EncryptedStreamData
import java.io.InputStream

interface Cryptor {
    fun id(): ByteArray // Assuming 4 bytes,
    fun encrypt(data: ByteArray): EncryptedData
    fun decrypt(encryptedData: EncryptedData): ByteArray
    fun encryptStream(stream: InputStream): Result<EncryptedStreamData> // todo tu zawsze jest randomIv
    fun decryptStream(encryptedData: EncryptedStreamData): Result<InputStream> // todo tu zawsze jest randomIv
}

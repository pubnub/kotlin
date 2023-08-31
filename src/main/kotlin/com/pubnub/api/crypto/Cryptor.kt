package com.pubnub.api.crypto

interface Cryptor : Encryptor, Decryptor {
    val id: ByteArray
}

interface Decryptor {
    fun decrypt(input: IncomingEncryptedData, cipherKey: String): ByteArray
}

interface Encryptor {
    fun encrypt(input: ByteArray, cipherKey: String): OutgoingEncryptedData
}

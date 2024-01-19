package com.pubnub.internal.crypto.cryptor

internal enum class CryptorHeaderVersion(val value: Int) {
    One(1);

    companion object {
        fun fromValue(value: Int): CryptorHeaderVersion? {
            return values().find { it.value == value }
        }
    }
}

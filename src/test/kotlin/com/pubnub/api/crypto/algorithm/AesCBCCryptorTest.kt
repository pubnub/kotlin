package com.pubnub.api.crypto.algorithm

import com.pubnub.api.crypto.cryptor.AesCbcCryptor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AesCBCCryptorTest {
    private lateinit var objectUnderTest: AesCbcCryptor

    @BeforeEach
    fun setUp() {
        objectUnderTest = AesCbcCryptor("enigma")
    }

    @Test
    fun `can decrypt and encrypt`() {
    }
}

package com.pubnub.api;

class PubnubCrypto extends PubnubCryptoCore {

    public PubnubCrypto(String CIPHER_KEY) {
        super(CIPHER_KEY);
    }

    public PubnubCrypto(String CIPHER_KEY, String INITIALIZATION_VECTOR) {
        super(CIPHER_KEY, INITIALIZATION_VECTOR);
    }

}

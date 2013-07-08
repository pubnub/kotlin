package com.pubnub.api;

class PubnubCrypto extends PubnubCryptoCore {

    public PubnubCrypto(String CIPHER_KEY) {
        super(CIPHER_KEY);
    }
    public PubnubCrypto(String CIPHER_KEY, String IV) {
        super(CIPHER_KEY, IV);
    }
}

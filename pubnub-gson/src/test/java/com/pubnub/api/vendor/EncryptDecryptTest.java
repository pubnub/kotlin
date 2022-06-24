package com.pubnub.api.vendor;

import com.pubnub.api.PubNubException;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class EncryptDecryptTest {
    @Test
    public void canDecryptTextWhatIsEncryptedWithStaticIV() throws IOException, PubNubException {
        //given
        final String cipherKey = "enigma";
        final String msgToEncrypt = "Hello world";


        //when
        Crypto crypto = new Crypto(cipherKey);
        final String encryptedMsg = crypto.encrypt(msgToEncrypt);
        String decryptedMsg = crypto.decrypt(encryptedMsg);

        //then
        Assert.assertEquals(msgToEncrypt, decryptedMsg);
    }

    @Test
    public void canDecryptTextWhatIsEncryptedWithRandomIV() throws IOException, PubNubException {
        //given
        final String cipherKey = "enigma";
        final String msgToEncrypt = "Hello world";


        //when
        Crypto crypto = new Crypto(cipherKey, true);
        final String encryptedMsg = crypto.encrypt(msgToEncrypt);
        String decryptedMsg = crypto.decrypt(encryptedMsg);

        //then
        Assert.assertEquals(msgToEncrypt, decryptedMsg);
    }

    @Test
    public void encryptingWithRandomIVTwoTimesTheSameMessageProducesDifferentOutput() throws PubNubException {
        //given
        final String cipherKey = "enigma";
        final String msgToEncrypt = "Hello world";

        //when
        Crypto crypto = new Crypto(cipherKey, true);
        final String encrypted1 = crypto.encrypt(msgToEncrypt);
        String encrypted2 = crypto.encrypt(msgToEncrypt);

        //then
        Assert.assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    public void encryptingWithRandomIVTwoTimesDecryptedMsgIsTheSame() throws PubNubException {
        //given
        final String cipherKey = "enigma";
        final String msgToEncrypt = "Hello world";

        //when
        Crypto crypto = new Crypto(cipherKey, true);
        final String encrypted1 = crypto.encrypt(msgToEncrypt);
        String encrypted2 = crypto.encrypt(msgToEncrypt);

        //then
        Assert.assertEquals(msgToEncrypt, crypto.decrypt(encrypted1));
        Assert.assertEquals(msgToEncrypt, crypto.decrypt(encrypted2));
    }


}

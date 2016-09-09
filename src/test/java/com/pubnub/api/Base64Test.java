package com.pubnub.api;

import com.pubnub.api.vendor.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class Base64Test {


    @Test
    public void testBase64Encode(){
       Assert.assertEquals("YWJj", Base64.encodeToString("abc".getBytes(Charset.forName("UTF-8")), 0).trim());
    }

}

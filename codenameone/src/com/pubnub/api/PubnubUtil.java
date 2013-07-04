package com.pubnub.api;

import com.codename1.io.Util;
/**
 * PubnubUtil class provides utility methods like urlEncode etc
 * @author Pubnub
 *
 */
public class PubnubUtil extends PubnubUtilCore {

    /**
     * Returns encoded String
     *
     * @param sUrl
     *            , input string
     * @return , encoded string
     */
    public static String urlEncode(String sUrl) {
        return Util.encodeUrl(sUrl);
    }
}

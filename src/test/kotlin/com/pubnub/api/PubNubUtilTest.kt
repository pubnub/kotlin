package com.pubnub.api

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Ignore
import org.junit.Test

class PubNubUtilTest {
    @Test
    fun signedSHA256ReturnPrecomputedSingleLine() {
        assertThat(PubNubUtil.signSHA256("key", "data"), Matchers.`is`("UDH-PZicbRU3oBP6bnOdojRj_a7DtwE32Cjjas4iG9A="))
    }

    @Test
    @Ignore
    fun aaaa() {
        PubNub(PNConfiguration("aaaa")).parseToken("qEF2AkF0GmKeYqVDdHRsGajAQ3Jlc6VEY2hhbqBDZ3JwoEN1c3KgQ3NwY6BEdXVpZKBDcGF0pURjaGFuongrXmNpXzQwXyg/OLSWLTLDKYLFKD86WZATOV0RKV8OPZPBMC05XSSPLS4QJANPXMNFNDATLIOKA0NNCNCGQ3VZCQBDC3BJOER1DWLKOERTZXRHOER1DWLKEC11C2VYLTEWOC02N2NLYZAZYS1JNTI1LTQ1YJMTYWIZMS0WZTJHY2RMOTCWYMNDC2LNWCAE4DN5YC0JM")
    }
}

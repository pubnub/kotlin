package com.pubnub.api.tests 

import com.jayway.awaitility.scala.AwaitilitySupport
import org.junit._
import Assert._


import java.util.concurrent.TimeUnit.MILLISECONDS

import com.jayway.awaitility.Awaitility._
import com.jayway.awaitility.core.ConditionTimeoutException


import com.pubnub.api._


import java.util.concurrent.TimeUnit


import  com.jayway.awaitility.Awaitility.await

class PublishResult {
    var result = 0;
}

@Test
class PublishTest extends AwaitilitySupport {

    /*
    @Test
    def subscribe() = {
        var pubnub = new Pubnub("demo", "demo")

        var pr = new PublishResult()

        var callback = new Callback() {

            override def successCallback(channel: String, message: Object) {
                pr.result = 1
            }

            override def errorCallback(channel: String, error: PubnubError) {

            }
        }

        pubnub.publish("abcd", "hi", callback)
        await atMost(3000, MILLISECONDS) until { pr.result == 1 }

    }
    */

}



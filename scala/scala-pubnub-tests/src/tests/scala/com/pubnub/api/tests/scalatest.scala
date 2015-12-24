
package com.pubnub.api.tests 

import org.scalatest.FunSpec

import com.jayway.awaitility.scala.AwaitilitySupport
import org.junit._
import Assert._


import java.util.concurrent.TimeUnit.MILLISECONDS

import com.jayway.awaitility.Awaitility._
import com.jayway.awaitility.core.ConditionTimeoutException


import com.pubnub.api._


import java.util.concurrent.TimeUnit
import  com.jayway.awaitility.Awaitility.await



import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
@RunWith(classOf[JUnitRunner])
class PublishSpec extends FunSpec with AwaitilitySupport {
  /*
  describe("Publish()") {

    it("should be able to publish message successfully") {
      
      var pubnub = new Pubnub("demo", "demo")

      var result = 0;

      var callback = new Callback() {

        override def successCallback(channel: String, message: Object) {
          result = 1
        }

        override def errorCallback(channel: String, error: PubnubError) {

        }
      }

      pubnub.publish("abcd", "hi", callback)
      await atMost(3000, MILLISECONDS) until { result == 1 }

    }
    it("should be able to publish message successfully with encryption enabled") {

      var pubnub = new Pubnub("demo", "demo", "demo", "demo", false)

      var result = 0;

      var callback = new Callback() {

        override def successCallback(channel: String, message: Object) {
          result = 1
        }

        override def errorCallback(channel: String, error: PubnubError) {

        }
      }

      pubnub.publish("abcd", "hi", callback)
      await atMost(3000, MILLISECONDS) until { result == 1 }

    }
  }
  */
}


package com.pubnub.api.tests

import org.json.{JSONArray, JSONObject}
import org.scalatest.{Tag, fixture}

import com.jayway.awaitility.scala.AwaitilitySupport
import org.junit._
import Assert._


import java.util.concurrent.TimeUnit.MILLISECONDS

import com.jayway.awaitility.Awaitility._
import com.jayway.awaitility.core.ConditionTimeoutException


import com.pubnub.api._


import java.util.concurrent.TimeUnit
import  com.jayway.awaitility.Awaitility.await


import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import scala.util.Try



import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.util.Random


object PublishTest extends Tag("com.pubnub.api.tests.PublishTest")

@RunWith(classOf[JUnitRunner])
class PublishSpec extends fixture.FunSpec with AwaitilitySupport {

  var PUBLISH_KEY   = ""
  var SUBSCRIBE_KEY = ""
  var SECRET_KEY    = ""
  var CIPHER_KEY    = ""
  var SSL           = false
  var RANDOM        = new Random()



  type FixtureParam = PubnubTestConfig

  def getRandom(): String = {
    return RANDOM.nextInt().toString
  }

  def withFixture(test: OneArgTest) {
    var pubnubTestConfig = new PubnubTestConfig()
    PUBLISH_KEY = test.configMap.getRequired[String]("publish_key").asInstanceOf[String]
    SUBSCRIBE_KEY = test.configMap.getRequired[String]("subscribe_key").asInstanceOf[String]
    SECRET_KEY = test.configMap.getRequired[String]("secret_key").asInstanceOf[String]
    var cipher = test.configMap.getOptional[String]("cipher_key")
    if (cipher != scala.None) {
      CIPHER_KEY = test.configMap.getRequired[String]("cipher_key").asInstanceOf[String]
    }
    SSL = Try(test.configMap.getRequired[String]("ssl").asInstanceOf[String].toBoolean).getOrElse(false)
    val pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY, CIPHER_KEY, SSL)
    pubnubTestConfig.pubnub = pubnub
    withFixture(test.toNoArgTest(pubnubTestConfig))
  }

  describe("Publish()") {

    it("should be able to publish String with double quotes successfully") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      //var channel = "channel-" + getRandom()
      var channel = "ab"
      var message = "message-" + "\"hi\""
      var testObj = new PnTest(3)

      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object): Unit = {
          testObj.test(true)
          pubnub.publish(channel, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assertTrue(error.toString, false)
            }
          })

        }
        override def successCallback(channel: String, message1: Object) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          println(message1)
          assertTrue(message1.toString, (message1.getClass()).equals(message.getClass()))
          assertTrue(message1.equals(message))
        }
        override def errorCallback(channel: String, error: PubnubError) {
          pubnub.unsubscribe(channel)
          assertTrue(error.toString, false)
        }
      });

      await atMost(5000, MILLISECONDS) until { testObj.checksRemaining() == 0 }

    }

    it("should be able to publish JSON Object successfully", PublishTest ) { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      //var channel = "channel-" + getRandom()
      var channel = "ab"
      var message = new JSONObject();
      message.put("a", "b")
      var testObj = new PnTest(3)

      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object): Unit = {
          testObj.test(true)
          pubnub.publish(channel, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assertTrue(error.toString, false)
            }
          })

        }
        override def successCallback(channel: String, message1: Object) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          assertTrue(message1.toString + " : " + message1.getClass(), (message1.getClass()).equals(message.getClass()))
          assertTrue(message1.asInstanceOf[JSONObject].getString("a").equals("b"))
        }
        override def errorCallback(channel: String, error: PubnubError) {
          pubnub.unsubscribe(channel)
          assertTrue(error.toString, false)
        }
      });

      await atMost(10000, MILLISECONDS) until { testObj.checksRemaining() == 0 }

    }

    it("should be able to publish JSON Object literal string successfully", PublishTest) { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      //var channel = "channel-" + getRandom()
      var channel = "ab"
      var message = "{\"a\":\"b\"}"
      var testObj = new PnTest(3)

      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object): Unit = {
          testObj.test(true)
          pubnub.publish(channel, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assertTrue(error.toString, false)
            }
          })

        }
        override def successCallback(channel: String, message1: Object) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          assertTrue(message1.toString + " : " + message1.getClass(), (message1.getClass()).equals(message.getClass()))
          assertTrue(message1.toString + " : " + message.toString + "[" + message1.getClass() + "]", message1.equals(message))

        }
        override def errorCallback(channel: String, error: PubnubError) {
          pubnub.unsubscribe(channel)
          assertTrue(error.toString, false)
        }
      });

      await atMost(10000, MILLISECONDS) until { testObj.checksRemaining() == 0 }

    }



  }
}
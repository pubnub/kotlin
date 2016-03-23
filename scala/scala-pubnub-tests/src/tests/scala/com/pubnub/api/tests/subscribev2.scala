
package com.pubnub.api.tests

import org.json.{JSONArray, JSONObject}
import org.scalatest.{BeforeAndAfterAll, fixture, Tag}

import com.jayway.awaitility.scala.AwaitilitySupport
import org.junit._

import org.json._
import Assert._


import java.util.concurrent.TimeUnit.MILLISECONDS

import com.jayway.awaitility.Awaitility._
import com.jayway.awaitility.core.ConditionTimeoutException


import com.pubnub.api._


import java.util.concurrent.TimeUnit
import  com.jayway.awaitility.Awaitility.await


import org.junit.Assert.assertEquals
import org.junit.Assert.fail



import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.util.Random
import scala.util.Try

object ErrorTest extends Tag("com.pubnub.api.tests.ErrorTest")


@RunWith(classOf[JUnitRunner])
class SubscribeV2Spec extends fixture.FunSpec with AwaitilitySupport  with BeforeAndAfterAll {

  var PUBLISH_KEY   = ""
  var SUBSCRIBE_KEY = ""
  var SECRET_KEY    = ""
  var CIPHER_KEY    = ""
  var SSL           = false
  var RANDOM        = new Random()
  var TIMEOUT       = 30000
  var UNICODE       = false


  type FixtureParam = PubnubTestConfig

  def getRandom(unicode:Boolean = false): String = {
    var s = RANDOM.nextInt(99999999).toString
    if (unicode) {
      s += "☺☻✌☹"
    }
    return s
  }

  // Delete the temp file
  override def afterAll(): Unit = {
    var pubnub_sync = new PubnubSync(PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY, CIPHER_KEY, SSL)
    var response = pubnub_sync.channelGroupListGroups()
    if (response != null && response.get("payload") != null &&
      response.get("payload").asInstanceOf[JSONObject].get("groups") != null) {
    var groups = response.get("payload").asInstanceOf[JSONObject].get("groups").asInstanceOf[JSONArray]

      for (i <- 1 to (groups.length() - 1)) {
        pubnub_sync.channelGroupRemoveGroup(groups.get(i).asInstanceOf[String])
      }
    }
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

    UNICODE = Try(test.configMap.getRequired[String]("unicode").asInstanceOf[String].toBoolean).getOrElse(false)

    val pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY, CIPHER_KEY, SSL)

    var metadata = new JSONObject()
    metadata.put("foo", "bar")

    pubnub.setResumeOnReconnect(true)
    pubnub.setV2(true)




    pubnubTestConfig.pubnub = pubnub
    pubnubTestConfig.unicode = UNICODE
    pubnubTestConfig.metadata = metadata

    /*
    var pubnub_sync = new PubnubSync(PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY, CIPHER_KEY, SSL)
    //pubnub_sync.setResumeOnReconnect(true)
    //pubnub_sync.setCacheBusting(false)

    var response = pubnub_sync.channelGroupListGroups()
    var groups = response.get("payload").asInstanceOf[JSONObject].get("groups").asInstanceOf[JSONArray]

    for (i <- 1 to (groups.length() - 1)) {
      pubnub_sync.channelGroupRemoveGroup(groups.get(i).asInstanceOf[String])
    }
    */

    withFixture(test.toNoArgTest(pubnubTestConfig))



  }


  describe("SubscribeV2()") {


    it("should receive message when subscribed with filtering attribute foo==bar " +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(4)

      pubnub.setFilter("foo==\"bar\"")

      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          testObj.test(message1.equals(message))

        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
      }
    }


    it("should be able to receive message successfully when subscribed with no filtering attribute" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(4)

      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          testObj.test(message1.equals(message))

        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
      }
    }

    it("should be able to receive message successfully when subscribed with no filtering attribute" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(4)

      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          testObj.test(message1.equals(message))

        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
      }
    }
    it("should not receive message when subscribed with filtering attribute foo==bar" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("foo==\"bar\"")
      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
      }

    }
    it("should not receive message when subscribed with filtering attribute a==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>
      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("a==\"b\"")
      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
      }
    }
    it("should not receive message when subscribed with filtering attribute foo==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("foo==\"b\"")
      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
      }

    }
    it("should not receive message when subscribed with filtering attribute bar==foo" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("bar==\"foo\"")
      pubnub.subscribe(channel, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
      }

    }


    it("should receive message when subscribed to wildcard channel with filtering attribute foo==bar " +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("foo==\"bar\"")
      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel_wildcard_c, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          assertTrue(message1.equals(message))

        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }
    }


    it("should be able to receive message successfully when subscribed to wildcard channel with no filtering attribute" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel_wildcard_c, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          assertTrue(message1.equals(message))

        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }
    }

    it("should be able to receive message successfully when subscribed to wildcard channel with no filtering attribute" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)

          pubnub.publish(channel_wildcard_c, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          testObj.test(true)
          assertTrue(message1.equals(message))

        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }
    }
    it("should not receive message when subscribed to wildcard channel with filtering attribute foo==bar" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("foo==\"bar\"")
      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel_wildcard_c, message, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to wildcard channel with filtering attribute a==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("a==\"b\"")
      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel_wildcard_c, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to wildcard channel with filtering attribute foo==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>
      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("foo==\"b\"")
      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel_wildcard_c, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to wildcard channel with filtering attribute bar==foo" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>
      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(3)

      pubnub.setFilter("bar==\"foo\"")
      pubnub.subscribe(channel_wildcard, new Callback {
        override def connectCallback(channel: String, message1: Object) {
          testObj.test(true)
          pubnub.publish(channel_wildcard_c, message, metadata, new Callback {

            override def successCallback(channel: String, message: Object) {
              testObj.test(true)
            }

            override def errorCallback(channel: String, error: PubnubError) {
              assert(false)
              testObj.test(false)
            }
          })

        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          pubnub.unsubscribe(channel)
          assertTrue(false)
        }
      });
      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }

    /*********/

    it("should receive message when subscribed to channel and wildcard channel with filtering attribute foo==bar " +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata  = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(6)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, metadata, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message_wildcard_c))
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          testObj.test(true)
          assertTrue(message1.equals(message))
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }
    }


    it("should be able to receive message successfully when subscribed to channel and wildcard channel with no filtering attribute" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(6)

      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message_wildcard_c))
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          testObj.test(true)
          assertTrue(message1.equals(message))
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }

    it("should be able to receive message successfully when subscribed to channel and wildcard channel with no filtering attribute" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata  = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(6)

      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, metadata, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message_wildcard_c))
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          testObj.test(true)
          assertTrue(message1.equals(message))
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to channel and wildcard channel with filtering attribute foo==bar" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(5)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          assertTrue(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to channel and wildcard channel with filtering attribute a==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata  = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(5)

      pubnub.setFilter("(a==\"b\")")
      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, metadata, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          assertTrue(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to channel and wildcard channel with filtering attribute foo==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata  = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(5)

      pubnub.setFilter("(foo==\"b\")")
      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, metadata, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          assertTrue(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }
    it("should not receive message when subscribed to channel and wildcard channel with filtering attribute bar==foo" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata  = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_wildcard_c = "message-wildcard-" + getRandom(pubnubTestConfig.unicode)
      var testObj = new PnTest(5)

      pubnub.setFilter("(bar==\"foo\")")
      pubnub.subscribe(channel, new Callback {

        override def connectCallback(channel1: String, message1: Object) {
          testObj.test(true)

          pubnub.subscribe(channel_wildcard, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, metadata, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })
        }
        override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
          assertTrue(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }


    }


    /*********/


    it("should receive message when subscribed to channel group with filtering attribute foo==bar " +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message_group_c))
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(error.toString, false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }
    }


    it("should be able to receive message successfully when subscribed to channel group with no filtering attribute" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)


      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message_group_c))
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(error.toString, false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }

    }

    it("should be able to receive message successfully when subscribed to channel group with no filtering attribute" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)


      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message_group_c))
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(error.toString, false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }


    }
    it("should not receive message when subscribed to channel group  with filtering attribute foo==bar" +
      " when message published with no metadata") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(error.toString, false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }


    }
    it("should not receive message when subscribed to channel group with filtering attribute a==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>
      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)

      pubnub.setFilter("(a==\"b\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }



    }
    it("should not receive message when subscribed to channel group with filtering attribute foo==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)

      pubnub.setFilter("(foo==\"b\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(error.toString(), false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }



    }
    it("should not receive message when subscribed to channel group with filtering attribute bar==foo" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(5)

      pubnub.setFilter("(bar==\"foo\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)
              pubnub.publish(channel, message, new Callback {

                override def successCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {
                      testObj.test(true)
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }

                  })
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }

            override def errorCallback(channel1: String, error: PubnubError): Unit = {
              assertTrue(false)
              pubnub.channelGroupUnsubscribe(channel_group)
            }


          })
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }


    }

    /**************/



    it("should receive message when subscribed to channel and channel group with filtering attribute foo==bar " +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(7)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel, message, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }
                  })
                }

                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  testObj.test(true)
                  //pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              //pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
          testObj.test(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }
    }


    it("should receive message when subscribed to channel and channel group with filtering attribute foo==bar " +
      " when message published with metadata foo:bar" +
      ", and when unsubscribed on receiving message") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(7)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.publish(channel, message, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }

                    override def errorCallback(channel1: String, error: PubnubError) {
                      assertTrue(false)
                    }
                  })
                }

                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  testObj.test(true)
                  pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }

            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
          testObj.test(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }
    }

    it("should be able to receive message successfully when subscribed to channel and channel group with no filtering attribute" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(7)

      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {
                  testObj.test(true)
                  pubnub.publish(channel, message, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  testObj.test(true)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }

    }

    it("should be able to receive message successfully when subscribed to channel and channel group with no filtering attribute" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>


      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(7)

      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {
                  testObj.test(true)
                  pubnub.publish(channel, message, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  testObj.test(true)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              testObj.test(true)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }

    }
    it("should not receive message when subscribed to channel and channel group  with filtering attribute foo==bar" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(6)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {
                  testObj.test(true)
                  pubnub.publish(channel, message, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  assertTrue(false)
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }


    }
    it("should not receive message when subscribed to channel and channel group with filtering attribute a==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>


      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(6)

      pubnub.setFilter("(a==\"b\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {
                  testObj.test(true)
                  pubnub.publish(channel, message, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  assertTrue(false)
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }


    }
    it("should not receive message when subscribed to channel and channel group with filtering attribute foo==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>


      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(6)

      pubnub.setFilter("(foo==\"b\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {
                  testObj.test(true)
                  pubnub.publish(channel, message, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  assertTrue(false)
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }


    }
    it("should not receive message when subscribed to channel and channel group with filtering attribute bar==foo" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"

      var testObj = new PnTest(6)

      pubnub.setFilter("(bar==\"foo\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {
                  testObj.test(true)
                  pubnub.publish(channel, message, metadata, new Callback {

                    override def successCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {
                          testObj.test(true)
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }

                      })
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  assertTrue(false)
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
      }
    }



    /**************/



    it("should receive message when subscribed to channel, wildcard channel" +
      " and channel group with filtering attribute foo==bar " +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(10)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      //println(message_wildcard_c)
                      testObj.test(true)
                      //pubnub.unsubscribe(channel_wildcard)
                      assertTrue(message1.equals(message_wildcard_c))
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {

                  testObj.test(true)
                  //pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              //println(message1)
              testObj.test(true)
              //pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false, error.toString)
          testObj.test(false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }
    }

    it("should receive message when subscribed to channel, wildcard channel" +
      " and channel group with filtering attribute foo==bar " +
      " when message published with metadata foo:bar," +
      " and when unsubscribed from on receiving messages in callback") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(10)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      //println(message1)
                      testObj.test(true)
                      pubnub.unsubscribe(channel_wildcard)
                      assertTrue(message1.equals(message_wildcard_c))
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  //println(message1)
                  testObj.test(true)
                  pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              //println(message1)
              testObj.test(true)
              pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
          testObj.test(false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }
    }



    it("should be able to receive message successfully when subscribed to channel, wildcard channel" +
      " and channel group with no filtering attribute" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(10)

      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      //println(message1)
                      testObj.test(true)
                      //pubnub.unsubscribe(channel_wildcard)
                      assertTrue(message1.equals(message_wildcard_c))
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  //println(message1)
                  testObj.test(true)
                  //pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              //println(message1)
              testObj.test(true)
              //pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
          testObj.test(false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }

    it("should be able to receive message successfully when subscribed to channel, wildcard channel" +
      " and channel group with no filtering attribute" +
      " when message published with no metadata," +
      " when unsubscribed after receiving message") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(10)

      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      //println(message1)
                      testObj.test(true)
                      pubnub.unsubscribe(channel_wildcard)
                      assertTrue(message1.equals(message_wildcard_c))
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  //println(message1)
                  testObj.test(true)
                  pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              //println(message1)
              testObj.test(true)
              pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
          testObj.test(false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }


    it("should be able to receive message successfully when subscribed to channel, wildcard channel" +
      " and channel group with no filtering attribute" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(10)

      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      //println(message1)
                      testObj.test(true)
                      //pubnub.unsubscribe(channel_wildcard)
                      assertTrue(message1.equals(message_wildcard_c))
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  //println(message1)
                  testObj.test(true)
                  //pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              //println(message1)
              testObj.test(true)
              //pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assert(false)
          testObj.test(false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
        pubnub.unsubscribe(channel)
        pubnub.unsubscribe(channel_wildcard)
      }

    }

    it("should be able to receive message successfully when subscribed to channel, wildcard channel" +
      " and channel group with no filtering attribute" +
      " when message published with metadata foo:bar," +
      " when unsubscribed after receiving messages") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(10)


      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(error.toString(), false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(error.toString(), false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(error.toString(), false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      //println(message1)
                      testObj.test(true)
                      pubnub.unsubscribe(channel_wildcard)
                      assertTrue(message1.equals(message_wildcard_c))
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  //println(message1)
                  testObj.test(true)
                  pubnub.channelGroupUnsubscribe(channel_group)
                  assertTrue(message1.equals(message_group_c))
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              //println(message1)
              testObj.test(true)
              pubnub.unsubscribe(channel)
              assertTrue(message1.equals(message))
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assertTrue(error.toString(), false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: Exception => throw e
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
      }

    }


    it("should not receive message when subscribed to channel, wildcard channel" +
      " and channel group  with filtering attribute foo==bar" +
      " when message published with no metadata") { pubnubTestConfig =>

      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(8)

      pubnub.setFilter("(foo==\"bar\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(error.toString(), false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(error.toString(), false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(error.toString(), false)
                        }
                      })

                    }
                    override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                      assertTrue(false)
                    }
                  })
                }
                override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
                  assertTrue(false)
                }
              })
            }
            override def successCallbackV2(channel: String, message1: Object, envelope: JSONObject) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assertTrue(error.toString(), false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
      }



    }
    it("should not receive message when subscribed to channel, wildcard channel" +
      " and channel group with filtering attribute a==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(8)

      pubnub.setFilter("(a==\"b\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(error.toString(), false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(error.toString(), false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(error.toString(), false)
                        }
                      })

                    }
                    override def successCallback(channel1: String, message1: Object) {
                      assertTrue(false)
                    }
                  })
                }
                override def successCallback(channel1: String, message1: Object) {
                  assertTrue(false)
                }
              })
            }
            override def successCallback(channel1: String, message1: Object) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assertTrue(error.toString(), false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
      }



    }
    it("should not receive message when subscribed to channel, wildcard channel" +
      " and channel group with filtering attribute foo==b" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(8)

      pubnub.setFilter("(foo==\"b\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(error.toString(), false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(error.toString(), false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(error.toString(), false)
                        }
                      })

                    }
                    override def successCallback(channel1: String, message1: Object) {
                      assertTrue(false)
                    }
                  })
                }
                override def successCallback(channel1: String, message1: Object) {
                  assertTrue(false)
                }
              })
            }
            override def successCallback(channel1: String, message1: Object) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assertTrue(error.toString(), false)
        }
      })


      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
      }



    }
    it("should not receive message when subscribed to channel, wildcard channel " +
      "and channel group with filtering attribute bar==foo" +
      " when message published with metadata foo:bar") { pubnubTestConfig =>

      var metadata = pubnubTestConfig.metadata
      var pubnub  = pubnubTestConfig.pubnub
      var channel = "channel-" + getRandom()
      var channel_wildcard = channel + ".*"
      var channel_wildcard_c = channel + ".a"
      var channel_group = channel + "-group"
      var channel_group_c = channel + "-channel"
      var message = "message-" + getRandom(pubnubTestConfig.unicode)
      var message_group_c = message + "-group-channel"
      var message_wildcard_c = message + "-wildcard-channel"

      var testObj = new PnTest(8)

      pubnub.setFilter("(bar==\"foo\")")
      pubnub.channelGroupAddChannel(channel_group, channel_group_c, new Callback {

        override def successCallback(channel1: String, message1: Object) {

          testObj.test(true)

          pubnub.subscribe(channel, new Callback {

            override def connectCallback(channel1: String, message1: Object) {
              testObj.test(true)

              pubnub.channelGroupSubscribe(Array(channel_group), new Callback {

                override def connectCallback(channel1: String, message1: Object) {

                  testObj.test(true)
                  pubnub.subscribe(channel_wildcard, new Callback {

                    override def connectCallback(channel1: String, message1: Object) {

                      testObj.test(true)
                      pubnub.publish(channel, message, metadata, new Callback {

                        override def successCallback(channel1: String, message1: Object) {

                          testObj.test(true)
                          pubnub.publish(channel_group_c, message_group_c, metadata, new Callback {

                            override def successCallback(channel1: String, message1: Object) {
                              testObj.test(true)
                              pubnub.publish(channel_wildcard_c, message_wildcard_c, metadata, new Callback {

                                override def successCallback(channel1: String, message1: Object) {
                                  testObj.test(true)
                                }

                                override def errorCallback(channel1: String, error: PubnubError) {
                                  assertTrue(error.toString(), false)
                                }

                              })
                            }

                            override def errorCallback(channel1: String, error: PubnubError) {
                              assertTrue(error.toString(), false)
                            }

                          })
                        }

                        override def errorCallback(channel1: String, error: PubnubError) {
                          assertTrue(error.toString(), false)
                        }
                      })

                    }
                    override def successCallback(channel1: String, message1: Object) {
                      assertTrue(false)
                    }
                  })
                }
                override def successCallback(channel1: String, message1: Object) {
                  assertTrue(false)
                }
              })
            }
            override def successCallback(channel1: String, message1: Object) {
              assertTrue(false)
            }
          })

        }

        override def errorCallback(channel: String, error: PubnubError) {
          assertTrue(error.toString(), false)
        }
      })

      try {
        await atMost(TIMEOUT, MILLISECONDS) until { testObj.checksRemaining() == 0 }
      } catch {
        case e: com.jayway.awaitility.core.ConditionTimeoutException => {
          if (testObj.checksRemaining() > 1) throw e
        }
        case e: Exception => {
          throw e
        }
      }
      finally {
        pubnub.channelGroupUnsubscribe(channel_group)
      }




    }


    /**************/
  }
}

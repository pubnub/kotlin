
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


object NewTest extends Tag("com.pubnub.api.tests.NewTest")

@RunWith(classOf[JUnitRunner])
class SubscribeV2TestsSpec extends fixture.FunSpec with AwaitilitySupport  with BeforeAndAfterAll {

  var PUBLISH_KEY   = ""
  var SUBSCRIBE_KEY = ""
  var SECRET_KEY    = ""
  var CIPHER_KEY    = ""
  var SSL           = false
  var RANDOM        = new Random()
  var TIMEOUT       = 30000
  var UNICODE       = false


  val tests = Array(
    Map(
      "name" -> "Exact Number Match",
      "filter" -> "(count == 42)",
      "metadata" -> new JSONObject().put("count", 42),
      "success" -> true
    ),
    Map(
      "name" -> "Arithmetic",
      "filter" -> "(attributes.var1 + attributes['var2'] == 30)",
      "metadata" -> new JSONObject().put("attributes", new JSONObject().put("var1", 10).put("var2", 20)),
      "success" -> true
    ),
    Map(
      "name" -> "Arithmetic",
      "filter" -> "(meta.data.var1 + data['var2'] == 30)",
      "metadata" -> new JSONObject().put("data", new JSONObject().put("var1", 10).put("var2", 20)),
      "success" -> true
    ),/*
    Map(
      "name" -> "Arithmetic BUG BUG BUG",
      "filter" -> "(data.var1 + data['var2'] == 20)",
      "metadata" -> new JSONObject().put("data", new JSONObject().put("var1", 10).put("var2", 20)),
      "success" -> false
    ),
  */
    Map(
      "name" -> "Larger than or equal match",
      "filter" -> "(regions.east.count >= 42)",
      "metadata" -> new JSONObject().put("regions", new JSONObject().put("east", new JSONObject().put("count", 42).put("other", "something"))),
      "success" -> true
    ),

    Map(
      "name" -> "Smaller than mismatch",
      "filter" -> "(regions.east.count < 42)",
      "metadata" -> new JSONObject().put("regions",
        new JSONObject().put("east", new JSONObject().put("count", 42).put("other", "something"))),
      "success" -> false
    ),
    Map(
      "name" -> "Missing variable evaluating to 0, mismatch",
      "filter" -> "(regions.east.volume > 0)",
      "metadata" -> new JSONObject().put("regions",
        new JSONObject().put("east", new JSONObject().put("count", 42).put("other", "something"))),
      "success" -> false
    ),
    Map(
      "name" -> "Missing variable evaluating to 0, mismatch",
      "filter" -> "(regions.east.volume > 0)",
      "metadata" -> new JSONObject().put("regions",
        new JSONObject().put("east", new JSONObject().put("count", 42).put("other", "something"))),
      "success" -> false
    ),
    Map(
      "name" -> "Exact string match",
      "filter" -> "(region==\"east\")",
      "metadata" -> new JSONObject().put("region", "east"),
      "success" -> true
    ),

    Map(
      "name" -> "String case mismatch for ==",
      "filter" -> "(region==\"East\")",
      "metadata" -> new JSONObject().put("region", "east"),
      "success" -> false
    ),

    Map(
      "name" -> "String match against list of matches",
      "filter" -> "(region in (\"east\",\"west\"))",
      "metadata" -> new JSONObject().put("region", "east"),
      "success" -> true
    ),
    Map(
      "name" -> "String match against list of matches",
      "filter" -> "(\"east\" in region)",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east").put("west")),
      "success" -> true
    ),
    Map(
      "name" -> "Negated array mismatch against string",
      "filter" -> "(!(\"central\" in region))",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east").put("west")),
      "success" -> true
    ),

    Map(
      "name" -> "Case mismatch in array match",
      "filter" -> "(\"East\" in region)",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east").put("west")),
      "success" -> false),
    Map(
      "name" -> "Array LIKE match",
      "filter" -> "(region like \"EAST\")",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east").put("west")),
      "success" -> true
    ),
    Map(
      "name" -> "Array LIKE match with wildcard",
      "filter" -> "(region like \"EAST%\")",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east").put("west")),
      "success" -> true
    ),

    Map(
      "name" -> "Array LIKE match with wildcard",
      "filter" -> "(region like \"EAST%\")",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east coast").put("west coast")),
      "success" -> true
    ),

    Map(
      "name" -> "Array LIKE match with wildcard",
      "filter" -> "(region like \"%east\")",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("north east").put("west")),
      "success" -> true
    ),
    Map(
      "name" -> "Array LIKE match with wildcard",
      "filter" -> "(region like \"%est%\")",
      "metadata" -> new JSONObject().put("region", new JSONArray().put("east coast").put("west coast")),
      "success" -> true
    )
  )


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
    //pubnub_sync.setResumeOnReconnect(true)
    //pubnub_sync.setCacheBusting(false)
    pubnub_sync.setOrigin("msgfiltering-dev")

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
    pubnub.setCacheBusting(false)
    pubnub.setOrigin("msgfiltering-dev")
    pubnub.setV2(true)




    pubnubTestConfig.pubnub = pubnub
    pubnubTestConfig.unicode = UNICODE

    /*
    var pubnub_sync = new PubnubSync(PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY, CIPHER_KEY, SSL)
    //pubnub_sync.setResumeOnReconnect(true)
    //pubnub_sync.setCacheBusting(false)
    pubnub_sync.setOrigin("msgfiltering-dev")

    var response = pubnub_sync.channelGroupListGroups()
    var groups = response.get("payload").asInstanceOf[JSONObject].get("groups").asInstanceOf[JSONArray]

    for (i <- 1 to (groups.length() - 1)) {
      pubnub_sync.channelGroupRemoveGroup(groups.get(i).asInstanceOf[String])
    }
    */

    withFixture(test.toNoArgTest(pubnubTestConfig))


  }

  def runTest(name: String, metadata: JSONObject, filter: String, success: Boolean): Unit = {

    if (success) {
      it(name, NewTest) { pubnubTestConfig =>

        var channel = "channel-" + getRandom()
        var pubnub = pubnubTestConfig.pubnub
        var message = "message-" + getRandom(pubnubTestConfig.unicode)
        var testObj = new PnTest(4)

        pubnub.setFilter(filter)

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
    } else {
      it(name, NewTest) { pubnubTestConfig =>

        var pubnub  = pubnubTestConfig.pubnub
        var channel = "channel-" + getRandom()
        var message = "message-" + getRandom(pubnubTestConfig.unicode)
        var testObj = new PnTest(3)

        pubnub.setFilter(filter)
        pubnub.subscribe(channel, new Callback {
          override def connectCallback(channel: String, message1: Object) {
            testObj.test(true)

            pubnub.publish(channel, message, metadata, new Callback {

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
    }

  }

  describe("SubscribeV2()") {




    for (i <- 0 to (tests.length - 1)) {
      runTest(i + ": " + tests(i).get("name").get.asInstanceOf[String],
        tests(i).get("metadata").get.asInstanceOf[JSONObject],
        tests(i).get("filter").get.asInstanceOf[String],
        tests(i).get("success").get.asInstanceOf[Boolean]
      )
    }


    /*
    runTest("0: " + tests(0).get("name").get.asInstanceOf[String],
      tests(0).get("metadata").get.asInstanceOf[JSONObject],
      tests(0).get("filter").get.asInstanceOf[String],
      tests(0).get("success").get.asInstanceOf[Boolean]
    )
    runTest("1: " +tests(1).get("name").get.asInstanceOf[String],
      tests(1).get("metadata").get.asInstanceOf[JSONObject],
      tests(1).get("filter").get.asInstanceOf[String],
      tests(1).get("success").get.asInstanceOf[Boolean]
    )

    runTest("2: " +tests(2).get("name").get.asInstanceOf[String],
      tests(2).get("metadata").get.asInstanceOf[JSONObject],
      tests(2).get("filter").get.asInstanceOf[String],
      tests(2).get("success").get.asInstanceOf[Boolean]
    )

    runTest("3: " +tests(3).get("name").get.asInstanceOf[String],
      tests(3).get("metadata").get.asInstanceOf[JSONObject],
      tests(3).get("filter").get.asInstanceOf[String],
      tests(3).get("success").get.asInstanceOf[Boolean]
    )

    runTest("4: " +tests(4).get("name").get.asInstanceOf[String],
      tests(4).get("metadata").get.asInstanceOf[JSONObject],
      tests(4).get("filter").get.asInstanceOf[String],
      tests(4).get("success").get.asInstanceOf[Boolean]
    )

    runTest("5: " +tests(5).get("name").get.asInstanceOf[String],
      tests(5).get("metadata").get.asInstanceOf[JSONObject],
      tests(5).get("filter").get.asInstanceOf[String],
      tests(5).get("success").get.asInstanceOf[Boolean]
    )

    runTest("6: " +tests(6).get("name").get.asInstanceOf[String],
      tests(6).get("metadata").get.asInstanceOf[JSONObject],
      tests(6).get("filter").get.asInstanceOf[String],
      tests(6).get("success").get.asInstanceOf[Boolean]
    )


    runTest("7: " +tests(7).get("name").get.asInstanceOf[String],
      tests(7).get("metadata").get.asInstanceOf[JSONObject],
      tests(7).get("filter").get.asInstanceOf[String],
      tests(7).get("success").get.asInstanceOf[Boolean]
    )

    runTest("8: " +tests(8).get("name").get.asInstanceOf[String],
      tests(8).get("metadata").get.asInstanceOf[JSONObject],
      tests(8).get("filter").get.asInstanceOf[String],
      tests(8).get("success").get.asInstanceOf[Boolean]
    )

    runTest("9: " +tests(9).get("name").get.asInstanceOf[String],
      tests(9).get("metadata").get.asInstanceOf[JSONObject],
      tests(9).get("filter").get.asInstanceOf[String],
      tests(9).get("success").get.asInstanceOf[Boolean]
    )


    runTest("10: " +tests(10).get("name").get.asInstanceOf[String],
      tests(10).get("metadata").get.asInstanceOf[JSONObject],
      tests(10).get("filter").get.asInstanceOf[String],
      tests(10).get("success").get.asInstanceOf[Boolean]
    )

    runTest("11: " +tests(11).get("name").get.asInstanceOf[String],
      tests(11).get("metadata").get.asInstanceOf[JSONObject],
      tests(11).get("filter").get.asInstanceOf[String],
      tests(11).get("success").get.asInstanceOf[Boolean]
    )


    runTest("12: " +tests(12).get("name").get.asInstanceOf[String],
      tests(12).get("metadata").get.asInstanceOf[JSONObject],
      tests(12).get("filter").get.asInstanceOf[String],
      tests(12).get("success").get.asInstanceOf[Boolean]
    )

    runTest("13: " +tests(13).get("name").get.asInstanceOf[String],
      tests(13).get("metadata").get.asInstanceOf[JSONObject],
      tests(13).get("filter").get.asInstanceOf[String],
      tests(13).get("success").get.asInstanceOf[Boolean]
    )

    runTest("14: " +tests(14).get("name").get.asInstanceOf[String],
      tests(14).get("metadata").get.asInstanceOf[JSONObject],
      tests(14).get("filter").get.asInstanceOf[String],
      tests(14).get("success").get.asInstanceOf[Boolean]
    )

    runTest("15: " +tests(15).get("name").get.asInstanceOf[String],
      tests(15).get("metadata").get.asInstanceOf[JSONObject],
      tests(15).get("filter").get.asInstanceOf[String],
      tests(15).get("success").get.asInstanceOf[Boolean]
    )

    runTest("16: " +tests(16).get("name").get.asInstanceOf[String],
      tests(16).get("metadata").get.asInstanceOf[JSONObject],
      tests(16).get("filter").get.asInstanceOf[String],
      tests(16).get("success").get.asInstanceOf[Boolean]
    )

    runTest("17: " +tests(17).get("name").get.asInstanceOf[String],
      tests(17).get("metadata").get.asInstanceOf[JSONObject],
      tests(17).get("filter").get.asInstanceOf[String],
      tests(17).get("success").get.asInstanceOf[Boolean]
    )
    */

  }
}
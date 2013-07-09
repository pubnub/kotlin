//package com.pubnub.examples

import com.pubnub.api._
import java.util._
import org.json._
import java.lang.Integer

object PubnubDevConsole {
  var pub_key = ""
  var sub_key = ""
  var sec_key = ""
  var cip_key = ""
  var SSL = false
  var reader: Scanner = null
  var pubnub: Pubnub = null 

  def getStringFromConsole(message: String, optional: Boolean = false): String = {

    var attempt_count = 0
    var input: String = null
    do {
      if (attempt_count > 0) print("Invalid input. ")
      var message1 = "Enter " + message
      message1 = if (optional) message1 + " ( Optional input. You can skip by pressing enter )" else message1
      notifyUser(message1)
      input = reader.nextLine()
      attempt_count += 1
    } while ((input == null || input.length() == 0) && !optional)
    notifyUser(message + " : " + input)
    return input
  }
  def getBooleanFromConsole(message: String, optional: Boolean = false): Boolean = {

    var attempt_count = 0
    var input: String = null
    var returnVal = false
    do {
      if (attempt_count > 0) notifyUser("Invalid input. ")
      var message1 = message + " ? ( Enter Yes/No or Y/N )"
      message1 = if (optional) message1 + " ( Optional input. You can skip by pressing enter ) " else message1
      notifyUser(message1)
      input = reader.nextLine()
      attempt_count += 1
    } while ((input == null || input.length() == 0 ||
      (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no") &&
        !input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n"))) && !optional)
    returnVal = if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) true else false
    notifyUser(message + " : " + returnVal)
    return returnVal
  }
  def getIntFromConsole(message: String, optional: Boolean = false): Int = {
    var attempt_count = 0
    var input: String = null
    var returnVal = -1
    do {
      if (attempt_count > 0) notifyUser("Invalid input. ")
      var message1 = "Enter " + message
      message1 = if (optional) message1 + " ( Optional input. You can skip by pressing enter ) " else message1
      notifyUser(message1)
      input = reader.nextLine()
      attempt_count += 1
      returnVal = Integer.parseInt(input)
    } while ((input == null || input.length() == 0 || returnVal == -1) && !optional)
    notifyUser(message + " : " + returnVal)
    return returnVal
  }
  def notifyUser(msg: String) {
    println(msg)
  }

  def disconnectAndResubscribeWithTimetoken(timetoken: String = "0") {
    pubnub.disconnectAndResubscribeWithTimetoken(timetoken)
  }

  def disconnectAndResubscribe(msg: String = "") {
    pubnub.disconnectAndResubscribe()
  }

  def subscribe(channelName: String) {

    try {
      pubnub.subscribe(channelName, new Callback() {
        override def connectCallback(channel: String, message: Object) {
          notifyUser("SUBSCRIBE : CONNECT on channel:" + channel
            + " : " + message.getClass() + " : "
            + message.toString())
        }

        override def disconnectCallback(channel: String, message: Object) {
          notifyUser("SUBSCRIBE : DISCONNECT on channel:" + channel
            + " : " + message.getClass() + " : "
            + message.toString())
        }

        override def reconnectCallback(channel: String, message: Object) {
          notifyUser("SUBSCRIBE : RECONNECT on channel:" + channel
            + " : " + message.getClass() + " : "
            + message.toString())
        }

        override def successCallback(channel: String, message: Object) {
          notifyUser("SUBSCRIBE : " + channel + " : "
            + message.getClass() + " : " + message.toString())
        }

        override def errorCallback(channel: String, error: PubnubError) {
          notifyUser("SUBSCRIBE : ERROR on channel " + channel
            + " : " + error.toString())
          error.errorCode match {
              case PubnubError.PNERR_FORBIDDEN => { pubnub.unsubscribe(channel);}
              case PubnubError.PNERR_UNAUTHORIZED => {pubnub.unsubscribe(channel);}
              case _ => {}
          } 
        }
      })

    } catch {
      case e: Exception => {}

    }
  }
  def publish(channel: String) {
    notifyUser("Enter the message for publish. To exit loop enter QUIT")
    var message = "";

    var callback = new Callback() {

      override def successCallback(channel: String, message: Object) {
        notifyUser("PUBLISH : " + message);
      }

      override def errorCallback(channel: String, error: PubnubError) {
        notifyUser("PUBLISH : " + error);
      }
    }
    while (true) {
      var args = new Hashtable(2);
      var parsed = false
      message = reader.nextLine();
      if (message.equalsIgnoreCase("QUIT")) {
        return
      }
      if (!parsed) {
        try {
          var i: Integer = message.toInt
          pubnub.publish(channel, i, callback)
          parsed = true
        } catch {
          case e: Exception => {}
        }
      }
      if (!parsed) {
        try {
          var d = message.toDouble
          parsed = true
          pubnub.publish(channel, d, callback)
        } catch {
          case e: Exception => {}

        }
      }
      if (!parsed) {
        try {
          var js: JSONArray = new JSONArray(message)
          parsed = true
          pubnub.publish(channel, js, callback)
        } catch {
          case e: Exception => {}

        }
      }
      if (!parsed) {
        try {
          var js: JSONObject = new JSONObject(message);
          pubnub.publish(channel, js, callback)
          parsed = true
        } catch {
          case e: Exception => {}

        }
      }
      if (!parsed) {
        pubnub.publish(channel, message, callback)
      }

    }

  }
  def presence(channel: String) {

    try {
      pubnub.presence(channel, new Callback() {

        override def successCallback(channel: String, message: Object) {
          notifyUser("PRESENCE : " + message)
        }

        override def errorCallback(channel: String, error: PubnubError) {
          notifyUser("PRESENCE : " + error)
        }
      })
    } catch {
      case e: PubnubException => {}

    }
  }
  def history(channel: String) {

    pubnub.history(channel, 2, new Callback() {
      override def successCallback(channel: String, message: Object) {
        notifyUser("DETAILED HISTORY : " + message)
      }

      override def errorCallback(channel: String, error: PubnubError) {
        notifyUser("DETAILED HISTORY : " + error)
      }
    })
  }
  def hereNow(channel: String) {

    pubnub.hereNow(channel, new Callback() {
      @Override
      override def successCallback(channel: String, message: Object) {
        notifyUser("HERE NOW : " + message)
      }

      @Override
      override def errorCallback(channel: String, error: PubnubError) {
        notifyUser("HERE NOW : " + error)
      }
    })
  }
  def unsubscribe(channel: String) {
    pubnub.unsubscribe(channel)
  }
  def unsubscribePresence(channel: String) {
    pubnub.unsubscribePresence(channel)
  }

  def pamGrant() {

    var channel = getStringFromConsole("Channel")
    var auth_key = getStringFromConsole("Auth Key")
    var read = getBooleanFromConsole("Read")
    var write = getBooleanFromConsole("Write")

    pubnub.pamGrant(channel, auth_key, read, write, new Callback() {

      override def successCallback(channel: String, message: Object) {
        notifyUser("CHANNEL : " + channel + " , " + message.toString())

      }

      override def errorCallback(channel: String, error: PubnubError) {
        notifyUser("CHANNEL : " + channel + " , " + error.toString())
      }

    })
  }
  def pamRevoke() {
    var channel = getStringFromConsole("Enter Channel")
    var auth_key = getStringFromConsole("Auth Key")

    pubnub.pamRevoke(channel, auth_key, new Callback() {

      override def successCallback(channel: String, message: Object) {
        notifyUser("CHANNEL : " + channel + " , " + message.toString())

      }
      override def errorCallback(channel: String, error: PubnubError) {
        notifyUser("CHANNEL : " + channel + " , " + error.toString())
      }

    })
  }

  def pamAudit() {
    var channel = getStringFromConsole("Channel", true)
    var auth_key = getStringFromConsole("Auth Key", true)

    var cb = new Callback() {

      override def successCallback(channel: String, message: Object) {
        notifyUser("CHANNEL : " + channel + " , " + message.toString())

      }

      override def errorCallback(channel: String, error: PubnubError) {
        notifyUser("CHANNEL : " + channel + " , " + error.toString())
      }
    }

    if (channel != null && channel.length() > 0) {
      if (auth_key != null && auth_key.length() != 0) {
        pubnub.pamAudit(channel, auth_key, cb)
      } else {
        pubnub.pamAudit(channel, cb)
      }
    } else {
      pubnub.pamAudit(cb)
    }
  }
  def startDemo() {

    reader = new Scanner(System.in)
    notifyUser("HINT:\tTo test Re-connect and catch-up")
    notifyUser("\tDisconnect your machine from network/internet and")
    notifyUser("\tre-connect your machine after sometime")

    SSL = getBooleanFromConsole("SSL")

    if (pub_key.length() == 0)
      pub_key = getStringFromConsole("Publish Key")

    if (sub_key.length() == 0)
      sub_key = getStringFromConsole("Subscribe Key")

    if (sec_key.length() == 0)
      sec_key = getStringFromConsole("Secret Key", true)

    if (cip_key.trim().length() == 0)
      cip_key = getStringFromConsole("Cipher Key", true)

    pubnub = new Pubnub(pub_key, sub_key, sec_key, cip_key, SSL)

    var channelName: String = null
    var command: Int = 0
    var loop: Boolean = true
    while (loop) {
      displayMenuOptions
      command = reader.nextInt()
      reader.nextLine()
      command match {

        case 0 =>
          displayMenuOptions()
        case 1 =>
          channelName = getStringFromConsole("Subscribe: Enter Channel name")
          subscribe(channelName)

          notifyUser("Subscribed to following channels: ")
        //notifyUser(PubnubUtil.joinString(pubnub.getSubscribedChannelsArray(), " : "))
        case 2 =>
          channelName = getStringFromConsole("Channel Name")
          publish(channelName)
        case 3 =>
          channelName = getStringFromConsole("Channel Name")
          presence(channelName)
        case 4 =>
          channelName = getStringFromConsole("Channel Name")
          history(channelName)
        case 5 =>
          channelName = getStringFromConsole("Channel Name")
          hereNow(channelName)
        case 6 =>
          channelName = getStringFromConsole("Channel Name")
          unsubscribe(channelName)
        case 7 =>
          channelName = getStringFromConsole("Channel Name")
          unsubscribePresence(channelName)
        case 8 =>
          pubnub.time(new Callback {
            override def successCallback(channel: String, message: Object) {
              notifyUser(message.toString())
            }
          })
        case 9 => loop = false
        case 10 =>
          disconnectAndResubscribe()
        case 11 =>
          notifyUser("Disconnect and Resubscribe with timetoken : Enter timetoken")
          var timetoken = getStringFromConsole("Timetoken")
          disconnectAndResubscribeWithTimetoken(timetoken)
        case 12 =>
          pubnub.setResumeOnReconnect(!pubnub.isResumeOnReconnect())
          notifyUser("RESUME ON RECONNECT : " + pubnub.isResumeOnReconnect())
        case 13 =>
          pubnub.setMaxRetries(getIntFromConsole("Max Retries"))
        case 14 =>
          pubnub.setRetryInterval(getIntFromConsole("Retry Interval"))
        case 15 =>
          pubnub.setSubscribeTimeout(getIntFromConsole("Subscribe Timeout ( in milliseconds) "))
        case 16 =>
          pubnub.setNonSubscribeTimeout(getIntFromConsole("Non Subscribe Timeout ( in milliseconds) "))
        case 17 =>
          notifyUser("Set/Unset Auth Key: Enter blank for unsetting key")
          var authKey = getStringFromConsole("Auth Key")
          pubnub.setAuthKey(authKey)
        case 18 =>
          pamGrant()
        case 19 =>
          pamRevoke()
        case 20 =>
          pamAudit()
        case 21 =>
          pubnub.setOrigin(getStringFromConsole("Origin"))
        case 22 =>
          pubnub.setDomain(getStringFromConsole("Domain"))
        case 23 =>
          pubnub.setCacheBusting(true)
        case 24 =>
          pubnub.setCacheBusting(false)
        case _ =>
          notifyUser("Invalid Input")
      }
    }
    notifyUser("Exiting")
    pubnub.shutdown()
  }

  def displayMenuOptions() {
    println("ENTER 1  FOR Subscribe "
      + "(Currently subscribed to "
      + pubnub.getCurrentlySubscribedChannelNames() + ")")
    println("ENTER 2  FOR Publish")
    println("ENTER 3  FOR Presence")
    println("ENTER 4  FOR History")
    println("ENTER 5  FOR Here_Now")
    println("ENTER 6  FOR Unsubscribe")
    println("ENTER 7  FOR Presence-Unsubscribe")
    println("ENTER 8  FOR Time")
    println("ENTER 9  FOR EXIT OR QUIT")
    println("ENTER 10 FOR Disconnect-And-Resubscribe")
    println("ENTER 11 FOR Disconnect-And-Resubscribe with timetoken")
    println("ENTER 12 FOR Toggle Resume On Reconnect ( current: " + pubnub.getResumeOnReconnect() + " )")
    println("ENTER 13 FOR Setting MAX Retries ( current: " + pubnub.getMaxRetries() + " )")
    println("ENTER 14 FOR Setting Retry Interval ( current: " + pubnub.getRetryInterval() + " milliseconds )")
    println("ENTER 15 FOR Setting Subscribe Timeout ( current: " + pubnub.getSubscribeTimeout() + " milliseconds )")
    println("ENTER 16 FOR Setting Non Subscribe Timeout ( current: " + pubnub.getNonSubscribeTimeout() + " milliseconds )")
    println("ENTER 17 FOR Setting/Unsetting auth key ( current: " + pubnub.getAuthKey() + " )")
    println("ENTER 18 FOR PAM grant")
    println("ENTER 19 FOR PAM revoke")
    println("ENTER 20 FOR PAM Audit")
    println("ENTER 21 FOR setting origin ( current: " + pubnub.getOrigin() + " )")
    println("ENTER 22 FOR setting domain ( current: "+ pubnub.getDomain() + " )")
    println("ENTER 23 FOR enabling Cache Busting  ( current: " + pubnub.getCacheBusting() + " )")
    println("ENTER 24 FOR disabling Cache Busting ( current: " + pubnub.getCacheBusting() + " )")
    println("\nENTER 0 to display this menu")
  }

  def main(args: Array[String]) {

    if (args.length == 4) {
        pub_key = args(0)
        sub_key = args(1)
        sec_key = args(2)
        cip_key = args(3)
    }
    startDemo
    pubnub.shutdown()
  }
}

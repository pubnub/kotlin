##### VIDEO - ANDROID + PUBNUB
[http://www.youtube.com/watch?v=pkxUYYhwb04](http://www.youtube.com/watch?v=pkxUYYhwb04)

##### YOU MUST HAVE A PUBNUB ACCOUNT TO USE THE API.

http://www.pubnub.com/account

## PubNub 3.4 Real-time Cloud Push API - ANDROID

PubNub is a Massively Scalable Real-time Service for Web and Mobile Games.
This is a cloud-based service for broadcasting Real-time messages
to thousands of web and mobile clients simultaneously.

The PubNub Android client provides a superior alternative to C2DM for broadcasting messages to entire user base.
C2DM is hard to implement, and it imposes artificial limits for you to reach your users quickly.  
C2DM is not recommended as it is a "broadcast" mechanism according to Google.  C2DM is Slow, and limited to 1 message at a time.
Use PubNub Instead!

##### PubNub Android Sample App

This is a full android sample app with the ability to Subscribe and UnSubscribe from PubNub channels. 
By UnSubscribing from channels, you can save resources and ultimately save billing costs.

Checkout the example app for examples on how to use the API! 
It can be found at in the 3.4/examples/PubnubExample directory.

##Durability - Reconnecting / Resuming when a connection is lost or changed
As the mobile device loses a connection, changes IP, etc, any current in-process subscribe operations can be forced
to abort, then reconnect given that a change has occured in the network environment. This is done by calling the
**disconnectAndResubscribe()** method on the PubNub instance.

The demo application has examples of setting up a broadcast receiver for the **ConnectivityManager.CONNECTIVITY_ACTION**.
The **onReceive()** method is where it checks for certain (or any) network change conditions, and 
calls **disconnectAndResubscribe()** as needed.

This logic has been purposely left out of the core and documented in the example application, so that if you wish to 
add additional tests for monitoring of a zombied subscribe connection, it is very to add your custom logic within this
given receiver template.

When **disconnectAndResubscribe()** is called, either via the broadcast receiver or otherwise, it will retry maxRetries times
to reconnect ever retryInterval seconds.  You can set these variables with the **setRetryInterval** and **setMaxRetries** 
methods.

###Init
```java
Pubnub pubnub = new Pubnub(
    "demo",  // PUBLISH_KEY   (Optional, supply "" to disable)
    "demo",  // SUBSCRIBE_KEY (Required)
    "",      // SECRET_KEY    (Optional, supply "" to disable)
    "",      // CIPHER_KEY    (Optional, supply "" to disable)
    false    // SSL_ON?
);
```

###Publish
```java

// Setup the argument Hash object to set the message and channel name

Hashtable args = new Hashtable(2);

String message = "Hello PubNub!";
String message = "hello_world";

args.put("message", message);
args.put("channel", channel); 

// Publish It!

pubnub.publish(args, new Callback() {
    public void successCallback(String channel, Object message) {
        notifyUser(message.toString());
    }

    public void errorCallback(String channel, Object message) {
    notifyUser(channel + " : " + message.toString());
    }
});

```

###Subscribe and Presence
```java

// Regular Subscribe

Hashtable args = new Hashtable(1);

args.put("channel", channel);

pubnub.subscribe(args, new Callback() {
    public void connectCallback(String channel) {
        notifyUser("CONNECT on channel:" + channel);
    }

    public void disconnectCallback(String channel) {
        notifyUser("DISCONNECT on channel:" + channel);
    }
    
    public void reconnectCallback(String channel) {
        notifyUser("RECONNECT on channel:" + channel);
    }

    public void successCallback(String channel, Object message) {
        notifyUser(channel + " " + message.toString());
    }
    
    public void errorCallback(String channel, Object message) {
        notifyUser(channel + " " + message.toString());
    }
});

// Presence

pubnub.presence(channel, new Callback() {
    public void successCallback(String channel, Object message) {
        notifyUser(message.toString());
    }

    public void errorCallback(String channel, Object message) {
        notifyUser(channel + " : " + message.toString());
    }
});
                    
```

###History
```java
pubnub.detailedHistory(channel, 2, new Callback() {
    public void successCallback(String channel, Object message) {
        notifyUser(message.toString());
    }

    public void errorCallback(String channel, Object message) {
        notifyUser(channel + " : " + message.toString());
    }
});
```

###Unsubscribe
```java
String channel = "goodbye_world";
pubnub.unsubscribe(channel);
```

###Time
```java
    // Get server time
    double time = pubnub.time();
    System.out.println("Time : "+time);
```

###UUID
```java
    // Get UUID
    System.out.println("UUID : "+Pubnub.uuid());
```

###Here Now

```java
pubnub.hereNow(channel, new Callback() {
    public void successCallback(String channel, Object message) {
        notifyUser(message.toString());
    }

    public void errorCallback(String channel, Object message) {
        notifyUser(channel + " : " + message.toString());
    }
});
```



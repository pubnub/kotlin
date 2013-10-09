##### YOU MUST HAVE A PUBNUB ACCOUNT TO USE THE API.
##### http://www.pubnub.com/account

## PubNub 3.5 Real-time Cloud Push API - JAVA

www.pubnub.com - PubNub Real-time Push Service in the Cloud. 

Please reference the demo app in jars/PubnubDemoConsole.jar for an example of using PubNub
Asyncronously in your applications!

Additional Java docs are available at [doc/index.html](doc/index.html)

###To browse source
Checkout [srcPubnubApi](/java/srcPubnubApi)

###To setup with your IDE:
[Check out the Eclipse video walkthrough here!](https://vimeo.com/69587309)

1. Add new project with java/ as the root
2. Set the new project source as java/srcPubnubApi
3. Add java/Pubnub-Standardedition-3.5.jar and 3.5/libs/*.jar as project libaries
4. You should be able to run PubnubDemoConsole and PubnubExample via your IDE

###To build from the CL, run:
```
$ ant clean build
$ ant
```

###To test from the CL, run:
```
$ ant test
```

###To be really cool and clean, build, and test at once!
```
$ ant clean build test
```

[A quick video demo of building with ant from the command line can be viewed here](https://vimeo.com/76488749).
###Configuring Logging

[Check out the logging video walkthrough here!](https://vimeo.com/71309975)

Pubnub implements swappable logging using SLF4J, which allows you to switch different logging frameworks easily. All the logging calls using SLF4J API will be delegated to the underlying logging framework.

Before you enable logging, you need to create a "debug-build" version of the Pubnub-StandardEdition.jar.

####To create a debug-build

1. Go to Terminal on Mac/Linux or command-line on Windows
2. Change directory to the Java folder where you have cloned the git java repo of pubnub (https://github.com/pubnub/java.git)
3. Type "ant debug-build"
4. The debug version of Pubnub-StandardEdition jar file (e.g Pubnub-StandardEdition-3.x.x.jar) will be created.
5. Add this jar as a reference in your project.

To implement logging using log4j you need to add the following references to the project. Using log4j you can log to console or a file or both.

1. slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API
2. slf4j-log4j jar file (e.g. slf4j-log4j-1.7.5.jar or a latest version) which acts as a bridge between slf4j and log4j
3. log4j jar file (log4j-1.2.17.jar or a latest version), which provides the underlying logging framework
4. Along with these references you need to add the log4j.properties file in the CLASSPATH

For example, in your log4j.properties file, configure it to write the logs to a log file:
```java
# Root logger option
log4j.rootLogger=ALL, FILE
 
# Direct log messages to a log file
log4j.appender.FILE =org.apache.log4j.FileAppender
log4j.appender.FILE.File=/Users/rajat/Projects/eclipsews/log4jloging.log
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
```

More info can be found here http://www.tutorialspoint.com/log4j/log4j_configuration.htm


ii) To implement logging using simple slf4j you need to add the following references to the project
- slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API
- slf4j-simple jar file (e.g. slf4j-simple-1.7.5.jar or a latest version) which provides the underlying logging framework
- Along with these references you need to add the simplelogger.properties file in the CLASSPATH

iii) To implement logging using logback-classic you need to add the following references to the project
- slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API
- logback-classic and logback-core jar files which provide the underlying logging framework

iv) To implement logging using java.util.logging you need to add the following references to the project
- slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API
- slf4j-jdk14 jar file which acts as a bridge between slf4j and java
- JVM runtime provides the underlying logging framework

v) To implement logging using commons-logging you need to add the following references to the project
- slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API
- slf4j-jcl jar file which acts as a bridge between slf4j and common-logging
- common-logging.jar file which acts as an abstraction layer
- The underlying logging framework is chosen dynamically by commons-logging

vi) To implement no logging you have two options

Option 1:
- Add the reference of slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API and nothing else.

Option 2:
- Add the reference of slf4j-api jar file (e.g. slf4j-api-1.7.5.jar or a latest version) which is the SLF4J API.
- And add a reference to slf4j-nop jar file (e.g. slf4j-nop-1.7.5.jar or a latest version).


More info on SLF4J can be found at http://stackoverflow.com/questions/8737204/how-slf4j-works-no-log-getting-created

###To run the DemoConsole:
```
$ cd jars
$ java -jar PubnubDemoConsole.jar
```

###Heres an example of running PubnubDemoConsole:

```
$~/pubnub/java/java/jars$ java -jar PubnubDemoConsole.jar 
HINT:  To test Re-connect and catch-up
	Disconnect your machine from network/internet and
	re-connect your machine after sometime
Enable SSL ? Enter Y for Yes, else N
N

SSL not enabled
Enter cipher key for encryption feature
If you don't want to avail at this time, press ENTER

No Cipher key provided
ENTER 1  FOR Subscribe (Currently subscribed to no channels.)
ENTER 2  FOR Publish
ENTER 3  FOR Presence
ENTER 4  FOR Detailed History
ENTER 5  FOR Here_Now
ENTER 6  FOR Unsubscribe
ENTER 7  FOR Presence-Unsubscribe
ENTER 8  FOR Time
ENTER 9  FOR EXIT OR QUIT
ENTER 10 FOR Disconnect-And-Resubscribe
ENTER 11 FOR Toggle Resume On Reconnect

ENTER 0 to display this menu
1
Subscribe: Enter Channel name
hello_world
Subscribed to following channels: 
hello_world
ENTER 1  FOR Subscribe (Currently subscribed to hello_world)
ENTER 2  FOR Publish
ENTER 3  FOR Presence
ENTER 4  FOR Detailed History
ENTER 5  FOR Here_Now
ENTER 6  FOR Unsubscribe
ENTER 7  FOR Presence-Unsubscribe
ENTER 8  FOR Time
ENTER 9  FOR EXIT OR QUIT
ENTER 10 FOR Disconnect-And-Resubscribe
ENTER 11 FOR Toggle Resume On Reconnect

ENTER 0 to display this menu

SUBSCRIBE : CONNECT on channel:hello_world
1
Subscribe: Enter Channel name
my_channel
Subscribed to following channels: 
hello_world : my_channel
ENTER 1  FOR Subscribe (Currently subscribed to hello_world,my_channel)
ENTER 2  FOR Publish
ENTER 3  FOR Presence
ENTER 4  FOR Detailed History
ENTER 5  FOR Here_Now
ENTER 6  FOR Unsubscribe
ENTER 7  FOR Presence-Unsubscribe
ENTER 8  FOR Time
ENTER 9  FOR EXIT OR QUIT
ENTER 10 FOR Disconnect-And-Resubscribe
ENTER 11 FOR Toggle Resume On Reconnect

ENTER 0 to display this menu
SUBSCRIBE : CONNECT on channel:my_channel
2
Publish: Enter Channel name
hello_world
Enter the message for publish. To exit loop enter QUIT
"hello, world!"
PUBLISH : [1,"Sent","13597534956712683"]
SUBSCRIBE : hello_world : class java.lang.String : hello, world!
SUBSCRIBE : hello_world : class java.lang.String : 3ZoxkVMB97lf09jpFU9gtw==
SUBSCRIBE : hello_world : class java.lang.String : 3ZoxkVMB97lf09jpFU9gtw==
```
[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/pubnub/java/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

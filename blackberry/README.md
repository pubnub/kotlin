##### YOU MUST HAVE A PUBNUB ACCOUNT TO USE THE API.
##### http://www.pubnub.com/account

## PubNub 3.5 Real-time Cloud Push API - BlackBerry

www.pubnub.com - PubNub Real-time Push Service in the Cloud. 

Please reference the demo app source code for examples of supercharging your Blackberry app with PubNub!

## Video Walkthrough
Checkout the [video walkthrough](https://vimeo.com/70973189) before proceeding to get an idea of how it works.

## Hello PubNub Demo App HOWTO

Follow this HOWTO step-by-step to setup the demo PubNub app for Blackberry from within Eclipse.

a. Open the Eclipse (Blackberry Java Plugin)

b. Goto File -> New -> Blackberry Project

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/1.png)

c. Enter a project name ( for this example, we'll use PubnubExample ) and click Next

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/2.png)

Your screen should look similar to:

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/3.png)

d. Click "Link additional Source"

e. Browse to and select ```java/blackberry/3.4/examples/PubnubExample/src``` and enter Folder name as src1

Your screen should look similar to:

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/4.png)

f. Click "Finish"

g. Select the Libraries tab

h. Click "Add External JARs"

i. Select ```bouncycastle.jar``` and ```Pubnub-Microedition-3.4.jar``` from ```pubnub-api/blackberry/3.4/libs```

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/5.png)

Your screen should look similar to:

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/6.png)

j. Go to the "Order and Export" tab and check the boxes against **bouncycastle.jar** and **Pubnub-Microedition-3.4.jar**.

k. Click "Finish"

Your screen should look similar to:

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/7.png)

and the "Project View" should look like this when "src1" and "Referenced Libraries" are expanded:

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/8.png)

l. Create a Run Configuration by clicking Run -> Run Configurations from the menu

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/9.png)

m. Select **Blackberry Simulator**, right-click, and select **New**

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/10.png)

You will now have access to some new tabs.

n. Click the "Projects" tab

o. Check the box against PubnubExample

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/11.png)

p. Click the "Simulator" tab, and confirm the **Launch Mobile Data System Connection Service (MDS-CS) with simulator** checkbox is selected

q. Change the configuration name to PubnubExample

r. Click "Apply"

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/12.png)

s. Right click PubnubExample in package explorer.

t. Select **Run As** -> **Blackberry Simulator**

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/14.png)

This will start MDS on command prompt and Blackberry Simulator

**NOTE**: MDS may need to be manually started by running run.bat located at ```Eclipse\plugins\net.rim.ejde.componentpack7.1.0_7.1.0.10\components\MDS```

u. The MDS console will display debug information as the simulator phone boots.

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/15.png)

v. Once the simulator has booted, scroll to and select **PubnubExample** (it will have the default “App Window” icon)

w. Run it, and select the menu button to see it run various PubNub for Blackberry API methods.

![File New](https://s3.amazonaws.com/pubnub-readme/blackberry/17.png)

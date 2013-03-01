package com.pubnub.examples.SubscribeAtBoot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class HelloWorldActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(this, PubnubService.class);
        startService(serviceIntent);

        Log.i("HelloWorldActivity", "PubNub Activity Started!");

    }
}
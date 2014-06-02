package com.example.AndroidGCMClientDemo;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.pubnub.api.Pubnub;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;

public class MyActivity extends Activity {

    Pubnub pubnub;
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    Context context;
    public static String SENDER_ID;
    public static String REG_ID;
    private static final String APP_VERSION = "3.6.1";

    String PUBLISH_KEY;
    String SUBSCRIBE_KEY;
    String SECRET_KEY;
    String ORIGIN;
    String CIPHER_KEY;
    Boolean SSL = false;


    static final String TAG = "Register Activity";

    private void notifyUser(Object message) {
        try {
            if (message instanceof JSONObject) {
                final JSONObject obj = (JSONObject) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj.toString(),
                                Toast.LENGTH_LONG).show();

                        Log.i("Received msg : ", String.valueOf(obj));
                    }
                });

            } else if (message instanceof String) {
                final String obj = (String) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj,
                                Toast.LENGTH_LONG).show();
                        Log.i("Received msg : ", obj.toString());
                    }
                });

            } else if (message instanceof JSONArray) {
                final JSONArray obj = (JSONArray) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), obj.toString(),
                                Toast.LENGTH_LONG).show();
                        Log.i("Received msg : ", obj.toString());
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(
                "PUBNUB_DEV_CONSOLE", Context.MODE_PRIVATE);
        init();
    }


    private void init() {


        PUBLISH_KEY = "demo-36";
        SUBSCRIBE_KEY = "demo-36";
        SECRET_KEY = "demo-36";
        SSL = false;
        ORIGIN = "dara20.devbuild";
        SENDER_ID = "506053237730";

        pubnub = new Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY,
                SECRET_KEY,
                CIPHER_KEY,
                SSL
        );
        pubnub.setCacheBusting(false);
        pubnub.setOrigin(ORIGIN);

        gcmRegister();
        gcmAddChannel("myGCMChannel");

        //gcmRemoveChannel("myGCMChannel");
        //gcmRemoveAllChannels();


    }

    private void gcmRemoveAllChannels() {
        if (TextUtils.isEmpty(REG_ID)) {
            Toast.makeText(getApplicationContext(),
                    "GCM Registration id not set. Register to GCM and try again.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        pubnub.removeAllPushNotificationsForDeviceRegistrationId(REG_ID, new Callback() {
            @Override
            public void successCallback(String channel,
                                        Object message) {
                notifyUser("GCM REMOVE ALL : " + message);
            }

            @Override
            public void errorCallback(String channel,
                                      PubnubError error) {
                notifyUser("GCM REMOVE ALL : " + error);
            }
        });

    }

    private void gcmRemoveChannel(String channel) {
        pubnub.disablePushNotificationsOnChannel(channel, REG_ID, new Callback() {
            @Override
            public void successCallback(String channel,
                                        Object message) {
                notifyUser("GCM REMOVE : " + message);
            }

            @Override
            public void errorCallback(String channel,
                                      PubnubError error) {
                notifyUser("GCM REMOVE : " + error);
            }
        });

    }

    private void gcmAddChannel(String channel) {

        pubnub.enablePushNotificationsOnChannel(channel, REG_ID, new Callback() {
            @Override
            public void successCallback(String channel,
                                        Object message) {
                notifyUser("GCM ADD : " + message);
            }

            @Override
            public void errorCallback(String channel,
                                      PubnubError error) {
                notifyUser("GCM ADD : " + error);
            }
        });
    }

    private void gcmUnregister() {
        //TODO add unregister code
    }


    private String gcmRegister() {

        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(this);

        registerInBackground();

        Log.d("RegisterActivity",
                "registerGCM - successfully registered with GCM server - regId: "
                        + REG_ID);
        return REG_ID;
    }


    private void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    REG_ID = gcm.register(SENDER_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + REG_ID);
                    msg = "Device registered, registration ID=" + REG_ID;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();
            }
        }.execute(null, null, null);
    }
}

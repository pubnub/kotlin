package com.pubnub.examples.pubnubExample;

import java.util.Hashtable;

import android.view.View;
import android.widget.LinearLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class MainActivity extends Activity {
    /*
     * //Noise Test Pubnub pubnub = new
     * Pubnub("pub-c-87f96934-8c44-4f8d-a35f-deaa2753f083",
     * "sub-c-3a693cf8-7401-11e2-8b02-12313f022c90", "", false); String channel
     * = "noise"; String channel = "a";
     */

    Pubnub pubnub = new Pubnub("demo", "demo", "", false);

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

        setContentView(R.layout.usage);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                pubnub.disconnectAndResubscribe();

            }

        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        LinearLayout clicklayout = (LinearLayout) findViewById(R.id.clicklayout);
        clicklayout.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                openOptionsMenu();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

        case R.id.option1:
            subscribe();
            return true;

        case R.id.option2:
            publish();
            return true;

        case R.id.option3:
            presence();
            return true;

        case R.id.option4:
            history();
            return true;

        case R.id.option5:
            hereNow();
            return true;

        case R.id.option6:
            unsubscribe();
            return true;

        case R.id.option7:
            presenceUnsubscribe();
            return true;

        case R.id.option8:
            time();
            return true;

        case R.id.option9:
            disconnectAndResubscribe();
            return true;

        case R.id.option10:
            disconnectAndResubscribeWithTimetoken();
            return true;

        case R.id.option11:
            toggleResumeOnReconnect();
            return true;

        case R.id.option12:
            setMaxRetries();
            return true;

        case R.id.option13:
            setRetryInterval();
            return true;

        case R.id.option14:
            setSubscribeTimeout();
            return true;

        case R.id.option15:
            setNonSubscribeTimeout();
            return true;

        case R.id.option16:
            setWindowInterval();
            return true;
        case R.id.option17:
            setOrigin();
            return true;
        case R.id.option18:
            setDomain();
            return true;
        case R.id.option19:
            toggleCacheBusting();
            return true;
        case R.id.option20:
            setHeartbeat();
            return true;
        case R.id.option21:
            setUUID();
            return true;
        case R.id.option22:
            setState();
            return true;
        case R.id.option23:
            setHeartbeatInterval();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void setHeartbeatInterval() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Presence Heartbeat Interval");
        builder.setMessage("Enter heartbeat value in seconds");
        final EditText edTimeout = new EditText(this);
        edTimeout.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edTimeout);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setHeartbeatInterval(Integer.parseInt(edTimeout.getText().toString()));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
    
    private void setHeartbeat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Presence Heartbeat");
        builder.setMessage("Enter heartbeat value in seconds");
        final EditText edTimeout = new EditText(this);
        edTimeout.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edTimeout);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setHeartbeat(Integer.parseInt(edTimeout.getText().toString()));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void toggleCacheBusting() {
        pubnub.setCacheBusting(pubnub.getCacheBusting() ? false : true);
        notifyUser("CACHE BUSTING : " + pubnub.getCacheBusting());

    }

    private void setOrigin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Origin");
        builder.setMessage("Enter Origin");
        final EditText edTimetoken = new EditText(this);
        builder.setView(edTimetoken);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setOrigin(
                    edTimetoken.getText().toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setDomain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Domain");
        builder.setMessage("Enter Domain");
        final EditText edTimetoken = new EditText(this);
        builder.setView(edTimetoken);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setDomain(
                    edTimetoken.getText().toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setUUID() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set UUID");
        builder.setMessage("Enter UUID");
        final EditText edTimetoken = new EditText(this);
        builder.setView(edTimetoken);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setUUID(
                    edTimetoken.getText().toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setNonSubscribeTimeout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Non Subscribe Timeout");
        builder.setMessage("Enter timeout value in milliseconds");
        final EditText edTimeout = new EditText(this);
        edTimeout.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edTimeout);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setNonSubscribeTimeout(Integer
                                              .parseInt(edTimeout.getText().toString()));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void subscribe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subscribe");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Subscribe",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String channel = input.getText().toString();

                try {
                    pubnub.subscribe(channel, new Callback() {
                        @Override
                        public void connectCallback(String channel,
                        Object message) {
                            notifyUser("SUBSCRIBE : CONNECT on channel:"
                                       + channel
                                       + " : "
                                       + message.getClass()
                                       + " : "
                                       + message.toString());
                        }
                        @Override
                        public void disconnectCallback(String channel,
                        Object message) {
                            notifyUser("SUBSCRIBE : DISCONNECT on channel:"
                                       + channel
                                       + " : "
                                       + message.getClass()
                                       + " : "
                                       + message.toString());
                        }

                        @Override
                        public void reconnectCallback(String channel,
                        Object message) {
                            notifyUser("SUBSCRIBE : RECONNECT on channel:"
                                       + channel
                                       + " : "
                                       + message.getClass()
                                       + " : "
                                       + message.toString());
                        }
                        @Override
                        public void successCallback(String channel,
                        Object message) {
                            notifyUser("SUBSCRIBE : " + channel + " : "
                                       + message.getClass() + " : "
                                       + message.toString());
                        }
                        @Override
                        public void errorCallback(String channel,
                        PubnubError error) {
                            notifyUser("SUBSCRIBE : ERROR on channel "
                                       + channel + " : "
                                       + error.toString());
                        }
                    });

                } catch (Exception e) {

                }
            }

        });
        AlertDialog alert = builder.create();
        alert.show();

    }
    
    private void _state(final String channel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set STATE");
        builder.setMessage("Enter state (JSON Object)");
        final EditText etMessage = new EditText(this);
        builder.setView(etMessage);
        builder.setPositiveButton("Submit",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Callback setMetaDataCallback = new Callback() {
                    @Override
                    public void successCallback(String channel,
                    Object message) {
                        notifyUser("SET STATE : " + message);
                    }
                    @Override
                    public void errorCallback(String channel,
                    PubnubError error) {
                        notifyUser("SET STATE : " + error);
                    }
                };

                String message = etMessage.getText().toString();
                JSONObject js = null;
                try {
                    js = new JSONObject(message);
                    pubnub.setState(channel, pubnub.getUUID(), js, setMetaDataCallback);
                    return;
                } catch (Exception e) {}

            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void _publish(final String channel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publish");
        builder.setMessage("Enter message");
        final EditText etMessage = new EditText(this);
        builder.setView(etMessage);
        builder.setPositiveButton("Publish",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Callback publishCallback = new Callback() {
                    @Override
                    public void successCallback(String channel,
                    Object message) {
                        notifyUser("PUBLISH : " + message);
                    }
                    @Override
                    public void errorCallback(String channel,
                    PubnubError error) {
                        notifyUser("PUBLISH : " + error);
                    }
                };

                String message = etMessage.getText().toString();

                try {
                    Integer i = Integer.parseInt(message);
                    pubnub.publish(channel, i, publishCallback);
                    return;
                } catch (Exception e) {}

                try {
                    Double d = Double.parseDouble(message);
                    pubnub.publish(channel, d, publishCallback);
                    return;
                } catch (Exception e) {}


                try {
                    JSONArray js = new JSONArray(message);
                    pubnub.publish(channel, js, publishCallback);
                    return;
                } catch (Exception e) {}

                try {
                    JSONObject js = new JSONObject(message);
                    pubnub.publish(channel, js, publishCallback);
                    return;
                } catch (Exception e) {}

                pubnub.publish(channel, message, publishCallback);
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void publish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Publish ");
        builder.setMessage("Enter channel name");
        final EditText etChannel = new EditText(this);
        builder.setView(etChannel);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                _publish(etChannel.getText().toString());
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void setState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set State ");
        builder.setMessage("Enter channel name");
        final EditText etChannel = new EditText(this);
        builder.setView(etChannel);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                _state(etChannel.getText().toString());
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void presence() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Presence");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Subscribe For Presence",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String channel = input.getText().toString();

                try {
                    pubnub.presence(channel, new Callback() {
                        @Override
                        public void successCallback(String channel,
                        Object message) {
                            notifyUser("PRESENCE : " + channel + " : "
                                       + message.getClass() + " : "
                                       + message.toString());
                        }
                        @Override
                        public void errorCallback(String channel,
                        PubnubError error) {
                            notifyUser("PRESENCE : ERROR on channel "
                                       + channel + " : "
                                       + error.toString());
                        }
                    });

                } catch (Exception e) {

                }
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void history() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("History");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Get history",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String channel = input.getText().toString();
                pubnub.history(channel, 2, new Callback() {
                    @Override
                    public void successCallback(String channel,
                    Object message) {
                        notifyUser("HISTORY : " + message);
                    }
                    @Override
                    public void errorCallback(String channel,
                    PubnubError error) {
                        notifyUser("HISTORY : " + error);
                    }
                });
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void hereNow() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Here Now");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Get Here Now",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String channel = input.getText().toString();
                pubnub.hereNow(channel, new Callback() {
                    @Override
                    public void successCallback(String channel,
                    Object message) {
                        notifyUser("HERE NOW : " + message);
                    }
                    @Override
                    public void errorCallback(String channel,
                    PubnubError error) {
                        notifyUser("HERE NOW : " + error);
                    }
                });
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void unsubscribe() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsubscribe");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Unsubscribe",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String channel = input.getText().toString();
                pubnub.unsubscribe(channel);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void presenceUnsubscribe() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsubscribe Presence");
        builder.setMessage("Enter channel name");
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setPositiveButton("Unsubscribe Presence",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String channel = input.getText().toString();
                pubnub.unsubscribePresence(channel);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void time() {
        pubnub.time(new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("TIME : " + message);
            }
            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("TIME : " + error);
            }
        });

    }

    private void disconnectAndResubscribe() {
        pubnub.disconnectAndResubscribe();

    }

    private void setSubscribeTimeout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Subscribe Timeout");
        builder.setMessage("Enter timeout value in milliseconds");
        final EditText edTimeout = new EditText(this);
        edTimeout.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edTimeout);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setSubscribeTimeout(Integer.parseInt(edTimeout
                                           .getText().toString()));
            }

        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void setRetryInterval() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Retry Interval");
        builder.setMessage("Enter retry interval in milliseconds");
        final EditText edInterval = new EditText(this);
        edInterval.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edInterval);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setRetryInterval(Integer.parseInt(edInterval
                                        .getText().toString()));
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setWindowInterval() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Window Interval");
        builder.setMessage("Enter Window interval in milliseconds");
        final EditText edInterval = new EditText(this);
        edInterval.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edInterval);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setWindowInterval(Integer.parseInt(edInterval
                                        .getText().toString()));
            }

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void toggleResumeOnReconnect() {
        pubnub.setResumeOnReconnect(pubnub.isResumeOnReconnect() ? false : true);
        notifyUser("RESUME ON RECONNECT : " + pubnub.isResumeOnReconnect());
    }

    private void setMaxRetries() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Max Retries");
        builder.setMessage("Enter Max Retries");
        final EditText edRetries = new EditText(this);
        edRetries.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(edRetries);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.setMaxRetries(Integer.parseInt(edRetries
                                                      .getText().toString()));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void disconnectAndResubscribeWithTimetoken() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disconnect and Resubscribe with timetoken");
        builder.setMessage("Enter Timetoken");
        final EditText edTimetoken = new EditText(this);
        builder.setView(edTimetoken);
        builder.setPositiveButton("Done",
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pubnub.disconnectAndResubscribeWithTimetoken(
                    edTimetoken.getText().toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

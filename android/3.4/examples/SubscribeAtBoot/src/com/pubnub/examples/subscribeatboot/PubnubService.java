package com.pubnub.examples.subscribeatboot;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PubnubService extends Service {

	String channel = "hello_world";
	Pubnub pubnub = new Pubnub("demo", "demo", false);
	private int msgid = 0;

	private void showNotification(String s) {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, s,
				System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, PubnubService.class), 0);
		notification.setLatestEventInfo(this, getText(R.string.app_name), s,
				contentIntent);
		notificationManager.notify(R.string.app_name, notification);
	}

	private void notifyUser(Object message) {

		try {
			if (message instanceof JSONObject) {
				final JSONObject obj = (JSONObject) message;

				showNotification(String.valueOf(obj));
				Log.i("Received msg : ", String.valueOf(obj));

			} else if (message instanceof String) {
				final String obj = (String) message;

				showNotification(String.valueOf(obj));
				Log.i("Received msg : ", obj.toString());

			} else if (message instanceof JSONArray) {
				final JSONArray obj = (JSONArray) message;

				showNotification(String.valueOf(obj));
				Log.i("Received msg : ", obj.toString());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "PubnubService created...", Toast.LENGTH_LONG).show();
		Log.i("PUBNUB", "PubnubService created...");
		try {
			pubnub.subscribe(new String[] { channel }, new Callback() {
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
		} catch (PubnubException e) {

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "PubnubService destroyed...", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

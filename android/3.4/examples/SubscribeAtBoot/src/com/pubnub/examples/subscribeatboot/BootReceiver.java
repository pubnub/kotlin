package com.pubnub.examples.subscribeatboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent intent = new Intent(context, PubnubService.class);
		context.startService(intent);
		Log.i("PubnubService", "started");
	}

}

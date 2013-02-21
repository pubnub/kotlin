package com.pubnub.api;

import android.util.Log;

public class Logger extends AbstractLogger {
	Class _class;
	public Logger(Class _class) {
		super();
		this._class = _class;
	}

	public void debug(String s) {
		Log.d(_class.getName(), s);
	}
	public void verbose(String s) {
		Log.v(_class.getName(), s);
	}
	public void error(String s) {
		Log.e(_class.getName(), s);
	}
	public void info(String s) {
		Log.i(_class.getName(), s );
	}
}

package com.pubnub.api;

import com.codename1.io.Log;

class Cn1Logger {

    private Log log;

    public Cn1Logger(Class c) {
        Log log = Log.getInstance();
    }

    public void debug(String s) {
        log.p(s, Log.DEBUG);
    }

    public void trace(String s) {
        log.p(s,Log.INFO);
    }

    public void error(String s) {
        log.p(s,Log.ERROR);
    }

    public void info(String s) {
        log.p(s,Log.INFO);
    }
}

class Logger extends AbstractLogger {

    private Class _class;
    private Cn1Logger log;

    public Logger(Class _class) {
        this._class = _class;
        this.log = new Cn1Logger(this._class);
    }

    @Override
    protected void nativeDebug(String s) {
        log.debug(s);
    }

    @Override
    protected void nativeVerbose(String s) {
        log.trace(s);
    }

    @Override
    protected void nativeError(String s) {
        log.error(s);
    }

    @Override
    protected void nativeInfo(String s) {
        log.info(s);
    }
}

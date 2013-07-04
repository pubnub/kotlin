package com.pubnub.api;

//import org.slf4j.LoggerFactory;

class Cn1Logger {

    public Cn1Logger(Class c) {

    }
    public void debug(String s) {

    }
    public void trace(String s) {

    }
    public void error(String s) {

    }
    public void info(String s) {

    }
}


class Logger extends AbstractLogger {
    private Class _class;
    private Cn1Logger log;
    public Logger(Class _class) {
        this._class = _class;
        this.log = new Cn1Logger(this._class);
        this.log = null;
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

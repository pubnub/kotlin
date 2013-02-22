package com.pubnub.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class Logger extends AbstractLogger {
    private Class _class;
    private Log log;

    public Logger(Class _class) {
        this._class = _class;
        log = LogFactory.getLog(this._class);
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

package com.pubnub.api;

import org.slf4j.LoggerFactory;


class Logger extends AbstractLogger {
    private Class _class;
    private org.slf4j.Logger log;

    public Logger(Class _class) {
        this._class = _class;
        this.log = LoggerFactory.getLogger(this._class);
    }

    @Override
    protected void nativeDebug(String s) {
        log.debug("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);

    }

    @Override
    protected void nativeVerbose(String s) {
        log.trace("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);
    }

    @Override
    protected void nativeError(String s) {
        log.error("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);

    }

    @Override
    protected void nativeInfo(String s) {
        log.info("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);

    }
}

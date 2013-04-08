package com.pubnub.api;



class Logger extends AbstractLogger {
    private Class _class;
    //private org.apache.log4j.Logger log;

    public Logger(Class _class) {
        this._class = _class;
        //this.log = org.apache.log4j.Logger.getLogger(this._class);
    }

    protected void nativeDebug(String s) {
        //log.debug("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);

    }

    protected void nativeVerbose(String s) {
        //log.trace("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);
    }

 
    protected void nativeError(String s) {
        //log.error("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);

    }

    protected void nativeInfo(String s) {
        //log.info("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().getId() + "  " + s);

    }
    
}

package com.pubnub.api;



class Logger extends AbstractLogger {
    private Class _class;

    public Logger(Class _class) {
        this._class = _class;
    }

    protected void nativeDebug(String s) {
        System.out.println("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().hashCode() + "  " + s);

    }

    protected void nativeVerbose(String s) {
        System.out.println("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().hashCode() + "  " + s);
    }


    protected void nativeError(String s) {
        System.out.println("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().hashCode() + "  " + s);

    }

    protected void nativeInfo(String s) {
        System.out.println("["+Thread.activeCount()+ "]  Thread ID : " + Thread.currentThread().hashCode() + "  " + s);

    }

}

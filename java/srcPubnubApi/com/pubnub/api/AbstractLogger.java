package com.pubnub.api;

abstract class AbstractLogger {

    private static boolean LOGGING = true;

    private static String VERSION = "";

    protected abstract void nativeDebug(String s);

    protected abstract void nativeVerbose(String s);

    protected abstract void nativeError(String s);

    protected abstract void nativeInfo(String s);

    private String prepareString(String s) {
        return  "[" + VERSION + "] : " + "[" + System.currentTimeMillis() + "] : " +
                "["+Thread.activeCount() +
                "]  Thread HashCode : " + Thread.currentThread().hashCode() +
                ",  Thread Name : " + Thread.currentThread().getName() +
                ",  " + s;
    }

    public void debug(String s) {
        if (LOGGING)
            nativeDebug(prepareString(s));
    }

    public void verbose(String s) {
        if (LOGGING)
            nativeVerbose(prepareString(s));
    }

    public void info(String s) {
        if (LOGGING)
            nativeInfo(prepareString(s));
    }

    public void error(String s) {
        if (LOGGING)
            nativeError(prepareString(s));
    }

}

package com.pubnub.api.models;

public class TokenBitmask {
    private TokenBitmask() {
    }

    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int MANAGE = 4;
    public static final int DELETE = 8;
    public static final int CREATE = 16;
}

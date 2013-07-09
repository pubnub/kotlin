package com.codename1.io;

public class PubnubCn1Response {
    private String response;
    private int responseStatusCode;
    public String getResponse() {
        return response;
    }
    public int getResponseStatusCode() {
        return responseStatusCode;
    }
    public PubnubCn1Response(int responseStatusCode, String response) {
        this.response = response;
        this.responseStatusCode = responseStatusCode;
    }
}


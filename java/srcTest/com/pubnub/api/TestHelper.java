package com.pubnub.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class TestHelper {
    static class SimpleCallback extends Callback {

        protected CountDownLatch latch;
        Object response;
        Boolean error;

        public SimpleCallback(CountDownLatch latch) {
            this.latch = latch;
        }
        public SimpleCallback() {}

        public Object getResponse() {
            return response;
        }

        public Boolean responseIsError() {
            return error;
        }

        @Override
        public void successCallback(String channel, Object message) {
            this.response = message;
            this.error = false;
            if (this.latch != null) {
                this.latch.countDown();
            }
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            this.error = true;
            if (this.latch != null) {
                this.latch.countDown();
            }
        }
    }


    static class PresenceCallback extends Callback {

        private String uuid;
        private String action;
        private CountDownLatch latch;

        public String getUUID() {
            return uuid;
        }

        public String getAction() {
            return action;
        }

        public PresenceCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        public PresenceCallback() {

        }

        @Override
        public void successCallback(String channel, Object message) {
            JSONObject resp = null;
            try {
                resp = new JSONObject(message.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            if (resp != null) {
                try {
                    uuid = (String) resp.get("uuid");
                    action = (String) resp.get("action");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (latch != null)
                latch.countDown();
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            if (latch != null)
                latch.countDown();
        }

    }
}

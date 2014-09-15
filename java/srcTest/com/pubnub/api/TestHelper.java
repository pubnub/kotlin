package com.pubnub.api;

import java.util.concurrent.CountDownLatch;

public class TestHelper {
    static class SimpleCallback extends Callback {

        private CountDownLatch latch;
        Object response;
        Boolean error;

        public SimpleCallback(CountDownLatch latch) {
            this.latch = latch;
        }

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
            this.latch.countDown();
        }


        @Override
        public void errorCallback(String channel, PubnubError error) {
            this.error = true;
            this.latch.countDown();
        }
    }
}

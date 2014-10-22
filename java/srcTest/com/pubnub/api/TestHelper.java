package com.pubnub.api;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

    static class SubscribeCallback extends SimpleCallback {
        public SubscribeCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void connectCallback(String channel, Object message) {
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

    public static void cleanup() throws InterruptedException, JSONException {
        Pubnub pubnub = new Pubnub("demo", "demo");
        pubnub.setOrigin("dara24.devbuild");
        pubnub.setCacheBusting(false);

        final CountDownLatch latch1 = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(1);
        final TestHelper.SimpleCallback cb1 = new TestHelper.SimpleCallback(latch1);
        final TestHelper.SimpleCallback cb2 = new TestHelper.SimpleCallback(latch2);

        pubnub.channelGroupListGroups(cb1);
        latch1.await(10, TimeUnit.SECONDS);

        JSONObject result = (JSONObject) cb1.getResponse();
        JSONArray groups = result.getJSONArray("groups");

        for (int i = 0; i < groups.length(); i++) {
            final String group = groups.getString(i);

            if (group.startsWith("jtest")) {
                CountDownLatch latch = new CountDownLatch(1);
                pubnub.channelGroupRemoveGroup(group, new TestHelper.SimpleCallback(latch) {
                    @Override
                    public void successCallback(String channel, Object message) {
                        System.out.println("Successfully removed group " + group);
                        super.successCallback(channel, message);
                    }
                });
                latch.await(10, TimeUnit.SECONDS);
            }
        }

        pubnub.channelGroupListNamespaces(cb2);
        latch2.await(10, TimeUnit.SECONDS);

        result = (JSONObject) cb2.getResponse();
        JSONArray namespaces = result.getJSONArray("namespaces");

        for (int i = 0; i < namespaces.length(); i++) {
            final String namespace = namespaces.getString(i);

            if (namespace.startsWith("jtest")) {
                CountDownLatch latch = new CountDownLatch(1);
                pubnub.channelGroupRemoveNamespace(namespace, new TestHelper.SimpleCallback(latch) {
                    @Override
                    public void successCallback(String channel, Object message) {
                        System.out.println("Successfully removed namespace " + namespace);
                        super.successCallback(channel, message);
                    }
                });
                latch.await(10, TimeUnit.SECONDS);
            }
        }
    }
}

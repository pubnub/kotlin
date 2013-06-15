package com.pubnub.api;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;






public class PubnubTest {

    class SubscribeCallback extends Callback {

        private CountDownLatch latch;

        private Object response;

        public SubscribeCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        public SubscribeCallback() {

        }


        public Object getResponse() {
            return response;
        }

        @Override
        public void successCallback(String channel, Object message) {
            response = message;
            if (latch != null) latch.countDown();
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            response = error;
            if (latch != null) latch.countDown();
        }
    }


    class PublishCallback extends Callback {

        private CountDownLatch latch;
        private int result = 0;

        public int getResult() {
            return result;
        }

        public PublishCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        public PublishCallback() {

        }

        public void successCallback(String channel, Object message) {
            JSONArray jsarr;
            try {
                jsarr = (JSONArray)message;
                result = (Integer) jsarr.get(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (latch != null) latch.countDown();
        }

        public void errorCallback(String channel, PubnubError error) {
            JSONArray jsarr;
            result = 0;
            if (latch != null) latch.countDown();
        }

    }

    class HereNowCallback extends Callback {

        private int occupancy;
        private String[] uuids;
        private CountDownLatch latch;

        public int getOccupancy() {
            return occupancy;
        }

        public HereNowCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        public HereNowCallback() {

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
                    occupancy = (Integer) resp.get("occupancy");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (latch != null) latch.countDown();
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            if (latch != null) latch.countDown();
        }

    }
    class PresenceCallback extends Callback {

        private String uuid;
        private CountDownLatch latch;

        public String getUUID() {
            return uuid;
        }

        public PresenceCallback(CountDownLatch latch) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (latch != null) latch.countDown();
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            if (latch != null) latch.countDown();
        }

    }

    class HistoryCallback extends Callback {

        private int count;
        private String[] uuids;
        private CountDownLatch latch;

        public int getCount() {
            return count;
        }

        public HistoryCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        public HistoryCallback() {

        }

        @Override
        public void successCallback(String channel, Object message) {
            JSONArray resp = null;
            try {
                resp = (new JSONArray(message.toString())).getJSONArray(0);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            if (resp != null) {
                count = resp.length();
            }
            if (latch != null) latch.countDown();
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            if (latch != null) latch.countDown();
        }

    }



    Pubnub pubnub = new Pubnub("demo", "demo");
    Pubnub pubnub_enc = new Pubnub("demo", "demo", "demo", "demo", false);
    String testSuccessMessage = "";


    @Test
    public void testPublishString() {
        String channel = "java-unittest-" + Math.random();
        final String sendMessage = "Test Message " + Math.random();

        final CountDownLatch latch = new CountDownLatch(2);

        final PublishCallback  pbCb = new PublishCallback(latch);
        SubscribeCallback sbCb = new SubscribeCallback(latch) {
            @Override
            public void connectCallback(String channel, Object message) {
                pubnub.publish(channel, sendMessage, pbCb);
            }
        };

        Hashtable args = new Hashtable();
        args.put("channel", channel);
        args.put("callback", sbCb);

        try {
            pubnub.subscribe(args);
        } catch (PubnubException e1) {

        }

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        assertEquals(1, pbCb.getResult());
        assertEquals(sendMessage,sbCb.getResponse());
    }

    @Test
    public void testPublishJSONArray() {
        String channel = "java-unittest-" + Math.random();
        final JSONArray sendMessage = new JSONArray().put(1).put("Test");

        final CountDownLatch latch = new CountDownLatch(2);

        final PublishCallback  pbCb = new PublishCallback(latch);
        SubscribeCallback sbCb = new SubscribeCallback(latch) {
            @Override
            public void connectCallback(String channel, Object message) {
                pubnub.publish(channel, sendMessage, pbCb);
            }
        };

        Hashtable args = new Hashtable();
        args.put("channel", channel);
        args.put("callback", sbCb);

        try {
            pubnub.subscribe(args);
        } catch (PubnubException e1) {

        }

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        assertEquals(1, pbCb.getResult());
        assertEquals(sendMessage.toString(), sbCb.getResponse().toString());
    }

    @Test
    public void testPublishJSONObject() {
        String channel = "java-unittest-" + Math.random();

        try {
            final JSONObject sendMessage;

            sendMessage = new JSONObject().put("1", "Test");

            final CountDownLatch latch = new CountDownLatch(2);

            final PublishCallback  pbCb = new PublishCallback(latch);
            SubscribeCallback sbCb = new SubscribeCallback(latch) {
                @Override
                public void connectCallback(String channel, Object message) {
                    pubnub.publish(channel, sendMessage, pbCb);
                }
            };

            Hashtable args = new Hashtable();
            args.put("channel", channel);
            args.put("callback", sbCb);

            pubnub.subscribe(args);

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(1, pbCb.getResult());
            assertEquals(sendMessage.toString(), sbCb.getResponse().toString());
        } catch (Exception e) {

        }
    }

    @Test
    public void testPublishStringWithEncryption() {
        String channel = "java-unittest-" + Math.random();
        final String sendMessage = "Test Message " + Math.random();

        final CountDownLatch latch = new CountDownLatch(2);

        final PublishCallback  pbCb = new PublishCallback(latch);
        SubscribeCallback sbCb = new SubscribeCallback(latch) {
            @Override
            public void connectCallback(String channel, Object message) {
                pubnub_enc.publish(channel, sendMessage, pbCb);
            }
        };

        Hashtable args = new Hashtable();
        args.put("channel", channel);
        args.put("callback", sbCb);

        try {
            pubnub_enc.subscribe(args);
        } catch (PubnubException e1) {

        }

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        assertEquals(1, pbCb.getResult());
        assertEquals(sendMessage,sbCb.getResponse());
    }

    @Test
    public void testPublishJSONArrayWithEncryption() {
        String channel = "java-unittest-" + Math.random();
        final JSONArray sendMessage = new JSONArray().put(1).put("Test");

        final CountDownLatch latch = new CountDownLatch(2);

        final PublishCallback  pbCb = new PublishCallback(latch);
        SubscribeCallback sbCb = new SubscribeCallback(latch) {
            @Override
            public void connectCallback(String channel, Object message) {
                pubnub_enc.publish(channel, sendMessage, pbCb);
            }
        };

        Hashtable args = new Hashtable();
        args.put("channel", channel);
        args.put("callback", sbCb);

        try {
            pubnub_enc.subscribe(args);
        } catch (PubnubException e1) {

        }

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }
        assertEquals(1, pbCb.getResult());
        assertEquals(sendMessage.toString(), sbCb.getResponse().toString());
    }

    @Test
    public void testPublishJSONObjectWithEncryption() {
        String channel = "java-unittest-" + Math.random();

        try {
            final JSONObject sendMessage;

            sendMessage = new JSONObject().put("1", "Test");

            final CountDownLatch latch = new CountDownLatch(2);

            final PublishCallback  pbCb = new PublishCallback(latch);
            SubscribeCallback sbCb = new SubscribeCallback(latch) {
                @Override
                public void connectCallback(String channel, Object message) {
                    pubnub_enc.publish(channel, sendMessage, pbCb);
                }
            };

            Hashtable args = new Hashtable();
            args.put("channel", channel);
            args.put("callback", sbCb);

            pubnub_enc.subscribe(args);

            latch.await(10, TimeUnit.SECONDS);
            assertEquals(1, pbCb.getResult());
            assertEquals(sendMessage.toString(), sbCb.getResponse().toString());
        } catch (Exception e) {

        }
    }



    @Test
    public void testHereNowOneUser() {
        String channel = "java-unittest-" + Math.random();

        try {
            final JSONObject sendMessage;

            sendMessage = new JSONObject().put("1", "Test");

            final CountDownLatch latch = new CountDownLatch(2);

            final HereNowCallback  hnCb = new HereNowCallback(latch);

            SubscribeCallback sbCb = new SubscribeCallback(latch) {
                @Override
                public void connectCallback(String channel, Object message) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                    pubnub.hereNow(channel, hnCb);
                    pubnub.unsubscribe(channel);
                    if (latch != null) latch.countDown();
                }
            };

            Hashtable args = new Hashtable();
            args.put("channel", channel);
            args.put("callback", sbCb);

            pubnub.subscribe(args);

            latch.await(10, TimeUnit.SECONDS);
            assertEquals(1, hnCb.getOccupancy());
        } catch (Exception e) {

        }
    }
    @Test
    public void testHistoryCountOne() {
        String channel = "java-unittest-" + Math.random();
        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch = new CountDownLatch(3);
        final int limit = 1;
        final HistoryCallback hCb = new HistoryCallback(latch);

        pubnub.publish(channel, sendMessage, new PublishCallback(latch){
            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, sendMessage, new PublishCallback(latch) {
                    @Override
                    public void successCallback(String channel, Object message) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pubnub.detailedHistory(channel, limit, hCb);
                        super.successCallback(channel, message);
                    }
                });
                super.successCallback(channel, message);
            }
        });
        try {
            latch.await(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(limit, hCb.getCount());
    }

    @Test
    public void testHistory() {
        String channel = "java-unittest-" + Math.random();
        final String sendMessage = "Test Message " + Math.random();
        final CountDownLatch latch = new CountDownLatch(3);

        final HistoryCallback hCb = new HistoryCallback(latch);

        pubnub.publish(channel, sendMessage, new PublishCallback(latch){
            @Override
            public void successCallback(String channel, Object message) {
                pubnub.publish(channel, sendMessage, new PublishCallback(latch) {
                    @Override
                    public void successCallback(String channel, Object message) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pubnub.detailedHistory(channel, 100,  hCb);
                        super.successCallback(channel, message);
                    }
                });
                super.successCallback(channel, message);
            }
        });
        try {
            latch.await(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(2, hCb.getCount());
    }


    @Test
    public void testPresence() {
        String channel = "java-unittest-" + Math.random();
        CountDownLatch latch = new CountDownLatch(2);

        PresenceCallback presenceCb = new PresenceCallback(latch);

        try {
            pubnub.presence(channel, presenceCb);

            Pubnub pubnub2 = new Pubnub("demo", "demo");

            Hashtable args = new Hashtable();
            args.put("channel", channel);
            args.put("callback", new SubscribeCallback(latch));

            pubnub2.subscribe(args);

            latch.await(10, TimeUnit.SECONDS);
            assertEquals(pubnub2.UUID, presenceCb.getUUID());
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testConnectionRestore() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(3);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(true);
            final Hashtable args = new Hashtable();
            //args.put("channels", new String[]{channel, "b"});
            args.put("channel", channel);
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.unsubscribe(channel);
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Hashtable args = new Hashtable();
                            args.put("channel", channel);
                            args.put("callback", subscribeCb);
                            try {
                                pubnub.subscribe(args);
                            } catch (PubnubException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertEquals(10, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    @Test
    public void testConnectionRestoreUnsubInSuccessCallback() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(5);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(true);
            final Hashtable args = new Hashtable();
            args.put("channel", channel);
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            pubnub.unsubscribe(channel);
                            pubnub.publish(channel, 20, new PublishCallback(latch){
                                public void successCallback(String channel, Object message) {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Hashtable args = new Hashtable();
                                    args.put("channel", channel);
                                    args.put("callback", subscribeCb);
                                    try {
                                        pubnub.subscribe(args);
                                    } catch (PubnubException e) {
                                        e.printStackTrace();
                                    }
                                    latch.countDown();
                                }
                            });
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertEquals(20, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testConnectionRestoreMultipleChannels() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(4);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(true);
            final Hashtable args = new Hashtable();
            args.put("channels", new String[]{channel, "b"});
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.unsubscribe(channel);
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Hashtable args = new Hashtable();
                            args.put("channel", channel);
                            args.put("callback", subscribeCb);
                            try {
                                pubnub.subscribe(args);
                            } catch (PubnubException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertEquals(10, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testConnectionRestoreMultipleChannelsUnsubInSuccessCallback() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(6);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(true);
            final Hashtable args = new Hashtable();
            args.put("channels", new String[]{channel, "b"});
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            pubnub.unsubscribe(channel);
                            pubnub.publish(channel, 20, new PublishCallback(latch){
                                public void successCallback(String channel, Object message) {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Hashtable args = new Hashtable();
                                    args.put("channel", channel);
                                    args.put("callback", subscribeCb);
                                    try {
                                        pubnub.subscribe(args);
                                    } catch (PubnubException e) {
                                        e.printStackTrace();
                                    }
                                    latch.countDown();
                                }
                            });
                            latch.countDown();

                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertEquals(20, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testConnectionRestoreFalse() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(3);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(false);
            final Hashtable args = new Hashtable();
            //args.put("channels", new String[]{channel, "b"});
            args.put("channel", channel);
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.unsubscribe(channel);
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Hashtable args = new Hashtable();
                            args.put("channel", channel);
                            args.put("callback", subscribeCb);
                            try {
                                pubnub.subscribe(args);
                            } catch (PubnubException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertNotEquals(10, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    @Test
    public void testConnectionRestoreFalseMultipleChannels() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(3);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(false);
            final Hashtable args = new Hashtable();
            args.put("channels", new String[]{channel, "b"});
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.unsubscribe(channel);
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            Hashtable args = new Hashtable();
                            args.put("channel", channel);
                            args.put("callback", subscribeCb);
                            try {
                                pubnub.subscribe(args);
                            } catch (PubnubException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }

            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertNotEquals(10, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testConnectionRestoreFalseUnsubInSuccessCallback() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(4);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(false);
            final Hashtable args = new Hashtable();
            args.put("channel", channel);
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            pubnub.unsubscribe(channel);
                            pubnub.publish(channel, 20, new PublishCallback(latch){
                                public void successCallback(String channel, Object message) {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Hashtable args = new Hashtable();
                                    args.put("channel", channel);
                                    args.put("callback", subscribeCb);
                                    try {
                                        pubnub.subscribe(args);
                                    } catch (PubnubException e) {
                                        e.printStackTrace();
                                    }
                                    latch.countDown();
                                }
                            });
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertNotEquals(20, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
    @Test
    public void testConnectionRestoreFalseMultipleChannelsUnsubInSuccessCallback() {
        String channel = "java-unittest-" + Math.random();
        final CountDownLatch latch = new CountDownLatch(4);

        final SubscribeCallback subscribeCb = new SubscribeCallback(latch);

        try {
            pubnub.setResumeOnReconnect(false);
            final Hashtable args = new Hashtable();
            args.put("channels", new String[]{channel, "b"});
            args.put("callback", new SubscribeCallback(latch) {
                public void connectCallback(String channel, Object message) {
                    pubnub.publish(channel, 10, new PublishCallback(latch){
                        public void successCallback(String channel, Object message) {
                            pubnub.unsubscribe(channel);
                            pubnub.publish(channel, 20, new PublishCallback(latch){
                                public void successCallback(String channel, Object message) {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e1) {
                                        // TODO Auto-generated catch block
                                        e1.printStackTrace();
                                    }
                                    Hashtable args = new Hashtable();
                                    args.put("channel", channel);
                                    args.put("callback", subscribeCb);
                                    try {
                                        pubnub.subscribe(args);
                                    } catch (PubnubException e) {
                                        e.printStackTrace();
                                    }
                                    latch.countDown();
                                }
                            });
                            latch.countDown();
                        }
                    });
                    latch.countDown();
                }
            });
            pubnub.subscribe(args);
            latch.await(30, TimeUnit.SECONDS);
            assertNotEquals(20, subscribeCb.getResponse());


        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}

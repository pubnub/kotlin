package com.pubnub.examples;

import java.util.Hashtable;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pubnub.api.*;
import static java.lang.System.out;

public class PubnubDemoConsole {

    Pubnub pubnub;
    String publish_key = "demo";
    String subscribe_key = "demo";
    String secret_key = "";
    String cipher_key = "";
    boolean SSL;
    Scanner reader;

    public PubnubDemoConsole(String publish_key, String subscribe_key, String secret_key,
            String cipher_key) {
        this.publish_key = publish_key;
        this.subscribe_key = subscribe_key;
        this.secret_key = secret_key;
        this.cipher_key = cipher_key;
    }

    public PubnubDemoConsole() {

    }

    private void notifyUser(Object message) {
        out.println(message.toString());
    }

    private void publish(String channel) {
        notifyUser("Enter the message for publish. To exit loop enter QUIT");
        String message = "";

        while (true) {
            Hashtable args = new Hashtable(2);
            message = reader.nextLine();
            if (message.equalsIgnoreCase("QUIT")) {
                break;
            }
            if (args.get("message") == null) {
                try {
                    Integer i = Integer.parseInt(message);
                    args.put("message", i);
                } catch (Exception e) {

                }
            }
            if (args.get("message") == null) {
                try {
                    Double d = Double.parseDouble(message);
                    args.put("message", d);
                } catch (Exception e) {

                }
            }
            if (args.get("message") == null) {
                try {
                    JSONArray js = new JSONArray(message);
                    args.put("message", js);
                } catch (Exception e) {

                }
            }
            if (args.get("message") == null) {
                try {
                    JSONObject js = new JSONObject(message);
                    args.put("message", js);
                } catch (Exception e) {

                }
            }
            if (args.get("message") == null) {
                args.put("message", message);
            }

            args.put("channel", channel); // Channel Name
            pubnub.publish(args, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    notifyUser("PUBLISH : " + message);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    notifyUser("PUBLISH : " + error);
                }
            });
        }

    }

    private void subscribe(final String channel) {
        Hashtable args = new Hashtable(6);
        args.put("channel", channel);

        try {
            pubnub.subscribe(args, new Callback() {

                @Override
                public void connectCallback(String channel, Object message) {
                    notifyUser("SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    notifyUser("SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                public void reconnectCallback(String channel, Object message) {
                    notifyUser("SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel, Object message) {
                    notifyUser("SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    notifyUser("SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }
            });

        } catch (Exception e) {
        }
    }

    private void presence(String channel) {
        try {
            pubnub.presence(channel, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    notifyUser("PRESENCE : " + message);
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    notifyUser("PRESENCE : " + error);
                }
            });
        } catch (PubnubException e) {

        }
    }

    private void detailedHistory(String channel) {
        pubnub.detailedHistory(channel, 2, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("DETAILED HISTORY : " + message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("DETAILED HISTORY : " + error);
            }
        });
    }

    private void hereNow(String channel) {
        pubnub.hereNow(channel, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("HERE NOW : " + message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("HERE NOW : " + error);
            }
        });
    }

    private void unsubscribe(String channel) {
        pubnub.unsubscribe(channel);
    }

    private void unsubscribePresence(String channel) {
        pubnub.unsubscribePresence(channel);
    }

    private void time() {
        pubnub.time(new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("TIME : " + message);
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("TIME : " + error);
            }
        });
    }

    private void disconnectAndResubscribe() {
        pubnub.disconnectAndResubscribe();

    }

    private void disconnectAndResubscribeWithTimetoken(String timetoken) {
        pubnub.disconnectAndResubscribeWithTimetoken(timetoken);

    }

    public void startDemo() {
        reader = new Scanner(System.in);
        notifyUser("HINT:\tTo test Re-connect and catch-up");
        notifyUser("\tDisconnect your machine from network/internet and");
        notifyUser("\tre-connect your machine after sometime");

        this.SSL = getBooleanFromConsole("SSL");

        if (this.publish_key.length() == 0) this.publish_key = getStringFromConsole("Publish Key");

        if (this.subscribe_key.length() == 0)  this.subscribe_key = getStringFromConsole("Subscribe Key");

        if (this.secret_key.length() == 0) this.secret_key = getStringFromConsole("Secret Key", true);

        if (this.cipher_key.length() == 0)  this.cipher_key = getStringFromConsole("Cipher Key", true);


        pubnub = new Pubnub(this.publish_key, this.subscribe_key, this.secret_key, this.cipher_key, this.SSL);

        displayMenuOptions();

        String channelName = null;
        int command = 0;
        while ((command = reader.nextInt()) != 9) {
            reader.nextLine();
            switch (command) {

            case 0:
                displayMenuOptions();
            case 1:
                channelName = getStringFromConsole("Subscribe: Enter Channel name");
                subscribe(channelName);

                notifyUser("Subscribed to following channels: ");
                notifyUser(PubnubUtil.joinString(
                        pubnub.getSubscribedChannelsArray(), " : "));
                break;
            case 2:
                channelName = getStringFromConsole("Channel Name");
                publish(channelName);
                break;
            case 3:
                channelName = getStringFromConsole("Channel Name");
                presence(channelName);
                break;
            case 4:
                channelName = getStringFromConsole("Channel Name");
                detailedHistory(channelName);
                break;
            case 5:
                channelName = getStringFromConsole("Channel Name");
                hereNow(channelName);
                break;
            case 6:
                channelName = getStringFromConsole("Channel Name");
                unsubscribe(channelName);
                break;
            case 7:
                channelName = getStringFromConsole("Channel Name");
                unsubscribePresence(channelName);
                break;
            case 8:
                time();
                break;
            case 10:
                disconnectAndResubscribe();
                break;
            case 11:
                notifyUser("Disconnect and Resubscribe with timetoken : Enter timetoken");
                String timetoken = getStringFromConsole("Timetoken");
                disconnectAndResubscribeWithTimetoken(timetoken);
                break;
            case 12:
                pubnub.setResumeOnReconnect(pubnub.isResumeOnReconnect() ? false
                        : true);
                notifyUser("RESUME ON RECONNECT : " + pubnub.isResumeOnReconnect());
                break;
            case 13:
                int maxRetries = getIntFromConsole("Max Retries");
                setMaxRetries(maxRetries);
                break;
            case 14:
                int retryInterval = getIntFromConsole("Retry Interval");
                setRetryInterval(retryInterval);
                break;
            case 15:
                int subscribeTimeout = getIntFromConsole("Subscribe Timeout ( in milliseconds) ");
                setSubscribeTimeout(subscribeTimeout);
                break;
            case 16:
                int nonSubscribeTimeout = getIntFromConsole("Non Subscribe Timeout ( in milliseconds) ");
                setNonSubscribeTimeout(nonSubscribeTimeout);
                break;
            case 17:
                notifyUser("Set/Unset Auth Key: Enter blank for unsetting key");
                String authKey = getStringFromConsole("Auth Key");
                pubnub.setAuthKey(authKey);
                break;
            case 18:
                ulsGrant();
                break;
            case 19:
                ulsRevoke();
                break;
            case 20:
                ulsAudit();
                break;
            case 21:
            	pubnub.setOrigin(getStringFromConsole("Origin"));
            	break;
            case 22:
            	pubnub.setDomain(getStringFromConsole("Domain"));
            	break;
            case 23:
            	pubnub.setCacheBusting(true);
            	break;
            case 24:
            	pubnub.setCacheBusting(false);
            	break;
            default:
                notifyUser("Invalid Input");
            }
            displayMenuOptions();
        }
        notifyUser("Exiting");
        pubnub.shutdown();

    }

    private String getStringFromConsole(String message, boolean optional) {

        int attempt_count = 0;
        String input = null;
        do {
            if (attempt_count > 0) System.out.print("Invalid input. ");
            String message1 = "Enter " + message ;
            message1 = (optional)?message1+" ( Optional input. You can skip by pressing enter )":message1;
            notifyUser(message1);
            input = reader.nextLine();
            attempt_count++;
        } while ((input == null || input.length() == 0) && !optional);
        notifyUser(message + " : " + input);
        return input;
    }

    private String getStringFromConsole(String message) {
        return getStringFromConsole(message, false);
    }
    private int getIntFromConsole(String message, boolean optional) {

        int attempt_count = 0;
        String input = null;
        int returnVal = -1;
        do {
            if (attempt_count > 0) notifyUser("Invalid input. ");
            String message1 = "Enter " + message;
            message1 = (optional)?message1+" ( Optional input. You can skip by pressing enter ) ":message1;
            notifyUser(message1);
            input = reader.nextLine();
            attempt_count++;
            returnVal = Integer.parseInt(input);
        } while ((input == null || input.length() == 0 || returnVal == -1) && !optional);
        notifyUser(message + " : " + returnVal);
        return returnVal;
    }

    private int getIntFromConsole(String message) {
        return getIntFromConsole(message, false);
    }

    private boolean getBooleanFromConsole(String message, boolean optional) {

        int attempt_count = 0;
        String input = null;
        boolean returnVal = false;
        do {
            if (attempt_count > 0) notifyUser("Invalid input. ");
            String message1 = message + " ? ( Enter Yes/No or Y/N )";
            message1 = (optional)?message1+" ( Optional input. You can skip by pressing enter ) ":message1;
            notifyUser(message1);
            input = reader.nextLine();
            attempt_count++;
        } while ((input == null || input.length() == 0 ||
                ( !input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no") &&
                        !input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n"))) && !optional);
        returnVal =  (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))?true:false;
        notifyUser(message + " : " + returnVal);
        return returnVal;
    }

    private boolean getBooleanFromConsole(String message) {
        return getBooleanFromConsole(message, false);
    }

    private void ulsGrant() {
        String channel = getStringFromConsole("Channel");
        String auth_key = getStringFromConsole("Auth Key");
        boolean read = getBooleanFromConsole("Read");
        boolean write = getBooleanFromConsole("Write");

        pubnub.ulsGrant(channel, auth_key, read, write, new Callback() {

            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("CHANNEL : " + channel + " , " + message.toString());

            }
            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("CHANNEL : " + channel + " , " + error.toString());
            }

        });
    }

    private void ulsAudit() {
        String channel = getStringFromConsole("Channel", true);
        String auth_key = getStringFromConsole("Auth Key", true);

        Callback cb = new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("CHANNEL : " + channel + " , " + message.toString());

            }
            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("CHANNEL : " + channel + " , " + error.toString());
            }
        };

        if (channel != null && channel.length() > 0) {
            if (auth_key != null && auth_key.length() != 0) {
                pubnub.ulsAudit(channel, auth_key, cb);
            } else {
                pubnub.ulsAudit(channel, cb);
            }
        } else {
            pubnub.ulsAudit(cb);
        }

    }

    private void ulsRevoke() {
        String channel = getStringFromConsole("Enter Channel");
        String auth_key = getStringFromConsole("Auth Key");

        pubnub.ulsRevoke(channel, auth_key, new Callback() {

            @Override
            public void successCallback(String channel, Object message) {
                notifyUser("CHANNEL : " + channel + " , " + message.toString());

            }
            @Override
            public void errorCallback(String channel, PubnubError error) {
                notifyUser("CHANNEL : " + channel + " , " + error.toString());
            }

        });
    }

    private void setMaxRetries(int maxRetries) {
        pubnub.setMaxRetries(maxRetries);
    }

    private void setRetryInterval(int retryInterval) {
        pubnub.setRetryInterval(retryInterval);
    }

    private void setSubscribeTimeout(int subscribeTimeout) {
        pubnub.setSubscribeTimeout(subscribeTimeout);
    }

    private void setNonSubscribeTimeout(int nonSubscribeTimeout) {
        pubnub.setNonSubscribeTimeout(nonSubscribeTimeout);
    }

    private void displayMenuOptions() {
        notifyUser("ENTER 1  FOR Subscribe "
                + "(Currently subscribed to "
                + this.pubnub.getCurrentlySubscribedChannelNames() + ")");
        notifyUser("ENTER 2  FOR Publish");
        notifyUser("ENTER 3  FOR Presence");
        notifyUser("ENTER 4  FOR Detailed History");
        notifyUser("ENTER 5  FOR Here_Now");
        notifyUser("ENTER 6  FOR Unsubscribe");
        notifyUser("ENTER 7  FOR Presence-Unsubscribe");
        notifyUser("ENTER 8  FOR Time");
        notifyUser("ENTER 9  FOR EXIT OR QUIT");
        notifyUser("ENTER 10 FOR Disconnect-And-Resubscribe");
        notifyUser("ENTER 11 FOR Disconnect-And-Resubscribe with timetoken");
        notifyUser("ENTER 12 FOR Toggle Resume On Reconnect");
        notifyUser("ENTER 13 FOR Setting MAX Retries");
        notifyUser("ENTER 14 FOR Setting Retry Interval");
        notifyUser("ENTER 15 FOR Setting Subscribe Timeout");
        notifyUser("ENTER 16 FOR Setting Non Subscribe Timeout");
        notifyUser("ENTER 17 FOR Setting/Unsetting auth key1");
        notifyUser("ENTER 18 FOR ULS grant");
        notifyUser("ENTER 19 FOR ULS revoke");
        notifyUser("ENTER 20 FOR ULS Audit");
        notifyUser("ENTER 21 FOR Setting Origin ( default: pubsub )");
        notifyUser("ENTER 22 FOR Setting Domain ( default: punub.com )");
        notifyUser("Enter 23 FOR Enabling Cache Busting");
        notifyUser("Enter 24 FOR Disabling Cache Busting");
        notifyUser("\nENTER 0 to display this menu");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        PubnubDemoConsole pdc;
        if (args.length == 4) {
            pdc = new PubnubDemoConsole(args[0], args[1], args[2], args[3]);
        } else
            pdc = new PubnubDemoConsole();
        pdc.startDemo();
    }

}

package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author PubnubCore
 *
 */
class Subscriptions {
    private Hashtable<String, SubscriptionItem> items;

    JSONObject state;

    public Subscriptions() {
        items    = new Hashtable<String, SubscriptionItem>();
        state    = new JSONObject();
    }

    public void addItem(SubscriptionItem item) {
        items.put(item.name, item);
    }

    public void removeItem(String name) {
        items.remove(name);
    }

    public void removeAllItems() {
        items.clear();
    }

    public SubscriptionItem getFirstItem() {
        SubscriptionItem ch = null;
        synchronized (items) {
            if (items.size() > 0) {
                ch = items.elements().nextElement();
            }
        }
        return ch;
    }

    public SubscriptionItem getItem(String name) {
        return items.get(name);
    }

    public String[] getItemNames() {

        return PubnubUtil.hashtableKeysToArray(items);
    }

    public String getItemStringNoPresence() {
        return PubnubUtil.hashTableKeysToDelimitedString(items, ",", "-pnpres");
    }

    public String getItemString() {
        return PubnubUtil.hashTableKeysToDelimitedString(items, ",");
    }

    public void invokeConnectCallbackOnItems(Object message) {
        invokeConnectCallbackOnItems(getItemNames(), message);
    }

    public void invokeDisconnectCallbackOnItems(Object message) {
        invokeDisconnectCallbackOnItems(getItemNames(), message);
    }

    public void invokeErrorCallbackOnItems(PubnubError error) {
        /*
         * Iterate over all the items and call error callback for items
         */
        synchronized (items) {
            Enumeration itemsElements = items.elements();
            while (itemsElements.hasMoreElements()) {
                SubscriptionItem _item = (SubscriptionItem) itemsElements.nextElement();
                _item.error = true;
                _item.callback.errorCallback(_item.name, error);
            }
        }
    }

    public void invokeConnectCallbackOnItems(String[] items, Object message) {
        synchronized (items) {
            for (int i = 0; i < items.length; i++) {
                SubscriptionItem _item = this.items.get(items[i]);
                if (_item != null) {
                    if (_item.connected == false) {
                        _item.connected = true;
                        if (_item.subscribed == false) {
                            _item.callback.connectCallback(_item.name,
                                    new JSONArray().put(1).put("Subscribe connected").put(message));
                        } else {
                            _item.subscribed = true;
                            _item.callback.reconnectCallback(_item.name,
                                    new JSONArray().put(1).put("Subscribe reconnected").put(message));
                        }
                    }
                }
            }
        }
    }

    public void invokeReconnectCallbackOnItems(Object message) {
        invokeReconnectCallbackOnItems(getItemNames(), message);
    }

    public void invokeReconnectCallbackOnItems(String[] items, Object message) {
        synchronized (items) {
            for (int i = 0; i < items.length; i++) {
                SubscriptionItem _item = this.items.get(items[i]);
                if (_item != null) {
                    _item.connected = true;
                    if ( _item.error ) {
                        _item.callback.reconnectCallback(_item.name,
                                new JSONArray().put(1).put("Subscribe reconnected").put(message));
                        _item.error = false;
                    }
                }
            }
        }
    }

    public void invokeDisconnectCallbackOnItems(String[] items, Object message) {
        synchronized (items) {
            for (int i = 0; i < items.length; i++) {
                SubscriptionItem _item = this.items.get(items[i]);
                if (_item != null) {
                    if (_item.connected == true) {
                        _item.connected = false;
                        _item.callback.disconnectCallback(_item.name,
                                new JSONArray().put(0).put("Subscribe unable to connect").put(message));
                    }
                }
            }
        }
    }
}

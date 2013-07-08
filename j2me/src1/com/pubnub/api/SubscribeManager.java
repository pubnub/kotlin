package com.pubnub.api;

class SubscribeManager extends AbstractSubscribeManager {

    public SubscribeManager(String name, int connectionTimeout,
                            int requestTimeout) {
        super(name, connectionTimeout, requestTimeout);
    }

    public void resetWorkersConnections() {
        for (int i = 0; i < _workers.length; i++) {
            _workers[i].die();
            _workers[i].interruptWorker();
            Worker w = getWorker();
            w.setThread(new Thread(w,name));
            _workers[i] = w;
            w.startWorker();
        }
    }

    public void clearRequestQueue() {
        _waiting.removeAllElements();
    }
}

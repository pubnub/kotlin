package com.pubnub.api;

//import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Vector;
import static com.pubnub.api.PubnubError.*;

class SubscribeWorker extends AbstractSubscribeWorker {
    private Exception excp = null;
    SubscribeWorker(Vector _requestQueue, int connectionTimeout,
                    int requestTimeout, int maxRetries, int retryInterval, Hashtable headers) {
        super(_requestQueue, connectionTimeout, requestTimeout,
              maxRetries, retryInterval, headers);
    }

    void process(HttpRequest hreq) {
        HttpResponse hresp = null;
        int currentRetryAttempt = (hreq.isDar())?1:maxRetries;
        log.verbose("disconnectAndResubscribe is " + hreq.isDar());
        if (hreq.getWorker() != null) {
            log.verbose("Request placed by worker " + hreq.getWorker().getThread().getName());
            if (hreq.getWorker()._die) {
                log.verbose("The thread which placed the request has died, so ignore the request : " + hreq.getWorker().getThread().getName());
                return;
            }
        }
        hreq.setWorker(this);
        while (!_die && currentRetryAttempt <= maxRetries) {
            try {
                log.debug(hreq.getUrl());
                hresp = httpclient.fetch(hreq.getUrl(), hreq.getHeaders());
                if (hresp != null
                        && HttpUtil.checkResponseSuccess(hresp
                                                         .getStatusCode())) {
                    currentRetryAttempt = 1;
                    break;
                }
            } /*catch (SocketTimeoutException e) {
                log.verbose("No Traffic , Read Timeout Exception in Fetch : " + e.toString());
                if (_die) {
                    log.verbose("Asked to Die, Don't do back from DAR processing");
                    break;
                }
                if (hreq.isDar()) {
                    hreq.getResponseHandler().handleBackFromDar(hreq);
                    return;
                }
                break;

            } */catch (PubnubException e) {
                excp = e;
                switch (e.getPubnubError().errorCode) {
                case PNERR_FORBIDDEN:
                case PNERR_UNAUTHORIZED:
                    log.verbose("Authentication Failure : " + e.toString());
                    currentRetryAttempt++;
                    break;
                default:
                    log.verbose("Retry Attempt : " + ((currentRetryAttempt == maxRetries)?"last":currentRetryAttempt)
                                + " Exception in Fetch : " + e.toString());
                    currentRetryAttempt++;
                    break;
                }

            } catch (Exception e) {
                excp = e;
                log.verbose("Retry Attempt : " + ((currentRetryAttempt == maxRetries)?"last":currentRetryAttempt)
                            + " Exception in Fetch : " + e.toString());
                currentRetryAttempt++;
            }

            try {
                Thread.sleep(retryInterval);
            } catch (InterruptedException e) {
            }
        }
        if (!_die) {
            if (hresp == null) {
                log.debug("Error in fetching url : " + hreq.getUrl());
                if (hreq.isDar()) {
                    log.verbose("Exhausted number of retries");
                    hreq.getResponseHandler().handleTimeout(hreq);
                } else {

                    if (excp != null && excp instanceof PubnubException && ((PubnubException) excp).getPubnubError() != null) {
                        hreq.getResponseHandler().handleError(hreq, ((PubnubException) excp).getPubnubError());
                    } else {
                        hreq.getResponseHandler().handleError(hreq, getErrorObject(PNERROBJ_HTTP_ERROR, 1));
                    }
                }
                return;
            }
            log.debug(hresp.getResponse());
            hreq.getResponseHandler().handleResponse(hreq, hresp.getResponse());
        }

    }

    public void shutdown() {
        if (httpclient != null) httpclient.shutdown();
    }
}

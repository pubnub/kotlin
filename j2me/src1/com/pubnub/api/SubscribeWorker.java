package com.pubnub.api;

import java.util.Hashtable;
import java.util.Vector;

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
        boolean sleep = false;
        while (!_die && currentRetryAttempt <= maxRetries) {
            if (sleep) {
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException e) {
                }
                sleep = true;
            }
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
                if (hreq.isDar()) {
                    hreq.getResponseHandler().handleBackFromDar(hreq);
                    return;
                }
                break;

            }*/catch (PubnubException e) {
                excp = e;
                switch (e.getPubnubError().errorCode) {
                case PubnubError.PNERR_FORBIDDEN:
                case PubnubError.PNERR_UNAUTHORIZED:
                    log.verbose("Authentication Failure : " + e.toString());
                    currentRetryAttempt = maxRetries + 1;
                    break;
                default:
                    log.verbose("Retry Attempt : " + ((currentRetryAttempt == maxRetries)?"last":String.valueOf(currentRetryAttempt))
                                + " Exception in Fetch : " + e.toString());
                    currentRetryAttempt++;
                    break;
                }

            } catch (Exception e) {
                log.verbose("Retry Attempt : " + ((currentRetryAttempt == maxRetries)?"last":String.valueOf(currentRetryAttempt))
                            + " Exception in Fetch : " + e.toString());
                currentRetryAttempt++;
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
                        hreq.getResponseHandler().handleError(hreq, PubnubError.getErrorObject(PubnubError.PNERROBJ_HTTP_ERROR, 1));
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

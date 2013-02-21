package com.pubnub.api;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import com.pubnub.http.HttpRequest;
import com.pubnub.httpclient.HttpResponse;

class SubscribeWorker extends AbstractSubscribeWorker {

	SubscribeWorker(Vector _requestQueue, int connectionTimeout, int requestTimeout) {
		super(_requestQueue, connectionTimeout, requestTimeout);
	}

	void process(HttpRequest hreq) {
		HttpResponse hresp = null;
		int currentRetryAttempt = 1;
		while (currentRetryAttempt <= maxRetries) {
			try {
				log.debug(hreq.getUrl());
				hresp = httpclient.fetch(hreq.getUrl(), hreq.getHeaders());
				if (hresp != null && httpclient.checkResponseSuccess(hresp.getStatusCode())) {
					currentRetryAttempt = 1;
					break;
				}
			}
			catch (SocketTimeoutException e){
				log.verbose("Exception in Fetch : " + e.toString());
				break;
			}
			catch (Exception e) {
				log.verbose("Retry Attempt : " + currentRetryAttempt + " Exception in Fetch : " + e.toString());
				currentRetryAttempt++;
			}

			try {
				Thread.sleep(retryInterval);
			} catch (InterruptedException e) {
			}
		}
		if (hresp == null) {
			log.debug("Error in fetching url : " + hreq.getUrl());
			if (currentRetryAttempt > maxRetries) {
				log.verbose("Exhausted number of retries");
				hreq.getResponseHandler().handleTimeout();
			} else
				hreq.getResponseHandler().handleError("Request Timeout");
			return;
		}
		log.debug(hresp.getResponse());
		hreq.getResponseHandler().handleResponse(hresp.getResponse());

	}
}
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubError;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.endpoints.pubsub.Publish;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by tukunare on 7/5/2016.
 */
public class PubNubExceptionTest extends TestHarness {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private PubNub pubnub;
    private Publish instance;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        instance = pubnub.publish();
    }

    @Test
    public void testPubnubError() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/publish/myPublishKey/mySubscribeKey/0/coolChannel/0/%22hi%22"))
                .willReturn(aResponse().withStatus(404).withBody("[1,\"Sent\",\"14598111595318003\"]")));

        int statusCode = -1;
        PubNubError pubnubError = null;
        int pnErrorCode= - 1;
        int pnErroCodeExtended = -1;
        JsonNode pnErrorJNode = null;
        String pnErrorMessage = null;
        String pnErrorString = null;
        String response = null;
        String erroMsg = null;
        JsonNode jNode = null;


        try {
            instance.channel("coolChannel").message(new Object()).sync();
        }
        catch (PubNubException error) {
            statusCode = error.getStatusCode();
            pubnubError = error.getPubnubError();
            pnErrorCode = pubnubError.getErrorCode();
            pnErroCodeExtended = pubnubError.getErrorCodeExtended();
            pnErrorJNode = pubnubError.getErrorObject();
            pnErrorMessage = pubnubError.getMessage();
            pnErrorString = pubnubError.getErrorString();
            response = error.getResponse();
            erroMsg = error.getErrormsg();
            jNode = error.getJso();
        }

        assertEquals(0, statusCode);
    }


}

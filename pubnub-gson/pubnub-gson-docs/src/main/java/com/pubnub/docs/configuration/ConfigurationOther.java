package com.pubnub.docs.configuration;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.crypto.CryptoModule;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.PNConfiguration;
import com.pubnub.api.java.v2.PNConfigurationOverride;
import com.pubnub.docs.SnippetBase;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class ConfigurationOther extends SnippetBase {

    private void basicConfiguration() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#methods

        // snippet.basicConfiguration
        // import com.pubnub.api.java.v2.PNConfiguration;
        PNConfiguration.builder(new UserId("yourUserId"), "demo").build();
        // snippet.end
    }

    private void cryptoModuleConfiguration() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#cryptomodule-configuration

        // snippet.cryptoModuleConfiguration
        PNConfiguration.Builder builder = PNConfiguration.builder(new UserId("yourUserId"), "demo");

        // encrypts using 256-bit AES-CBC cipher (recommended)
        // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
        builder.cryptoModule(CryptoModule.createAesCbcCryptoModule("enigma", true));

        // OR

        // encrypts with 128-bit cipher key entropy (legacy)
        // decrypts data encrypted with the legacy and the 256-bit AES-CBC ciphers
        builder.cryptoModule(CryptoModule.createLegacyCryptoModule("enigma", true));

        PNConfiguration pnConfiguration = builder.build();
        // snippet.end
    }

    private void valueOverride() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#value-override

        PubNub pubNub = createPubNub();

        // snippet.valueOverride
        //import com.pubnub.api.java.v2.PNConfiguration;
        //import com.pubnub.api.java.v2.PNConfigurationOverride;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.publishKey("PublishKey");
        configBuilder.cryptoModule(CryptoModule.createAesCbcCryptoModule("enigma", true));
        PNConfiguration config = configBuilder.build();

        // publish
        PNConfiguration overrideConfig = PNConfigurationOverride.from(config)
                .publishKey("overridePublishKey")
                .build();

        pubNub.publish().channel("myChannel").message("myMessage")
                .overrideConfiguration(overrideConfig).sync();
        // snippet.end
    }

    private void initializePubNubMethod() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#methods-1

        // snippet.initializePubNubMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub.create(configBuilder.build());
        // snippet.end
    }


    private void initializePubNubBasieExample() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#initialize-the-pubnub-client-api

        // snippet.initializePubNubBasieExample
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void initializeNonSecureClient() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#initialize-a-non-secure-client
        // snippet.initializeNonSecureClient
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.secure(false);
        PubNub pubNub = PubNub.create(configBuilder.build());

        // snippet.end
    }

    private void initializeReadOnlyClient() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#initialization-for-a-read-only-client
        // snippet.initializeReadOnlyClient
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void initializeWithSSL() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#initializing-with-ssl-enabled

        // snippet.initializeWithSsl
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.secure(true);
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void initializeWithAccessManager() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#initializing-with-access-manager

        // snippet.initializeWithAccessManager
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.secure(true);
        configBuilder.secretKey("secretKey");
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void initializeWithProxy() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#how-to-set-proxy
        // snippet.initializeWithProxy
        //  import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.publishKey("myPublishKey");
        configBuilder.secure(true);
        configBuilder.secretKey("secretKey");
        configBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("http://mydomain.com", 8080)));
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void configurationSetUserIdMethod() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#methods-2

        // snippet.configurationSetUserIdMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        // snippet.end
    }

    private void configurationSetUserId() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#set-user-id

        // snippet.configurationSetUserId
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());
        // snippet.end
    }

    private void configurationGetUserIdMethod() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#methods-2  // <-- this is the same anchore as for setUserId

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());

        // snippet.configurationGetUserIdMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        pubNub.getConfiguration().getUserId();
        // snippet.end
    }

    private void configurationGetUserId() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#get-user-id

        // snippet.configurationGetUserId
        //  import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        PubNub pubNub = PubNub.create(configBuilder.build());

        pubNub.getConfiguration().getUserId();
        // snippet.end
    }

    private void configurationSetFilterExpressionMethod() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#methods-3

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");

        // snippet.configurationSetFilterExpressionMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        configBuilder.filterExpression("someString");
        // snippet.end
    }

    private void configurationSetFilterExpression() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#set-filter-expression
        // snippet.configurationSetFilterExpression
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.filterExpression("such=wow");
        // snippet.end
    }

    private void configurationGetFilterExpressionMethod() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#methods-3 // <-- this is the same anchore like for setFilterExpression

        PubNub pubNub = createPubNub();
        // snippet.configurationGetFilterExpressionMethod
        // import com.pubnub.api.java.v2.PNConfiguration;

        pubNub.getConfiguration().getFilterExpression();
        // snippet.end
    }

    private void configurationGetFilterExpression() throws PubNubException {
        // https://www.pubnub.com/docs/sdks/java/api-reference/configuration#get-filter-expression
        // snippet.configurationGetFilterExpression
        // import com.pubnub.api.java.v2.PNConfiguration;

        PNConfiguration.Builder configBuilder = PNConfiguration.builder(new UserId("yourUserId"), "yourSubscribeKey");
        configBuilder.filterExpression("such=wow");
        PubNub pubNub = PubNub.create(configBuilder.build());

        pubNub.getConfiguration().getFilterExpression();
        // snippet.end
    }
}

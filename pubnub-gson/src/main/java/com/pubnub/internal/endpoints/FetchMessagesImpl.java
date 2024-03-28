package com.pubnub.internal.endpoints;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction;
import com.pubnub.api.models.consumer.PNBoundedPage;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.internal.PubNubCore;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
@Slf4j
@Accessors(chain = true, fluent = true)
public class FetchMessagesImpl extends DelegatingEndpoint<PNFetchMessagesResult> implements com.pubnub.api.endpoints.FetchMessages {
    private static final int SINGLE_CHANNEL_DEFAULT_MESSAGES = 100;
    private static final int SINGLE_CHANNEL_MAX_MESSAGES = 100;
    private static final int MULTIPLE_CHANNEL_DEFAULT_MESSAGES = 25;
    private static final int MULTIPLE_CHANNEL_MAX_MESSAGES = 25;
    private static final int DEFAULT_MESSAGES_WITH_ACTIONS = 25;
    private static final int MAX_MESSAGES_WITH_ACTIONS = 25;

    private List<String> channels = new ArrayList<>();
    private Integer maximumPerChannel;
    private Long start;
    private Long end;

    private boolean includeMeta;
    private boolean includeMessageActions;
    private boolean includeMessageType = true;
    private boolean includeUUID = true;

    public FetchMessagesImpl(PubNubCore pubnub) {
        super(pubnub);
    }

    @NotNull
    @Override
    protected ExtendedRemoteAction<PNFetchMessagesResult> createAction() {
        return pubnub.fetchMessages(
                channels,
                new PNBoundedPage(start, end, maximumPerChannel),
                includeUUID,
                includeMeta,
                includeMessageActions,
                includeMessageType
        );
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (channels == null || channels.isEmpty()) {
            throw new PubNubException(PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING);
        }
        if (!includeMessageActions) {
            if (channels.size() == 1) {
                if (maximumPerChannel == null || maximumPerChannel < 1) {
                    maximumPerChannel = SINGLE_CHANNEL_DEFAULT_MESSAGES;
                    log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
                } else if (maximumPerChannel > SINGLE_CHANNEL_MAX_MESSAGES) {
                    maximumPerChannel = SINGLE_CHANNEL_MAX_MESSAGES;
                    log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
                }
            } else {
                if (maximumPerChannel == null || maximumPerChannel < 1) {
                    maximumPerChannel = MULTIPLE_CHANNEL_DEFAULT_MESSAGES;
                    log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
                } else if (maximumPerChannel > MULTIPLE_CHANNEL_MAX_MESSAGES) {
                    maximumPerChannel = MULTIPLE_CHANNEL_MAX_MESSAGES;
                    log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
                }
            }
        } else {
            if (maximumPerChannel == null || maximumPerChannel < 1 || maximumPerChannel > MAX_MESSAGES_WITH_ACTIONS) {
                maximumPerChannel = DEFAULT_MESSAGES_WITH_ACTIONS;
                log.info("maximumPerChannel param defaulting to " + maximumPerChannel);
            }
        }
    }
}

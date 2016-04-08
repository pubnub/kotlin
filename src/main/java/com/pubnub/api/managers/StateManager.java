package com.pubnub.api.managers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Data
@Slf4j
public class StateManager {

    private Map<String, Object> stateStorage;

    public StateManager() {
        this.stateStorage = new HashMap<>();
    }


    public void addStateForChannel(String channel, Object data){
        stateStorage.put(channel, data);
    }

    public void removeStateForChannel(String channel){
        stateStorage.remove(channel);
    }

    public String getStringifiedStateByChannel() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            return objMapper.writeValueAsString(stateStorage);
        } catch (JsonProcessingException e) {
            log.error("failed to parse the state in stateManager", e);
            return null;
        }
    }

}

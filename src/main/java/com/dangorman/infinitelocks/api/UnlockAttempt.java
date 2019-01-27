package com.dangorman.infinitelocks.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnlockAttempt {

    private String lock;
    private String key;

    public UnlockAttempt() {
        // Jackson deserialization
    }

    @JsonProperty
    public String getLock() {
        return lock;
    }

    @JsonProperty
    public String getKey() {
        return key;
    }
}
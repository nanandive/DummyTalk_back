package com.example.DummyTalk.Chat.Channel.Enum;

public enum PayloadResponseType {
    JOIN_USER("user-joined"),
    OTHER_USER("other-user"),
    OFFER("offer"),
    ANSWER("answer"),
    ICE_CANDIDATE("ice-candidate");

    private final String value;

    PayloadResponseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

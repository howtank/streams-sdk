package com.howtank.streams.client.api;

public enum CommandType {
    ADD_STREAM_MESSAGE ("add_stream_message"),
    ADD_REACTION_STREAM_MESSAGE ("add_reaction_stream_message"),
    GET_USER_STREAMS ("get_user_streams"),
    GET_STREAMS("get_streams"),
    GET_PRESENCE ("get_presence"),
    GET_STREAM_MESSAGES ("get_stream_messages"),
    PUSH_STREAM_MESSAGE("push_stream_message");

    private final String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

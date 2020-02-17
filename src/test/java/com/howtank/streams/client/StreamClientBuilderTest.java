package com.howtank.streams.client;

import com.howtank.streams.client.bean.ClientProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StreamClientBuilderTest {
    private static final String A_USER_ID = "USER_X";

    @Test
    void buildAndGetUser() {
        ClientProperties properties = ClientProperties.builder()
                .currentUserId(A_USER_ID)
                .build();

        StreamClient streamClient = new StreamClientBuilder().build(properties);

        Assertions.assertNotNull(streamClient);
        Assertions.assertEquals(A_USER_ID, streamClient.getCurrentUserId());
    }

    @Test
    void buildWithNoUser() {
        ClientProperties properties = ClientProperties.builder().build();

        StreamClient streamClient = new StreamClientBuilder().build(properties);

        Assertions.assertNotNull(streamClient);
        Assertions.assertEquals(A_USER_ID, streamClient.getCurrentUserId());
    }
}
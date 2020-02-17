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
                .userAgent("Android Widget")
                .accessToken("TOKEN")
                .build();

        StreamClient streamClient = new StreamClientBuilder().build(properties);

        Assertions.assertNotNull(streamClient);
        Assertions.assertEquals(A_USER_ID, streamClient.getCurrentUserId());
    }

    @Test
    void builPropertiesdWithNoUser() {
        ClientProperties properties = null;

        try {
            properties = ClientProperties.builder().build();

            Assertions.fail("currentUserId is null but not exception thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof NullPointerException);
        }
    }
}
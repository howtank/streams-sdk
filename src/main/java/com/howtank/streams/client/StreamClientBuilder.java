package com.howtank.streams.client;

import com.howtank.streams.client.bean.ClientProperties;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public final class StreamClientBuilder {
    private static final String DEFAULT_ENDPOINT = "www.howtank.com";

    public StreamClient build(@NonNull ClientProperties properties) {
        String endpoint = properties.getEndpoint() != null ? properties.getEndpoint() : DEFAULT_ENDPOINT;

        return new StreamClientHttp(
                properties.getCurrentUserId(),
                endpoint,
                properties.getUserAgent(),
                properties.getAccessToken());
    }
}

package com.howtank.streams.client.bean;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ClientProperties {
    @NonNull
    private String currentUserId;
    private String endpoint;

    @NonNull
    private String userAgent;

    @NonNull
    private String accessToken;
}

package com.howtank.streams.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Host {
    private String id;
    private String name;
    private String displayName;
    private String partnerId;
    private boolean howtankHost;
}

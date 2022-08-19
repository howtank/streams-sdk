package com.howtank.streams.client.bean;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SearchStreamsFilter {
    Integer from;
    Integer size;
    Boolean discoverable;
    Boolean subscribed;
    String keyword;
}

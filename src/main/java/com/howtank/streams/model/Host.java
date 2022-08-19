package com.howtank.streams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Host {
    private String id;
    private String name;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("howtank_host")
    private boolean howtankHost;

    @JsonProperty("default_stream")
    private boolean defaultStream;

    @JsonProperty("landing_stream")
    private boolean landingStream;
}

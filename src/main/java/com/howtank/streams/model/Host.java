package com.howtank.streams.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Host {
    private String id;
    private String name;

    @SerializedName(value = "display_name")
    private String displayName;

    @SerializedName(value = "partner_id")
    private String partnerId;

    @SerializedName(value = "howtank_host")
    private boolean howtankHost;

    @SerializedName(value = "default_stream")
    private boolean defaultStream;

    @SerializedName(value = "landing_stream")
    private boolean landingStream;
}

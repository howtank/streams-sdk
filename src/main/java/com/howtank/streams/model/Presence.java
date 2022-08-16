package com.howtank.streams.model;

import com.google.gson.annotations.SerializedName;
import com.howtank.streams.model.types.ConnectionStatusType;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class Presence {
    @SerializedName(value = "user")
    private User user;

    @SerializedName(value = "connection_date")
    private Date connectionDate;

    @SerializedName(value = "connection_status")
    private ConnectionStatusType connectionStatus;
}

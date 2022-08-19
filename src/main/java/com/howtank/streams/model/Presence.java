package com.howtank.streams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.howtank.streams.model.types.ConnectionStatusType;
import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presence {
    @JsonProperty("user")
    private User user;

    @JsonProperty("connection_date")
    private Date connectionDate;

    @JsonProperty("connection_status")
    private ConnectionStatusType connectionStatus;
}

package com.howtank.streams.model;

import com.howtank.streams.model.types.ConnectionStatusType;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class Presence {
    private User user;
    private Date connectionDate;
    private ConnectionStatusType connectionStatus;
}

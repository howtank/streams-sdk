package com.howtank.streams.model;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class User {
    private String id;
    private String displayName;
    private String type;
    private boolean howtankTeam;
    private String photo;
    private long points;
    private boolean moderator;

    private String sourceHostId;
    private Set<String> hostIds;
    private Set<String> streamIds;
}

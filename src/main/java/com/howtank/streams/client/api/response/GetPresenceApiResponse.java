package com.howtank.streams.client.api.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.howtank.streams.model.Presence;
import com.howtank.streams.model.User;
import com.howtank.streams.model.types.ConnectionStatusType;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class GetPresenceApiResponse extends ApiResponse {
    @JsonAlias({"streamId", "stream_id"})
    @JsonProperty("stream_id")
    private String streamId;
    private Integer count;
    private List<PresenceDto> users;

    @JsonIgnore
    public List<Presence> getPresences() {
        return users.stream().map(PresenceDto::toPresence).collect(Collectors.toList());
    }
}

@Data
class PresenceDto {
    @JsonProperty("connection_date")
    private Date connectionDate;

    @JsonProperty("connection_status")
    private ConnectionStatusType connectionStatus;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_display_name")
    private String userDisplayName;

    @JsonProperty("user_type")
    private String userType;

    @JsonProperty("howtank_team")
    private boolean howtankTeam;

    @JsonProperty("user_photo_key")
    private String userPhotoKey;
    private long points;
    private boolean moderator;

    @JsonProperty("host_id")
    private String hostId;

    @JsonProperty("host_ids")
    private Set<String> hostIds;

    @JsonProperty("stream_ids")
    private Set<String> streamIds;

    public Presence toPresence() {
        return Presence.builder()
                .connectionDate(connectionDate)
                .connectionStatus(connectionStatus)
                .user(User.builder()
                        .id(userId)
                        .displayName(userDisplayName)
                        .hostIds(hostIds)
                        .howtankTeam(howtankTeam)
                        .moderator(moderator)
                        .photo(userPhotoKey)
                        .points(points)
                        .sourceHostId(hostId)
                        .streamIds(streamIds)
                        .type(userType)
                        .build())
                .build();
    }
}
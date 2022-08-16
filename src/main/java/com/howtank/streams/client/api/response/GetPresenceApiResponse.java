package com.howtank.streams.client.api.response;

import com.google.gson.annotations.SerializedName;
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
    @SerializedName(value = "streamId", alternate = "stream_id")
    private String streamId;
    private Integer count;
    private List<PresenceDto> users;

    public List<Presence> getPresences() {
        return users.stream().map(PresenceDto::toPresence).collect(Collectors.toList());
    }
}

@Data
class PresenceDto {
    @SerializedName(value = "connection_date")
    private Date connectionDate;

    @SerializedName(value = "connection_status")
    private ConnectionStatusType connectionStatus;

    @SerializedName(value = "user_id")
    private String userId;

    @SerializedName(value = "user_display_name")
    private String userDisplayName;

    @SerializedName(value = "user_type")
    private String userType;

    @SerializedName(value = "howtank_team")
    private boolean howtankTeam;

    @SerializedName(value = "user_photo_key")
    private String userPhotoKey;
    private long points;
    private boolean moderator;

    @SerializedName(value = "host_id")
    private String hostId;

    @SerializedName(value = "host_ids")
    private Set<String> hostIds;

    @SerializedName(value = "stream_ids")
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
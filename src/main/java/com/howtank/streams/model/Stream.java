package com.howtank.streams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.howtank.streams.model.types.RoleType;
import com.howtank.streams.model.types.UserType;
import com.howtank.streams.model.types.VisaState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stream {
    private String id;
    private String title;
    private String description;
    private String logo;
    private boolean discoverable;

    @JsonProperty("channel_id")
    private String channelId;

    @JsonProperty("presence_id")
    private String presenceId;

    @JsonProperty("first_stream_color")
    private String firstStreamColor;

    @JsonProperty("second_stream_color")
    private String secondStreamColor;

    @JsonProperty("experimental_flags")
    private Set<String> experimentalFlags;

    @JsonProperty("available_message_types")
    private Set<String> availableMessageTypes;

    @JsonProperty("creator_user_id")
    private String creatorUserId;

    @JsonProperty("creator_user_display_name")
    private String creatorUserDisplayName;

    @JsonProperty("creator_user_type")
    private UserType creatorUserType;

    @JsonProperty("creator_user_role")
    private RoleType creatorUserRole;

    @JsonProperty("all_users")
    private boolean allUsers;

    @JsonProperty("restricted_user_types")
    private Set<UserType> restrictedUserTypes;

    @JsonProperty("restricted_roles")
    private Set<RoleType> restrictedRoles;

    @JsonProperty("restricted_visa_states")
    private Set<VisaState> restrictedVisaStates;

    private Set<Host> hosts;
}

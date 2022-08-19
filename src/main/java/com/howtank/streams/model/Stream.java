package com.howtank.streams.model;

import com.google.gson.annotations.SerializedName;
import com.howtank.streams.model.types.RoleType;
import com.howtank.streams.model.types.UserType;
import com.howtank.streams.model.types.VisaState;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class Stream {
    private String id;
    private String title;
    private String description;
    private String logo;
    private boolean discoverable;

    @SerializedName("channel_id")
    private String channelId;

    @SerializedName("presence_id")
    private String presenceId;

    @SerializedName("first_stream_color")
    private String firstStreamColor;

    @SerializedName("second_stream_color")
    private String secondStreamColor;

    @SerializedName("experimental_flags")
    private Set<String> experimentalFlags;

    @SerializedName("available_message_types")
    private Set<String> availableMessageTypes;

    @SerializedName("creator_user_id")
    private String creatorUserId;

    @SerializedName("creator_user_display_name")
    private String creatorUserDisplayName;

    @SerializedName("creator_user_type")
    private UserType creatorUserType;

    @SerializedName("creator_user_role")
    private RoleType creatorUserRole;

    @SerializedName("all_users")
    private boolean allUsers;

    @SerializedName("restricted_user_types")
    private Set<UserType> restrictedUserTypes;

    @SerializedName("restricted_roles")
    private Set<RoleType> restrictedRoles;

    @SerializedName("restricted_visa_states")
    private Set<VisaState> restrictedVisaStates;

    private Set<Host> hosts;
}

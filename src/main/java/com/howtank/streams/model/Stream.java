package com.howtank.streams.model;

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
    private String channelId;
    private String presenceId;
    private String firstStreamColor;
    private String secondStreamColor;
    private Set<String> experimentalFlags;
    private Set<String> availableMessageTypes;
    private String creatorUserId;
    private String creatorUserDisplayName;
    private UserType creatorUserType;
    private RoleType creatorUserRole;
    private boolean allUsers;
    private Set<UserType> restrictedUserTypes;
    private Set<RoleType> restrictedRoles;
    private Set<VisaState> restrictedVisaStates;
    private Set<Host> hosts;
}

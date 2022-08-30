package com.howtank.streams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class Reaction {
    @JsonProperty("content")
    private String content;

    @JsonProperty("user_ids")
    private Set<String> userIds;

    @JsonProperty("first_appearance_date")
    private Date firstAppearanceDate;
}

package com.howtank.streams.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class Reaction {
    @SerializedName("content")
    private String content;

    @SerializedName("user_ids")
    private Set<String> userIds;

    @SerializedName("first_appearance_date")
    private Date firstAppearanceDate;
}

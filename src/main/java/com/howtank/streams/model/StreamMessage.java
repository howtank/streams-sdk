package com.howtank.streams.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StreamMessage {
    @SerializedName("id")
    String id;

    @SerializedName("publication_timestamp")
    long publicationTimestamp;

    @SerializedName("publication_state")
    String publicationState;

    @SerializedName("local_id")
    long localId;

    @SerializedName("type")
    String type;

    @SerializedName("pinned")
    boolean pinned;

    @SerializedName("quoted_count")
    int quotesCount;

    @SerializedName("content")
    String content;

    @SerializedName("subject")
    String subject;

    @SerializedName("url")
    String url;

    @SerializedName("chat_id")
    String chatId;

    @SerializedName("level")
    Integer level;

    @SerializedName("reactions")
    List<Reaction> reactions;

    @SerializedName("quoted_message")
    StreamMessage quotedMessage;

    @SerializedName("user_id")
    String userId;

    @SerializedName("user_display_name")
    String userDisplayName;

    @SerializedName("user_expert_type")
    String userType;

    @SerializedName("user_photo_key")
    String userPhotoKey;

    @SerializedName("howtank_team")
    boolean howtankTeam;

    @SerializedName("source_host_id")
    String sourceHostId;

    @SerializedName("source_partner_id")
    String sourcePartnerId;

    @SerializedName("user_anon")
    boolean anon;
}

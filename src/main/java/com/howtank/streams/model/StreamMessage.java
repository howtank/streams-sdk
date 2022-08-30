package com.howtank.streams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StreamMessage {
    @JsonProperty("id")
    String id;

    @JsonProperty("publication_timestamp")
    long publicationTimestamp;

    @JsonProperty("publication_state")
    String publicationState;

    @JsonProperty("local_id")
    long localId;

    @JsonProperty("type")
    String type;

    @JsonProperty("pinned")
    boolean pinned;

    @JsonProperty("quoted_count")
    int quotesCount;

    @JsonProperty("content")
    String content;

    @JsonProperty("subject")
    String subject;

    @JsonProperty("url")
    String url;

    @JsonProperty("chat_id")
    String chatId;

    @JsonProperty("level")
    Integer level;

    @JsonProperty("reactions")
    List<Reaction> reactions;

    @JsonProperty("quoted_message")
    StreamMessage quotedMessage;

    @JsonProperty("user_id")
    String userId;

    @JsonProperty("user_display_name")
    String userDisplayName;

    @JsonProperty("user_expert_type")
    String userType;

    @JsonProperty("user_photo_key")
    String userPhotoKey;

    @JsonProperty("howtank_team")
    boolean howtankTeam;

    @JsonProperty("source_host_id")
    String sourceHostId;

    @JsonProperty("source_partner_id")
    String sourcePartnerId;

    @JsonProperty("user_anon")
    boolean anon;
}

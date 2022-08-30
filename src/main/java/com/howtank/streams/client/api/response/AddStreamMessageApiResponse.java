package com.howtank.streams.client.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddStreamMessageApiResponse extends ApiResponse{
    @JsonProperty("stream_publication_timestamp")
    private Long streamPublicationTimestamp;

    @JsonProperty("publication_timestamp")
    private Long publicationTimestamp;

    @JsonProperty("stream_id")
    private String streamId;

    @JsonProperty("channel_id")
    private String channelId;
}

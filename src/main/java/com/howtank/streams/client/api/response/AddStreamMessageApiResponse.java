package com.howtank.streams.client.api.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AddStreamMessageApiResponse extends ApiResponse{
    @SerializedName("stream_publication_timestamp")
    private Long streamPublicationTimestamp;

    @SerializedName("publication_timestamp")
    private Long publicationTimestamp;

    @SerializedName("stream_id")
    private String streamId;

    @SerializedName("channel_id")
    private String channelId;
}

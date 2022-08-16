package com.howtank.streams.client.api.response;

import com.howtank.streams.model.StreamMessage;
import lombok.Data;

import java.util.List;

@Data
public class GetStreamMessagesApiResponse extends ApiResponse{
    private List<StreamMessage> messages;
    private Integer count;
    private String timestamp;
    private Boolean last;
}

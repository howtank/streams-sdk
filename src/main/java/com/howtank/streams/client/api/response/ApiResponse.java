package com.howtank.streams.client.api.response;

import lombok.Data;

@Data
public class ApiResponse {
    private boolean accepted;
    private String reason;
    private String error;
}

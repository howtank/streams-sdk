package com.howtank.streams.client.exception;

import lombok.Getter;

@Getter
public class HowtankApiException extends Exception {
    private final String reason;

    public HowtankApiException(String error) {
        this(error, null);
    }

    public HowtankApiException(String error, String reason) {
        super(error);
        this.reason = reason;
    }

    public HowtankApiException(Throwable cause) {
        super(cause);
        this.reason = null;
    }
}

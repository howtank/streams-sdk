package com.howtank.streams.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.howtank.streams.client.api.CommandApiClient;
import com.howtank.streams.client.api.response.GetUserStreamsApiResponse;
import com.howtank.streams.client.bean.SearchStreamsFilter;
import com.howtank.streams.client.exception.HowtankApiException;
import com.howtank.streams.model.Presence;
import com.howtank.streams.model.Stream;
import com.howtank.streams.model.StreamMessage;
import lombok.Getter;
import lombok.NonNull;
import org.apache.http.NameValuePair;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.howtank.streams.client.api.CommandType.GET_USER_STREAMS;

public class StreamClientHttp implements StreamClient {
    private final String HTTP_SCHEME = "http://";
    private final String HTTPS_SCHEME = "https://";

    @Getter
    private final String currentUserId;
    private final CommandApiClient client;
    private final Gson gson;

    StreamClientHttp(
            @NonNull final String currentUserId,
            @NonNull final String endpoint,
            @NonNull final String userAgent,
            @NonNull final String accessToken) {
        this.currentUserId = currentUserId;

        String endpointWithSchema;
        if (!endpoint.startsWith(HTTPS_SCHEME)) {
            if (endpoint.startsWith(HTTP_SCHEME)) {
                endpointWithSchema = HTTPS_SCHEME + endpoint.substring(7);
            } else {
                endpointWithSchema = HTTPS_SCHEME + endpoint;
            }
        } else {
            endpointWithSchema = endpoint;
        }

        this.client = new CommandApiClient(endpointWithSchema, userAgent, accessToken);

        this.gson = new GsonBuilder()
                .create();
    }

    @Override
    public List<Stream> getCurrentUserStreams() throws HowtankApiException {
        String response = this.client.performGet(GET_USER_STREAMS, Collections.<NameValuePair>emptyList());

        GetUserStreamsApiResponse getUserStreamsApiResponse = gson.fromJson(response, GetUserStreamsApiResponse.class);

        if (getUserStreamsApiResponse.isAccepted()) {
            return getUserStreamsApiResponse.getStreams();
        } else {
            throw new HowtankApiException(getUserStreamsApiResponse.getError(), getUserStreamsApiResponse.getReason());
        }
    }

    @Override
    public List<Stream> searchStreams(SearchStreamsFilter searchStreamsFilter) {
        return null;
    }

    @Override
    public List<Presence> getPresence(String streamId) {
        return null;
    }

    @Override
    public List<StreamMessage> getStreamMessages(String streamId, int count) {
        return null;
    }

    @Override
    public List<StreamMessage> getStreamMessagesOlderThanDate(String streamId, Date referenceDate, int count) {
        return null;
    }

    @Override
    public List<StreamMessage> getStreamMessagesNewerThanDate(String streamId, Date referenceDate, int count) {
        return null;
    }

    @Override
    public List<StreamMessage> getStreamMessagesAroundDate(String streamId, Date referenceDate, int count) {
        return null;
    }

    @Override
    public void publishStreamMessage(String streamId, String content) {

    }

    @Override
    public void pushStreamMessageNotification(String streamId, String content) {

    }
}
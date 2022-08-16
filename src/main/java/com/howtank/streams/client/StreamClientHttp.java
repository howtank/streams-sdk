package com.howtank.streams.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.howtank.streams.client.api.CommandApiClient;
import com.howtank.streams.client.api.CommandType;
import com.howtank.streams.client.api.RequestParameters;
import com.howtank.streams.client.api.response.*;
import com.howtank.streams.client.bean.SearchStreamsFilter;
import com.howtank.streams.client.exception.HowtankApiException;
import com.howtank.streams.model.Presence;
import com.howtank.streams.model.Stream;
import com.howtank.streams.model.StreamMessage;
import lombok.Getter;
import lombok.NonNull;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.*;

import static com.howtank.streams.client.api.CommandType.*;

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
        GetUserStreamsApiResponse getUserStreamsApiResponse = performGet(GET_USER_STREAMS, GetUserStreamsApiResponse.class, Collections.emptyList());

        if (!getUserStreamsApiResponse.isAccepted()) {
            throw new HowtankApiException(getUserStreamsApiResponse.getError(), getUserStreamsApiResponse.getReason());
        }
        return getUserStreamsApiResponse.getStreams();
    }

    @Override
    public List<Stream> searchStreams(SearchStreamsFilter searchStreamsFilter) throws HowtankApiException {
        Objects.requireNonNull(searchStreamsFilter);
        List<NameValuePair> params = RequestParams.builder()
                .addIfPresent(RequestParameters.KEYWORD, searchStreamsFilter.getKeyword())
                .addIfPresent(RequestParameters.FROM, searchStreamsFilter.getFrom())
                .addIfPresent(RequestParameters.SIZE, searchStreamsFilter.getSize())
                .addIfPresent(RequestParameters.DISCOVERABLE, searchStreamsFilter.getDiscoverable())
                .addIfPresent(RequestParameters.SUBSCRIBED, searchStreamsFilter.getSubscribed())
                .build();

        GetStreamsApiResponse getStreamsApiResponse = performGet(GET_STREAMS, GetStreamsApiResponse.class, params);

        validateAcceptedResponse(getStreamsApiResponse);
        return getStreamsApiResponse.getStreams();
    }


    @Override
    public List<Presence> getPresence(String streamId) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        List<NameValuePair> params = RequestParams.builder()
                .addIfPresent(RequestParameters.STREAM_ID, streamId)
                .build();

        GetPresenceApiResponse getPresenceApiResponse = performGet(GET_PRESENCE, GetPresenceApiResponse.class, params);

        validateAcceptedResponse(getPresenceApiResponse);
        return getPresenceApiResponse.getPresences();
    }

    @Override
    public List<StreamMessage> getStreamMessages(String streamId, int count) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        validateCountGreaterThanZero(count);
        return getStreamMessages(streamId, null, count, "older");
    }

    @Override
    public List<StreamMessage> getStreamMessagesOlderThanDate(String streamId, Date referenceDate, int count) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        Objects.requireNonNull(referenceDate);
        validateCountGreaterThanZero(count);
        return getStreamMessages(streamId, referenceDate.getTime(), count, "older");
    }

    @Override
    public List<StreamMessage> getStreamMessagesNewerThanDate(String streamId, Date referenceDate, int count) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        Objects.requireNonNull(referenceDate);
        validateCountGreaterThanZero(count);
        return getStreamMessages(streamId, referenceDate.getTime(), count, "newer");
    }

    @Override
    public List<StreamMessage> getStreamMessagesAroundDate(String streamId, Date referenceDate, int count) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        Objects.requireNonNull(referenceDate);
        validateCountGreaterThanZero(count);
        return getStreamMessages(streamId, referenceDate.getTime(), count, "both");
    }

    @Override
    public void publishStreamMessage(String streamId, String content) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        Objects.requireNonNull(content);
        List<NameValuePair> params = RequestParams.builder()
                .addIfPresent(RequestParameters.STREAM_ID, streamId)
                .addIfPresent(RequestParameters.CONTENT, content)
                .addIfPresent(RequestParameters.LOCAL_ID, (new Date()).getTime())
                .addIfPresent(RequestParameters.TYPE, "group_chat")
                .build();

        AddStreamMessageApiResponse response = performGet(ADD_STREAM_MESSAGE, AddStreamMessageApiResponse.class, params);
        validateAcceptedResponse(response);
    }

    @Override
    public void pushStreamMessageNotification(String streamId, String content) throws HowtankApiException {
        Objects.requireNonNull(streamId);
        Objects.requireNonNull(content);
        List<NameValuePair> params = RequestParams.builder()
                .addIfPresent(RequestParameters.STREAM_ID, streamId)
                .addIfPresent(RequestParameters.CONTENT, content)
                .addIfPresent(RequestParameters.LOCAL_ID, (new Date()).getTime())
                .addIfPresent(RequestParameters.TYPE, "notification")
                .build();

        AddStreamMessageApiResponse response = performGet(PUSH_STREAM_MESSAGE, AddStreamMessageApiResponse.class, params);
        validateAcceptedResponse(response);
    }

    private void validateCountGreaterThanZero(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count should be greater than 0");
        }
    }

    private void validateAcceptedResponse(ApiResponse response) throws HowtankApiException {
        if (!response.isAccepted()) {
            throw new HowtankApiException(response.getError(), response.getReason());
        }
    }

    private List<StreamMessage> getStreamMessages(String streamId, Long timestamp, int count, String direction) throws HowtankApiException {
        List<NameValuePair> params = RequestParams.builder()
                .addIfPresent(RequestParameters.STREAM_ID, streamId)
                .addIfPresent(RequestParameters.DIRECTION, direction)
                .addIfPresent(RequestParameters.TIMESTAMP, timestamp)
                .addIfPresent(RequestParameters.COUNT, count)
                .build();
        GetStreamMessagesApiResponse getStreamMessagesApiResponse = performGet(GET_STREAM_MESSAGES, GetStreamMessagesApiResponse.class, params);

        validateAcceptedResponse(getStreamMessagesApiResponse);
        return getStreamMessagesApiResponse.getMessages();
    }

    private <T> T performGet(CommandType commandType, Type typeOfT, List<NameValuePair> params) throws HowtankApiException {
        String response = this.client.performGet(commandType, params);
        return gson.fromJson(response, typeOfT);
    }
}

class RequestParams {
    List<NameValuePair> params;

    private RequestParams() {
        params = new ArrayList<>();
    }

    static RequestParams builder() {
        return new RequestParams();
    }

    <T> RequestParams addIfPresent(String key, T value) {
        if (value != null) {
            params.add(new BasicNameValuePair(key, value.toString()));
        }
        return this;
    }

    List<NameValuePair> build() {
        return params;
    }
}
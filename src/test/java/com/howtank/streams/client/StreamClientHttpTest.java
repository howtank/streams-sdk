package com.howtank.streams.client;

import com.howtank.streams.FileLoader;
import com.howtank.streams.client.api.CommandType;
import com.howtank.streams.client.api.RequestParameters;
import com.howtank.streams.client.bean.ClientProperties;
import com.howtank.streams.client.bean.SearchStreamsFilter;
import com.howtank.streams.client.exception.HowtankApiException;
import com.howtank.streams.model.Presence;
import com.howtank.streams.model.Stream;
import com.howtank.streams.model.StreamMessage;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mock.Expectation;
import org.mockserver.model.*;
import org.mockserver.socket.tls.KeyStoreFactory;
import org.mockserver.verify.VerificationTimes;

import javax.net.ssl.HttpsURLConnection;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.howtank.streams.client.api.CommandType.GET_STREAM_MESSAGES;

@ExtendWith(MockServerExtension.class)
@MockServerSettings(ports = {7777})
public class StreamClientHttpTest {

    private static final String ACCESS_TOKEN = "fake-access-token";

    private static final String INVALID_ACCESS_TOKEN = "fake-invalid-access-token";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:102.0) Gecko/20100101 Firefox/102.0";
    private static final String CURRENT_USER_ID = "fake-user-id";
    private  static final String ENDPOINT = "localhost:7777";

    private static final String STREAM_ID = "stream-id-test";

    private final ClientAndServer client;
    private final FileLoader fileLoader;

    private final StreamClient streamClient;

    public StreamClientHttpTest(ClientAndServer client) {
        HttpsURLConnection.setDefaultSSLSocketFactory(new KeyStoreFactory(new MockServerLogger()).sslContext().getSocketFactory());
        this.client = client;
        fileLoader = new FileLoader(StreamClientHttpTest.class);
        streamClient =   new StreamClientBuilder()
                .build(ClientProperties.builder()
                        .accessToken(ACCESS_TOKEN)
                        .currentUserId(CURRENT_USER_ID)
                        .userAgent(USER_AGENT)
                        .endpoint(ENDPOINT)
                        .build());
    }

    @Test
    public void testInvalidToken() {
        StreamClient streamClient2 = new StreamClientBuilder()
                .build(ClientProperties.builder()
                        .accessToken(INVALID_ACCESS_TOKEN)
                        .currentUserId(CURRENT_USER_ID)
                        .userAgent(USER_AGENT)
                        .endpoint(ENDPOINT)
                        .build());
        registerInvalidTokenExpectation(CommandType.GET_USER_STREAMS, null);

        Assertions.assertThrows(HowtankApiException.class, streamClient2::getCurrentUserStreams);
    }

    @Test
    public void testGetUserStreams() throws HowtankApiException {
        registerExpectation(CommandType.GET_USER_STREAMS, null, "user_streams..json");
        List<Stream> streams = streamClient.getCurrentUserStreams();
        Assertions.assertNotNull(streams);
        Assertions.assertEquals(11, streams.size());
        
        //verify stream
        Stream egyptStream = streams.stream().filter(s -> s.getId().equals("egypt")).findFirst().orElse(null);
        Assertions.assertNotNull(egyptStream);
        Assertions.assertEquals("Alexandria", egyptStream.getTitle());
        Assertions.assertEquals("Alexandrie, Alexandra.", egyptStream.getDescription());
        Assertions.assertEquals("private-local.stream.egypt", egyptStream.getChannelId());
        Assertions.assertEquals("private-local.presence.stream-egypt", egyptStream.getPresenceId());
        Assertions.assertEquals("000000", egyptStream.getFirstStreamColor());
        Assertions.assertEquals("F2F2F2", egyptStream.getSecondStreamColor());
        Assertions.assertNotNull(egyptStream.getAvailableMessageTypes());
        Assertions.assertEquals(7, egyptStream.getAvailableMessageTypes().size());
    }

    @Test
    public void testSearchStream() throws HowtankApiException {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.SIZE, "2"));
        params.add(new Parameter(RequestParameters.DISCOVERABLE, "true"));
        registerExpectation(CommandType.GET_STREAMS, params, "filtered_user_streams.json");

        SearchStreamsFilter filter = SearchStreamsFilter.builder()
                .discoverable(true)
                .size(2)
                .build();
        List<Stream> streams = streamClient.searchStreams(filter);

        Assertions.assertNotNull(streams);
        Assertions.assertEquals(1, streams.size());

        Stream stream = streams.get(0);
        Assertions.assertEquals("Primary stream of Team howtank", stream.getDescription());
        Assertions.assertEquals("private-local.presence.stream-howtank", stream.getPresenceId());
        Assertions.assertEquals("Team howtank", stream.getTitle());
        Assertions.assertTrue(stream.isDiscoverable());
        Assertions.assertEquals("howtank", stream.getId());
        Assertions.assertEquals("1FA2FF", stream.getFirstStreamColor());
        Assertions.assertEquals("private-local.stream.howtank", stream.getChannelId());
        Assertions.assertEquals("5662E8", stream.getSecondStreamColor());
    }
    
    @Test
    public void testGetStreamMessage() throws HowtankApiException {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));
        params.add(new Parameter(RequestParameters.DIRECTION, "older"));
        params.add(new Parameter(RequestParameters.COUNT, "50"));
        registerExpectation(GET_STREAM_MESSAGES, params, "stream_messages.json");
        List<StreamMessage> streamMessages = streamClient.getStreamMessages(STREAM_ID, 50);
        Assertions.assertNotNull(streamMessages);
        Assertions.assertEquals(5, streamMessages.size());

        StreamMessage streamMessage = streamMessages.stream().filter(s -> s.getPublicationTimestamp() == 1660622912089L).findFirst().orElse(null);
        Assertions.assertNotNull(streamMessage);

        verifyStreamMessageAt1660622912089L(streamMessage);
    }


    @Test
    public void testGetStreamMessagesNewerThanSpecificDate() throws HowtankApiException {
        Date date = Date.from(Instant.ofEpochMilli(1660579163449L));
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));
        params.add(new Parameter(RequestParameters.DIRECTION, "newer"));
        params.add(new Parameter(RequestParameters.COUNT, "50"));
        params.add(new Parameter(RequestParameters.TIMESTAMP, String.valueOf(date.getTime())));
        registerExpectation(GET_STREAM_MESSAGES, params, "stream_messages_newer_than_1660579413770.json");

        List<StreamMessage> streamMessages = streamClient.getStreamMessagesNewerThanDate(STREAM_ID, date, 50);
        Assertions.assertNotNull(streamMessages);
        Assertions.assertEquals(2, streamMessages.size());

        StreamMessage streamMessage = streamMessages.stream().filter(s -> s.getPublicationTimestamp() == 1660622912089L).findFirst().orElse(null);
        Assertions.assertNotNull(streamMessage);

        verifyStreamMessageAt1660622912089L(streamMessage);
    }

    @Test
    public void testGetStreamMessagesOlderThanSpecificDate() throws HowtankApiException {
        Date date = Date.from(Instant.ofEpochMilli(1660579163449L));
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));
        params.add(new Parameter(RequestParameters.DIRECTION, "older"));
        params.add(new Parameter(RequestParameters.COUNT, "20"));
        params.add(new Parameter(RequestParameters.TIMESTAMP, String.valueOf(date.getTime())));
        registerExpectation(GET_STREAM_MESSAGES, params, "stream_messages_older_than_1660579413770.json");

        List<StreamMessage> streamMessages = streamClient.getStreamMessagesOlderThanDate(STREAM_ID, date, 20);
        Assertions.assertNotNull(streamMessages);
        Assertions.assertEquals(3, streamMessages.size());

        StreamMessage streamMessage = streamMessages.stream().filter(s -> s.getPublicationTimestamp() == 1660579163449L).findFirst().orElse(null);
        Assertions.assertNotNull(streamMessage);

        verifyStreamMessageAt1660579163449L(streamMessage);
    }

    @Test
    public void testGetStreamMessagesAroundSpecificDate() throws HowtankApiException {
        Date date = Date.from(Instant.ofEpochMilli(1660579163449L));
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));
        params.add(new Parameter(RequestParameters.DIRECTION, "both"));
        params.add(new Parameter(RequestParameters.COUNT, "50"));
        params.add(new Parameter(RequestParameters.TIMESTAMP, String.valueOf(date.getTime())));
        registerExpectation(GET_STREAM_MESSAGES, params,  "stream_messages.json");

        List<StreamMessage> streamMessages = streamClient.getStreamMessagesAroundDate(STREAM_ID, date, 50);
        Assertions.assertNotNull(streamMessages);
        Assertions.assertEquals(5, streamMessages.size());

        StreamMessage streamMessage = streamMessages.stream().filter(s -> s.getPublicationTimestamp() == 1660579163449L).findFirst().orElse(null);
        Assertions.assertNotNull(streamMessage);
        verifyStreamMessageAt1660579163449L(streamMessage);

        StreamMessage streamMessage2 = streamMessages.stream().filter(s -> s.getPublicationTimestamp() == 1660622912089L).findFirst().orElse(null);
        Assertions.assertNotNull(streamMessage2);
        verifyStreamMessageAt1660622912089L(streamMessage2);
    }
    
    @Test
    public void testGetPresences() throws HowtankApiException {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));

        registerExpectation(CommandType.GET_PRESENCE, params,  "presences.json");

        List<Presence> presences = streamClient.getPresence(STREAM_ID);
        Assertions.assertNotNull(presences);
        Assertions.assertEquals(1, presences.size());

        Presence presence = presences.get(0);
        Assertions.assertEquals("1231131312dwqw", presence.getUser().getId());
        Assertions.assertEquals("ADMIN", presence.getUser().getDisplayName());
        Assertions.assertEquals(1661304051000L, presence.getConnectionDate().getTime());
        Assertions.assertEquals(0L, presence.getUser().getPoints());
    }

    @Test
    public void testPublishStreamMessage() throws HowtankApiException {
        String content = "hello";
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));
        params.add(new Parameter(RequestParameters.CONTENT, content));
        params.add(new Parameter(RequestParameters.TYPE, "group_chat"));
        params.add(new Parameter(RequestParameters.LOCAL_ID, "[0-9]+"));

        String requestId = registerExpectation(CommandType.ADD_STREAM_MESSAGE, params,  "push_stream_message_response.json");

        streamClient.publishStreamMessage(STREAM_ID, content);

        client.verify(
                requestId,
                VerificationTimes.once()
        );
    }


    @Test
    public void testPushStreamMessageNotification() throws HowtankApiException {
        String content = "A new note";
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter(RequestParameters.STREAM_ID, STREAM_ID));
        params.add(new Parameter(RequestParameters.CONTENT, content));
        params.add(new Parameter(RequestParameters.TYPE, "notification"));
        params.add(new Parameter(RequestParameters.LOCAL_ID, "[0-9]+"));

        String requestId = registerExpectation(CommandType.PUSH_STREAM_MESSAGE, params, "push_new_notification_response.json");

        streamClient.pushStreamMessageNotification(STREAM_ID, content);

        client.verify(
                requestId,
                VerificationTimes.once()
        );
    }


    private void verifyStreamMessageAt1660579163449L(StreamMessage streamMessage) {
        Assertions.assertEquals("Alecs", streamMessage.getUserDisplayName());
        Assertions.assertEquals("f7008d63a09111e7bec9dca904889ab11c64a5ea", streamMessage.getUserId());
        Assertions.assertEquals("test2", streamMessage.getContent());
        Assertions.assertEquals("0d936d4ffdcb11e2afeb22000aa8245f5d071c7c", streamMessage.getSourcePartnerId());
        Assertions.assertEquals("group_chat", streamMessage.getType());
        Assertions.assertEquals("support",streamMessage.getUserType());
        Assertions.assertEquals("howtank", streamMessage.getSourceHostId());
    }

    private void verifyStreamMessageAt1660622912089L(StreamMessage streamMessage) {
        Assertions.assertEquals("Alecs", streamMessage.getUserDisplayName());
        Assertions.assertEquals("f7008d63a09111e7bec9dca904889ab11c64a5ea", streamMessage.getUserId());
        Assertions.assertEquals("noti1", streamMessage.getContent());
        Assertions.assertEquals("0d936d4ffdcb11e2afeb22000aa8245f5d071c7c", streamMessage.getSourcePartnerId());
        Assertions.assertEquals("notification", streamMessage.getType());
        Assertions.assertEquals("support",streamMessage.getUserType());
        Assertions.assertEquals("howtank", streamMessage.getSourceHostId());
    }

    private String registerExpectation(CommandType commandType,  List<Parameter> requestParams, String responseFileName) {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("command", commandType.getName()));
        params.add(new Parameter("mode", "expert"));
        if (requestParams != null) {
            params.addAll(requestParams);
        }

        List<Header> headers = new ArrayList<>();
        headers.add(new Header(HttpHeaders.CONTENT_TYPE, "application/json"));
        headers.add(new Header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN));

        String responseBody = fileLoader.loadFile(responseFileName);
        Expectation[] expectation = client.when(
                new HttpRequest()
                        .withMethod("GET")
                        .withPath("/api/v4")
                        .withQueryStringParameters(params)
                        .withHeaders(headers)
        ).respond(HttpResponse.response()
                .withStatusCode(200)
                .withBody(responseBody, MediaType.APPLICATION_JSON)
        );

        return expectation[0].getId();
    }

    private void registerInvalidTokenExpectation(CommandType commandType,  List<Parameter> requestParams) {
        List<Parameter> params = new ArrayList<>();
        params.add(new Parameter("command", commandType.getName()));
        params.add(new Parameter("mode", "expert"));
        if (requestParams != null) {
            params.addAll(requestParams);
        }

        List<Header> headers = new ArrayList<>();
        headers.add(new Header(HttpHeaders.CONTENT_TYPE, "application/json"));
        headers.add(new Header(HttpHeaders.AUTHORIZATION, "Bearer " + INVALID_ACCESS_TOKEN));

        String responseBody = fileLoader.loadFile("invalid_token_response.json");
        Expectation[] expectation = client.when(
                new HttpRequest()
                        .withMethod("GET")
                        .withPath("/api/v4")
                        .withQueryStringParameters(params)
                        .withHeaders(headers)
        ).respond(HttpResponse.response()
                .withStatusCode(200)
                .withBody(responseBody, MediaType.APPLICATION_JSON)
        );
    }
}
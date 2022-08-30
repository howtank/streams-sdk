package com.howtank.streams.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtank.streams.client.exception.HowtankApiException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Log
public class CommandApiClient implements Closeable {
    private static final int API_VERSION = 4;
    private static final String basePath = "/api/v" + API_VERSION;
    private static final int MAX_TOTAL_CONNECTIONS = 10;

    private final String urlPath;
    private final String userAgent;
    private final String accessToken;

    private final ObjectMapper mapper;
    private final PoolingHttpClientConnectionManager poolingConnManager;

    public CommandApiClient(@NonNull String baseUrl,
                            @NonNull String userAgent,
                            @NonNull String accessToken,
                            @NonNull ObjectMapper mapper) {
        this(baseUrl, userAgent, accessToken, mapper, MAX_TOTAL_CONNECTIONS);
    }

    public CommandApiClient(@NonNull String baseUrl,
                            @NonNull String userAgent,
                            @NonNull String accessToken,
                            @NonNull ObjectMapper mapper,
                            int maxConnections) {
        this.urlPath = baseUrl + basePath;
        this.userAgent = userAgent;
        this.accessToken = accessToken;
        this.mapper = mapper;

        this.poolingConnManager = new PoolingHttpClientConnectionManager();
        this.poolingConnManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        this.poolingConnManager.setDefaultMaxPerRoute(MAX_TOTAL_CONNECTIONS);
    }

    @SneakyThrows
    public void close() {
        this.poolingConnManager.close();
    }

    public <T> T performGet(@NonNull CommandType command, @NonNull List<NameValuePair> params, Class<T> responseClazz) throws HowtankApiException {
        return this.performGet(command, "expert", params, responseClazz);
    }

    @SneakyThrows(URISyntaxException.class)
    public <T> T performGet(@NonNull CommandType command, @NonNull String mode, @NonNull List<NameValuePair> params, Class<T> responseClazz)
            throws HowtankApiException {

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        headers.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("command", command.getName());
        uriBuilder.addParameter("mode", mode);
        uriBuilder.addParameters(params);
        HttpUriRequest request = RequestBuilder.get()
                .setUri(this.urlPath + "?" + uriBuilder.build().getRawQuery())
                .build();

        try (CloseableHttpClient httpClient = getHttpClient(headers);
             CloseableHttpResponse response = httpClient.execute(request)) {
            return mapper.readValue(response.getEntity().getContent(), responseClazz);
        } catch (IOException e) {
            throw new HowtankApiException(e);
        }
    }

    private CloseableHttpClient getHttpClient(List<Header> headers) {
        return HttpClientBuilder
                .create()
                .setUserAgent(userAgent)
                .setMaxConnTotal(MAX_TOTAL_CONNECTIONS)
                .setDefaultHeaders(headers)
                .build();
    }
}

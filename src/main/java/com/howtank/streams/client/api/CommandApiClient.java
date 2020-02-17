package com.howtank.streams.client.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.howtank.streams.client.exception.HowtankApiException;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

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

    private final PoolingHttpClientConnectionManager poolingConnManager;

    public CommandApiClient(@NonNull String baseUrl, @NonNull String userAgent, @NonNull String accessToken) {
        this(baseUrl, userAgent, accessToken, MAX_TOTAL_CONNECTIONS);
    }

    public CommandApiClient(@NonNull String baseUrl, @NonNull String userAgent, @NonNull String accessToken, int maxConnections) {
        this.urlPath = baseUrl + basePath;
        this.userAgent = userAgent;
        this.accessToken = accessToken;

        this.poolingConnManager = new PoolingHttpClientConnectionManager();
        this.poolingConnManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        this.poolingConnManager.setDefaultMaxPerRoute(MAX_TOTAL_CONNECTIONS);
    }

    @SneakyThrows
    public void close() {
        this.poolingConnManager.close();
    }

    public String performGet(@NonNull CommandType command, @NonNull List<NameValuePair> params) throws HowtankApiException {
        return this.performGet(command, "expert", params);
    }

    @SneakyThrows(URISyntaxException.class)
    public String performGet(@NonNull CommandType command, @NonNull String mode, @NonNull List<NameValuePair> params)
            throws HowtankApiException {

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
        headers.add(new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));

        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setUserAgent(userAgent)
                .setMaxConnTotal(MAX_TOTAL_CONNECTIONS)
                .setDefaultHeaders(headers)
                .build();

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("command", command.getName());
        uriBuilder.addParameter("mode", mode);
        uriBuilder.addParameters(params);

        HttpUriRequest request = RequestBuilder.get()
                    .setUri(this.urlPath + "?" + uriBuilder.build().getRawQuery())
                    .build();

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return this.parseResponse(response);
        } catch (IOException e) {
            throw new HowtankApiException(e);
        }
    }

    private String parseResponse(HttpResponse response) throws HowtankApiException {
        try {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new HowtankApiException(e);
        }
    }
}

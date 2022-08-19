package com.howtank.streams.client;

import com.howtank.streams.client.bean.SearchStreamsFilter;
import com.howtank.streams.client.exception.HowtankApiException;
import com.howtank.streams.model.Presence;
import com.howtank.streams.model.Stream;
import com.howtank.streams.model.StreamMessage;

import java.util.Date;
import java.util.List;

public interface StreamClient {
    String getCurrentUserId();
    List<Stream> getCurrentUserStreams() throws HowtankApiException;
    List<Stream> searchStreams(SearchStreamsFilter searchStreamsFilter) throws HowtankApiException;
    List<Presence> getPresence(String streamId) throws HowtankApiException;

    List<StreamMessage> getStreamMessages(String streamId, int count) throws HowtankApiException;
    List<StreamMessage> getStreamMessagesOlderThanDate(String streamId, Date referenceDate, int count) throws HowtankApiException;
    List<StreamMessage> getStreamMessagesNewerThanDate(String streamId, Date referenceDate, int count) throws HowtankApiException;
    List<StreamMessage> getStreamMessagesAroundDate(String streamId, Date referenceDate, int count) throws HowtankApiException;

    void publishStreamMessage(String streamId, String content) throws HowtankApiException;
    void pushStreamMessageNotification(String streamId, String content) throws HowtankApiException;
}

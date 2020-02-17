package com.howtank.streams.client;

import com.howtank.streams.model.Presence;
import com.howtank.streams.model.Stream;
import com.howtank.streams.model.StreamMessage;

import java.util.Date;
import java.util.List;

public interface StreamClient {
    String getCurrentUserId();
    List<Stream> getCurrentUserStreams();
    List<Stream> searchStreams(SearchStreamsFilter searchStreamsFilter);
    List<Presence> getPresence(String streamId);

    List<StreamMessage> getStreamMessages(String streamId, int count);
    List<StreamMessage> getStreamMessagesOlderThanDate(String streamId, Date referenceDate, int count);
    List<StreamMessage> getStreamMessagesNewerThanDate(String streamId, Date referenceDate, int count);
    List<StreamMessage> getStreamMessagesAroundDate(String streamId, Date referenceDate, int count);

    void publishStreamMessage(String streamId, String content);
    void pushStreamMessageNotification(String streamId, String content);
}

package com.sunhacks.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "eventshort")
public class EventShort {
    @Id
    private String eventName;
    private String eventRating;

    public String getEventName() {
        return eventName;
    }

    public String getEventRating() {
        return eventRating;
    }

    public void setEventRating(String eventRating) {
        this.eventRating = eventRating;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}

package com.sunhacks.models;


import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Key implements Serializable {
    public String getUsername() {
        return username;
    }

    public String getEventName() {
        return eventName;
    }

    @Field(order = 1)
    private final @NotNull String username;
    @Field(order = 2)
    private final @NotNull String eventName;
    public Key(@NotNull String username, @NotNull String eventName) {
        this.username = username;
        this.eventName = eventName;
    }
}

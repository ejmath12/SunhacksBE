package com.sunhacks.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "event_classifier")
public class EventClassifier {
    @Id
    private String genreKey;
    private String genreName;

    public int getIntegerMapper() {
        return integerMapper;
    }

    public void setIntegerMapper(int integerMapper) {
        this.integerMapper = integerMapper;
    }

    public String getGenreKey() {
        return genreKey;
    }

    public void setGenreKey(String genreKey) {
        this.genreKey = genreKey;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    private int integerMapper;
}

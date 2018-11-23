package com.sunhacks.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "predictions")
public class Prediction {
    @Id
    private Key id;

    public Key getId() {
        return id;
    }

    public void setId(Key id) {
        this.id = id;
    }

    public float getPredictedRating() {
        return predictedRating;
    }

    public void setPredictedRating(float predictedRating) {
        this.predictedRating = predictedRating;
    }

    private float predictedRating;
}

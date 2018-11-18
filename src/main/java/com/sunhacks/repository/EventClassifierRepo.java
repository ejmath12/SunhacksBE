package com.sunhacks.repository;


import com.sunhacks.models.Event;
import com.sunhacks.models.EventClassifier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventClassifierRepo extends MongoRepository<EventClassifier, String> {
    public EventClassifier findByIntegerMapper(int integerMapper);
}

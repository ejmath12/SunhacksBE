package com.sunhacks.repository;

import com.sunhacks.models.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findAllByUsername(String username);
    List<Event> findAllByIsRated(Boolean rated);

}
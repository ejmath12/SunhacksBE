package com.sunhacks.repository;

import com.sunhacks.models.Events;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Events, String> {
}
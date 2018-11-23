package com.sunhacks.repository;

import com.sunhacks.models.Prediction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;



public interface PredictionRepo extends MongoRepository<Prediction, String> {

}
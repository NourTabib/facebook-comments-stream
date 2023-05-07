package com.example.kafkaStreamProcessing.repository;

import com.example.kafkaStreamProcessing.entity.wordCountsResults;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface wordCountsRepository extends MongoRepository<wordCountsResults, String> {
}

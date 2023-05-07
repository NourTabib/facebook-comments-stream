package com.example.kafkaStreamProcessing.repository;

import com.example.kafkaStreamProcessing.entity.Index;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface indexRepository extends MongoRepository<Index,Integer> {
    Index findFirstByOrderByTimestampsDesc();
    Index findFirstByTimestampsGreaterThanOrderByTimestamps(Date timestamp);
}

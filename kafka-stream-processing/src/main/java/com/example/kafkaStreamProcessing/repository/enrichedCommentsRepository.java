package com.example.kafkaStreamProcessing.repository;

import com.example.kafkaStreamProcessing.entity.EnrichedComment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface enrichedCommentsRepository extends MongoRepository<EnrichedComment,String> {
    List<EnrichedComment> findByTimestampsGreaterThanEqualOrderByTimestampsAsc(Date date);
}

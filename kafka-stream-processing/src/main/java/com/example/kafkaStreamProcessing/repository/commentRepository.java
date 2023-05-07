package com.example.kafkaStreamProcessing.repository;

import com.example.kafkaStreamProcessing.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.Optional;

public interface commentRepository extends MongoRepository<Comment,String> {
    Optional<Comment> findBy_id(String _id);
    Comment findFirstByOrderByTimestampsAsc();
    Comment findFirstByTimestampsGreaterThanOrderByTimestampsAsc(Date timestamps);
    Comment findByTimestamps(Date timestamps);
}

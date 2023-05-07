package com.example.kafkaStreamProcessing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class wordCountAggregationResult {
    @Id
    String word;
    Integer total;
}

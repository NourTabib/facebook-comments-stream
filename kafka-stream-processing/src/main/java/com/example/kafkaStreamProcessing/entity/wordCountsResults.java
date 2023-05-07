package com.example.kafkaStreamProcessing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "word_counts_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class wordCountsResults {
    @Id
    private String _id;
    @Indexed(unique = true)
    private String comment_id;
    @Indexed
    private String word;
    private Integer count;
    private Date timestamps;
}

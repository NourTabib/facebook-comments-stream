package com.example.kafkaStreamProcessing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "enriched_comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrichedComment {
    @Id
    private String _id;
    @Indexed(unique = true)
    private String comment_id;
    private double sentiment;
    private Date timestamps;
}

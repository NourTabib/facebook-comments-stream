package com.example.kafkaStreamProcessing.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "index")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Index {
    @Id
    private int _id;
    private String comment__id;
    private Date timestamps;
}

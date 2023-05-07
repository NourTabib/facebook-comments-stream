package com.example.kafkaStreamProcessing.analytics;

import com.example.kafkaStreamProcessing.entity.wordCountAggregationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

@Component
public class wordsCloudAnalytics {
    private final MongoTemplate mongoTemplate;
    @Autowired
    wordsCloudAnalytics(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }
    public List<wordCountAggregationResult> getWordsCount(Date startDate){
        Date currentDate = new Date();
        GroupOperation groupStage = group("word").sum("count").as("total");
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("timestamps").gte(startDate)),
                groupStage
        );
        AggregationResults<wordCountAggregationResult> result = mongoTemplate.aggregate(
                agg, "word_counts_results", wordCountAggregationResult.class);
        List<wordCountAggregationResult> list = new ArrayList<>();
        List<wordCountAggregationResult>  wordCountAggregationResultList = new LinkedList<wordCountAggregationResult>(result.getMappedResults());
        Iterator<wordCountAggregationResult> iterator = wordCountAggregationResultList.iterator();
        System.out.println(wordCountAggregationResultList.size());
        while (iterator.hasNext()) {
            wordCountAggregationResult item = iterator.next();
            if((item.getWord().length() > 3 && item.getTotal() >300) ){
                list.add(item);
            }
        }
        System.out.println(list.size());
        return list;
    }

}

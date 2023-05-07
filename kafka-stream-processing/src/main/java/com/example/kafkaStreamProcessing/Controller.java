package com.example.kafkaStreamProcessing;

import com.example.kafkaStreamProcessing.analytics.sentimentAnalysisAnalytics;
import com.example.kafkaStreamProcessing.analytics.wordsCloudAnalytics;
import com.example.kafkaStreamProcessing.entity.EnrichedComment;
import com.example.kafkaStreamProcessing.entity.wordCountAggregationResult;
import com.example.kafkaStreamProcessing.repository.enrichedCommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class Controller {
    private final sentimentAnalysisAnalytics sentimentAnalysisAnalytic;
    private final wordsCloudAnalytics wordsCloudAnalytics;
    private final enrichedCommentsRepository enrichedCommentsRepository;


    @Autowired
    public Controller(sentimentAnalysisAnalytics sentimentAnalysisAnalytics, wordsCloudAnalytics wordsCloudAnalytics, com.example.kafkaStreamProcessing.repository.enrichedCommentsRepository enrichedCommentsRepository) {
        this.sentimentAnalysisAnalytic = sentimentAnalysisAnalytics;
        this.wordsCloudAnalytics = wordsCloudAnalytics;
        this.enrichedCommentsRepository = enrichedCommentsRepository;
    }
    @GetMapping("/sa/year")
    public Map<Integer, List<EnrichedComment>> get(){
        return this.sentimentAnalysisAnalytic.getThisYearResult();

    }
    @GetMapping("/sa/day")
    public Map<Integer, List<EnrichedComment>> geta(){
//        String dateString = "2021-02-23T12:32:16.000Z";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//        try {
//            Date date = sdf.parse(dateString);
//            System.out.println(date);
//            return this.enrichedCommentsRepository.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
        return this.sentimentAnalysisAnalytic.getDayResult();

    }
    @GetMapping("/test3")
    public Map<Integer, List<EnrichedComment>> get2(){
//        String dateString = "2021-02-23T12:32:16.000Z";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//        try {
//            Date date = sdf.parse(dateString);
//            System.out.println(date);
//            return this.enrichedCommentsRepository.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
        return this.sentimentAnalysisAnalytic.getLastMonthResult();

    }
    @GetMapping("/wc/year")
    public List<wordCountAggregationResult> getYearWordCounts(){
        String dateString = "2023-01-01T00:00:00.000Z";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = formatter.parse(dateString);
            System.out.println(dateString);
        } catch (ParseException e) {
            System.out.println(e.toString());
            return null;
        }
        return this.wordsCloudAnalytics.getWordsCount(date);
    }
}

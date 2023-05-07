package com.example.kafkaStreamProcessing.analytics;

import com.example.kafkaStreamProcessing.repository.enrichedCommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.kafkaStreamProcessing.entity.EnrichedComment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class sentimentAnalysisAnalytics {
    private final enrichedCommentsRepository enrichedCommentsRepo;
    @Autowired
    sentimentAnalysisAnalytics(enrichedCommentsRepository enrichedCommentsRepo){
        this.enrichedCommentsRepo = enrichedCommentsRepo;
    }
    public Map<Integer, List<EnrichedComment>> getThisYearResult(){
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
        Map<Integer, List<EnrichedComment>> commentsMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        List<EnrichedComment> enrichedCommentList = enrichedCommentsRepo.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(date);

        for(EnrichedComment enComment:enrichedCommentList){
            calendar.setTime(enComment.getTimestamps());
            int monthNum = calendar.get(Calendar.MONTH);
            List<EnrichedComment> commentsForDay = commentsMap.getOrDefault(monthNum, new ArrayList<>());
            commentsForDay.add(enComment);
            commentsMap.put(monthNum, commentsForDay);
        }
        return commentsMap;
    }
    public Map<Integer, List<EnrichedComment>> getLastMonthResult(){
        Map<Integer, List<EnrichedComment>> commentsMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -29);
        List<EnrichedComment> enrichedCommentList = enrichedCommentsRepo.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(calendar.getTime());

        for(EnrichedComment enComment:enrichedCommentList){
            Date timestamp = enComment.getTimestamps();
            Date now = Calendar.getInstance().getTime();

            long diffirenceTimeMilliseconds = now.getTime() - timestamp.getTime();
            int diffirenceInDays = (int) Math.floor(TimeUnit.MILLISECONDS.toDays(diffirenceTimeMilliseconds));
            List<EnrichedComment> commentsForDay = commentsMap.getOrDefault(diffirenceInDays, new ArrayList<>());
            commentsForDay.add(enComment);
            commentsMap.put(diffirenceInDays, commentsForDay);
        }
        return commentsMap;
    }
    public Map<Integer, List<EnrichedComment>> getLastSemesterResult(){
        try{
            Map<Integer, List<EnrichedComment>> commentsMap = new HashMap<>();
            List<EnrichedComment> enrichedCommentList = enrichedCommentsRepo.findAll();
            System.out.println(enrichedCommentList.toString());
            for(EnrichedComment enComment:enrichedCommentList){
                Date timestamp = enComment.getTimestamps();
                Calendar cal = Calendar.getInstance();
                cal.setTime(timestamp);
                int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
                List<EnrichedComment> commentsForDay = commentsMap.getOrDefault(weekNumber, new ArrayList<>());
                commentsForDay.add(enComment);
                commentsMap.put(weekNumber, commentsForDay);
            }
            return commentsMap;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    Map<Integer, List<EnrichedComment>> getLastWeekResult(){
        Map<Integer, List<EnrichedComment>> commentsMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        List<EnrichedComment> enrichedCommentList = enrichedCommentsRepo.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(calendar.getTime());

        for(EnrichedComment enComment:enrichedCommentList) {
            Date timestamp = enComment.getTimestamps();
            Date now = Calendar.getInstance().getTime();
            long diffirenceTimeMilliseconds = now.getTime() - timestamp.getTime();
            int diffirenceInDays = (int) Math.floor(TimeUnit.MILLISECONDS.toDays(diffirenceTimeMilliseconds));
            List<EnrichedComment> commentsForDay = commentsMap.getOrDefault(diffirenceInDays, new ArrayList<>());
            commentsForDay.add(enComment);
            commentsMap.put(diffirenceInDays, commentsForDay);
        }
        return commentsMap;
    }
    public Map<Integer, List<EnrichedComment>> getDayResult() {
        try {
            String dateString = "2023-05-01T00:00:00.000Z";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            Date date = formatter.parse(dateString);
            System.out.println(date);
            Map<Integer, List<EnrichedComment>> commentsMap = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -24);
            List<EnrichedComment> enrichedCommentList = enrichedCommentsRepo.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(date);
            System.out.println(enrichedCommentsRepo.findByTimestampsGreaterThanEqualOrderByTimestampsAsc(calendar.getTime()));
            for (EnrichedComment enComment : enrichedCommentList) {
                Date timestamp = enComment.getTimestamps();
                Date now = Calendar.getInstance().getTime();
                long differenceTimeMillisecons = now.getTime() - timestamp.getTime();
                int diffirenceTimeHours = (int) Math.floor(TimeUnit.MILLISECONDS.toHours(differenceTimeMillisecons));
                List<EnrichedComment> commentsForDay = commentsMap.getOrDefault(diffirenceTimeHours, new ArrayList<>());
                commentsForDay.add(enComment);
                commentsMap.put(diffirenceTimeHours, commentsForDay);
            }
            return commentsMap;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

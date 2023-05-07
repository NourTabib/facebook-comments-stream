package com.example.kafkaStreamProcessing.streams;

import com.example.kafkaStreamProcessing.entity.Comment;
import com.example.kafkaStreamProcessing.entity.wordCountsResults;
import com.example.kafkaStreamProcessing.repository.wordCountsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class wordCountsStream {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private final ObjectMapper objectMapper= new ObjectMapper();
    private final wordCountsRepository wordCountsRepo;
    wordCountsStream(wordCountsRepository wordCountsRepo){
        this.wordCountsRepo = wordCountsRepo;
    }
    @Autowired
    void buildPipeLine(StreamsBuilder streamsBuilder){
        KStream<String,String> commentsStream = streamsBuilder
                .stream("raw-comments", Consumed.with(STRING_SERDE,STRING_SERDE))
                .mapValues((value) -> {
                    try{
                        String stringRes = "";
                        Map<String,Integer> map = new HashMap<>();
                        Comment commentaire = objectMapper.readValue(value,Comment.class);
                        String[] words =commentaire.getComment_text().split("[ ,\\n,/n]+");
                        List<String> list = new ArrayList<>();
                        for(String word:words) {
                            if (map.containsKey(word)) {
                                int counter = map.get(word) + 1;
                                map.put(word, counter);
                            } else {
                                map.put(word, 1);
                            }
                        }
                        map.entrySet().forEach(entry -> {
                            wordCountsResults wordCount = new wordCountsResults();
                            wordCount.setWord(entry.getKey());
                            wordCount.setCount(entry.getValue());
                            wordCount.setTimestamps(commentaire.getTimestamps());
                            wordCount.setComment_id(commentaire.getComment_id());
                            wordCountsRepo.save(wordCount);
                            try{
                                String result = objectMapper.writeValueAsString(wordCount);
                                list.add(result);
                            }catch(Exception e){
                                System.out.println(e);
                            }

                        });
                        for(String str:list){
                            stringRes = stringRes + ";;"+str;
                        }
                        return stringRes;
                    }catch(Exception e){
                        System.out.println(e);
                        return null;
                    }
                });
        commentsStream.to("comments-word-counts",Produced.with(STRING_SERDE,STRING_SERDE));
        };
    @Autowired
    void buildSink(StreamsBuilder streamsBuilder){
        KStream<String,String> sinkStream = streamsBuilder
                .stream("comments-word-counts",Consumed.with(STRING_SERDE,STRING_SERDE))
                .peek((key,value) -> {
                    try{
                        String[] valueString = value.toString().split(";;");
                        for(String wordCount:valueString) {
                            if(!wordCount.isEmpty()){
                                wordCountsResults result = objectMapper.readValue(wordCount,wordCountsResults.class);
                                wordCountsRepo.save(result);
                            }
                        }
                    }catch(Exception e){
                        System.out.println(e);
                    }
                });
    }
}

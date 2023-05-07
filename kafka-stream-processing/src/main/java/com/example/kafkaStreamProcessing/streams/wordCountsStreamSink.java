//package com.example.kafkaStreamProcessing.streams;
//
//import com.example.kafkaStreamProcessing.entity.wordCountsResults;
//import com.example.kafkaStreamProcessing.repository.wordCountsRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.kstream.Consumed;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class wordCountsStreamSink {
//    private static final Serde<String> STRING_SERDE = Serdes.String();
//    private final ObjectMapper objectMapper= new ObjectMapper();
//    private final wordCountsRepository wordCountsRepo;
//    wordCountsStreamSink(wordCountsRepository wordCountsRepo){
//        this.wordCountsRepo = wordCountsRepo;
//    }
//    @Autowired
//    void buildSink(StreamsBuilder streamsBuilder){
//        KStream<String,String> sinkStream = streamsBuilder
//                .stream("comments-word-counts", Consumed.with(STRING_SERDE,STRING_SERDE))
//                .peek((key,value) -> {
//                    try{
//                        System.out.println(value);
//                        String[] valueString = value.toString().split(";;");
//                        for(String wordCount:valueString) {
//                            if(!wordCount.isEmpty()){
//                                wordCountsResults result = objectMapper.readValue(wordCount,wordCountsResults.class);
//                                //wordCountsRepo.save(result);
//                            }
//                        }
//                    }catch(Exception e){
//                        System.out.println(e);
//                    }
//                });
//    }
//}

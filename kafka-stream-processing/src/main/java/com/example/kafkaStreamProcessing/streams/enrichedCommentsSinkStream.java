package com.example.kafkaStreamProcessing.streams;

import com.example.kafkaStreamProcessing.entity.EnrichedComment;
import com.example.kafkaStreamProcessing.repository.enrichedCommentsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class enrichedCommentsSinkStream {
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private final ObjectMapper objectMapper= new ObjectMapper();
    private final enrichedCommentsRepository enrichedCommentRepo;
    enrichedCommentsSinkStream(enrichedCommentsRepository enrichedCommentRepo){
        this.enrichedCommentRepo = enrichedCommentRepo;
    }
    @Autowired
    void buildSink(StreamsBuilder streamsBuilder){
        KStream<String,String> sinkStream = streamsBuilder
                .stream("processed-comments", Consumed.with(STRING_SERDE,STRING_SERDE))
                .mapValues(value ->{
                    try {
                        if(value.equals("2")){
                            return null;
                        }
                        else{
                            EnrichedComment enrichedComment = objectMapper.readValue(value,EnrichedComment.class);
                            enrichedCommentRepo.save(enrichedComment);
                            return null;
                        }
                    }catch (Exception e){
                        return null;
                    }
                });

    }
}

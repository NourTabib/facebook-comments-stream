package com.example.kafkaStreamProcessing.eventHandler;
import com.example.kafkaStreamProcessing.entity.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
public class commentEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(commentEventHandler.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    public commentEventHandler(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public boolean publishEvent(Comment comment){
        try{
            String payload = objectMapper.writeValueAsString(comment);
            kafkaTemplate.send("raw-comments",""+payload);
            return true;
        }catch(Exception e){
            LOGGER.error("Error : ",e);
            return false;
        }
    }
}

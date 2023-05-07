package com.example.kafkaStreamProcessing.Config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    //@Value(value = "${spring.kafka.bootstrap-servers}")
    //private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        return new KafkaAdmin(configs);
    }
    @Bean
    public NewTopic rawComments(){
        return new NewTopic("raw-comments",1,(short) 1);
    }
    @Bean
    public NewTopic processedComments(){
        return new NewTopic("processed-comments",1,(short) 1);
    }
    @Bean
    public NewTopic commentsWordCount(){
        return new NewTopic("comments-word-counts",1,(short) 1);
    }
}

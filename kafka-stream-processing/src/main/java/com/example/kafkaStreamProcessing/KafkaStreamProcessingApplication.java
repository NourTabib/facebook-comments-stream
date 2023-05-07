package com.example.kafkaStreamProcessing;

import com.example.kafkaStreamProcessing.entity.Comment;
import com.example.kafkaStreamProcessing.entity.Index;
import com.example.kafkaStreamProcessing.grpcClient.grpcClient;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import com.example.kafkaStreamProcessing.repository.indexRepository;
import com.example.kafkaStreamProcessing.databaseListener.commentCollectionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableMongoRepositories
public class KafkaStreamProcessingApplication {
	@Autowired
	indexRepository indexRepo;
	@Autowired
	commentCollectionListener commentCollectionListener;
	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		List<Index> i = indexRepo.findAll();
		Comment currentElement;
		if(i.isEmpty()){
			 currentElement = commentCollectionListener.fromBeginningListening();
		}

		while(true){
			currentElement = commentCollectionListener.resumeListening(indexRepo.findById(1).get());
			if(currentElement == null){
				break;
			}
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamProcessingApplication.class, args);
	}

}

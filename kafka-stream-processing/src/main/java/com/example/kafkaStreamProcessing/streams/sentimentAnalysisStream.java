package com.example.kafkaStreamProcessing.streams;
import com.example.kafkaStreamProcessing.entity.Comment;
import com.example.kafkaStreamProcessing.entity.EnrichedComment;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.kafkaStreamProcessing.grpcClient.grpcClient;
import java.util.Locale;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class sentimentAnalysisStream
{

    private static grpcClient grpcCl = null;
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private final ObjectMapper objectMapper = new ObjectMapper();
    public grpcClient getGrpcClient(){
        if(grpcCl == null){
            grpcCl = new grpcClient();
            return grpcCl;
        }
        return grpcCl;
    }
    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder){
        KStream<String,String> saStream = streamsBuilder
                .stream("raw-comments",Consumed.with(STRING_SERDE, STRING_SERDE))
                //.peek((key,value)-> System.out.println("received Comment"))
                .mapValues(value -> {
                      try{
                          Comment commentaire = objectMapper.readValue(value,Comment.class);
                          String sentiment = getGrpcClient().predictComment(
                                  getGrpcClient().getStub(),
                                  commentaire.getComment_text()
                          );
                          EnrichedComment enrichedComment = EnrichedComment.builder()
                                  .comment_id(commentaire.getComment_id())
                                  ._id(commentaire.get_id())
                                  .sentiment(Double.parseDouble(sentiment))
                                  .timestamps((commentaire.getTimestamps()))
                                  .build();
                          String enchrichedCommentString = objectMapper.writeValueAsString(enrichedComment);
                          return enchrichedCommentString;
                      }catch (Exception e){
                          return null;
                      }
                });

        saStream.to("processed-comments",Produced.with(STRING_SERDE,STRING_SERDE));
    }
}

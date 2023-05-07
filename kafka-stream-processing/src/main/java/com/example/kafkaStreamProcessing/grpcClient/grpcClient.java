package com.example.kafkaStreamProcessing.grpcClient;

import PredictionService.Prediction;
import PredictionService.PredictionServiceGrpc;
import PredictionService.Text;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class grpcClient {
    public static PredictionServiceGrpc.PredictionServiceBlockingStub Stub = null;
    static Logger LOGGER = LoggerFactory.getLogger(grpcClient.class);

    public grpcClient() {

    }

    public PredictionServiceGrpc.PredictionServiceBlockingStub getStub() {
        if (Stub == null) {
            ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051).usePlaintext().build();
            PredictionServiceGrpc.PredictionServiceBlockingStub blockingStub = PredictionServiceGrpc.newBlockingStub(managedChannel);
            grpcClient.Stub = blockingStub;
            return grpcClient.Stub;
        }
        return grpcClient.Stub;
    }

    public String predictComment(PredictionServiceGrpc.PredictionServiceBlockingStub Stub, String Comment) {
        Prediction sentiment = getStub().predictText(Text.newBuilder().setText(Comment).build());
        return sentiment.getPrediction();
    }
}

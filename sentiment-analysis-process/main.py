import transformers
from sentimentAnalysis import *
import tensorflow as tf
import pandas as pd
import grpc
from concurrent import futures
from server import PredictionService_pb2
from server import PredictionService_pb2_grpc

import warnings 
warnings.simplefilter('ignore')


BATCH_SIZE = 32 
MAX_LEN = 192
AUTO = tf.data.experimental.AUTOTUNE
model_path = "./pretrained/model/weights"
transformer_path = "./pretrained/transformer"
vocab_path = "./pretrained/vocab/vocab.txt"

transformerr = (transformers.TFDistilBertModel.from_pretrained(transformer_path))
fast_tokenizer = BertWordPieceTokenizer(vocab_path, lowercase=False)
loaded_model1 = build_model(transformerr, max_len=MAX_LEN)

loaded_model1.load_weights(model_path)

class PredictionService(PredictionService_pb2_grpc.PredictionServiceServicer):
    def PredictText(self, request, context):
        print("Received")
        req_text = request.text
        print(req_text)
        df2 = pd.DataFrame([req_text], columns = ['texts'])
        enco = fast_encode(df2.texts.values.astype(str), fast_tokenizer, maxlen=MAX_LEN)
        enco_ten = getTensor(enco)
        resultat = loaded_model1.predict(enco_ten)[0][0]
        print(resultat)
        resp = PredictionService_pb2.Prediction(prediction = str(resultat))
        return resp
    
server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
PredictionService_pb2_grpc.add_PredictionServiceServicer_to_server(PredictionService(), server)
server.add_insecure_port('127.0.0.1:50051')
server.start()
server.wait_for_termination()
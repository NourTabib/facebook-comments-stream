import numpy as np
import transformers
from transformers import TFAutoModel, AutoTokenizer
from tqdm.notebook import tqdm
from tokenizers import Tokenizer, models, pre_tokenizers, decoders, processors
import tensorflow as tf
from tensorflow.keras.layers import Dense, Input
from tensorflow.keras.models import Model
from tensorflow.keras.callbacks import ModelCheckpoint
import transformers
import tensorflow as tf
import pandas as pd
from tokenizers import BertWordPieceTokenizer
import warnings 
warnings.simplefilter('ignore')


BATCH_SIZE = 32 
MAX_LEN = 192
AUTO = tf.data.experimental.AUTOTUNE
model_path = "./pretrained/model/weights"
transformer_path = "./pretrained/transformer"
vocab_path = "./pretrained/vocab/vocab.txt"


def fast_encode(texts, tokenizer, chunk_size=256, maxlen=512):

    tokenizer.enable_truncation(max_length=maxlen)
    tokenizer.enable_padding(length=maxlen)
    all_ids = []
    
    for i in tqdm(range(0, len(texts), chunk_size)):
        text_chunk = texts[i:i+chunk_size].tolist()
        encs = tokenizer.encode_batch(text_chunk)
        all_ids.extend([enc.ids for enc in encs])
    
    return np.array(all_ids)

def regular_encode(texts, tokenizer, maxlen=512):
    enc_di = tokenizer.batch_encode_plus(
        texts, 
        return_attention_masks=False, 
        return_token_type_ids=False,
        pad_to_max_length=True,
        max_length=maxlen
    )
    
    return np.array(enc_di['input_ids'])

def build_model(transformer, max_len=512):

    input_word_ids = Input(shape=(max_len,), dtype=tf.int32, name="input_word_ids")
    sequence_output = transformer(input_word_ids)[0]
    cls_token = sequence_output[:, 0, :]
    out = Dense(1, activation='sigmoid')(cls_token)
    
    model = Model(inputs=input_word_ids, outputs=out)
    model.compile(tf.keras.optimizers.legacy.Adam(lr=1e-6), loss='binary_crossentropy', metrics=['accuracy','AUC'])
    
    return model

def getTensor(text) :
    test_dataset = (
        tf.data.Dataset
        .from_tensor_slices(text)
        .batch(BATCH_SIZE)
    )
    return test_dataset

def loadModel(model):
    #transformerr = (transformers.TFDistilBertModel.from_pretrained(transformer_path))
    #fast_tokenizer = BertWordPieceTokenizer(vocab_path, lowercase=False)
    #model = build_model(transformerr, max_len=MAX_LEN)
    loaded_model = model.load_weights(model_path)
    return loaded_model
def encodeText(tokenizer,text):
    df = pd.DataFrame([text], columns = ['texts'])
    texts = fast_encode(df.texts.values.astype(str), tokenizer, maxlen=MAX_LEN)
    tensor = getTensor(texts)
    return tensor
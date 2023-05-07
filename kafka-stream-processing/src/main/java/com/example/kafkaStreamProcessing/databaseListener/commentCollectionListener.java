package com.example.kafkaStreamProcessing.databaseListener;
import com.example.kafkaStreamProcessing.entity.Comment;
import com.example.kafkaStreamProcessing.entity.Index;
import com.example.kafkaStreamProcessing.repository.indexRepository;
import com.example.kafkaStreamProcessing.repository.commentRepository;
import com.example.kafkaStreamProcessing.eventHandler.commentEventHandler;
import org.springframework.stereotype.Component;

@Component
public class commentCollectionListener {
    private commentRepository commentRep;
    private indexRepository indexRep;
    private commentEventHandler commentEventHandler;
    commentCollectionListener(commentRepository commentRep,indexRepository indexRep,commentEventHandler commentEventHandler){
        this.commentRep = commentRep;
        this.indexRep = indexRep;
        this.commentEventHandler = commentEventHandler;
    }
    public Comment resumeListening(Index index){
        try{
            Comment currentComment = commentRep.findFirstByTimestampsGreaterThanOrderByTimestampsAsc(index.getTimestamps());
            if(currentComment == null){
                return null;
            }
            commentEventHandler.publishEvent(
                    currentComment
            );
            Index newIndex = new Index();
            newIndex.setComment__id(currentComment.getComment_id());
            newIndex.set_id(1);
            newIndex.setTimestamps(currentComment.getTimestamps());
            indexRep.save(newIndex);
            return currentComment;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }

    }
    public Comment fromBeginningListening(){
        try{
            Comment firstComment = commentRep.findFirstByOrderByTimestampsAsc();
            commentEventHandler.publishEvent(firstComment);
            Index i = new Index();
            i.setComment__id(firstComment.getComment_id());
            i.set_id(1);
            i.setTimestamps(firstComment.getTimestamps());
            indexRep.save(i);
            return firstComment;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}

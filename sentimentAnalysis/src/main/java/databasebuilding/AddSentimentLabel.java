package databasebuilding;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dao.DAOTweets;
import dao.DAOTweetsNGrams;
import nlp.NLPTools;
import org.bson.Document;


/**
 * This class is used to apply a sentiment label on each tweet of the MongoDB database
 */
public class AddSentimentLabel {

    public static void main(String args[]){
        labelizeSentiment();
    }

    public static void labelizeSentiment(){
        final DAOTweets daoTweets = new DAOTweets();

        FindIterable<Document> allTweet = daoTweets.getAllTweet();
        allTweet.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println("Adding Sentiment Label to "+document.get("message"));
                Document newDocument = new Document();
                newDocument.append("$set", new Document().append("sentiment", NLPTools.getSentiment((String)document.get("message"))));

                daoTweets.updateDocument(document, newDocument);
            }
        });
    }

}

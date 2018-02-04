package databasebuilding;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dao.DAOTweetsNGrams;
import nlp.NLPTools;
import org.bson.Document;

/**
 * Not used anymore
 */
public class LabelizeSentiment {

    public static void main(String args[]){
        labelizeSentiment();
    }

    public static void labelizeSentiment(){
        final DAOTweetsNGrams daoTweetsNGrams = new DAOTweetsNGrams();

        FindIterable<Document> allTweet = daoTweetsNGrams.getAllTweet();
        allTweet.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {

                System.out.println("Sentiment :" + document.get("sentiment"));

                if(document.get("sentiment").equals("")){
                    System.out.println("Adding Sentiment Label");
                    Document newDocument = new Document();
                    newDocument.append("$set", new Document().append("sentiment", NLPTools.getSentiment((String)document.get("message"))));

                    daoTweetsNGrams.updateDocument(document, newDocument);
                }
            }
        });
    }

}

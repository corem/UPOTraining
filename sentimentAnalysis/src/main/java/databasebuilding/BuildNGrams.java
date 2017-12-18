package databasebuilding;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dao.DAOTweets;
import dao.DAOTweetsNGrams;
import model.Tweet;
import model.TweetNGrams;
import nlp.NLPTools;
import nlp.WordNet;
import org.bson.Document;

import java.util.List;

/**
 * This class uses the raw Twitter data in MongoDB to build a new database
 * containing the preprocessed tokens of the Tweets.
 */
public class BuildNGrams {

    public static void main(String args[]){
        buildNGrams();
    }

    public static void buildNGrams(){
        final DAOTweetsNGrams daoTweetsNGrams = new DAOTweetsNGrams();
        final DAOTweets daoTweets = new DAOTweets();
        final WordNet wordNet = new WordNet();

        FindIterable<Document> allTweet = daoTweets.getAllTweet();
        allTweet.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Tweet tweet = DAOTweets.documentToTweet(document);

                List<String> lemmasWordNet = NLPTools.preprocessTweet(tweet.getMessage());

                TweetNGrams tweetNGrams = new TweetNGrams(tweet.getAuthor(), tweet.getMessage(), tweet.getDate(), tweet.getLanguage(), lemmasWordNet, "");
                daoTweetsNGrams.insertTweetNGrams(tweetNGrams);

            }
        });
    }

}

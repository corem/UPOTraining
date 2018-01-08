package databasebuilding;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dao.DAOTweets;
import dao.DAOTweetsNGrams;
import model.Tweet;
import model.TweetNGrams;
import nlp.NLPTools;
import nlp.WordNet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.bson.Document;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class uses the raw Twitter data in MongoDB to build a new database
 * containing the preprocessed tokens of the Tweets.
 */
public class BuildNGrams {

    public static void main(String args[]){

        if(args.length < 2){
            System.out.println("Usage : BuildNGrams min max");
            System.exit(0);
        }

        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);

        buildNGrams(min, max);
    }

    public static void buildNGrams(int min, int max){
        final DAOTweetsNGrams daoTweetsNGrams = new DAOTweetsNGrams();
        final DAOTweets daoTweets = new DAOTweets();
        final WordNet wordNet = new WordNet();

        FindIterable<Document> allTweet = daoTweets.getAllTweet();
        allTweet.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Tweet tweet = DAOTweets.documentToTweet(document);

                System.out.println("DEBUG : "+tweet.getSentiment());

                List<String> lemmasWordNet = NLPTools.preprocessTweet(tweet.getMessage());

                String message = "";
                for(String string:lemmasWordNet){
                    message+= string+" ";
                }

                Reader reader = new StringReader(message);
                TokenStream nGramTokenizer = new StandardTokenizer(Version.LUCENE_4_10_3, reader);
                nGramTokenizer = new ShingleFilter(nGramTokenizer, min, max);
                //NGramTokenizer nGramTokenizer = new NGramTokenizer(reader, 2, 2);

                CharTermAttribute charTermAttribute = nGramTokenizer.addAttribute(CharTermAttribute.class);

                List<String> lemmaStrings = new ArrayList<>();

                try {
                    nGramTokenizer.reset();

                    while (nGramTokenizer.incrementToken()) {
                        String token = charTermAttribute.toString();
                        System.out.println(token);
                        lemmaStrings.add(token);
                    }
                    nGramTokenizer.end();
                    nGramTokenizer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TweetNGrams tweetNGrams = new TweetNGrams(tweet.getAuthor(), tweet.getMessage(), tweet.getDate(), tweet.getLanguage(), lemmaStrings, tweet.getSentiment());
                daoTweetsNGrams.insertTweetNGrams(tweetNGrams);

            }
        });
    }

}

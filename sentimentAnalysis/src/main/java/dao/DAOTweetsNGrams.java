package dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.TweetNGrams;
import org.bson.Document;

public class DAOTweetsNGrams {

    private MongoSingleton singleton = null;
    private MongoDatabase database = null;
    private MongoCollection<Document> collection = null;

    private final static String SERVER = "localhost";
    private final static int PORT = 27017;
    private final static String DATABASE = "tweets";
    private final static String COLLECTION = "tweetNGrams";

    public DAOTweetsNGrams(){
        singleton = new MongoSingleton(SERVER, PORT);
        database = singleton.getDatabase(DATABASE);
        collection = database.getCollection(COLLECTION);
    }

    public void insertTweetNGrams(TweetNGrams tweetNGrams){
        Document doc = new Document("author", tweetNGrams.getAuthor())
                .append("message", tweetNGrams.getMessage())
                .append("date", tweetNGrams.getDate())
                .append("language", tweetNGrams.getLanguage())
                .append("ngrams", tweetNGrams.getListNGrams())
                .append("sentiment", tweetNGrams.getSentiment());
        collection.insertOne(doc);
    }

    public void updateDocument(Document oldDocument, Document newDocument){
        collection.updateOne(oldDocument, newDocument);
    }

    public FindIterable<Document> getAllTweet(){
        return collection.find().noCursorTimeout(true);
    }

    public FindIterable<Document> getAllTweetNGrams(){
        return collection.find();
    }

    public Document getFirstTweetNGrams(){
        return collection.find().first();
    }

}

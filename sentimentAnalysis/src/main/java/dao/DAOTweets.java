package dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Tweet;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DAOTweets implements DAOTweetsInterface{

    private MongoSingleton singleton = null;
    private MongoDatabase database = null;
    private MongoCollection<Document> collection = null;

    private final static String SERVER = "localhost";
    private final static int PORT = 27017;
    private final static String DATABASE = "tweets";
    private final static String COLLECTION = "tweet";

    public DAOTweets(){
        singleton = new MongoSingleton(SERVER, PORT);
        database = singleton.getDatabase(DATABASE);
        collection = database.getCollection(COLLECTION);
    }

    public void insertTweet(Tweet tweet){
        Document doc = tweetToDocument(tweet);
        collection.insertOne(doc);
    }

    public FindIterable<Document> getAllTweet(){
        return collection.find().noCursorTimeout(true);
    }

    public Tweet getFirstTweet(){
        Document document = collection.find().first();
        Tweet tweet = documentToTweet(document);
        return tweet;
    }

    public static Tweet documentToTweet(Document document) {
        //String id = (String)document.get("_id");
        String username = (String)document.get("username");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        Date date = null;
        try {
            date = formatter.parse((String)document.get("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String message = (String)document.get("message");
        String language = (String)document.get("language");
        Tweet tweet = new Tweet(username, message, date, language);
        return tweet;
    }

    private static Document tweetToDocument(Tweet tweet){
        Document doc = new Document("author", tweet.getAuthor())
                .append("message", tweet.getMessage())
                .append("date", tweet.getDate());
        return doc;
    }



}

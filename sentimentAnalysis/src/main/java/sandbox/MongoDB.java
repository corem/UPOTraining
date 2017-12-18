package sandbox;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;

public class MongoDB {

    public static void main(String args[]){

        MongoClient mongoClient = new MongoClient("localhost", 27017);
        mongoClient.getDatabase("twitter");

        //Mongo mongo = new Mongo("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("twitter");

        MongoCollection<Document> collection = database.getCollection("tweet");

        Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));

        collection.insertOne(doc);
    }

}

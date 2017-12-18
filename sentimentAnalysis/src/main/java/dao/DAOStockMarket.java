package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Quote;
import org.bson.Document;

public class DAOStockMarket implements DAOStockMarketInterface{

    private MongoSingleton singleton = null;
    private MongoDatabase database = null;
    private MongoCollection<Document> collection = null;

    private final static String SERVER = "localhost";
    private final static int PORT = 27017;
    private final static String DATABASE = "tweets";
    private final static String COLLECTION = "stockmarket";

    public DAOStockMarket(){
        singleton = new MongoSingleton(SERVER, PORT);
        database = singleton.getDatabase(DATABASE);
        collection = database.getCollection(COLLECTION);
    }

    public void insertQuote(Quote quote){
        Document doc = quoteToDocument(quote);
        collection.insertOne(doc);
    }

    private Document quoteToDocument(Quote quote){
        Document doc = new Document("date", quote.getDate())
                .append("low", quote.getLow())
                .append("high", quote.getHigh())
                .append("open", quote.getOpen())
                .append("close", quote.getClose())
                .append("adjClose", quote.getAdjClose());

        return doc;
    }
}

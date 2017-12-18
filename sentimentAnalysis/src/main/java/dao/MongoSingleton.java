package dao;

import com.mongodb.MongoClient;

public class MongoSingleton extends MongoClient {

    private static MongoSingleton instance = null;

    protected MongoSingleton (String ip, int port){
        super(ip, port);
    }

    public static synchronized MongoSingleton getInstance(String ip, int port){

        if (instance == null){
            instance =  new MongoSingleton(ip,port);
        }

        return instance;
    }

}

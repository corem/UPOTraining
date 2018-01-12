package stats;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dao.DAOTweets;
import model.Tweet;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class MongoDBStats {

    public static void main(String args[]){
        countNumberOfTweets();
    }

    public static void countNumberOfTweets(){

        final DAOTweets daoTweets = new DAOTweets();

        StatsContainer statsContainer = new StatsContainer();

        System.out.println(statsContainer.getSentimentTotal());

        FindIterable<Document> allTweet = daoTweets.getAllTweet();
        allTweet.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Tweet tweet = DAOTweets.documentToTweet(document);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                String day = dateFormat.format(tweet.getDate());

                //System.out.println(day);

                SentimentCount sentimentCountTotal = statsContainer.getSentimentTotal();
                HashMap<String, SentimentCount> sentimentCountByDay = statsContainer.getSentimentByDate();

                switch (tweet.getSentiment()){
                    case "very_negative" :
                        sentimentCountTotal.incNegative();
                        if(sentimentCountByDay.containsKey(day)){
                            sentimentCountByDay.get(day).incNegative();
                        }else{
                            sentimentCountByDay.put(day, new SentimentCount());
                        }
                        break;
                    case "negative" :
                        sentimentCountTotal.incNegative();
                        if(sentimentCountByDay.containsKey(day)){
                            sentimentCountByDay.get(day).incNegative();
                        }else{
                            sentimentCountByDay.put(day, new SentimentCount());
                        }
                        break;
                    case "neutral" :
                        sentimentCountTotal.incNeutral();
                        if(sentimentCountByDay.containsKey(day)){
                            sentimentCountByDay.get(day).incNeutral();
                        }else{
                            sentimentCountByDay.put(day, new SentimentCount());
                        }
                        break;
                    case "positive" :
                        sentimentCountTotal.incPositive();
                        if(sentimentCountByDay.containsKey(day)){
                            sentimentCountByDay.get(day).incPositive();
                        }else{
                            sentimentCountByDay.put(day, new SentimentCount());
                        }
                        break;
                    case "very_positive" :
                        sentimentCountTotal.incPositive();
                        if(sentimentCountByDay.containsKey(day)){
                            sentimentCountByDay.get(day).incPositive();
                        }else{
                            sentimentCountByDay.put(day, new SentimentCount());
                        }
                        break;
                }


            }

        });

        System.out.println(statsContainer);

    }



}

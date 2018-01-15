package stats;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

public class StatsContainer {

    private HashMap<String, SentimentCount> sentimentByDate;
    private SentimentCount sentimentTotal;

    public StatsContainer() {
        this.sentimentByDate = new HashMap<>();
        this.sentimentTotal = new SentimentCount();
    }

    public HashMap<String, SentimentCount> getSentimentByDate() {
        return sentimentByDate;
    }

    public void setSentimentByDate(HashMap<String, SentimentCount> sentimentByDate) {
        this.sentimentByDate = sentimentByDate;
    }

    public SentimentCount getSentimentTotal() {
        return sentimentTotal;
    }

    public void setSentimentTotal(SentimentCount sentimentTotal) {
        this.sentimentTotal = sentimentTotal;
    }

    public void printStats(){
        DescriptiveStatistics negativeStats = new DescriptiveStatistics();
        DescriptiveStatistics positiveStats = new DescriptiveStatistics();
        DescriptiveStatistics neutralStats = new DescriptiveStatistics();

        for(Map.Entry<String, SentimentCount> numberOfSentiment: getSentimentByDate().entrySet()){
            negativeStats.addValue(numberOfSentiment.getValue().getNegative());
            neutralStats.addValue(numberOfSentiment.getValue().getNeutral());
            positiveStats.addValue(numberOfSentiment.getValue().getPositive());
        }

        System.out.println("Tweets number [ total : "+ getSentimentTotal().getTotal() +", negative : "+getSentimentTotal().getNegative()+", neutral : "+getSentimentTotal().getNeutral()+", positive : "+getSentimentTotal().getPositive()+" ]");
        System.out.println("Negative tweets [ average : "+negativeStats.getMean()+", standard deviation : "+negativeStats.getStandardDeviation()+" ]");
        System.out.println("Neutral tweets [ average : "+neutralStats.getMean()+", standard deviation : "+neutralStats.getStandardDeviation()+" ]");
        System.out.println("Positive tweets [ average : "+positiveStats.getMean()+", standard deviation : "+positiveStats.getStandardDeviation()+" ]");
    }

    @Override
    public String toString() {
        return "StatsContainer{" +
                "sentimentByDate=" + sentimentByDate +
                ", sentimentTotal=" + sentimentTotal +
                '}';
    }
}

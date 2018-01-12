package stats;

import java.util.HashMap;

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

    @Override
    public String toString() {
        return "StatsContainer{" +
                "sentimentByDate=" + sentimentByDate +
                ", sentimentTotal=" + sentimentTotal +
                '}';
    }
}

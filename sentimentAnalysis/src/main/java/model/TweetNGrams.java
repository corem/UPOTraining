package model;

import java.util.Date;
import java.util.List;

public class TweetNGrams extends Tweet{

    private List<String> listNGrams;
    private String sentiment;

    public TweetNGrams(String author,String message, Date date, String language, List<String> listNGrams, String sentiment){
        super(author, message, date, language);
        this.listNGrams = listNGrams;
        this.sentiment = sentiment;
    }

    public TweetNGrams(String id, String author,String message, Date date, String language, List<String> listNGrams, String sentiment){
        super(id, author, message, date, language);
        this.listNGrams = listNGrams;
        this.sentiment = sentiment;
    }

    public List<String> getListNGrams() {
        return listNGrams;
    }

    public void setListNGrams(List<String> listNGrams) {
        this.listNGrams = listNGrams;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "TweetNGrams{" +
                "listNGrams=" + listNGrams +
                ", sentiment='" + sentiment + '\'' +
                "} " + super.toString();
    }
}

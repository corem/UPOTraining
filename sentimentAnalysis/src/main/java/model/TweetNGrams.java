package model;

import java.util.Date;
import java.util.List;

public class TweetNGrams extends Tweet{

    private List<String> listNGrams;

    public TweetNGrams(String author,String message, Date date, String language, List<String> listNGrams, String sentiment){
        super(author, message, date, language, sentiment);
        this.listNGrams = listNGrams;
    }

    public TweetNGrams(String id, String author,String message, Date date, String language, List<String> listNGrams, String sentiment){
        super(id, author, message, date, language, sentiment);
        this.listNGrams = listNGrams;
    }

    public List<String> getListNGrams() {
        return listNGrams;
    }

    public void setListNGrams(List<String> listNGrams) {
        this.listNGrams = listNGrams;
    }

    @Override
    public String toString() {
        return "TweetNGrams{" +
                "listNGrams=" + listNGrams +
                "} " + super.toString();
    }
}

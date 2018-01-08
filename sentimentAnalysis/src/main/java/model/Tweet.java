package model;

import java.util.Date;

public class Tweet {

    private String id;
    private String author;
    private String message;
    private Date date;
    private String language;
    private String sentiment;

    public Tweet(String author,String message, Date date, String language, String sentiment){
        this.author = author;
        this.message = message;
        this.date = date;
        this.language = language;
        this.sentiment = sentiment;
    }

    public Tweet(String id, String author, String message, Date date, String language, String sentiment){
        this(author, message, date, language, sentiment);
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSentiment() { return sentiment; }

    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

    @Override
    public String toString() {
        return "Tweet{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", language='" + language + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }
}

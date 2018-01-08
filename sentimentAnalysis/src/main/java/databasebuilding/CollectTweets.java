package databasebuilding;

import dao.DAOTweets;
import model.Tweet;
import twitter4j.*;

/**
 * Not used in the project anymore.
 */
public class CollectTweets {

    public static void main(String[] args) throws TwitterException {
        collectTweets();
    }

    private static void collectTweets(){

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        StatusListener listener = new StatusListener() {

            DAOTweets dao = new DAOTweets();

            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText() + " - " + status.getCreatedAt());
                Tweet tweet = new Tweet(status.getUser().getScreenName(), status.getText(), status.getCreatedAt(), status.getLang(), "");
                dao.insertTweet(tweet);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);

        twitterStream.sample("en");
    }
}

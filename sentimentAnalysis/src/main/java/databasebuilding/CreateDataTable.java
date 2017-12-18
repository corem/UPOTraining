package databasebuilding;

import com.mongodb.client.FindIterable;
import dao.DAOTweetsNGrams;
import file.CreateCSV;
import org.bson.Document;

import java.util.*;

/**
 * This class is used to apply a sentiment label to each Tweets of the MongoDB database.
 */
public class CreateDataTable {

    //private static String FILE_OUTPUT = "csvMatrix.csv";
    //private static int CHUNK_SIZE = 0;

    private static int CHUNK_SIZE = 5000;
    private static String FILE_OUTPUT = "csvMatrixChunk"+String.valueOf(CHUNK_SIZE)+".csv";

    public static void main(String args[]){

        //Used to gain time while executing code in IDE.
        if(args.length != 0){
            FILE_OUTPUT = "../sentimentAnalysis/csvMatrixChunk"+args[0]+".csv";
            CHUNK_SIZE = Integer.parseInt(args[0]);
        }
        createDataTable();
    }

    public static void createDataTable(){
        DAOTweetsNGrams daoTweetsNGrams = new DAOTweetsNGrams();
        FindIterable<Document> allTweet = daoTweetsNGrams.getAllTweet();
        Set<String> setOfWords = new LinkedHashSet<String>();
        List<String> listOfWords = new ArrayList<String>();

        CreateCSV createCSV = new CreateCSV(FILE_OUTPUT);

        HashMap<String, Integer> tokenCount = getTweetHashMap(allTweet);

        removeAppearingOnlyOnceToken(setOfWords, tokenCount);

        //Convert the set to an arraylist
        listOfWords.addAll(setOfWords);

        //Add the "sentimentLabelValue" column in CSV Header
        listOfWords.add("sentimentLabelValue");

        //Write the CSV Header
        createCSV.writeOneLine(listOfWords);

        //Remove the "sentimentLabelValue" before checking if tokens are in the set.
        listOfWords.remove(listOfWords.size()-1);

        List<String> tokenPresence;
        List<String> tweetTokens;

        int i = 0;

        for(Document document : allTweet){
            System.out.println("Traitement du tweet n° : "+i);

            //Store the lemmas of a Tweet
            tweetTokens = (List<String>)document.get("ngrams");

            for(String string : tweetTokens){
                System.out.println(string);
            }

            //For each lemma in the list of words, check if this tweet contains the lemma.
            tokenPresence = new ArrayList<String>();
            for(String token:listOfWords){
                if(tweetTokens.contains(token)){
                    tokenPresence.add("1");
                }else{
                    tokenPresence.add("0");
                }
            }

            String sentimentAsNumber = getSentimentAsNumber((String)document.get("sentiment"));
            tokenPresence.add(sentimentAsNumber);
            createCSV.writeOneLine(tokenPresence);
            i++;
            if(i == CHUNK_SIZE){
                break;
            }
        }
    }

    private static void removeAppearingOnlyOnceToken(Set<String> setOfWords, HashMap<String, Integer> tokenCount) {
        Iterator it = tokenCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            if((Integer)pair.getValue() > 1){
                setOfWords.add((String)pair.getKey());
            }
            it.remove();
        }
    }

    private static HashMap<String, Integer> getTweetHashMap(FindIterable<Document> allTweet) {
        HashMap<String, Integer> tokenCount = new HashMap<String, Integer>();

        int i = 0;
        for(Document document : allTweet){
            List<String> lemmas = (List<String>)document.get("ngrams");

            for(String lemma : lemmas){
                if(tokenCount.containsKey(lemma)){
                    tokenCount.put(lemma, tokenCount.get(lemma) +1);
                }else{
                    tokenCount.put(lemma, 1);
                }
            }
            i++;
            System.out.println("Selection of the tweets n°"+i);
            if(i == CHUNK_SIZE){
                break;
            }
        }
        return tokenCount;
    }

    private enum Sentiment {
        very_negative, negative, neutral, positive, very_positive
    }

    private static String getSentimentAsNumber(String sentiment){
        Sentiment sentimentEnum = Sentiment.valueOf(sentiment);
        String sentimentAsNumber = null;
        switch(sentimentEnum) {
            case very_negative:
                sentimentAsNumber = "0";
            break;
            case negative:
                sentimentAsNumber = "0";
            break;
            case neutral:
                sentimentAsNumber = "1";
            break;
            case positive:
                sentimentAsNumber = "2";
            break;
            case very_positive:
                sentimentAsNumber = "2";
            break;
        }
        return sentimentAsNumber;
    }

}

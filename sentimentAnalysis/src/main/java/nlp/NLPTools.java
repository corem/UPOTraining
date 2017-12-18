package nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.simple.SentimentClass;
import edu.stanford.nlp.util.CoreMap;
import model.Tweet;
import model.TweetNGrams;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitary class used to preprocess the Tweets.
 */

public class NLPTools {

    private static TokenizerFactory<Word> tf = null;

    private final static String URL_REGEX = "((www\\.[\\s]+)|(https?://[^\\s]+)|[pic.]?twitter.)";
    private final static String CONSECUTIVE_CHARS = "([a-z])\\1{1,}";
    private final static String STARTS_WITH_NUMBER = "[1-9]\\s*(\\w+)";
    private final static String PUNCTUATION = "\\p{Punct}";
    private final static String USER_REGEX = "@([^\\s]+)";

    static final WordNet wordNet = new WordNet();

    static List<String> stopwords = Arrays.asList("a", "able", "about",
            "across", "after", "all", "almost", "also", "am", "among", "an",
            "and", "any", "are", "as", "at", "b", "be", "because", "been",
            "but", "by", "c", "can", "cannot", "could", "d", "dear", "did",
            "do", "does", "e", "either", "else", "ever", "every", "f", "for",
            "from", "g", "get", "got", "h", "had", "has", "have", "he", "her",
            "hers", "him", "his", "how", "however", "http", "https", "hypertext_transfer_protocol", "i", "if", "in", "into",
            "is", "it", "its", "j", "just", "k", "l", "least", "let", "like",
            "likely", "lrb", "m", "may", "me", "might", "most", "must", "my",
            "neither", "n", "no", "nor", "not", "o", "of", "off", "often",
            "on", "only", "or", "other", "our", "own", "p", "q", "r", "rather", "rrb",
            "s", "said", "say", "says", "she", "should", "since", "so", "some",
            "t", "than", "that", "the", "their", "them", "then", "there",
            "these", "they", "this", "tis", "to", "too", "twas", "u", "us",
            "v", "w", "wants", "was", "we", "were", "what", "when", "where",
            "which", "while", "who", "whom", "why", "will", "with", "would",
            "x", "y", "yet", "you", "your", "z", "-lrb-", "-rrb-", "-lcb-", "-lsb-", "-rsb-", "-rcb-");

    static Properties props = new Properties();

    public static List<Word> getTokens(String sentence)
    {
        if(tf == null)
            tf = PTBTokenizer.factory();

        List<Word> tokensWords = tf.getTokenizer(new StringReader(sentence)).tokenize();

        return tokensWords;
    }

    public static List<String> getLemmasList(String text) {
        Sentence sentence = null;
        try{
            sentence = new Sentence(text);
        }catch(IllegalStateException e){
            System.out.println("TODO : Empty sentence");
        }
        List<String> lemmas = sentence.lemmas();
        return lemmas;
    }

    public static String getSentiment(String text) {
        Sentence sentence = new Sentence(text);
        SentimentClass sentiment = sentence.sentiment();
        return sentiment.toString().toLowerCase();
    }

    public static TweetNGrams nlpPipe(Tweet tweet){
        props.setProperty("annotators", "tokenize, ssplit, pos, parse, lemma, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(tweet.getMessage());

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<String> lemmas = new ArrayList<String>();

        String sentiment = null;

        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase();
                if(!isPunctuation(lemma)){
                    lemmas.add(lemma);
                }
            }
            sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class).toLowerCase();
        }

        lemmas.removeAll(stopwords);

        TweetNGrams tweetNGrams = new TweetNGrams(tweet.getAuthor(), tweet.getMessage(), tweet.getDate(), tweet.getLanguage(), lemmas, sentiment);
        return tweetNGrams;
    }

    public static boolean isPunctuation(String string){
        //String pattern = "\\p{Punct}&&(^@)";
        String pattern = "[\\p{Punct}&&[^@#]]";
        Pattern r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(string);

        return m.find();
    }

    public static String removeUrl(String tweet){
        return tweet.replaceAll(URL_REGEX, "");
    }

    public static String removeUserName(String tweet){
        return tweet.replaceAll(USER_REGEX, "");
    }

    public static List<String> removeSpecialCharacters(List<String> tokens){
        List<String> cleanedUpList = new ArrayList<String>();

        for(String token:tokens){
            String tokenToClean = token;
            tokenToClean = tokenToClean.replaceAll(PUNCTUATION, "");
            if(!tokenToClean.equals("")){
                cleanedUpList.add(tokenToClean);
            }
        }

        return cleanedUpList;
    }

    public static String removeStartingWithNumber(String tweet){
        return tweet.replaceAll(STARTS_WITH_NUMBER, "");
    }

    public static String removeConsecutiveChars(String tweet){
        return tweet.replaceAll(CONSECUTIVE_CHARS, "$1");
    }

    public static List<String> removeStopWords(List<String> lemmas){
        lemmas.removeAll(stopwords);
        return lemmas;
    }

    public static List<String> preprocessTweet(String tweet){
        String tweetMessage = tweet.toLowerCase();
        tweetMessage = removeUrl(tweetMessage);
        tweetMessage = removeUserName(tweetMessage);
        //tweetMessage = removeSpecialCharacters(tweetMessage);
        tweetMessage = removeStartingWithNumber(tweetMessage);

        List<String> lemmas = getLemmasList(tweetMessage);
        ArrayList<String> lemmasArray = new ArrayList<String>(lemmas);

        lemmasArray = new ArrayList<String>(NLPTools.removeStopWords(lemmasArray));

        List<String> lemmasWordNet = new ArrayList<String>();

        for(String string : lemmasArray){
            lemmasWordNet.add(wordNet.getSynonym(string));
        }

        lemmasWordNet = removeStopWords(lemmasWordNet);

        List<String> returnList = removeSpecialCharacters(lemmasWordNet);

        return returnList;
    }


}

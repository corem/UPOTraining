package sandbox;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import dao.DAOTweets;
import deeplearning.TrainModelCSV;
import model.Tweet;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.bson.Document;

import java.io.StringReader;

public class Main {

    public static void main(String args[]) throws Exception{

        String theSentence = "this is a dog and some cat";
        StringReader reader = new StringReader(theSentence);
        StandardTokenizer source = new StandardTokenizer(Version.LUCENE_46, reader);
        TokenStream tokenStream = new StandardFilter(Version.LUCENE_46, source);
        ShingleFilter sf = new ShingleFilter(tokenStream, 2, 3);
        sf.setOutputUnigrams(false);

        CharTermAttribute charTermAttribute = sf.addAttribute(CharTermAttribute.class);
        sf.reset();

        while (sf.incrementToken()) {
            System.out.println(charTermAttribute.toString());
        }

        sf.end();
        sf.close();
    }

        //String test = "cats running ran cactus cactuses cacti community communities";
        //String negative = "This place is a fucking hell. I hate you.";
        //String positive = "I'm really happy to @BarackObama be here # with you, guys. This place is awesome.";
        //String positive2 = "yes Republic of Catalonia is a free country now Peaceful world greetings for Catalonia on this Auspicious day automobile auto cat dog";

        //Tweet tweet = new Tweet("Author", positive, new Date(), "en");

        //List<Word> listWords = NLPTools.getTokens(test);

        /*for(Word w : listWords){
            System.out.println(w.word());
        }*/

        /*List<String> lemmas = NLPTools.getLemmasList(positive2);

        for(String str : lemmas){
            System.out.println(str);
        }*/

        //String string = "@BarackObama";
        //System.out.println(NLPTools.isPunctuation(string));

        //System.out.println(NLPTools.getSentiment(positive));

        /*WordNet wordNet = new WordNet();
        for(String lemma:lemmas){
            System.out.println(wordNet.getSynonym(lemma));
        }*/

        //System.out.println(tweetNGrams);

        //String testRemove = "L'arbre, l\"arbre";
        //String removed = testRemove.replaceAll("['\"]", " ");

        //System.out.println(removed);

        //String urlTest = "http://www.lesoir.be bienvenue @barackobama #usa I'm soooooo happy pic.twitter.com twitter.com to see you here 1999 Mexico won't recognize and independent Catalonia pic.twitter.com/ITWkBSuaMw";
        //String urlTest2 = "In a world of lrb belligerent ( nationalisms ) { [ ] } lrb rrb, it’s not just Catalonia that needs cool-headed mediation http://www. friendsofeurope.org/publication/wo rld-belligerent-nationalisms-its-not-just-catalonia-needs-cool-headed-mediation …";
        //Sentence sentence = new Sentence(urlTest);

        //List<String> lemmas = sentence.lemmas();

        /*for(String string: lemmas){
            System.out.println(string);
        }*/

       // System.out.println(NLPTools.removeUrl(urlTest));

        //System.out.println(urlTest.replaceAll("\\S+://\\S+", ""));
        //ArrayList<String> listTest = new ArrayList<String>(NLPTools.getLemmasList(urlTest2));
        //System.out.println(NLPTools.removeStopWords(listTest));

        //System.out.println(NLPTools.preprocessTweet(urlTest2));

        //TrainModelCSV.main(ArrayUtils.toArray());
    //}

    /*public static void main(String args[]){

        countNumberOfTweets();

    }

    public static void countNumberOfTweets(){

        final DAOTweets daoTweets = new DAOTweets();

        FindIterable<Document> allTweet = daoTweets.getAllTweet();
        allTweet.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Tweet tweet = DAOTweets.documentToTweet(document);

                System.out.println(tweet.getDate());


            }
        });

    }*/



}

package sandbox;

import deeplearning.TrainModelCSV;
import org.apache.commons.lang3.ArrayUtils;

public class Main {

    public static void main(String args[]) throws Exception{

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

        TrainModelCSV.main(ArrayUtils.toArray());
    }

}

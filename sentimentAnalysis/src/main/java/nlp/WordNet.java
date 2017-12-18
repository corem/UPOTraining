package nlp;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Utilitary class used to access the Wordnet dictionnary of synonyms.
 */
public class WordNet {

    IDictionary dict = null;

    public WordNet(){
        dict = getDict();
    }

    private IDictionary getDict() {
        URL url = null;
        try {
            url = new URL("file", null, getClass().getClassLoader().getResource("dict").getFile().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        IDictionary dict = new Dictionary(url);
        try {
            dict.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dict;
    }

    public String getSynonym(String wordToChange){
        IIndexWord idxWord = dict.getIndexWord(wordToChange, POS.NOUN);
        if(idxWord != null){
            IWordID wordID = idxWord.getWordIDs().get(0);
            IWord word = dict.getWord(wordID);
            ISynset synset = word.getSynset();
            return synset.getWords().get(0).getLemma().toLowerCase();
        }else{
            return wordToChange;
        }
    }

}

package databasebuilding;

import file.CreateCSV;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to remove Tweets with Symmetrical Uncertainty equal to zero from the dataset.
 */
public class ApplySymmetricalUncertainty {

    private static String FILE_INPUT;
    private static String FILE_OUTPUT;

    public static void main(String args[]){

        if(args.length < 2){
            System.out.println("Usage : ApplySymmetricalUncertainty inputFile outputFile");
            System.exit(0);
        }

        FILE_INPUT = "src/main/resources/"+args[0];
        FILE_OUTPUT = "src/main/resources/"+args[1];

        applySymmetricalUncertainty();
    }

    public static void applySymmetricalUncertainty(){
        CreateCSV createCSV = new CreateCSV(FILE_OUTPUT);

        int[] sentimentArray = extractSentimentArray();

        //System.out.println("Size of sentimentArray : " + sentimentArray.length);

        removeSU(createCSV, sentimentArray);
    }

    private static void removeSU(CreateCSV createCSV, int[] sentimentArray) {
        Iterable<CSVRecord> records = getCsvRecords();
        Iterator<CSVRecord> iterator = records.iterator();

        CSVRecord record;

        while(iterator.hasNext()) {
            record = iterator.next();
            if (iterator.hasNext()) {
                Iterator<String> tokenPresence = record.iterator();
                ArrayList<Integer> tokenPresenceList = new ArrayList<>();

                tokenPresence.next();
                while (tokenPresence.hasNext()) {
                    Integer presence = Integer.parseInt(tokenPresence.next());
                    //System.out.println(presence);

                    tokenPresenceList.add(presence);
                }
                int[] tokenPresenceArray = tokenPresenceList.stream().mapToInt(i -> i).toArray();

                double su = SymmetricalUncertaintyUtils.symmetricalUncertainty(tokenPresenceArray, 2, sentimentArray, 5);
                System.out.println("SU : "+ su);
                if (su != 0.0) {
                    createCSV.writeOneLine(record);
                }
            }
        }

        ArrayList<String> sentimentLine = new ArrayList<>();
        sentimentLine.add("sentimentLabelValue");
        for(int i = 0; i < sentimentArray.length; i++){
            sentimentLine.add(String.valueOf(sentimentArray[i]));
        }

        createCSV.writeOneLine(sentimentLine);
    }

    private static Iterable<CSVRecord> getCsvRecords() {
        Reader in = null;
        try {
            in = new FileReader(FILE_INPUT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.DEFAULT.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static int[] extractSentimentArray(){
        Iterable<CSVRecord> records = getCsvRecords();

        Iterator<CSVRecord> iterator = records.iterator();

        CSVRecord record = null;

        while(iterator.hasNext()){
            record = iterator.next();
            //System.out.println(record.get(0));
        }

        Iterator<String> iteratorSentimentValue = record.iterator();

        iteratorSentimentValue.next();

        ArrayList<Integer> sentimentList = new ArrayList<>();

        while(iteratorSentimentValue.hasNext()){
            Integer sentiment = Integer.parseInt(iteratorSentimentValue.next());
            sentimentList.add(sentiment);
            System.out.println("Sentiment : " +sentiment);
        }

        return sentimentList.stream().mapToInt(i -> i).toArray();
    }


}

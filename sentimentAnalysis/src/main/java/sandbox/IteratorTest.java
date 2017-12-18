package sandbox;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IteratorTest {

    public static void main(String args[]){

        /*Reader in = null;

        try {
            in = new FileReader("csvMatrix.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Iterable<CSVRecord> records = null;

        try {
            records = CSVFormat.DEFAULT.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<CSVRecord> iterator = records.iterator();

        for(int i = 0; i < 10; i++){
            System.out.println("Iteration : "+i);
            while(iterator.hasNext()){
                System.out.println(iterator.next().get(0));
            }
            iterator = records.iterator();
        }*/


        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader("csvMatrixChunk50.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String [] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + nextLine[1] + "etc...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}

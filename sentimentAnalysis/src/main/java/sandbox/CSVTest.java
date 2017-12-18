package sandbox;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

public class CSVTest {

    public static void main(String args[]){
        /*CreateCSV createCSV = new CreateCSV("testCSV.csv");
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        createCSV.writeOneLine(list);
        createCSV.writeOneLine(list);*/



        Reader in = null;
        try {
            in = new FileReader("csvMatrix.csv");
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            System.out.println(records.iterator().next().get(86100));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            System.out.println(count("csvMatrix.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public static int count(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }
}

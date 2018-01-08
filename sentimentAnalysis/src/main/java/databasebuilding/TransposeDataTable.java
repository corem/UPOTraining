package databasebuilding;

import com.opencsv.CSVReader;
import file.CreateCSV;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Utilitary class used to transpose a CSV file.
 */
public class TransposeDataTable {

    private static String FILE_INPUT = "csvMatrixChunk500TransposeSU.csv";
    private static String FILE_OUTPUT = "csvMatrixChunk500TransposeSUTranspose.csv";

    public static void main(String args[]){

        if(args.length < 2){
            System.out.println("Usage : TransposeDataTable inputFile outputFile");
            System.exit(0);
        }

        FILE_INPUT = args[0];
        FILE_OUTPUT = args[1];

        CreateCSV createCSV = new CreateCSV(FILE_OUTPUT);
        Reader in = null;
        ArrayList<String> csvLine;

        CSVReader reader = openCSVFile();

        int headerSize = getHeaderSize(reader);

        transposeDataTable(createCSV, reader, headerSize);
    }

    private static void transposeDataTable(CreateCSV createCSV, CSVReader reader, int headerSize) {
        ArrayList<String> csvLine;
        String [] nextLine;

        for(int i = 0; i < headerSize; i++){
            System.out.println("Begin iteration n°"+i);

            csvLine = new ArrayList<String>();

            try {
                reader = new CSVReader(new FileReader(FILE_INPUT));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                while ((nextLine = reader.readNext()) != null){
                    String cell = nextLine[i];
                    System.out.println("Adding "+cell+" to record n° "+i);
                    csvLine.add(cell);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            createCSV.writeOneLine(csvLine);
            //System.out.println("End iteration n°"+i);
        }
    }

    private static CSVReader openCSVFile() {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(FILE_INPUT));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private static int getHeaderSize(CSVReader reader) {
        int headerSize = reader.iterator().next().length;
        System.out.println(headerSize);
        return headerSize;
    }

}

package file;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Utilitary class used to write in CSV files.
 */
public class CreateCSV {

    private String fileName;

    private FileWriter fileWriter = null;
    private CSVPrinter csvFilePrinter = null;
    private CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");

    public CreateCSV(String fileName){
        super();
        this.fileName = fileName;
    }

    public void writeOneLine(Iterable line){
        try {
            fileWriter = new FileWriter(fileName, true);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter.");
                e.printStackTrace();
            }
        }
    }

}

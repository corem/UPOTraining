package deeplearning;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.arbiter.optimize.api.data.DataProvider;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.MultipleEpochsIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.shade.jackson.annotation.JsonProperty;

import java.io.*;
import java.util.Map;

public class TweetDataProvider implements DataProvider {
    private int numEpochs;
    private int batchSize;

    private static String INPUT_FILE = "csvMatrixChunk1000TransposeSUTranspose.csv";
    private static ListDataSetIterator listTrainingDataSetIterator = null;
    private static ListDataSetIterator listTestDataSetIterator = null;

    public TweetDataProvider(@JsonProperty("numEpochs") int numEpochs, @JsonProperty("batchSize") int batchSize) throws Exception{
        this.numEpochs = numEpochs;
        this.batchSize = batchSize;

        int numLinesToSkip = 1;
        char delimiter = ',';
        RecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter);
        ClassPathResource classPathResource = new ClassPathResource(INPUT_FILE);
        File file = classPathResource.getFile();
        FileSplit fileSplit = new FileSplit(file);
        recordReader.initialize(fileSplit);

        int columnNumber = getCSVColumnNumber();

        int labelIndex = columnNumber - 1;
        int numClasses = 5;
        int batchSize2 = 500;

        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,batchSize2,labelIndex,numClasses);
        DataSet allData = iterator.next();
        allData.shuffle();
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);  //Use 65% of data for training

        DataSet trainingData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        listTrainingDataSetIterator = new ListDataSetIterator(trainingData.asList(), 50);
        listTestDataSetIterator = new ListDataSetIterator(testData.asList(), 50);
    }

    private TweetDataProvider() throws Exception{

    }


    @Override
    public Object trainData(Map<String, Object> dataParameters) {
        return new MultipleEpochsIterator(numEpochs, listTrainingDataSetIterator);
    }

    @Override
    public Object testData(Map<String, Object> dataParameters) {
        return listTestDataSetIterator;
    }

    @Override
    public Class<?> getDataType() {
        return DataSetIterator.class;
    }

    public static int getCSVColumnNumber(){
        Reader in = null;
        try {
            in = new FileReader(INPUT_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Iterable<CSVRecord> records = null;
        try {
            records = CSVFormat.DEFAULT.parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records.iterator().next().size();
    }
}


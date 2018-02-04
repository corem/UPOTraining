package deeplearning;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.*;

public class TrainModelCSV {

    private static String INPUT_FILE;
    private static int seed = 5;
    private static int iterations = 1000;
    private static int outputNum = 3;
    private static int layer2Size = 100;
    private static int layer3size = 100;

    public static void main(String[] args) throws  Exception {

        if(args.length < 1){
            System.out.println("Usage : TrainModelCSV inputFile");
            System.exit(0);
        }

        INPUT_FILE = args[0];

        int numLinesToSkip = 1;
        char delimiter = ',';
        RecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter);

        System.out.println("Loading file...");
        ClassPathResource classPathResource = new ClassPathResource(INPUT_FILE);

        //System.out.println(classPathResource.getFile().getAbsolutePath());

        File file = classPathResource.getFile();
        FileSplit fileSplit = new FileSplit(file);
        recordReader.initialize(fileSplit);

        System.out.println("Get file column number...");
        int columnNumber = getCSVColumnNumber();

        int labelIndex = columnNumber - 1;
        int batchSize = 1000;

        System.out.println("Initialize dataset iterator...");
        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,batchSize,labelIndex,outputNum);

        System.out.println("Get iterator.next()");
        DataSet allData = iterator.next();

        System.out.println("Shuffling data");
        allData.shuffle();

        System.out.println("Spliting training and test data");
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);  //Use 65% of data for training
        DataSet trainingData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        //ListDataSetIterator listDataSetIterator = new ListDataSetIterator((Collection) trainingData, 500);

        //DataSetIteratorFactoryProvider dataSetIteratorFactoryProvider = new DataSetIteratorFactoryProvider();

        //We need to normalize our data. We'll use NormalizeStandardize (which gives us mean 0, unit variance):
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
        normalizer.transform(trainingData);     //Apply normalization to the training data
        normalizer.transform(testData);         //Apply normalization to the test data. This is using statistics calculated from the *training* set

        final int numInputs = columnNumber-1;

        System.out.println("Build model...");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iterations)
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .learningRate(0.1)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(layer2Size)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(layer2Size).nOut(layer3size)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(layer3size).nOut(outputNum).build())
                .backprop(true).pretrain(false)
                .build();

        System.out.println("Running model...");
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));

        model.fit(trainingData);

        //MultiLayerNetwork bestModel = findBestNetwork();

        //evaluate the model on the test set
        System.out.println("Print model...");
        Evaluation eval = new Evaluation(outputNum);
        INDArray output = model.output(testData.getFeatureMatrix());
        eval.eval(testData.getLabels(), output);
        System.out.println(eval.stats());
    }

    /*
    private static MultiLayerNetwork findBestNetwork() throws Exception {

        ParameterSpace<Double> learningRateHyperparam = new ContinuousParameterSpace(0.0001, 0.1);  //Values will be generated uniformly at random between 0.0001 and 0.1 (inclusive)
        ParameterSpace<Integer> layerSizeHyperparam = new IntegerParameterSpace(16,256);

        int numInputs = TweetDataProvider.getCSVColumnNumber();

        MultiLayerSpace hyperparameterSpace = new MultiLayerSpace.Builder()
                .seed(seed)
                .iterations(iterations)
                //.activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .learningRate(learningRateHyperparam)
                .regularization(true)
                .l2(1e-4)
                .addLayer(new DenseLayerSpace.Builder()
                        .nIn(numInputs)
                        .nOut(layerSizeHyperparam)
                        .build())
                //.addLayer(new DenseLayerSpace.Builder()
                //        .nIn(100)
                //        .nOut(100)
                //        .build())
                .addLayer(new OutputLayerSpace.Builder()
                        .activation(Activation.SOFTMAX)
                        .nIn(layerSizeHyperparam)
                        .nOut(outputNum)
                        .build())
                //.backprop(true).pretrain(false)
                .build();

        CandidateGenerator candidateGenerator = new RandomSearchGenerator(hyperparameterSpace, null);    //Alternatively: new GridSearchCandidateGenerator<>(hyperparameterSpace, 5, GridSearchCandidateGenerator.Mode.RandomOrder);

        ScoreFunction scoreFunction = new TestSetAccuracyScoreFunction();

        TerminationCondition[] terminationConditions = {
                new MaxTimeCondition(15, TimeUnit.MINUTES),
                new MaxCandidatesCondition(20)};

        ResultSaver modelSaver = new InMemoryResultSaver();

        DataProvider dataProvider = new TweetDataProvider(10, 20);

        OptimizationConfiguration configuration = new OptimizationConfiguration.Builder()
                .candidateGenerator(candidateGenerator)
                .dataProvider(dataProvider)
                .modelSaver(modelSaver)
                .scoreFunction(scoreFunction)
                .terminationConditions(terminationConditions)
                .build();

        IOptimizationRunner runner = new LocalOptimizationRunner(configuration, new MultiLayerNetworkTaskCreator());

        runner.execute();

        String s = "Best score: " + runner.bestScore() + "\n" +
                "Index of model with best score: " + runner.bestScoreCandidateIndex() + "\n" +
                "Number of configurations evaluated: " + runner.numCandidatesCompleted() + "\n";
        System.out.println(s);

        int indexOfBestResult = runner.bestScoreCandidateIndex();
        List<ResultReference> allResults = runner.getResults();

        OptimizationResult bestResult = allResults.get(indexOfBestResult).getResult();
        MultiLayerNetwork bestModel = (MultiLayerNetwork)bestResult.getResult();

        System.out.println("\n\nConfiguration of best model:\n");
        System.out.println(bestModel.getLayerWiseConfigurations().toJson());

        return bestModel;
    }
    */

    /*private static DataProvider getDataProvider() throws Exception{
        //First: get the dataset using the record reader. CSVRecordReader handles loading/parsing
        int numLinesToSkip = 1;
        char delimiter = ',';
        RecordReader recordReader = new CSVRecordReader(numLinesToSkip,delimiter);
        ClassPathResource classPathResource = new ClassPathResource(INPUT_FILE);
        File file = classPathResource.getFile();
        FileSplit fileSplit = new FileSplit(file);
        recordReader.initialize(fileSplit);

        int columnNumber = getCSVColumnNumber(INPUT_FILE);

        //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
        int labelIndex = columnNumber - 1;
        int numClasses = 5;
        int batchSize = 500;

        DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader,batchSize,labelIndex,numClasses);
        DataSet allData = iterator.next();
        allData.shuffle();
        SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);  //Use 65% of data for training

        DataSet trainingData = testAndTrain.getTrain();
        DataSet testData = testAndTrain.getTest();

        ListDataSetIterator listTrainingDataSetIterator = new ListDataSetIterator((Collection) trainingData);
        ListDataSetIterator listTestDataSetIterator = new ListDataSetIterator((Collection) testData);

        //ListDataSetIterator listDataSetIterator = new ListDataSetIterator((Collection) allData);

        //DataSetIteratorFactoryProvider dataSetIteratorFactoryProvider = new DataSetIteratorFactoryProvider();

        DataProvider dataProvider = new DataSetIteratorFactoryProvider();
    }*/

    public static int getCSVColumnNumber(){
        Reader in = null;
        try {

            ClassPathResource classPathResource = new ClassPathResource(INPUT_FILE);

            String file = classPathResource.getFile().getAbsolutePath();

            //in = new FileReader(INPUT_FILE);
            in = new FileReader(file);
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

import classification.crossValidation.CrossValidation;
import classification.inducer.LibLINEAR;
import exceptions.ArffLoadException;
import exceptions.ClassifierBuildingException;
import exceptions.EvaluationException;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.util.DataLoaderHelper.loadData;

/**
 * Created by Jean Metz.
 */
public class Runner {

    public static final String DEFAULT_OPTIONS = "-S 1 -C 1 -E 000.1 -B 1.0";
    public static final String COLUMN_SEPARATOR = ";";

    private final Logger logger = Logger.getLogger(Runner.class.getName());
    private final AbstractClassifier classifier = new LibLINEAR();
    private final CrossValidation validationMethod;
    private final String options;
    private final String srcFile;
    private final int nFolds;
    private String relationName;
    private Map<Integer, Evaluation> results;

    public Runner(final String srcFile, final int nFolds, final String classifierOptions, CrossValidation validationMethod) {
        checkNotNull(srcFile);
        checkNotNull(validationMethod);
        checkArgument(nFolds > 1); // cross-validation check!!!

        this.srcFile = srcFile;
        this.nFolds = nFolds;
        this.validationMethod = validationMethod;

        options = Optional.ofNullable(classifierOptions).orElse(DEFAULT_OPTIONS);
        if(classifierOptions == null)
            logger.info("Using default LibLINEAR options");

        try {
            classifier.setOptions(weka.core.Utils.splitOptions(options));
            logger.info("Inducer ready to learn.");
            logger.info(this.srcFile + "; " + classifier.toString());
        } catch (Exception e) {
            logger.info("Wrong LibLINEAR options format.");
            throw new IllegalArgumentException("Wrong LibLINEAR options format.");
        }
    }

    public Runner(final String srcFile, final int nFolds, CrossValidation validationMethod) {
        this(srcFile, nFolds, DEFAULT_OPTIONS, validationMethod);
    }


    public void run() {
        Instances data = null;
        try {
            data = loadData(srcFile);
        } catch (ArffLoadException e) {
            System.out.println("Could not load input data: " + e.getMessage());
            System.exit(0);
        }

        relationName = data.relationName();
        logger.info("Data loaded");

        try {
            results = validationMethod.perform(classifier, data, this.nFolds);
            logger.info("Classifier evaluated");
        } catch (EvaluationException e) {
            e.printStackTrace();
        } catch (ClassifierBuildingException e) {
            e.printStackTrace();
        }
    }

    public String resultAsString() {
        StringBuilder output = new StringBuilder();
        String identifier = relationName + COLUMN_SEPARATOR + classifier.toString() + COLUMN_SEPARATOR + String.join(" ", classifier.getOptions());
        for (int fold = 0; fold < this.nFolds; ++fold) {
            String id = identifier + COLUMN_SEPARATOR + fold;
            output.append(output(id, results.get(fold))).append("\n");
        }
        return output.toString();
    }

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("Source file", srcFile)
                .add("Relation name", relationName)
                .add("Classifier", classifier)
                .add("Options", options)
                .add("Number of folds", nFolds)
                .toString();
    }

    private String output(String identifier, Evaluation eval) {

        StringBuilder sb = new StringBuilder();

        sb.append(identifier).append(COLUMN_SEPARATOR);
        sb.append(eval.weightedAreaUnderPRC()).append(COLUMN_SEPARATOR);
        sb.append(eval.weightedAreaUnderROC()).append(COLUMN_SEPARATOR);
        sb.append(eval.weightedFMeasure()).append(COLUMN_SEPARATOR);
        sb.append(eval.unweightedMacroFmeasure()).append(COLUMN_SEPARATOR);
        sb.append(eval.unweightedMicroFmeasure()).append(COLUMN_SEPARATOR);
        sb.append(eval.errorRate()).append(COLUMN_SEPARATOR);
        sb.append(eval.pctCorrect()).append(COLUMN_SEPARATOR);

        logger.info(String.format("Results for %s generated", identifier));
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println("Use ExpRunnerTest unit test to run the demo :)");
    }

}

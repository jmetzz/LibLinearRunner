package classification.crossValidation;

import classification.InstancesHelper;
import classification.SplitEvaluator;
import exceptions.ClassifierBuildingException;
import exceptions.EvaluationException;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean Metz.
 */
public class Nx2FoldCV implements CrossValidation {

    private final int N;
    private final Classifier classifier;
    private final Instances data;
    private final int nFolds;
    private final int seed;

    public Nx2FoldCV(Classifier classifier, Instances data, int nFolds, int n) {
       this(classifier, data, nFolds, n, 1);
    }

    public Nx2FoldCV(Classifier classifier, Instances data, int nFolds, int n, int seed) {
        this.N = n;
        this.classifier = classifier;
        this.data = data;
        this.nFolds = nFolds;
        this.seed = seed;
    }

    @Override
    public Map<Integer, Evaluation> perform(Classifier c, Instances source, int nFolds) throws EvaluationException {

        Map<Integer, Evaluation> results = new HashMap<Integer, Evaluation>();

        if (source.classIndex() == -1)
            source.setClassIndex(source.numAttributes() - 1);
        try {
            for (int iteration = 0; iteration < N; iteration++) {
                Instances[] data = InstancesHelper.splitData(source, 0.5, seed + iteration, true, true);
                results.put(iteration, SplitEvaluator.evaluate(c, data[0], data[1]));
                results.put(iteration + 1, SplitEvaluator.evaluate(c, data[1], data[0]));
            }
        } catch (ClassifierBuildingException e) {
            e.printStackTrace();
        }

        return results;
    }
}

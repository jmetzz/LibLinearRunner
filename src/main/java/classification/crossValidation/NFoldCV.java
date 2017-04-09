package classification.crossValidation;

import exceptions.ClassifierBuildingException;
import exceptions.EvaluationException;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import classification.SplitEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jean Metz.
 */
public class NFoldCV implements CrossValidation {


    @Override
    public Map<Integer, Evaluation> perform(Classifier classifier, Instances source, int nFolds) throws EvaluationException, ClassifierBuildingException {
        if (source.classIndex() == -1)
            source.setClassIndex(source.numAttributes() - 1);

        Map<Integer, Evaluation> results = new HashMap<>();

        for (int fold = 0; fold < nFolds; fold++) {

            Instances train = source.trainCV(nFolds, fold);
            Instances test = source.testCV(nFolds, fold);

            Evaluation eval = SplitEvaluator.evaluate(classifier, train, test);

            results.put(fold, eval);
        }
        return results;
    }

}

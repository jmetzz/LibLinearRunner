package classification;

import exceptions.ClassifierBuildingException;
import exceptions.EvaluationException;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

/**
 * Created by Jean Metz.
 */
public class SplitEvaluator {


    public static Evaluation evaluate(Classifier classifier, Instances train, Instances test)
            throws ClassifierBuildingException, EvaluationException {
        // setting class attribute if the data format does not provide this
        // information. For example, the XRFF format saves the class attribute information as
        // well.
        // Assumes default class attribute as the last one
        if (train.classIndex() == -1)
            train.setClassIndex(train.numAttributes() - 1);

        try {
            classifier.buildClassifier(train);
        } catch (Exception e) {
            throw new ClassifierBuildingException("WEKA Classifier build error", e);
        }

        // perform classifier and print some statistics
        Evaluation eval = null;
        try {
            eval = new Evaluation(train);
            eval.evaluateModel(classifier, test);
        } catch (Exception e) {
            throw new EvaluationException("WEKA Evaluation error", e);
        }

        if (eval == null)
            throw new AssertionError("Unknown Weka Exception occurred.");

        return eval;
    }


    public static Evaluation evaluate(Classifier classifier, Instances source,
                                      double split,
                                      long seed,
                                      boolean shuffle,
                                      boolean stratify
    )
            throws ClassifierBuildingException, EvaluationException {

        Instances[] data = InstancesHelper.splitData(source, split, seed, shuffle, stratify);

       return evaluate(classifier, data[0], data[1]);
    }

}

package classification.crossValidation;

import exceptions.ClassifierBuildingException;
import exceptions.EvaluationException;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;

import java.util.Map;

public interface CrossValidation {

    Map<Integer, Evaluation> perform(Classifier c, Instances source, int nFolds) throws EvaluationException, ClassifierBuildingException;

}

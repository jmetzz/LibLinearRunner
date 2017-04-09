package classification;

import exceptions.ClassifierBuildingException;
import exceptions.EvaluationException;
import weka.core.Instances;

import java.util.Random;

/**
 * Created by Jean Metz.
 */
public class InstancesHelper {

    public static Instances[] splitData(Instances source, double split, long seed,
                                        boolean shuffle, boolean stratify)
            throws IllegalArgumentException, EvaluationException, ClassifierBuildingException {

        if ((split < 0) || (split > 1))
            throw new IllegalArgumentException("Split argument must be a value greater than zero and smaller than 1");

        if (shuffle)
            source.randomize(new Random(seed));

        if (stratify)
            source.stratify(2);


        int trainSize = (int) Math.round(source.numInstances() * split);
        int testSize = source.numInstances() - trainSize;
        Instances[] result = new Instances[2];

        result[0] = new Instances(source, 0, trainSize);
        result[1] = new Instances(source, trainSize, testSize);

        return result;
    }
}

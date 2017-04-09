import classification.crossValidation.CrossValidation;
import classification.crossValidation.NFoldCV;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.util.DataLoaderHelper.listFiles;

/**
 * Created by Jean Metz.
 */

@RunWith(Parameterized.class)
public class ExpRunnerTest {

    public static final String SEPARATOR = FileSystems.getDefault().getSeparator();
    private static StringBuilder output;
    private final String srcFile;
    private final int nFolds;
    private static String resultFile;


    public ExpRunnerTest(String srcFile,  int nFolds, String resultFile) {
        this.srcFile = srcFile;
        this.nFolds = nFolds;
        this.resultFile = resultFile;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {

        String basePath = "./src/test/resources";
        List<String> paths = new ArrayList<>();
        String resultFile = basePath + FileSystems.getDefault().getSeparator() + "Result.csv";

        Integer nfolds = 10;

        paths.add(basePath + SEPARATOR + "2classes");
        paths.add(basePath + SEPARATOR + "3classes");

        List<Object[]> parameters = new ArrayList<>();
        for(String p : paths) {

            List<String> files = listFiles(p);

            for (String f : files) {
                parameters.add(new Object[]{f, nfolds, resultFile});
            }
        }
        return parameters;
    }

    @BeforeClass
    public static void setup() {
        output = new StringBuilder();
    }

    @AfterClass
    public static void flushResults() {
        output.insert(0, "AreaUnderPRC;AreaUnderROC;WFMeasure;UWMacroFMeasure;UWMicroFMeasure;ErrorRate;PCTCorrect\n");
        System.out.println(output.toString());
    }

    @Test
    public void run() {
        CrossValidation method = new NFoldCV();
        Runner runner = new Runner(this.srcFile, this.nFolds, method);
        System.out.println(runner.toString());
        runner.run();
        System.out.println(runner.resultAsString());
    }

}
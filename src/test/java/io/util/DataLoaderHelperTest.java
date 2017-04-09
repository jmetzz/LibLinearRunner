package io.util;

import com.google.common.base.Function;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Jean Metz.
 */
public class DataLoaderHelperTest {

    private static String RESOURCES_PATH = "src/test/resources";

    public static final String ARFF_EXTENSION = "arff";
    public static final String JAVA_EXTENSION = "java";
    public static final String TXT_EXTENSION = "txt";
    public static final String CLASS_EXTENSION = "class";


    public void loadData() {

    }


    public void shouldFailDueToFileNotFound() {

    }


    public void shouldFailDueToWrongArffFormat() {

    }



    @Test
    public void shouldListFilesWithDefaultExtension() throws IOException {

        assertThat(DataLoaderHelper.listFiles(RESOURCES_PATH))
                .isNotNull()
                .isNotEmpty()
                .contains(toPath("25surf.arff"))
                .hasSize(1);

        List<String> expectedList = toPath(true, RESOURCES_PATH + File.separator + "2classes", "25sift.arff",
                "25surf.arff",
                "50sift.arff",
                "50surf.arff",
                "100sift.arff",
                "100surf.arff");

        assertThat(DataLoaderHelper.listFiles(toPath("2classes")))
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .containsAll(expectedList)
                .containsOnlyElementsOf(expectedList);
    }


    @Test
    public void shouldListFilesSpecific() throws IOException {

        List<String> fileNameList = DataLoaderHelper.listFiles(RESOURCES_PATH, DataLoaderHelper.buildFilePatterns(ARFF_EXTENSION));
        assertThat(fileNameList)
                .isNotNull()
                .isNotEmpty()
                .contains(toPath("25surf.arff"))
                .hasSize(1);

        fileNameList.clear();
        fileNameList = DataLoaderHelper.listFiles(RESOURCES_PATH, DataLoaderHelper.buildFilePatterns(ARFF_EXTENSION, TXT_EXTENSION));
        assertThat(fileNameList)
                .isNotNull()
                .isNotEmpty()
                .contains(toPath("25surf.arff"))
                .contains(toPath("DatasetInfo.txt"))
                .hasSize(2);

        fileNameList.clear();


        List<String> expectedList = toPath(true, RESOURCES_PATH + File.separator + "2classes", "25sift.arff",
                "25surf.arff",
                "50sift.arff",
                "50surf.arff",
                "100sift.arff",
                "100surf.arff");

        assertThat(DataLoaderHelper.listFiles(toPath("2classes"), DataLoaderHelper.buildFilePatterns(ARFF_EXTENSION)))
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .containsAll(expectedList)
                .containsOnlyElementsOf(expectedList);

        assertThat(DataLoaderHelper.listFiles(toPath("2classes"), DataLoaderHelper.buildFilePatterns(TXT_EXTENSION)))
                .isNotNull()
                .isEmpty();
    }


    @Test
    public void test() {
        String[] exArray = {JAVA_EXTENSION, CLASS_EXTENSION, TXT_EXTENSION, ARFF_EXTENSION};

        String expected = "glob:**.{java,class,txt,arff}";
        assertThat(DataLoaderHelper.buildFilePatterns(exArray)).isEqualTo(expected);

        List<String> exList = new ArrayList<>();
        exList.add(JAVA_EXTENSION);
        exList.add(CLASS_EXTENSION);
        exList.add(TXT_EXTENSION);
        exList.add(ARFF_EXTENSION);

        assertThat(DataLoaderHelper.buildFilePatterns(exList)).isEqualTo(expected);
    }

    @Test
    public void tempTest(){
        System.out.println(toPath(false, "25sift.arff", "25surf.arff","50sift.arff"));

        System.out.println(toPath(true, RESOURCES_PATH + File.separator + "2classes", "25sift.arff", "25surf.arff","50sift.arff"));

    }

    private List<String> toPath(boolean firstIsRoot, String ... strings){

        int n = firstIsRoot? 1 : 0;
        final String finalRoot = firstIsRoot? strings[0] : RESOURCES_PATH;

        return Arrays.asList(strings).stream()
                .skip(n)
                .map(s -> toPath(finalRoot, s))
                .collect(Collectors.toList());
    }

    private String toPath(String s) {
        return toPath(RESOURCES_PATH, s);
    }

    private String toPath(String root, String sub) {
        return Paths.get(root, sub).toString();
    }


}
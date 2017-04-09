package io.util;

import exceptions.ArffLoadException;
import exceptions.EvaluationException;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jean Metz.
 */
public class DataLoaderHelper {


    public static final String GLOB_ARFF_PATTERN = "glob:**.{arff}";

    public static Instances loadData(String file) throws ArffLoadException {
        ConverterUtils.DataSource source = null;
        try {
            source = new ConverterUtils.DataSource(file);
        } catch (Exception e) {
            throw new ArffLoadException("WEKA DataSource loader error", e);
        }

        Instances data = null;
        try {
            data = source.getDataSet();
        } catch (Exception e) {
            throw new ArffLoadException("WEKA DataSource access data error", e);
        }
        // setting class attribute if the data format does not provide this information
        // For example, the XRFF format saves the class attribute information as well
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);

        return data;
    }


    public static List<String> listFiles(String directory) throws IOException {
        return listFiles(directory, GLOB_ARFF_PATTERN);
    }

    public static List<String> listFiles(String directory, String pattern) throws IOException {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);

                if (matcher.matches(path))
                    fileNames.add(path.toString());
            }
        }
        return fileNames;
    }

    public static String buildFilePatterns(final String... extensions){
        return buildFilePatterns(Arrays.asList(extensions));
    }

    public static String buildFilePatterns(final List<String> extList){
        String pattern = extList.stream()
                .map(e -> e.toString())
                .collect(Collectors.joining(","));


        return "glob:**.{" + pattern.replaceAll(" ", "") + "}";
    }
}

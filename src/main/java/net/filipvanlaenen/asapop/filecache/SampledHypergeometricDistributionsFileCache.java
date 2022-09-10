package net.filipvanlaenen.asapop.filecache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import net.filipvanlaenen.asapop.analysis.SampledHypergeometricDistribution;
import net.filipvanlaenen.asapop.yaml.SampledHypergeometricDistributionData;
import net.filipvanlaenen.asapop.yaml.SampledHypergeometricDistributionDataBuilder;

/**
 * Class providing a cache for sampled hypergeometric distributions backed by the file system.
 */
public final class SampledHypergeometricDistributionsFileCache {
    /**
     * Toggle to control whether the class should be turned on, default being false.
     */
    private static boolean toggle = false;
    /**
     * Builder to convert a sampled hypergeometric distribution to a data object.
     */
    private static final SampledHypergeometricDistributionDataBuilder DATA_BUILDER =
            new SampledHypergeometricDistributionDataBuilder();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledHypergeometricDistributionsFileCache() {
    }

    /**
     * Calculates the path for a sampled hypergeometric distribution.
     *
     * @param value          The number of responses in the opinion poll.
     * @param sampleSize     The sample size of the opinion poll.
     * @param populationSize The population size.
     * @return The path for a sampled hypergeometric distribution.
     */
    static Path calculatePath(final Long value, final Long sampleSize, final Long populationSize) {
        return Paths.get(System.getProperty("user.home"), ".asapop", "sampled-hypergeometric-distributions",
                populationSize.toString(), sampleSize.toString(), value.toString() + ".yaml");
    }

    /**
     * Toggles this class to be turned on.
     */
    public static void toggleOn() {
        toggle = true;
    }

    /**
     * Writes a sampled hypergeometric distribution to the file system.
     *
     * @param value          The number of responses in the opinion poll.
     * @param sampleSize     The sample size of the opinion poll.
     * @param populationSize The population size.
     * @param pmf            The sampled hypergeometric distribution.
     */
    public static void write(final Long value, final Long sampleSize, final Long populationSize,
            final SampledHypergeometricDistribution pmf) {
        if (toggle) {
            Path filePath = calculatePath(value, sampleSize, populationSize);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            Path directoryPath = filePath.getParent();
            try {
                Files.createDirectories(directoryPath);
                objectMapper.writeValue(filePath.toFile(), DATA_BUILDER.toData(pmf));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads a sampled hypergeometric distribution from the file system.
     *
     * @param value          The number of responses in the opinion poll.
     * @param sampleSize     The sample size of the opinion poll.
     * @param populationSize The population size.
     * @return The sampled hypergeometric distribution.
     */
    public static SampledHypergeometricDistribution read(final Long value, final Long sampleSize,
            final Long populationSize) {
        if (!toggle) {
            return null;
        }
        File file = calculatePath(value, sampleSize, populationSize).toFile();
        if (!file.exists()) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        try {
            SampledHypergeometricDistributionData data =
                    objectMapper.readValue(file, SampledHypergeometricDistributionData.class);
            return DATA_BUILDER.fromData(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

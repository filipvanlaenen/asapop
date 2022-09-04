package net.filipvanlaenen.asapop.filecache;

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

public class SampledHypergeometricDistributionsFileCache {
    public static boolean toggle = false;
    private static final SampledHypergeometricDistributionDataBuilder DATA_BUILDER =
            new SampledHypergeometricDistributionDataBuilder();

    /**
     * Private constructor to prevent the instantiation of this utility class.
     */
    private SampledHypergeometricDistributionsFileCache() {
    }

    public static void write(final Long value, final Long sampleSize, final Long populationSize,
            final SampledHypergeometricDistribution pmf) {
        if (toggle) {
            SampledHypergeometricDistributionData data = DATA_BUILDER.toData(pmf);
            Path filePath =
                    Paths.get(System.getProperty("user.home"), ".asapop", "sampled-hypergeometric-distributions",
                            populationSize.toString(), sampleSize.toString(), value.toString() + ".yaml");
            Path directoryPath = filePath.getParent();
            try {
                Files.createDirectories(directoryPath);
                ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                objectMapper.setSerializationInclusion(Include.NON_NULL);
                objectMapper.writeValue(filePath.toFile(), data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package net.filipvanlaenen.asapop.filecache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SampledHypergeometricDistributionsFileCache</code> class.
 */
public class SampledHypergeometricDistributionsFileCacheTest {
    /**
     * Test verifying that the path for a hypergeometric distribution is calculated correctly.
     */
    @Test
    public void pathForAHypergeometricDistributionShouldBeCalculatedCorrectly() {
        Path path = Paths.get(System.getProperty("user.home"), ".asapop", "sampled-hypergeometric-distributions", "2",
                "1", "0.yaml");
        assertEquals(path, SampledHypergeometricDistributionsFileCache.calculatePath(0L, 1L, 2L));
    }
}

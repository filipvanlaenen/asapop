package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>CsvConfiguration</code> class.
 */
public class CsvConfigurationTest {
    /**
     * Verifies that the getter method <code>getElectoralListKeys</code> is wired correctly to the setter method
     * <code>setElectoralListKeys</code>.
     */
    @Test
    public void getElectoralListKeysShouldBeWiredCorrectlyToSetElectoralListKeys() {
        CsvConfiguration csvConfiguration = new CsvConfiguration();
        csvConfiguration.setElectoralListKeys(List.of("A", "B"));
        assertEquals(List.of("A", "B"), csvConfiguration.getElectoralListKeys());
    }
}

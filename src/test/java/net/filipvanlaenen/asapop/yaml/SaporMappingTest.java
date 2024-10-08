package net.filipvanlaenen.asapop.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SaporMapping</code> class.
 */
public class SaporMappingTest {
    /**
     * Verifies that the getter method <code>getAdditiveMapping</code> is wired correctly to the setter method
     * <code>setAdditiveMapping</code>.
     */
    @Test
    public void getAdditiveMappingShouldBeWiredCorrectlyToSetAdditiveMapping() {
        SaporMapping saporMapping = new SaporMapping();
        AdditiveSaporMapping additiveMapping = new AdditiveSaporMapping();
        saporMapping.setAdditiveMapping(additiveMapping);
        assertEquals(additiveMapping, saporMapping.getAdditiveMapping());
    }

    /**
     * Verifies that the getter method <code>getDirectMapping</code> is wired correctly to the setter method
     * <code>setDirectMapping</code>.
     */
    @Test
    public void getDirectMappingShouldBeWiredCorrectlyToSetDirectMapping() {
        SaporMapping saporMapping = new SaporMapping();
        DirectSaporMapping directMapping = new DirectSaporMapping();
        saporMapping.setDirectMapping(directMapping);
        assertEquals(directMapping, saporMapping.getDirectMapping());
    }

    /**
     * Verifies that the getter method <code>getEssentialEntriesMapping</code> is wired correctly to the setter method
     * <code>setEssentialEntriesMapping</code>.
     */
    @Test
    public void getEssentialEntriesMappingShouldBeWiredCorrectlyToSetEssentialEntriesMapping() {
        SaporMapping saporMapping = new SaporMapping();
        EssentialEntriesSaporMapping essentialEntriesMapping = new EssentialEntriesSaporMapping();
        saporMapping.setEssentialEntriesMapping(essentialEntriesMapping);
        assertEquals(essentialEntriesMapping, saporMapping.getEssentialEntriesMapping());
    }

    /**
     * Verifies that the getter method <code>getSplittingMapping</code> is wired correctly to the setter method
     * <code>setSplittingMapping</code>.
     */
    @Test
    public void getSplittingMappingShouldBeWiredCorrectlyToSetSplittingMapping() {
        SaporMapping saporMapping = new SaporMapping();
        SplittingSaporMapping splittingMapping = new SplittingSaporMapping();
        saporMapping.setSplittingMapping(splittingMapping);
        assertEquals(splittingMapping, saporMapping.getSplittingMapping());
    }

    /**
     * Verifies that the getter method <code>getAdditiveSplittingMapping</code> is wired correctly to the setter method
     * <code>setAdditiveSplittingMapping</code>.
     */
    @Test
    public void getAdditiveSplittingMappingShouldBeWiredCorrectlyToSetAdditiveSplittingMapping() {
        SaporMapping saporMapping = new SaporMapping();
        AdditiveSplittingSaporMapping additiveSplittingMapping = new AdditiveSplittingSaporMapping();
        saporMapping.setAdditiveSplittingMapping(additiveSplittingMapping);
        assertEquals(additiveSplittingMapping, saporMapping.getAdditiveSplittingMapping());
    }

    /**
     * Verifies that the getter method <code>getStartDate</code> is wired correctly to the setter method
     * <code>setStartDate</code>.
     */
    @Test
    public void getStartDateShouldBeWiredCorrectlyToSetStartDate() {
        SaporMapping saporMapping = new SaporMapping();
        saporMapping.setStartDate("2023-07-03");
        assertEquals("2023-07-03", saporMapping.getStartDate());
    }

    /**
     * Verifies that the getter method <code>getEndDate</code> is wired correctly to the setter method
     * <code>setEndDate</code>.
     */
    @Test
    public void getEndDateShouldBeWiredCorrectlyToSetEndDate() {
        SaporMapping saporMapping = new SaporMapping();
        saporMapping.setEndDate("2023-07-03");
        assertEquals("2023-07-03", saporMapping.getEndDate());
    }
}

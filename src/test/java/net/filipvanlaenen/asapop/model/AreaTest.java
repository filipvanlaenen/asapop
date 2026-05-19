package net.filipvanlaenen.asapop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Unit tests on the class <code>Area</code>.
 */
public class AreaTest {
    /**
     * An elected body for the test instance.
     */
    private static final ElectedBody ELECTED_BODY =
            new ElectedBody("B", Map.empty(), Map.empty(), OrderedCollection.empty(), false);
    /**
     * An elected office for the test instance.
     */
    private static final ElectedOffice ELECTED_OFFICE =
            new ElectedOffice("O", Map.empty(), Map.empty(), OrderedCollection.empty(), false);
    /**
     * A test instance for the unit tests.
     */
    private static final Area TEST_INSTANCE = new Area("A", Collection.of(ELECTED_BODY), Collection.of(ELECTED_OFFICE));

    /**
     * Verifies that the <code>getId</code> method returns the ID.
     */
    @Test
    public void getIdShouldReturnTheId() {
        assertEquals("A", TEST_INSTANCE.getId());
    }

    /**
     * Verifies that <code>get</code> returns the area with the corresponding ID.
     */
    @Test
    public void getShouldReturnAddedAreaWithId() {
        Area instance = new Area("id", Collection.empty(), Collection.empty());
        Area.add(instance);
        assertEquals(instance, Area.get("id"));
    }

    /**
     * Verifies that <code>getAll</code> returns a collection containing an added area.
     */
    @Test
    public void getAllShouldReturnACollectionContainingAnAddedArea() {
        Area instance = new Area("ad", Collection.empty(), Collection.empty());
        Area.add(instance);
        assertTrue(Area.getAll().contains(instance));
    }

    /**
     * Verifies that <code>getElectedBodies</code> returns the elected bodies.
     */
    @Test
    public void getElectedBodiesShouldReturnTheElectedBodies() {
        assertTrue(TEST_INSTANCE.getElectedBodies().containsSame(Collection.of(ELECTED_BODY)));
    }

    /**
     * Verifies that <code>getElectedOffices</code> returns the elected offices.
     */
    @Test
    public void getElectedOfficesShouldReturnTheElectedOffices() {
        assertTrue(TEST_INSTANCE.getElectedOffices().containsSame(Collection.of(ELECTED_OFFICE)));
    }
}

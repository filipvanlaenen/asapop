package net.filipvanlaenen.asapop.exporter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Unit tests on the <code>SaporDirectory</code> class.
 */
public class SaporDirectoryTest {
    /**
     * Verifies that the map is empty by default.
     */
    @Test
    public void mapShouldBeEmptyByDefault() {
        assertTrue(new SaporDirectory().asMap().isEmpty());
    }

    /**
     * Verifies that when a SAPOR file is added with a path, it is included in the map.
     */
    @Test
    public void addedSaporFileShouldBeInTheMap() {
        Path path = Paths.get("foo.poll");
        SaporDirectory saporDirectory = new SaporDirectory();
        saporDirectory.put(path, "Foo");
        assertEquals(Map.of(path, "Foo"), saporDirectory.asMap());
    }
}

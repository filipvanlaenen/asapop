package net.filipvanlaenen.asapop.exporter;

import java.nio.file.Path;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableMap;
import net.filipvanlaenen.kolektoj.hash.HashMap;

/**
 * Class modeling a directory with SAPOR files.
 */
public class SaporDirectory {
    /**
     * A map with the SAPOR file paths and their content.
     */
    private final ModifiableMap<Path, String> map = ModifiableMap.empty();

    /**
     * Returns an unmodifiable map with the SAPOR file paths and their content.
     *
     * @return An unmodifiable map with the SAPOR file paths and their content.
     */
    public Map<Path, String> asMap() {
        return new HashMap<Path, String>(map);
    }

    /**
     * Adds a SAPOR file with its path and content.
     *
     * @param saporFilePath The path for the SAPOR file.
     * @param saporContent  The content of the SAPOR file.
     */
    void put(final Path saporFilePath, final String saporContent) {
        map.put(saporFilePath, saporContent);
    }
}

package net.filipvanlaenen.asapop.exporter;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SaporDirectory {
    private final Set<ExporterWarning> warnings = new HashSet<ExporterWarning>();
    private final Map<Path, String> map = new HashMap<Path, String>();

    void addWarnings(Set<ExporterWarning> saporWarnings) {
        warnings.addAll(saporWarnings);
    }

    public Map<Path, String> asMap() {
        return Collections.unmodifiableMap(map);
    }

    public Set<ExporterWarning> getWarnings() {
        return Collections.unmodifiableSet(warnings);
    }

    void put(Path saporFilePath, String saporContent) {
        map.put(saporFilePath, saporContent);
    }
}

package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;

class MissingSaporMappingWarning extends ExporterWarning {
    private final Set<ElectoralList> missingElectoralList;

    MissingSaporMappingWarning(Set<ElectoralList> missingElectoralList) {
        this.missingElectoralList = missingElectoralList;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MissingSaporMappingWarning) {
            MissingSaporMappingWarning otherWarning = (MissingSaporMappingWarning) obj;
            return otherWarning.missingElectoralList.equals(missingElectoralList);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(missingElectoralList);
    }

    @Override
    public String toString() {
        List<String> keys = new ArrayList<String>(ElectoralList.getKeys(missingElectoralList));
        Collections.sort(keys);
        return "SAPOR mapping missing for “" + String.join("+", keys) + "”.";
    }
}

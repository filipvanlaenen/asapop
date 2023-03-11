package net.filipvanlaenen.asapop.exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.filipvanlaenen.asapop.model.ElectoralList;

/**
 * Class modeling a warning for a missing combination of electoral lists in the SAPOR mapping.
 */
class MissingSaporMappingWarning extends ExporterWarning {
    /**
     * The electoral list combination missing.
     */
    private final Set<ElectoralList> missingElectoralListCombination;

    /**
     * Constructor taking the missing electoral list combination as its parameter.
     *
     * @param missingElectoralListCombination The combination of electoral lists missing in the SAPOR mapping.
     */
    MissingSaporMappingWarning(final Set<ElectoralList> missingElectoralListCombination) {
        this.missingElectoralListCombination = missingElectoralListCombination;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MissingSaporMappingWarning) {
            MissingSaporMappingWarning otherWarning = (MissingSaporMappingWarning) obj;
            return otherWarning.missingElectoralListCombination.equals(missingElectoralListCombination);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(missingElectoralListCombination);
    }

    @Override
    public String toString() {
        List<String> ids = new ArrayList<String>(ElectoralList.getIds(missingElectoralListCombination));
        Collections.sort(ids);
        return "SAPOR mapping missing for “" + String.join("+", ids) + "”.";
    }
}

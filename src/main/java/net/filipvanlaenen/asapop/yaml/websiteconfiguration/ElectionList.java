package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.filipvanlaenen.asapop.yaml.IntegerToStringMapDeserializer;
import net.filipvanlaenen.kolektoj.Map;

/**
 * Class representing a list with elections for the YAML file containing the website configuration.
 */
@Deprecated
public class ElectionList {
    /**
     * A map holding the election dates.
     */
    @JsonDeserialize(using = IntegerToStringMapDeserializer.class)
    private Map<Integer, String> dates;

    /**
     * Returns the map with the election dates.
     *
     * @return The map with the election dates.
     */
    public Map<Integer, String> getDates() {
        return dates;
    }

    /**
     * Sets the map with the election dates.
     *
     * @param dates The map with the election dates.
     */
    public void setDates(final Map<Integer, String> dates) {
        this.dates = dates;
    }
}

package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.filipvanlaenen.asapop.yaml.OrderedStringCollectionDeserializer;
import net.filipvanlaenen.asapop.yaml.StringToStringMapDeserializer;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;

/**
 * Class representing an elected office in the YAML file containing the website configuration.
 */
public class ElectedOfficeConfiguration {
    /**
     * The election dates for the elected office.
     */
    @JsonDeserialize(using = OrderedStringCollectionDeserializer.class)
    private OrderedCollection<String> electionDates;
    /**
     * The ID.
     */
    private String id;
    /**
     * A map with the proper names of the elected office in the local languages.
     */
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> properNames;
    /**
     * A map with the translated names of the elected office.
     */
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> translatedNames;

    /**
     * Returns the election dates.
     *
     * @return The election dates.
     */
    public OrderedCollection<String> getElectionDates() {
        return electionDates;
    }

    /**
     * Returns the ID.
     *
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the proper names in the local languages.
     *
     * @return The proper names in the local languages.
     */
    public Map<String, String> getProperNames() {
        return properNames;
    }

    /**
     * Returns the translated names.
     *
     * @return The translated names.
     */
    public Map<String, String> getTranslatedNames() {
        return translatedNames;
    }

    /**
     * Sets the election dates.
     *
     * @param electionDates The election dates.
     */
    public void setElectionDates(final OrderedCollection<String> electionDates) {
        this.electionDates = electionDates;
    }

    /**
     * Sets the ID.
     *
     * @param id The ID.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Sets the proper names in the local languages.
     *
     * @param properNames The proper names in the local languages.
     */
    public void setProperNames(final Map<String, String> properNames) {
        this.properNames = properNames;
    }

    /**
     * Sets the translated names.
     *
     * @param translatedNames The translated names.
     */
    public void setTranslatedNames(final Map<String, String> translatedNames) {
        this.translatedNames = translatedNames;
    }
}

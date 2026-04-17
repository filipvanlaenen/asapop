package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.filipvanlaenen.asapop.yaml.StringToStringMapDeserializer;
import net.filipvanlaenen.kolektoj.Map;

/**
 * Class representing an elected body in the YAML file containing the website configuration.
 */
public class ElectedBody {
    /**
     * The election dates for the elected body.
     */
    private String[] electionDates; // TODO: Consider to make this an OrderedCollection
    /**
     * The ID.
     */
    private String id;
    /**
     * A map with the proper names of the elected body in the local languages.
     */
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> properNames;
    /**
     * A map with the translated names of the elected body.
     */
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> translatedNames;

    /**
     * Returns the election dates.
     *
     * @return The election dates.
     */
    public String[] getElectionDates() {
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
    public void setElectionDates(final String[] electionDates) {
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

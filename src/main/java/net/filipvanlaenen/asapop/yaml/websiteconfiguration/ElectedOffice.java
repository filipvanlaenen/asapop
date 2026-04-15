package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.filipvanlaenen.asapop.yaml.StringToStringMapDeserializer;
import net.filipvanlaenen.kolektoj.Map;

public class ElectedOffice {
    // TODO: Consider to make this an OrderedCollection
    private String[] electionDates;
    private String id;
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> properNames;
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> translatedNames;

    public String[] getElectionDates() {
        return electionDates;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getProperNames() {
        return properNames;
    }

    public Map<String, String> getTranslatedNames() {
        return translatedNames;
    }

    public void setElectionDates(final String[] electionDates) {
        this.electionDates = electionDates;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setProperNames(final Map<String, String> properNames) {
        this.properNames = properNames;
    }

    public void setTranslatedNames(final Map<String, String> translatedNames) {
        this.translatedNames = translatedNames;
    }
}

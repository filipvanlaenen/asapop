package net.filipvanlaenen.asapop.yaml.websiteconfiguration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.filipvanlaenen.asapop.yaml.IntegerToStringMapDeserializer;
import net.filipvanlaenen.asapop.yaml.StringToStringMapDeserializer;
import net.filipvanlaenen.kolektoj.Map;

public class ElectedBody {
    @JsonDeserialize(using = IntegerToStringMapDeserializer.class)
    private Map<Integer, String> electionDates;
    private String id;
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> properNames;
    @JsonDeserialize(using = StringToStringMapDeserializer.class)
    private Map<String, String> translatedNames;

    public Map<Integer, String> getElectionDates() {
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

    public void setElectionDates(final Map<Integer, String> electionDates) {
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

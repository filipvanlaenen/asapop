package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;

import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedValueCollection;

public class ElectedBody {
    private final OrderedCollection<OrderedValueCollection<ElectionDate>> elections;
    private final Map<String, String> properNames;
    private final Map<Language, String> translatedNames;

    public ElectedBody(final Map<String, String> properNames, final Map<Language, String> translatedNames,
            final OrderedCollection<OrderedValueCollection<ElectionDate>> elections) {
        this.properNames = Map.of(properNames);
        this.translatedNames = Map.of(translatedNames);
        this.elections = OrderedCollection.of(elections);
    }

    public Collection<String> getAllProperNames() {
        return properNames.getValues();
    }

    public Collection<String> getLanguagesOfProperNames() {
        return properNames.getKeys();
    }

    public String getName(final Language language) {
        String id = language.getId();
        if (properNames.containsKey(id)) {
            return properNames.get(id);
        }
        if (translatedNames.containsKey(language)) {
            return translatedNames.get(language);
        }
        return null;
    }

    public ElectionDate getNextElectionDate(final LocalDate now) {
        for (OrderedValueCollection<ElectionDate> electionDates : elections) {
            for (ElectionDate electionDate : electionDates) {
                if (!electionDate.getEndDate().isBefore(now)) {
                    return electionDate;
                }
            }
        }
        return null;
    }
}

package net.filipvanlaenen.asapop.model;

import java.time.LocalDate;
import java.util.Comparator;

import net.filipvanlaenen.asapop.website.Language;
import net.filipvanlaenen.kolektoj.Collection;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.OrderedCollection;
import net.filipvanlaenen.kolektoj.OrderedValueCollection;
import net.filipvanlaenen.kolektoj.SortedCollection;

/**
 * Class modeling an election public institution.
 */
abstract class ElectedPublicInstitution {
    /**
     * Whether the elected body is defunct. If an elected body is defunct, it doesn't have any future elections.
     */
    private final boolean defunct;
    /**
     * The elected body's elections.
     */
    private final OrderedCollection<OrderedValueCollection<ElectionDate>> elections;
    /**
     * The ID.
     */
    private final String id;
    /**
     * The proper names for the elected body.
     */
    private final Map<String, String> properNames;
    /**
     * The translations of the proper names into a fixed set of languages.
     */
    private final Map<Language, String> translatedNames;

    /**
     * Constructor creating a new elected public institution.
     *
     * @param id              The ID.
     * @param properNames     The proper names.
     * @param translatedNames The translated names.
     * @param elections       The elections.
     * @param defunct         Whether the elected body is defunct.
     */
    ElectedPublicInstitution(final String id, final Map<String, String> properNames,
            final Map<Language, String> translatedNames,
            final OrderedCollection<OrderedValueCollection<ElectionDate>> elections, final boolean defunct) {
        this.id = id;
        this.properNames = Map.of(properNames);
        this.translatedNames = Map.of(translatedNames);
        this.elections = OrderedCollection.of(elections);
        this.defunct = defunct;
    }

    /**
     * Returns all proper names as a collection of strings.
     *
     * @return All proper names as a collection of strings.
     */
    public Collection<String> getAllProperNames() {
        return properNames.getValues();
    }

    /**
     * Returns a concatenation of all proper names, with a middle dot (<code>·</code>) between them, sorted in
     * alphabetical order.
     *
     * @return A concatenation of all proper names.
     */
    public String getAllProperNamesConcatenated() {
        return String.join(" · ", SortedCollection.of(Comparator.naturalOrder(), getAllProperNames()));
    }

    /**
     * Returns true if the elected body is defunct.
     *
     * @return True if the elected body is defunct.
     */
    public boolean getDefunct() {
        return defunct;
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
     * Returns the language codes for the proper names as a collection of strings.
     *
     * @return The language codes for the proper names as a collection of strings.
     */
    public Collection<String> getLanguagesOfProperNames() {
        return properNames.getKeys();
    }

    /**
     * Returns the name of the elected body for a given language code. If the language code matches the language code of
     * a proper name, then that proper name is returned. If it matches the language code of a translation, then the
     * translation is returned. Otherwise, it returns <code>null</code>.
     *
     * @param language The language code.
     * @return The proper name or the translation for the provided language code, or <code>null</code>.
     */
    public String getName(final Language language) {
        String languageId = language.getId();
        if (properNames.containsKey(languageId)) {
            return properNames.get(languageId);
        }
        if (translatedNames.containsKey(language)) {
            return translatedNames.get(language);
        }
        return null;
    }

    /**
     * Returns the first next election date relative to a provided date (usually “now”), or <code>null</code> if there
     * isn't any.
     *
     * @param now The date to compare the election dates to.
     * @return The first next election date relative to a provided date, or <code>null</code> if there isn't any.
     */
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

package net.filipvanlaenen.asapop.website;

import java.util.Comparator;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.ModifiableSortedMap;
import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap;

/**
 * Class holding a dictionary for the internationalization.
 */
public class Internationalization {
    /**
     * A modifiable sorted map holding the dictionary.
     */
    private ModifiableSortedMap<String, Translations> translations =
            new ModifiableSortedTreeMap<String, Translations>(Comparator.naturalOrder());

    /**
     * Default constructor.
     */
    Internationalization() {
    }

    /**
     * Constructor taking terms as its parameters.
     *
     * @param terms The terms with translations.
     */
    public Internationalization(final Terms terms) {
        for (Term term : terms.getTerms()) {
            String key = term.getKey();
            translations.add(key, new Translations(key, term.getTranslations()));
        }
    }

    /**
     * Adds translations for a key.
     *
     * @param key             The key.
     * @param newTranslations A map with the translations.
     */
    public void addTranslations(final String key, final Map<String, String> newTranslations) {
        translations.add(key, new Translations(key, newTranslations));
    }

    /**
     * Returns whether the key is present.
     *
     * @param key The key.
     * @return True if the map with the translations contains the key.
     */
    public boolean containsKey(final String key) {
        return translations.containsKey(key);
    }

    /**
     * Returns the translation for a key and a language.
     *
     * @param key      The key.
     * @param language The language.
     * @return Returns the translation for the key in the language.
     */
    public String getTranslation(final String key, final Language language) {
        if (translations.containsKey(key)) {
            return translations.get(key).getTranslation(language);
        }
        return null;
    }

    /**
     * Returns all the translations as a sorted array. The array is sorted by the keys.
     *
     * @return A sorted array with all the translations.
     */
    public Translations[] getTranslations() {
        return translations.getValues().toArray(new Translations[translations.size()]);
    }

    /**
     * Returns all the translations for a key.
     *
     * @param key The key.
     * @return All the translations for the key.
     */
    public Translations getTranslations(final String key) {
        return translations.get(key);
    }
}

package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;

/**
 * A class representing the translations for a key.
 */
class Translations {
    /**
     * The key.
     */
    private final String key;
    /**
     * A map with the translations.
     */
    private final Map<Language, String> translations;

    /**
     * Constructor taking the key and a map with the translations as its parameters.
     *
     * @param key             The key.
     * @param newTranslations A map with the translations for the key.
     */
    Translations(final String key, final Map<String, String> newTranslations) {
        this.key = key;
        Entry<Language, String>[] entries = new Entry[newTranslations.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : newTranslations) {
            entries[i++] = new Entry<Language, String>(Language.parse(entry.key()), entry.value());
        }
        translations = Map.of(entries);
    }

    /**
     * Returns whether there's a translation present for the language.
     *
     * @param language The language.
     * @return True if there's a translation present for the language.
     */
    boolean containsLanguage(final Language language) {
        return translations.containsKey(language);
    }

    /**
     * Returns the key.
     *
     * @return The key.
     */
    String getKey() {
        return key;
    }

    /**
     * Returns the translation for a language.
     *
     * @param language The language.
     * @return The translation for a language.
     */
    String getTranslation(final Language language) {
        return translations.get(language, null);
    }
}

package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.kolektoj.Map;
import net.filipvanlaenen.kolektoj.Map.Entry;

class Translations {
    private final String key;
    private final Map<Language, String> translations;

    Translations(String key, java.util.Map<String, String> map) {
        this.key = key;
        Entry<Language, String>[] entries = new Entry[map.size()];
        int i = 0;
        for (java.util.Map.Entry<String, String> entry : map.entrySet()) {
            entries[i++] = new Entry<Language, String>(Language.parse(entry.getKey()), entry.getValue());
        }
        translations = Map.of(entries);
    }

    boolean containsLanguage(Language language) {
        return translations.containsKey(language);
    }

    String getKey() {
        return key;
    }

    String getTranslation(Language language) {
        return translations.get(language);
    }
}

package net.filipvanlaenen.asapop.website;

import java.util.Comparator;
import java.util.Map;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;
import net.filipvanlaenen.kolektoj.ModifiableSortedMap;
import net.filipvanlaenen.kolektoj.sortedtree.ModifiableSortedTreeMap;

public class Internationalization {
    private ModifiableSortedMap<String, Translations> translations =
            new ModifiableSortedTreeMap<String, Translations>(Comparator.naturalOrder());

    Internationalization() {
    }

    public Internationalization(final Terms terms) {
        for (Term term : terms.getTerms()) {
            String key = term.getKey();
            translations.add(key, new Translations(key, term.getTranslations()));
        }
    }

    public void addTranslations(String key, Map<String, String> newTranslations) {
        translations.add(key, new Translations(key, newTranslations));
    }

    public boolean containsKey(String key) {
        return translations.containsKey(key);
    }

    public String getTranslation(String key, Language language) {
        if (translations.containsKey(key)) {
            return translations.get(key).getTranslation(language);
        }
        return null;
    }

    public Translations[] getTranslations() {
        return translations.getValues().toArray(new Translations[translations.size()]);
    }

    public Translations getTranslations(String key) {
        return translations.get(key);
    }
}

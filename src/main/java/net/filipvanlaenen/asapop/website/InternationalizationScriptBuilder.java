package net.filipvanlaenen.asapop.website;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Class building the internationalization script.
 */
class InternationalizationScriptBuilder {
    /**
     * The internationalization dictionary.
     */
    private final Internationalization internationalization;

    /**
     * Constructor using the terms as its parameter.
     *
     * @param terms The terms.
     */
    InternationalizationScriptBuilder(final Internationalization internationalization) {
        this.internationalization = internationalization;
    }

    /**
     * Builds the internationalization script.
     *
     * @return The content of the internationalization script.
     */
    String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(buildInitializeLanguage());
        sb.append("\n");
        sb.append(buildLoadLanguage());
        sb.append("\n");
        sb.append(buildUpdateLanguage());
        return sb.toString();
    }

    /**
     * Builds the <code>initializeLanguage</code> function.
     *
     * @return The <code>initializeLanguage</code> function.
     */
    private String buildInitializeLanguage() {
        StringBuilder sb = new StringBuilder();
        sb.append("function initializeLanguage() {\n");
        sb.append("  var language = localStorage.getItem('asapop-language');\n");
        sb.append("  if (language == null) {\n");
        sb.append("    language = 'en';\n");
        sb.append("  }\n");
        sb.append("  var element = document.getElementById('language-selector');\n");
        sb.append("  element.value = language;\n");
        sb.append("  updateLanguage(language);\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Builds the <code>loadLanguage</code> function.
     *
     * @return The <code>loadLanguage</code> function.
     */
    private String buildLoadLanguage() {
        StringBuilder sb = new StringBuilder();
        sb.append("function loadLanguage() {\n");
        sb.append("  var language = document.getElementById(\"language-selector\").value;\n");
        sb.append("  localStorage.setItem('asapop-language', language);\n");
        sb.append("  updateLanguage(language);\n");
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Builds the <code>updateLanguage</code> function.
     *
     * @return The <code>updateLanguage</code> function.
     */
    private String buildUpdateLanguage() {
        StringBuilder sb = new StringBuilder();
        sb.append("function updateLanguage(language) {\n");
        sb.append("  switch (language) {\n");
        List<Language> sortedLanguages = Arrays.asList(Language.values());
        sortedLanguages.sort(new Comparator<Language>() {
            @Override
            public int compare(final Language l1, final Language l2) {
                return l1.getId().compareTo(l2.getId());
            }
        });
        Translations[] translations = internationalization.getTranslations();
        for (Language language : sortedLanguages) {
            sb.append("    case \"" + language.getId() + "\":\n");
            for (Translations translation : translations) {
                sb.append("      $('.");
                sb.append(translation.getKey());
                sb.append("').text(\"");
                if (translation.containsLanguage(language)) {
                    sb.append(translation.getTranslation(language));
                } else {
                    sb.append("[");
                    sb.append(translation.getTranslation(Language.ENGLISH));
                    sb.append("]");
                }
                sb.append("\");\n");
            }
            sb.append("      break;\n");
        }
        sb.append("  }\n");
        sb.append("}\n");
        return sb.toString();
    }
}

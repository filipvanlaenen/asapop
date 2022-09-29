package net.filipvanlaenen.asapop.website;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;

/**
 * Class building the internationalization script.
 */
public class InternationalizationScriptBuilder {
    /**
     * The internationalization terms.
     */
    private final Terms terms;

    /**
     * Constructor using the terms as its parameter.
     *
     * @param terms The terms.
     */
    public InternationalizationScriptBuilder(final Terms terms) {
        this.terms = terms;
    }

    /**
     * Builds the internationalization script.
     *
     * @return The content of the internationalization script.
     */
    public String build() {
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
        for (Language language : Language.values()) {
            sb.append("    case \"" + language.getId() + "\":\n");
            for (Term term : terms.getTerms()) {
                sb.append("      $('.");
                sb.append(term.getKey());
                sb.append("').text(\"");
                if (term.getTranslations().containsKey(language.getId())) {
                    sb.append(term.getTranslations().get(language.getId()));
                } else {
                    sb.append("[");
                    sb.append(term.getTranslations().get(Language.English.getId()));
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
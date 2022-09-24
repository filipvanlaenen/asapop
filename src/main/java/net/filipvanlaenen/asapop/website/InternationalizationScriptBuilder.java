package net.filipvanlaenen.asapop.website;

/**
 * Class building the internationalization script.
 */
public class InternationalizationScriptBuilder {
    /**
     * Builds the internationalization script.
     *
     * @return The content of the internationalization script.
     */
    public String build() {
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
        sb.append("\n");
        sb.append("function loadLanguage() {\n");
        sb.append("  var language = document.getElementById(\"language-selector\").value;\n");
        sb.append("  localStorage.setItem('asapop-language', language);\n");
        sb.append("  updateLanguage(language);\n");
        sb.append("}\n");
        sb.append("\n");
        sb.append("function updateLanguage(language) {\n");
        sb.append("  switch (language) {\n");
        sb.append("    case \"en\":\n");
        sb.append("      $('.language').text(\"Language\");\n");
        sb.append("      break;\n");
        sb.append("  }\n");
        sb.append("}\n");
        return sb.toString();
    }
}

package net.filipvanlaenen.asapop.website;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.filipvanlaenen.asapop.yaml.Term;
import net.filipvanlaenen.asapop.yaml.Terms;

/**
 * Unit tests on the <code>InternationalizationScriptBuilder</code> class.
 */
public class InternationalizationScriptBuilderTest {
    /**
     * Creates a set of terms for the internationalization script builder.
     *
     * @return A set of terms for the internationalization script builder.
     */
    private Terms createTerms() {
        Terms terms = new Terms();
        Term term = new Term();
        term.setKey("language");
        term.setTranslations(Map.of("en", "Language"));
        terms.setTerms(Set.of(term));
        return terms;
    }

    /**
     * Verifies that the script contains a switch statement for English.
     */
    @Test
    public void scriptShouldContainSwitchForEnglishLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("case \"en\":"));
    }

    /**
     * Verifies that the script contains a load statement for the English term for language.
     */
    @Test
    public void scriptShouldContainEnglishTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("$('.language').text(\"Language\");"));
    }

    /**
     * Verifies that the script contains a substitute load statement for the Dutch term for language.
     */
    @Test
    public void scriptShouldContainSubstituteDutchTermForLanguage() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("$('.language').text(\"[Language]\");"));
    }

    /**
     * Verifies that the script contains the <code>initializeLanguage</code> function.
     */
    @Test
    public void scriptShouldContainInitializeLanguageFunction() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("initializeLanguage()"));
    }

    /**
     * Verifies that the script contains the <code>loadLanguage</code> function.
     */
    @Test
    public void scriptShouldContainLoadLanguageFunction() {
        InternationalizationScriptBuilder builder = new InternationalizationScriptBuilder(createTerms());
        assertTrue(builder.build().contains("loadLanguage()"));
    }
}
